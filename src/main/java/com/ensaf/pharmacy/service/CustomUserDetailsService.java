package com.ensaf.pharmacy.service;

import com.ensaf.pharmacy.model.User;
import com.ensaf.pharmacy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * Service utilisé par Spring Security pour charger un utilisateur à partir de la base lors de l'authentification.
 * Quand un utilisateur soumet le formulaire de login, Spring Security
 * appelle automatiquement loadUserByUsername() avec le username saisi. On va donc
 * chercher l'utilisateur en base et on le retourne tel quel : comme la classe User
 * implémente déjà UserDetails, elle contient tout ce dont Spring Security a besoin
 * (le mot de passe haché et les autorités via getAuthorities()).
 */

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Charge l'utilisateur correspondant au username fourni.
     * @throws UsernameNotFoundException si aucun utilisateur ne correspond,
     *         pour que Spring Security renvoie bien un échec d'authentification
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Utilisateur non trouvé : " + username);
        }
        return user;
    }
}