package com.honeyai.repository;

import com.honeyai.enums.HoneyType;
import com.honeyai.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Find all products ordered by name ascending.
     */
    List<Product> findAllByOrderByNameAsc();

    /**
     * Find product by honey type and unit (format).
     */
    Optional<Product> findByTypeAndUnit(HoneyType type, String unit);
}
