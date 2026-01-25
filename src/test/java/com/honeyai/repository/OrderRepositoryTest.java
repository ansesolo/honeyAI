package com.honeyai.repository;

import com.honeyai.enums.HoneyType;
import com.honeyai.enums.OrderStatus;
import com.honeyai.model.Client;
import com.honeyai.model.Order;
import com.honeyai.model.OrderLine;
import com.honeyai.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class OrderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    private Client client;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        // Create test client
        client = Client.builder()
                .name("Test Client")
                .phone("0612345678")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        client = entityManager.persistAndFlush(client);

        // Create test products
        product1 = Product.builder()
                .name("Miel Toutes Fleurs 500g")
                .type(HoneyType.TOUTES_FLEURS)
                .unit("pot 500g")
                .build();
        product1 = entityManager.persistAndFlush(product1);

        product2 = Product.builder()
                .name("Miel Forêt 1kg")
                .type(HoneyType.FORET)
                .unit("pot 1kg")
                .build();
        product2 = entityManager.persistAndFlush(product2);

        entityManager.clear();
    }

    @Test
    void save_shouldPersistCommandeWithGeneratedId() {
        // Given
        Order order = Order.builder()
                           .client(client)
                           .orderDate(LocalDate.now())
                           .status(OrderStatus.ORDERED)
                           .build();

        // When
        Order saved = orderRepository.save(order);

        // Then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getClient().getId()).isEqualTo(client.getId());
        assertThat(saved.getStatus()).isEqualTo(OrderStatus.ORDERED);
    }

    @Test
    void save_shouldCascadeSaveLignes() {
        // Given - AC: create command with 2-3 lines, verify cascade save
        Order order = Order.builder()
                           .client(client)
                           .orderDate(LocalDate.now())
                           .build();

        OrderLine ligne1 = OrderLine.builder()
                                    .product(product1)
                                    .quantity(2)
                                    .unitPrice(new BigDecimal("8.00"))
                                    .build();
        order.addLigne(ligne1);

        OrderLine ligne2 = OrderLine.builder()
                                    .product(product2)
                                    .quantity(1)
                                    .unitPrice(new BigDecimal("17.00"))
                                    .build();
        order.addLigne(ligne2);

        // When
        Order saved = orderRepository.saveAndFlush(order);
        entityManager.clear();

        // Then - lines auto-saved with command
        Optional<Order> found = orderRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getLines()).hasSize(2);
    }

    @Test
    void removeLine_shouldTriggerOrphanRemoval() {
        // Given - AC: verify orphan removal (delete line removes it)
        Order order = Order.builder()
                           .client(client)
                           .orderDate(LocalDate.now())
                           .build();

        OrderLine ligne1 = OrderLine.builder()
                                    .product(product1)
                                    .quantity(2)
                                    .unitPrice(new BigDecimal("8.00"))
                                    .build();
        order.addLigne(ligne1);

        OrderLine ligne2 = OrderLine.builder()
                                    .product(product2)
                                    .quantity(1)
                                    .unitPrice(new BigDecimal("17.00"))
                                    .build();
        order.addLigne(ligne2);

        Order saved = orderRepository.saveAndFlush(order);
        entityManager.clear();

        // When - remove one line
        Order toUpdate = orderRepository.findById(saved.getId()).orElseThrow();
        toUpdate.getLines().removeFirst();
        orderRepository.saveAndFlush(toUpdate);
        entityManager.clear();

        // Then - orphan should be removed
        Order updated = orderRepository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getLines()).hasSize(1);
    }

    @Test
    void save_shouldPersistStatusAsString() {
        // Given - AC: verify status enum persists as string
        Order order = Order.builder()
                           .client(client)
                           .orderDate(LocalDate.now())
                           .status(OrderStatus.RECOVERED)
                           .build();

        // When
        Order saved = orderRepository.saveAndFlush(order);
        entityManager.clear();

        // Then
        Optional<Order> found = orderRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getStatus()).isEqualTo(OrderStatus.RECOVERED);
    }

    @Test
    void findByClientIdOrderByOrderDateDesc_shouldReturnOrderedOrders() {
        // Given
        Order order1 = Order.builder()
                            .client(client)
                            .orderDate(LocalDate.of(2024, 1, 1))
                            .build();
        orderRepository.save(order1);

        Order order2 = Order.builder()
                            .client(client)
                            .orderDate(LocalDate.of(2024, 6, 15))
                            .build();
        orderRepository.save(order2);

        Order order3 = Order.builder()
                            .client(client)
                            .orderDate(LocalDate.of(2024, 3, 10))
                            .build();
        orderRepository.save(order3);

        // When
        List<Order> orders = orderRepository.findByClientIdOrderByOrderDateDesc(client.getId());

        // Then - most recent first
        assertThat(orders).hasSize(3);
        assertThat(orders.get(0).getOrderDate()).isEqualTo(LocalDate.of(2024, 6, 15));
        assertThat(orders.get(1).getOrderDate()).isEqualTo(LocalDate.of(2024, 3, 10));
        assertThat(orders.get(2).getOrderDate()).isEqualTo(LocalDate.of(2024, 1, 1));
    }

    @Test
    void findByStatus_shouldReturnOrdersWithStatus() {
        // Given
        Order commandee = Order.builder()
                               .client(client)
                               .orderDate(LocalDate.now())
                               .status(OrderStatus.ORDERED)
                               .build();
        orderRepository.save(commandee);

        Order recovered = Order.builder()
                               .client(client)
                               .orderDate(LocalDate.now())
                               .status(OrderStatus.RECOVERED)
                               .build();
        orderRepository.save(recovered);

        Order payee = Order.builder()
                           .client(client)
                           .orderDate(LocalDate.now())
                           .status(OrderStatus.PAID)
                           .build();
        orderRepository.save(payee);

        // When
        List<Order> ordered = orderRepository.findByStatus(OrderStatus.ORDERED);
        List<Order> recovered2 = orderRepository.findByStatus(OrderStatus.RECOVERED);
        List<Order> paid = orderRepository.findByStatus(OrderStatus.PAID);

        // Then
        assertThat(ordered).hasSize(1);
        assertThat(recovered2).hasSize(1);
        assertThat(paid).hasSize(1);
    }

    @Test
    void findByOrderDateBetween_shouldReturnCommandesInRange() {
        // Given
        Order jan = Order.builder()
                         .client(client)
                         .orderDate(LocalDate.of(2024, 1, 15))
                         .build();
        orderRepository.save(jan);

        Order mar = Order.builder()
                         .client(client)
                         .orderDate(LocalDate.of(2024, 3, 15))
                         .build();
        orderRepository.save(mar);

        Order dec = Order.builder()
                         .client(client)
                         .orderDate(LocalDate.of(2024, 12, 15))
                         .build();
        orderRepository.save(dec);

        // When
        List<Order> q1 = orderRepository.findByOrderDateBetween(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31)
        );

        // Then
        assertThat(q1).hasSize(2);
    }

    @Test
    void save_shouldDefaultToOrderStatus() {
        // Given
        Order order = Order.builder()
                           .client(client)
                           .orderDate(LocalDate.now())
                           .build();

        // When
        Order saved = orderRepository.save(order);

        // Then
        assertThat(saved.getStatus()).isEqualTo(OrderStatus.ORDERED);
    }

    @Test
    void orderLines_getTotal_shouldCalculateCorrectly() {
        // Given
        OrderLine ligne = OrderLine.builder()
                                   .quantity(3)
                                   .unitPrice(new BigDecimal("8.50"))
                                   .build();

        // When/Then
        assertThat(ligne.getTotal()).isEqualByComparingTo(new BigDecimal("25.50"));
    }

    @Test
    void findByDateRangeOrderByOrderDateDesc_shouldReturnOrdersForDateRange() {
        // Given
        Order order2025 = Order.builder()
                               .client(client)
                               .orderDate(LocalDate.of(2025, 6, 15))
                               .build();
        orderRepository.save(order2025);

        Order order2026A = Order.builder()
                                .client(client)
                                .orderDate(LocalDate.of(2026, 1, 10))
                                .build();
        orderRepository.save(order2026A);

        Order order2026B = Order.builder()
                                .client(client)
                                .orderDate(LocalDate.of(2026, 3, 20))
                                .build();
        orderRepository.save(order2026B);

        // When - Filter for year 2026 using date range
        LocalDate startDate = LocalDate.of(2026, 1, 1);
        LocalDate endDate = LocalDate.of(2027, 1, 1);
        List<Order> orders2026 = orderRepository.findByDateRangeOrderByOrderDateDesc(startDate, endDate);

        // Then
        assertThat(orders2026).hasSize(2);
        assertThat(orders2026.getFirst().getOrderDate()).isEqualTo(LocalDate.of(2026, 3, 20));
        assertThat(orders2026.get(1).getOrderDate()).isEqualTo(LocalDate.of(2026, 1, 10));
    }

    @Test
    void findByDateRangeAndStatusOrderByOrderDateDesc_shouldFilterByDateRangeAndStatus() {
        // Given
        Order order2026Commandee = Order.builder()
                                        .client(client)
                                        .orderDate(LocalDate.of(2026, 1, 15))
                                        .status(OrderStatus.ORDERED)
                                        .build();
        orderRepository.save(order2026Commandee);

        Order order2026Payee = Order.builder()
                                    .client(client)
                                    .orderDate(LocalDate.of(2026, 2, 20))
                                    .status(OrderStatus.PAID)
                                    .build();
        orderRepository.save(order2026Payee);

        Order order2025Commandee = Order.builder()
                                        .client(client)
                                        .orderDate(LocalDate.of(2025, 6, 10))
                                        .status(OrderStatus.ORDERED)
                                        .build();
        orderRepository.save(order2025Commandee);

        // When - Filter for year 2026 and ORDERED status using date range
        LocalDate startDate = LocalDate.of(2026, 1, 1);
        LocalDate endDate = LocalDate.of(2027, 1, 1);
        List<Order> result = orderRepository.findByDateRangeAndStatusOrderByOrderDateDesc(startDate, endDate, OrderStatus.ORDERED);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getOrderDate().getYear()).isEqualTo(2026);
        assertThat(result.getFirst().getStatus()).isEqualTo(OrderStatus.ORDERED);
    }

    @Test
    void findAllOrderDates_shouldReturnAllDatesDescending() {
        // Given
        Order c2024 = Order.builder()
                           .client(client)
                           .orderDate(LocalDate.of(2024, 5, 1))
                           .build();
        orderRepository.save(c2024);

        Order c2025a = Order.builder()
                            .client(client)
                            .orderDate(LocalDate.of(2025, 1, 15))
                            .build();
        orderRepository.save(c2025a);

        Order c2025b = Order.builder()
                            .client(client)
                            .orderDate(LocalDate.of(2025, 6, 20))
                            .build();
        orderRepository.save(c2025b);

        Order c2026 = Order.builder()
                           .client(client)
                           .orderDate(LocalDate.of(2026, 1, 5))
                           .build();
        orderRepository.save(c2026);

        // When
        List<LocalDate> dates = orderRepository.findAllOrderDates();

        // Then - Should have all distinct dates, sorted descending
        assertThat(dates).hasSize(4);
        assertThat(dates.getFirst()).isEqualTo(LocalDate.of(2026, 1, 5));

        // Extract distinct years for verification
        List<Integer> years = dates.stream()
                .map(LocalDate::getYear)
                .distinct()
                .sorted((a, b) -> b.compareTo(a))
                .toList();
        assertThat(years).containsExactly(2026, 2025, 2024);
    }

    @Test
    void findAllByOrderByOrderDateDesc_shouldReturnAllOrdersSortedOrder() {
        // Given
        Order c1 = Order.builder()
                        .client(client)
                        .orderDate(LocalDate.of(2025, 1, 1))
                        .build();
        orderRepository.save(c1);

        Order c2 = Order.builder()
                        .client(client)
                        .orderDate(LocalDate.of(2026, 6, 15))
                        .build();
        orderRepository.save(c2);

        Order c3 = Order.builder()
                        .client(client)
                        .orderDate(LocalDate.of(2025, 12, 31))
                        .build();
        orderRepository.save(c3);

        // When
        List<Order> all = orderRepository.findAllByOrderByOrderDateDesc();

        // Then
        assertThat(all).hasSize(3);
        assertThat(all.get(0).getOrderDate()).isEqualTo(LocalDate.of(2026, 6, 15));
        assertThat(all.get(1).getOrderDate()).isEqualTo(LocalDate.of(2025, 12, 31));
        assertThat(all.get(2).getOrderDate()).isEqualTo(LocalDate.of(2025, 1, 1));
    }
}
