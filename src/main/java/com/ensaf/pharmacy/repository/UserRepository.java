package com.ensaf.pharmacy.repository;

import com.ensaf.pharmacy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //Retrouve un utilisateur par son username.
    User findByUsername(String username);
}

