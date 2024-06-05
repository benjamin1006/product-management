package de.benjamin1006.productmanagement.fish.strategies;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Benjamin Woitczyk
 */
class FishDtoUtilTest {

    @Test
    void testCalculateCatchDate() {
        LocalDate expected = LocalDate.now().minusDays(1);
        LocalDate actual = FishDtoUtil.calculateCatchDate();
        assertThat(actual)
                .describedAs("Es wird erwartet, dass das Catch Date ein Tag in der Vergangenheit liegt.")
                .isEqualTo(expected);
    }

    @Test
    void testCalculateExpirationDate() {
        LocalDate catchDate = LocalDate.of(2024, 1, 1);
        LocalDate expected = catchDate.plusDays(7);
        LocalDate actual = FishDtoUtil.calculateExpirationDate(catchDate);
        assertThat(actual)
                .describedAs("Es wird erwartet, dass das Verfallsdatum sieben Tage in der Zukunft liegt.")
                .isEqualTo(expected);
    }
}