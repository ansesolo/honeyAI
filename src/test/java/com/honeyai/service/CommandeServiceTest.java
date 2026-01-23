package com.honeyai.service;

import com.honeyai.enums.HoneyType;
import com.honeyai.enums.StatutCommande;
import com.honeyai.exception.InvalidStatusTransitionException;
import com.honeyai.model.Client;
import com.honeyai.model.Commande;
import com.honeyai.model.LigneCommande;
import com.honeyai.model.Product;
import com.honeyai.repository.CommandeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommandeServiceTest {

    @Mock
    private CommandeRepository commandeRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CommandeService commandeService;

    private Client client;
    private Product product1;
    private Product product2;
    private Commande commande;

    @BeforeEach
    void setUp() {
        client = Client.builder()
                .id(1L)
                .name("Test Client")
                .build();

        product1 = Product.builder()
                .id(1L)
                .name("Miel Toutes Fleurs 500g")
                .type(HoneyType.TOUTES_FLEURS)
                .unit("pot 500g")
                .build();

        product2 = Product.builder()
                .id(2L)
                .name("Miel Forêt 1kg")
                .type(HoneyType.FORET)
                .unit("pot 1kg")
                .build();

        commande = Commande.builder()
                .id(1L)
                .client(client)
                .dateCommande(LocalDate.now())
                .statut(StatutCommande.COMMANDEE)
                .build();
    }

    @Test
    void findAll_shouldReturnAllCommandes() {
        // Given
        when(commandeRepository.findAll()).thenReturn(List.of(commande));

        // When
        List<Commande> result = commandeService.findAll();

        // Then
        assertThat(result).hasSize(1);
        verify(commandeRepository).findAll();
    }

    @Test
    void findById_shouldReturnCommande_whenExists() {
        // Given
        when(commandeRepository.findById(1L)).thenReturn(Optional.of(commande));

        // When
        Optional<Commande> result = commandeService.findById(1L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
    }

    @Test
    void findByClientId_shouldReturnCommandesSortedByDateDesc() {
        // Given
        Commande older = Commande.builder()
                .id(2L)
                .client(client)
                .dateCommande(LocalDate.of(2024, 1, 1))
                .build();
        when(commandeRepository.findByClientIdOrderByDateCommandeDesc(1L))
                .thenReturn(Arrays.asList(commande, older));

        // When
        List<Commande> result = commandeService.findByClientId(1L);

        // Then
        assertThat(result).hasSize(2);
        verify(commandeRepository).findByClientIdOrderByDateCommandeDesc(1L);
    }

    @Test
    void findByStatut_shouldReturnCommandesWithStatus() {
        // Given
        when(commandeRepository.findByStatut(StatutCommande.COMMANDEE))
                .thenReturn(List.of(commande));

        // When
        List<Commande> result = commandeService.findByStatut(StatutCommande.COMMANDEE);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatut()).isEqualTo(StatutCommande.COMMANDEE);
    }

    @Test
    void create_shouldAutoPopulatePrices_whenNotProvided() {
        // Given
        LigneCommande ligne = LigneCommande.builder()
                .product(product1)
                .quantite(2)
                .build();
        commande.addLigne(ligne);

        when(productService.getCurrentYearPrice(1L)).thenReturn(new BigDecimal("8.00"));
        when(commandeRepository.save(any(Commande.class))).thenReturn(commande);

        // When
        Commande result = commandeService.create(commande);

        // Then
        assertThat(result.getLignes().get(0).getPrixUnitaire())
                .isEqualByComparingTo(new BigDecimal("8.00"));
        verify(productService).getCurrentYearPrice(1L);
    }

    @Test
    void create_shouldAutoPopulateDateCommande_whenNull() {
        // Given
        LigneCommande ligne = LigneCommande.builder()
                .product(product1)
                .quantite(2)
                .prixUnitaire(new BigDecimal("8.00"))
                .build();

        Commande newCommande = Commande.builder()
                .client(client)
                .build();
        newCommande.addLigne(ligne);

        when(commandeRepository.save(any(Commande.class))).thenAnswer(i -> i.getArgument(0));

        // When
        Commande result = commandeService.create(newCommande);

        // Then
        assertThat(result.getDateCommande()).isEqualTo(LocalDate.now());
    }

    @Test
    void create_shouldSetDefaultStatus_whenNull() {
        // Given
        LigneCommande ligne = LigneCommande.builder()
                .product(product1)
                .quantite(2)
                .prixUnitaire(new BigDecimal("8.00"))
                .build();

        Commande newCommande = Commande.builder()
                .client(client)
                .dateCommande(LocalDate.now())
                .statut(null)
                .build();
        newCommande.addLigne(ligne);

        when(commandeRepository.save(any(Commande.class))).thenAnswer(i -> i.getArgument(0));

        // When
        Commande result = commandeService.create(newCommande);

        // Then
        assertThat(result.getStatut()).isEqualTo(StatutCommande.COMMANDEE);
    }

    @Test
    void create_shouldThrowException_whenNoLignes() {
        // Given
        Commande emptyCommande = Commande.builder()
                .client(client)
                .dateCommande(LocalDate.now())
                .build();

        // When/Then
        assertThatThrownBy(() -> commandeService.create(emptyCommande))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("au moins une ligne");
    }

    @Test
    void calculateTotal_shouldSumAllLignes() {
        // Given
        LigneCommande ligne1 = LigneCommande.builder()
                .product(product1)
                .quantite(2)
                .prixUnitaire(new BigDecimal("8.00"))
                .build();
        LigneCommande ligne2 = LigneCommande.builder()
                .product(product2)
                .quantite(1)
                .prixUnitaire(new BigDecimal("17.00"))
                .build();
        commande.addLigne(ligne1);
        commande.addLigne(ligne2);

        when(commandeRepository.findById(1L)).thenReturn(Optional.of(commande));

        // When
        BigDecimal total = commandeService.calculateTotal(1L);

        // Then - (2 * 8.00) + (1 * 17.00) = 33.00
        assertThat(total).isEqualByComparingTo(new BigDecimal("33.00"));
    }

    @Test
    void updateStatut_shouldTransition_fromCommandeeToRecuperee() {
        // Given
        when(commandeRepository.findById(1L)).thenReturn(Optional.of(commande));
        when(commandeRepository.save(any(Commande.class))).thenAnswer(i -> i.getArgument(0));

        // When
        Commande result = commandeService.updateStatut(1L, StatutCommande.RECUPEREE);

        // Then
        assertThat(result.getStatut()).isEqualTo(StatutCommande.RECUPEREE);
    }

    @Test
    void updateStatut_shouldTransition_fromRecupereeToPayee() {
        // Given
        commande.setStatut(StatutCommande.RECUPEREE);
        when(commandeRepository.findById(1L)).thenReturn(Optional.of(commande));
        when(commandeRepository.save(any(Commande.class))).thenAnswer(i -> i.getArgument(0));

        // When
        Commande result = commandeService.updateStatut(1L, StatutCommande.PAYEE);

        // Then
        assertThat(result.getStatut()).isEqualTo(StatutCommande.PAYEE);
    }

    @Test
    void updateStatut_shouldThrowException_whenBackwardTransition() {
        // Given
        commande.setStatut(StatutCommande.PAYEE);
        when(commandeRepository.findById(1L)).thenReturn(Optional.of(commande));

        // When/Then
        assertThatThrownBy(() -> commandeService.updateStatut(1L, StatutCommande.COMMANDEE))
                .isInstanceOf(InvalidStatusTransitionException.class)
                .hasMessageContaining("PAYEE")
                .hasMessageContaining("COMMANDEE");
    }

    @Test
    void updateStatut_shouldThrowException_whenSkippingStatus() {
        // Given - trying to go from COMMANDEE directly to PAYEE
        when(commandeRepository.findById(1L)).thenReturn(Optional.of(commande));

        // When/Then
        assertThatThrownBy(() -> commandeService.updateStatut(1L, StatutCommande.PAYEE))
                .isInstanceOf(InvalidStatusTransitionException.class)
                .hasMessageContaining("COMMANDEE")
                .hasMessageContaining("PAYEE");
    }

    @Test
    void updateStatut_shouldThrowException_whenCommandeNotFound() {
        // Given
        when(commandeRepository.findById(999L)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> commandeService.updateStatut(999L, StatutCommande.RECUPEREE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("999");
    }
}
