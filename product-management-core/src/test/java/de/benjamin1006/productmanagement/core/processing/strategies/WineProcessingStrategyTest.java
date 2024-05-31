package de.benjamin1006.productmanagement.core.processing.strategies;

import de.benjamin1006.productmanagement.datamodel.interfaces.days.ICurrentDayProvider;
import de.benjamin1006.productmanagement.datamodel.dto.Cheese;
import de.benjamin1006.productmanagement.datamodel.dto.Wine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Benjamin Woitczyk
 */
class WineProcessingStrategyTest {

    private final ICurrentDayProvider currentDayProvider = Mockito.mock(ICurrentDayProvider.class);
    private final WineProcessingStrategy cut = new WineProcessingStrategy(currentDayProvider);
    private Wine wine;

    @BeforeEach
    void setUp() {
        //Wenn der mock nach dem aktuellen Tag gefragt, wird soll er einfach den aktuellen Tag zurückgeben.
        Mockito.when(currentDayProvider.getCurrentDay()).thenReturn(LocalDate.now());
        /*
        Erstellung eines Standardobjekts vom Type Wein, die benötigten Felder werden in den jeweiligen Tests
        so überschrieben wie es gebraucht wird
        */
        wine = new Wine("wein", 2, 5.99, LocalDate.now());
    }

    @Test
    void testIsCorrectTypeWithGenericObject() {
        assertThat(cut.isCorrectType(new Object()))
                .describedAs("Das es sich beim Objekt Typ nicht um den Typ Wein handelt sollte die Methode false zurückgeben")
                .isFalse();
    }

    @Test
    void testIsCorrectTypeWithCheese() {
        assertThat(cut.isCorrectType(new Cheese("käse", 30, LocalDate.now().plusDays(50), 4.99)))
                .describedAs("Das es sich beim Objekt Typ nicht um den Typ Wein handelt sollte die Methode false zurückgeben")
                .isFalse();
    }

    @Test
    void testIsCorrectTypeWithWine() {
        assertThat(cut.isCorrectType(wine))
                .describedAs("Das es sich beim Objekt Typ um den Typ Wein handelt sollte die Methode true zurückgeben")
                .isTrue();
    }

    @Test
    void testCalculateQualityForFreshWine() {
        wine.setQuality(2);
        wine.setEntryDate(LocalDate.now());
        assertThat(cut.calculateQuality(wine))
                .describedAs("Der Aufruf sollte den aktuellen Qualitätswert nicht erhöhen! Aktueller Qualitätswert: " + wine.getQuality())
                .isEqualTo(2);
    }

    @Test
    void testCalculateQualityFor10DaysOldWine() {
        wine.setQuality(2);
        wine.setEntryDate(LocalDate.now().minusDays(10));
        assertThat(cut.calculateQuality(wine))
                .describedAs("Der Aufruf sollte den aktuellen Qualitätswert erhöhen! Aktueller Qualitätswert : " +  wine.getQuality())
                .isEqualTo(3);
    }

    @Test
    void testCalculateQualityFor100DaysOldHighQualityWine() {
        wine.setQuality(50);
        wine.setEntryDate(LocalDate.now().minusDays(100));
        assertThat(cut.calculateQuality(wine))
                .describedAs("Der Aufruf sollte den aktuellen Qualitätswert nicht erhöhen! Aktueller Qualitätswert: " + wine.getQuality())
                .isEqualTo(50);
    }

    @Test
    void testCalculateQualityFor100DaysOldLowQualityWine() {
        wine.setQuality(5);
        wine.setEntryDate(LocalDate.now().minusDays(100));
        assertThat(cut.calculateQuality(wine))
                .describedAs("Der Aufruf sollte den aktuellen Qualitätswert erhöhen! Aktueller Qualitätswert : " +  wine.getQuality())
                .isEqualTo(6);
    }

    @Test
    void testCalculateDayPrice() {
        wine.setPrice(15.99);
        assertThat(cut.calculateDayPrice(wine))
                .describedAs("Der Aufruf sollte den Preis nicht verändern. Aktueller Preis: " + wine.getPrice())
                .isEqualTo(15.99);
    }

    @Test
    void testRemoveProductWhenQualityIsZero() {
        wine.setQuality(0);
        assertThat(cut.removeProduct(wine))
                .describedAs("Rückgabewert sollte hier false sein, da die Qualität im Rahmen ist.")
                .isFalse();
    }

    @Test
    void testRemoveProductWhenQualityIsNegative() {
        wine.setQuality(-5);
        assertThat(cut.removeProduct(wine))
                .describedAs("Rückgabewert sollte hier true sein, da der Wein seine Qualitätsgrenze unterschritten hat.")
                .isTrue();
    }
}