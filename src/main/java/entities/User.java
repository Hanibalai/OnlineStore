package entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Cache;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity(name = "Customer")
@RequiredArgsConstructor
@Setter
@Getter
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", length = 30)
    private String username;

    @Column(name = "is_active", columnDefinition = "BIT")
    private boolean isActive;

    @Column(name = "email", length = 30)
    private String email;

    @Embedded
    private UserAddress address;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_time")
    private Date creationTime;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time")
    private Date updateTime;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private List<Order> orders;

    @PrePersist
    private void onCreate() {
        creationTime = new Date();
    }

    @PreUpdate
    private void onUpdate() {
        updateTime = new Date();
    }

    //Embedded class for storing information about the user's address
    @Embeddable
    @AllArgsConstructor
    @RequiredArgsConstructor
    @EqualsAndHashCode
    public static class UserAddress {
        @Column(name = "address_country", length = 20)
        private String country;

        @Column(name = "address_city", length = 20)
        private String city;

        @Column(name = "address_street", length = 30)
        private String street;

        @Column(name = "address_house", length = 10)
        private String house;

        @Override
        public String toString() {
            return "country = '" + country + '\'' +
                    ", city = '" + city + '\'' +
                    ", street = '" + street + '\'' +
                    ", house = " + house;
        }
    }

    public User(String username, String email, UserAddress address) {
        this.username = username;
        this.isActive = true;
        this.email = email;
        this.address = address;
    }

    private String getDateFormat(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yy hh:mm");
        return dateFormat.format(date);
    }

    @Override
    public String toString() {
        return "User ID - " + id +
                " | username: " + username +
                " | active status - " + isActive +
                ",\nemail: " + email +
                " | address: " + address +
                ",\ncreation time: " + getDateFormat(creationTime) +
                " | update time: " + getDateFormat(updateTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return getId() == user.getId() && isActive() == user.isActive() && getUsername().equals(user.getUsername()) && getAddress().equals(user.getAddress()) && getCreationTime().equals(user.getCreationTime()) && getUpdateTime().equals(user.getUpdateTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername(), isActive(), getAddress(), getCreationTime(), getUpdateTime());
    }
}

