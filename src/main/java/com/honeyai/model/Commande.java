package com.honeyai.model;

import com.honeyai.enums.StatutCommande;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "commandes")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    @NotNull(message = "Le client est obligatoire")
    private Client client;

    @NotNull(message = "La date de commande est obligatoire")
    @Column(name = "date_commande", nullable = false)
    private LocalDate dateCommande;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatutCommande statut = StatutCommande.COMMANDEE;

    @Column(length = 1000)
    private String notes;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<LigneCommande> lignes = new ArrayList<>();

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void addLigne(LigneCommande ligne) {
        lignes.add(ligne);
        ligne.setCommande(this);
    }

    public void removeLigne(LigneCommande ligne) {
        lignes.remove(ligne);
        ligne.setCommande(null);
    }

    /**
     * Check if transition to new status is allowed.
     * Only forward transitions are permitted: COMMANDEE -> RECUPEREE -> PAYEE
     */
    public boolean canTransitionTo(StatutCommande newStatut) {
        if (this.statut == null || newStatut == null) {
            return false;
        }
        return switch (this.statut) {
            case COMMANDEE -> newStatut == StatutCommande.RECUPEREE;
            case RECUPEREE -> newStatut == StatutCommande.PAYEE;
            case PAYEE -> false; // Terminal state
        };
    }
}
