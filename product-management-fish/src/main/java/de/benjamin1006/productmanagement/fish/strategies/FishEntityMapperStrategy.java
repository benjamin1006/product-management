package de.benjamin1006.productmanagement.fish.strategies;

import de.benjamin1006.productmanagement.core.component.strategy.IEntityMapperStrategy;
import de.benjamin1006.productmanagement.core.dto.ProductDto;
import de.benjamin1006.productmanagement.core.repository.ProductEntity;
import de.benjamin1006.productmanagement.fish.dto.FishCondition;
import de.benjamin1006.productmanagement.fish.dto.FishDtoBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * @author Benjamin Woitczyk
 */
@Component
public class FishEntityMapperStrategy implements IEntityMapperStrategy {
    @Override
    public String getMapperType() {
        return "fisch";
    }

    @Override
    public ProductDto mapEntityToDto(ProductEntity entity) {

        final double price = entity.getPrice();
        final int quality = entity.getQuality();
        final LocalDate catchDate = FishDtoUtil.calculateCatchDate();
        final LocalDate expirationDate = FishDtoUtil.calculateExpirationDate(catchDate);
        return FishDtoBuilder.aFish()
                .withType(entity.getType())
                .withQuality(quality)
                .withBasePrice(price)
                .withCatchDate(catchDate)
                .withExpirationDate(expirationDate)
                .withFishCondition(FishCondition.FRESH)
                .withPrice(price + 0.1 * quality)
                .build();
    }
}
