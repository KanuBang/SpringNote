package book.jpaShopAPI.api;

import book.jpaShopAPI.domain.Address;
import book.jpaShopAPI.domain.Order;
import book.jpaShopAPI.domain.OrderSearch;
import book.jpaShopAPI.domain.OrderStatus;
import book.jpaShopAPI.repository.OrderRepository;
import book.jpaShopAPI.repository.order.simplequery.OrderSimpleQueryDto;
import book.jpaShopAPI.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    /*
    v1-엔티티 직접 노출
    Hibernate5Module 모듈 등록, LAZY=null 처리
    양방향 관계 문제 발생 -> @JsonIgnore
     */

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for(Order order : all) {
            order.getMember().getName();  //Lazy 강제 초기화
            order.getDelivery().getAddress(); //Lazy 강제 초기화
        }

        return all;
    }


    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> orderV2() {

        //order 조회 1번
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    // 엔티티를 fetch join을 사용해서 쿼리 1번에 조회
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {

        List<Order> orders = orderRepository.findAllWithMemberDelivery();

        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderSimpleQueryRepository.findOrderDtos();
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            // member 지연 로딩 조회 N번
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            // delivery 지연 로딩 조회 N번
            address = order.getDelivery().getAddress();
        }
    }
}
