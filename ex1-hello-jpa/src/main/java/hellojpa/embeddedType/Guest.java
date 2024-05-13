package hellojpa.embeddedType;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//@Entity
public class Guest {

    @Id @GeneratedValue
    @Column(name = "GUEST_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    @Embedded
    private Address homeAddress;

    @ElementCollection(fetch = FetchType.LAZY) // 디폴트가 지연로딩, 여기서는 그냥 명시적으로 해보면
    @CollectionTable(name="FAVORITE_FOOD", joinColumns = @JoinColumn(name = "GUEST_ID"))
    @Column(name = "FOOD_NAME")
    private List<String> favoriteFoods = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name="ADDRESS", joinColumns = @JoinColumn(name = "GUEST_ID"))
    private List<Address> addressHistory = new ArrayList<>();


    public Guest() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public List<String> getFavoriteFoods() {
        return favoriteFoods;
    }

    public void setFavoriteFoods(List<String> favoriteFoods) {
        this.favoriteFoods = favoriteFoods;
    }

    public List<Address> getAddressHistory() {
        return addressHistory;
    }

    public void setAddressHistory(ArrayList<Address> addressHistory) {
        this.addressHistory = addressHistory;
    }
}
