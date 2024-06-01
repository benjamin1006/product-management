package de.benjamin1006.productmanagement.fish.dto;

import java.time.LocalDate;

/**
 * @author Benjamin Woitczyk
 */
public final class FishBuilder {
    private double basePrice;
    private FishCondition fishCondition;
    private LocalDate catchDate;
    private String type;
    private int quality;
    private LocalDate expirationDate;
    private double price;

    private FishBuilder() {
    }

    public static FishBuilder aFish() {
        return new FishBuilder();
    }

    public FishBuilder withBasePrice(double basePrice) {
        this.basePrice = basePrice;
        return this;
    }

    public FishBuilder withFishCondition(FishCondition fishCondition) {
        this.fishCondition = fishCondition;
        return this;
    }

    public FishBuilder withCatchDate(LocalDate catchDate) {
        this.catchDate = catchDate;
        return this;
    }

    public FishBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public FishBuilder withQuality(int quality) {
        this.quality = quality;
        return this;
    }

    public FishBuilder withExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public FishBuilder withPrice(double price) {
        this.price = price;
        return this;
    }

    public Fish build() {
        Fish fish = new Fish(type, quality, price, expirationDate, fishCondition, catchDate);
        fish.setBasePrice(basePrice);
        return fish;
    }
}
