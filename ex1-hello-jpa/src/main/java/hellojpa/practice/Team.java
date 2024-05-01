package hellojpa.practice;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

// @Entity 잠시 지우기로
public class Team {

    @Id @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;
    private String name;

    @OneToMany(mappedBy = "team")
    private List<Mate> mates = new ArrayList<Mate>();

    public List<Mate> getMates() {
        return mates;
    }

    public void setMates(List<Mate> mates) {
        this.mates = mates;
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

    public void addMate(Mate mate) {
        this.getMates().add(mate);
    }
}
