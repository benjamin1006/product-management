package de.benjamin1006.productmanagement.processing.extecution.strategies;

import de.benjamin1006.productmanagement.core.interfaces.days.ICurrentDayProvider;
import de.benjamin1006.productmanagement.core.dto.Product;
import de.benjamin1006.productmanagement.core.dto.Wine;
import de.benjamin1006.productmanagement.core.interfaces.strategy.processing.IProductProcessingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

/**
 * @author Benjamin Woitczyk
 */
@Component
public class WineProcessingStrategy implements IProductProcessingStrategy {
    private static final Logger log = LoggerFactory.getLogger(WineProcessingStrategy.class);

    private final ICurrentDayProvider currentDayProvider;

    public WineProcessingStrategy(ICurrentDayProvider currentDayProvider) {
        this.currentDayProvider = currentDayProvider;
    }

    @Override
    public boolean isCorrectType(Object product) {
        return product instanceof Wine;
    }

    @Override
    public int calculateQuality(Product product) {
        final Wine wine = (Wine) product;
        if (wine.getQuality() >= 50) {
            return wine.getQuality();
        }
        final long daysSinceEntry = ChronoUnit.DAYS.between(wine.getEntryDate(), currentDayProvider.getCurrentDay());
        if (daysSinceEntry > 0 && daysSinceEntry % 10 == 0) {
            log.debug("Erhöhung der Qualität von {}, da {} Tage seit der Eintragung vergangen sind!", product, daysSinceEntry);
            return wine.getQuality() + 1;
        }
        log.debug("Wein Qualität von {}, wird nicht erhöht, da nur {} Tage seit der Eintragung vergangen sind!", product, daysSinceEntry);
        return wine.getQuality();
    }

    @Override
    public double calculateDayPrice(Product product) {
        return product.getPrice();
    }

    @Override
    public boolean removeProduct(Product product) {
        final boolean removeProduct = product.getQuality() < 0;
        log.debug("Product {} sollte entfernt werden? {}. Qualität ist {}.", product, removeProduct, product.getQuality());
        return removeProduct;
    }
}
