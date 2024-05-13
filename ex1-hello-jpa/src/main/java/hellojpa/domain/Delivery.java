package hellojpa.domain;

import jakarta.persistence.*;

import static jakarta.persistence.FetchType.*;

@Entity
@Table(name = "DELIVERY")
public class Delivery {
    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;
    @Embedded
    private Address address;
    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;
    @Enumerated
    private DeliveryStatus status;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }
}
