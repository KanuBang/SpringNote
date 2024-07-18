package hello.coreReview.discount;

import lombok.Getter;


public interface OrderService {
    Order createOrder(Long memberId, String itemName, int itemPrice);
}
