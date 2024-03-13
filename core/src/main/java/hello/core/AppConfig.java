package hello.core;

import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;
import hello.core.discount.DiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// AppConfig가 @Configuration으로 스프링 컨테이너로 등록됨.
@Configuration
public class AppConfig {

    //`@Bean`이 라 적힌 메서드를 모두 호출해서 반환된 객체를 스프링 컨테이너에 등록한다.
    // 이렇게 스프링 컨테이너에 등록된 객체를 스프링 빈이라 한다.
    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(
                memberRepository(), discountPolicy()
        );
    }

    @Bean
    public MemoryMemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        return new RateDiscountPolicy();
    }


}
