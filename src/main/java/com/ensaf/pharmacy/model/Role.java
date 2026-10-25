package com.ensaf.pharmacy.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Représente un rôle d'un utilisateur (ex: ROLE_ADMIN, ROLE_PHARMACIEN).
 * Les rôles servent ensuite à construire les autorités utilisées par Spring Security
 * pour contrôler l'accès aux différentes parties de l'application.
 */


@Entity
//Le nom de la table associé
@Table(name = "roles")
@Data //generer les getters and setters et toString
@NoArgsConstructor // constructeur vide
@AllArgsConstructor // constructeur avec tous les attributs

public class Role {
    //Generer l'id automatiquement
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Le nom du role est unique et non null pour éviter les doublons
    @Column(unique = true, nullable = false)
    private String name; // Ex: "ROLE_ADMIN", "ROLE_PHARMACIEN", etc.
}