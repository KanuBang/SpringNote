package hellojpa.practice;

import hellojpa.domain.Member;
import jakarta.persistence.*;

@Entity
public class Locker {

    @Id @GeneratedValue
    @Column(name = "LOCKER_ID")
    private long id;
    private String name;
    @OneToOne
    @JoinColumn(name = "student_id", unique = true)
    private Student student;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
