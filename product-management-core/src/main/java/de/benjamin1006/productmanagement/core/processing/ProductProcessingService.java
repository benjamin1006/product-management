package de.benjamin1006.productmanagement.core.processing;

import de.benjamin1006.productmanagement.core.processing.strategies.IProductProcessingStrategy;
import de.benjamin1006.productmanagement.datamodel.dto.Product;
import de.benjamin1006.productmanagement.datamodel.exception.ProductNotFoundException;
import de.benjamin1006.productmanagement.observer.manager.IEventManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static de.benjamin1006.productmanagement.observer.EventType.REMOVE;
import static de.benjamin1006.productmanagement.observer.EventType.UPDATE;

/**
 * Service für die Verarbeitung von Produkten, nutzt das StrategyPattern für die Verarbeitung({@link IProductProcessingStrategy}
 * und das ObserverPattern für das Versenden von Notifications
 *
 * @author Benjamin Woitczyk
 */
@Service
public class ProductProcessingService {


    private final List<IProductProcessingStrategy> iProductProcessingStrategies;
    private final IEventManager productManagementEventManager;

    public ProductProcessingService(List<IProductProcessingStrategy> iProductProcessingStrategies,
                                    IEventManager productManagementEventManager
    ) {
        this.iProductProcessingStrategies = iProductProcessingStrategies;
        this.productManagementEventManager = productManagementEventManager;
    }

    /**
     * Führt die Verarbeitung von Produkten für einen kompletten Tag durch, dafür wird die {@link IProductProcessingStrategy}
     * genutzt. In dieser Methode werden sowohl die Qualität als auch der Preis angepasst. Zusätzlich werden abgelaufene oder
     * unter die Qualitätsgrenze gefallene Produkte entfernt.
     * Zusätzlich werden mithilfe des ObserverPatterns, die Produkte ausgegeben.
     * @param productList die Liste für die die Verarbeitung durchgeführt werden soll
     * @return die verarbeitete Liste ohne Produkte die entfernt werden müssen
     */
    public List<Product> processProductsForOneDay(List<Product> productList) {

        productList = productList.stream()
                .filter(product -> {
                    updateQualityAndPrice(product);
                    final boolean removeProduct = removeLowQualityOrExpiredProduct(product);
                    if (removeProduct) {
                        productManagementEventManager.notifyProductObservers(REMOVE, product);

                    } else {
                        productManagementEventManager.notifyProductObservers(UPDATE, product);
                    }
                    //Wir drehen hier den booleschen Wert um, da wird die Produkte ja aus der Liste entfernen wollen,
                    // die nicht mehr den Rahmenbedingungen entsprechen
                    return !removeProduct;
                }).toList();

        return productList;
    }

    /**
     * Überprüft, ob das übergebene Produkt abgelaufene oder unter die Qualitätsgrenze gefallen ist.
     * @param product das zu überprüfen Produkt
     * @return true, wenn das Produkt entfernt werden soll oder false, wenn es nicht entfernt werden soll.
     */
    public boolean removeLowQualityOrExpiredProduct(Product product) {
        final Optional<Boolean> removeProduct = iProductProcessingStrategies.stream()
                .filter(iProductProcessingStrategy -> iProductProcessingStrategy.isCorrectType(product))
                .findAny()
                .map(iProductProcessingStrategy -> iProductProcessingStrategy.removeProduct(product));

        return removeProduct
                .orElseThrow(() -> new ProductNotFoundException(String.format("Es gibt keine ProductProcessingStrategy Implementierung für das Product vom Typ %s!", product.getType())));
    }

    /**
     * In dieser Methode werden sowohl die Qualität als auch der Preis angepasst
     * @param product das anzupassende Produkt
     */
    private void updateQualityAndPrice(Product product) {
        iProductProcessingStrategies.stream()
                .filter(iProductProcessingStrategy -> iProductProcessingStrategy.isCorrectType(product))
                .findAny()
                .ifPresent(iProductProcessingStrategy -> {
                    product.setQuality(iProductProcessingStrategy.calculateQuality(product));
                    product.setPrice(iProductProcessingStrategy.calculateDayPrice(product));
                });

    }
}
