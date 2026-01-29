package com.honeyai.controller;

import com.honeyai.enums.OrderStatus;
import com.honeyai.repository.OrderRepository;
import com.honeyai.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final DashboardService dashboardService;
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
        model.addAttribute("selectedYear", selectedYear);
        model.addAttribute("availableYears", getAvailableYears());
        model.addAttribute("activeMenu", "dashboard");

        return "home";
    }

    private List<Integer> getAvailableYears() {
        int currentYear = LocalDate.now().getYear();
        return List.of(currentYear, currentYear - 1, currentYear - 2);
    }
}
