package hello.core.Order;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceImpl implements OrderService{
    private final MemberService memberService;
    private final DiscountPolicy discountPolicy;

    public OrderServiceImpl(MemberService memberService, @MainDiscountPolicy DiscountPolicy discountPolicy) {
        this.memberService = memberService;
        this.discountPolicy = discountPolicy;
    }

    public MemberService getMemberService() {
        return memberService;
    }

    public DiscountPolicy getDiscountPolicy() {
        return discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberService.findMember(memberId);
        int discountPrice = discountPolicy.discount(member,itemPrice);
        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}



