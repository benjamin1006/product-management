package de.benjamin1006.productmanagement.csvimport.strategies;

import de.benjamin1006.productmanagement.core.component.strategy.ICsvMapperStrategy;
import de.benjamin1006.productmanagement.core.dto.CheeseDto;
import de.benjamin1006.productmanagement.core.dto.ProductDto;
import de.benjamin1006.productmanagement.core.util.CheeseDtoUtil;
import org.springframework.stereotype.Component;

/**
 * @author Benjamin Woitczyk
 */
@Component
public class CheeseMapperStrategy implements ICsvMapperStrategy {

    @Override
    public ProductDto mapTo(String[] line) {
        final String type = line[0];
        final int quality = Integer.parseInt(line[1]);
        final double basePrice = Double.parseDouble(line[2]);
        final double price = CheeseDtoUtil.calculateFirstDayPrice(basePrice, quality);
        return new CheeseDto(type, quality, CheeseDtoUtil.getRandomExpirationDate(), price, basePrice);
    }

    @Override
    public String getMapperType() {
        return "käse";
    }
}
