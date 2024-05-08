package hellojpa;

import hellojpa.cascadee.Attach;
import hellojpa.cascadee.FileType;
import hellojpa.cascadee.Post;
import hellojpa.domain.Album;
import hellojpa.domain.Book;
import hellojpa.domain.Member;
import hellojpa.domain.Movie;
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

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        //code

        tx.begin();

        try {

            Post post = new Post();
            post.setName("europe travel");

            Attach video = new Attach();
            video.setAttach_type(FileType.VIDEO);
            video.setName("in amsterdam-arena");

            Attach image1 = new Attach();
            image1.setAttach_type(FileType.IMAGE);
            image1.setName("amsteradam");

            Attach image2 = new Attach();
            image2.setAttach_type(FileType.IMAGE);
            image2.setName("paris");

            Attach text = new Attach();
            text.setAttach_type(FileType.TEXT);
            text.setName("myFirstEuTrip");

            post.addAttach(video);
            post.addAttach(image1);
            post.addAttach(image2);
            post.addAttach(text);

            System.out.println("영속성 전이 시작");

            //Cascade.PERSIST
            em.persist(post);

            Attach findText = em.find(Attach.class, text.getId());
            System.out.println("++++++++++++++++++++++++++++++++++");
            System.out.println("found Text: " + findText.getName());
            System.out.println("++++++++++++++++++++++++++++++++++");

            //고아 객체 제거: 부모 엔티티와 연관관계가 끊어진 자식 엔티티 를 자동으로 삭제

            Post findPost = em.find(Post.class, post.getId());
            //em.remove(findPost);
            findPost.getAttachList().remove(1);

            tx.commit();



        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
