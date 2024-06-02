package de.benjamin1006.productmanagement.core.processing;

import de.benjamin1006.productmanagement.core.dto.ProductDto;
import de.benjamin1006.productmanagement.core.observer.EventType;
import de.benjamin1006.productmanagement.core.observer.listener.IEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementiert das Interface {@link IEventListener}
 * @author Benjamin Woitczyk
 */

@Component
public class NotificationService implements IEventListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @Override
    public void updateProduct(EventType eventType, ProductDto productDto) {
        if (eventType.equals(EventType.REMOVE)) {
            log.info("{} Bitte ausräumen!", productDto);
        } else if (eventType.equals(EventType.UPDATE)) {
            log.info("{}", productDto);
        }
    }

    @Override
    public void updateNewDay(EventType eventType, LocalDate newDay) {
        log.info("--------PRODUKTE AM {}---------", newDay);
    }

    @Override
    public void updateProductList(EventType eventType, List<ProductDto> productDtoList) {
        if (eventType.equals(EventType.CREATE)) {
            log.info("----------STARTWERTE ALLER PRODUKTE----------");
            productDtoList.forEach(product -> log.info(product.toString()));
        }
    }
}
