package de.benjamin1006.productmanagement.core.repository.cheese;

import de.benjamin1006.productmanagement.core.repository.ProductEntity;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;


/**
 * @author Benjamin Woitczyk
 */
@Entity
@DiscriminatorValue("Cheese")
public class CheeseEntity extends ProductEntity {

    private Double basePrice;

    public CheeseEntity(String type, int quality, LocalDate expirationDate, double price, Double basePrice) {
        super(type, quality, expirationDate, price);
        this.basePrice = basePrice;
    }

    public CheeseEntity() {
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }
}
