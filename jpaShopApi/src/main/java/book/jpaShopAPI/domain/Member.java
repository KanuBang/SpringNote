package book.jpaShopAPI.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
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

    // 엔티티에 API 검증을 위한 로직이 들어간다. (@NotEmpty 등등)
    // 실무에서는 회원 엔티티를 위한 API가 다양하게 만들어지는데, 한 엔티티에 각각의 API를 위한
    // 모든 요청 요구사항을 담기는 어렵다.
    @NotEmpty
    private String name;

    @Embedded
    private Address address;

    // 1:N 연관관계에서 양방향 매핑을 위한 @OneToMany MappedBy 속성
    // 1쪽에서도 N에 접근하기 위해 사용
    // 이 속성은 N의 연관관계 메서드에서 업데이트 됨
    @JsonIgnore // 양방향 연관관계 시 양쪽을 서로 호출하는 문제 방지
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
