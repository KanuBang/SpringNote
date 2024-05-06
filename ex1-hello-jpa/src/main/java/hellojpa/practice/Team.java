package hellojpa.practice;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

//@Entity
@Table(name = "TEAM")
public class Team {

    @Id @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;
    private String name;

    @OneToMany(mappedBy = "team")
    private List<Mate> mates = new ArrayList<>();

    public void addMate(Mate mate) {
        mate.setTeam(this);
        mates.add(mate);
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

    public List<Mate> getMates() {
        return mates;
    }

    public void setMates(List<Mate> mates) {
        this.mates = mates;
    }
}
