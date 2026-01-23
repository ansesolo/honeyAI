package com.honeyai.controller;

import com.honeyai.enums.StatutCommande;
import com.honeyai.model.Commande;
import com.honeyai.service.CommandeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/commandes")
@RequiredArgsConstructor
@Slf4j
public class CommandeController {

    private final CommandeService commandeService;

    @GetMapping
    public String list(
            @RequestParam(required = false) Integer annee,
            @RequestParam(required = false) StatutCommande statut,
            Model model) {

        int currentYear = LocalDate.now().getYear();

        // Default to current year if not specified
        Integer filterYear = annee;

        // Get filtered commandes
        List<Commande> commandes = commandeService.findWithFilters(filterYear, statut);

        // Get distinct years for filter dropdown (as mutable list)
        List<Integer> years = new java.util.ArrayList<>(commandeService.getDistinctYears());
        // Ensure current year is included even if no orders exist for it
        if (!years.contains(currentYear)) {
            years.add(0, currentYear);
        }

        model.addAttribute("commandes", commandes);
        model.addAttribute("currentYear", currentYear);
        model.addAttribute("years", years);
        model.addAttribute("statuts", StatutCommande.values());
        model.addAttribute("selectedYear", filterYear);
        model.addAttribute("selectedStatut", statut);
        model.addAttribute("commandeService", commandeService);
        model.addAttribute("activeMenu", "commandes");

        return "commandes/list";
    }
}
