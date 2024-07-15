package hello.coreReview.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {

    // 구현에 의존하고 있다. -> 변경에 용이하지 않다.
    MemberService memberService = new MembreServiceImpl();

    @Test
    void join() {

        //given
        Member member = new Member(1L, "memberA", Grade.VIP);

        //when
        memberService.join(member);
        Member findMember = memberService.findMember(1L);

        //then

        Assertions.assertThat(findMember).isEqualTo(member);
    }

}