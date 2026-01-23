package com.honeyai.repository;

import com.honeyai.enums.HoneyType;
import com.honeyai.model.Price;
import com.honeyai.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@org.springframework.test.context.ActiveProfiles("test")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PriceRepository priceRepository;

    @BeforeEach
    void setUp() {
        priceRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    void save_shouldPersistProductWithGeneratedId() {
        // Given
        Product product = Product.builder()
                .name("Miel Toutes Fleurs 500g")
                .type(HoneyType.TOUTES_FLEURS)
                .unit("pot 500g")
                .build();

        // When
        Product saved = productRepository.save(product);

        // Then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Miel Toutes Fleurs 500g");
        assertThat(saved.getType()).isEqualTo(HoneyType.TOUTES_FLEURS);
        assertThat(saved.getUnit()).isEqualTo("pot 500g");
    }

    @Test
    void save_shouldAllowNullTypeForNonHoneyProducts() {
        // Given
        Product product = Product.builder()
                .name("Cire avec miel")
                .type(null)
                .unit("unite")
                .build();

        // When
        Product saved = productRepository.save(product);

        // Then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getType()).isNull();
    }

    @Test
    void findAllByOrderByNameAsc_shouldReturnProductsOrderedByName() {
        // Given
        Product mielForet = Product.builder()
                .name("Miel Forêt 1kg")
                .type(HoneyType.FORET)
                .unit("pot 1kg")
                .build();

        Product cire = Product.builder()
                .name("Cire avec miel")
                .type(null)
                .unit("unite")
                .build();

        Product reine = Product.builder()
                .name("Reine")
                .type(null)
                .unit("unite")
                .build();

        Product mielToutesFleurs = Product.builder()
                .name("Miel Toutes Fleurs 500g")
                .type(HoneyType.TOUTES_FLEURS)
                .unit("pot 500g")
                .build();

        productRepository.save(mielForet);
        productRepository.save(cire);
        productRepository.save(reine);
        productRepository.save(mielToutesFleurs);

        // When
        List<Product> products = productRepository.findAllByOrderByNameAsc();

        // Then
        assertThat(products).hasSize(4);
        assertThat(products.get(0).getName()).isEqualTo("Cire avec miel");
        assertThat(products.get(1).getName()).isEqualTo("Miel Forêt 1kg");
        assertThat(products.get(2).getName()).isEqualTo("Miel Toutes Fleurs 500g");
        assertThat(products.get(3).getName()).isEqualTo("Reine");
    }

    @Test
    void save_shouldCascadePrices() {
        // Given
        Product product = Product.builder()
                .name("Miel Toutes Fleurs 500g")
                .type(HoneyType.TOUTES_FLEURS)
                .unit("pot 500g")
                .build();

        Price price2024 = Price.builder()
                .year(2024)
                .price(new BigDecimal("8.00"))
                .build();
        product.addPrice(price2024);

        Price price2025 = Price.builder()
                .year(2025)
                .price(new BigDecimal("8.50"))
                .build();
        product.addPrice(price2025);

        // When
        Product saved = productRepository.save(product);

        // Then
        assertThat(saved.getPrices()).hasSize(2);

        Optional<Product> found = productRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getPrices()).hasSize(2);
    }

    @Test
    void findById_shouldReturnProduct_whenExists() {
        // Given
        Product product = Product.builder()
                .name("Reine")
                .type(null)
                .unit("unite")
                .build();
        Product saved = productRepository.save(product);

        // When
        Optional<Product> found = productRepository.findById(saved.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Reine");
    }

    @Test
    void createMultipleProducts_shouldSucceed() {
        // Given - AC8: Create products (Miel 500g Toutes Fleurs, Miel 1kg Foret, Cire avec miel, Reine)
        Product miel500gToutesFleurs = Product.builder()
                .name("Miel Toutes Fleurs 500g")
                .type(HoneyType.TOUTES_FLEURS)
                .unit("pot 500g")
                .build();

        Product miel1kgForet = Product.builder()
                .name("Miel Forêt 1kg")
                .type(HoneyType.FORET)
                .unit("pot 1kg")
                .build();

        Product cire = Product.builder()
                .name("Cire avec miel")
                .type(null)
                .unit("unite")
                .build();

        Product reine = Product.builder()
                .name("Reine")
                .type(null)
                .unit("unite")
                .build();

        // When
        productRepository.save(miel500gToutesFleurs);
        productRepository.save(miel1kgForet);
        productRepository.save(cire);
        productRepository.save(reine);

        // Then
        List<Product> all = productRepository.findAll();
        assertThat(all).hasSize(4);
    }
}
