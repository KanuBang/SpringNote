package book.jpaShopAPI.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "CATEGORY_ID")
    private Long id;

    private String name;

    // @JoinTable -> Item:Category의 다대다 관계 중간 테이블
    // name -> 중간 테이블의 이름
    // joinColumns -> 현재 엔터티와 매핑되는 외래키 컬럼을 지정한다.
    // inverseJoinColumns -> 반대쪽 엔티티와 매핑되는 외래키 컬럼을 지정합니다.
    // 여기서 현재 엔터티는 Category, 반대쪽 엔터티는 Item 이다.
    @ManyToMany
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "CATEGORY_ID"),
            inverseJoinColumns = @JoinColumn(name = "ITEM_ID"))
    private List<Item> items;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<Category>();

    //==연관관계 메서드==//
    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }
}
