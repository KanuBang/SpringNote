package hellojpa;

import hellojpa.cascadee.Attach;
import hellojpa.cascadee.FileType;
import hellojpa.cascadee.Post;
import hellojpa.domain.Album;
import hellojpa.domain.Book;
import hellojpa.domain.Movie;
import hellojpa.embeddedType.*;
import hellojpa.jpql.Address;
import hellojpa.jpql.Member;
import hellojpa.jpql.MemberDTO;
import hellojpa.jpql.Team;
import hellojpa.loading.Club;
import hellojpa.loading.Player;
import hellojpa.practice.Locker;
import hellojpa.practice.Mate;
import hellojpa.practice.Student;
import hellojpa.proxyAndRelationshipManage.Phone;
import hellojpa.superMapping.Seller;
import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;


public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        //code

        tx.begin();

        try {
            // Team 생성
            String[] teams = {"Arsenal", "Spurs", "Dortmund", "Bayern"};
            for(int i = 0; i < teams.length; i++) {
                Team t = new Team();
                t.setName(teams[i]);
                em.persist(t);
            }

            // Member 생성 - ARS
            String[] ars = {"Henry", "Xhaka", "Trossard", "Cech", "Giroud"};
            for (int i = 0; i < ars.length; i++) {
                Member m = new Member();
                m.setUsername(ars[i]);
                m.setTeam(em.find(Team.class, 1));
                em.persist(m);
            }

            // Member 생성 - SPURS
            String[] spurs = {"Son", "Kane", "Persic"};
            for(int i = 0; i < spurs.length; i++) {
                Member m = new Member();
                m.setUsername(spurs[i]);
                m.setTeam(em.find(Team.class, 2));
                em.persist(m);
            }

            // FA 선수들 - OUTER JOIN 용
            String[] FaPlayer = {"Mbappe", "varane"};
            for(int i = 0; i < FaPlayer.length; i++) {
                Member m = new Member();
                m.setUsername(FaPlayer[i]);
                em.persist(m);
            }

            em.flush();
            em.clear();

            String query1 = "SELECT m.username FROM Member m";
            TypedQuery<String> queryResult1 = em.createQuery(query1,String.class);
            List<String> usernames = queryResult1.setFirstResult(1).setMaxResults(3).getResultList();

            for (String username: usernames) {
                System.out.println(username);
            }

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace(); // 예외 정보 출력 추가
        } finally {
            em.close();
        }

        emf.close();
    }
}
