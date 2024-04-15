package hellojpa;

import hellojpa.domain.Member;
import hellojpa.domain.Order;
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

            Mate mate1 = new Mate();
            mate1.setName("son");
            Mate mate2 = new Mate();
            mate2.setName("romero");

            Team team1 = new Team();
            team1.setName("spurs");

            mate1.setTeam(team1);
            mate2.setTeam(team1);
            team1.getMates().add(mate1);

            em.persist(mate1);
            em.persist(mate2);
            em.persist(team1);

            tx.commit();

            //커밋한 후에 봐야 한다.
            //역방향 참조 (onetomany)
            Team findTeam = em.find(Team.class, team1.getId());
            System.out.println(findTeam.getMates());
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
