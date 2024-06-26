package book.jpaShopAPI.api;

import book.jpaShopAPI.domain.Address;
import book.jpaShopAPI.domain.Order;
import book.jpaShopAPI.domain.OrderItem;
import book.jpaShopAPI.domain.OrderSearch;
import book.jpaShopAPI.repository.OrderRepository;
import book.jpaShopAPI.repository.order.query.OrderQueryDto;
import book.jpaShopAPI.repository.order.query.OrderQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiRecapController {
    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    /*
    Order 기준
    - Member: ManyToOne
    - Delivery: OneToOne
    - OrderItem: OneToMany

    OrderItems 기준
    - Order: ManyToOne
    - Item: ManyToOne
     */

    /*
    1. 엔티티를 직접 노출

    문제점: 엔티티가 바뀌면 그대로 API 스펙이 변한다. 민감한 정보가 노출될 수 있다.
     */
    @GetMapping("/review/v1/orders")
    public List<Order> ordersV1 () {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());

        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();

            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(oi -> oi.getItem().getName());

        }

        return all;
    }

    /*
    2. DTO를 이용한 노출
    문제점: 지연 로딩으로 인한 N+1 문제
     */
    @GetMapping("/review/v2/orders")
    public List<OrderDto> ordersV2 () {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderDto> all = orders.stream().map(o -> new OrderDto(o)).collect(Collectors.toList());
        return all;
    }

    /*
    3. 페치 조인을 이용하여 즉시 로딩 N+1문제 해결

    문제점: 페이징 불가능

    ToMany(컬렉션 프레임워크)를 페치 조인하면 페이징이 불가능한 이유:
    ToMany 관계를 조인하면 데이터 중복이 많아져 정합성이 깨져버림.
    정합성이 어긋나기 때문에 DB 상에서 페이징 하기 어려워 메모리로 다 끌고와 페이징 시도
    하지만, 말했듯이 ToMany 관계 조인 자체가 데이터를 폭증시키기에 메모리로 모두 끌고오면
    out of memory 발생 가능 -> 결론적으로, ToMany(컬렉션 프레임워크) 페치 조인은 페이징 불가능
     */
    @GetMapping("/review/v3/orders")
    public List<OrderDto> ordersV3 () {
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> all = orders.stream().map(o -> new OrderDto(o)).collect(Collectors.toList());
        return all;
    }

    /*
    3.1: ToOne 관계는 페치 조인으로 끌고옴. 컬렉션으로 default_batch_fetch_size 를 지정해 끌고 온다. -> 1+1로 끝내기
    가정 추천하는 방식
     */
    @GetMapping("/review/v3.1/orders")
    public List<OrderDto> ordersV3_1 () {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<OrderDto> all = orders.stream().map(o -> new OrderDto(o)).collect(Collectors.toList());
        return all;
    }

    // 4. DTO로 조회하기
    // 문제점: N+1
    @GetMapping("/review/v4/orders")
    public List<OrderQueryDto> ordersV4 () {
        return orderQueryRepository.findOrderQueryDtos();
    }

    // 5. DTO로 조회하기 - in 쿼리를 이용해 한 방에 끌고옴 (batch와 비슷한 느낌)
    // 가장 좋음
    @GetMapping("/review/v5/orders")
    public List<OrderQueryDto> ordersV5 () {
        return orderQueryRepository.findAllByDto_optimization();
    }
    @Data
    static class OrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order o) {
            orderId = o.getId();
            name = o.getMember().getName();
            orderDate = o.getOrderDate();
            address = o.getDelivery().getAddress();
            orderItems = o.getOrderItems().stream().map(oi -> new OrderItemDto(oi)).collect(Collectors.toList());

        }
    }

    @Data
    static class OrderItemDto {
        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDto(OrderItem oi) {
            itemName = oi.getItem().getName();
            orderPrice = oi.getOrderPrice();
            count = oi.getCount();
        }
    }
}
