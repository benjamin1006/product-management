package de.benjamin1006.productmanagement.fish.strategies;

import java.time.LocalDate;

/**
 * @author Benjamin Woitczyk
 */
public class FishDtoUtil {

    private FishDtoUtil() {
    }

    public static LocalDate calculateCatchDate() {
        return LocalDate.now().minusDays(1);
    }

    public static LocalDate calculateExpirationDate(LocalDate catchDate) {
        return catchDate.plusDays(7);
    }
}
