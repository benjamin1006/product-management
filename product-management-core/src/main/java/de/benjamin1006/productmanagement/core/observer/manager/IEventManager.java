package de.benjamin1006.productmanagement.core.observer.manager;

import de.benjamin1006.productmanagement.core.dto.ProductDto;
import de.benjamin1006.productmanagement.core.observer.EventType;
import de.benjamin1006.productmanagement.core.observer.listener.IEventListener;

import java.time.LocalDate;
import java.util.List;

/**
 * Publisher Interface für das ObserverPattern bietet mehrere Möglichkeiten Notifications rund um das Thema Product Management zu senden.
 * Zusätzlich bietet es die Möglichkeit sich auf EventTypen zu subscriben und unsubscriben.
 *
 * @author Benjamin Woitczyk
 */
public interface IEventManager {
    /**
     * Erstellt eine Map vom Typ {@link EventType} als Key und eine List vom Typ {@link IEventListener}.
     */
    void init();

    /**
     * Bietet die Möglichkeit {@link IEventListener} auf ein oder mehrere {@link EventType} zu subscriben
     *
     * @param listener   der Listener der sich auf den Event Type subscriben möchte
     * @param eventTypes der EventType auf den sich subscribed werden soll
     */
    void subscribe(IEventListener listener, EventType... eventTypes);

    /**
     * Bietet die Möglichkeit {@link IEventListener} auf ein oder mehrere {@link EventType} zu unsubscriben
     *
     * @param listener   der Listener der sich auf den Event Type unsubscriben möchte
     * @param eventTypes der EventType auf den sich unsubscribed werden soll
     */
    void unsubscribe(IEventListener listener, EventType... eventTypes);

    /**
     * Sendet Notifications rund um einzelne Objekte vom Typ Product
     *
     * @param eventType das Event für das die Notification versendet wird
     * @param productDto   das Objekt vom Typ Product, für das die Notification versendet wird.
     */
    void notifyProductObservers(EventType eventType, ProductDto productDto);

    /**
     * Sendet Notifications beim Ändern des aktuellen Tag Objekts
     *
     * @param eventType das Event für das die Notification versendet wird
     * @param newDay    der neue Tag der versendet wird
     */
    void notifyNewDayObservers(EventType eventType, LocalDate newDay);

    /**
     * Sendet Notifications rund um Listen vom Typ Product
     *
     * @param eventType   das Event für das die Notification versendet wird
     * @param productDtoList die Liste vom Typ Product, für das die Notification versendet wird.
     */
    void notifyProductListObservers(EventType eventType, List<ProductDto> productDtoList);
}
