package hellojpa.domain;

import jakarta.persistence.*;

import static jakarta.persistence.FetchType.*;

@Entity
@Table(name = "DELIVERY")
public class Delivery {
    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;
    private String city;
    private String street;
    private String zipcode;

    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;
    @Enumerated
    private DeliveryStatus status;
}
