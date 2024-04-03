package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

// @Embeddable 애노테이션은 이 클래스가 다른 엔티티 클래스에 포함될 수 있는 값 타입임을 나타냅니다.
// 즉, Address 객체는 다른 엔티티에 속할 수 있으며 해당 엔티티의 일부로 저장될 수 있습니다.
@Embeddable
@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;

    protected Address() {
    }
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
