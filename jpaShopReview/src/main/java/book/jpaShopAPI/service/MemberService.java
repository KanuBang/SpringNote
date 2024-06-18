package book.jpaShopAPI.service;

import book.jpaShopAPI.domain.Member;
import book.jpaShopAPI.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // 스프링 빈에 등록되고 비즈니스 로직을 작성할 것이고 스프링 계층임을 명시
@Transactional(readOnly = true) // 트랜잭션 관리, 읽기 전용
@RequiredArgsConstructor // 생성자 주입, final이 붙은 멤버에 객체를 생성자 주입
// 별도의 생성자 코드를 작성할 필요 없어 편리
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional // 메서드 단위로 @Transactional 이용 가능, 읽기 / 쓰기 모두 가능
    public Long join(Member member) {
        this.validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    // 중복 회원 가입 예외 처리
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());

        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 모든 member 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // member의 id로 member 조회
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
