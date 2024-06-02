package de.benjamin1006.productmanagement.core.processing.strategies;

import de.benjamin1006.productmanagement.core.dto.CheeseDto;
import de.benjamin1006.productmanagement.core.dto.ProductDto;
import de.benjamin1006.productmanagement.core.processing.IProductProcessingStrategy;
import de.benjamin1006.productmanagement.core.processing.days.ICurrentDayProvider;
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
        return product instanceof CheeseDto;
    }

    /**
     * Käse verringert seine Qualität jeden Tag um einen Punkt
     *
     * @param productDto vom Typ Cheese, für das die Qualität berechnet werden soll
     * @return der neue Qualitätswert
     */
    @Override
    public int calculateQuality(ProductDto productDto) {
        return productDto.getQuality() - 1;
    }

    /**
     * Käse hat einen tagesaktuellen Preis, welcher folgendermaßen berechnet wird:
     * Grundpreis + 0,10 € * Qualität
     *
     * @param productDto vom Typ Cheese, für das die Qualität berechnet werden soll
     * @return der neue Tagespreis
     */
    @Override
    public double calculateDayPrice(ProductDto productDto) {
        final CheeseDto cheeseDto = (CheeseDto) productDto;
        return cheeseDto.getBasePrice() + 0.1 * cheeseDto.getQuality();
    }

    /**
     * Entscheidet, ob ein Objekt vom Typ Produkt Kind klasse Cheese, entfernt werden sollte.
     * Käse darf nicht abgelaufen sein und muss mindestens eine Qualität von 30 haben damit er nicht entfernt wird.
     *
     * @param productDto Das Objekt vom Typ Product das überprüft werden soll
     * @return true, wenn der Käse entfernt werden sollte, false, wenn nicht
     */
    @Override
    public boolean removeProduct(ProductDto productDto) {
        final boolean isExpired = productDto.getExpirationDate().isBefore(iCurrentDayProvider.getCurrentDay());
        final boolean lowQuality = productDto.getQuality() < 30;
        final boolean removeProduct = isExpired || lowQuality;
        log.debug("Product {} sollte entfernt werden? {}. Qualität ist {}.", productDto, removeProduct, productDto.getQuality());
        return removeProduct;
    }
}
