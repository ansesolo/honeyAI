package com.honeyai.repository;

import com.honeyai.model.Price;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PriceRepository extends JpaRepository<Price, Long> {

    /**
     * Find price for a specific product and year.
     */
    Optional<Price> findByProductIdAndYear(Long productId, Integer year);

    /**
     * Find all prices for a specific year.
     */
    List<Price> findByYear(Integer year);
}
