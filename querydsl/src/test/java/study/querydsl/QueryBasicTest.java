package study.querydsl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.Team;

import java.util.List;

import static com.querydsl.jpa.JPAExpressions.*;
import static org.assertj.core.api.Assertions.*;
import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;

@SpringBootTest
@Transactional
public class QueryBasicTest {

    @PersistenceContext
    EntityManager em;

    @PersistenceUnit
    EntityManagerFactory emf;

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

    @Test
    @DisplayName("조회 건수 제한 페이징")
    public void paging1() {
        List<Member> result = queryFactory.selectFrom(member).orderBy(member.username.desc()).offset(1).limit(2).fetch();
        for (Member member1 : result) {
            System.out.println(member1.toString());
        }
    }

    @Test
    @DisplayName("전체 조회 수 페이징")
    public void paging2() {
        // fetchResults() : 페이징 정보 포함, total count 쿼리 추가 실행
        QueryResults<Member> queryResults = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetchResults();

        assertThat(queryResults.getTotal()).isEqualTo(4);
        assertThat(queryResults.getLimit()).isEqualTo(2);
        assertThat(queryResults.getOffset()).isEqualTo(1);
        assertThat(queryResults.getResults().size()).isEqualTo(2);
    }

    @Test
    public void aggregation() throws Exception {

        List<Tuple> result = queryFactory.select(member.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min())
                .from(member)
                .fetch();

        Tuple tuple = result.get(0);
        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(100);
        assertThat(tuple.get(member.age.avg())).isEqualTo(25);
        assertThat(tuple.get(member.age.max())).isEqualTo(40);
        assertThat(tuple.get(member.age.min())).isEqualTo(10);
    }

    @Test
    public void group() throws Exception {

        List<Tuple> result = queryFactory
                .select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .fetch();

        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);

        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(15);
        assertThat(teamB.get(team.name)).isEqualTo("teamB");
        assertThat(teamB.get(member.age.avg())).isEqualTo(35);
    }

    @Test
    public void join() throws Exception {
        //연관관계가 있는 조인
        List<Member> result = queryFactory.selectFrom(member)
                .join(member.team, team)
                .where(team.name.eq("teamA"))
                .fetch();

        assertThat(result).extracting("username").containsExactly("member1", "member2");
    }

    @Test
    public void theta_join() throws Exception {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        List<Member> result = queryFactory.selectFrom(member).from(member, team).where(member.username.eq(team.name)).fetch();
        assertThat(result).extracting("username").containsExactly("teamA","teamB");
    }


    @Test
    public void join_on_filtering() throws Exception {
        List<Tuple> result = queryFactory.select(member, team).from(member).leftJoin(member.team, team).on(team.name.eq("teamA")).fetch();
        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    @Test
    public void join_on_no_relation() throws Exception {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));

        List<Tuple> result = queryFactory.select(member, team).from(member).leftJoin(team).on(member.username.eq(team.name)).fetch();

        for (Tuple tuple : result) {
            System.out.println("t= " + tuple);
        }

    }

    @Test
    public void fetchJoinNo() throws Exception {
        em.flush();
        em.clear();

        Member member1 = queryFactory
                .select(member)
                .where(member.username.eq("member1"))
                .from(member)
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(member1.getTeam());
        assertThat(loaded).as("페치 조인 미적용").isFalse();
    }

    @Test
    public void fetchJoinUse() throws Exception {
        em.flush();
        em.clear();

        Member findMember = queryFactory.selectFrom(member)
                .join(member.team, team).fetchJoin()
                .where(member.username.eq("member1"))
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("페치 조인 적용").isTrue();

    }

    @Test
    public void subQuery() throws Exception {
        QMember memberSub = new QMember("memberSub");
        List<Member> result = queryFactory.selectFrom(member)
                .where(member.age.eq(
                        select(memberSub.age.max()).from(memberSub)
                )).fetch();
        assertThat(result).extracting("age").containsExactly(40);

    }

    @Test
    public void subQueryGoe() throws Exception {
        QMember memberSub = new QMember("memberSub");
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.goe(select(memberSub.age.avg())
                .from(memberSub)))
                .fetch();
        assertThat(result).extracting("age").containsExactly(30, 40);
    }

    @Test
    public void selectSubQuery() throws Exception {
        QMember memberSub = new QMember("memberSub");
        List<Tuple> result = queryFactory.select(member.username,
                select(memberSub.age.avg())
                        .from(memberSub)
        ).from(member)
        .fetch();

        for (Tuple tuple : result) {
            String username = tuple.get(member.username);
            System.out.println("username: " + username);
            System.out.println("age: " + tuple.get(select(memberSub.age.avg())
                    .from(memberSub)));
        }
    }
}
