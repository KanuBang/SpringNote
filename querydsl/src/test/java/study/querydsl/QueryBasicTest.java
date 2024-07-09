package study.querydsl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
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

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static study.querydsl.entity.QMember.member;

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

        for (int i = 1; i < 5; i++) {
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
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void startQuerydsl() {

        QMember m = new QMember("m");

        Member findMember = queryFactory.select(m).from(m).where(m.username.eq("member1")).fetchOne();
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void startQueryDsl3() {
        Member findMember = queryFactory.select(member).from(member).where(member.username.eq("member1")).fetchOne();
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void search() {
        Member findMember = queryFactory.selectFrom(member).where(member.username.eq("member1").and(member.age.eq(10))).fetchOne();
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void searchQueries() {
        member.username.eq("member1");
        member.username.ne("member1");
        member.username.eq("member1").not();

        member.username.isNotNull();
        member.age.in(10, 20); // age in (10, 20)
        member.age.notIn(10, 20); // age not in (10,20)
        member.age.between(10, 30); // 10 <= age <= 20

        member.age.goe(30); //greater or equal
        member.age.gt(30); //greater than

        member.age.loe(30); // less or equal
        member.age.lt(30); // less than

        member.username.like("member%"); // like 검색
        member.username.contains("member"); //%member%
        member.username.startsWith("member"); //member%
    }

    @Test
    public void searchAndParam() {
        List<Member> result1 = queryFactory.selectFrom(member).where(member.username.eq("member1"), member.age.eq(10)).fetch();
        assertThat(result1.size()).isEqualTo(1);
    }

    @Test
    public void queryResult() {

        //List
        List<Member> result1 = queryFactory.selectFrom(member).fetch();
        assertThat(result1.size()).isEqualTo(4);

        //단건
        Member member1 = queryFactory.selectFrom(member).where(member.username.eq("member1")).fetchOne();
        assertThat(member1.getUsername()).isEqualTo("member1");


        //처음 한 건 조회
        Member member2 = queryFactory.selectFrom(member).fetchFirst();
        assertThat(member2.getUsername()).isEqualTo("member1");

        //페이징에서 사용
        QueryResults<Member> memberQueryResults = queryFactory.selectFrom(member).fetchResults();


        long cnt = queryFactory.selectFrom(member).fetchCount();
        assertThat(cnt).isEqualTo(4);
    }

    @Test
    public void sort() {
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(100)).orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetch();

        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull = result.get(2);

        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();

    }
}
