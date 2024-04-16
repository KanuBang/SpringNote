package hellojpa;

import hellojpa.domain.Member;
import hellojpa.domain.Order;
import hellojpa.practice.Mate;
import hellojpa.practice.Team;
import jakarta.persistence.*;

import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        //code

        tx.begin();

        try {

            Mate mate1 = new Mate();
            mate1.setName("grimaldo");
            Mate mate2 = new Mate();
            mate2.setName("xhaka");

            Team team1 = new Team();
            team1.setName("bayer04");

            mate1.setTeam(team1);
            mate2.setTeam(team1);

            // 쓰기 지연 저장소에 INSERT 쿼리가 저장됨
            em.persist(mate1);
            em.persist(mate2);

            // 연관 관계 편의 메서드: 순수 객체 상태를 고려해서 양쪽에 값을 설정
            team1.addMate(mate1);
            team1.addMate(mate2);
            em.persist(team1);

            // 1차 캐시에 없으므로 DB에서 조회 -> 1차 캐시에 정보 저장
            Team findTeam = em.find(Team.class, team1.getId());

            // 역방향 참조
            List<Mate> mates = findTeam.getMates();
            System.out.println("searchsearchsearchsearchsearchsearchsearchsearch");
            for(Mate m : mates) {
                System.out.println("m = " + m.getName());
            }

            System.out.println("commitcommitcommitcommitcommitcommitcommitcommit");
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
