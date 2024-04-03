package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    //회원가입
    @Transactional // 변경
    public Long join(Member member) {
        this.validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    //회원 전체 조회
    //동시에 같은 이름을 가입할 경우 문제가 발생할 수도. 그래서 최후의 방어로 DB에 MEMBER의 named을 UNIUQUE 제약조건을 걸기도 한다.

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()) {
            throw  new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //전체 회원 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
    //한 건 조회
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }


}
