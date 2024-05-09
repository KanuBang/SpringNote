package hellojpa;

import hellojpa.cascadee.Attach;
import hellojpa.cascadee.FileType;
import hellojpa.cascadee.Post;
import hellojpa.domain.Album;
import hellojpa.domain.Book;
import hellojpa.domain.Movie;
import hellojpa.embeddedType.*;
import hellojpa.loading.Club;
import hellojpa.loading.Player;
import hellojpa.practice.Locker;
import hellojpa.practice.Mate;
import hellojpa.practice.Student;
import hellojpa.practice.Team;
import hellojpa.proxyAndRelationshipManage.Phone;
import hellojpa.superMapping.Seller;
import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.util.Date;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        //code

        tx.begin();

        try {
            Date date = new Date();

            // 일반 Embedded 연습
            Period period = new Period();
            period.setStartDate(date);
            period.setEndDate(date);

            Address homeAddress = new Address();
            homeAddress.setCity("home");
            homeAddress.setStreet("home park");
            homeAddress.setZipcode("home17002");

            Member member = new Member();
            member.setName("marco reus");
            member.setPeriod(period);
            member.setAddress(homeAddress);

            em.persist(member);

            // @AttributeOverride와 @AttributeOverrides
            Shipment shipment = new Shipment();

            Address workAddress = new Address();
            workAddress.setCity("work");
            workAddress.setStreet("work park");
            workAddress.setZipcode("work17002");

            shipment.setHomeAddress(homeAddress);
            shipment.setWorkAddress(workAddress);
            em.persist(shipment);

            //값 비교
            TestEntity testEntity1 = new TestEntity(1L, "testEntity1");
            TestEntity testEntity2 = new TestEntity(1L, "testEntity1");
            System.out.println("testEntity1 == testEntity2: " + (testEntity1 == testEntity2));
            System.out.println("testEntity1.equals(testEntity2): " + testEntity1.equals(testEntity2));


            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
