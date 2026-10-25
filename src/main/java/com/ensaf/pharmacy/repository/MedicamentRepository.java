package com.ensaf.pharmacy.repository;

import com.ensaf.pharmacy.model.Medicament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicamentRepository extends JpaRepository<Medicament, Long> {
    //Retourne les médicaments dont le stock est supérieur à la valeur donnée.
    //Utilisé dans le code pour retourner les medicaments si le stock > 0
    List<Medicament> findByStockGreaterThan(int stock);
}
