package com.honeyai.service;

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
}
