package de.benjamin1006.productmanagement.fish.strategies;

import de.benjamin1006.productmanagement.core.dto.ProductDto;
import de.benjamin1006.productmanagement.core.processing.IProductProcessingStrategy;
import de.benjamin1006.productmanagement.core.processing.days.ICurrentDayProvider;
import de.benjamin1006.productmanagement.fish.dto.FishDto;
import de.benjamin1006.productmanagement.fish.dto.FishCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author Benjamin Woitczyk
 */
@Component
@ConditionalOnProperty(prefix = "product-management", name = "fish-is-active", havingValue = "true", matchIfMissing = false)
public class FishProcessingStrategy implements IProductProcessingStrategy {

    private static final Logger log = LoggerFactory.getLogger(FishProcessingStrategy.class);
    private final ICurrentDayProvider currentDayProvider;

    public FishProcessingStrategy(ICurrentDayProvider currentDayProvider) {
        this.currentDayProvider = currentDayProvider;
    }

    @Override
    public boolean isCorrectType(Object product) {
        return product instanceof FishDto;
    }

    @Override
    public int calculateQuality(ProductDto productDto) {
        FishDto fishDto = (FishDto) productDto;

        if (fishDto.getFishCondition().equals(FishCondition.FROZEN)) {
            return fishDto.getQuality();
        }

        final int quality = fishDto.getQuality() - 2;
        if (quality == 40) {
            freezeFishIfNeeded(fishDto);
        }

        return quality;
    }

    @Override
    public double calculateDayPrice(ProductDto productDto) {
        FishDto fishDto = (FishDto) productDto;

        if (fishDto.getFishCondition().equals(FishCondition.FROZEN)) {
            return productDto.getPrice() + 0.05;
        }

        return fishDto.getBasePrice() + 0.1 * productDto.getQuality();
    }

    @Override
    public boolean removeProduct(ProductDto productDto) {
        FishDto fishDto = (FishDto) productDto;

        if (fishDto.getFishCondition().equals(FishCondition.FROZEN) && fishDto.getExpirationDate().isBefore(currentDayProvider.getCurrentDay())) {
            return true;
        }

        boolean lowQuality = fishDto.getQuality() < 40;
        final boolean isExpired = fishDto.getExpirationDate().isBefore(currentDayProvider.getCurrentDay());
        final boolean removeProduct = isExpired || lowQuality;
        log.debug("Product {} sollte entfernt werden? {}. Qualität ist {}.", fishDto, removeProduct, fishDto.getQuality());
        return removeProduct;
    }

    private void freezeFishIfNeeded(FishDto fishDto) {
        log.debug("Casting fresh fish to frozen fish");
        fishDto.setFishCondition(FishCondition.FROZEN);
        fishDto.setBasePrice(fishDto.getPrice() + 0.5);
        fishDto.setPrice(fishDto.getBasePrice());
        fishDto.setExpirationDate(currentDayProvider.getCurrentDay().plusDays(30));
    }
}