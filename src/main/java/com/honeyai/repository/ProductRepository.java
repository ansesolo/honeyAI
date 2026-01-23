package com.honeyai.repository;

import com.honeyai.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Find all products ordered by name ascending.
     */
    List<Product> findAllByOrderByNameAsc();
}
