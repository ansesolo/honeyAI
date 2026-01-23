package com.honeyai.service;

import com.honeyai.enums.HoneyType;
import com.honeyai.exception.PriceNotFoundException;
import com.honeyai.model.Price;
import com.honeyai.model.Product;
import com.honeyai.repository.PriceRepository;
import com.honeyai.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PriceRepository priceRepository;

    @InjectMocks
    private ProductService productService;

    private Product product1;
    private Product product2;
    private Price price1;

    @BeforeEach
    void setUp() {
        product1 = Product.builder()
                .id(1L)
                .name("Miel Toutes Fleurs 500g")
                .type(HoneyType.TOUTES_FLEURS)
                .unit("pot 500g")
                .build();

        product2 = Product.builder()
                .id(2L)
                .name("Miel Forêt 1kg")
                .type(HoneyType.FORET)
                .unit("pot 1kg")
                .build();

        price1 = Price.builder()
                .id(1L)
                .product(product1)
                .year(LocalDate.now().getYear())
                .price(new BigDecimal("8.00"))
                .build();
    }

    @Test
    void findAll_shouldReturnProductsOrderedByName() {
        // Given
        when(productRepository.findAllByOrderByNameAsc()).thenReturn(Arrays.asList(product1, product2));

        // When
        List<Product> result = productService.findAll();

        // Then
        assertThat(result).hasSize(2);
        verify(productRepository).findAllByOrderByNameAsc();
    }

    @Test
    void findById_shouldReturnProduct_whenExists() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

        // When
        Optional<Product> result = productService.findById(1L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Miel Toutes Fleurs 500g");
    }

    @Test
    void getCurrentYearPrice_shouldReturnPrice_whenExists() {
        // Given
        int currentYear = LocalDate.now().getYear();
        when(priceRepository.findByProductIdAndYear(1L, currentYear))
                .thenReturn(Optional.of(price1));

        // When
        BigDecimal result = productService.getCurrentYearPrice(1L);

        // Then
        assertThat(result).isEqualByComparingTo(new BigDecimal("8.00"));
    }

    @Test
    void getCurrentYearPrice_shouldThrowException_whenNotFound() {
        // Given
        int currentYear = LocalDate.now().getYear();
        when(priceRepository.findByProductIdAndYear(1L, currentYear))
                .thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> productService.getCurrentYearPrice(1L))
                .isInstanceOf(PriceNotFoundException.class)
                .hasMessageContaining("product #1")
                .hasMessageContaining(String.valueOf(currentYear));
    }

    @Test
    void updatePrice_shouldCreateNewPrice_whenNotExists() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(priceRepository.findByProductIdAndYear(1L, 2025)).thenReturn(Optional.empty());
        when(priceRepository.save(any(Price.class))).thenAnswer(invocation -> {
            Price p = invocation.getArgument(0);
            p.setId(10L);
            return p;
        });

        // When
        Price result = productService.updatePrice(1L, 2025, new BigDecimal("9.00"));

        // Then
        assertThat(result.getPrice()).isEqualByComparingTo(new BigDecimal("9.00"));
        assertThat(result.getYear()).isEqualTo(2025);
        verify(priceRepository).save(any(Price.class));
    }

    @Test
    void updatePrice_shouldUpdateExistingPrice() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(priceRepository.findByProductIdAndYear(1L, LocalDate.now().getYear()))
                .thenReturn(Optional.of(price1));
        when(priceRepository.save(any(Price.class))).thenReturn(price1);

        // When
        Price result = productService.updatePrice(1L, LocalDate.now().getYear(), new BigDecimal("10.00"));

        // Then
        assertThat(result.getPrice()).isEqualByComparingTo(new BigDecimal("10.00"));
        verify(priceRepository).save(price1);
    }
}
