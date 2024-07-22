package hello.coreReview.scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

/*
프로토타입 스코프 빈
1. 스프링 컨테이너에 요청될 때마다 새로운 빈 생성
2. 초기화 콜백 이후 스프링 컨테이너는 관리에서 손 땜.
3. 초기화 콟잭 이후 빈이 클라이언트에게 반환되어 관리 권한이 클라이언트에게 넘어감
 */
public class PrototypeTest {
    @Test
    public void myPrototypeBeanTest() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        PrototypeBean bean1 = ac.getBean(PrototypeBean.class);
        PrototypeBean bean2 = ac.getBean(PrototypeBean.class);
        System.out.println("bean 1 = "+ bean1);
        System.out.println("bean 2 = "+ bean2);
        Assertions.assertThat(bean1).isNotSameAs(bean2);
    }

    @Scope("prototype")
    static class PrototypeBean {
        @PostConstruct
        public void init() {
            System.out.println("prototypeBean init");
        }

        @PreDestroy
        public void destroy() {
            System.out.println("prototypeBean destroy");
        }
    }
}
