package hello.core.member;

import hello.core.AppConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MemberServiceTest {

    MemberService memberService;

    @BeforeEach
    public void beforeEach() {
        AppConfig appConfig = new AppConfig();
        memberService = appConfig.memberService();
    }
    @Test
    void join() {
        //given
        Member member = new Member(3L, "gnabry", Grade.VIP);
        //when
        memberService.join(member);
        Member foundMember = memberService.findMember(3L);
        //then
        Assertions.assertThat(member).isEqualTo(foundMember);
    }

    @Test
    void findMember(){
        //given
        Member member = new Member(4L,"dier", Grade.BASIC);
        //when
        memberService.join(member);
        Member result = memberService.findMember(4L);
        //then
        Assertions.assertThat(result).isEqualTo(member);
    }
}
