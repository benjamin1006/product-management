package de.benjamin1006.productmanagement.fish.repository;

import de.benjamin1006.productmanagement.core.repository.ProductEntity;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * @author Benjamin Woitczyk
 */
@Entity
@DiscriminatorValue("Fish")
public class FishEntity extends ProductEntity {
}
