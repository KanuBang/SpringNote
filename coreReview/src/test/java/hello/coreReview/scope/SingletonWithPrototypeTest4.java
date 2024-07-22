package hello.coreReview.scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

public class SingletonWithPrototypeTest4 {
    @Test
    public void myTest() {
        // AnnotationConfigApplicationContext의 생성자에 클래스를 직접 전달하여, 애노테이션 없이도 스프링 빈으로 등록할 수 있다.
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);

        ClientBean bean1 = ac.getBean(ClientBean.class);
        int logic1 = bean1.logic();

        ClientBean bean2 = ac.getBean(ClientBean.class);
        int logic2 = bean2.logic();

        System.out.println("bean1 count = " + logic1);
        System.out.println("bean2 count = " + logic2);

        Assertions.assertThat(logic1).isEqualTo(1);
        Assertions.assertThat(logic2).isEqualTo(1);
    }

    static class ClientBean {
        @Autowired
        private ObjectProvider<PrototypeBean> prototypeBeanObjectProvider;

        public int logic() {
            PrototypeBean bean = prototypeBeanObjectProvider.getObject();
            bean.addCount();
            int count = bean.getCount();
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
