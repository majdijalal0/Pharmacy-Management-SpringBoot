package com.ensaf.pharmacy.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users") // Le nom de la table SQL
@Data //generer les getters and setters et toString
@NoArgsConstructor //Generer constructeur vide
@AllArgsConstructor //Generer constructeur avec attributs

public class User implements UserDetails {
    //Generer l'id automatiquement
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Username est unique et NON NULL
    @Column(unique = true, nullable = false)
    private String username;

    //Password est NON NULL
    @Column(nullable = false)
    private String password;

    //Un utilisateur peut avoir plusieurs rôles donc Relation ManyToMany avec les roles
    @ManyToMany(fetch = FetchType.EAGER)  // EAGER est utilisé ici parce qu'on a besoin des rôles dès le chargement
    @JoinTable(
            name = "user_roles", // table de jointure entre users et roles
            joinColumns = @JoinColumn(name = "user_id"), //Reference id pour la table user
            inverseJoinColumns = @JoinColumn(name = "role_id") //Reference id pour la table role
    )

    //Set contient les roles de l'utilisateur
    private Set<Role> roles = new HashSet<>();

    //Transforme les rôles de l'utilisateur en autorités comprises par Spring Security.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    // Ces méthodes viennent de l'interface UserDetails.
    // On les laisse à true pour simplifier : on ne gère ni l'expiration,
    // ni le verrouillage, ni la désactivation des comptes dans ce projet
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}