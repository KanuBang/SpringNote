package book.jpaShopAPI.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ORDERS")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "ORDER_ID")
    private Long id;

    // 1:N 관계 (Member:Orders)
    // 지연 로딩 Order을 로딩할 때 연관관계에 있는 Member도 한 거번에 로딩하는 것이 아니라
    // Order에서 Member에 접근할 때 실제 DB에 접근하여 로딩
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID") // 1:N의 N 쪽에 외래키 컬럼을 명시적으로 지정
    private Member member;


    // 1:N 관계 (Orders: Order_Item)
    // 양방향 참조를 위한 속성이다.
    // mappedBy를 통해 연관관계의 주인을 알리고,주인이 어떤 필드로 자신을 참조하는 지 알린다.
    // Order 엔티티의 orderItems 필드는 OrderItem 엔티티의 order 필드를 통해 참조됩니다.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    // 1:1 관계 (Orders:Delivery)
    // 개발자 관점에서 주 테이블을 연관관계 주인으로 설
    // CascadeType.ALL 부모테이블에 대한 작업이 자식테이블에 그대로 동기화됨.
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "DELIVERY_ID")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    //==연관관계 메서드==//
    //1:N 관계에서는 일반적으로 N쪽에 작성한다.
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

    // 비즈니스 로직 - 데이터 관련 작업은 데이터를 가지고 있는 쪽에 메서드를 넣어야 응집성이 좋다.
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
