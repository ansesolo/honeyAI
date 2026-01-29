package com.honeyai.repository;

import com.honeyai.enums.CategorieAchat;
import com.honeyai.model.Achat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AchatRepository extends JpaRepository<Achat, Long> {

    List<Achat> findByDateAchatBetween(LocalDate start, LocalDate end);

    List<Achat> findByCategorie(CategorieAchat categorie);

    List<Achat> findAllByOrderByDateAchatDesc();
}
