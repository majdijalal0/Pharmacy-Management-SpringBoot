package com.ensaf.pharmacy.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un médicament vendu par la pharmacie.
 * elle est référencée par les ventes pour garder une trace de ce qui a été vendu.
 */


@Entity
//Le nom de la table associé
@Table(name = "medicaments")
@Data //generer les getters and setters et toString
@NoArgsConstructor // constructeur vide
@AllArgsConstructor // constructeur avec tous les attributs
public class Medicament {

    //Id generer automatiquement
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Le nom identifie le médicament, doit etre non null
    @Column(nullable = false)
    private String nom;

    //Détails du produit
    private String description;
    private Double prix;
    private Integer stock;

    // Relation bidirectionnelle : Un médicament peut avoir plusieurs ventes
    // cascade = ALL : supprimer un médicament supprime aussi les ventes liées.
    // orphanRemoval = true : si on retire une vente de cette liste, elle est supprimée en base.
    @OneToMany(mappedBy = "medicament", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vente> ventes = new ArrayList<>();

}