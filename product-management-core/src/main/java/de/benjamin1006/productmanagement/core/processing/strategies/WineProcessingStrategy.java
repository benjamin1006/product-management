package de.benjamin1006.productmanagement.core.processing.strategies;

import de.benjamin1006.productmanagement.core.dto.ProductDto;
import de.benjamin1006.productmanagement.core.processing.days.ICurrentDayProvider;
import de.benjamin1006.productmanagement.core.dto.WineDto;
import de.benjamin1006.productmanagement.core.processing.IProductProcessingStrategy;
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
        return product instanceof WineDto;
    }

    @Override
    public int calculateQuality(ProductDto productDto) {
        final WineDto wineDto = (WineDto) productDto;
        if (wineDto.getQuality() >= 50) {
            return wineDto.getQuality();
        }
        final long daysSinceEntry = ChronoUnit.DAYS.between(wineDto.getEntryDate(), currentDayProvider.getCurrentDay());
        if (daysSinceEntry > 0 && daysSinceEntry % 10 == 0) {
            log.debug("Erhöhung der Qualität von {}, da {} Tage seit der Eintragung vergangen sind!", productDto, daysSinceEntry);
            return wineDto.getQuality() + 1;
        }
        log.debug("Wein Qualität von {}, wird nicht erhöht, da nur {} Tage seit der Eintragung vergangen sind!", productDto, daysSinceEntry);
        return wineDto.getQuality();
    }

    @Override
    public double calculateDayPrice(ProductDto productDto) {
        return productDto.getPrice();
    }

    @Override
    public boolean removeProduct(ProductDto productDto) {
        final boolean removeProduct = productDto.getQuality() < 0;
        log.debug("Product {} sollte entfernt werden? {}. Qualität ist {}.", productDto, removeProduct, productDto.getQuality());
        return removeProduct;
    }
}
