package de.benjamin1006.productmanagement.core.dataimport.csv.strategies;

import de.benjamin1006.productmanagement.core.dto.Product;
import de.benjamin1006.productmanagement.core.dto.Wine;
import de.benjamin1006.productmanagement.core.processing.days.ICurrentDayProvider;
import org.springframework.stereotype.Component;

/**
 * @author Benjamin Woitczyk
 */
@Component
public class WineMapperStrategy implements ICsvMapperStrategy {

    private final ICurrentDayProvider currentDayProvider;

    public WineMapperStrategy(ICurrentDayProvider currentDayProvider) {
        this.currentDayProvider = currentDayProvider;
    }

    @Override
    public Product mapTo(String[] line) {
        final String type = line[0];
        final int quality = Integer.parseInt(line[1]);
        final double price = Double.parseDouble(line[2]);

        return new Wine(type, quality, price, currentDayProvider.getCurrentDay());
    }

    @Override
    public String getMapperType() {
        return "wein";
    }
}