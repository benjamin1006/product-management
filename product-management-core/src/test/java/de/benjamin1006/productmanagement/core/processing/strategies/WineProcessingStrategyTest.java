package de.benjamin1006.productmanagement.core.processing.strategies;

import de.benjamin1006.productmanagement.core.processing.days.ICurrentDayProvider;
import de.benjamin1006.productmanagement.core.dto.CheeseDto;
import de.benjamin1006.productmanagement.core.dto.WineDto;
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
    private WineDto wineDto;

    @BeforeEach
    void setUp() {
        //Wenn der mock nach dem aktuellen Tag gefragt, wird soll er einfach den aktuellen Tag zurückgeben.
        Mockito.when(currentDayProvider.getCurrentDay()).thenReturn(LocalDate.now());
        /*
        Erstellung eines Standardobjekts vom Type Wein, die benötigten Felder werden in den jeweiligen Tests
        so überschrieben wie es gebraucht wird
        */
        wineDto = new WineDto("wein", 2, 5.99, LocalDate.now());
    }

    @Test
    void testIsCorrectTypeWithGenericObject() {
        assertThat(cut.isCorrectType(new Object()))
                .describedAs("Das es sich beim Objekt Typ nicht um den Typ Wein handelt sollte die Methode false zurückgeben")
                .isFalse();
    }

    @Test
    void testIsCorrectTypeWithCheese() {
        assertThat(cut.isCorrectType(new CheeseDto("käse", 30, LocalDate.now().plusDays(50), 4.99, 1.99)))
                .describedAs("Das es sich beim Objekt Typ nicht um den Typ Wein handelt sollte die Methode false zurückgeben")
                .isFalse();
    }

    @Test
    void testIsCorrectTypeWithWine() {
        assertThat(cut.isCorrectType(wineDto))
                .describedAs("Das es sich beim Objekt Typ um den Typ Wein handelt sollte die Methode true zurückgeben")
                .isTrue();
    }

    @Test
    void testCalculateQualityForFreshWine() {
        wineDto.setQuality(2);
        wineDto.setEntryDate(LocalDate.now());
        assertThat(cut.calculateQuality(wineDto))
                .describedAs("Der Aufruf sollte den aktuellen Qualitätswert nicht erhöhen! Aktueller Qualitätswert: " + wineDto.getQuality())
                .isEqualTo(2);
    }

    @Test
    void testCalculateQualityFor10DaysOldWine() {
        wineDto.setQuality(2);
        wineDto.setEntryDate(LocalDate.now().minusDays(10));
        assertThat(cut.calculateQuality(wineDto))
                .describedAs("Der Aufruf sollte den aktuellen Qualitätswert erhöhen! Aktueller Qualitätswert : " +  wineDto.getQuality())
                .isEqualTo(3);
    }

    @Test
    void testCalculateQualityFor100DaysOldHighQualityWine() {
        wineDto.setQuality(50);
        wineDto.setEntryDate(LocalDate.now().minusDays(100));
        assertThat(cut.calculateQuality(wineDto))
                .describedAs("Der Aufruf sollte den aktuellen Qualitätswert nicht erhöhen! Aktueller Qualitätswert: " + wineDto.getQuality())
                .isEqualTo(50);
    }

    @Test
    void testCalculateQualityFor100DaysOldLowQualityWine() {
        wineDto.setQuality(5);
        wineDto.setEntryDate(LocalDate.now().minusDays(100));
        assertThat(cut.calculateQuality(wineDto))
                .describedAs("Der Aufruf sollte den aktuellen Qualitätswert erhöhen! Aktueller Qualitätswert : " +  wineDto.getQuality())
                .isEqualTo(6);
    }

    @Test
    void testCalculateDayPrice() {
        wineDto.setPrice(15.99);
        assertThat(cut.calculateDayPrice(wineDto))
                .describedAs("Der Aufruf sollte den Preis nicht verändern. Aktueller Preis: " + wineDto.getPrice())
                .isEqualTo(15.99);
    }

    @Test
    void testRemoveProductWhenQualityIsZero() {
        wineDto.setQuality(0);
        assertThat(cut.removeProduct(wineDto))
                .describedAs("Rückgabewert sollte hier false sein, da die Qualität im Rahmen ist.")
                .isFalse();
    }

    @Test
    void testRemoveProductWhenQualityIsNegative() {
        wineDto.setQuality(-5);
        assertThat(cut.removeProduct(wineDto))
                .describedAs("Rückgabewert sollte hier true sein, da der Wein seine Qualitätsgrenze unterschritten hat.")
                .isTrue();
    }
}