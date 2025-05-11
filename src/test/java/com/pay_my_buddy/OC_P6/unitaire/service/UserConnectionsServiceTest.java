package com.pay_my_buddy.OC_P6.unitaire.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.pay_my_buddy.OC_P6.dto.UserConnectionsResponseDTO;
import com.pay_my_buddy.OC_P6.model.User;
import com.pay_my_buddy.OC_P6.model.UserConnectionId;
import com.pay_my_buddy.OC_P6.model.UserConnections;
import com.pay_my_buddy.OC_P6.repository.UserConnectionsRepository;
import com.pay_my_buddy.OC_P6.repository.UserRepository;
import com.pay_my_buddy.OC_P6.service.UserConnectionsService;

class UserConnectionsServiceTest {

    @Mock
    private UserConnectionsRepository userConnectionsRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserConnectionsService userConnectionsService;

    private User user;
    private User connectionUser;
    private UserConnections userConnections;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("Laurent");
        user.setEmail("laurent@example.com");

        connectionUser = new User();
        connectionUser.setId(2L);
        connectionUser.setUsername("Jean");
        connectionUser.setEmail("jean@example.com");

        userConnections = new UserConnections();
        userConnections.setId(new UserConnectionId(user.getId(), connectionUser.getId()));
        userConnections.setUser(user);
        userConnections.setConnection(connectionUser);
    }

    @Test
    void testAddConnectionsSuccess() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("jean@example.com")).thenReturn(Optional.of(connectionUser));
        when(userConnectionsRepository.existsByUserIdAndConnectionId(user.getId(), connectionUser.getId()))
                .thenReturn(false);
        when(userConnectionsRepository.save(any(UserConnections.class))).thenReturn(userConnections);

        UserConnections result = userConnectionsService.addConnections(1L, "jean@example.com");

        assertNotNull(result);
        assertEquals(user.getId(), result.getUser().getId());
        assertEquals(connectionUser.getId(), result.getConnection().getId());
        verify(userConnectionsRepository, times(1)).save(any(UserConnections.class));
    }

    @Test
    void testAddConnectionsAlreadyExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("jean@example.com")).thenReturn(Optional.of(connectionUser));
        when(userConnectionsRepository.existsByUserIdAndConnectionId(user.getId(), connectionUser.getId()))
                .thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userConnectionsService.addConnections(1L, "jean@example.com"));
        assertEquals("Cette connexion existe déjà", exception.getMessage()); // ✅ Modifier le message attendu

    }

    @Test
    void testAddConnectionsToSelf() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("laurent@example.com")).thenReturn(Optional.of(user));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userConnectionsService.addConnections(1L, "laurent@example.com"));
        assertEquals("Vous ne pouvez pas vous connecter à vous-même", exception.getMessage()); // ✅ Modifier le message
                                                                                               // attendu

    }

    @Test
    void testAddConnectionsUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userConnectionsService.addConnections(1L, "jean@example.com"));
        assertEquals("Aucun utilisateur trouvé", exception.getMessage()); // ✅ Modifier le message attendu

    }

    @Test
    void testAddConnectionsEmailNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("jean@example.com")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userConnectionsService.addConnections(1L, "jean@example.com"));
        assertEquals("Aucun utilisateur trouvé avec cet e-mail : jean@example.com", exception.getMessage()); // ✅
                                                                                                             // Modifier
                                                                                                             // le
                                                                                                             // message
                                                                                                             // attendu

    }

    @Test
    void testGetUserConnectionsSuccess() {
        when(userConnectionsRepository.findConnectionsByUserId(1L))
                .thenReturn(Arrays.asList(userConnections));

        List<UserConnectionsResponseDTO> result = userConnectionsService.getUserConnections(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Jean", result.get(0).getUsername());
        assertEquals("jean@example.com", result.get(0).getEmail());
    }

    @Test
    void testGetUserConnectionsEmpty() {
        when(userConnectionsRepository.findConnectionsByUserId(1L))
                .thenReturn(Arrays.asList());

        List<UserConnectionsResponseDTO> result = userConnectionsService.getUserConnections(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
