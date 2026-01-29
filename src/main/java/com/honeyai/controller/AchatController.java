package com.honeyai.controller;

import com.honeyai.enums.CategorieAchat;
import com.honeyai.model.Achat;
import com.honeyai.service.AchatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/achats")
@RequiredArgsConstructor
@Slf4j
public class AchatController {

    private final AchatService achatService;

    @GetMapping
    public String list(@RequestParam(required = false) Integer year,
                       @RequestParam(required = false) CategorieAchat categorie,
                       Model model) {

        List<Achat> achats;
        BigDecimal total;

        if (year != null && categorie != null) {
            LocalDate start = LocalDate.of(year, 1, 1);
            LocalDate end = LocalDate.of(year, 12, 31);
            achats = achatService.findByPeriod(start, end).stream()
                    .filter(a -> a.getCategorie() == categorie)
                    .toList();
        } else if (year != null) {
            LocalDate start = LocalDate.of(year, 1, 1);
            LocalDate end = LocalDate.of(year, 12, 31);
            achats = achatService.findByPeriod(start, end);
        } else if (categorie != null) {
            achats = achatService.findByCategorie(categorie);
        } else {
            achats = achatService.findAll();
        }

        total = achats.stream()
                .map(Achat::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("achats", achats);
        model.addAttribute("total", total);
        model.addAttribute("categories", CategorieAchat.values());
        model.addAttribute("selectedYear", year);
        model.addAttribute("selectedCategorie", categorie);
        model.addAttribute("availableYears", getAvailableYears());
        model.addAttribute("achat", new Achat());
        model.addAttribute("activeMenu", "achats");

        return "achats/list";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute("achat") Achat achat,
                       BindingResult bindingResult,
                       Model model,
                       RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            log.warn("Erreurs de validation achat: {}", bindingResult.getAllErrors());
            model.addAttribute("achats", achatService.findAll());
            model.addAttribute("total", BigDecimal.ZERO);
            model.addAttribute("categories", CategorieAchat.values());
            model.addAttribute("availableYears", getAvailableYears());
            model.addAttribute("activeMenu", "achats");
            model.addAttribute("formError", true);
            return "achats/list";
        }

        achatService.save(achat);
        log.info("Achat enregistre: {} - {} EUR", achat.getDesignation(), achat.getMontant());
        redirectAttributes.addFlashAttribute("successMessage", "Achat enregistre avec succes");
        return "redirect:/achats";
    }

    private List<Integer> getAvailableYears() {
        int currentYear = LocalDate.now().getYear();
        return List.of(currentYear, currentYear - 1, currentYear - 2);
    }
}
