package com.ensaf.pharmacy.controller;

import com.ensaf.pharmacy.model.Medicament;
import com.ensaf.pharmacy.model.Vente;
import com.ensaf.pharmacy.repository.MedicamentRepository;
import com.ensaf.pharmacy.repository.VenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Optional;

//Contrôleur dédié au caissier : enregistrer une vente et mettre à jour
//automatiquement le stock du médicament concerné.
@Controller
@RequestMapping("/caissier/ventes")
public class CaissierController {

    @Autowired
    private MedicamentRepository medicamentRepository;

    @Autowired
    private VenteRepository venteRepository;

    //Afficher la page d'enregistrement des ventes
    @GetMapping
    public String showVenteForm(Model model) {
        // On charge tous les médicaments pour alimenter le menu déroulant :
        // le caissier choisit le produit, saisit la quantité, puis valide.
        model.addAttribute("medicaments", medicamentRepository.findAll());
        return "ventes"; // -> templates/ventes.html
    }

    //Traiter la vente
    @PostMapping
    public String enregistrerVente(@RequestParam Long medicamentId,
                                   @RequestParam Integer quantite,
                                   RedirectAttributes ra) {
        // Optional évite de manipuler un null si l'id envoyé depuis le formulaire
        // ne correspond à aucun médicament (formulaire altéré, id obsolète, etc.).
        Optional<Medicament> optMedicament = medicamentRepository.findById(medicamentId);

        if (optMedicament.isPresent()) {
            Medicament med = optMedicament.get();

            // Vérifier si le stock est suffisant
            // On refuse la vente si le stock disponible est inférieur à la quantité
            // demandée, et on renvoie un message explicite au caissier.
            if (med.getStock() < quantite) {
                ra.addFlashAttribute("error", "Erreur : Stock insuffisant ! Il ne reste que " + med.getStock() + " unités.");
                return "redirect:/caissier/ventes";
            }

            //Mise à jour du stock
            med.setStock(med.getStock() - quantite);
            medicamentRepository.save(med);

            //Enregistrer la vente
            Vente vente = new Vente();
            vente.setMedicament(med);
            vente.setQuantite(quantite);
            vente.setDateVente(LocalDateTime.now());
            venteRepository.save(vente);

            ra.addFlashAttribute("success", "Vente enregistrée avec succès ! Stock mis à jour.");
        } else {
            ra.addFlashAttribute("error", "Médicament introuvable.");
        }

        return "redirect:/caissier/ventes";
    }
}