package hello.core.singleton;


import hello.core.AppConfig;
import hello.core.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
class SingletonServiceTest {
    @Test
    @DisplayName("싱글톤 패턴")
    public void test() {
        //의존 관계 상 클라이언트가 구체 클래스를 의존하게 됨.
        SingletonService singletonService1 = SingletonService.getInstance();
        SingletonService singletonService2 = SingletonService.getInstance();

        System.out.println("singleton Service1 " + singletonService1);
        System.out.println("singleton Service2 " + singletonService2);

        assertThat(singletonService1).isEqualTo(singletonService2);
        singletonService1.logic();
    }

    @Test
    @DisplayName("스프링 컨테이너는 싱글톤 컨테이너")
    // 싱글톤 컨테이너에 있는 객체들은 모두 싱글톤 패턴으로 관리됨.
    // 싱클톤 객체를 생성하고 관리하는 기능을 싱글톤 레지스트리라고 한다.
    public void springTest() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        MemberService memberService1 = ac.getBean("memberService", MemberService.class);
        MemberService memberService2 = ac.getBean("memberService", MemberService.class);

        System.out.println("memberService1: " + memberService1);
        System.out.println("memberService2: " + memberService2);

        assertThat(memberService1).isSameAs(memberService2);
    }
}