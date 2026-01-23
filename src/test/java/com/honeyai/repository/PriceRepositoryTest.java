package com.honeyai.repository;

import com.honeyai.enums.HoneyType;
import com.honeyai.model.Price;
import com.honeyai.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@org.springframework.test.context.ActiveProfiles("test")
class PriceRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PriceRepository priceRepository;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        priceRepository.deleteAll();
        productRepository.deleteAll();

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
    }

    @Test
    void save_shouldPersistPriceWithGeneratedId() {
        // Given
        Price price = Price.builder()
                .product(product1)
                .year(2024)
                .price(new BigDecimal("8.00"))
                .build();

        // When
        Price saved = priceRepository.save(price);

        // Then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getYear()).isEqualTo(2024);
        assertThat(saved.getPrice()).isEqualByComparingTo(new BigDecimal("8.00"));
        assertThat(saved.getProduct().getId()).isEqualTo(product1.getId());
    }

    @Test
    void findByProductIdAndYear_shouldReturnPrice_whenExists() {
        // Given
        Price price = Price.builder()
                .product(product1)
                .year(2024)
                .price(new BigDecimal("8.00"))
                .build();
        priceRepository.save(price);

        // When
        Optional<Price> found = priceRepository.findByProductIdAndYear(product1.getId(), 2024);

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getPrice()).isEqualByComparingTo(new BigDecimal("8.00"));
    }

    @Test
    void findByProductIdAndYear_shouldReturnEmpty_whenNotExists() {
        // When
        Optional<Price> found = priceRepository.findByProductIdAndYear(product1.getId(), 2024);

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    void findByYear_shouldReturnAllPricesForYear() {
        // Given - Assign prices for 2024 and 2025
        Price price1_2024 = Price.builder()
                .product(product1)
                .year(2024)
                .price(new BigDecimal("8.00"))
                .build();

        Price price2_2024 = Price.builder()
                .product(product2)
                .year(2024)
                .price(new BigDecimal("17.00"))
                .build();

        Price price1_2025 = Price.builder()
                .product(product1)
                .year(2025)
                .price(new BigDecimal("8.50"))
                .build();

        priceRepository.save(price1_2024);
        priceRepository.save(price2_2024);
        priceRepository.save(price1_2025);

        // When
        List<Price> prices2024 = priceRepository.findByYear(2024);
        List<Price> prices2025 = priceRepository.findByYear(2025);

        // Then
        assertThat(prices2024).hasSize(2);
        assertThat(prices2025).hasSize(1);
    }

    @Test
    void uniqueConstraint_shouldPreventDuplicatePriceForSameProductAndYear() {
        // Given
        Price price1 = Price.builder()
                .product(product1)
                .year(2024)
                .price(new BigDecimal("8.00"))
                .build();
        entityManager.persistAndFlush(price1);
        entityManager.clear();

        // Re-fetch product after clearing persistence context
        Product managedProduct = entityManager.find(Product.class, product1.getId());

        // When/Then - Verify unique constraint violation
        Price duplicatePrice = Price.builder()
                .product(managedProduct)
                .year(2024)
                .price(new BigDecimal("9.00"))
                .build();

        assertThatThrownBy(() -> {
            entityManager.persistAndFlush(duplicatePrice);
        }).isInstanceOf(Exception.class)
          .satisfies(ex -> assertThat(ex.getMessage().toLowerCase())
              .containsAnyOf("unique", "constraint", "duplicate"));
    }

    @Test
    void uniqueConstraint_shouldAllowSamePriceForDifferentProducts() {
        // Given
        Price price1 = Price.builder()
                .product(product1)
                .year(2024)
                .price(new BigDecimal("8.00"))
                .build();
        priceRepository.save(price1);

        // When - Same year, different product
        Price price2 = Price.builder()
                .product(product2)
                .year(2024)
                .price(new BigDecimal("17.00"))
                .build();
        Price saved = priceRepository.save(price2);

        // Then
        assertThat(saved.getId()).isNotNull();
        assertThat(priceRepository.findAll()).hasSize(2);
    }

    @Test
    void uniqueConstraint_shouldAllowSameProductDifferentYears() {
        // Given
        Price price2024 = Price.builder()
                .product(product1)
                .year(2024)
                .price(new BigDecimal("8.00"))
                .build();
        priceRepository.save(price2024);

        // When - Same product, different year
        Price price2025 = Price.builder()
                .product(product1)
                .year(2025)
                .price(new BigDecimal("8.50"))
                .build();
        Price saved = priceRepository.save(price2025);

        // Then
        assertThat(saved.getId()).isNotNull();
        assertThat(priceRepository.findAll()).hasSize(2);
    }

    @Test
    void pricesForMultipleYears_shouldBeRetrievableByProductAndYear() {
        // Given - AC8: Assign prices for years 2024 and 2025
        Price price2024 = Price.builder()
                .product(product1)
                .year(2024)
                .price(new BigDecimal("8.00"))
                .build();

        Price price2025 = Price.builder()
                .product(product1)
                .year(2025)
                .price(new BigDecimal("8.50"))
                .build();

        priceRepository.save(price2024);
        priceRepository.save(price2025);

        // When - Verify retrieval by product and year
        Optional<Price> found2024 = priceRepository.findByProductIdAndYear(product1.getId(), 2024);
        Optional<Price> found2025 = priceRepository.findByProductIdAndYear(product1.getId(), 2025);

        // Then
        assertThat(found2024).isPresent();
        assertThat(found2024.get().getPrice()).isEqualByComparingTo(new BigDecimal("8.00"));

        assertThat(found2025).isPresent();
        assertThat(found2025.get().getPrice()).isEqualByComparingTo(new BigDecimal("8.50"));
    }

    @Test
    void bigDecimal_shouldMaintainPrecision() {
        // Given
        Price price = Price.builder()
                .product(product1)
                .year(2024)
                .price(new BigDecimal("12.50"))
                .build();

        // When
        Price saved = priceRepository.save(price);

        // Then
        assertThat(saved.getPrice()).isEqualByComparingTo(new BigDecimal("12.50"));
    }
}
