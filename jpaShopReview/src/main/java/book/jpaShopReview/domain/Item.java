package book.jpaShopReview.domain;

import book.jpaShopReview.exception.NotEnoughStockException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "ITEM_ID")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList <Category>();

    //비즈니스 로직 - 데이터 관련 작업은 데이터를 가지고 있는 쪽에 메서드를 넣어야 응집성이 좋다.

    //재고 추가
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    //재고 삭제
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}

