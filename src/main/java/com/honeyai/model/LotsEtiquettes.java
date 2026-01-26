package com.honeyai.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity tracking the last used lot number per year and honey type combination.
 * Used to generate sequential, unique lot numbers for labels.
 */
@Entity
@Table(name = "lots_etiquettes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"annee", "type_miel"}, name = "uk_lots_annee_type")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LotsEtiquettes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Year for lot numbering (e.g., 2024, 2025).
     */
    @Column(nullable = false)
    private Integer annee;

    /**
     * Honey type abbreviation (TF, FOR, CHA).
     */
    @Column(name = "type_miel", nullable = false, length = 10)
    private String typeMiel;

    /**
     * Last used sequential number for this year/type combination.
     */
    @Column(name = "dernier_numero", nullable = false)
    private Integer dernierNumero;
}
