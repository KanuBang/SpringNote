package book.jpaShopReview.repository;

import book.jpaShopReview.domain.Item;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    // @RequiredArgsConstructor는 Lombok 라이브러리에 의해 제공되는 생성자 주입이다.
    // 불변성,명확한 의존성,테스트 용이성 장점이 있다.
    // 테스트 시 스프링이 반드시 필요한 필드 주입과 달리 테스트가 간편하다.
    private final EntityManager em;

    public void save(Item item) {
        if(item.getId() == null) {
            em.persist(item);
        } else {
            em.merge(item);
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("SELECT i FROM Item i", Item.class).getResultList();
    }
}
