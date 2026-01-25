package com.honeyai.repository;

import com.honeyai.enums.OrderStatus;
import com.honeyai.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Find order by ID with client, lines, and products eagerly loaded.
     */
    @Query("SELECT o FROM Order o JOIN FETCH o.client LEFT JOIN FETCH o.lines l LEFT JOIN FETCH l.product WHERE o.id = :id")
    Optional<Order> findByIdWithClient(@Param("id") Long id);

    /**
     * Count orders for a client.
     */
    long countByClientId(Long clientId);

    /**
     * Find orders for a client with pagination (for limiting results).
     */
    List<Order> findByClientIdOrderByOrderDateDesc(Long clientId, Pageable pageable);

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
     * Find orders by date range, sorted by date descending.
     * Uses JOIN FETCH to eagerly load client for display.
     */
    @Query("SELECT o FROM Order o JOIN FETCH o.client WHERE o.orderDate >= :startDate AND o.orderDate < :endDate ORDER BY o.orderDate DESC")
    List<Order> findByDateRangeOrderByOrderDateDesc(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * Find orders by date range and status, sorted by date descending.
     * Uses JOIN FETCH to eagerly load client for display.
     */
    @Query("SELECT o FROM Order o JOIN FETCH o.client WHERE o.orderDate >= :startDate AND o.orderDate < :endDate AND o.status = :status ORDER BY o.orderDate DESC")
    List<Order> findByDateRangeAndStatusOrderByOrderDateDesc(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("status") OrderStatus status);

    /**
     * Find orders by status, sorted by date descending.
     * Uses JOIN FETCH to eagerly load client for display.
     */
    @Query("SELECT o FROM Order o JOIN FETCH o.client WHERE o.status = :status ORDER BY o.orderDate DESC")
    List<Order> findByStatusOrderByOrderDateDesc(@Param("status") OrderStatus status);

    /**
     * Find all orders sorted by date descending.
     * Uses JOIN FETCH to eagerly load client for display.
     */
    @Query("SELECT o FROM Order o JOIN FETCH o.client ORDER BY o.orderDate DESC")
    List<Order> findAllByOrderByOrderDateDesc();

    /**
     * Get all order dates to extract years (SQLite compatible).
     */
    @Query("SELECT DISTINCT o.orderDate FROM Order o ORDER BY o.orderDate DESC")
    List<LocalDate> findAllOrderDates();
}
