package hello.coreReview.discount;

import hello.coreReview.member.Grade;
import hello.coreReview.member.Member;

public class RateDiscountPolicy implements DiscountPolicy{
    private int discountPercent = 10; //10프로 할인
    @Override
    public int discount(Member member, int price) {

        if(member.getGrade() == Grade.VIP) {
            return price * discountPercent / 100;
        } else {
            return 0;
        }

    }
}
