package hellojpa;

import hellojpa.practice.Locker;
import hellojpa.practice.Mate;
import hellojpa.practice.Student;
import hellojpa.practice.Team;
import hellojpa.superMapping.Movie;
import jakarta.persistence.*;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        //code

        tx.begin();

        try {
            Movie movie = new Movie();
            movie.setDirector("uuu");
            movie.setActor("chris");
            movie.setName("Avengers");
            movie.setPrice(10000);
            em.persist(movie);

            em.flush();
            em.clear();

            Movie findMovie = em.find(Movie.class, movie.getId());
            System.out.println("Movie: " + findMovie.getName() + ", Director: " + findMovie.getDirector());
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
