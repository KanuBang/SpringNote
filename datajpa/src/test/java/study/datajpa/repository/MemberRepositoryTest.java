package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.hibernate.NonUniqueResultException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    EntityManager em;

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

    @Test
    @DisplayName("@Query 테스트 - 파라미터 바인딩 - 동갑 찾기")
    public void queryTest5() {

        int age = 32;
        Member member1 = new Member("pogba", age);
        Member member2 = new Member("iu", age);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> sameAge = memberRepository.findSameAge(age);

        assertThat(sameAge.size()).isEqualTo(2);

    }

    @Test
    @DisplayName("@Query 테스트 - 컬렉션 파라미터 바인딩")
    public void queryTest6() {
        Member member1 = new Member("pogba");
        Member member2 = new Member("iu");
        Member member3 = new Member("tim");

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        List<Member> names = memberRepository.findByNames(List.of("pogba", "iu", "tim"));
        assertThat(names.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("@Query 테스트 - 조회 결과가 없을 때")
    public void queryTest7() {

        Member member1 = new Member("yamal", 17);
        Member member2 = new Member("jinsu", 32);
        Member member3 = new Member("son", 32);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        // 조회 결과에 해당 하는 것이 없이 빈 리스트를 반환해야만 한다.
        List<Member> result1 = memberRepository.findByAge1();

        // 단건 조회 결과에 해당 하는 것이 없기에 null을 반환해야 한다.
        // 내부적으로는 예외가 발생하지만 data jpa가 null을 반환한다.
        Member byAge2 = memberRepository.findByAge2();

        // Optional은 조회 결과가 없을 때 null이다.
        Optional<Member> byAge3 = memberRepository.findByAge3();

        assertThat(result1.size()).isEqualTo(0);
        assertThat(byAge2).isEqualTo(null);
        assertThat(byAge3.isEmpty()).isEqualTo(true);

    }

    @Test
    @DisplayName("단건 조회인데 결과가 2개 반환될 때 예외 발생")
    public void queryTest8() {

        Member member1 = new Member("jinsu", 30);
        Member member2 = new Member("son", 30);

        memberRepository.save(member1);
        memberRepository.save(member2);

        assertThrows(IncorrectResultSizeDataAccessException.class, () -> {
            memberRepository.findByAge2();
        });
    }

    @Test
    public void page() throws Exception {

        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        //when
        PageRequest pageRequest = PageRequest.of(0,3, Sort.by(Sort.Direction.DESC, "username"));
        Page<Member> page = memberRepository.findByAge(10, pageRequest);

        //then
        List<Member> content = page.getContent();// 조회된 데이터
        assertThat(content.size()).isEqualTo(3); // 조호된 데이터 수
        assertThat(page.getTotalElements()).isEqualTo(5); // 전체 데이터 수
        assertThat(page.getNumber()).isEqualTo(0); // 페이지 번호
        assertThat(page.getTotalPages()).isEqualTo(2); // 전체 페이지 번호
        assertThat(page.isFirst()).isTrue(); // 첫번째 항목인가?
        assertThat(page.hasNext()).isTrue(); // 다음 페이지가 있는가?

    }

    @Test
    public void slicePage() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));


        //when
        PageRequest pageRequest = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "username"));
        Slice<Member> page = memberRepository.sliceFindByAge(10, pageRequest);
        List<Member> content = page.getContent();

        //then
        assertThat(content.size()).isEqualTo(2);
        assertThat(page.getNumberOfElements()).isEqualTo(2);
    }

    @Test
    public void separateCountQueryFromPage() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));


        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        // 쿼리 차이점 확인 목적
        memberRepository.findByAge(10, pageRequest);
        memberRepository.findMemberAllCountBy(pageRequest);
    }

    @Test
    @DisplayName("top query test")
    public void topQuery() throws Exception {

        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        //when
        List<Member> result = memberRepository.findTop3By();

        //then
        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("page를 유지하면서 엔티티를 DTO로 변환하기")
    public void dtoTransfer() {

        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        //when
        PageRequest request = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // 페이지를 유지하면서 엔티티를 DTO로 변환하기
        Page<Member> result = memberRepository.findByAge(10, request);
        Page<MemberDto> dtoResult = result.map(m -> new MemberDto(m.getId(), m.getUsername()));

        List<MemberDto> content = dtoResult.getContent();
        assertThat(content.get(0)).isInstanceOf(MemberDto.class);
    }

    @Test
    @DisplayName("bulk test")
    public void bulkTest() {

        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        //when
        int cnt = memberRepository.bulkAgePlus(20);

        //then
        assertThat(cnt).isEqualTo(3);
    }


    @Test
    @DisplayName("쿼리보고 N+1 문제가 발생과 fetch join으로 해결한 것을 인지하자.")
    public void findMemberLazy() throws Exception{

        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        memberRepository.save(new Member("member1",10,teamA));
        memberRepository.save(new Member("member2",20,teamB));

        //when
        em.flush();
        em.clear();

        //when
        List<Member> members = memberRepository.findAll();
        
        //then
        for (Member member : members) {
            member.getTeam().getName();
        }

        System.out.println("직접 @Query에 JPQL 작성하고 fetch join 사용");
        memberRepository.findMemberFetchJoin();
        System.out.println("스프링 데이터 JPA 오버라이딩 한 다음 EntityGraph를 이용하여 fetch join");
        memberRepository.findAll();
        System.out.println("@Query + @EntiryGraph fetch join");
        memberRepository.findMemberEntityGraph();

    }

    @Test
    @DisplayName("읽어온 엔티티의 값을 변경하고 flush를 호출해도, 실제로는 데이터베이스에 변경 사항이 반영되지 않는다")
    public void hint_test() throws Exception {

        //given
        memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();

        //then
        Member member1 = memberRepository.findReadOnlyByUsername("member1");
        member1.setUsername("chanwu");

        //when
        em.flush(); // update 쿼리 미발생
    }

    @Test
    public void hint_test2() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        //then, 카운트 쿼리 미발생 (forCount hint를 false로 지정했기 때문)
        Page<Member> members = memberRepository.hintFindByUsername("member1", pageRequest);
    }
}