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
            String[] players = {"rice", "saka", "raya", "doku", "ederson", "dias", "son", "romero", "porro", "lukaku", "virtz"};
            String[] teams = {"ars", "mc", "tot", "liv", "mu"};
            Team[] team = new Team[teams.length];
            int idx = 0;

            for (int i = 0; i < teams.length; i++) {
                team[i] = new Team();
                team[i].setName(teams[i]);
                em.persist(team[i]);
            }

            for (int i = 0; i < players.length; i++) {
                Member member = new Member();
                member.setUsername(players[i]);
                member.setAge(i);

                idx = i / 3;
                if (idx <= 2) {
                    member.setTeam(team[idx]);
                }
                em.persist(member);
            }

            em.flush(); //쿼리 DB에 전송
            em.clear(); //엔티티 영속성 해제

            String query1 = "SELECT m from Member m WHERE m.age > (SELECT AVG(m2.age) FROM Member m2)";
            TypedQuery<Member> memberTypedQuery = em.createQuery(query1, Member.class);
            List<Member> memberTypedQueryResultList = memberTypedQuery.getResultList();
            for(Member member: memberTypedQueryResultList) {
                System.out.println("평균 보다 나이가 많은 사람들: "  + member.getUsername());
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
