package hello.core.order;

import hello.core.member.Member;
import hello.core.member.MemoryMemberRepository;

public class OrderServiceImpl implements OrderService{
    private MemoryMemberRepository memberRepository;
    // private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    private hello.core.discount.DiscountPolicy discountPolicy;

    public OrderServiceImpl(MemoryMemberRepository memberRepository, hello.core.discount.DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy =  discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member,itemPrice);
        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
