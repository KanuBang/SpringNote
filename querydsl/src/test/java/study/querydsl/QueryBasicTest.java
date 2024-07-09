package study.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.Team;

@SpringBootTest
@Transactional
public class QueryBasicTest {

    @PersistenceContext
    EntityManager em;

    JPAQueryFactory queryFactory;
    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        for(int i = 1 ; i < 5; i++) {
            if (i <= 2) {
                em.persist(new Member("member" + i, i * 10, teamA));
            } else {
                em.persist(new Member("member" + i, i * 10, teamB));
            }
        }
    }

    @Test
    public void startJPQL() {
        String qlString = "select m from Member m " + "where m.username = :username";
        Member findMember = em.createQuery(qlString, Member.class).setParameter("username", "member1").getSingleResult();
        Assertions.assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void startQuerydsl() {

        QMember m = new QMember("m");

        Member findMember = queryFactory.select(m).from(m).where(m.username.eq("member1")).fetchOne();
        Assertions.assertThat(findMember.getUsername()).isEqualTo("member1");
    }
}
