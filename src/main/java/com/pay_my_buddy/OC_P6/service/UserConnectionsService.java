package com.pay_my_buddy.OC_P6.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pay_my_buddy.OC_P6.dto.UserConnectionsResponseDTO;
import com.pay_my_buddy.OC_P6.model.User;
import com.pay_my_buddy.OC_P6.model.UserConnectionId;
import com.pay_my_buddy.OC_P6.model.UserConnections;
import com.pay_my_buddy.OC_P6.repository.UserConnectionsRepository;
import com.pay_my_buddy.OC_P6.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service pour gérer les connexions entre utilisateurs.
 */
@Service
@RequiredArgsConstructor
public class UserConnectionsService {

    private final UserConnectionsRepository userConnectionsRepository;
    private final UserRepository userRepository;

    /**
     * Ajoute une nouvelle connexion entre deux utilisateurs.
     *
     * @param userId l'ID de l'utilisateur qui initie la connexion
     * @param email  l'email de l'utilisateur à ajouter en connexion
     * @return l'objet UserConnections représentant la connexion
     * @throws IllegalArgumentException si un utilisateur est introuvable ou si la
     *                                  connexion est invalide
     */
    @Transactional
    public UserConnections addConnections(Long userId, String email) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Aucun utilisateur trouvé"));

        User connectionUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Aucun utilisateur trouvé avec cet e-mail : " + email));

        if (user.getId().equals(connectionUser.getId())) {
            throw new IllegalArgumentException("Vous ne pouvez pas vous connecter à vous-même");
        }

        UserConnectionId userConnectionId = new UserConnectionId(user.getId(), connectionUser.getId());

        if (userConnectionsRepository.existsByUserIdAndConnectionId(user.getId(), connectionUser.getId())) {
            throw new IllegalArgumentException("Cette connexion existe déjà");
        }

        UserConnections userConnections = new UserConnections();
        userConnections.setId(userConnectionId);
        userConnections.setUser(user);
        userConnections.setConnection(connectionUser);

        return userConnectionsRepository.save(userConnections);
    }

    /**
     * Récupère la liste des connexions d'un utilisateur.
     *
     * @param userId l'ID de l'utilisateur dont on veut récupérer les connexions
     * @return une liste de DTO contenant les informations des connexions de
     *         l'utilisateur
     */
    @Transactional
    public List<UserConnectionsResponseDTO> getUserConnections(Long userId) {

        List<UserConnections> connections = userConnectionsRepository.findConnectionsByUserId(userId);

        return connections.stream()
                .map(connection -> new UserConnectionsResponseDTO(
                        connection.getConnection().getId(),
                        connection.getConnection().getUsername(),
                        connection.getConnection().getEmail()))
                .collect(Collectors.toList());
    }
}