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
@RequestMapping("/products")
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
        model.addAttribute("activeMenu", "products");

        return "products/list";
    }

    @PostMapping("/{id}/price")
    public String updatePrice(
            @PathVariable Long id,
            @RequestParam Integer year,
            @RequestParam BigDecimal price,
            RedirectAttributes redirectAttributes) {

        log.info("Updating price for product #{} year {} price {}", id, year, price);

        Product product = productService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produit introuvable: " + id));

        productService.updatePrice(id, year, price);

        redirectAttributes.addFlashAttribute("success",
                String.format("Prix mis à jour pour %s en %d", product.getName(), year));

        return "redirect:/products";
    }
}
