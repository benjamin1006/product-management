package de.benjamin1006.productmanagement.core.dataimport.csv.strategies;

import de.benjamin1006.productmanagement.core.dto.Product;

/**
 * Strategy Pattern für das Mappen von Csv auf verschiedene Produkte.
 * @author Benjamin Woitczyk
 */
public interface ICsvMapperStrategy {
    /**
     * Hilft dabei den passenden Mapper Type zu identifizieren, hierbei wird ein String zurückgegeben,
     * welcher mit der Bezeichnung/type des Objekts vom Typ Product übereinstimmen sollte um die richtige Strategie zu finden-
     * @return Type String mit der Bezeichnung/type des zur Strategie passenden Product Typs.
     */
    String getMapperType();

    /**
     * Mappt die aktuelle Zeile aus der Csv-Datei auf den gewünschten Typ der Klasse Product
     * @param line die zu mappende Zeile
     * @return das Objekt vom Typ Product
     */
    Product mapTo(String[] line);
}
