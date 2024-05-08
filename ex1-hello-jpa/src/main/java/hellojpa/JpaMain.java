package hellojpa;

import hellojpa.domain.Album;
import hellojpa.domain.Book;
import hellojpa.domain.Member;
import hellojpa.domain.Movie;
import hellojpa.loading.Club;
import hellojpa.loading.Player;
import hellojpa.practice.Locker;
import hellojpa.practice.Mate;
import hellojpa.practice.Student;
import hellojpa.practice.Team;
import hellojpa.proxyAndRelationshipManage.Phone;
import hellojpa.superMapping.Seller;
import jakarta.persistence.*;
import org.hibernate.Hibernate;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        //code

        tx.begin();

        try {

            Player player1 = new Player();
            player1.setFirst_name("Cedric");

            Player player2 = new Player();
            player2.setFirst_name("Xhaka");

            em.persist(player1);
            em.persist(player2);

            Club club = new Club();
            club.setName("Arsenal");
            em.persist(club);

            player1.setClub(club);
            player2.setClub(club);

            Player findPlayer = em.find(Player.class, player1.getId());
            String findClub = findPlayer.getClub().getName(); // 지연 로딩: 엔티티에 접근하는 순간 로딩

            System.out.printf("player: %s, club: %s\n", findPlayer.getFirst_name(), findClub);


            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
