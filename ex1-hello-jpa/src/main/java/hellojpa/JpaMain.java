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
            Member member1 = new Member();
            member1.setUsername("짱구");
            member1.setAge(5);

            Member member2 = new Member();
            member2.setUsername("아따맘마");
            member2.setAge(40);

            Member member3 = new Member();
            member3.setUsername("루피");
            member3.setAge(23);

            Member member4 = new Member();
            Team team1 = new Team();
            team1.setName("japan");
            team1.addMember(member2);
            team1.addMember(member3);

            Team team2 = new Team();
            team2.setName("korea");
            team2.addMember(member1);

            Member[] members = {member1, member2, member3, member4};
            Team[] teams = {team1, team2};

            for(Member member : members) {
                em.persist(member);
            }

            for(Team team: teams) {
                em.persist(team);
            }

            em.flush();
            em.clear();

            String caseExpr1 =
                                "select " +
                                    "case when  10 <= m.age AND m.age <= 30 then '학생요금'" +
                                          "when m.age >= 31 then '성인요금'"  +
                                          "else '유아요금'" +
                                    "end from Member m";

            List<String> result = em.createQuery(caseExpr1, String.class).getResultList();
            for(String s: result) {
                System.out.println("s = " + s);
            }

            String caseExpr2 = "select " +
                                    "case t.name " +
                                    "when 'japan' then '일본만화' " +
                                    "when 'korea' then '한국만화' " +
                                    "else '출저불분명'" +
                                    "end from Team t";

            List<String> stringList = em.createQuery(caseExpr2, String.class).getResultList();
            for (String s: stringList) {
                System.out.println("s = " + s);
            }

            String caseExpr3 = "select coalesce(m.username, '이름이 없는 캐릭터') from Member m";
            List<String> stringList1 = em.createQuery(caseExpr3, String.class).getResultList();
            for(String s: stringList1) {
                System.out.println("s = " + s);
            }

            String caseExpr4 = "select NULLIF(m.username, '아따맘마') from Member m";
            List<String> stringList2 = em.createQuery(caseExpr4, String.class).getResultList();
            for(String s : stringList2) {
                System.out.println("s = " + s);
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
