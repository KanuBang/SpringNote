package hellojpa;

import hellojpa.practice.Mate;
import hellojpa.practice.Team;
import jakarta.persistence.*;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        //code

        tx.begin();

        try {
            Team team = new Team();
            team.setName("izone");
            em.persist(team);

            Mate mate = new Mate();
            mate.setUsername("sakura");
            em.persist(mate);

            team.addMate(mate);


           tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
