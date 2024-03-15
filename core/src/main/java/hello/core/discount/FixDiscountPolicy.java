package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@Qualifier("fixDiscountPolicy")
public class FixDiscountPolicy implements DiscountPolicy{

    private final int dicountFixAmount = 1000;

    // VIP면 1000원 할인, 아니면 할인 없음(0원 할인)
    @Override
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP) {
            return dicountFixAmount;
        } else {
            return 0;
        }
    }
}
