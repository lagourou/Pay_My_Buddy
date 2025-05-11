package com.pay_my_buddy.OC_P6.unitaire.service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.pay_my_buddy.OC_P6.dto.RegisterRequestDTO;
import com.pay_my_buddy.OC_P6.dto.UserConnectionsResponseDTO;
import com.pay_my_buddy.OC_P6.exception.ContactAlreadyExistException;
import com.pay_my_buddy.OC_P6.exception.EntityNotFoundException;
import com.pay_my_buddy.OC_P6.exception.FriendAlreadyExistsException;
import com.pay_my_buddy.OC_P6.exception.InsufficientBalanceException;
import com.pay_my_buddy.OC_P6.exception.SelfTransferredAmountException;
import com.pay_my_buddy.OC_P6.exception.UserNotFoundException;
import com.pay_my_buddy.OC_P6.model.User;
import com.pay_my_buddy.OC_P6.repository.UserRepository;
import com.pay_my_buddy.OC_P6.service.UserService;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private User friend;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("Laurent");
        user.setEmail("laurent@example.com");
        user.setAccountBalance(BigDecimal.valueOf(100));
        user.setFriends(new HashSet<>());

        friend = new User();
        friend.setId(2L);
        friend.setUsername("Jean");
        friend.setEmail("jean@example.com");
        friend.setAccountBalance(BigDecimal.valueOf(50));
        friend.setFriends(new HashSet<>());
    }

    @Test
    void testGetUserByIdSuccess() throws EntityNotFoundException {

        // Vérifie que l'utilisateur est correctement récupéré par son ID
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(1L);

        assertNotNull(foundUser);
        assertEquals(1L, foundUser.getId());
    }

    @Test
    void testGetUserByIdNotFound() {

        // Vérifie que l'exception est levée si l'utilisateur est introuvable
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> userService.getUserById(1L));
        assertNotNull(exception);
    }

    @Test
    void testSaveNewUser() {

        // Simule l'encodage du mot de passe avant sauvegarde
        user.setPassword("plainPassword");
        when(passwordEncoder.encode("plainPassword")).thenReturn("hashedPassword");

        userService.saveNewUser(user);

        assertEquals("hashedPassword", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testAddFriendSuccess() throws FriendAlreadyExistsException {

        // Vérifie qu'un utilisateur peut ajouter un ami
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(friend));

        userService.addFriend(1L, 2L);

        assertTrue(user.getFriends().contains(friend));
        verify(userRepository, times(1)).save(user);
        verify(userRepository, times(1)).save(friend);
    }

    @Test
    void testAddFriendAlreadyExists() {

        // Vérifie qu'on ne peut pas ajouter un ami déjà existant
        user.getFriends().add(friend);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(friend));

        FriendAlreadyExistsException exception = assertThrows(FriendAlreadyExistsException.class,
                () -> userService.addFriend(1L, 2L));
        assertNotNull(exception.getMessage());
    }

    @Test
    void testUpdateBalanceSuccess() throws InsufficientBalanceException, SelfTransferredAmountException {

        // Vérifie qu'une mise à jour du solde entre utilisateurs fonctionne
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(friend));

        userService.updateBalance(user, friend, BigDecimal.valueOf(50));

        assertEquals(BigDecimal.valueOf(50), user.getAccountBalance());
        assertEquals(BigDecimal.valueOf(100), friend.getAccountBalance());
        verify(userRepository, times(1)).save(user);
        verify(userRepository, times(1)).save(friend);
    }

    @Test
    void testUpdateBalanceInsufficientFunds() {

        // Vérifie qu'une transaction échoue si le solde est insuffisant
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(friend));

        InsufficientBalanceException exception = assertThrows(InsufficientBalanceException.class,
                () -> userService.updateBalance(user, friend, BigDecimal.valueOf(200)));
        assertNotNull(exception.getMessage());

    }

    @Test
    void testUpdateBalanceToSelf() {

        // Vérifie qu'un utilisateur ne peut pas transférer de l'argent à lui-même
        SelfTransferredAmountException exception = assertThrows(SelfTransferredAmountException.class,
                () -> userService.updateBalance(user, user, BigDecimal.valueOf(20)));
        assertNotNull(exception.getMessage());
    }

    @Test
    void testAddBalanceSuccess() throws InsufficientBalanceException {

        // Vérifie qu'un utilisateur peut ajouter des fonds à son compte
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.addBalance(user, BigDecimal.valueOf(50));

        assertEquals(BigDecimal.valueOf(150), user.getAccountBalance());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testAddBalanceNegativeAmount() {

        // Vérifie qu'un montant négatif ne peut pas être ajouté au compte
        InsufficientBalanceException exception = assertThrows(InsufficientBalanceException.class,
                () -> userService.addBalance(user, BigDecimal.valueOf(-10)));
        assertNotNull(exception.getMessage());
    }

    @Test
    void testGetUserByEmailSuccess() throws EntityNotFoundException {

        // Vérifie que l'utilisateur est correctement récupéré par son email
        when(userRepository.findByEmail("laurent@example.com")).thenReturn(Optional.of(user));

        User foundUser = userService.getUserByEmail("laurent@example.com");

        assertNotNull(foundUser);
        assertEquals("laurent@example.com", foundUser.getEmail());
    }

    @Test
    void testGetUserByEmailNotFound() {

        // Vérifie qu'une exception est levée si l'utilisateur n'est pas trouvé
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.getUserByEmail("unknown@example.com"));

        assertEquals("Utilisateur introuvable", exception.getMessage());
    }

    @Test
    void testSaveUser() {

        // Vérifie que l'utilisateur est correctement sauvegardé
        userService.saveUser(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUserSuccess() throws Exception {

        // Vérifie qu'un utilisateur peut être mis à jour correctement
        RegisterRequestDTO updatedUser = new RegisterRequestDTO();
        updatedUser.setUsername("LaurentUpdated");
        updatedUser.setEmail("laurent.updated@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.updateUser(updatedUser, 1L);

        assertEquals("LaurentUpdated", user.getUsername());
        assertEquals("laurent.updated@example.com", user.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUserNotFound() {

        // Vérifie qu'un utilisateur peut être mis à jour correctement
        RegisterRequestDTO updatedUser = new RegisterRequestDTO();
        updatedUser.setUsername("LaurentUpdated");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> userService.updateUser(updatedUser, 1L));

        assertEquals("Utilisateur introuvable", exception.getMessage());
    }

    @Test
    void testDeleteFriendNotExisting() {

        // Vérifie qu'une exception est levée si l'ami à supprimer n'existe pas
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(friend));

        ContactAlreadyExistException exception = assertThrows(ContactAlreadyExistException.class,
                () -> userService.deleteFriend(1L, 2L));

        assertEquals("La relation n'existe pas", exception.getMessage());
    }

    @Test
    void testGetFriendsUserNotFound() {

        // Vérifie que l'exception est levée si l'utilisateur est introuvable lors de la
        // récupération des amis
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.getFriends(1L));

        assertEquals("Utilisateur introuvable", exception.getMessage());
    }

    @Test
    void testUpdateBalanceNegativeAmount() {

        // Vérifie qu'un montant négatif ne peut pas être transféré
        InsufficientBalanceException exception = assertThrows(InsufficientBalanceException.class,
                () -> userService.updateBalance(user, friend, BigDecimal.valueOf(-10)));

        assertEquals("Le Montant doit être positive", exception.getMessage());
    }

    @Test
    void testDeleteFriendSuccess() throws ContactAlreadyExistException {

        // Vérifie qu'une suppression d'ami fonctionne correctement
        user.getFriends().add(friend);
        friend.getFriends().add(user);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(friend));

        userService.deleteFriend(1L, 2L);

        assertFalse(user.getFriends().contains(friend));
        assertFalse(friend.getFriends().contains(user));
        verify(userRepository, times(1)).save(user);
        verify(userRepository, times(1)).save(friend);
    }

    @Test
    void testGetFriendsSuccess() throws UserNotFoundException {

        // Vérifie que la liste des amis de l'utilisateur est correctement récupérée
        user.getFriends().add(friend);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        List<UserConnectionsResponseDTO> friends = userService.getFriends(1L);

        assertEquals(1, friends.size());
        assertEquals(friend.getId(), friends.get(0).getId());
        assertEquals(friend.getUsername(), friends.get(0).getUsername());
        assertEquals(friend.getEmail(), friends.get(0).getEmail());
    }

}
