package hellojpa;

import hellojpa.domain.Album;
import hellojpa.domain.Book;
import hellojpa.domain.Member;
import hellojpa.domain.Movie;
import hellojpa.practice.Locker;
import hellojpa.practice.Mate;
import hellojpa.practice.Student;
import hellojpa.practice.Team;
import hellojpa.proxyAndRelationshipManage.Phone;
import hellojpa.superMapping.Seller;
import jakarta.persistence.*;
import org.hibernate.Hibernate;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        //code

        tx.begin();

        try {
            Phone phone = new Phone();
            phone.setName("ipad-air-3");
            em.persist(phone);

            em.flush();
            em.clear();

            Phone refPhone = em.getReference(Phone.class, phone.getId());
            boolean isloaded = em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(refPhone);

            System.out.println("is Loaded? " + isloaded);
            System.out.println("class: " + refPhone.getClass().getName());

            // 일반적으로 프록시 객체의 속성에 접근하면 자동으로 초기화됩니다.
            String name = phone.getName();
            System.out.println("phone name: " + name);
            System.out.println("class: " + refPhone.getClass().getName());

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
