package hello.core.autowired;

import hello.core.member.Member;
import jakarta.annotation.Nullable;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Optional;


public class Autowired {

    @Test
    void autowiredOption() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class);
    }
    static class TestBean {
        // @Autowired` 만 사용하면 `required` 옵션의 기본값이 `true` 로 되어 있어서 자동 주입 대상이 없으면 오류 가 발생한다.
        @org.springframework.beans.factory.annotation.Autowired(required = false)
        public void setNoBean1(Member member1) {
            System.out.println("noBean1: " + member1);
        }

        @org.springframework.beans.factory.annotation.Autowired
        public void setNoBean2(@Nullable Member member2){
            System.out.println("noBean2: " + member2);
        }

        @org.springframework.beans.factory.annotation.Autowired
        public void setNoBean3(Optional<Member> member) {
            System.out.println("setNoBean3 = " + member);
        }
    }
}
