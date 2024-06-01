package de.benjamin1006.productmanagement.fish.strategies;

import de.benjamin1006.productmanagement.datamodel.dto.Product;
import de.benjamin1006.productmanagement.datamodel.interfaces.strategy.csv.ICsvMapperStrategy;
import de.benjamin1006.productmanagement.fish.dto.FishBuilder;
import de.benjamin1006.productmanagement.fish.dto.FishCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * @author Benjamin Woitczyk
 */
@Component
@ConditionalOnProperty(prefix = "product-management", name = "fish", havingValue = "true", matchIfMissing = false)
public class FishMapperStrategy implements ICsvMapperStrategy {

    @Override
    public String getMapperType() {
        return "fisch";
    }

    @Override
    public Product mapTo(String[] line) {
        final String type = line[0];
        final int quality = Integer.parseInt(line[1]);
        final double price = Double.parseDouble(line[2]);
        final LocalDate catchDate = calculateCatchDate();
        final LocalDate expirationDate = calculateExpirationDate(catchDate);
        return FishBuilder.aFish()
                .withType(type)
                .withQuality(quality)
                .withBasePrice(price)
                .withCatchDate(catchDate)
                .withExpirationDate(expirationDate)
                .withFishCondition(FishCondition.FRESH)
                .withPrice(price + 0.1 * quality)
                .build();
    }

    private LocalDate calculateCatchDate() {
        return LocalDate.now().minusDays(1);
    }

    private LocalDate calculateExpirationDate(LocalDate catchDate) {
        return catchDate.plusDays(7);
    }
}