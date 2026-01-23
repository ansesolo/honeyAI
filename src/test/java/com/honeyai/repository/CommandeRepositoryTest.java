package com.honeyai.repository;

import com.honeyai.enums.HoneyType;
import com.honeyai.enums.StatutCommande;
import com.honeyai.model.Client;
import com.honeyai.model.Commande;
import com.honeyai.model.LigneCommande;
import com.honeyai.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class CommandeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    private Client client;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        // Create test client
        client = Client.builder()
                .name("Test Client")
                .phone("0612345678")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        client = entityManager.persistAndFlush(client);

        // Create test products
        product1 = Product.builder()
                .name("Miel Toutes Fleurs 500g")
                .type(HoneyType.TOUTES_FLEURS)
                .unit("pot 500g")
                .build();
        product1 = entityManager.persistAndFlush(product1);

        product2 = Product.builder()
                .name("Miel Forêt 1kg")
                .type(HoneyType.FORET)
                .unit("pot 1kg")
                .build();
        product2 = entityManager.persistAndFlush(product2);

        entityManager.clear();
    }

    @Test
    void save_shouldPersistCommandeWithGeneratedId() {
        // Given
        Commande commande = Commande.builder()
                .client(client)
                .dateCommande(LocalDate.now())
                .statut(StatutCommande.COMMANDEE)
                .build();

        // When
        Commande saved = commandeRepository.save(commande);

        // Then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getClient().getId()).isEqualTo(client.getId());
        assertThat(saved.getStatut()).isEqualTo(StatutCommande.COMMANDEE);
    }

    @Test
    void save_shouldCascadeSaveLignes() {
        // Given - AC: create command with 2-3 lines, verify cascade save
        Commande commande = Commande.builder()
                .client(client)
                .dateCommande(LocalDate.now())
                .build();

        LigneCommande ligne1 = LigneCommande.builder()
                .product(product1)
                .quantite(2)
                .prixUnitaire(new BigDecimal("8.00"))
                .build();
        commande.addLigne(ligne1);

        LigneCommande ligne2 = LigneCommande.builder()
                .product(product2)
                .quantite(1)
                .prixUnitaire(new BigDecimal("17.00"))
                .build();
        commande.addLigne(ligne2);

        // When
        Commande saved = commandeRepository.saveAndFlush(commande);
        entityManager.clear();

        // Then - lines auto-saved with command
        Optional<Commande> found = commandeRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getLignes()).hasSize(2);
    }

    @Test
    void removeLigne_shouldTriggerOrphanRemoval() {
        // Given - AC: verify orphan removal (delete line removes it)
        Commande commande = Commande.builder()
                .client(client)
                .dateCommande(LocalDate.now())
                .build();

        LigneCommande ligne1 = LigneCommande.builder()
                .product(product1)
                .quantite(2)
                .prixUnitaire(new BigDecimal("8.00"))
                .build();
        commande.addLigne(ligne1);

        LigneCommande ligne2 = LigneCommande.builder()
                .product(product2)
                .quantite(1)
                .prixUnitaire(new BigDecimal("17.00"))
                .build();
        commande.addLigne(ligne2);

        Commande saved = commandeRepository.saveAndFlush(commande);
        entityManager.clear();

        // When - remove one line
        Commande toUpdate = commandeRepository.findById(saved.getId()).orElseThrow();
        toUpdate.getLignes().remove(0);
        commandeRepository.saveAndFlush(toUpdate);
        entityManager.clear();

        // Then - orphan should be removed
        Commande updated = commandeRepository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getLignes()).hasSize(1);
    }

    @Test
    void save_shouldPersistStatusAsString() {
        // Given - AC: verify status enum persists as string
        Commande commande = Commande.builder()
                .client(client)
                .dateCommande(LocalDate.now())
                .statut(StatutCommande.RECUPEREE)
                .build();

        // When
        Commande saved = commandeRepository.saveAndFlush(commande);
        entityManager.clear();

        // Then
        Optional<Commande> found = commandeRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getStatut()).isEqualTo(StatutCommande.RECUPEREE);
    }

    @Test
    void findByClientIdOrderByDateCommandeDesc_shouldReturnOrderedCommandes() {
        // Given
        Commande commande1 = Commande.builder()
                .client(client)
                .dateCommande(LocalDate.of(2024, 1, 1))
                .build();
        commandeRepository.save(commande1);

        Commande commande2 = Commande.builder()
                .client(client)
                .dateCommande(LocalDate.of(2024, 6, 15))
                .build();
        commandeRepository.save(commande2);

        Commande commande3 = Commande.builder()
                .client(client)
                .dateCommande(LocalDate.of(2024, 3, 10))
                .build();
        commandeRepository.save(commande3);

        // When
        List<Commande> commandes = commandeRepository.findByClientIdOrderByDateCommandeDesc(client.getId());

        // Then - most recent first
        assertThat(commandes).hasSize(3);
        assertThat(commandes.get(0).getDateCommande()).isEqualTo(LocalDate.of(2024, 6, 15));
        assertThat(commandes.get(1).getDateCommande()).isEqualTo(LocalDate.of(2024, 3, 10));
        assertThat(commandes.get(2).getDateCommande()).isEqualTo(LocalDate.of(2024, 1, 1));
    }

    @Test
    void findByStatut_shouldReturnCommandesWithStatus() {
        // Given
        Commande commandee = Commande.builder()
                .client(client)
                .dateCommande(LocalDate.now())
                .statut(StatutCommande.COMMANDEE)
                .build();
        commandeRepository.save(commandee);

        Commande recuperee = Commande.builder()
                .client(client)
                .dateCommande(LocalDate.now())
                .statut(StatutCommande.RECUPEREE)
                .build();
        commandeRepository.save(recuperee);

        Commande payee = Commande.builder()
                .client(client)
                .dateCommande(LocalDate.now())
                .statut(StatutCommande.PAYEE)
                .build();
        commandeRepository.save(payee);

        // When
        List<Commande> commandees = commandeRepository.findByStatut(StatutCommande.COMMANDEE);
        List<Commande> recuperees = commandeRepository.findByStatut(StatutCommande.RECUPEREE);
        List<Commande> payees = commandeRepository.findByStatut(StatutCommande.PAYEE);

        // Then
        assertThat(commandees).hasSize(1);
        assertThat(recuperees).hasSize(1);
        assertThat(payees).hasSize(1);
    }

    @Test
    void findByDateCommandeBetween_shouldReturnCommandesInRange() {
        // Given
        Commande jan = Commande.builder()
                .client(client)
                .dateCommande(LocalDate.of(2024, 1, 15))
                .build();
        commandeRepository.save(jan);

        Commande mar = Commande.builder()
                .client(client)
                .dateCommande(LocalDate.of(2024, 3, 15))
                .build();
        commandeRepository.save(mar);

        Commande dec = Commande.builder()
                .client(client)
                .dateCommande(LocalDate.of(2024, 12, 15))
                .build();
        commandeRepository.save(dec);

        // When
        List<Commande> q1 = commandeRepository.findByDateCommandeBetween(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31)
        );

        // Then
        assertThat(q1).hasSize(2);
    }

    @Test
    void save_shouldDefaultToCommandeeStatus() {
        // Given
        Commande commande = Commande.builder()
                .client(client)
                .dateCommande(LocalDate.now())
                .build();

        // When
        Commande saved = commandeRepository.save(commande);

        // Then
        assertThat(saved.getStatut()).isEqualTo(StatutCommande.COMMANDEE);
    }

    @Test
    void ligneCommande_getTotal_shouldCalculateCorrectly() {
        // Given
        LigneCommande ligne = LigneCommande.builder()
                .quantite(3)
                .prixUnitaire(new BigDecimal("8.50"))
                .build();

        // When/Then
        assertThat(ligne.getTotal()).isEqualByComparingTo(new BigDecimal("25.50"));
    }
}
