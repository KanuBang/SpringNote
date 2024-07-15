package hello.coreReview;

import hello.coreReview.discount.*;
import hello.coreReview.member.MemberRepository;
import hello.coreReview.member.MemberService;
import hello.coreReview.member.MemberServiceImpl;
import hello.coreReview.member.MemoryMemberRepository;

public class AppConfig {
    public MemberService memberService() {
        return new MemberServiceImpl(new MemoryMemberRepository());
    }

    public OrderService orderService() {
        return new OrderServiceImpl(new MemoryMemberRepository(), new FixDiscountPolicy());
    }

    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }
    public DiscountPolicy discountPolicy() {
        return new RateDiscountPolicy();
    }
}
