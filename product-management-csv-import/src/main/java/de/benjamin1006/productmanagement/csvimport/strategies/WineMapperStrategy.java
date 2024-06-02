package de.benjamin1006.productmanagement.csvimport.strategies;

import de.benjamin1006.productmanagement.core.dto.ProductDto;
import de.benjamin1006.productmanagement.core.processing.days.ICurrentDayProvider;
import de.benjamin1006.productmanagement.core.dto.WineDto;
import de.benjamin1006.productmanagement.core.component.strategy.ICsvMapperStrategy;
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
    public ProductDto mapTo(String[] line) {
        final String type = line[0];
        final int quality = Integer.parseInt(line[1]);
        final double price = Double.parseDouble(line[2]);

        return new WineDto(type, quality, price, currentDayProvider.getCurrentDay());
    }

    @Override
    public String getMapperType() {
        return "wein";
    }
}