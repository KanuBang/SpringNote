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
            
            Member member1 = em.find(Member.class, 1L);
            System.out.println("================");
            System.out.println(member1.getName());

            // UPDATE
            member1.setName("ronaldo");
            System.out.println("================");
            System.out.println(member1.getName());

            tx.commit(); // commit -> dirty checking -> flush

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
