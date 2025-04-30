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

@Service
@RequiredArgsConstructor
public class UserConnectionsService {

    private final UserConnectionsRepository userConnectionsRepository;
    private final UserRepository userRepository;

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

    @Transactional
    public void deleteConnection(Long userId, String email) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        User connectionUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Aucun utilisateur trouvé avec cet e-mail"));

        UserConnectionId userConnectionId = new UserConnectionId(user.getId(), connectionUser.getId());

        if (!userConnectionsRepository.existsByUserIdAndConnectionId(user.getId(), connectionUser.getId())) {
            throw new IllegalArgumentException("Cette connexion n'existe pas");
        }

        userConnectionsRepository.deleteById(userConnectionId);
    }

}