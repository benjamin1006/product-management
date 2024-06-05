package de.benjamin1006.productmanagement.core.util;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Utility Klassen für das {@link de.benjamin1006.productmanagement.core.dto.CheeseDto}.
 * @author Benjamin Woitczyk
 */
public class CheeseDtoUtil {

    private CheeseDtoUtil() {
    }

    /**
     * Damit beim Käse auch bereits am ersten Tag die Qualität mit einberechnet wird, wird hier direkt der erste Tagespreis
     * berechnet.
     *
     * @param basePrice der Grundpreis, auf dessen Grundlage der Tagespreis berechnet wird.
     * @param quality   die Qualität die mit einberechnet werden soll
     * @return der tagespreis für Tag 1
     */
    public static double calculateFirstDayPrice(double basePrice, int quality) {
        return basePrice + 0.1 * quality;
    }

    /**
     * Erstellt ein Datum das zwischen 50 und 100 Tagen in der Zukunft liest
     * Werte sind hier 50 und 101, da die untere Kante mit eingeschlossen, die obere Kante allerdings nicht mit eingeschlossen ist.
     *
     * @return Datum
     */
    public static LocalDate getRandomExpirationDate() {

        LocalDate currentDate = LocalDate.now();

        int randomDays = ThreadLocalRandom.current().nextInt(50, 101);

        return currentDate.plusDays(randomDays);
    }
}
