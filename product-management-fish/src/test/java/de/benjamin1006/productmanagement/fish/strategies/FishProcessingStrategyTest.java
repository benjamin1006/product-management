package de.benjamin1006.productmanagement.fish.strategies;

import de.benjamin1006.productmanagement.core.dto.CheeseDto;
import de.benjamin1006.productmanagement.core.dto.ProductDto;
import de.benjamin1006.productmanagement.core.dto.WineDto;
import de.benjamin1006.productmanagement.core.processing.days.ICurrentDayProvider;
import de.benjamin1006.productmanagement.fish.dto.FishDto;
import de.benjamin1006.productmanagement.fish.dto.FishDtoBuilder;
import de.benjamin1006.productmanagement.fish.dto.FishCondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Benjamin Woitczyk
 */
class FishProcessingStrategyTest {

    private final ICurrentDayProvider currentDayProvider = Mockito.mock(ICurrentDayProvider.class);
    private final FishProcessingStrategy cut = new FishProcessingStrategy(currentDayProvider);

    @BeforeEach
    void setUp() {
        //Wenn der mock nach dem aktuellen Tag gefragt, wird soll er einfach den aktuellen Tag zurückgeben.
        Mockito.when(currentDayProvider.getCurrentDay()).thenReturn(LocalDate.now());
    }


    /**
     * Test für die Methode {@link FishProcessingStrategy#removeProduct(ProductDto)}.
     * In dieser Methode wird mit einem Fisch innerhalb der Rahmenbedingungen getestet.
     * Von daher wird erwartet, dass die Methode false liefert und die Felder sich nicht ändern.
     */
    @Test
    void testRemoveProductGoodFish() {
        final FishDto product = FishDtoBuilder.aFish()
                .withType("fisch")
                .withQuality(40)
                .withPrice(1.99)
                .withExpirationDate(LocalDate.now().plusDays(6))
                .withFishCondition(FishCondition.FRESH)
                .withCatchDate(LocalDate.now().minusDays(1))
                .withBasePrice(1.99)
                .build();

        assertThat(cut.removeProduct(product))
                .describedAs("Rückgabewert sollte hier false sein, da seine Qualität und Verfallsdatum im Rahmen sind.")
                .isFalse();
        assertThat(product.getFishCondition())
                .describedAs("Der Wert für das Feld fishCondition sollte sich nicht verändert haben.")
                .isEqualTo(FishCondition.FRESH);
        assertThat(product.getBasePrice())
                .describedAs("Der Wert für das Feld basePrice sollte sich nicht verändert haben.")
                .isEqualTo(1.99);
        assertThat(product.getPrice())
                .describedAs("Der Wert für das Feld price sollte sich nicht verändert haben.")
                .isEqualTo(1.99);
    }

    /**
     * Test für die Methode {@link FishProcessingStrategy#removeProduct(ProductDto)}.
     * In dieser Methode wird mit einem Fisch, der unterhalb der Qualitätsgrenze liegt getestet.
     * Von daher wird erwartet, dass die Methode trze liefert und die Felder sich nicht ändern.
     */
    @Test
    void testRemoveProductLowQualityFish() {
        final FishDto product = FishDtoBuilder.aFish()
                .withType("fisch")
                .withQuality(39)
                .withPrice(1.99)
                .withExpirationDate(LocalDate.now().plusDays(6))
                .withFishCondition(FishCondition.FRESH)
                .withCatchDate(LocalDate.now().minusDays(1))
                .withBasePrice(1.99)
                .build();

        assertThat(cut.removeProduct(product))
                .describedAs("Rückgabewert sollte hier true sein, da seine Qualität unterhalb der Qualitätsgrenze liegt.")
                .isTrue();
        assertThat(product.getFishCondition())
                .describedAs("Der Wert für das Feld fishCondition sollte sich nicht verändert haben.")
                .isEqualTo(FishCondition.FRESH);
        assertThat(product.getBasePrice())
                .describedAs("Der Wert für das Feld basePrice sollte sich nicht verändert haben.")
                .isEqualTo(1.99);
        assertThat(product.getPrice())
                .describedAs("Der Wert für das Feld price sollte sich nicht verändert haben.")
                .isEqualTo(1.99);
    }

