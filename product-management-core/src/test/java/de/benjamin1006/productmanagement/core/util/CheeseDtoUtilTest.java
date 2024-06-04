package de.benjamin1006.productmanagement.core.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Benjamin Woitczyk
 */
class CheeseDtoUtilTest {
    @Test
    void testCalculateFirstDayPrice() {
        double basePrice = 50.5;

        double result = CheeseDtoUtil.calculateFirstDayPrice(basePrice, 1);
        assertThat(result)
                .describedAs("Bei einer Qualität von 1, sollte sich der Preis um 0.1 erhöhen.")
                .isEqualTo(basePrice + 0.1);

        result = CheeseDtoUtil.calculateFirstDayPrice(basePrice, 10);
        assertThat(result)
                .describedAs("Bei einer Qualität von 10, sollte sich der Preis um 1 erhöhen.")
                .isEqualTo(basePrice + 1);

        result = CheeseDtoUtil.calculateFirstDayPrice(basePrice, 50);
        assertThat(result)
                .describedAs("Bei einer Qualität von 50, sollte sich der Preis um 5 erhöhen.")
                .isEqualTo(basePrice + 5);
    }

    @Test
    void testGetRandomExpirationDate() {
        LocalDate date = CheeseDtoUtil.getRandomExpirationDate();

        long daysDiff = ChronoUnit.DAYS.between(LocalDate.now(), date);
        assertThat(daysDiff)
                .describedAs("Datum sollte mindestens 50 Tage in der Zukunft liegen")
                .isGreaterThan(50)
                .describedAs("Das Datum sollte maximal 101 Tage in der Zukunft liegen")
                .isLessThan(100);
    }

}