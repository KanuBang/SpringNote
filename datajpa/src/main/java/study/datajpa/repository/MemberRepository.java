package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);
    List<Member> findByUsername(@Param("username") String username); // 컬렉션 타입 반환

    // @Query
    @Query("select m from Member m where m.username= :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);
    // 실행할 메서드 정적 쿼리를 직접 작성하므로 이름 없는 Named 쿼리라 할 수 있음.
    // 장점: JPA NAMED QUERY 쿼리처럼 애플리케이션 실행 시점에 문법 오류를 발견할 수 있다.
    // 실무에서 메소드 이름으로 쿼리 생성 기능은 파라미터가 증가하면 메서드 이름이 지저분해서 @Query를 자주 사용한다.

    // 단수히 값 하나를 조회한다.
    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.datajpa.repository.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    // 파라미터 바인딩
    @Query("select m from Member m where m.age = :age")
    List<Member> findSameAge(@Param("age") int age);

    // 컬렉션 파라미터 바인딩
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);


    //반환 타입 -> 굉장히 다양해서 문서를 참고해야.
    @Query("select m from Member m where m.age between 20 and 30")
    List<Member> findByAge1();
    @Query("select m from Member m where m.age = 30")
    Member findByAge2();
    @Query("select m from Member m where m.age = 30")
    Optional<Member> findByAge3();


    //페이징

    // Page
    Page<Member> findByAge(int age, Pageable pageable);

    // Slice
    @Query("select m from Member m where m.age = :age")
    Slice<Member> sliceFindByAge(@Param(("age")) int age, Pageable pageable);

    // count 쿼리를 Page와 분리
    @Query(value = "select m from Member m", countQuery = "select count(m.username) from Member m")
    Page<Member> findMemberAllCountBy(Pageable pageable);


    // Top
    List<Member> findTop3By();

    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);


    // JPQL 페치 조인
    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    // 공통 메서드 오버라이드
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    // JPQL + 엔티티 그래프
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();


}
