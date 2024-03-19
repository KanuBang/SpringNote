package hello.core.scan;

import hello.core.AutoAppConfig;
import hello.core.Order.OrderService;
import hello.core.Order.OrderServiceImpl;
import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.web.reactive.HttpHandlerAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AutoAppConfigTest {
    @Test
    void basicScan() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class);
        MemberService memberService = ac.getBean(MemberService.class);
        Assertions.assertThat(memberService).isInstanceOf(MemberService.class);
    }

    @Test
    @DisplayName("수정자 주입 테스트")
    void setterInjectionTest() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class);
        OrderServiceImpl orderService = ac.getBean(OrderServiceImpl.class);
    }


    @Test
    @DisplayName("스프링은 @Autowired의 자동 주입을 보장하지 한다.")
    void createOrder1() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class);
        OrderServiceImpl orderService = ac.getBean(OrderServiceImpl.class);
        Assertions.assertThat(orderService.getMemberService()).isInstanceOf(MemberService.class);
    }

    @Test
    @DisplayName("순수자바코드는 @Autowired의 자동 주입을 보장하지 않는다.")
    void createOrder2() {
        //OrderServiceImpl orderService = new OrderServiceImpl();
        //java.lang.AssertionError: Expecting actual not to be null
        //Assertions.assertThat(orderService.getMemberService()).isInstanceOf(MemberService.class);
    }

    @Test
    @DisplayName("lombok test")
    void lombokTest() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class);
        OrderServiceImpl orderService = ac.getBean(OrderServiceImpl.class);
        MemberService memberService = orderService.getMemberService();
        Assertions.assertThat(memberService).isInstanceOf(MemberService.class);
    }

    @Test
    @DisplayName("아무런 중복 처리 없이 타입 조회 시, 조회 빈이 2개 이상인 문제가 발생해야만 한다.")
    void noUniqueBean() {
        org.junit.jupiter.api.Assertions.assertThrows(UnsatisfiedDependencyException.class, () -> new AnnotationConfigApplicationContext(AutoAppConfig.class));
    }

    @Test
    @DisplayName("타입 조회 시, 조회 빈이 2개 이상인 문제 해결: primary + qualifier")
    void uniqueBean() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class);
        RateDiscountPolicy rateDiscountPolicy = ac.getBean(RateDiscountPolicy.class);
        Assertions.assertThat(rateDiscountPolicy).isInstanceOf(RateDiscountPolicy.class);
    }

    @Test
    @DisplayName("Qualifier와 Primary의 우선순위")
    void 우선순위테스트() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class);
        Assertions.assertThat(ac.getBean(FixDiscountPolicy.class)).isInstanceOf(FixDiscountPolicy.class);
    }


    @Test
    @DisplayName("annotation test")
    void annotationTest() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class);
        RateDiscountPolicy discountPolicy = ac.getBean(RateDiscountPolicy.class);
        Assertions.assertThat(discountPolicy).isInstanceOf(RateDiscountPolicy.class);
    }
}
