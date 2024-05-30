package de.benjamin1006.productmanagement.core.dataimport.csv.strategies;

import de.benjamin1006.productmanagement.core.dto.Cheese;
import de.benjamin1006.productmanagement.core.dto.Product;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Benjamin Woitczyk
 */
class CheeseMapperStrategyTest {

    private final CheeseMapperStrategy cut = new CheeseMapperStrategy();

    @Test
    void testMapTo() {
        String[] testData = {"käse", "20", "5.99"};

        final Product cheese = cut.mapTo(testData);

        assertThat(cheese)
                .describedAs("Das Objekt sollte nicht null sein.")
                .isNotNull()
                .describedAs("Das Objekt sollte vom Typ Cheese.class sein! Tatsächlicher Wert: " + cheese.getClass())
                .isInstanceOf(Cheese.class);

        assertThat(cheese.getType())
                .describedAs("Der typ sollte käse sein! Tatsächlicher Wert: " + cheese.getType())
                .isEqualTo("käse");
        assertThat(cheese.getQuality())
                .describedAs("Die Qualität sollte 20 sein! Tatsächlicher Wert: " + cheese.getQuality())
                .isEqualTo(20);
        assertThat(cheese.getPrice())
                .describedAs("Der Preis sollte bei 5.99 liegen! Tatsächlicher Wert: " + cheese.getPrice())
                .isEqualTo(5.99);

        final LocalDate expirationdate = cheese.getExpirationDate();
        assertThat(expirationdate)
                .describedAs("Auch casted sollte das Käse Objekt nicht null sein!")
                .isNotNull()
                .describedAs("Das Datum sollte nach dem heutigen Tag liegen! Tatsächlicher Wert: {}!", expirationdate)
                .isAfter(LocalDate.now())
                .describedAs("Das Datum sollte mindestens 50 Tage in der Zukunft liegen! Tatsächlicher Wert: " + expirationdate)
                .isAfter(LocalDate.now().plusDays(50))
                .describedAs("Das Datum sollte maximal 100 Tage in der Zukunft liegen! Tatsächlicher Wert: " + expirationdate)
                .isBefore(LocalDate.now().plusDays(101));
    }

}