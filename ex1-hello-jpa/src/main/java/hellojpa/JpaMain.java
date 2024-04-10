package hellojpa;

import hellojpa.domain.Member;
import hellojpa.domain.Order;
import jakarta.persistence.*;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        //code

        tx.begin();

        try {

            Member member = new Member();
            Order order = new Order();

            // 영속성 컨텍스트에 저장 -> 1차 캐시
            em.persist(member);
            em.persist(order);

            // 1차 캐시에서 값 조회
            order.setMemberId(member.getId());

            tx.commit();

            System.out.println("order에 있는 memberId와 entity manager를 이용해 같은 member가 조회되는지 혹인하기");
            Member sameMember = em.find(Member.class, order.getMemberId());
            System.out.println("same member??: "  + (member == sameMember));

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
