package com.honeyai.repository;

import com.honeyai.enums.StatutCommande;
import com.honeyai.model.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    /**
     * Find orders by year (extracted from dateCommande), sorted by date descending.
     */
    @Query("SELECT c FROM Commande c WHERE YEAR(c.dateCommande) = :year ORDER BY c.dateCommande DESC")
    List<Commande> findByYearOrderByDateCommandeDesc(@Param("year") Integer year);

    /**
     * Find orders by year and status, sorted by date descending.
     */
    @Query("SELECT c FROM Commande c WHERE YEAR(c.dateCommande) = :year AND c.statut = :statut ORDER BY c.dateCommande DESC")
    List<Commande> findByYearAndStatutOrderByDateCommandeDesc(@Param("year") Integer year, @Param("statut") StatutCommande statut);

    /**
     * Find orders by status, sorted by date descending.
     */
    List<Commande> findByStatutOrderByDateCommandeDesc(StatutCommande statut);

    /**
     * Find all orders sorted by date descending.
     */
    List<Commande> findAllByOrderByDateCommandeDesc();

    /**
     * Get distinct years from all orders for filter dropdown.
     */
    @Query("SELECT DISTINCT YEAR(c.dateCommande) FROM Commande c ORDER BY YEAR(c.dateCommande) DESC")
    List<Integer> findDistinctYears();
}
