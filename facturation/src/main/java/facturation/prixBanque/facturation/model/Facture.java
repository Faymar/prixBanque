package facturation.prixBanque.facturation.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateEmission;

    @Column(nullable = false)
    private Double montant;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Long clientId; // Référence à l'ID du client dans le service de gestion des comptes

    @Column(nullable = false)
    private Long accountId; // Référence à l'ID du compte dans le service de gestion des comptes

    @Column(nullable = false)
    private Boolean paye = false;
    // Getters et setters
}

