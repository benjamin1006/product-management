package de.benjamin1006.productmanagement.csvimport.strategies;

import de.benjamin1006.productmanagement.core.processing.days.ICurrentDayProvider;
import de.benjamin1006.productmanagement.core.dto.ProductDto;
import de.benjamin1006.productmanagement.core.dto.WineDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Benjamin Woitczyk
 */
class WineMapperStrategyTest {

    private final ICurrentDayProvider currentDayProvider = Mockito.mock(ICurrentDayProvider.class);
    private final WineMapperStrategy cut = new WineMapperStrategy(currentDayProvider);

    @Test
    void getMapperType() {
        assertThat(cut.getMapperType())
                .describedAs("Da es sich um eine WineMapperStrategy handelt sollte hier Fisch returned wein")
                .isEqualTo("wein");
    }

    @Test
    void testMapTo() {

        Mockito.when(currentDayProvider.getCurrentDay()).thenReturn(LocalDate.now());

        String[] testData = {"wein", "-1", "3.99"};

        final ProductDto wine = cut.mapTo(testData);

        assertThat(wine)
                .describedAs("Das Objekt sollte nicht null sein.")
                .isNotNull()
                .describedAs("Das Objekt sollte vom Typ Wine.class sein! Tatsächlicher Wert: " + wine.getClass())
                .isInstanceOf(WineDto.class);

        assertThat(wine.getType())
                .describedAs("Der typ sollte wein sein! Tatsächlicher Wert: " + wine.getType())
                .isEqualTo("wein");
        assertThat(wine.getQuality())
                .describedAs("Die Qualität sollte -1 sein! Tatsächlicher Wert: " + wine.getQuality())
                .isEqualTo(-1);
        assertThat(wine.getPrice())
                .describedAs("Der preis sollte bei 3.99 liegen! Tatsächlicher Wert: " + wine.getPrice())
                .isEqualTo(3.99);

        final WineDto wineDtoCasted = (WineDto) wine;
        final LocalDate expirationdate = wineDtoCasted.getExpirationDate();
        assertThat(expirationdate)
                .describedAs("Das expirationDate sollte null sein, da Wein nicht schlecht werden kann!")
                .isNull();
    }

}