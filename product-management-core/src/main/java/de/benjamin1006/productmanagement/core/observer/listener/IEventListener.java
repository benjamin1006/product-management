package de.benjamin1006.productmanagement.core.observer.listener;

import de.benjamin1006.productmanagement.core.dto.ProductDto;
import de.benjamin1006.productmanagement.core.observer.EventType;

import java.time.LocalDate;
import java.util.List;


/**
 * Observer Interface für das ObserverPattern bietet mehrere Möglichkeiten auf Aktionen rund um das Product Management zu hören.
 * @author Benjamin Woitczyk
 */
public interface IEventListener {
    /**
     * Methode für das Updaten von einzelnen Objekten des Types Product.
     * @param eventType die Art Event für das Objekt vom Typ Product
     * @param productDto das aktualisierte Objekt vom Typ Product
     */
    void updateProduct(EventType eventType, ProductDto productDto);

    /**
     * Methode für das Updaten von Tagen
     * @param eventType die Art Event
     * @param newDay das aktualisierte Tag Objekt
     */
    void updateNewDay(EventType eventType, LocalDate newDay);

    /**
     * Methode für das Updaten von Liste vom Typ Product
     * @param eventType die Art Event für das Objekt vom Typ Product
     * @param productDtoList die Liste mit den aktualisierten Objekten vom Typ Product
     */
    void updateProductList(EventType eventType, List<ProductDto> productDtoList);
}
