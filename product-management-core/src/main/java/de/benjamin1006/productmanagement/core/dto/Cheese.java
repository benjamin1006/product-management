package de.benjamin1006.productmanagement.core.dto;

import java.time.LocalDate;

/**
 * @author Benjamin Woitczyk
 */
public class Cheese extends Product {

    public Cheese(String type, int quality, LocalDate expirationDate, double price) {
        super(type, quality, expirationDate, price);
    }
}
