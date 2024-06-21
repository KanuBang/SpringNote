package book.jpaShopAPI.repository;

import book.jpaShopAPI.domain.Order;
import book.jpaShopAPI.domain.OrderSearch;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

// 주문 리포지토리 개발
@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em; //엔티티 매니저

    // 영속성 컨텍스트 저장
    public void save(Order order) {
        em.persist(order);
    }

    // 영속성 컨텍스트에서 id로 Order을 찾아 리턴
    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery("select o from Order o" +
                " join fetch o.member m" +
                " join fetch o.delivery d", Order.class).getResultList();
    }


    public List<Order> findAllByString(OrderSearch orderSearch) {

        //language=JPQL
        String jpql = "select o From Order o join o.member m";
        boolean isFirstCondition = true;

        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }

        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }
        TypedQuery<Order> query = em.createQuery(jpql, Order.class).setMaxResults(1000); //최대 1000건
        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();
    }
}

