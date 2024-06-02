package de.benjamin1006.productmanagement.fish.dto;

import de.benjamin1006.productmanagement.core.dto.Product;

import java.time.LocalDate;

/**
 * @author Benjamin Woitczyk
 */
public class Fish extends Product {

    private FishCondition fishCondition;
    private LocalDate catchDate;
    private double basePrice;

    public Fish(String type, int quality, double price, LocalDate expirationDate,FishCondition fishCondition, LocalDate catchDate) {
        super(type, quality, expirationDate, price);
        this.fishCondition = fishCondition;
        this.catchDate = catchDate;
        this.basePrice = price;
    }

    public FishCondition getFishCondition() {
        return fishCondition;
    }

    public void setFishCondition(FishCondition fishCondition) {
        this.fishCondition = fishCondition;
    }

    public LocalDate getCatchDate() {
        return catchDate;
    }

    public void setCatchDate(LocalDate catchDate) {
        this.catchDate = catchDate;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }
}
