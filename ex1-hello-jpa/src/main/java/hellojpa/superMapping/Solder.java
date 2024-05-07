package hellojpa.superMapping;

import jakarta.persistence.Entity;

////@Entity
public class Solder extends BaseEntity{
    private String email;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
