package study.datajpa.customeRepo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

@SpringBootTest
@Transactional
@Rollback(value = false)
public class CustomTest {

    @Autowired
    MemberRepository1 memberRepository;
    @Test
    public void customTest() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> memberCustom = memberRepository.findMemberCustom();

        Assertions.assertThat(memberCustom.size()).isEqualTo(2);
    }
}
