package com.ensaf.pharmacy.controller;

import com.ensaf.pharmacy.repository.MedicamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//Contrôleur réservé aux clients : consultation du catalogue des médicaments

@Controller
@RequestMapping("/client/medicaments")   // ← réservé à Client
public class ClientMedicamentController {

    @Autowired
    private MedicamentRepository medicamentRepository;

    /**
     * Affiche la liste des médicaments en stock (stock > 0)
     * On utilise findByStockGreaterThan(0) plutôt que findAll() pour ne pas
     * afficher au client des produits épuisés
     */

    @GetMapping
    public String listDisponibles(Model model) {
        model.addAttribute("medicaments", medicamentRepository.findByStockGreaterThan(0));
        return "client-medicaments"; // -> templates/client-medicaments.html
    }
}