package com.honeyai.controller;

import com.honeyai.model.Client;
import com.honeyai.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}
