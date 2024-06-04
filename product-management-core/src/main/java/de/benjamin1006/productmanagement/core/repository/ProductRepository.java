package de.benjamin1006.productmanagement.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findByTypeAndPriceAndQualityAndExpirationDate(String type, double price, int quality, LocalDate expirationDate);
}