package de.benjamin1006.productmanagement.core.processing;


import de.benjamin1006.productmanagement.core.dto.ProductDto;

/**
 * Interface für das Strategy Pattern für das Verarbeiten von Produkten.
 * @author Benjamin Woitczyk
 */
public interface IProductProcessingStrategy {

    /**
     * Überprüft, ob das übergebene Produkt den richtigen Typ hat.
     *
     * @param product das zu überprüfende Produkt
     * @return true, wenn es der richtige Produkttyp ist
     */
    boolean isCorrectType(Object product);

    /**
     * Methode zum Berechnen der aktuellen Qualität des übergebenen Objekts vom Typ ProductDto
     * @param productDto das Objekt für das die Qualität berechnet werden soll
     * @return der neue Qualitätswert
     */
    int calculateQuality(ProductDto productDto);

    /**
     * Methode zum Berechnen des aktuellen Tagespreises für das übergebene Produkt
     * @param productDto das Objekt für das die der Tagespreis berechnet werden soll
     * @return der neue Tagespreis
     */
    double calculateDayPrice(ProductDto productDto);

    /**
     * Überprüft, ob das übergebene Objekt vom Typ ProductDto aussortiert werden muss. Dies geschieht entweder aufgrund
     * einer zu niedrigen Qualität, oder weil das Produkt sein Verfallsdatum überschritten hat.
     * @param productDto das Objekt welches überprüft werden soll
     * @return true, wenn das Produkt entfernt werden soll
     */
    boolean removeProduct(ProductDto productDto);
}
