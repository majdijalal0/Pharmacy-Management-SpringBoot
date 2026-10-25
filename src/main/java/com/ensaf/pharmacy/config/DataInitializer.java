package com.ensaf.pharmacy.config;

import com.ensaf.pharmacy.model.Role;
import com.ensaf.pharmacy.model.User;
import com.ensaf.pharmacy.repository.RoleRepository;
import com.ensaf.pharmacy.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

    /**
     * Initialise la base de données au démarrage de l'application : création des
     * rôles et des comptes par défaut (un par rôle) s'ils n'existent pas encore.
     */
@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepository,
                                          RoleRepository roleRepository,
                                          PasswordEncoder passwordEncoder) {
        return args -> {
            // On n'initialise que si la base est vide (premier lancement).
            // Sur les lancements suivants, les données existent déjà
            if (userRepository.count() == 0) {

                //création des rôles
                Role adminRole = roleRepository.save(new Role(null, "ROLE_ADMIN"));
                Role pharmaRole = roleRepository.save(new Role(null, "ROLE_PHARMACIEN"));
                Role caissierRole = roleRepository.save(new Role(null, "ROLE_CAISSIER"));
                Role clientRole = roleRepository.save(new Role(null, "ROLE_CLIENT"));

                //Création des utilisateurs
                //Chaque compte est associé à un seul rôle
                userRepository.save(new User(null, "admin", passwordEncoder.encode("admin123"), Set.of(adminRole)));
                userRepository.save(new User(null, "pharma", passwordEncoder.encode("pharma123"), Set.of(pharmaRole)));
                userRepository.save(new User(null, "caissier", passwordEncoder.encode("caisse123"), Set.of(caissierRole)));
                userRepository.save(new User(null, "client", passwordEncoder.encode("client123"), Set.of(clientRole)));

                System.out.println("4 Roles and 4 Users created successfully!");
            }
        };
    }
}