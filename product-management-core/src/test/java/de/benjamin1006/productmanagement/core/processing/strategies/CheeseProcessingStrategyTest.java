package de.benjamin1006.productmanagement.core.processing.strategies;

import de.benjamin1006.productmanagement.core.dto.CheeseDto;
import de.benjamin1006.productmanagement.core.dto.WineDto;
import de.benjamin1006.productmanagement.core.processing.days.ICurrentDayProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Benjamin Woitczyk
 */
class CheeseProcessingStrategyTest {

    private final ICurrentDayProvider currentDayProvider = Mockito.mock(ICurrentDayProvider.class);
    private final CheeseProcessingStrategy cut = new CheeseProcessingStrategy(currentDayProvider);
    private CheeseDto cheeseDto;

    @BeforeEach
    void setUp() {
        //Wenn der mock nach dem aktuellen Tag gefragt, wird soll er einfach den aktuellen Tag zurückgeben.
        Mockito.when(currentDayProvider.getCurrentDay()).thenReturn(LocalDate.now());
        /*
        Erstellung eines Standardobjekts vom Typ Cheese, die benötigten Felder werden in den jeweiligen Tests
        so überschrieben wie es gebraucht wird
         */
        cheeseDto = new CheeseDto("käse", 10, LocalDate.now().plusDays(50L), 4.99, 1.99);
    }

    @Test
    void testIsCorrectTypeWithGenericObject() {
        assertThat(cut.isCorrectType(new Object()))
                .describedAs("Das es sich beim Objekt Typ nicht um den Typ Cheese handelt sollte die Methode false zurückgeben")
                .isFalse();
    }

    @Test
    void testIsCorrectTypeWithCheese() {
        assertThat(cut.isCorrectType(cheeseDto))
                .describedAs("Das es sich beim Objekt Typ um den Typ Cheese handelt sollte die Methode true zurückgeben")
                .isTrue();
    }

    @Test
    void testIsCorrectTypeWithWine() {
        assertThat(cut.isCorrectType(new WineDto("wine", 5, 5.99, LocalDate.now())))
                .describedAs("Das es sich beim Objekt Typ nicht um den Typ Cheese handelt sollte die Methode false zurückgeben")
                .isFalse();
    }

    @Test
    void testCalculateQuality() {
        assertThat(cut.calculateQuality(cheeseDto))
                .describedAs("Der Aufruf sollte den aktuellen Wert um eins verringern! Aktueller Wert: " + cheeseDto.getQuality())
                .isEqualTo(9);
    }

    @Test
    void testCalculateDayPrice() {
        cheeseDto.setBasePrice(4.99);
        cheeseDto.setQuality(40);

        assertThat(cut.calculateDayPrice(cheeseDto))
                .describedAs("Der Preis sollte 8.99 sein.")
                .isEqualTo(8.99);
    }

    @Test
    void testRemoveProductWithGoodCheese() {
        cheeseDto.setQuality(40);
        cheeseDto.setExpirationDate(LocalDate.now());
        assertThat(cut.removeProduct(cheeseDto))
                .describedAs("Rückgabewert sollte hier false sein, da seine Qualität und Verfallsdatum im Rahmen sind.")
                .isFalse();
    }

    @Test
    void testRemoveProductWithNormalCheese() {
        cheeseDto.setQuality(30);
        cheeseDto.setExpirationDate(LocalDate.now());
        assertThat(cut.removeProduct(cheeseDto))
                .describedAs("Rückgabewert sollte hier false sein, da seine Qualität und Verfallsdatum im Rahmen sind.")
                .isFalse();
    }

    @Test
    void testRemoveProductWithExpiredCheese() {
        cheeseDto.setQuality(40);
        cheeseDto.setExpirationDate(LocalDate.now().minusDays(1));
        assertThat(cut.removeProduct(cheeseDto))
                .describedAs("Rückgabewert sollte hier true sein, da der Käse sein Verfallsdatum überschritten hat.")
                .isTrue();
    }

    @Test
    void testRemoveProductWithLowQualityCheese() {
        cheeseDto.setQuality(29);
        cheeseDto.setExpirationDate(LocalDate.now());
        assertThat(cut.removeProduct(cheeseDto))
                .describedAs("Rückgabewert sollte hier true sein, da der Käse seine Qualitätsgrenze unterschritten hat.")
                .isTrue();
    }

}