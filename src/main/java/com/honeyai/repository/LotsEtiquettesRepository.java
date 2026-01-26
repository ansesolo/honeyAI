package com.honeyai.repository;

import com.honeyai.model.LotsEtiquettes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for managing lot number sequences per year and honey type.
 */
@Repository
public interface LotsEtiquettesRepository extends JpaRepository<LotsEtiquettes, Long> {

    /**
     * Find the lot counter for a specific year and honey type combination.
     *
     * @param annee    the year (e.g., 2024)
     * @param typeMiel the honey type abbreviation (TF, FOR, CHA)
     * @return the lot counter if exists
     */
    Optional<LotsEtiquettes> findByAnneeAndTypeMiel(Integer annee, String typeMiel);
}
