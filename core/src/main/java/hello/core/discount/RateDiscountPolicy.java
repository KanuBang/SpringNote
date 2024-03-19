package hello.core.discount;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.member.Grade;
import hello.core.member.Member;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@MainDiscountPolicy
public class RateDiscountPolicy implements DiscountPolicy{
    @Override
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP) {
            return (int)(price*0.1);
        } else {
            return 0;
        }
    }
}
