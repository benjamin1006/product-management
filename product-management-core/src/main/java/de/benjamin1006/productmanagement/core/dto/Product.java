package de.benjamin1006.productmanagement.core.dto;

import java.time.LocalDate;
import java.util.StringJoiner;

/**
 * @author Benjamin Woitczyk
 */
public abstract class Product {
    private String type;
    private int quality;
    private LocalDate expirationDate;
    private double price;

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
        return new StringJoiner(", ", Product.class.getSimpleName() + "[", "]")
                .add("type='" + type + "'")
                .add("quality=" + quality)
                .add("expirationDate=" + expirationDate)
                .add("price=" + price)
                .toString();
    }
}
