package com.ensaf.pharmacy.config;

import com.ensaf.pharmacy.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration centrale de Spring Security.
 * Trois responsabilités :
 *   1. Définir le PasswordEncoder (BCrypt) utilisé pour hacher les mots de passe
 *   2. Brancher notre CustomUserDetailsService sur l'authentification
 *   3. Déclarer les règles d'accès aux URLs, le formulaire de login et le logout.

 * Chaque rôle a un préfixe d'URL dédié
 * (/admin/**, /pharmacien/**, /caissier/**, /client/**),
 * ce qui rend les règles lisibles et faciles à vérifier.
 */

@Configuration
@EnableWebSecurity // active la sécurité web de Spring
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    //Encodeur de mots de passe.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Indique à Spring Security comment authentifier un utilisateur :
     *   - via notre CustomUserDetailsService (recherche en base par username),
     *   - et en comparant le mot de passe saisi avec le hash stocké via BCrypt.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * C'est ici que sont définies toutes les règles
     * de sécurité (qui peut accéder à quoi, comment on se connecte, comment on se déconnecte).
     */

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Ressources statiques et pages publiques
                        .requestMatchers("/", "/login", "/css/**", "/js/**").permitAll()

                        // Admin : Gestion des utilisateurs, médicaments, stocks
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Pharmacien : Consulter medicaments , modifier stocks
                        .requestMatchers("/pharmacien/**").hasRole("PHARMACIEN")

                        // Caissier : Enregistrer les ventes uniquement
                        .requestMatchers("/caissier/**").hasRole("CAISSIER")

                        // Client : Consulter les médicaments disponibles (lecture seule)
                        .requestMatchers("/client/**").hasRole("CLIENT")

                        // Toute autre requête nécessite une authentification
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/access-denied")
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        // true = toujours rediriger vers /home après connexion
                        .defaultSuccessUrl("/home", true)
                        // /login doit être accessible sans être connecté (sinon boucle infinie)
                        .permitAll()
                )
                .logout(logout -> logout
                        // URL à appeler pour se déconnecter
                        .logoutUrl("/logout")
                        // Après déconnexion, on revient sur /login avec ?logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}