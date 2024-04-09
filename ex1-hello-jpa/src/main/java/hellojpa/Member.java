package hellojpa;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
// @Table(name = "user")
public class Member {

    // @id: 영속성 컨텍스트의 1차 캐시의 key로 지정됨.
    @Id
    private Long id;
    // @Column(name = "username")
    private String name;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
