package com.ensaf.pharmacy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    //Affiche le formulaire de connexion.Accessible pour tous les roles
    @GetMapping("/login")
    public String login() {
        return "login"; // -> templates/login.html
    }

    //Page d'accueil affichée après une connexion réussie.Accessible aux utilisateurs authentifiés
    @GetMapping("/home")
    public String home() {
        return "home"; // -> templates/home.html
    }

    //Page affichée quand un utilisateur connecté tente d'accéder à une
    //ressource pour laquelle il n'a pas les droits (ex: un USER qui essai d'atteindre une page réservée à l'admin).
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied"; // -> templates/access-denied.html
    }
}