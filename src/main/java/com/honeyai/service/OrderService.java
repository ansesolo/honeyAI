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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        return orderRepository.findById(id);
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

    @Transactional(readOnly = true)
    public List<Order> findByYear(Integer year) {
        return orderRepository.findByYearOrderByOrderDateDesc(year);
    }

    @Transactional(readOnly = true)
    public List<Order> findByYearAndStatus(Integer year, OrderStatus status) {
        return orderRepository.findByYearAndStatusOrderByOrderDateDesc(year, status);
    }

    @Transactional(readOnly = true)
    public List<Order> findByStatusSortedByDateDesc(OrderStatus status) {
        return orderRepository.findByStatusOrderByOrderDateDesc(status);
    }

    @Transactional(readOnly = true)
    public List<Integer> getDistinctYears() {
        return orderRepository.findDistinctYears();
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
        Order order = orderRepository.findById(orderId)
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
        Order order = orderRepository.findById(orderId)
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
}
