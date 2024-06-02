package de.benjamin1006.productmanagement.core.dto;

import java.time.LocalDate;

/**
 * @author Benjamin Woitczyk
 */
public class CheeseDto extends ProductDto {

    private double basePrice;

    public CheeseDto(String type, int quality, LocalDate expirationDate, double price, double basePrice) {
        super(type, quality, expirationDate, price);
        this.basePrice = basePrice;
    }

    public CheeseDto() {
        super();
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }
}
