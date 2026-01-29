package com.honeyai.service;

import com.honeyai.enums.CategorieAchat;
import com.honeyai.model.Achat;
import com.honeyai.repository.AchatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AchatService {

    private final AchatRepository achatRepository;

    @Transactional(readOnly = true)
    public List<Achat> findAll() {
        return achatRepository.findAllByOrderByDateAchatDesc();
    }

    @Transactional(readOnly = true)
    public Achat findById(Long id) {
        return achatRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Achat #" + id + " introuvable"));
    }

    public Achat save(Achat achat) {
        log.info("Enregistrement achat: {} - {} €", achat.getDesignation(), achat.getMontant());
        return achatRepository.save(achat);
    }

    public void delete(Long id) {
        Achat achat = findById(id);
        log.info("Suppression achat #{}: {}", id, achat.getDesignation());
        achatRepository.delete(achat);
    }

    @Transactional(readOnly = true)
    public List<Achat> findByPeriod(LocalDate start, LocalDate end) {
        return achatRepository.findByDateAchatBetween(start, end);
    }

    @Transactional(readOnly = true)
    public List<Achat> findByCategorie(CategorieAchat categorie) {
        return achatRepository.findByCategorie(categorie);
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateTotalDepenses(LocalDate start, LocalDate end) {
        List<Achat> achats = achatRepository.findByDateAchatBetween(start, end);
        return achats.stream()
                .map(Achat::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional(readOnly = true)
    public Map<CategorieAchat, BigDecimal> calculateDepensesByCategorie(LocalDate start, LocalDate end) {
        List<Achat> achats = achatRepository.findByDateAchatBetween(start, end);
        Map<CategorieAchat, BigDecimal> result = new EnumMap<>(CategorieAchat.class);
        for (Achat achat : achats) {
            result.merge(achat.getCategorie(), achat.getMontant(), BigDecimal::add);
        }
        return result;
    }
}
