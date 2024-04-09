package hellojpa;

import jakarta.persistence.*;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        //code

        tx.begin();

        try {
            // 비영속
            Member member = new Member();
            member.setId(1L);
            member.setName("chanwu");

            // 영속-> 영속성 컨텍스트에 엔티티를 저장 (1차 캐시)
            // persist -> insert sql -> 쓰기 지연 sql 저장소에 저장
            // 1차 캐시: @id: Entity
            em.persist(member);

            // 1차 캐시에서 엔티티 조회
            // 1차 캐시에서 조회되지 않는다면 DB 조회 -> DB에서 조회되면 1차 캐시에 저장
            Member findMember1 = em.find(Member.class, 1L);
            Member same = em.find(Member.class, 1L);
            System.out.println("=================================" );
            System.out.println(" I found in the cache: " + findMember1.getName());

            // 동일성 보장
            System.out.println(" same?: " + (findMember1 == same));

            // commmit -> flush() -> dirty checking
            tx.commit(); // 이때 쿼리가 날라간다.

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
