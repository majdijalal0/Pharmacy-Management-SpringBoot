package com.ensaf.pharmacy.repository;

import com.ensaf.pharmacy.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    //Retrouve un rôle par son nom
    Role findByName(String name);
}
