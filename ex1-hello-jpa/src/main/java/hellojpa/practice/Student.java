package hellojpa.practice;

import jakarta.persistence.*;

//@Entity
public class Student {
    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;
    private String username;

    @OneToOne(mappedBy = "student")
    private Locker locker;


    public void addLocker(Locker locker) {
        setLocker(locker);
        locker.setStudent(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Locker getLocker() {
        return locker;
    }

    public void setLocker(Locker locker) {
        this.locker = locker;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
