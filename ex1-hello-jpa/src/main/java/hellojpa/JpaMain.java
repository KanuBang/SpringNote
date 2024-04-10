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

           Member member = new Member();
           member.setRoleType(RoleType.USER);
           System.out.println("==================");
           em.persist(member);
           System.out.println("IDENTITY 전략은 이때 바로 INSERT SQL을 DB에 전송하고 Id 값을 1차 캐시에 사용한다.");
           Member found = em.find(Member.class, member.getId());
           System.out.println("same???: " + (member == found));
           System.out.println("==================");

           tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
