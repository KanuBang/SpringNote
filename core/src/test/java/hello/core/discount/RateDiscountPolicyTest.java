package hello.core.discount;

import hello.core.AppConfig;
import hello.core.member.Grade;
import hello.core.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class RateDiscountPolicyTest {
    DiscountPolicy rateDiscountPolicy;

    @BeforeEach
    public void beforeEach(){
        AppConfig appConfig = new AppConfig();
        rateDiscountPolicy = appConfig.discountPolicy();
    }
    @Test
    @DisplayName("vip는 10%로 할인해준다.")
    void vip_o() {
        //given
        Member member = new Member(1L, "Alvarez", Grade.VIP);
        //when
        int discount = rateDiscountPolicy.discount(member, 20000);
        //then
        Assertions.assertThat(discount).isEqualTo((int)(20000*0.1));

    }

    @Test
    @DisplayName("vip가 아니면 할인 해주지 않는다")
    void vip_x() {
        //given
        Member member = new Member(1L, "ligard", Grade.BASIC);

        //when
        int discount = rateDiscountPolicy.discount(member, 20000);

        //then
        Assertions.assertThat(discount).isEqualTo(0);
    }
}