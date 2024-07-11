package study.querydsl.repository;


import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    EntityManager em;
    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void basicTest() {
        Member member = new Member("member1", 10);
        memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.findById(member.getId()).get();
        assertThat(findMember).isEqualTo(member);

        List<Member> result = memberJpaRepository.findAll();
        assertThat(result).contains(member);

        List<Member> result2 = memberJpaRepository.findByUsername("member1");
        assertThat(result).contains(member);
    }

    @Test
    public void queryDslRepoTest() {
        Member member = new Member("member1", 10);
        memberJpaRepository.save(member);

        List<Member> result = memberJpaRepository.findAll_queryDSL();
        assertThat(result).contains(member);

        List<Member> result1 = memberJpaRepository.findByUsername_queryDSL("member1");
        assertThat(result1).contains(member);
    }
}