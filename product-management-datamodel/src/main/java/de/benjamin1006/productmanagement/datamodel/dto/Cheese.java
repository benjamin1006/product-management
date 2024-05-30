package de.benjamin1006.productmanagement.datamodel.dto;

import java.time.LocalDate;

/**
 * @author Benjamin Woitczyk
 */
public class Cheese extends Product {

    private double basePrice;

    public Cheese(String type, int quality, LocalDate expirationDate, double price) {
        super(type, quality, expirationDate, price);
        this.basePrice = price;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }
}
