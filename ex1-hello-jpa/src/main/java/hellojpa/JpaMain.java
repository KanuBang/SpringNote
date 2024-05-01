package hellojpa;

import hellojpa.domain.Member;
import hellojpa.domain.Order;
import hellojpa.domain.OrderItem;
import hellojpa.practice.Email;
import hellojpa.practice.Employee;
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

            Email gmail = new Email();
            em.persist(gmail);

            Employee employee = new Employee();
            employee.getEmails().add(gmail);
            gmail.setEmployee(employee);
            em.persist(employee);
           tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
