package de.benjamin1006.productmanagement.core.dataimport.csv.strategies;

import de.benjamin1006.productmanagement.datamodel.dto.Cheese;
import de.benjamin1006.productmanagement.datamodel.dto.Product;
import de.benjamin1006.productmanagement.datamodel.interfaces.strategy.csv.ICsvMapperStrategy;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Benjamin Woitczyk
 */
@Component
public class CheeseMapperStrategy implements ICsvMapperStrategy {

    @Override
    public Product mapTo(String[] line) {
        final String type = line[0];
        final int quality = Integer.parseInt(line[1]);
        final double price = Double.parseDouble(line[2]);
        return new Cheese(type, quality, getRandomExpirationDate(), price);
    }

    @Override
    public String getMapperType() {
        return "käse";
    }

    /**
     * Erstellt ein Datum das zwischen 50 und 100 Tagen in der Zukunft liest
     * Werte sind hier 50 und 101, da die untere Kante mit eingeschlossen, die obere Kante allerdings nicht mit eingeschlossen ist.
     * @return Datum
     */
    private LocalDate getRandomExpirationDate() {

        LocalDate currentDate = LocalDate.now();

        int randomDays = ThreadLocalRandom.current().nextInt(50, 101);

        return currentDate.plusDays(randomDays);
    }
}
