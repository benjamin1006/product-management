package de.benjamin1006.productmanagement.core.dataimport.csv.strategies;

import de.benjamin1006.productmanagement.core.dto.Cheese;
import de.benjamin1006.productmanagement.core.dto.Product;
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
        Cheese cheese = new Cheese();
        cheese.setType(line[0]);
        cheese.setQuality(Integer.parseInt(line[1]));
        cheese.setPrice(Double.parseDouble(line[2]));
        cheese.setExpirationDate(getRandomExpirationDate());
        return cheese;
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
