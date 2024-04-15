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
            //팀 저장
            Team team = new Team();
            team.setName("A");
            em.persist(team);

            //회원 저장
            Mate mate = new Mate();
            mate.setName("ronaldo");
            mate.setTeam(team); // 단방향 연관관계 설정, 참조 저장
            em.persist(mate);

            //조회
            Mate findMate = em.find(Mate.class, mate.getId());

            //참조를 사용해서 연관관계 조회
            Team findTeam = findMate.getTeam();

            System.out.println("생성한 이름: " + mate.getName() + " 팀: " + team.getName());
            System.out.println("이름: " + findMate.getName() + " 팀: " + findMate.getTeam().getName());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
