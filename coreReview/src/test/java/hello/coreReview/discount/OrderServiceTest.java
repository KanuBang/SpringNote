package hello.coreReview.discount;

import hello.coreReview.member.Grade;
import hello.coreReview.member.Member;
import hello.coreReview.member.MemberService;
import hello.coreReview.member.MembreServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {
    MemberService memberService = new MembreServiceImpl();
    OrderService orderService = new OrderServiceImpl();
    @Test
    void createOrder() {
        //given
        long memberId = 1L;
        Member member = new Member(memberId, "chanwu", Grade.VIP);
        memberService.join(member);

        Order order = orderService.createOrder(memberId, "이어폰", 10000);
        assertThat(order.calculatePrice()).isEqualTo(9000);

    }
}