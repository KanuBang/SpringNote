package hello.coreReview.discount;

import hello.coreReview.member.Grade;
import hello.coreReview.member.Member;
import hello.coreReview.member.MembreServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class DiscountPolicyTest {
    DiscountPolicy discountPolicy = new RateDiscountPolicy();

    @Test
    void rate_vip_o() {
        Member member = new Member(1L, "chan", Grade.VIP);
        int discount = discountPolicy.discount(member, 10000);
        assertThat(discount).isEqualTo(1000);
    }

    @Test
    void rate_vip_x() {
        Member member = new Member(1L, "chan", Grade.BASIC);
        int discount = discountPolicy.discount(member, 10000);
        assertThat(discount).isEqualTo(0);
    }
}