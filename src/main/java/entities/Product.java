package entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity(name = "Product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@RequiredArgsConstructor
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private BigDecimal price;

    @Lob
    @Column(name = "photo", columnDefinition = "BLOB")
    private byte[] photo;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "category")
    private Category category;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "addition_time")
    private Date additionTime;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time")
    private Date updateTime;

    @ElementCollection
    @CollectionTable(name = "Product_Comments",
            joinColumns = @JoinColumn(name = "product_id"))
    private List<String> comments;

    @PrePersist
    private void onAdd() {
        additionTime = new Date();
    }

    @PreUpdate
    private void onUpdate() {
        updateTime = new Date();
    }

    public Product(String name, BigDecimal price, Product.Category category) {
        this.name = name;
        this.price = price;
        this.photo = null;
        this.category = category;
    }

    public enum Category {
        Phone ("Phone"),
        Laptop ("Laptop"),
        Tablet ("Tablet"),
        Watches ("Watch"),
        Headphones ("Headphones"),
        Stereo ("Stereo"),
        TV ("TV");
        private final String title;
        Category(String title) {
            this.title = title;
        }
        public String getTitle() {
            return title;
        }
    }

    private String getDateFormat(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yy hh:mm");
        return dateFormat.format(date);
    }

    @Override
    public String toString() {
        return "Product ID = " + id +
                ", name = '" + name + '\'' +
                ", category = '" + category + '\'' +
                ", price = " + price +
                " | addition time: " + getDateFormat(additionTime) +
                " , update time: " + getDateFormat(updateTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return getId() == product.getId() && getName().equals(product.getName()) && getPrice().equals(product.getPrice()) && getAdditionTime().equals(product.getAdditionTime()) && getUpdateTime().equals(product.getUpdateTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPrice(), getAdditionTime(), getUpdateTime());
    }
}
