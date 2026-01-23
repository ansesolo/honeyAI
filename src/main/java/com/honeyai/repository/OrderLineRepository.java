package com.honeyai.repository;

import com.honeyai.model.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {
    // Standard CRUD operations sufficient per story requirements
}
