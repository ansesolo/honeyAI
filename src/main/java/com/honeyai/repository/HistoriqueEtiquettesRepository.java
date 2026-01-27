package com.honeyai.repository;

import com.honeyai.model.HistoriqueEtiquettes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for label generation history.
 */
public interface HistoriqueEtiquettesRepository extends JpaRepository<HistoriqueEtiquettes, Long> {

    /**
     * Find the most recent label generations, limited to top 20.
     *
     * @return list of recent history entries ordered by generation date descending
     */
    List<HistoriqueEtiquettes> findTop20ByOrderByDateGenerationDesc();
}
