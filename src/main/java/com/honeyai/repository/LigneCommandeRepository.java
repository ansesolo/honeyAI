package com.honeyai.repository;

import com.honeyai.model.LigneCommande;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LigneCommandeRepository extends JpaRepository<LigneCommande, Long> {
    // Standard CRUD operations sufficient per story requirements
}
