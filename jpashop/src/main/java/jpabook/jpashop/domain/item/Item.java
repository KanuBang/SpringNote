package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // item 한 테이블에 Album, Book, Movie 다 박는다.
@DiscriminatorColumn(name = "dtype") // 구분?
@Getter @Setter
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stackQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<Category>();

    //--비즈니스로직--//
    // 데이터를 가지고 있는 쪽에 메서드를 넣어야 응집성이 좋다.
    public void addStock(int quantity) {
        this.stackQuantity += quantity;
    }

    public void removeStock(int quantity) {
        int restStock = this.stackQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stackQuantity = restStock;
    }
}
