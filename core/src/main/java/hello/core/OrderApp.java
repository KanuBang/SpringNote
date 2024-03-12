package hello.core;

import hello.core.Order.Order;
import hello.core.Order.OrderService;
import hello.core.Order.OrderServiceImpl;
import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;

public class OrderApp {
    public static void main(String[] args) {
        MemberService memberService = new MemberServiceImpl();
        OrderService orderService = new OrderServiceImpl();

        Member member = new Member(1L, "De Ligt", Grade.VIP);
        memberService.join(member);

        Order order = orderService.createOrder(1L, "adidas underwear", 10000);

        System.out.println("가격은: " + order.calculatePrice());
        System.out.println("주문 정보는 아래와 같습니다: " + order);
    }
}
