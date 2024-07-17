package hello.coreReview.discount;

import hello.coreReview.member.Member;
import hello.coreReview.member.MemberRepository;
import hello.coreReview.member.MemoryMemberRepository;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.lang.annotation.Retention;

@Component
@Getter
//@RequiredArgsConstructor 가 final이 붙은 필드의 생성자를 자동으로 만들어준다.
//그리고 생성자 하나라면 자동으로 @Autowired가 붙어 생성자 주입이 발생한다.
public class OrderServiceImpl implements OrderService{
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;
    /*
    필드 주입 - 편하긴 하지만, 스프링 프레임워크가 없는 환경에서 무용 지물
    @Autowired
    private final MemberRepository memberRepository;
    @Autowired
    private final DiscountPolicy discountPolicy;
     */

    /*
    생성자가 하나라면 @Autowired 생략가능 -> LOMBOK으로 대체
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }
     */

    public OrderServiceImpl(MemberRepository memberRepository,
                            DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member findMember = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(findMember, itemPrice);
        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    /*
    수정자 주입 - 필수가 아닌 의존관게에서 사용, 변경 의존 관계에서 사용
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    @Autowired
    public void setDiscountPolicy(DiscountPolicy discountPolicy){
        this.discountPolicy = discountPolicy;
    }
    */

    /*
    일반 메서드 주입
    @Autowired
    public void init(MemberRepository memberRepository, DiscountPolicy discountPolicy){
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }
    */
}
