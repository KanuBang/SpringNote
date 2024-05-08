package hellojpa.loading;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Club {
    @Id @GeneratedValue
    @Column(name = "club_id")
    private Long id;

    @Column(name = "club_name")
    private String name;

    @OneToMany(mappedBy = "club")
    private List<Player> players = new ArrayList<>();

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

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
