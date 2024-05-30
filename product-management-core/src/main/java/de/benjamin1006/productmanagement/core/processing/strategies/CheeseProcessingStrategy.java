package de.benjamin1006.productmanagement.core.processing.strategies;

import de.benjamin1006.productmanagement.core.processing.days.ICurrentDayProvider;
import de.benjamin1006.productmanagement.datamodel.dto.Cheese;
import de.benjamin1006.productmanagement.datamodel.dto.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Benjamin Woitczyk
 */
@Component
public class CheeseProcessingStrategy implements IProductProcessingStrategy {

    private static final Logger log = LoggerFactory.getLogger(CheeseProcessingStrategy.class);

    private final ICurrentDayProvider iCurrentDayProvider;

    public CheeseProcessingStrategy(ICurrentDayProvider iCurrentDayProvider) {
        this.iCurrentDayProvider = iCurrentDayProvider;
    }

    @Override
    public boolean isCorrectType(Object product) {
        return product instanceof Cheese;
    }

    /**
     * Käse verringert seine Qualität jeden Tag um einen Punkt
     * @param product vom Typ Cheese, für das die Qualität berechnet werden soll
     * @return der neue Qualitätswert
     */
    @Override
    public int calculateQuality(Product product) {
        return product.getQuality() - 1;
    }

    /**
     * Käse hat einen tagesaktuellen Preis, welcher folgendermaßen berechnet wird:
     * Grundpreis + 0,10 € * Qualität
     * @param product vom Typ Cheese, für das die Qualität berechnet werden soll
     * @return der neue Tagespreis
     */
    @Override
    public double calculateDayPrice(Product product) {
        final Cheese cheese = (Cheese) product;
        return cheese.getBasePrice() + 0.1 * cheese.getQuality();
    }

    @Override
    public boolean removeProduct(Product product) {
        final boolean isExpired = product.getExpirationDate().isBefore(iCurrentDayProvider.getCurrentDay());
        final boolean lowQuality = product.getQuality() < 30;
        final boolean removeProduct = isExpired || lowQuality;
        log.debug("Product {} sollte entfernt werden? {}. Qualität ist {}.", product, removeProduct, product.getQuality());
        return removeProduct;
    }
}
