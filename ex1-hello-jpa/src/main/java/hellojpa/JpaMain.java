package hellojpa;

import hellojpa.domain.Album;
import hellojpa.domain.Book;
import hellojpa.domain.Member;
import hellojpa.domain.Movie;
import hellojpa.practice.Locker;
import hellojpa.practice.Mate;
import hellojpa.practice.Student;
import hellojpa.practice.Team;
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
            Album[] album = new Album[3];
            for(int i = 0; i < 3; i++) {
                album[i] = new Album();
                album[i].setName("bubble-gum" + i);
                album[i].setPrice(30000);
                album[i].setStackQuantity(0);
                album[i].setArtist("newjeans" + i);
                album[i].setEtc("ador" + i);
                em.persist(album[i]);
            }

            Book[] books = new Book[3];
            for(int i = 0; i < 3; i++) {
                books[i] = new Book();
                books[i].setAuthor("a" + i);
                books[i].setName("name" + i);
                books[i].setStackQuantity(3 + i + 1);
                books[i].setPrice(3000);
                books[i].setIsbn("432432-2342-234234324");
                em.persist(books[i]);
            }

            em.flush();
            em.clear();

            Album findAlbum = em.find(Album.class, album[0].getId());
            Book findBook = em.find(Book.class, books[0].getId());
            System.out.println(findAlbum.getName());
            System.out.println(findBook.getName());

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
