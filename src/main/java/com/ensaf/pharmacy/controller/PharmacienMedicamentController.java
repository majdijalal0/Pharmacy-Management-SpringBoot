package com.ensaf.pharmacy.controller;

import com.ensaf.pharmacy.model.Medicament;
import com.ensaf.pharmacy.repository.MedicamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

    /**
     * Contrôleur réservé au pharmacien : consulter la liste des médicaments et
     * ajuster leur stock
    */

@Controller
@RequestMapping("/pharmacien/medicaments")   // ← réservé à Pharmacien
public class PharmacienMedicamentController {

    @Autowired
    private MedicamentRepository medicamentRepository;

    // Consulter la liste (avec affichage du stock)
    @GetMapping
    public String list(Model model) {
        model.addAttribute("medicaments", medicamentRepository.findAll());
        return "medicaments-list";  // -> templates/medicaments-list.html
    }

    // Seule action de modification autorisée : le stock
    @PostMapping("/stock/{id}")
    public String updateStock(@PathVariable Long id,
                              @RequestParam Integer nouveauStock,
                              RedirectAttributes ra) {
        //trouver le medicament concerné
        Medicament m = medicamentRepository.findById(id).orElseThrow();
        //On remplace par la nouvelle valeur du stock
        m.setStock(nouveauStock);
        medicamentRepository.save(m);
        ra.addFlashAttribute("success", "Stock mis à jour.");
        return "redirect:/pharmacien/medicaments";
    }

}