package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class TmpMemberRepositoryTest {

    @Autowired
    TmpMemberRepository tmpMemberRepository;
    @Autowired
    TeamRepository teamRepository;

    @Autowired
    EntityManager em;
    @Test
    @DisplayName("나가는 쿼리 확인")
    public void 페치조인_메서드이름조회_조건() throws Exception {

        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        tmpMemberRepository.save(new Member("member1",10,teamA));
        tmpMemberRepository.save(new Member("member2",20,teamB));

        //when
        em.flush();
        em.clear();

        //then
        List<Member> member1 = tmpMemberRepository.findByUsername("member1");
        member1.get(0).getTeam().getName();

    }
}