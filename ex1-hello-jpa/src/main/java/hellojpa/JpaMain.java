package hellojpa;

import hellojpa.cascadee.Attach;
import hellojpa.cascadee.FileType;
import hellojpa.cascadee.Post;
import hellojpa.domain.Album;
import hellojpa.domain.Book;
import hellojpa.domain.Movie;
import hellojpa.embeddedType.*;
import hellojpa.jpql.Member;
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
            member1.setUsername("bergcamp");
            member1.setAge(60);
            em.persist(member1);
            
            Member member2 = new Member();
            member2.setUsername("cedric");
            member2.setAge(60);
            em.persist(member2);

            Team team1 = new Team();
            team1.setName("Gunners");
            em.persist(team1);

            //TypeQuery 반환 타입이 명확할 때 사용한다.
            TypedQuery<Team> query0 = em.createQuery("select t from Team t", Team.class);
            //getSingleResult 결과가 정확히 하나, 단일 객체 반환 -> 결과가 없으면 NoResultException, 둘 이상이면 NonUniqueResultException
            Team team = query0.getSingleResult();
            System.out.println("========query0=========");
            System.out.println(team.getName());
            System.out.println("========query0=========");

            //TypeQuery 반환 타입이 명확할 때 사용한다.
            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
            List<Member> members = query1.getResultList(); // 결과가 하나 이상일 때, 리스트 반환 OR 결과가 없다면 빈 리스트 반환
            System.out.println("========query1=========");
            for(Member member: members) {
                System.out.printf("q1: %s is %d\n", member.getUsername(), member.getAge());
            }
            System.out.println("========query1=========");

            TypedQuery<String> query2 = em.createQuery("select m.username from Member m ", String.class);
            List<String> usernames = query2.getResultList();
            System.out.println("========query2=========");
            for(String username: usernames) {
                System.out.printf("q2: name is %s\n", username);
            }
            System.out.println("========query2=========");


            // Query 반환 타입이 명확하지 않을 때 사용한다.
            Query query3 = em.createQuery("select m.username, m.age from Member m");
            List<Object[]> results = query3.getResultList();

            for(Object[] result: results) {
                String username = (String)result[0];
                Integer age = (Integer) result[1];
                System.out.println(username + " is " + age + " years old");
            }

            //파라미터 바인딩 -> 위치 기준도 있지만 왠만해서는 이름 기준을 사용하자
            Member member3 = new Member();
            member3.setUsername("xhaka");
            member3.setAge(32);
            em.persist(member3);

            String userNamePara = "xhaka";
            TypedQuery<Member> query4 = em.createQuery("select m from Member m where m.username =: username", Member.class)
                            .setParameter("username", userNamePara);

            List<Member> targetMember = query4.getResultList();

            System.out.println("========query4=========");
            System.out.println("targetMember is " + targetMember.get(0).getUsername());
            System.out.println("========query4=========");

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
