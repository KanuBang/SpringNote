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

            String query1 = "SELECT m FROM Member m LEFT OUTER JOIN m.team t";
            List<Member> members = em.createQuery(query1, Member.class).getResultList();

            System.out.println("==========query1==========");
            for (Member member : members) {
                if (member.getTeam() != null) {
                    System.out.printf("team: %s , name: %s\n", member.getTeam().getName(), member.getUsername());
                } else {
                    System.out.println("name: " + member.getUsername());
                }
            }
            System.out.println("==========query1==========");

            System.out.println("==========query2==========");
            String query2 = "SELECT t FROM Member m RIGHT OUTER JOIN m.team t";
            TypedQuery<Team> teamTypedQuery = em.createQuery(query2, Team.class);
            List<Team> teamTypedQueryResultList = teamTypedQuery.getResultList();
            for (Team team1 : teamTypedQueryResultList) {
                System.out.println("right outer join: " + team1.getName());
            }
            System.out.println("==========query2==========");

            System.out.println("==========query3==========");
            String query3 = "SELECT m FROM Member m, Team t WHERE m.team.id = t.id";
            TypedQuery<Member> memberTypedQuery = em.createQuery(query3, Member.class);
            List<Member> memberTypedQueryResultList = memberTypedQuery.getResultList();
            for(Member member: memberTypedQueryResultList) {
                System.out.println("세타 조인: " + member.getUsername());
            }
            System.out.println("==========query3==========");

            System.out.println("==========query4==========");
            String query4 = "SELECT m,t FROM Member m JOIN m.team t on t.name = 'ars'";
            TypedQuery<Object[]> leftjoin = em.createQuery(query4, Object[].class);
            List<Object[]> leftjoinResultList = leftjoin.getResultList();
            for (Object[] result : leftjoinResultList) {
                Member member1 = (Member) result[0];
                Team team1 = (Team) result[1];
                System.out.println("Member: " + member1.getUsername() + ", Team: " + team1.getName());
            }
            System.out.println("==========query4==========");

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
