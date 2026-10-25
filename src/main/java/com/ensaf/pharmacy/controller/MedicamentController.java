package com.ensaf.pharmacy.controller;

import com.ensaf.pharmacy.repository.MedicamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.ensaf.pharmacy.model.Medicament;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Contrôleur réservé à l'administrateur pour gérer le catalogue de médicaments
 * (lister, ajouter, modifier, supprimer).
 * Le préfixe /admin/medicaments est protégé par la règle ADMIN dans la
 * configuration de sécurité, comme /admin/users. Contrairement au contrôleur
 * client (lecture seule), celui-ci autorise les modifications : c'est ici que
 * l'admin tient à jour les prix, les stocks et les descriptions.
 */


@Controller
@RequestMapping("/admin/medicaments")
public class MedicamentController {

    @Autowired
    private MedicamentRepository medicamentRepository;

    //Lister tous les médicaments
    @GetMapping
    public String listMedicaments(Model model) {
        //Renvoi tous les medicaments
        model.addAttribute("medicaments", medicamentRepository.findAll());
        return "medicaments"; // -> templates/medicaments.html
    }

    //Formulaire pour ajouter un médicament
    @GetMapping("/new")
    public String showNewForm(Model model) {
        // Entité vide : Thymeleaf va remplir ses champs (th:object="${medicament}")
        model.addAttribute("medicament", new Medicament());
        return "medicament-form";
    }

    //Formulaire pour modifier un médicament
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        // orElse(null) : si l'id n'existe pas (URL tapée à la main, id obsolète),
        // on redirige vers la liste avec un message plutôt que d'afficher une 500.
        Medicament medicament = medicamentRepository.findById(id)
                .orElse(null);
        if (medicament == null) {
            ra.addFlashAttribute("error", "Médicament introuvable (id=" + id + ")");
            return "redirect:/admin/medicaments"; // si le médicament n'existe pas, on retour vers la liste des médicaments
        }
        model.addAttribute("medicament", medicament);
        return "medicament-form";
    }

    //La fonction d'enregistrement du médicament dans la DB (marche avec l'ajout et l'update)
    @PostMapping("/save")
    public String saveMedicament(@Valid @ModelAttribute("medicament") Medicament medicament,
                                 BindingResult result,
                                 RedirectAttributes ra) {
        if (result.hasErrors()) {
            // On réaffiche le formulaire en conservant les valeurs saisies.
            return "medicament-form";
        }
        medicamentRepository.save(medicament);
        ra.addFlashAttribute("success", "Médicament enregistré avec succès.");
        return "redirect:/admin/medicaments";
    }

    //La fonction de Suppression un médicament
    @PostMapping("/delete/{id}")
    public String deleteMedicament(@PathVariable Long id, RedirectAttributes ra) {
        // On vérifie l'existence avant de supprimer
        if (!medicamentRepository.existsById(id)) {
            ra.addFlashAttribute("error", "Médicament introuvable (id=" + id + ")");
        } else {
            medicamentRepository.deleteById(id);
            ra.addFlashAttribute("success", "Médicament supprimé.");
        }
        return "redirect:/admin/medicaments"; //Retour vers la list des médicaments
    }
}