    /**
     * Test für die Methode {@link FishProcessingStrategy#removeProduct(ProductDto)}.
     * In dieser Methode wird mit einem Fisch, der sein Verfallsdatum überschritten hat, getestet.
     * Von daher wird erwartet, dass die Methode true liefert und die Felder sich nicht ändern.
     */
    @Test
    void testRemoveProductExpiredFish() {
        final FishDto product = FishDtoBuilder.aFish()
                .withType("fisch")
                .withQuality(41)
                .withPrice(1.99)
                .withExpirationDate(LocalDate.now().minusDays(1))
                .withFishCondition(FishCondition.FRESH)
                .withCatchDate(LocalDate.now().minusDays(2))
                .withBasePrice(1.99)
                .build();

        assertThat(cut.removeProduct(product))
                .describedAs("Rückgabewert sollte hier true sein, da das Verfallsdatum überschritten wurde.")
                .isTrue();
        assertThat(product.getFishCondition())
                .describedAs("Der Wert für das Feld fishCondition sollte sich nicht verändert haben.")
                .isEqualTo(FishCondition.FRESH);
        assertThat(product.getBasePrice())
                .describedAs("Der Wert für das Feld basePrice sollte sich nicht verändert haben.")
                .isEqualTo(1.99);
        assertThat(product.getPrice())
                .describedAs("Der Wert für das Feld price sollte sich nicht verändert haben.")
                .isEqualTo(1.99);
    }

    /**
     * Test für die Methode {@link FishProcessingStrategy#removeProduct(ProductDto)}.
     * In dieser Methode wird mit einem gefrorenen Fisch innerhalb der Rahmenbedingungen getestet.
     * Von daher wird erwartet, dass die Methode false liefert und die Felder sich nicht ändern.
     */
    @Test
    void testRemoveProductGoodFrozenFish() {
        final FishDto product = FishDtoBuilder.aFish()
                .withType("fisch")
                .withQuality(41)
                .withPrice(1.99)
                .withExpirationDate(LocalDate.now())
                .withFishCondition(FishCondition.FROZEN)
                .withCatchDate(LocalDate.now().minusDays(1))
                .withBasePrice(1.99)
                .build();

        assertThat(cut.removeProduct(product))
                .describedAs("Rückgabewert sollte hier false sein, da seine Qualität und Verfallsdatum im Rahmen sind.")
                .isFalse();
        assertThat(product.getFishCondition())
                .describedAs("Der Wert für das Feld fishCondition sollte sich nicht verändert haben.")
                .isEqualTo(FishCondition.FROZEN);
        assertThat(product.getBasePrice())
                .describedAs("Der Wert für das Feld basePrice sollte sich nicht verändert haben.")
                .isEqualTo(1.99);
        assertThat(product.getPrice())
                .describedAs("Der Wert für das Feld price sollte sich nicht verändert haben.")
                .isEqualTo(1.99);
    }

