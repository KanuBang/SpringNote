package hello.servlet.domain.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberRepositoryTest {
    MemberRepository memberRepository = MemberRepository.getInstance();

    //각 테스트가 실행될 때마다 선행되어 실행됨
    @AfterEach
    void afterEach() {
        memberRepository.clear();
    }

    @Test
    void save() {
        //given
        Member member = new Member("hello", 20);

        //when
        Member save = memberRepository.save(member);

        //then
        Member findMember = memberRepository.findById(save.getId());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    void findAll() {

        //given
        for(int i = 1; i < 3; i++) {
            memberRepository.save(new Member("member"+i, i+20));
        }

        //when
        List<Member> findMembers = memberRepository.findAll();

        //then
        Assertions.assertThat(findMembers.size()).isEqualTo(2);
    }

}