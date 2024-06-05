package de.benjamin1006.productmanagement.core.component.strategy;

import de.benjamin1006.productmanagement.core.dto.ProductDto;
import de.benjamin1006.productmanagement.core.repository.ProductEntity;

/**
 * Strategy Pattern für das Mappen von Entities auf verschiedene Produkte.
 * @author Benjamin Woitczyk
 */
public interface IEntityMapperStrategy {

    /**
     * Hilft dabei den passenden Mapper Type zu identifizieren, hierbei wird ein String zurückgegeben,
     * welcher mit der Bezeichnung/type des Objekts vom Typ Product übereinstimmen sollte, um die richtige Strategie zu finden.
     * @return Type String mit der Bezeichnung/type des zur Strategie passenden Product Typs.
     */
    String getMapperType();

    /**
     * Mappt die aktuelle Entity auf den passenden Typ der Klasse Product
     * @param entity die zu mappende Entity
     * @return das Objekt vom Typ Product
     */
    ProductDto mapEntityToDto(ProductEntity entity);
}
