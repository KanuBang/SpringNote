package hellojpa;

import hellojpa.domain.Member;
import hellojpa.practice.Locker;
import hellojpa.practice.Mate;
import hellojpa.practice.Student;
import hellojpa.practice.Team;
import hellojpa.superMapping.Movie;
import hellojpa.superMapping.Seller;
import jakarta.persistence.*;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        //code

        tx.begin();

        try {
            Seller seller = new Seller();
            seller.setName("Musk");
            seller.setShopName("tesla");
            em.persist(seller);

            em.flush();
            em.clear();

            Seller findSeller = em.find(Seller.class, seller.getId());
            System.out.println("Seller: " + findSeller.getName() + ", shop name: " + findSeller.getShopName());
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
