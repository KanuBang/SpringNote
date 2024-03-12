package hello.core;

import hello.core.order.Order;
import hello.core.order.OrderService;
import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class OrderApp {
    public static void main(String[] args) {
        //AppConfig appConfig = new AppConfig();

        //스프링 컨테이너
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);

        //스프링 컨테이너를 통해 필요한 스프링 빈(객체) 찾는다. 스프링 컨테이너의 getBean() 메서드로 Bean 조회.
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);
        OrderService orderService = applicationContext.getBean("orderService", OrderService.class);

        Member member = new Member(1L, "De Ligt", Grade.VIP);
        memberService.join(member);

        Order order = orderService.createOrder(1L, "adidas underwear", 10000);

        System.out.println("가격은: " + order.calculatePrice());
        System.out.println("주문 정보는 아래와 같습니다: " + order);
    }
}
