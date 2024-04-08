package chamwu.jpabasic;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JpabasicApplication {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		tx.begin();

		Member member = new Member();
		member.setId(1L);
		member.setName("chanwu");
		em.persist(member);
		tx.commit();
		em.close();
		emf.close();
		SpringApplication.run(JpabasicApplication.class, args);
	}

}
