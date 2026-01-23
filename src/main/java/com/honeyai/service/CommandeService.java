package com.honeyai.service;

import com.honeyai.enums.StatutCommande;
import com.honeyai.exception.InvalidStatusTransitionException;
import com.honeyai.model.Commande;
import com.honeyai.model.LigneCommande;
import com.honeyai.repository.CommandeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommandeService {

    private final CommandeRepository commandeRepository;
    private final ProductService productService;

    @Transactional(readOnly = true)
    public List<Commande> findAll() {
        return commandeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Commande> findById(Long id) {
        return commandeRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Commande> findByClientId(Long clientId) {
        return commandeRepository.findByClientIdOrderByDateCommandeDesc(clientId);
    }

    @Transactional(readOnly = true)
    public List<Commande> findByStatut(StatutCommande statut) {
        return commandeRepository.findByStatut(statut);
    }

    @Transactional(readOnly = true)
    public List<Commande> findAllSortedByDateDesc() {
        return commandeRepository.findAllByOrderByDateCommandeDesc();
    }

    @Transactional(readOnly = true)
    public List<Commande> findByYear(Integer year) {
        return commandeRepository.findByYearOrderByDateCommandeDesc(year);
    }

    @Transactional(readOnly = true)
    public List<Commande> findByYearAndStatut(Integer year, StatutCommande statut) {
        return commandeRepository.findByYearAndStatutOrderByDateCommandeDesc(year, statut);
    }

    @Transactional(readOnly = true)
    public List<Commande> findByStatutSortedByDateDesc(StatutCommande statut) {
        return commandeRepository.findByStatutOrderByDateCommandeDesc(statut);
    }

    @Transactional(readOnly = true)
    public List<Integer> getDistinctYears() {
        return commandeRepository.findDistinctYears();
    }

    @Transactional(readOnly = true)
    public List<Commande> findWithFilters(Integer year, StatutCommande statut) {
        if (year != null && statut != null) {
            return findByYearAndStatut(year, statut);
        } else if (year != null) {
            return findByYear(year);
        } else if (statut != null) {
            return findByStatutSortedByDateDesc(statut);
        } else {
            return findAllSortedByDateDesc();
        }
    }

    public Commande create(Commande commande) {
        log.info("Creating new commande for client #{}",
                commande.getClient() != null ? commande.getClient().getId() : "null");

        // Auto-populate dateCommande with today if null
        if (commande.getDateCommande() == null) {
            commande.setDateCommande(LocalDate.now());
        }

        // Set default status if null
        if (commande.getStatut() == null) {
            commande.setStatut(StatutCommande.COMMANDEE);
        }

        // Validate at least one ligne exists
        if (commande.getLignes() == null || commande.getLignes().isEmpty()) {
            throw new IllegalArgumentException("La commande doit contenir au moins une ligne");
        }

        // Auto-fetch prixUnitaire from current year tarif if not provided
        for (LigneCommande ligne : commande.getLignes()) {
            if (ligne.getPrixUnitaire() == null && ligne.getProduct() != null) {
                BigDecimal price = productService.getCurrentYearPrice(ligne.getProduct().getId());
                ligne.setPrixUnitaire(price);
                log.debug("Auto-populated price {} for product #{}", price, ligne.getProduct().getId());
            }
        }

        Commande saved = commandeRepository.save(commande);
        log.info("Created commande #{} with {} lignes", saved.getId(), saved.getLignes().size());
        return saved;
    }

    public Commande updateStatut(Long commandeId, StatutCommande newStatut) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new IllegalArgumentException("Commande not found: " + commandeId));

        StatutCommande oldStatut = commande.getStatut();

        if (!commande.canTransitionTo(newStatut)) {
            log.warn("Invalid status transition attempted: {} -> {} for commande #{}",
                    oldStatut, newStatut, commandeId);
            throw new InvalidStatusTransitionException(oldStatut, newStatut);
        }

        commande.setStatut(newStatut);
        Commande saved = commandeRepository.save(commande);

        log.info("Status transition for commande #{}: {} -> {} at {}",
                commandeId, oldStatut, newStatut, LocalDateTime.now());

        return saved;
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateTotal(Long commandeId) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new IllegalArgumentException("Commande not found: " + commandeId));

        return commande.getLignes().stream()
                .map(LigneCommande::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Save an existing commande (for updates).
     */
    public Commande save(Commande commande) {
        log.info("Saving commande #{}", commande.getId());
        return commandeRepository.save(commande);
    }
}