    /**
     * Test für die Methode {@link FishProcessingStrategy#removeProduct(ProductDto)}.
     * In dieser Methode wird mit einem gefrorenen Fisch, der sein Verfallsdatum überschritten hat, getestet.
     * Von daher wird erwartet, dass die Methode true liefert und die Felder sich nicht ändern.
     */
    @Test
    void testRemoveProductExpiredFrozenFish() {
        final FishDto product = FishDtoBuilder.aFish()
                .withType("fisch")
                .withQuality(41)
                .withPrice(1.99)
                .withExpirationDate(LocalDate.now().minusDays(1))
                .withFishCondition(FishCondition.FROZEN)
                .withCatchDate(LocalDate.now().minusDays(2))
                .withBasePrice(1.99)
                .build();

        assertThat(cut.removeProduct(product))
                .describedAs("Rückgabewert sollte hier true sein, da das Verfallsdatum überschritten wurde.")
                .isTrue();
        assertThat(product.getFishCondition())
                .describedAs("Der Wert für das Feld fishCondition sollte sich nicht verändert haben.")
                .isEqualTo(FishCondition.FROZEN);
        assertThat(product.getBasePrice())
                .describedAs("Der Wert für das Feld basePrice sollte sich nicht verändert haben.")
                .isEqualTo(1.99);
        assertThat(product.getPrice())
                .describedAs("Der Wert für das Feld price sollte sich nicht verändert haben.")
                .isEqualTo(1.99);
    }

    /**
     * Test für die Methode {@link FishProcessingStrategy#calculateQuality(ProductDto)}.
     * In dieser Methode wird mit einem Fisch innerhalb der Rahmenbedingungen getestet.
     * Von daher wird erwartet, dass die Methode den Ursprungswert des Feldes Qualität um zwei verringert.
     */
    @Test
    void testCalculateQualityFreshFish() {
        final FishDto product = FishDtoBuilder.aFish()
                .withType("fisch")
                .withQuality(43)
                .withPrice(1.99)
                .withExpirationDate(LocalDate.now().plusDays(6))
                .withFishCondition(FishCondition.FRESH)
                .withCatchDate(LocalDate.now().minusDays(1))
                .withBasePrice(1.99)
                .build();

        assertThat(cut.calculateQuality(product))
                .describedAs("Die Methode sollten den Ausgangswert um zwei verringern! Ausgangswert: " + product.getQuality())
                .isEqualTo(41);
    }
    /**
     * Test für die Methode {@link FishProcessingStrategy#calculateQuality(ProductDto)}.
     * In dieser Methode wird mit einem Fisch getestet, der eine Qualität von 40 besitzt, diese sollte dazu führen,
     * dass er seinen Status auf gefroren ändert und auch die Felder price, basePrice und expirationDate sollten sich ändern.
     */
    @Test
    void testCalculateQualityFreshFishToFrozenFish() {
        final FishDto product = FishDtoBuilder.aFish()
                .withType("fisch")
                .withQuality(42)
                .withPrice(1.99)
                .withExpirationDate(LocalDate.now().plusDays(6))
                .withFishCondition(FishCondition.FRESH)
                .withCatchDate(LocalDate.now().minusDays(1))
                .withBasePrice(1.99)
                .build();

        assertThat(cut.calculateQuality(product))
                .describedAs("Die Qualität sollte sich um zwei Punkte nach unten korrigieren! Ausgangswert: "+ product.getQuality())
                .isEqualTo(40);
        assertThat(product.getFishCondition())
                .describedAs("Der Wert für das Feld fishCondition sollte sich auf FROZEN verändert haben. Tatsächlicher Wert: FROZEN")
                .isEqualTo(FishCondition.FROZEN);
        assertThat(product.getBasePrice())
                .describedAs("Der Wert für das Feld basePrice sollte sich verändert haben.")
                .isEqualTo(2.49);
        assertThat(product.getPrice())
                .describedAs("Der Wert für das Feld price sollte sich verändert haben.")
                .isEqualTo(2.49);
        assertThat(product.getExpirationDate())
                .describedAs("Der Wert für das Feld expirationDate sollte sich auf " + LocalDate.now().plusDays(30) + " geändert haben!")
                .isEqualTo(LocalDate.now().plusDays(30));
    }

