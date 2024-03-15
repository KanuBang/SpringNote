package hello.core.order;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.discount.DiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
// @RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    /* 필드 주입
    @Autowired private MemberRepository memberRepository;
    @Autowired private DiscountPolicy discountPolicy;
     */

    /* 의존 관계 자동 주입 시, 생서자가 1개 있으면 @Autowired 어노테이션 생략 가능 */

    // DiscountPolicy discountPolicy 에서 조회 빈이 2개 이상되어 에러가 터진다.
    // FixDiscountPolicy, RateDiscountPolicy 2개를 빈으로 등록했기 때문이다.
    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, @MainDiscountPolicy DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    /* 수정자 주입
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        System.out.println("수정자 memberRepository: " + memberRepository);
        this.memberRepository = memberRepository;
    }

    @Autowired
    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
        System.out.println("수정자 discountPolicy: " + discountPolicy);
        this.discountPolicy = discountPolicy;
    }
    */

    /*일반 메서드 주입
    @Autowired
    public void init(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        System.out.println("일반 메서드 주입 memberRepository: " + memberRepository);
        System.out.println("일반 메서드 주입 discountPolicy: " + discountPolicy);
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }
    */

    public MemberRepository getMemberRepository() {
        return memberRepository;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);
        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
