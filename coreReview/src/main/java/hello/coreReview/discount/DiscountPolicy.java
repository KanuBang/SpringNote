package hello.coreReview.discount;

import hello.coreReview.member.Member;

public interface DiscountPolicy {
    int discount(Member member, int price);
}
