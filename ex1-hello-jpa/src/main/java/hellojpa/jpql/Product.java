package hellojpa.jpql;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Product {
    @GeneratedValue @Id
    private Long id;
    private String name;
    private int price;
    private int stockAmount;
    @OneToMany(mappedBy = "product")
    private List<Order> orders;
}
