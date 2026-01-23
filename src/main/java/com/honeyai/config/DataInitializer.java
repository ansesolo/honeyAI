package com.honeyai.config;

import com.honeyai.enums.HoneyType;
import com.honeyai.model.Client;
import com.honeyai.model.Price;
import com.honeyai.model.Product;
import com.honeyai.repository.ClientRepository;
import com.honeyai.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@Profile("!test")
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        if (clientRepository.count() == 0) {
            log.info("Initializing seed clients...");
            createSeedClients();
            log.info("Seed clients initialized successfully");
        }
        if (productRepository.count() == 0) {
            log.info("Initializing seed products...");
            createSeedProducts();
            log.info("Seed products initialized successfully");
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

    private void createSeedProducts() {
        // Honey products - 3 types x 2 formats
        createHoneyProduct("Miel Toutes Fleurs 500g", HoneyType.TOUTES_FLEURS, "pot 500g", "8.00");
        createHoneyProduct("Miel Toutes Fleurs 1kg", HoneyType.TOUTES_FLEURS, "pot 1kg", "15.00");
        createHoneyProduct("Miel Forêt 500g", HoneyType.FORET, "pot 500g", "9.00");
        createHoneyProduct("Miel Forêt 1kg", HoneyType.FORET, "pot 1kg", "17.00");
        createHoneyProduct("Miel Châtaignier 500g", HoneyType.CHATAIGNIER, "pot 500g", "10.00");
        createHoneyProduct("Miel Châtaignier 1kg", HoneyType.CHATAIGNIER, "pot 1kg", "19.00");

        // Non-honey products
        createNonHoneyProduct("Cire avec miel", "unite", "5.00");
        createNonHoneyProduct("Reine", "unite", "30.00");

        log.info("Created 8 seed products with 2024 prices");
    }

    private void createHoneyProduct(String name, HoneyType type, String unit, String price2024) {
        Product product = Product.builder()
                .name(name)
                .type(type)
                .unit(unit)
                .build();

        Price price = Price.builder()
                .year(2024)
                .price(new BigDecimal(price2024))
                .build();
        product.addPrice(price);

        productRepository.save(product);
    }

    private void createNonHoneyProduct(String name, String unit, String price2024) {
        Product product = Product.builder()
                .name(name)
                .type(null)
                .unit(unit)
                .build();

        Price price = Price.builder()
                .year(2024)
                .price(new BigDecimal(price2024))
                .build();
        product.addPrice(price);

        productRepository.save(product);
    }
}
