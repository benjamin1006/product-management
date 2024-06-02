package de.benjamin1006.productmanagement.fish.strategies;

import de.benjamin1006.productmanagement.core.dto.ProductDto;
import de.benjamin1006.productmanagement.fish.dto.FishDto;
import de.benjamin1006.productmanagement.fish.dto.FishCondition;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Benjamin Woitczyk
 */
class FishMapperStrategyTest {

    private final FishMapperStrategy cut = new FishMapperStrategy();

    @Test
    void getMapperType() {
        assertThat(cut.getMapperType())
                .describedAs("Da es sich um eine FishMapperStrategy handelt sollte hier fisch returned werden")
                .isEqualTo("fisch");
    }

    @Test
    void mapTo() {
        String[] testData = {"fisch", "45", "4.99"};
        final ProductDto fish = cut.mapTo(testData);

        assertThat(fish)
                .describedAs("Das Objekt sollte nicht null sein.")
                .isNotNull()
                .describedAs("Das Objekt sollte vom Typ Fish.class sein! Tatsächlicher Wert: " + fish.getClass())
                .isInstanceOf(FishDto.class);

        assertThat(fish.getType())
                .describedAs("Der typ sollte fisch sein! Tatsächlicher Wert: " + fish.getType())
                .isEqualTo("fisch");
        assertThat(fish.getQuality())
                .describedAs("Die Qualität sollte 45 sein! Tatsächlicher Wert: " + fish.getQuality())
                .isEqualTo(45);
        assertThat(fish.getPrice())
                .describedAs("Der Preis sollte bei 9.49 liegen! Tatsächlicher Wert: " + fish.getPrice())
                .isEqualTo(4.99 + 0.1 * fish.getQuality());
        assertThat(fish.getExpirationDate())
                .describedAs("Das Verfallsdatum sollte sechs Tage in der Zukunft liegen.")
                .isEqualTo(LocalDate.now().plusDays(6));

        final FishDto castedFishDto = (FishDto) fish;

        assertThat(castedFishDto.getFishCondition())
                .describedAs("Die FishConditon sollte FRESH sein")
                .isEqualTo(FishCondition.FRESH);
        assertThat(castedFishDto.getCatchDate())
                .describedAs("Das Fangdatum des Fisches sollte einen Tag in der Vergangenheit liegen")
                .isEqualTo(LocalDate.now().minusDays(1));
        assertThat(castedFishDto.getBasePrice())
                .describedAs("Der Grundpreis des Fisches sollte bei 4.99 liegen! Tatsächlicher Wert: " + castedFishDto.getBasePrice())
                .isEqualTo(4.99);
    }
}