package book.jpaShopAPI.api;

import book.jpaShopAPI.domain.Address;
import book.jpaShopAPI.domain.Order;
import book.jpaShopAPI.domain.OrderItem;
import book.jpaShopAPI.domain.OrderSearch;
import book.jpaShopAPI.repository.OrderRepository;
import book.jpaShopAPI.repository.order.query.OrderFlatDto;
import book.jpaShopAPI.repository.order.query.OrderItemQueryDto;
import book.jpaShopAPI.repository.order.query.OrderQueryDto;
import book.jpaShopAPI.repository.order.query.OrderQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    /*
     Order을 조회하면서 연관관계를 맺고있는 Member, OrderItems, Delivery도 조회한다.
     어떻게 해야 발생하는 쿼리량을 줄여서 네트워크 비용을 최소화시킬 수 있을까?
     Order:Member = N:1
     Order:Delivery = 1:1
     Order:OrderItems = 1:N
     OrderItems:Item = N:1
    */

    /*
    엔티티를 직접 노출한다면...
    1. 민감한 정보 노출
    2. 트랜잭션 내에서 지연 로딩 필요
    3. 엔티티가 변하면 API 스펙이 변한다. -> 해결방안 DTO 사용
     */
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {

        // Order와 Member를 조인하면서 Order 리스트를 가지고옴.
        List<Order> all = orderRepository.findAllByString(new OrderSearch());

        for (Order order : all) {
            order.getMember().getName(); // Lazy 강제 초기화
            order.getDelivery().getAddress(); // Lazy 강제 초기화
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName()); // Lazy 강제 초기화
        }

        return all;
    }

    /*
    DTO 사용
    1. Order 리스트 사용.
    2. 엔티티를 DTO로 변환

    문제점: 지연 로딩으로 인한 너무 많은 쿼리 조회 수. 이로 인한 네트워크 비용 증가
    SQL 실행 수
    `order` 1번
    `member` , `address` N번(order 조회 수 만큼) `orderItem` N번(order 조회 수 만큼)
    `item` N번(orderItem 조회 수 만큼)
     */
    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderDto> result = orders.stream().map(o -> new OrderDto(o)).collect(toList());
        return result;
    }

    /*
    페치 조인: 지연 로딩으로 인한 문제점 해결. 한 방 쿼리로 해결 가능

    페치 조인: 연관된 엔티티를 한 번의 쿼리로 즉시 로딩합니다. 즉, 조인된 엔티티를 프록시 객체가 아닌 실제 객체로 로딩

    페치 조인과 즉시 로딩의 차이점:
    즉시 로딩은 엔티티를 조회하는 순간 즉시 로딩으로 설정된 엔티티도 모두 로딩되어 복잡하다.
    하지만, 페치 조인은 지연 로딩으로 설정된 연관관계에 있는 엔티티를 선택적으로 즉시 로딩할 수 있다.

    페치 조인의 단점1: ToMany일 경우 중복되는 튜플이 많아 데이터의 정합성이 깨진다.
    해결책: distinct를 사용하여 중복되는 튜플을 제거한다.
    여기서 JPA에서 distinct는 SQL문에서의 distinct와 다르다.
    SQL문의 distinct는 튜플의 내용이 완전히 같아야 중복 처리 되는 거고
    JPA distinct는 어플리케이션 레벨에서 식별자만 같아도 중복 처리가 된다.

    페치 조인의 단점2: 페이징이 불가능하다.
    ToMany 관계를 페치 조인할 경우 데이터가 뻥튀기 되어 중복된 데이터가 급격히 많아진다.
    DB 레벨에서 페이징 하는 것이 의미가 없어 hibernate는 경고를 띄우고 메모리로 모든 데이터를 로드하여 페이징을 시도한다.
    하지만, 말했 듯이, 페치 조인으로 데이터 중복이 많아진 상태에서 메모리에 데이터를 로드 하면 out of memory가 발생할 위험이 생긴다.
    따라서, 즉, ToMany 관계(컬렉션 프레임워크)와의 페치 조인은 페이징이 불가능하다.
     */

    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> result = orders.stream().map(o -> new OrderDto(o)).collect(toList());
        return result;
    }

    /*
    1. ToOne 관계는 페치 조인해도 데이터 중복이 많은 데이터 개수 뻥튀기가 발생하지 않는다. 그래서 페치 조인을 허용한다.
    2. ToMany 관계는 패치 조인시 데이터 중복이 많은 데이터 뻥튀기가 발생한다. 그래서 지연 로딩으로 조회한다.
    3. 2번의 문제는 N+1이다. 즉, 많은 쿼리가 발생한다.
    4. 3번의 문제를 해결하기 위해 default_batch_fetch_size를 이용.
    Hibernate에서 지연 로딩된 엔티티나 컬렉션을 한 번에 몇 개씩 가져올지 설정하는 옵션입니다.
    이 값을 설정하면 여러 개의 엔티티를 한 번에 가져와 성능을 최적화할 수 있습니다.
    5. Order과 ToMany 관계인 OrderItems -> OrderItems 컬렉션을 한 번에 지정된 사이즈 만큼 가지고 온다.
    6. Item도 한번에 가져온다.
     */
    @GetMapping("/api/v31/orders")
    public List<OrderDto> orderV3_page(@RequestParam(value="offest", defaultValue = "0") int offset,
                                       @RequestParam(value="limit", defaultValue = "100") int limit)
    {

        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<OrderDto> result = orders.stream().map(o -> new OrderDto(o)).collect(toList());
        return result;
    }
    /*
    DTO 직접 조회 후 반환-> 굳이 필요 없는 엔티티의 모든 정보를 가져올 필요 없이 내가 원하는 정보만 가져오고 싶음.
    장점: 단건 조회 시 최적.
    단점: ToMany 조회 시 N+1문제
     */
    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4() {
        return orderQueryRepository.findOrderQueryDtos();
    }

    /*
    컬렉션 조회 최적화 - 일대다 관계인 컬렉션은 IN 절을 활용해서 메모리에 미리 조회해서 최적화: V5
    1. 장점: N+1문제 해결, default_fetch_batch_size와 비슷한 느낌.
    2. 단점: 코드가 복잡함
     */
    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5() {
        return orderQueryRepository.findAllByDto_optimization();
    }

    /*
    플랫 데이터 최적화 - JOIN 결과를 그대로 조회 후 애플리케이션에서 원하는 모양으로 직접 변환: V6
    1. 장점: 쿼리 수가 1개
    2. 단점: 페이징 불가능, 쿼리 수가 1개라도 중복 데이터가 많아져 V5와 성능 차이가 거의 없음.
    차라리 V5를 선택하자.
     */
    @GetMapping("/api/v6/orders")
    public List<OrderQueryDto> ordersV6() {
        List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();

        return flats.stream()
                .collect(groupingBy(o -> new OrderQueryDto(o.getOrderId(), o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
                        mapping(o -> new OrderItemQueryDto(o.getOrderId(), o.getItemName(), o.getOrderPrice(), o.getCount()), toList())
                )).entrySet().stream()
                .map(e -> new OrderQueryDto(e.getKey().getOrderId(), e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(),
                        e.getKey().getAddress(), e.getValue())).collect(toList());
    }

    @Data
    static class OrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate; //주문시간 private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName();
            this.orderDate = order.getOrderDate();
            this.address = order.getDelivery().getAddress();
            this.orderItems = order.getOrderItems().stream()
                    .map(o -> new OrderItemDto(o))
                    .collect(toList());
        }
    }

    @Data
    static class OrderItemDto {
        private String itemName;//상품 명
        private int orderPrice; //주문 가격
        private int count; //주문 수량


        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
}
