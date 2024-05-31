package hellojpa;

import hellojpa.cascadee.Attach;
import hellojpa.cascadee.FileType;
import hellojpa.cascadee.Post;
import hellojpa.domain.Album;
import hellojpa.domain.Movie;
import hellojpa.embeddedType.*;
import hellojpa.fetchJoin.Author;
import hellojpa.fetchJoin.Book;
import hellojpa.jpql.Address;
import hellojpa.jpql.Member;
import hellojpa.jpql.MemberDTO;
import hellojpa.jpql.Team;
import hellojpa.loading.Club;
import hellojpa.loading.Player;
import hellojpa.practice.Locker;
import hellojpa.practice.Mate;
import hellojpa.practice.Student;
import hellojpa.proxyAndRelationshipManage.Phone;
import hellojpa.superMapping.Seller;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.hibernate.Hibernate;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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

            Book book2 = new Book();
            book2.setTitle("웨일스 여행");

            Book book3 = new Book();
            book3.setTitle("브리즈번 여행");

            Book book4 = new Book();
            book4.setTitle("멜버른 여행");

            Book book5 = new Book();
            book5.setTitle("시드니 여행");

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

            //fetchjoin을 쓰냐 안 쓰냐에 따라 쿼리문 나가느 게 달라짐.
            String normalQuery = "SELECT a FROM Author a";
            String fetchJoinQuery = "SELECT a FROM Author a JOIN FETCH a.books";
            List<Author> authorList = em.createQuery(fetchJoinQuery, Author.class).getResultList();

            for(Author a: authorList) {
                System.out.println(a.getName());
                //페치 조인은 객체 그래프를 SQL 한번에 조회하는 개념
                //페치 조인을 사용했다면 저자와 책을 함께 조회해서 지연 로딩 발생 안함.
                //일반 조인 실행시 연관된 엔티티를 함께 조회하지 않음. 그래서 프록시 이므로 이때 지연 로딩함.
                List<Book> tmp = a.getBooks();

                for(Book b: tmp){
                    System.out.println(b.getTitle());
                }
                System.out.println("\n");
            }

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
