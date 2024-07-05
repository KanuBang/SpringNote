package study.datajpa.customeRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Member;

public interface MemberRepository1 extends JpaRepository<Member, Long>, MemberRepositoryCustom {
}
