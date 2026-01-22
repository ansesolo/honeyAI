package com.honeyai.controller;

import com.honeyai.model.Client;
import com.honeyai.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

import java.util.List;

@Controller
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public String list(@RequestParam(required = false) String search, Model model) {
        List<Client> clients;
        if (search != null && !search.trim().isEmpty()) {
            clients = clientService.searchClients(search);
            model.addAttribute("search", search);
        } else {
            clients = clientService.findAllActive();
        }
        model.addAttribute("clients", clients);
        model.addAttribute("activeMenu", "clients");
        return "clients/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Client client = clientService.findByIdOrThrow(id);
        model.addAttribute("client", client);
        model.addAttribute("activeMenu", "clients");
        return "clients/detail";
    }

    @GetMapping("/nouveau")
    public String createForm(Model model) {
        model.addAttribute("client", new Client());
        model.addAttribute("activeMenu", "clients");
        return "clients/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Client client = clientService.findByIdOrThrow(id);
        model.addAttribute("client", client);
        model.addAttribute("activeMenu", "clients");
        return "clients/form";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute Client client, BindingResult result,
                       Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("activeMenu", "clients");
            return "clients/form";
        }
        clientService.save(client);
        redirectAttributes.addFlashAttribute("successMessage",
                "Client \"" + client.getName() + "\" enregistre avec succes");
        return "redirect:/clients";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Client client = clientService.findByIdOrThrow(id);
        clientService.softDelete(id);
        redirectAttributes.addFlashAttribute("successMessage",
                "Client \"" + client.getName() + "\" supprime avec succes");
        return "redirect:/clients";
    }
}
