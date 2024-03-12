package hello.core.order;

import hello.core.Order.Order;
import hello.core.Order.OrderService;
import hello.core.Order.OrderServiceImpl;
import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;

public class OrderServiceTest {
    MemberService memberService = new MemberServiceImpl();
    OrderService orderService = new OrderServiceImpl();
    DiscountPolicy discountPolicy = new FixDiscountPolicy();
    @Test
    void createOrder(){
        //given
        Member member = new Member(2L, "sane", Grade.VIP);
        memberService.join(member);
        //when
        Order order = orderService.createOrder(2L,"adidas hair band", 20000);
        //then
        Assertions.assertThat(order.getDiscountPrice()).isEqualTo(discountPolicy.discount(member,20000));
    }
}
