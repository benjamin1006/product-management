package de.benjamin1006.productmanagement.core.dataimport;

import de.benjamin1006.productmanagement.core.dto.Product;

import java.util.List;

/**
 * Interface für das Mappen von verschiedenen Datenquelle auf eine List mit Objekten vom Typ Product
 * @author Benjamin Woitczyk
 */
public interface IDataImport {
    /**
     * Diese Methode importiert Daten und parsed diese dann zu einer Liste vom Typ Product.
     * Achtung diese werden nicht gefiltert.
     * @return eine ungefilterte Liste vom Typ Product
     */
    List<Product> importDataAndParseToProduct();
}