    /**
     * Test für die Methode {@link FishProcessingStrategy#calculateQuality(ProductDto)}.
     * In dieser Methode wird mit einem gefrorenen Fisch getestet, der eine Qualität von 40 besitzt, diese sollte dazu führen,
     * dass sich seine Qualität nicht reduziert.
     */
    @Test
    void testCalculateQualityFrozenFish() {
        final FishDto product = FishDtoBuilder.aFish()
                .withType("fisch")
                .withQuality(40)
                .withPrice(1.99)
                .withExpirationDate(LocalDate.now().plusDays(6))
                .withFishCondition(FishCondition.FROZEN)
                .withCatchDate(LocalDate.now().minusDays(1))
                .withBasePrice(1.99)
                .build();

        assertThat(cut.calculateQuality(product))
                .describedAs("Das es sich um gefrorenen Fisch handelt, sollte die Qualität sich nicht verändern!")
                .isEqualTo(40);
    }

    /**
     * Test für die Methode {@link FishProcessingStrategy#calculateDayPrice(ProductDto)}.
     * Da es sich um fischen Frisch handelt, sollte sich das Feld price auf Grundlage der Formel basePrice + 0.1 * quality anpassen.
     */
    @Test
    void testCalculateDayPriceFish() {
        final FishDto product = FishDtoBuilder.aFish()
                .withType("fisch")
                .withQuality(40)
                .withPrice(1.99)
                .withExpirationDate(LocalDate.now().plusDays(6))
                .withFishCondition(FishCondition.FRESH)
                .withCatchDate(LocalDate.now().minusDays(1))
                .withBasePrice(1.99)
                .build();

        assertThat(cut.calculateDayPrice(product))
                .describedAs("Der Wert für das Feld price sollte sich erhöhen.")
                .isEqualTo(5.99);
    }

    /**
     * Test für die Methode {@link FishProcessingStrategy#calculateDayPrice(ProductDto)}.
     * Da es sich um fischen Frisch handelt, sollte sich das Feld price um 0.05 erhöhen..
     */
    @Test
    void testCalculateDayPriceFrozenFish() {
        final FishDto product = FishDtoBuilder.aFish()
                .withType("fisch")
                .withQuality(40)
                .withPrice(1.99)
                .withExpirationDate(LocalDate.now().plusDays(6))
                .withFishCondition(FishCondition.FROZEN)
                .withCatchDate(LocalDate.now().minusDays(1))
                .withBasePrice(1.99)
                .build();

        assertThat(cut.calculateDayPrice(product))
                .describedAs("Der Wert für das Feld price sollte sich erhöhen.")
                .isEqualTo(2.04);
    }

    @Test
    void testIsCorrectTypeWithFish() {
        final FishDto fishDto = FishDtoBuilder.aFish().build();
        assertThat(cut.isCorrectType(fishDto))
                .describedAs("Das es sich beim Objekt Typ um den Typ Fish handelt sollte die Methode true zurückgeben")
                .isTrue();
    }

    @Test
    void testIsCorrectTypeWithNull() {
        assertThat(cut.isCorrectType(null))
                .describedAs("Das es sich beim Objekt Typ nicht um den Typ Fish handelt sollte die Methode false zurückgeben")
                .isFalse();
    }

    @Test
    void testIsCorrectTypeWithWine() {
        assertThat(cut.isCorrectType(new WineDto("wine", 5, 5.99, LocalDate.now())))
                .describedAs("Das es sich beim Objekt Typ nicht um den Typ Fish handelt sollte die Methode false zurückgeben")
                .isFalse();
    }

    @Test
    void testIsCorrectTypeWithGenericObject() {
        assertThat(cut.isCorrectType(new Object()))
                .describedAs("Das es sich beim Objekt Typ nicht um den Typ Fish handelt sollte die Methode false zurückgeben")
                .isFalse();
    }

    @Test
    void testIsCorrectTypeWithCheese() {
        assertThat(cut.isCorrectType(new CheeseDto("käse", 30, LocalDate.now().plusDays(50), 4.99, 1.99)))
                .describedAs("Das es sich beim Objekt Typ nicht um den Typ Fish handelt sollte die Methode false zurückgeben")
                .isFalse();
    }
}