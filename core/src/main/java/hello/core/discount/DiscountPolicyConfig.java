package hello.core.discount;

import org.springframework.context.annotation.Bean;

// AllBeanTest에서 `DiscountService` 가 의존관계 자동 주입으로
// `Map<String, DiscountPolicy>`가 RateDiscountPolicy, FixDiscountPolicy을 주입받는다.
// 이렇게 다형성을 적극적으로 사용하는 것은 굉장히 좋다.
// 하지만, 다른 개발자가 봤을 때, 자동 주입이기 때문에 DiscountPolicy에 뭐가 들어가는 지 혼동할 수도 있다.
// 그래서 이때는 자동 주입말고 수동 주입으로 관련 클래스를 묶어 확실하게 표현하는 게 더 좋을 수도 있다.
public class DiscountPolicyConfig {

    @Bean
    public DiscountPolicy rateDiscountPolicy() {
        return new RateDiscountPolicy();
    }
    @Bean
    public DiscountPolicy fixDiscountPolicy() {
        return new FixDiscountPolicy();
    }
}
