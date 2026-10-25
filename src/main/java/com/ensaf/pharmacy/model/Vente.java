package com.ensaf.pharmacy.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Représente une vente effectuée dans la pharmacie.
 * Chaque vente concerne un seul médicament et une quantité donnée,
 * avec la date/heure à laquelle la vente a eu lieu.
 */

@Setter // genere les setters
@Getter //genere les getters
@Entity
@Table(name = "ventes")
public class Vente {
    //Id generer automatiquement
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Plusieurs ventes peuvent concerner le même médicament, mais chaque vente
    // ne référence qu'un seul médicament : c'est donc une relation ManyToOne.
    // nullable = false empêche d'enregistrer une vente sans médicament associé.
    @ManyToOne
    @JoinColumn(name = "medicament_id", nullable = false)
    private Medicament medicament;

    // Nombre d'unités vendues
    @Column(nullable = false)
    private Integer quantite;

    // Date et heure de la vente
    @Column(name = "date_vente", nullable = false)
    private LocalDateTime dateVente;

    // Constructeur vide
    public Vente() {
    }

    // Constructeur avec attributs
    public Vente(Medicament medicament, Integer quantite, LocalDateTime dateVente) {
        this.medicament = medicament;
        this.quantite = quantite;
        this.dateVente = dateVente;
    }



}