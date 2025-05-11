package com.pay_my_buddy.OC_P6.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pay_my_buddy.OC_P6.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Recherche un utilisateur par son email
    Optional<User> findByEmail(String email);

    // Recherche un utilisateur par son email et son mot de passe
    Optional<User> findByEmailAndPassword(String email, String password);

}
