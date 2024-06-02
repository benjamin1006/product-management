package de.benjamin1006.productmanagement.fish.dto;

import java.time.LocalDate;

/**
 * @author Benjamin Woitczyk
 */
public final class FishDtoBuilder {
    private double basePrice;
    private FishCondition fishCondition;
    private LocalDate catchDate;
    private String type;
    private int quality;
    private LocalDate expirationDate;
    private double price;

    private FishDtoBuilder() {
    }

    public static FishDtoBuilder aFish() {
        return new FishDtoBuilder();
    }

    public FishDtoBuilder withBasePrice(double basePrice) {
        this.basePrice = basePrice;
        return this;
    }

    public FishDtoBuilder withFishCondition(FishCondition fishCondition) {
        this.fishCondition = fishCondition;
        return this;
    }

    public FishDtoBuilder withCatchDate(LocalDate catchDate) {
        this.catchDate = catchDate;
        return this;
    }

    public FishDtoBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public FishDtoBuilder withQuality(int quality) {
        this.quality = quality;
        return this;
    }

    public FishDtoBuilder withExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public FishDtoBuilder withPrice(double price) {
        this.price = price;
        return this;
    }

    public FishDto build() {
        FishDto fishDto = new FishDto(type, quality, price, expirationDate, fishCondition, catchDate);
        fishDto.setBasePrice(basePrice);
        return fishDto;
    }
}
