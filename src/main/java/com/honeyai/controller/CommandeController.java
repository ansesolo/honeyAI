package com.honeyai.controller;

import com.honeyai.dto.CommandeFormDto;
import com.honeyai.dto.LigneCommandeDto;
import com.honeyai.dto.ProductPriceDto;
import com.honeyai.enums.StatutCommande;
import com.honeyai.model.Client;
import com.honeyai.model.Commande;
import com.honeyai.model.LigneCommande;
import com.honeyai.model.Product;
import com.honeyai.service.ClientService;
import com.honeyai.service.CommandeService;
import com.honeyai.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/commandes")
@RequiredArgsConstructor
@Slf4j
public class CommandeController {

    private final CommandeService commandeService;
    private final ClientService clientService;
    private final ProductService productService;

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

    @GetMapping("/nouvelle")
    public String showCreateForm(
            @RequestParam(required = false) Long clientId,
            Model model) {

        CommandeFormDto form = CommandeFormDto.builder()
                .dateCommande(LocalDate.now())
                .clientId(clientId)
                .build();

        // Add one empty ligne for UX (AC10)
        form.addEmptyLigne();

        prepareFormModel(model, form);
        return "commandes/form";
    }

    @PostMapping
    public String createCommande(
            @Valid @ModelAttribute("commandeForm") CommandeFormDto form,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Remove empty lignes (where productId is null) before validation
        if (form.getLignes() != null) {
            form.getLignes().removeIf(ligne -> ligne.getProductId() == null);
        }

        // Check if at least one ligne remains
        if (form.getLignes() == null || form.getLignes().isEmpty()) {
            result.rejectValue("lignes", "Size.commandeForm.lignes",
                    "La commande doit contenir au moins une ligne");
        }

        if (result.hasErrors()) {
            log.warn("Validation errors in order form: {}", result.getAllErrors());
            // Add empty ligne back if all were removed
            if (form.getLignes() == null || form.getLignes().isEmpty()) {
                form.addEmptyLigne();
            }
            prepareFormModel(model, form);
            return "commandes/form";
        }

        try {
            // Build Commande entity from form
            Client client = clientService.findByIdOrThrow(form.getClientId());

            Commande commande = Commande.builder()
                    .client(client)
                    .dateCommande(form.getDateCommande())
                    .notes(form.getNotes())
                    .build();

            // Add lignes
            for (LigneCommandeDto ligneDto : form.getLignes()) {
                Product product = productService.findById(ligneDto.getProductId())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Produit non trouve: " + ligneDto.getProductId()));

                LigneCommande ligne = LigneCommande.builder()
                        .product(product)
                        .quantite(ligneDto.getQuantite())
                        .prixUnitaire(ligneDto.getPrixUnitaire())
                        .build();

                commande.addLigne(ligne);
            }

            Commande saved = commandeService.create(commande);
            log.info("Created commande #{}", saved.getId());

            redirectAttributes.addFlashAttribute("success", "Commande creee avec succes");
            return "redirect:/commandes/" + saved.getId();

        } catch (Exception e) {
            log.error("Error creating commande", e);
            model.addAttribute("error", "Erreur lors de la creation de la commande: " + e.getMessage());
            prepareFormModel(model, form);
            return "commandes/form";
        }
    }

    @GetMapping("/{id}")
    public String showDetail(@PathVariable Long id, Model model) {
        Commande commande = commandeService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Commande non trouvee: " + id));

        model.addAttribute("commande", commande);
        model.addAttribute("total", commandeService.calculateTotal(id));
        model.addAttribute("activeMenu", "commandes");

        return "commandes/detail";
    }

    private void prepareFormModel(Model model, CommandeFormDto form) {
        List<Client> clients = clientService.findAllActive();
        List<ProductPriceDto> products = productService.findAllWithCurrentYearPrices();

        model.addAttribute("commandeForm", form);
        model.addAttribute("clients", clients);
        model.addAttribute("products", products);
        model.addAttribute("activeMenu", "commandes");
    }
}
