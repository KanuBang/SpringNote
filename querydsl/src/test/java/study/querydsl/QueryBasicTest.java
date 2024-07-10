package study.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPADeleteClause;
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
import study.querydsl.dto.MemberDto;
import study.querydsl.dto.QMemberDto;
import study.querydsl.dto.UserDto;
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

    @Test
    public void caseExp1() throws Exception {
        List<String> result = queryFactory.select(member.age
                .when(10).then("열살")
                .when(20).then("스무살")
                .otherwise("늙은이")).from(member).fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void caseExp2() throws Exception {
        List<String> result = queryFactory.select(new CaseBuilder()
                .when(member.age.between(0, 20)).then("젊은이")
                .when(member.age.between(21, 40)).then("늙은이")
                .otherwise("기")
        ).from(member).fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void constantArithmetic() throws Exception {
        Tuple result = queryFactory
                .select(member.username, Expressions.constant("A"))
                .from(member)
                .fetchFirst();

        System.out.println(result.toString());
    }

    @Test
    @DisplayName("concat은 문자열만 가능하기에 타입이 String으로 일치되어야 한다.")
    public void constantTest() throws Exception {
        String result = queryFactory
                .select(member.username.concat("_").concat(member.age.stringValue()))
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();
        System.out.println(result);
    }
    @Test
    public void projectionTest() throws Exception {
        List<Tuple> result = queryFactory.select(member.username, member.age).from(member).fetch();
        for (Tuple tuple : result) {
            System.out.println(tuple.get(member.username) + " " + tuple.get(member.age));
        }
    }

    @Test
    @DisplayName("프로퍼티 접근-setter, 필드 접근, 생성자 접근")
    public void dtoTest1() {

        List<MemberDto> result = em.createQuery(
                        "select new study.querydsl.dto.MemberDto(m.username, m.age) " +
                        "from Member m", MemberDto.class)
                .getResultList();

        List<MemberDto> result1 = queryFactory
                .select(Projections.bean(MemberDto.class, member.username, member.age))
                .from(member)
                .fetch();

        List<MemberDto> result2 = queryFactory
                .select(Projections.fields(MemberDto.class, member.username, member.age))
                .from(member)
                .fetch();

        List<MemberDto> result3 = queryFactory
                .select(Projections.constructor(MemberDto.class, member.username, member.age))
                .from(member)
                .fetch();

        assertThat(result1).isEqualTo(result);
        assertThat(result2).isEqualTo(result);
        assertThat(result3).isEqualTo(result);
    }


    @Test
    public void dtoTest2() {
        QMember memberSub = new QMember("memberSub");
        List<UserDto> result = queryFactory.select(Projections.fields(
                UserDto.class,
                member.username.as("name"),
                ExpressionUtils.as(
                        select(memberSub.age.max()).from(memberSub), "age")
        )).from(member).fetch();

        for (UserDto userDto : result) {
            System.out.println("name = " + userDto.getName());
            System.out.println("age = " + userDto.getAge());
        }
    }

    @Test
    public void dtoTest3() {
        List<MemberDto> result = queryFactory.select(new QMemberDto(member.username, member.age)).from(member).fetch();
        assertThat(result.size()).isEqualTo(4);
    }

    @Test
    public void distinctTest() {
        em.persist(new Member("member1", 50));
        List<String> result1 = queryFactory.select(member.username).from(member).fetch();
        List<String> result2 = queryFactory.select(member.username).distinct().from(member).fetch();

        assertThat(result1.size()).isEqualTo(5);
        assertThat(result2.size()).isEqualTo(4);
    }

    @Test
    public void deleteTest() {
        long deletedCnt = queryFactory.delete(member).where(member.age.eq(10), member.username.eq("member1")).execute();
        em.flush();
        em.clear();
        assertThat(deletedCnt).isEqualTo(1);
    }

    @Test
    public void 동적쿼리_booleanBuilder() throws Exception {
        String username = "member1";
        Integer ageParam = 10;
        List<Member> result = searchMember1(username, ageParam);
        assertThat(result.size()).isEqualTo(1);
    }

    private List<Member> searchMember1(String usernameCond, Integer ageParamCond) {
        BooleanBuilder builder = new BooleanBuilder();

        if (usernameCond != null) {
            builder.and(member.username.eq(usernameCond));
        }

        if (ageParamCond != null) {
            builder.and(member.age.eq(ageParamCond));
        }

        return queryFactory.selectFrom(member).where(builder).fetch();

    }

    @Test
    public void 동적쿼리_WhereParam() throws Exception {
        String usernameParam = "member1";
        Integer ageParam = 10;
        
        List<Member> result1 = searchMember2(usernameParam, ageParam);
        List<Member> result2 = searchMember3(usernameParam, ageParam);

        assertThat(result1.size()).isEqualTo(1);
        assertThat(result2.size()).isEqualTo(1);
    }

    private List<Member> searchMember2(String usernameCond, Integer ageCond) {
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(usernameEq(usernameCond), ageEq(ageCond))
                .fetch();
        return result;
    }
    private List<Member> searchMember3(String usernameCond, Integer ageCond) {
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(allEq(usernameCond,ageCond))
                .fetch();
        return result;
    }

    //조합
    private BooleanExpression ageEq(Integer ageCond) {
        return ageCond != null ? member.age.eq(ageCond) : null;
    }

    private BooleanExpression usernameEq(String usernameCond) {
        return usernameCond != null ? member.username.eq(usernameCond) : null;
    }

    private BooleanExpression allEq(String usernameCond, Integer ageCond) {
        return usernameEq(usernameCond).and(ageEq(ageCond));
    }

    @Test
    public void updateTest1() {
        long cnt = queryFactory.update(member)
                .set(member.username, "비회원")
                .where(member.age.lt(20))
                .execute();
        //벌크 연산 처리
        em.flush();
        em.clear();

        assertThat(cnt).isEqualTo(1);
    }

    @Test
    public void updateTest2() {
        long cnt = queryFactory.update(member).set(member.age, member.age.add(1)).execute();
        Integer result = queryFactory.select(member.age).from(member).fetchFirst();
        assertThat(result).isEqualTo(11);
    }

    @Test
    public void updateTest3() {
        long cnt = queryFactory.update(member).set(member.age, member.age.multiply(2)).execute();
        Integer result = queryFactory.select(member.age).from(member).fetchFirst();
        assertThat(result).isEqualTo(20);
    }

    @Test
    public void deleteTest2() {
        long cnt = queryFactory.delete(member).where(member.username.eq("member4")).execute();
        assertThat(cnt).isEqualTo(1);
    }

}
