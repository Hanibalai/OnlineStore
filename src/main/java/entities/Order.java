package entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CreationTimestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "Customer_Order")
@Getter
@Setter
@EqualsAndHashCode
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User user;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_time")
    private Date creationTime;

    @OneToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    @JoinTable(name = "Order_Product",
            joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"))
    private List<Product> products;

    @PrePersist
    private void onCreate() {
        creationTime = new Date();
    }

    public Order() {
        this.products = new ArrayList<>();
    }

    private String getDateFormat(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yy hh:mm");
        return dateFormat.format(date);
    }

    @Override
    public String toString() {
        return "Order ID = " + id +
                ", User ID = " + user.getId() +
                ", username: " + user.getUsername() +
                " | order creation time: " + getDateFormat(creationTime) +
                "\nproducts: " + products;
    }
}
