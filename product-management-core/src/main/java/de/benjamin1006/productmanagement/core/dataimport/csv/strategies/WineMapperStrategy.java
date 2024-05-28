package de.benjamin1006.productmanagement.core.dataimport.csv.strategies;

import de.benjamin1006.productmanagement.core.dto.Product;
import de.benjamin1006.productmanagement.core.dto.Wine;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * @author Benjamin Woitczyk
 */
@Component
public class WineMapperStrategy implements ICsvMapperStrategy {


    @Override
    public Product mapTo(String[] line) {
        Wine wine = new Wine();
        wine.setType(line[0]);
        wine.setQuality(Integer.parseInt(line[1]));
        wine.setPrice(Double.parseDouble(line[2]));
        wine.setEntryDate(LocalDate.now());
        wine.setExpirationDate(null);

        return wine;
    }

    @Override
    public String getMapperType() {
        return "wein";
    }
}