package book.jpaShopAPI.api;

import book.jpaShopAPI.domain.Order;
import book.jpaShopAPI.domain.OrderSearch;
import book.jpaShopAPI.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;

    /*
    v1-엔티티 직접 노출
    Hibernate5Module 모듈 등록, LAZY=null 처리
    양방향 관계 문제 발생 -> @JsonIgnore
     */

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for(Order order : all) {
            order.getOrderItems(); //Lazy 강제 초기화
            order.getMember().getName();  //Lazy 강제 초기화
            order.getDelivery().getAddress(); //Lazy 강제 초기화
        }

        return all;
    }
}
