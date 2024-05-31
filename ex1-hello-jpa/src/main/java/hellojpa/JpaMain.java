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
            String[] ars = {"Henry", "Cech", "Giroud", "Xhaka", "Trossard"};
            for (int i = 0; i < ars.length; i++) {
                Member m = new Member();
                m.setUsername(ars[i]);
                m.setTeam(em.find(Team.class, 1));
                m.setAge(50 - i * 5);
                em.persist(m);
            }

            // Member 생성 - SPURS
            String[] spurs = {"Persic", "Son", "Kane"};
            for(int i = 0; i < spurs.length; i++) {
                Member m = new Member();
                m.setUsername(spurs[i]);
                m.setAge(35 - i * 3);
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

            //inner join
            String query1 = "SELECT m.username FROM Member m INNER JOIN m.team t";
            TypedQuery<String> queryResult1 = em.createQuery(query1,String.class);
            List<String> usernames = queryResult1.getResultList();

            for (String username: usernames) {
                System.out.println(username);
            }

            //OUTER JOIN
            String query2 = "SELECT m.username FROM Member m LEFT OUTER JOIN m.team t";
            TypedQuery<String> queryResult2 = em.createQuery(query2, String.class);
            List<String> queryResult2ResultList = queryResult2.getResultList();
            for(String s : queryResult2ResultList) {
                System.out.println(s);
            }

            //Theta Join
            String query3 = "SELECT DISTINCT t.name FROM Member m, Team t WHERE m.team.id = t.id";
            TypedQuery<String> queryResult3 = em.createQuery(query3, String.class);
            List<String> queryResult3ResultList = queryResult3.getResultList();
            for(String s: queryResult3ResultList) {
                System.out.println(s);
            }

            //subquery1
            String query4 = "SELECT m.username, m.age FROM Member m WHERE m.team.id = 1 AND m.age > (SELECT avg(m1.age) FROM Member m1 WHERE m1.team.id = 1)";
            TypedQuery<Object[]> queryResult4 = em.createQuery(query4, Object[].class);
            List<Object[]> queryResult4ResultList = queryResult4.getResultList();
            for(Object[] o : queryResult4ResultList) {
                System.out.printf("username: %s, age: %d\n", o[0], (int)o[1]);
            }

            //subquery2
            String query5 = "SELECT m.username, m.team.name FROM Member m WHERE exists (SELECT t FROM m.team t WHERE t.id = 1L)";
            TypedQuery<Object[]> queryResult5 = em.createQuery(query5, Object[].class);
            List<Object[]> queryResult5ResultList = queryResult5.getResultList();
            for (Object[] o : queryResult5ResultList){
                System.out.printf("username: %s, team: %s\n", o[0], o[1]);
            }
            //subquery3
            String query6 = "SELECT m.username, m.team.name FROM Member m WHERE m.team  = ANY (SELECT t FROM Team t)";
            TypedQuery<Object[]> queryResult6 = em.createQuery(query6, Object[].class);
            List<Object[]> queryResult6ResultList = queryResult6.getResultList();
            for (Object[] o : queryResult6ResultList){
                System.out.printf("username: %s, team: %s\n", o[0], o[1]);
            }

            //JPQL String type
            String query7 = "SELECT m FROM Member m WHERE m.team.name = 'Arsenal' ";
            TypedQuery<Object[]> queryResult7 = em.createQuery(query7, Object[].class);
            List<Object[]> queryResult7ResultList = queryResult7.getResultList();
            for(Object[] o : queryResult7ResultList) {
                System.out.println(o);
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
