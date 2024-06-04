package de.benjamin1006.productmanagement.core.repository;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.StringJoiner;

/**
 * @author Benjamin Woitczyk
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "Product")
@DiscriminatorColumn(name = "PROD_TYPE", discriminatorType = DiscriminatorType.STRING)
public abstract class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private int quality;
    @Nullable
    private LocalDate expirationDate;
    @Nullable
    private double price;

    protected ProductEntity(String type, int quality, LocalDate expirationDate, double price) {
        this.type = type;
        this.quality = quality;
        this.expirationDate = expirationDate;
        this.price = price;
    }

    protected ProductEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ProductEntity.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("type='" + type + "'")
                .add("quality=" + quality)
                .add("expirationDate=" + expirationDate)
                .add("price=" + price)
                .toString();
    }
}
