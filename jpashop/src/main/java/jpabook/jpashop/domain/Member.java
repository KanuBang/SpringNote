package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id") // 컬럼 이름을 id에서 -> member_id
    private Long id;

    private String name;

    @Embedded
    private Address address;

    // order 테이블에 있는 member로부터 매핑된다.
    // 여기에 값이 들어가도 order 테이블의 member가 바뀌지 않는다?
    // 반면, order 테이블의 member가 바뀌면 이 값이 바뀐다.
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
