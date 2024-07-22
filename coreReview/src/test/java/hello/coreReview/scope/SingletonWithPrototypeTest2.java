package hello.coreReview.scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

public class SingletonWithPrototypeTest2 {
    @Test
    public void myTest() {
        // AnnotationConfigApplicationContext의 생성자에 클래스를 직접 전달하여, 애노테이션 없이도 스프링 빈으로 등록할 수 있다.
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);
        ClientBean bean1 = ac.getBean(ClientBean.class);
        bean1.logic();
        ClientBean bean2 = ac.getBean(ClientBean.class);
        bean2.logic();
        bean2.logic();
        System.out.println("count = " + bean1.getCount());
        Assertions.assertThat(bean2.getCount()).isEqualTo(3);
    }

    static class ClientBean {
        private final PrototypeBean prototypeBean;

        @Autowired
        public ClientBean(PrototypeBean prototypeBean) {
            this.prototypeBean = prototypeBean;
        }

        public int logic() {
            this.prototypeBean.addCount();
            int count = prototypeBean.getCount();
            return count;
        }

        public int getCount() {
            int count = this.prototypeBean.getCount();
            return count;
        }

    }

    // 프로토타입 스코프 빈 -> 해당 빈에 대한 요청이 들어오면, 스프링 컨테이너가 새롭게 각각 생성하여 새로운 인스턴스를 반환한다.
    @Scope("prototype")
    static class PrototypeBean {
        private int count = 0;

        public void addCount() {
            this.count++;
        }

        public int getCount() {
            return this.count;
        }

        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean init");
        }

        @PreDestroy
        public void destroy() {
            System.out.println("PrototypeBean close");
        }
    }
}
