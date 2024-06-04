package de.benjamin1006.productmanagement.core.repository.wine;

import de.benjamin1006.productmanagement.core.repository.ProductEntity;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;

/**
 * @author Benjamin Woitczyk
 */
@Entity
@DiscriminatorValue("Wine")
public class WineEntity extends ProductEntity {

    private LocalDate entryDate;

    public WineEntity(String type, int quality, double price, LocalDate entryDate) {
        super(type, quality, null, price);
        this.entryDate = entryDate;
    }

    public WineEntity() {

    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }
}
