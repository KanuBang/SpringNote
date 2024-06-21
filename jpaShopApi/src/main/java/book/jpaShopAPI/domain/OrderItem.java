package book.jpaShopAPI.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// 주문 상품
@Entity
@Table(name = "ORDER_ITEM")
@Getter @Setter
public class OrderItem {

    @Id @GeneratedValue
    @Column(name ="ORDER_ITEM_ID")
    private Long id;

    // Order:OrderItem -> 1:N
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    @JsonIgnore
    private Order order;

    // Item:OrderItem -> 1:N
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    private int orderPrice;
    private int count;
    // 생성 메서드
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);
        item.removeStock(count);
        return orderItem;
    }

    // 비즈니스 로직
    // 주문 취소 -> 취소한 재고 만큼 추가
    public void cancle() {
        getItem().addStock(count);
    }

    // 조회 로직
    // 주문상품 전체 가격 조회 -> 가격 * 개수 = 총 금액
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
