package hellojpa;

import hellojpa.practice.Locker;
import hellojpa.practice.Mate;
import hellojpa.practice.Student;
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

            Locker locker = new Locker();
            locker.setName("cs");
            em.persist(locker);

            Student student = new Student();
            student.setUsername("messi");
            student.addLocker(locker);
            em.persist(student);

            System.out.println(locker.getStudent().getUsername());
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
