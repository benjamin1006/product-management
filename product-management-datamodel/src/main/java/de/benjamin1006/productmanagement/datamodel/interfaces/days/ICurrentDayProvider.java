package de.benjamin1006.productmanagement.datamodel.interfaces.days;

import java.time.LocalDate;

/**
 * Interface welches den aktuellen Tag zurückgeben soll, da wir im ersten Schritt die Tage simulieren, ist das hier nötig.
 * @author Benjamin Woitczyk
 */
public interface ICurrentDayProvider {
    LocalDate getCurrentDay();
    void setCurrentDay(LocalDate localDate);
}
