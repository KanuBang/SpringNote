package study.datajpa.controller;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberDto;
import study.datajpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final EntityManager em;

    // 도메인 클래스 컨버터
    /*
    @GetMapping("/members/{fake_id}")
    public String findMember(@PathVariable("fake_id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }
     */

    // 도메인 클래스 컨버터 사용후
    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    /* 기본 폼 - DTO 사용전
    @GetMapping("/members")
    public Page<Member> list(Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);
        return page;
    }
    */

    // Page 내용을 DTO로 변환하기
    @GetMapping("/members")
    public Page<MemberDto> list(Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);
        Page<MemberDto> pageDto = page.map(m -> new MemberDto(m.getId(),m.getUsername()));
        return pageDto;
    }
    @PostConstruct
    public void init() {
        for(int i = 1; i < 51; i++) {
            memberRepository.save(new Member("member"+i));
        }
    }
}
