package com.honeyai.repository;

import com.honeyai.enums.StatutCommande;
import com.honeyai.model.Commande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Long> {

    /**
     * Find all orders for a client, most recent first.
     */
    List<Commande> findByClientIdOrderByDateCommandeDesc(Long clientId);

    /**
     * Find all orders with a specific status.
     */
    List<Commande> findByStatut(StatutCommande statut);

    /**
     * Find all orders within a date range.
     */
    List<Commande> findByDateCommandeBetween(LocalDate start, LocalDate end);
}
