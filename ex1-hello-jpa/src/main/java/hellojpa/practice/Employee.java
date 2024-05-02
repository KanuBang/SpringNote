package hellojpa.practice;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

// @Entity
public class Employee {
    @Id @GeneratedValue
    @Column(name = "employee_id")
    private Long id;

    @OneToMany(mappedBy = "employee")
    private List<Email> emails = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }
}
