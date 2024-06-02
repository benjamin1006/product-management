package de.benjamin1006.productmanagement.dataimport.csv.strategies;

import de.benjamin1006.productmanagement.core.dto.Cheese;
import de.benjamin1006.productmanagement.core.dto.Product;
import de.benjamin1006.productmanagement.core.interfaces.strategy.csv.ICsvMapperStrategy;
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
        final double basePrice = Double.parseDouble(line[2]);
        final double price = calculateFristDayPrice(basePrice, quality);
        return new Cheese(type, quality, getRandomExpirationDate(), price, basePrice);
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

    /**
     * Damit beim Käse auch bereits am ersten Tag die Qualität mit einberechnet wird, wird hier direkt der erste Tagespreis
     * berechnet.
     * @param basePrice der Grundpreis, auf dessen Grundlage der Tagespreis berechnet wird.
     * @param quality die Qualität die mit einberechnet werden soll
     * @return der tagespreis für Tag 1
     */
    private double calculateFristDayPrice(double basePrice, int quality) {
        return basePrice + 0.1 * quality;
    }
}
