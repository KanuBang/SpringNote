package hello.core;

import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MemberApp {
    public static void main(String[] args) {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberService memberService = ac.getBean("memberService", MemberService.class);
        Member member1 = new Member(1L, "chanwu", Grade.VIP);
        Member member2 = new Member(2L, "cancelo", Grade.BASIC);
        memberService.join(member1);
        memberService.join(member2);

        System.out.println(memberService.findMember(1L).getName());
        System.out.println(memberService.findMember(2L).getName());
    }
}
