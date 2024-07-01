package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @Test
    public void testMember() {
        System.out.println(memberRepository.getClass());
        Member member = new Member("memberA");

        Member savedMember = memberRepository.save(member);

        // 있을 수도 없을 수도 있으니까 Optional로 제공
        // Optional<Member> example = memberRepository.findById(savedMember.getId());

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);

    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void queryTest() {
        Member member1 = new Member("chanwu");
        Member member2 = new Member("hosung");

        memberRepository.save(member1);
        memberRepository.save(member2);

        Member result = memberRepository.findByUsername("chanwu").get(0);

        assertThat(result).isEqualTo(member1);
    }

    @Test
    @DisplayName("@Query 테스트")
    public void queryTest2() {
        Member member1 = new Member("chanwu",20);
        Member member2 = new Member("hosung",50);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findUser("chanwu", 20);
        assertThat(result.get(0)).isEqualTo(member1);
    }

    @Test
    @DisplayName("@Query 테스트 - 단건 조회")
    public void queryTest3() {
        Member member1 = new Member("chanwu",20);
        Member member2 = new Member("hosung",50);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> result = memberRepository.findUsernameList();
        assertThat(result.get(0)).isEqualTo(member1.getUsername());
        assertThat(result.get(1)).isEqualTo(member2.getUsername());
    }

    @Test
    @DisplayName("@Query 테스트 - DTO로 직접 조회")
    public void queryTest4() {

        Team team = new Team("mu");
        teamRepository.save(team);

        // 처음에 Team을 안 만들고 Member로만 테스트 했더니 아무 것도 빈 리스트가 반환되는 현상이 발생
        // 빈 테이블과 조인하면 이런 현상이 발생함
        Member member1 = new Member("chanwu",20);
        member1.setTeam(team);
        memberRepository.save(member1);

        List<MemberDto> result = memberRepository.findMemberDto();
        assertThat(result.get(0).getUsername()).isEqualTo(member1.getUsername());
    }

}