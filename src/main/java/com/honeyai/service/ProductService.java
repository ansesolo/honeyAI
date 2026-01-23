package com.honeyai.service;

import com.honeyai.dto.ProductPriceDto;
import com.honeyai.exception.PriceNotFoundException;
import com.honeyai.model.Price;
import com.honeyai.model.Product;
import com.honeyai.repository.PriceRepository;
import com.honeyai.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final PriceRepository priceRepository;

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAllByOrderByNameAsc();
    }

    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public BigDecimal getCurrentYearPrice(Long productId) {
        int currentYear = LocalDate.now().getYear();
        return priceRepository.findByProductIdAndYear(productId, currentYear)
                .map(Price::getPrice)
                .orElseThrow(() -> new PriceNotFoundException(productId, currentYear));
    }

    @Transactional(readOnly = true)
    public BigDecimal getPriceForYear(Long productId, Integer year) {
        return priceRepository.findByProductIdAndYear(productId, year)
                .map(Price::getPrice)
                .orElse(null);
    }

    public Price updatePrice(Long productId, Integer year, BigDecimal price) {
        log.info("Updating price for product #{} year {} to {}", productId, year, price);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        Price priceEntity = priceRepository.findByProductIdAndYear(productId, year)
                .orElse(Price.builder()
                        .product(product)
                        .year(year)
                        .build());

        priceEntity.setPrice(price);
        return priceRepository.save(priceEntity);
    }

    /**
     * Get all products with their current year prices.
     * Products without a price for the current year will have null price.
     */
    @Transactional(readOnly = true)
    public List<ProductPriceDto> findAllWithCurrentYearPrices() {
        int currentYear = LocalDate.now().getYear();
        List<Product> products = productRepository.findAllByOrderByNameAsc();

        return products.stream()
                .map(product -> {
                    BigDecimal price = priceRepository.findByProductIdAndYear(product.getId(), currentYear)
                            .map(Price::getPrice)
                            .orElse(null);
                    return ProductPriceDto.builder()
                            .id(product.getId())
                            .name(product.getName())
                            .unit(product.getUnit())
                            .price(price)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
