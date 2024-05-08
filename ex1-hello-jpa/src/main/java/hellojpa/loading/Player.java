package hellojpa.loading;

import jakarta.persistence.*;

//@Entity
public class Player {

    @Id @GeneratedValue
    @Column(name = "PLAYER_ID")
    private Long id;
    private String first_name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "club_id")
    private Club club;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        club.getPlayers().add(this);
        this.club = club;
    }
}
