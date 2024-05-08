package hellojpa.cascadee;

import jakarta.persistence.*;

@Entity
public class Attach {

    @Id @GeneratedValue
    @Column(name = "attach_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    @Column(name = "attach_name")
    private String name;
    @Enumerated
    private FileType attach_type;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FileType getAttach_type() {
        return attach_type;
    }

    public void setAttach_type(FileType attach_type) {
        this.attach_type = attach_type;
    }
}
