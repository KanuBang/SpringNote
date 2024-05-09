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

            //엔티티 생성
            Guest guest = new Guest();
            guest.setUsername("chan-wu");
            guest.setHomeAddress(new Address("homeCity", "street", "10000"));

            //음식 추가
            guest.getFavoriteFoods().add("치킨");
            guest.getFavoriteFoods().add("피자");
            guest.getFavoriteFoods().add("파스타");
            System.out.println(guest.getFavoriteFoods().toString());

            //주소 기록 목록 추가
            guest.getAddressHistory().add(new Address("old1", "Street1", "20000"));
            guest.getAddressHistory().add(new Address("old2", "Street2", "30000"));
            System.out.println(guest.getAddressHistory().toString());

            // Member 엔티티를 영속성 컨텍스트에 저장 -> Cascade.persist 처럼 작용
            em.persist(guest); // 값 타입 컬렉션도 Cascade.persist AND Cascade.remove 처럼 작동한다.

            em.flush();
            em.clear();

            // 이때, 값 타입 컬렉션으로 끌고 오지는 않음.(지연로딩)
            // 값 타인 컬렉션을 즉시 로딩으로 설정하면 실제로 Guest 조회할 때 한 거 번에 데이터 끌고 오는 쿼리 문을 확인할 수 있음.

            Guest findGuest = em.find(Guest.class, guest.getId());
            //getFavoriteFoods()를 호출하는 시점에 데이터베이스에서 해당 데이터를 로드하는 지연 로딩이 발생합니다.
            List<String> findFoods = findGuest.getFavoriteFoods();

            for(String food: findFoods) {
                System.out.println("food: " + food);
            }

            List<Address> findAddress = findGuest.getAddressHistory();
            for(Address address : findAddress) {
                System.out.printf("city: %s, street: %s, zipcode: %s\n", address.getCity(), address.getStreet(), address.getZipcode());
            }

            // 값 타입 컬렉션을 수정할 때는 왼전히 새로 -> 값 타입 공유 참조를 피하기 위해
            findGuest.setHomeAddress(new Address("yong-in","평촌로","17002"));

            // 치킨 -> 한식
            findGuest.getFavoriteFoods().remove("치킨");
            findGuest.getFavoriteFoods().add("한식");

            // 새로운 주소
            findGuest.getAddressHistory().remove(new Address("old1", "Street1", "20000"));
            findGuest.getAddressHistory().add(new Address("new1", "new_street1", "999"));
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
