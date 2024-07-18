package hello.coreReview.beanfind;

import hello.coreReview.AutoAppConfig;
import hello.coreReview.discount.OrderService;
import hello.coreReview.discount.OrderServiceImpl;
import hello.coreReview.discount.RateDiscountPolicy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MyBeanPrimaryTest {
    @Test
    public void beanPrimaryTest() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class);
        OrderServiceImpl orderService = ac.getBean(OrderServiceImpl.class);
        Assertions.assertThat(orderService.getDiscountPolicy()).isInstanceOf(RateDiscountPolicy.class);
    }
}
