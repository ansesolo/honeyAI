package com.honeyai.config;

import com.honeyai.model.Client;
import com.honeyai.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Profile("!test")
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final ClientRepository clientRepository;

    @Override
    public void run(String... args) {
        if (clientRepository.count() == 0) {
            log.info("Initializing seed data...");
            createSeedClients();
            log.info("Seed data initialized successfully");
        }
    }

    private void createSeedClients() {
        Client client1 = Client.builder()
                .name("Dupont Jean")
                .phone("06 12 34 56 78")
                .email("jean.dupont@email.fr")
                .address("12 Rue des Abeilles, 31000 Toulouse")
                .notes("Client regulier depuis 2020")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        clientRepository.save(client1);

        Client client2 = Client.builder()
                .name("Martin Marie")
                .phone("06 98 76 54 32")
                .email("marie.martin@email.fr")
                .address("45 Avenue du Miel, 31100 Toulouse")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        clientRepository.save(client2);

        Client client3 = Client.builder()
                .name("Bernard Pierre")
                .phone("07 11 22 33 44")
                .email("p.bernard@email.fr")
                .address("8 Chemin des Ruches, 31200 Toulouse")
                .notes("Prefere le miel de foret")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        clientRepository.save(client3);

        log.info("Created 3 seed clients");
    }
}
