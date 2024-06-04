package book.jpaShopReview.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ORDERS")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "ORDER_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "DELIVERY_ID")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    //==연관관계 메서드==//
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //==생성 메서드==//
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {

        Order order = new Order(); //주문 객체 생성
        order.setMember(member); // 주문자 설정
        order.setDelivery(delivery); // 배송지 설정

        // 주문에 주문 아이템 넣기
        for(OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }


        order.setStatus(OrderStatus.ORDER); // 주문 상태 -> 주문 중 설정
        order.setOrderDate(LocalDateTime.now()); // 주문 날짜 설정
        return order;
    }

    // 비즈니스 로직
    // 주문 취소
    public void cancle() {

        // 주문 상태 확인 -> 완료 -> 이미 배송 완료된 상품은 취소 불가능
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송 완료된 상품은 취소 불가능합니다.");
        }

        // 주문 상태 확인 -> 완료 아님 -> 취소 가능
        this.setStatus(OrderStatus.CANCLE);

        // 주문 아이템 취소
        for(OrderItem orderItem : orderItems) {
            orderItem.cancle();
        }
    }

    // 조회 로직
    // 전체 주문 가격 조회
    public int getTotalPrice() {

        // 총 금액
        int totalPrice = 0;

        // 총 금액 구하기
        for(OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}
