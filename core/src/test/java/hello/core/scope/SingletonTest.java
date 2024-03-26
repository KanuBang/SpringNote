package hello.core.scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

public class SingletonTest {

    @Test
    public void singletonBeanFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SingletonBean.class);
        System.out.println("find singletonBean1");
        SingletonBean singletonBean1 = ac.getBean(SingletonBean.class);
        System.out.println("find singletonBean2");
        SingletonBean singletonBean2 = ac.getBean(SingletonBean.class);

        System.out.println("singletonBean1: " + singletonBean1);
        System.out.println("singletonBean2: " + singletonBean2);
        Assertions.assertThat(singletonBean1).isSameAs(singletonBean2);
    }


    @Scope("singleton")
    static class SingletonBean {

        @PostConstruct
        public void init() {
            System.out.println("singletonBean.init");
        }

        @PreDestroy
        public void destropy() {
            System.out.println("singletonBean.destroy");
        }
    }
}
