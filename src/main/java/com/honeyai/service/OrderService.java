package com.honeyai.service;

import com.honeyai.enums.OrderStatus;
import com.honeyai.exception.InvalidStatusTransitionException;
import com.honeyai.model.Order;
import com.honeyai.model.OrderLine;
import com.honeyai.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.honeyai.dto.ClientOrderStatsDto;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;

    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Order> findById(Long id) {
        return orderRepository.findByIdWithClient(id);
    }

    @Transactional(readOnly = true)
    public List<Order> findByClientId(Long clientId) {
        return orderRepository.findByClientIdOrderByOrderDateDesc(clientId);
    }

    @Transactional(readOnly = true)
    public List<Order> findByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Order> findAllSortedByDateDesc() {
        return orderRepository.findAllByOrderByOrderDateDesc();
    }

    /**
     * Find orders for a specific year, sorted by date descending.
     * Converts year to date range (Jan 1 to Jan 1 of next year).
     */
    @Transactional(readOnly = true)
    public List<Order> findByYear(Integer year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year + 1, 1, 1);
        return orderRepository.findByDateRangeOrderByOrderDateDesc(startDate, endDate);
    }

    /**
     * Find orders for a specific year and status, sorted by date descending.
     * Converts year to date range (Jan 1 to Jan 1 of next year).
     */
    @Transactional(readOnly = true)
    public List<Order> findByYearAndStatus(Integer year, OrderStatus status) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year + 1, 1, 1);
        return orderRepository.findByDateRangeAndStatusOrderByOrderDateDesc(startDate, endDate, status);
    }

    @Transactional(readOnly = true)
    public List<Order> findByStatusSortedByDateDesc(OrderStatus status) {
        return orderRepository.findByStatusOrderByOrderDateDesc(status);
    }

    /**
     * Get distinct years from all order dates.
     * Extracts years from findAllOrderDates and returns sorted descending.
     */
    @Transactional(readOnly = true)
    public List<Integer> getDistinctYears() {
        return orderRepository.findAllOrderDates().stream()
                .map(LocalDate::getYear)
                .distinct()
                .sorted((a, b) -> b.compareTo(a))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Order> findWithFilters(Integer year, OrderStatus status) {
        if (year != null && status != null) {
            return findByYearAndStatus(year, status);
        } else if (year != null) {
            return findByYear(year);
        } else if (status != null) {
            return findByStatusSortedByDateDesc(status);
        } else {
            return findAllSortedByDateDesc();
        }
    }

    public Order create(Order order) {
        log.info("Creating new order for client #{}",
                order.getClient() != null ? order.getClient().getId() : "null");

        // Auto-populate orderDate with today if null
        if (order.getOrderDate() == null) {
            order.setOrderDate(LocalDate.now());
        }

        // Set default status if null
        if (order.getStatus() == null) {
            order.setStatus(OrderStatus.ORDERED);
        }

        // Validate at least one ligne exists
        if (order.getLines() == null || order.getLines().isEmpty()) {
            throw new IllegalArgumentException("La commande doit contenir au moins une ligne");
        }

        // Auto-fetch unitPrice from current year price if not provided
        for (OrderLine ligne : order.getLines()) {
            if (ligne.getUnitPrice() == null && ligne.getProduct() != null) {
                BigDecimal price = productService.getCurrentYearPrice(ligne.getProduct().getId());
                ligne.setUnitPrice(price);
                log.debug("Auto-populated price {} for product #{}", price, ligne.getProduct().getId());
            }
        }

        Order saved = orderRepository.save(order);
        log.info("Created order #{} with {} lignes", saved.getId(), saved.getLines().size());
        return saved;
    }

    public Order updateStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findByIdWithClient(orderId)
                                     .orElseThrow(() -> new IllegalArgumentException("order not found: " + orderId));

        OrderStatus oldStatus = order.getStatus();

        if (!order.canTransitionTo(newStatus)) {
            log.warn("Invalid status transition attempted: {} -> {} for order #{}",
                    oldStatus, newStatus, orderId);
            throw new InvalidStatusTransitionException(oldStatus, newStatus);
        }

        order.setStatus(newStatus);
        Order saved = orderRepository.save(order);

        log.info("Status transition for order #{}: {} -> {} at {}",
                orderId, oldStatus, newStatus, LocalDateTime.now());

        return saved;
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateTotal(Long orderId) {
        Order order = orderRepository.findByIdWithClient(orderId)
                                     .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        return order.getLines().stream()
                    .map(OrderLine::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Save an existing order (for updates).
     */
    public Order save(Order order) {
        log.info("Saving order #{}", order.getId());
        return orderRepository.save(order);
    }

    /**
     * Find orders for a client with a limit on results.
     */
    @Transactional(readOnly = true)
    public List<Order> findByClientIdWithLimit(Long clientId, int limit) {
        return orderRepository.findByClientIdOrderByOrderDateDesc(clientId, PageRequest.of(0, limit));
    }

    /**
     * Count total orders for a client.
     */
    @Transactional(readOnly = true)
    public long countByClientId(Long clientId) {
        return orderRepository.countByClientId(clientId);
    }

    /**
     * Get order statistics for a client (for detail page).
     */
    @Transactional(readOnly = true)
    public ClientOrderStatsDto getClientOrderStats(Long clientId) {
        List<Order> allOrders = orderRepository.findByClientIdOrderByOrderDateDesc(clientId);

        long totalOrders = allOrders.size();
        LocalDate lastOrderDate = allOrders.isEmpty() ? null : allOrders.get(0).getOrderDate();

        BigDecimal totalPaid = allOrders.stream()
                .filter(order -> order.getStatus() == OrderStatus.PAID)
                .map(order -> order.getLines().stream()
                        .map(line -> line.getUnitPrice().multiply(BigDecimal.valueOf(line.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        return ClientOrderStatsDto.builder()
                .totalOrders(totalOrders)
                .totalPaidAmount(totalPaid)
                .lastOrderDate(lastOrderDate)
                .build();
    }
}
