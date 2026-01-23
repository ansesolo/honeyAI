package com.honeyai.repository;

import com.honeyai.enums.OrderStatus;
import com.honeyai.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Find all orders for a client, most recent first.
     */
    List<Order> findByClientIdOrderByOrderDateDesc(Long clientId);

    /**
     * Find all orders with a specific status.
     */
    List<Order> findByStatus(OrderStatus status);

    /**
     * Find all orders within a date range.
     */
    List<Order> findByOrderDateBetween(LocalDate start, LocalDate end);

    /**
     * Find orders by year (extracted from dateCommand), sorted by date descending.
     */
    @Query("SELECT o FROM Order o WHERE YEAR(o.orderDate) = :year ORDER BY o.orderDate DESC")
    List<Order> findByYearOrderByOrderDateDesc(@Param("year") Integer year);

    /**
     * Find orders by year and status, sorted by date descending.
     */
    @Query("SELECT o FROM Order o WHERE YEAR(o.orderDate) = :year AND o.status = :status ORDER BY o.orderDate DESC")
    List<Order> findByYearAndStatusOrderByOrderDateDesc(@Param("year") Integer year, @Param("status") OrderStatus status);

    /**
     * Find orders by status, sorted by date descending.
     */
    List<Order> findByStatusOrderByOrderDateDesc(OrderStatus status);

    /**
     * Find all orders sorted by date descending.
     */
    List<Order> findAllByOrderByOrderDateDesc();

    /**
     * Get distinct years from all orders for filter dropdown.
     */
    @Query("SELECT DISTINCT YEAR(o.orderDate) FROM Order o ORDER BY YEAR(o.orderDate) DESC")
    List<Integer> findDistinctYears();
}
