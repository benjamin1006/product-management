package de.benjamin1006.productmanagement.fish.strategies;

import de.benjamin1006.productmanagement.datamodel.dto.Product;
import de.benjamin1006.productmanagement.datamodel.interfaces.days.ICurrentDayProvider;
import de.benjamin1006.productmanagement.datamodel.interfaces.strategy.processing.IProductProcessingStrategy;
import de.benjamin1006.productmanagement.fish.dto.Fish;
import de.benjamin1006.productmanagement.fish.dto.FishCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author Benjamin Woitczyk
 */
@Component
@ConditionalOnProperty(prefix = "product-management", name = "fish", havingValue = "true", matchIfMissing = false)
public class FishProcessingStrategy implements IProductProcessingStrategy {

    private static final Logger log = LoggerFactory.getLogger(FishProcessingStrategy.class);
    private final ICurrentDayProvider currentDayProvider;

    public FishProcessingStrategy(ICurrentDayProvider currentDayProvider) {
        this.currentDayProvider = currentDayProvider;
    }

    @Override
    public boolean isCorrectType(Object product) {
        return product instanceof Fish;
    }

    @Override
    public int calculateQuality(Product product) {
        Fish fish = (Fish) product;

        if (fish.getFishCondition().equals(FishCondition.FROZEN)) {
            return fish.getQuality();
        }

        final int quality = fish.getQuality() - 2;
        if (quality == 40) {
            freezeFishIfNeeded(fish);
        }

        return quality;
    }

    @Override
    public double calculateDayPrice(Product product) {
        Fish fish = (Fish) product;

        if (fish.getFishCondition().equals(FishCondition.FROZEN)) {
            return product.getPrice() + 0.05;
        }

        return fish.getBasePrice() + 0.1 * product.getQuality();
    }

    @Override
    public boolean removeProduct(Product product) {
        Fish fish = (Fish) product;

        if (fish.getFishCondition().equals(FishCondition.FROZEN) && fish.getExpirationDate().isBefore(currentDayProvider.getCurrentDay())) {
            return true;
        }

        boolean lowQuality = fish.getQuality() < 40;
        final boolean isExpired = fish.getExpirationDate().isBefore(currentDayProvider.getCurrentDay());
        final boolean removeProduct = isExpired || lowQuality;
        log.debug("Product {} sollte entfernt werden? {}. Qualität ist {}.", fish, removeProduct, fish.getQuality());
        return removeProduct;
    }

    private void freezeFishIfNeeded(Fish fish) {
        log.debug("Casting fresh fish to frozen fish");
        fish.setFishCondition(FishCondition.FROZEN);
        fish.setBasePrice(fish.getPrice() + 0.5);
        fish.setPrice(fish.getBasePrice());
        fish.setExpirationDate(currentDayProvider.getCurrentDay().plusDays(30));
    }
}