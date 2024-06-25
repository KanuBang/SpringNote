package book.jpaShopAPI.repository.order.query;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    private final EntityManager em;

    /*
    컬렉션은 별도로 조회
    Query: 루트 1번, 컬렉션 N번
    단건 조회에서 많이 사용하는 방식
     */

    public List<OrderQueryDto> findOrderQueryDtos() {
        //루트 조회(toOne 코드를 모두 한번에 조회한다.)
        List<OrderQueryDto> result = findOrders();

        //루프를 돌면서 컬렉션 추가(추가 퀴리 시행)
        result.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });

        return result;
    }

    // 1:N 관계(컬렉션)을 제외한 나머지를 한번에 조회
    public List<OrderQueryDto> findOrders() {
        // ToOne
        // fetch 조인을 할 필요 없는 이유는 DTO로 조회하기 때문이다.
        // 필요한 것만 조회하고 싶기 때문에 굳이 모든 것을 fetch로 끌어올 필요가 없다.
        return em.createQuery("select new book.jpaShopAPI.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                " from Order o" +
                " join o.member m" +
                " join o.delivery d", OrderQueryDto.class
        ).getResultList();
    }

    // 1:N 관계인 orderItems 조회
    public List<OrderItemQueryDto> findOrderItems(Long orderId) {
        // ToOne
        return em.createQuery(
                "select new book.jpaShopAPI.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                    " from OrderItem oi" +
                    " join oi.item i" +
                    " where oi.order.id = :orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    // 최적화
    // Query: 루트 1번, 컬렉션 1번
    // 데이터를 한 꺼번에 처리할 때 많이 사용하는 방식
    public List<OrderQueryDto> findAllByDto_optimization() {

        //루트 조회 (toOne 코드를 모두 한번에 조회) -> 주문 쿼리 1개로 모두 조회
        List<OrderQueryDto> result = findOrders();

        //orderItem 컬렉션을 MAP 한방에 조회 -> 주문 상품 쿼리 1개로 모두 조회 -> Map으로 저장 <주문ID, 주문상품DTO>
        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(toOrderIds(result));

        //주문 리스트를 루프로 각 주문에 주문 상품 리스트를 초기화
        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));
        return result;
    }

    private List<Long> toOrderIds(List<OrderQueryDto> result) {
        return result.stream().map(o -> o.getOrderId()).collect(Collectors.toList());
    }

    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {

        // 주문에 있는 주문 상품 모두 조회
        List<OrderItemQueryDto> orderItems = em.createQuery(
                        "select new book.jpaShopAPI.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                                " from OrderItem oi" +
                                " join oi.item i" +
                                " where oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        // OrderItemQueryDto 객체를 getOrderId 메소드 기준으로 그룹화합니다.
        // 그룹화된 결과를 맵으로 반환합니다.
        // 이 맵은 주문 ID를 키로 하고, 해당 주문 ID에 속하는 OrderItemQueryDto 객체 리스트를 값으로 가집니다.
        return orderItems.stream().collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));
    }

    public List<OrderFlatDto> findAllByDto_flat() {
        return em.createQuery(
                "select new book.jpaShopAPI.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
                " from Order o" +
                " join o.member m" +
                " join o.delivery d" +
                " join o.orderItems oi" +
                " join oi.item i", OrderFlatDto.class).getResultList();
    }
}
