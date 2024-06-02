package de.benjamin1006.productmanagement.core.interfaces.strategy.processing;


import de.benjamin1006.productmanagement.core.dto.Product;

/**
 * @author Benjamin Woitczyk
 */
public interface IProductProcessingStrategy {

    boolean isCorrectType(Object product);

    int calculateQuality(Product product);

    double calculateDayPrice(Product product);

    boolean removeProduct(Product product);
}
