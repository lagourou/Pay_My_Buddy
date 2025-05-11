package com.pay_my_buddy.OC_P6.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pay_my_buddy.OC_P6.model.UserConnectionId;
import com.pay_my_buddy.OC_P6.model.UserConnections;

@Repository
public interface UserConnectionsRepository extends JpaRepository<UserConnections, UserConnectionId> {

    // Récupère les connexions d'un utilisateur avec son ID
    List<UserConnections> findByUserId(Long userId);

    // Vérifie si une connexion existe entre deux utilisateurs
    boolean existsByUserIdAndConnectionId(Long userId, Long connectionId);

    // Récupère la liste des connexions avec leurs détails
    @Query("SELECT uc FROM UserConnections uc JOIN FETCH uc.connection WHERE uc.user.id = :userId")
    List<UserConnections> findConnectionsByUserId(@Param("userId") Long userId);

}
