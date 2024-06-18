package book.jpaShopReview.repository;

import book.jpaShopReview.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

// 데이터 엑세스 계층을 정의하는 Annotation
// Spring의 컴포넌트 스캔 대상이 되어 스프링 빈으로 동륵되어 관리되고
// JPA의 EntityManager가 던지는 특정 예외를 Spring의 DataAccessException으로 변환한다.
@Repository
public class MemberRepository {
    // JPA의 EntityManger을 주입하기 위한 Annotation, 필드 주입
    // 테스트 시 반드시 스프링이 필요하기에 테스트가 어려움
    @PersistenceContext
    private EntityManager em;

    //Member를 저장
    public void save(Member member){
        em.persist(member);
    }

    //id로 Member조회 후 객체 반환
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    //모든 Member 반환
    public List<Member> findAll() {
        return em.createQuery("SELECT m FROM Member m", Member.class).getResultList();
    }

    //이름으로 Member 조회 (동명이인 고려)
    public List<Member> findByName(String name) {
        return em.createQuery("SELECT m FROM Member m WHERE m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
