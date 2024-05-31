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
import jakarta.persistence.criteria.CriteriaBuilder;
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
            Member member1 = new Member();
            member1.setUsername("엔소 페르난데스  ");// TRIM 실습을 위해 공백을 남겨둠
            member1.setAge(24);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("  페르난지뉴");
            member2.setAge(39);
            em.persist(member2);

            Team team1 = new Team();
            team1.setName("argentina");
            em.persist(team1);

            Team team2 = new Team();
            team2.setName("BRAZIL");
            em.persist(team2);

            em.flush();
            em.clear();

            //CONCAT
            String q1 = "SELECT CONCAT(m.username, ':', m.age) FROM Member m";
            List<String> stringList = em.createQuery(q1, String.class).getResultList();
            for(String s: stringList) {
                System.out.println("s = " + s);
            }

            //SUBSTR
            String q2 = "SELECT SUBSTR(m.username, 1,3)FROM Member m";
            List<String> stringList1 = em.createQuery(q2, String.class).getResultList();
            for(String s: stringList1) {
                System.out.println("s = " + s);
            }

            //TRIM
            String q3 = "SELECT TRIM(m.username) FROM Member m";
            List<String> stringList2 = em.createQuery(q3, String.class).getResultList();
            for(String s: stringList2) {
                System.out.println("s = " + s);
            }
            tx.commit();

            //UPPER,LOWER,LENGTH
            String q4 = "SELECT UPPER(t.name) FROM Team t";
            List<String> stringList3 = em.createQuery(q4, String.class).getResultList();
            for(String s: stringList3) {
                System.out.println("s = " + s);
            }

            String q5 = "SELECT LOWER(t.name) FROM Team t";
            List<String> stringList4 = em.createQuery(q5, String.class).getResultList();
            for(String s: stringList4) {
                System.out.println("s = " + s);
            }

            String q6 = "SELECT LENGTH(t.name) FROM Team t";
            List<Integer> stringList5 = em.createQuery(q6, Integer.class).getResultList();
            for(Integer s: stringList5) {
                System.out.println("s = " + s);
            }

            //LOCATE
            String q7 = "SELECT LOCATE('페', TRIM(m.name)) FROM Member m";
            List<Integer> stringList6 = em.createQuery(q7, Integer.class).getResultList();
            for(Integer s: stringList6) {
                System.out.println("s = " + s);
            }

          
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace(); // 예외 정보 출력 추가
        } finally {
            em.close();
        }

        emf.close();
    }
}
