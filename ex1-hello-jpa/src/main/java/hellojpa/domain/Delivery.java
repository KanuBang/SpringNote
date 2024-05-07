package hellojpa.domain;

import jakarta.persistence.*;

//@Entity
@Table(name = "DELIVERY")
public class Delivery {
    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;
    private String city;
    private String street;
    private String zipcode;

    @OneToOne(mappedBy = "delivery")
    private Order order;
    @Enumerated
    private DeliveryStatus status;
}
