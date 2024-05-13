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

            Member member1 = new Member();
            member1.setUsername("rice");
            member1.setAddress(new Address("london", "north", "17002-1"));
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("henry");
            member2.setAddress(new Address("london", "north", "17002-2"));
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("antony");
            member3.setAddress(new Address("manchester", "old", "88332-1"));
            em.persist(member3);

            Team team1 = new Team();
            team1.setName("ARS");
            team1.addMember(member1);
            team1.addMember(member2);
            em.persist(team1);

            Team team2 = new Team();
            team2.setName("MU");
            team2.addMember(member3);
            em.persist(team2);


            em.flush(); //쿼리 DB에 전송
            em.clear(); //엔티티 영속성 해제

            //엔티티 프로젝션 -> 프로젝션 되는 엔티티들 모두 영속성 컨텍스트에 저장된다.
            TypedQuery<Member> member = em.createQuery("select m from Member m", Member.class);
            List<Member> memberResultList = member.getResultList();
            System.out.println("======action1======");

            for(Member m: memberResultList) {
                //LAZY fetch
                System.out.println(m.getUsername() +" " + m.getTeam().getName());
            }
            System.out.println("======action1======");

            //엔티티 프로젝션
            TypedQuery<String> username = em.createQuery("select m.username from Member m", String.class);
            List<String> usernameResultList = username.getResultList();
            System.out.println("======action2======");
            for(String n: usernameResultList) {
                System.out.println("name is " + n);
            }
            System.out.println("======action2======");

            //임베디드 타입 프로젝션
            String myName = "rice";
            TypedQuery<Address> address = em.createQuery("select m.address from Member m where m.username =: username ", Address.class).setParameter("username", myName);
            Address addressSingleResult = address.getSingleResult();
            System.out.println("======action3======");
            System.out.println(addressSingleResult.getCity() + " " +addressSingleResult.getStreet() +" " + addressSingleResult.getZipcode());
            System.out.println("======action3======");


            // 멤버의 username과 age를 조회
            TypedQuery<Object[]> query = em.createQuery(
                    "SELECT m.username, m.age FROM Member m", Object[].class);
            List<Object[]> resultList = query.getResultList();
            // 결과 출력
            System.out.println("======action4======");
            if (!resultList.isEmpty()) {
                Object[] result = resultList.get(0);
                System.out.println("Username = " + result[0]);
                System.out.println("Age = " + result[1]);
            }
            System.out.println("======action4======");


            //스칼라 타입 프로젝션, new 명령어로 조회, 순서와 타입이 일치하는 생성자 필요, 단순값을 DTO로 바로 조회
            String myTeam = "MU";
            TypedQuery<MemberDTO> result = em.createQuery("select new MemberDTO(m.username, m.age) from Member m where m.team.name = :myTeam", MemberDTO.class)
                    .setParameter("myTeam", myTeam);

            // 결과 출력
            MemberDTO memberDTO = result.getResultList().get(0);
            System.out.println("======action5======");
            System.out.println("MemberDTO = " + memberDTO.getUsername());
            System.out.println("MemberDTO = " + memberDTO.getAge());
            System.out.println("======action5======");



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
