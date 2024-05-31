package de.benjamin1006.productmanagement.observer.manager;

import de.benjamin1006.productmanagement.datamodel.dto.Product;
import de.benjamin1006.productmanagement.observer.EventType;
import de.benjamin1006.productmanagement.observer.listener.IEventListener;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;


/**
 * Implementierung des Interfaces {@link IEventManager}
 *
 * @author Benjamin Woitczyk
 */
@Component
public class ProductManagementEventManager implements IEventManager {

    protected final Map<EventType, List<IEventListener>> listeners = new EnumMap<>(EventType.class);

    @PostConstruct
    @Override
    public void init() {
        Arrays.stream(EventType.values()).forEach(event -> listeners.put(event, new ArrayList<>()));
    }

    @Override
    public void subscribe(IEventListener listener, EventType... eventTypes) {
        for (EventType eventType : eventTypes) {
            List<IEventListener> users = listeners.get(eventType);
            users.add(listener);
        }
    }

    @Override
    public void unsubscribe(IEventListener listener, EventType... eventTypes) {
        for (EventType eventType : eventTypes) {
            List<IEventListener> users = listeners.get(eventType);
            users.remove(listener);
        }
    }

    @Override
    public void notifyProductObservers(EventType eventType, Product product) {
        List<IEventListener> users = listeners.get(eventType);
        for (IEventListener listener : users) {
            listener.updateProduct(eventType, product);
        }
    }

    @Override
    public void notifyNewDayObservers(EventType eventType, LocalDate newDay) {
        final List<IEventListener> users = listeners.get(eventType);
        for (IEventListener user : users) {
            user.updateNewDay(eventType, newDay);
        }
    }

    @Override
    public void notifyProductListObservers(EventType eventType, List<Product> products) {
        final List<IEventListener> users = listeners.get(eventType);
        for (IEventListener user : users) {
            user.updateProductList(eventType, products);
        }
    }
}
