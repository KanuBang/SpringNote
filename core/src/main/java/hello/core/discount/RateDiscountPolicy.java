package hello.core.discount;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.member.Grade;
import hello.core.member.Member;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
//@Qualifier("mainDiscountPolicy") 문자는 타입 체크가 안되서 오타 발생할 경우 에러 발생 -> 어노테이션 만들기
@MainDiscountPolicy
//@Primary
public class RateDiscountPolicy implements DiscountPolicy{

    private final int discountPercent = 10;
    @Override
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP) {
            return (int) (price * 0.1);
        } else {
            return 0;
        }
    }
}
