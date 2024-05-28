package de.benjamin1006.productmanagement.core.dto;


import java.time.LocalDate;
import java.util.StringJoiner;

/**
 * @author Benjamin Woitczyk
 */
public class Wine extends Product {

    private LocalDate entryDate;

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Wine.class.getSimpleName() + "[", "]")
                .add(super.toString())
                .add("entryDate=" + entryDate)
                .toString();
    }
}
