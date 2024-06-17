package book.jpaShopReview.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;
    private String name;

    @Embedded
    private Address address;

    // 1:N 연관관계에서 양방향 매핑을 위한 @OneToMany MappedBy 속성
    // 1쪽에서도 N에 접근하기 위해 사용
    // 이 속성은 N의 연관관계 메서드에서 업데이트 됨
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
