package com.honeyai.controller;

import com.honeyai.model.Product;
import com.honeyai.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/produits")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public String list(Model model) {
        List<Product> products = productService.findAll();
        int currentYear = LocalDate.now().getYear();

        model.addAttribute("products", products);
        model.addAttribute("currentYear", currentYear);
        model.addAttribute("productService", productService);
        model.addAttribute("activeMenu", "produits");

        return "produits/list";
    }

    @PostMapping("/{id}/tarif")
    public String updateTarif(
            @PathVariable Long id,
            @RequestParam Integer annee,
            @RequestParam BigDecimal prix,
            RedirectAttributes redirectAttributes) {

        log.info("Updating tarif for product #{} year {} price {}", id, annee, prix);

        Product product = productService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produit introuvable: " + id));

        productService.updatePrice(id, annee, prix);

        redirectAttributes.addFlashAttribute("success",
                String.format("Prix mis à jour pour %s en %d", product.getName(), annee));

        return "redirect:/produits";
    }
}
