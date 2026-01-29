package com.honeyai.controller;

import com.honeyai.dto.DepenseCategorieDto;
import com.honeyai.enums.CategorieAchat;
import com.honeyai.enums.OrderStatus;
import com.honeyai.repository.OrderRepository;
import com.honeyai.service.AchatService;
import com.honeyai.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final DashboardService dashboardService;
    private final AchatService achatService;
    private final OrderRepository orderRepository;

    @GetMapping("/")
    public String home(@RequestParam(required = false) Integer year, Model model) {
        int selectedYear = (year != null) ? year : LocalDate.now().getYear();
        LocalDate start = LocalDate.of(selectedYear, 1, 1);
        LocalDate end = LocalDate.of(selectedYear, 12, 31);

        BigDecimal ca = dashboardService.calculateChiffreAffaires(start, end);
        BigDecimal depenses = dashboardService.calculateTotalDepenses(start, end);
        BigDecimal benefice = dashboardService.calculateBenefice(start, end);
        long commandesPayees = orderRepository.findByStatus(OrderStatus.PAID).stream()
                .filter(o -> !o.getOrderDate().isBefore(start) && !o.getOrderDate().isAfter(end))
                .count();

        model.addAttribute("ca", ca);
        model.addAttribute("depenses", depenses);
        model.addAttribute("benefice", benefice);
        model.addAttribute("commandesPayees", commandesPayees);
        model.addAttribute("topProduits", dashboardService.getTopProduits(start, end, 3));
        model.addAttribute("depensesParCategorie", buildDepensesParCategorie(start, end, depenses));
        model.addAttribute("selectedYear", selectedYear);
        model.addAttribute("availableYears", getAvailableYears());
        model.addAttribute("activeMenu", "dashboard");

        return "home";
    }

    private List<DepenseCategorieDto> buildDepensesParCategorie(LocalDate start, LocalDate end, BigDecimal total) {
        Map<CategorieAchat, BigDecimal> byCategorie = achatService.calculateDepensesByCategorie(start, end);
        return byCategorie.entrySet().stream()
                .map(e -> DepenseCategorieDto.builder()
                        .categorie(e.getKey())
                        .montant(e.getValue())
                        .pourcentage(total.signum() > 0
                                ? e.getValue().multiply(BigDecimal.valueOf(100))
                                        .divide(total, 0, RoundingMode.HALF_UP)
                                        .intValue()
                                : 0)
                        .build())
                .sorted(Comparator.comparing(DepenseCategorieDto::getMontant).reversed())
                .toList();
    }

    private List<Integer> getAvailableYears() {
        int currentYear = LocalDate.now().getYear();
        return List.of(currentYear, currentYear - 1, currentYear - 2);
    }
}
