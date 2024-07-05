package study.datajpa.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import java.util.List;

// MemberRepository에서 중복되는 메서드를 따로 모아두기 위해서 만듬.
public interface TmpMemberRepository extends JpaRepository<Member, Long> {
    // 메서드 이름으로 쿼리에서 특히 편리하다.
    @EntityGraph(attributePaths = {"team"})
    List<Member> findByUsername(@Param("username") String username);
}
