package com.honeyai.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity for tracking label generation history.
 * Provides traceability for regulatory compliance.
 */
@Entity
@Table(name = "historique_etiquettes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoriqueEtiquettes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_miel", nullable = false)
    private String typeMiel;

    @Column(name = "format_pot", nullable = false)
    private String formatPot;

    @Column(name = "date_recolte", nullable = false)
    private LocalDate dateRecolte;

    @Column(name = "dluo", nullable = false)
    private LocalDate dluo;

    @Column(nullable = false)
    private Integer quantite;

    @Column(name = "date_generation", nullable = false)
    private LocalDateTime dateGeneration;

    @Column(name = "prix_unitaire", precision = 10, scale = 2)
    private BigDecimal prixUnitaire;
}
