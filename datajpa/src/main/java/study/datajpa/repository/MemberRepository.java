package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);
    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username= :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);
    // 실행할 메서드 정적 쿼리를 직접 작성하므로 이름 없는 Named 쿼리라 할 수 있음.
    // 장점: JPA NAMED QUERY 쿼리처럼 애플리케이션 실행 시점에 문법 오류를 발견할 수 있다.
    // 실무에서 메소드 이름으로 쿼리 생성 기능은 파라미터가 증가하면 메서드 이름이 지저분해서 @Query를 자주 사용한다.
}
