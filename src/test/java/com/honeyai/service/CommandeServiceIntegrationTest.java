package com.honeyai.service;

import com.honeyai.enums.HoneyType;
import com.honeyai.enums.StatutCommande;
import com.honeyai.model.*;
import com.honeyai.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CommandeServiceIntegrationTest {

    @Autowired
    private CommandeService commandeService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PriceRepository priceRepository;

    private Client client;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        // Create client
        client = Client.builder()
                .name("Test Client")
                .phone("0612345678")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        client = clientRepository.save(client);

        // Create products
        product1 = Product.builder()
                .name("Miel Toutes Fleurs 500g")
                .type(HoneyType.TOUTES_FLEURS)
                .unit("pot 500g")
                .build();
        product1 = productRepository.save(product1);

        product2 = Product.builder()
                .name("Miel Forêt 1kg")
                .type(HoneyType.FORET)
                .unit("pot 1kg")
                .build();
        product2 = productRepository.save(product2);

        // Create current year prices
        int currentYear = LocalDate.now().getYear();

        Price price1 = Price.builder()
                .product(product1)
                .year(currentYear)
                .price(new BigDecimal("8.00"))
                .build();
        priceRepository.save(price1);

        Price price2 = Price.builder()
                .product(product2)
                .year(currentYear)
                .price(new BigDecimal("17.00"))
                .build();
        priceRepository.save(price2);
    }

    @Test
    void integrationTest_createCommandeWithAutoPopulatedPrices_andTransitionThroughStatuses() {
        // AC: create real commande with 2 lignes
        LigneCommande ligne1 = LigneCommande.builder()
                .product(product1)
                .quantite(2)
                .build(); // No price - should be auto-populated

        LigneCommande ligne2 = LigneCommande.builder()
                .product(product2)
                .quantite(1)
                .build(); // No price - should be auto-populated

        Commande commande = Commande.builder()
                .client(client)
                .build(); // No date - should be auto-populated
        commande.addLigne(ligne1);
        commande.addLigne(ligne2);

        // Create commande
        Commande created = commandeService.create(commande);

        // Verify commande was created
        assertThat(created.getId()).isNotNull();
        assertThat(created.getDateCommande()).isEqualTo(LocalDate.now());
        assertThat(created.getStatut()).isEqualTo(StatutCommande.COMMANDEE);
        assertThat(created.getLignes()).hasSize(2);

        // AC: verify prices auto-populated
        assertThat(created.getLignes().get(0).getPrixUnitaire())
                .isEqualByComparingTo(new BigDecimal("8.00"));
        assertThat(created.getLignes().get(1).getPrixUnitaire())
                .isEqualByComparingTo(new BigDecimal("17.00"));

        // AC: calculate total
        BigDecimal total = commandeService.calculateTotal(created.getId());
        // (2 * 8.00) + (1 * 17.00) = 33.00
        assertThat(total).isEqualByComparingTo(new BigDecimal("33.00"));

        // AC: transition through all statuses successfully
        // COMMANDEE -> RECUPEREE
        Commande recuperee = commandeService.updateStatut(created.getId(), StatutCommande.RECUPEREE);
        assertThat(recuperee.getStatut()).isEqualTo(StatutCommande.RECUPEREE);

        // RECUPEREE -> PAYEE
        Commande payee = commandeService.updateStatut(created.getId(), StatutCommande.PAYEE);
        assertThat(payee.getStatut()).isEqualTo(StatutCommande.PAYEE);
    }

    @Test
    void integrationTest_getCurrentYearPrice_shouldReturnCorrectPrice() {
        // When
        BigDecimal price = productService.getCurrentYearPrice(product1.getId());

        // Then
        assertThat(price).isEqualByComparingTo(new BigDecimal("8.00"));
    }

    @Test
    void integrationTest_updatePrice_shouldPersistNewPrice() {
        // Given
        int nextYear = LocalDate.now().getYear() + 1;

        // When
        Price updated = productService.updatePrice(product1.getId(), nextYear, new BigDecimal("9.50"));

        // Then
        assertThat(updated.getId()).isNotNull();
        assertThat(updated.getYear()).isEqualTo(nextYear);
        assertThat(updated.getPrice()).isEqualByComparingTo(new BigDecimal("9.50"));
    }
}
