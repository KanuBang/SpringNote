package hellojpa.superMapping;

import jakarta.persistence.Entity;

@Entity
public class Album extends Product{
    private String artist;

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
