package com.ensaf.pharmacy.controller;

import com.ensaf.pharmacy.model.Role;
import com.ensaf.pharmacy.model.User;
import com.ensaf.pharmacy.repository.RoleRepository;
import com.ensaf.pharmacy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import java.util.Set;

/**
 * Contrôleur réservé à l'administrateur pour gérer les comptes utilisateurs
 * (lister, ajouter, modifier, supprimer).
 * Toutes les routes commencent par /admin/users. L'accès à ce préfixe est
 * restreint au rôle ADMIN dans la configuration de sécurité
 */


@Controller
@RequestMapping("/admin/users")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Pour crypter les mots de passe

    //Lister tous les utilisateurs
    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "users"; // -> templates/users.html
    }

    //Formulaire d'ajout d'un utilisateur
    @GetMapping("/new")
    public String showNewForm(Model model) {
        // On passe un User vide : Thymeleaf va remplir ses champs via th:object
        model.addAttribute("user", new User());
        // On charge tous les rôles pour alimenter le menu déroulant du formulaire
        model.addAttribute("allRoles", roleRepository.findAll());
        return "user-form"; // -> templates/user-form.html
    }

    //Formulaire de modification
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        // Optional évite le retour null de findById ; si l'utilisateur n'existe pas
        // (id invalide saisi à la main dans l'URL), on renvoie vers la liste
        // avec un message d'erreur plutôt que d'afficher une erreur 500.
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            ra.addFlashAttribute("error", "Utilisateur introuvable.");
            return "redirect:/admin/users";
        }
        // même concept que pour l'ajout
        model.addAttribute("user", user.get());
        model.addAttribute("allRoles", roleRepository.findAll());
        return "user-form"; // même template que pour l'ajout
    }

    //Enregistrer (Ajout ou Modification)
    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user,
                           @RequestParam("roleName") String roleName,
                           @RequestParam(value = "newPassword", required = false) String newPassword,
                           RedirectAttributes ra) {

        // Le mot de passe n'est pas récupéré depuis le formulaire directement dans l'entité,
        // car il doit être encodé avant sauvegarde. On le récupère donc séparément
        // dans newPassword, puis on l'encode avec BCrypt.
        if (user.getId() == null) {
            //Nouvel utilisateur : mot de passe obligatoire
            if (newPassword == null || newPassword.trim().isEmpty()) {
                ra.addFlashAttribute("error", "Le mot de passe est obligatoire pour un nouvel utilisateur.");
                return "redirect:/admin/users/new";
            }
            user.setPassword(passwordEncoder.encode(newPassword));
        } else {
            //Modification : si le champ est rempli, on met à jour, sinon on garde l'ancien
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                user.setPassword(passwordEncoder.encode(newPassword));
            } else {
                User existingUser = userRepository.findById(user.getId()).orElseThrow();
                user.setPassword(existingUser.getPassword());
            }
        }

        // Gestion du rôle
        Role role = roleRepository.findByName(roleName);
        if (role != null) {
            user.setRoles(Set.of(role));
        }

        userRepository.save(user);
        ra.addFlashAttribute("success", "Utilisateur enregistré avec succès.");
        return "redirect:/admin/users";
    }

    //Supprimer un utilisateur
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes ra) {
        // On vérifie d'abord que l'utilisateur existe
        if (!userRepository.existsById(id)) {
            ra.addFlashAttribute("error", "Utilisateur introuvable.");
        } else {
            userRepository.deleteById(id);
            ra.addFlashAttribute("success", "Utilisateur supprimé.");
        }
        return "redirect:/admin/users";
    }
}