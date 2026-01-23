package com.honeyai.service;

import com.honeyai.enums.HoneyType;
import com.honeyai.enums.OrderStatus;
import com.honeyai.model.*;
import com.honeyai.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class OrderServiceIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PriceRepository priceRepository;

    private Client client;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        // Create client
        client = Client.builder()
                .name("Test Client")
                .phone("0612345678")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        client = clientRepository.save(client);

        // Create products
        product1 = Product.builder()
                .name("Miel Toutes Fleurs 500g")
                .type(HoneyType.TOUTES_FLEURS)
                .unit("pot 500g")
                .build();
        product1 = productRepository.save(product1);

        product2 = Product.builder()
                .name("Miel Forêt 1kg")
                .type(HoneyType.FORET)
                .unit("pot 1kg")
                .build();
        product2 = productRepository.save(product2);

        // Create current year prices
        int currentYear = LocalDate.now().getYear();

        Price price1 = Price.builder()
                .product(product1)
                .year(currentYear)
                .price(new BigDecimal("8.00"))
                .build();
        priceRepository.save(price1);

        Price price2 = Price.builder()
                .product(product2)
                .year(currentYear)
                .price(new BigDecimal("17.00"))
                .build();
        priceRepository.save(price2);
    }

    @Test
    void integrationTest_createCommandeWithAutoPopulatedPrices_andTransitionThroughStatuses() {
        // AC: create real order with 2 lignes
        OrderLine ligne1 = OrderLine.builder()
                .product(product1)
                .quantity(2)
                .build(); // No price - should be auto-populated

        OrderLine ligne2 = OrderLine.builder()
                .product(product2)
                .quantity(1)
                .build(); // No price - should be auto-populated

        Order order = Order.builder()
                .client(client)
                .build(); // No date - should be auto-populated
        order.addLigne(ligne1);
        order.addLigne(ligne2);

        // Create order
        Order created = orderService.create(order);

        // Verify order was created
        assertThat(created.getId()).isNotNull();
        assertThat(created.getOrderDate()).isEqualTo(LocalDate.now());
        assertThat(created.getStatus()).isEqualTo(OrderStatus.ORDERED);
        assertThat(created.getLines()).hasSize(2);

        // AC: verify prices auto-populated
        assertThat(created.getLines().getFirst().getUnitPrice())
                .isEqualByComparingTo(new BigDecimal("8.00"));
        assertThat(created.getLines().get(1).getUnitPrice())
                .isEqualByComparingTo(new BigDecimal("17.00"));

        // AC: calculate total
        BigDecimal total = orderService.calculateTotal(created.getId());
        // (2 * 8.00) + (1 * 17.00) = 33.00
        assertThat(total).isEqualByComparingTo(new BigDecimal("33.00"));

        // AC: transition through all statuses successfully
        // ORDERED -> RECOVERED
        Order ordered = orderService.updateStatus(created.getId(), OrderStatus.RECOVERED);
        assertThat(ordered.getStatus()).isEqualTo(OrderStatus.RECOVERED);

        // RECOVERED -> PAID
        Order paid = orderService.updateStatus(created.getId(), OrderStatus.PAID);
        assertThat(paid.getStatus()).isEqualTo(OrderStatus.PAID);
    }

    @Test
    void integrationTest_getCurrentYearPrice_shouldReturnCorrectPrice() {
        // When
        BigDecimal price = productService.getCurrentYearPrice(product1.getId());

        // Then
        assertThat(price).isEqualByComparingTo(new BigDecimal("8.00"));
    }

    @Test
    void integrationTest_updatePrice_shouldPersistNewPrice() {
        // Given
        int nextYear = LocalDate.now().getYear() + 1;

        // When
        Price updated = productService.updatePrice(product1.getId(), nextYear, new BigDecimal("9.50"));

        // Then
        assertThat(updated.getId()).isNotNull();
        assertThat(updated.getYear()).isEqualTo(nextYear);
        assertThat(updated.getPrice()).isEqualByComparingTo(new BigDecimal("9.50"));
    }
}
