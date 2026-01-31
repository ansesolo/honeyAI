package com.honeyai.controller;

import com.honeyai.dto.OrderFormDto;
import com.honeyai.dto.OrderLineDto;
import com.honeyai.dto.ProductPriceDto;
import com.honeyai.enums.OrderStatus;
import com.honeyai.exception.InvalidStatusTransitionException;
import com.honeyai.model.Client;
import com.honeyai.model.Order;
import com.honeyai.model.OrderLine;
import com.honeyai.model.Product;
import com.honeyai.service.ClientService;
import com.honeyai.service.OrderService;
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
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final ClientService clientService;
    private final ProductService productService;

    @GetMapping
    public String list(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) OrderStatus status,
            Model model) {

        int currentYear = LocalDate.now().getYear();

        // Default to current year if not specified
        Integer filterYear = year;

        // Get filtered orders
        List<Order> orders = orderService.findWithFilters(filterYear, status);

        // Get distinct years for filter dropdown (as mutable list)
        List<Integer> years = new java.util.ArrayList<>(orderService.getDistinctYears());
        // Ensure current year is included even if no orders exist for it
        if (!years.contains(currentYear)) {
            years.addFirst(currentYear);
        }

        model.addAttribute("orders", orders);
        model.addAttribute("currentYear", currentYear);
        model.addAttribute("years", years);
        model.addAttribute("status", OrderStatus.values());
        model.addAttribute("selectedYear", filterYear);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("orderService", orderService);
        model.addAttribute("activeMenu", "orders");

        return "orders/list";
    }

    @GetMapping("/new")
    public String showCreateForm(
            @RequestParam(required = false) Long clientId,
            Model model) {

        OrderFormDto form = OrderFormDto.builder()
                                        .orderDate(LocalDate.now())
                                        .clientId(clientId)
                                        .build();

        // Add one empty ligne for UX (AC10)
        form.addEmptyLine();

        prepareFormModel(model, form);
        return "orders/form";
    }

    @PostMapping
    public String createOrder(
            @Valid @ModelAttribute("orderForm") OrderFormDto form,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Remove empty lignes (where productId is null) before validation
        if (form.getLines() != null) {
            form.getLines().removeIf(line -> line.getProductId() == null);
        }

        // Check if at least one ligne remains
        if (form.getLines() == null || form.getLines().isEmpty()) {
            result.rejectValue("lines", "Size.orderForm.lines",
                    "La commande doit contenir au moins une ligne");
        }

        if (result.hasErrors()) {
            log.warn("Validation errors in order form: {}", result.getAllErrors());
            // Add empty ligne back if all were removed
            if (form.getLines() == null || form.getLines().isEmpty()) {
                form.addEmptyLine();
            }
            prepareFormModel(model, form);
            return "orders/form";
        }

        try {
            // Build Order entity from form
            Client client = clientService.findByIdOrThrow(form.getClientId());

            Order order = Order.builder()
                               .client(client)
                               .orderDate(form.getOrderDate())
                               .notes(form.getNotes())
                               .build();

            // Add lignes
            for (OrderLineDto ligneDto : form.getLines()) {
                Product product = productService.findById(ligneDto.getProductId())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Produit non trouve: " + ligneDto.getProductId()));

                OrderLine ligne = OrderLine.builder()
                                           .product(product)
                                           .quantity(ligneDto.getQuantity())
                                           .unitPrice(ligneDto.getUnitPrice())
                                           .build();

                order.addLigne(ligne);
            }

            Order saved = orderService.create(order);
            log.info("Created order #{}", saved.getId());

            redirectAttributes.addFlashAttribute("successMessage", "Commande creee avec succes");
            return "redirect:/orders/" + saved.getId();

        } catch (Exception e) {
            log.error("Error creating order", e);
            model.addAttribute("error", "Erreur lors de la creation de la commande: " + e.getMessage());
            prepareFormModel(model, form);
            return "orders/form";
        }
    }

    @GetMapping("/{id}")
    public String showDetail(@PathVariable Long id, Model model) {
        Order order = orderService.findById(id)
                                  .orElseThrow(() -> new IllegalArgumentException("Commande non trouvee: " + id));

        model.addAttribute("order", order);
        model.addAttribute("total", orderService.calculateTotal(id));
        model.addAttribute("activeMenu", "orders");

        return "orders/detail";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus newStatus,
            RedirectAttributes redirectAttributes) {

        try {
            Order updated = orderService.updateStatus(id, newStatus);
            log.info("Updated order #{} status to {}", id, newStatus);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Statut mis a jour: " + updated.getStatus().getDisplayLabel());
        } catch (InvalidStatusTransitionException e) {
            log.warn("Invalid status transition for order #{}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Transition invalide");
        } catch (Exception e) {
            log.error("Error updating status for order #{}", id, e);
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Erreur lors de la mise a jour du statut: " + e.getMessage());
        }

        return "redirect:/orders/" + id;
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Order order = orderService.findById(id)
                                  .orElseThrow(() -> new IllegalArgumentException("Commande non trouvee: " + id));

        // Convert Order to OrderFormDto for form binding
        OrderFormDto form = OrderFormDto.builder()
                                        .clientId(order.getClient().getId())
                                        .orderDate(order.getOrderDate())
                                        .notes(order.getNotes())
                                        .build();

        // Convert lines
        for (OrderLine line : order.getLines()) {
            OrderLineDto lineDto = OrderLineDto.builder()
                                                .productId(line.getProduct().getId())
                                                .quantity(line.getQuantity())
                                                .unitPrice(line.getUnitPrice())
                                                .build();
            form.getLines().add(lineDto);
        }

        // Add empty ligne if no lignes exist
        if (form.getLines().isEmpty()) {
            form.addEmptyLine();
        }

        model.addAttribute("orderId", id);
        model.addAttribute("isEdit", true);
        prepareFormModel(model, form);
        return "orders/form";
    }

    @PostMapping("/{id}/edit")
    public String updateOrder(
            @PathVariable Long id,
            @Valid @ModelAttribute("orderForm") OrderFormDto form,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Remove empty lines (where productId is null) before validation
        if (form.getLines() != null) {
            form.getLines().removeIf(line -> line.getProductId() == null);
        }

        // Check if at least one line remains
        if (form.getLines() == null || form.getLines().isEmpty()) {
            result.rejectValue("lines", "Size.orderForm.lines",
                    "La commande doit contenir au moins une ligne");
        }

        if (result.hasErrors()) {
            log.warn("Validation errors in edit form: {}", result.getAllErrors());
            if (form.getLines() == null || form.getLines().isEmpty()) {
                form.addEmptyLine();
            }
            model.addAttribute("orderId", id);
            model.addAttribute("isEdit", true);
            prepareFormModel(model, form);
            return "orders/form";
        }

        try {
            Order order = orderService.findById(id)
                                      .orElseThrow(() -> new IllegalArgumentException("Commande non trouvee: " + id));

            // Update basic fields
            Client client = clientService.findByIdOrThrow(form.getClientId());
            order.setClient(client);
            order.setOrderDate(form.getOrderDate());
            order.setNotes(form.getNotes());

            // Clear existing lignes and add new ones
            order.getLines().clear();
            for (OrderLineDto lineDto : form.getLines()) {
                Product product = productService.findById(lineDto.getProductId())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Produit non trouve: " + lineDto.getProductId()));

                OrderLine line = OrderLine.builder()
                                           .product(product)
                                           .quantity(lineDto.getQuantity())
                                           .unitPrice(lineDto.getUnitPrice())
                                           .build();

                order.addLigne(line);
            }

            orderService.save(order);
            log.info("Updated order #{}", id);

            redirectAttributes.addFlashAttribute("successMessage", "Commande modifiee avec succes");
            return "redirect:/orders/" + id;

        } catch (Exception e) {
            log.error("Error updating order #{}", id, e);
            model.addAttribute("error", "Erreur lors de la modification: " + e.getMessage());
            model.addAttribute("orderId", id);
            model.addAttribute("isEdit", true);
            prepareFormModel(model, form);
            return "orders/form";
        }
    }

    private void prepareFormModel(Model model, OrderFormDto form) {
        List<Client> clients = clientService.findAllActive();
        List<ProductPriceDto> products = productService.findAllWithCurrentYearPrices();

        model.addAttribute("orderForm", form);
        model.addAttribute("clients", clients);
        model.addAttribute("products", products);
        model.addAttribute("activeMenu", "orders");
    }
}
