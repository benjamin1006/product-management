package de.benjamin1006.productmanagement.core.processing.strategies;


import de.benjamin1006.productmanagement.datamodel.dto.Product;

/**
 * @author Benjamin Woitczyk
 */
public interface IProductProcessingStrategy {

    boolean isCorrectType(Object product);

    int calculateQuality(Product product);

    double calculateDayPrice(Product product);

    boolean removeProduct(Product product);
}
