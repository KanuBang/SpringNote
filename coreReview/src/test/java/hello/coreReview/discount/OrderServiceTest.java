package hello.coreReview.discount;

import hello.coreReview.AppConfig;
import hello.coreReview.member.Grade;
import hello.coreReview.member.Member;
import hello.coreReview.member.MemberService;
import hello.coreReview.member.MemberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderServiceTest {
    MemberService memberService;
    OrderService orderService;

    @BeforeEach
    public void beforeEach() {
        AppConfig appConfig = new AppConfig();
        memberService = appConfig.memberService();
        orderService = appConfig.orderService();
    }


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