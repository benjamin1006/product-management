package de.benjamin1006.productmanagement.observer.manager;

import de.benjamin1006.productmanagement.datamodel.dto.Cheese;
import de.benjamin1006.productmanagement.datamodel.dto.Product;
import de.benjamin1006.productmanagement.observer.EventType;
import de.benjamin1006.productmanagement.observer.listener.IEventListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


/**
 * @author Benjamin Woitczyk
 */
class ProductManagementEventManagerTest {

    private ProductManagementEventManager cut;

    private IEventListener listener;

    private LocalDate today;
    private Product product;

    @BeforeEach
    void setUp() {
        product = new Cheese("käse", 40, LocalDate.now().plusDays(50L), 1.99);
        MockitoAnnotations.openMocks(this);
        cut = new ProductManagementEventManager();
        listener = Mockito.mock(IEventListener.class);
        cut.init();
        today = LocalDate.now();
    }

    @Test
    void testSubscribeAndUnsubscribe() {
        cut.subscribe(listener, EventType.UPDATE);

        List<IEventListener> updateListeners = cut.listeners.get(EventType.UPDATE);
        assertThat(updateListeners)
                .describedAs("Nachdem subscriben sollten ein Listener vorhanden sein")
                .contains((listener));

        cut.unsubscribe(listener, EventType.UPDATE);

        updateListeners = cut.listeners.get(EventType.UPDATE);
        assertThat(updateListeners)
                .describedAs("Nachdem unsubscriben sollte der Listener nicht mehr vorhanden sein")
                .doesNotContain((listener));
    }

    @Test
    void testNotifyProductObservers() {
        cut.subscribe(listener, EventType.UPDATE);

        cut.notifyProductObservers(EventType.UPDATE, product);

        verify(listener, times(1)).updateProduct(EventType.UPDATE, product);
    }

    @Test
    void testNotifyNewDayObservers() {
        cut.subscribe(listener, EventType.NEW_DAY);

        cut.notifyNewDayObservers(EventType.NEW_DAY, today);

        verify(listener, times(1)).updateNewDay(EventType.NEW_DAY, today);
    }

    @Test
    void testNotifyProductListObservers() {
        List<Product> productList = new ArrayList<>();
        cut.subscribe(listener, EventType.UPDATE);

        cut.notifyProductListObservers(EventType.UPDATE, productList);

        verify(listener, times(1)).updateProductList(EventType.UPDATE, productList);
    }
}