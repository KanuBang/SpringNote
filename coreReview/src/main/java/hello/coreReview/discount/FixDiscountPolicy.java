package hello.coreReview.discount;

import hello.coreReview.member.Grade;
import hello.coreReview.member.Member;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

// @Component
// DiscountPolicy로 조회 시 2개의 결과가 도출되므로 이쪽에는 @Component를 붙이지 않았음.
public class FixDiscountPolicy implements DiscountPolicy{

    private int discountFixAmount = 1000;
    @Override
    public int discount(Member member, int price) {
        if(member.getGrade() == Grade.VIP) {
            return discountFixAmount;
        } else {
            return 0;
        }
    }
}
