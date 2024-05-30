package de.benjamin1006.productmanagement.datamodel.dto;


import java.time.LocalDate;

/**
 * @author Benjamin Woitczyk
 */
public class Wine extends Product {

    private LocalDate entryDate;

    /**
     * Wine hat kein Verfallsdatum, daher wird das Feld der Elternklasse Product mit null initialisiert.
     */
    public Wine(String type, int quality, double price, LocalDate entryDate) {
        super(type, quality, null, price);
        this.entryDate = entryDate;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }
}
