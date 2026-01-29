package com.honeyai.service;

import com.honeyai.dto.TopProduitDto;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final OrderRepository orderRepository;
    private final AchatService achatService;

    public BigDecimal calculateChiffreAffaires(LocalDate start, LocalDate end) {
        List<Order> paidOrders = orderRepository.findPaidOrdersWithLinesBetween(start, end);
        return paidOrders.stream()
                .flatMap(order -> order.getLines().stream())
                .map(OrderLine::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateTotalDepenses(LocalDate start, LocalDate end) {
        return achatService.calculateTotalDepenses(start, end);
    }

    public BigDecimal calculateBenefice(LocalDate start, LocalDate end) {
        BigDecimal ca = calculateChiffreAffaires(start, end);
        BigDecimal depenses = calculateTotalDepenses(start, end);
        return ca.subtract(depenses).setScale(2, RoundingMode.HALF_UP);
    }

    public List<TopProduitDto> getTopProduits(LocalDate start, LocalDate end, int limit) {
        List<Order> paidOrders = orderRepository.findPaidOrdersWithLinesBetween(start, end);

        Map<Long, TopProduitDto> productMap = new LinkedHashMap<>();

        for (Order order : paidOrders) {
            for (OrderLine line : order.getLines()) {
                Long productId = line.getProduct().getId();
                productMap.merge(productId,
                        TopProduitDto.builder()
                                .produitNom(line.getProduct().getName())
                                .typeMiel(line.getProduct().getType() != null
                                        ? line.getProduct().getType().getDisplayLabel()
                                        : null)
                                .quantiteTotale(Long.valueOf(line.getQuantity()))
                                .chiffreAffaires(line.getTotal())
                                .build(),
                        (existing, incoming) -> {
                            existing.setQuantiteTotale(existing.getQuantiteTotale() + incoming.getQuantiteTotale());
                            existing.setChiffreAffaires(existing.getChiffreAffaires().add(incoming.getChiffreAffaires()));
                            return existing;
                        });
            }
        }

        return productMap.values().stream()
                .sorted(Comparator.comparingLong(TopProduitDto::getQuantiteTotale).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}
