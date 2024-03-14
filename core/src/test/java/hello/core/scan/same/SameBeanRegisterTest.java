package hello.core.scan.same;

import hello.core.scan.same.beanA.BeanA;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

public class SameBeanRegisterTest {

    @Test
    void sameBeanRegisterTest () {
        Assertions.assertThrows(Exception.class, () -> new AnnotationConfigApplicationContext(sameBeanTest.class));
    }

    @ComponentScan(
            basePackages = "hello.core.scan.same"
    )
    static class sameBeanTest {

    }


}


