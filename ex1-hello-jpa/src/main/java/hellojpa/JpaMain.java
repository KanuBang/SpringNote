package hellojpa;

import hellojpa.bulkOperation.Author;
import hellojpa.bulkOperation.Book;
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
            Author author1 = new Author();
            author1.setName("샘");

            Author author2 = new Author();
            author2.setName("이사벨라");

            Author[] authors = {author1,author2};

            for(Author a : authors) {
                em.persist(a);
            }

            Book book1 = new Book();
            book1.setTitle("잉글랜드 여행");
            book1.setFollwer(10L);

            Book book2 = new Book();
            book2.setTitle("웨일스 여행");
            book2.setFollwer(20L);

            Book book3 = new Book();
            book3.setTitle("브리즈번 여행");
            book3.setFollwer(5L);

            Book book4 = new Book();
            book4.setTitle("멜버른 여행");
            book4.setFollwer(15L);

            Book book5 = new Book();
            book5.setTitle("시드니 여행");
            book5.setFollwer(25L);

            author1.addBook(book1);
            author1.addBook(book2);

            author2.addBook(book3);
            author2.addBook(book4);
            author2.addBook(book5);

            Book[] books = {book1, book2, book3, book4, book5};

            for(Book b: books) {

                em.persist(b);
            }

            em.flush();
            em.clear();

            // bulk operation is used to delete or update rows
            String bulkSql1 = "UPDATE Book b set b.follwer = b.follwer * 2 WHERE b.follwer >= 15";

            /*
                When you call executeUpdate() for a JPQL or native SQL query,
                a flush operation does occur automatically.
             */
            int count = em.createQuery(bulkSql1).executeUpdate(); // it will return the number of rows updated.
            System.out.println("updated: " + count);

            // after bulk operation was called, flush() occured.
            // So, DB status will be different from Persistence Context
            System.out.println("book5 follwer: " + book5.getFollwer());

            // Therefore, we have to synchronize each status manually
            // 벌크 연산 수행 후 영속성 컨텍스트 초기화
            em.clear();
            Book findBook = em.find(Book.class, book5.getId());
            System.out.println("book5 follwer: " + findBook.getFollwer());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace(); // 예외 정보 출력 추가
        } finally {
            em.close();
        }

        emf.close();
    }
}
