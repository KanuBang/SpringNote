package hello.core.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberServiceImpl implements MemberService {
    private MemberRepository memberRepository;
    //테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }

    @Autowired
    public MemberServiceImpl(MemberRepository memoryMemberRepository) {
        this.memberRepository = memoryMemberRepository;
    }

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
