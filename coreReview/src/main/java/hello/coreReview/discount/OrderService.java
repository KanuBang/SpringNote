package hello.coreReview.discount;

public interface OrderService {
    Order createOrder(Long memberId, String itemName, int itemPrice);
}
