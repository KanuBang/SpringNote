package hellojpa.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

//@Entity
@Table(name = "CATEGORY")
public class Category {
    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;
    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "CATEGORY_ITEM",
            joinColumns = @JoinColumn(name = "CATEGORY_ID"),
            inverseJoinColumns = @JoinColumn(name = "ITEM_ID"))
    private List<Item> items = new ArrayList<>();

}
