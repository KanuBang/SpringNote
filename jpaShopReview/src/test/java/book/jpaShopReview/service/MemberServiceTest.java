package book.jpaShopReview.service;

import book.jpaShopReview.domain.Member;
import book.jpaShopReview.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class) // 스프링과 테스트 통합
@SpringBootTest // 스프링 부트를 띄우고 테스트를 한다. 이게 없으면 @Autowired 미작동
@Transactional // 반복 가능한 테스트 지원, 트랜잭션 지원, 테스트가 끝나면 강제로 롤백
public class MemberServiceTest {
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;


    @Test
    public void 회원가입() throws Exception {
        //Given
        Member member = new Member();
        member.setName("kim");

        //When
        Long saveId = memberService.join(member);

        //Then
        assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {

        //Given
        Member member1 = new Member();
        member1.setName("minjae");

        Member member2 = new Member();
        member2.setName("minjae");

        //When
        memberService.join(member1);
        memberService.join(member2); // 예외가 발생해야 한다.

        //Then
        fail("예외가 발생해야 한다.");
    }
}