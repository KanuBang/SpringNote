package hellojpa.cascadee;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Post {

    @GeneratedValue @Id
    @Column(name = "POST_ID")
    private Long id;
    @Column(name = "POST_NAME")
    private String name;

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Attach> attachList = new ArrayList<>();

    // 연관관계 편의 메서드
    public void addAttach(Attach attach) {
        attachList.add(attach);
        attach.setPost(this);
    }

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

    public List<Attach> getAttachList() {
        return attachList;
    }

    public void setAttachList(List<Attach> attachList) {
        this.attachList = attachList;
    }
}
