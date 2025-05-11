package com.pay_my_buddy.OC_P6.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pay_my_buddy.OC_P6.configuration.UserDetailsImplements;
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

import lombok.RequiredArgsConstructor;

/**
 * Service pour la gestion des utilisateurs et de leurs connexions.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Récupère un utilisateur par son ID.
     *
     * @param id l'ID de l'utilisateur
     * @return l'utilisateur correspondant
     * @throws EntityNotFoundException si l'utilisateur n'existe pas
     */
    public User getUserById(Long id) throws EntityNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));

        return user;
    }

    /**
     * Récupère un utilisateur par son email.
     *
     * @param email l'email de l'utilisateur
     * @return l'utilisateur correspondant
     * @throws UserNotFoundException si l'utilisateur n'existe pas
     */
    public User getUserByEmail(String email) throws UserNotFoundException {
        System.out.println("Recherche utilisateur avec email : " + email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur introuvable"));
    }

    /**
     * Sauvegarde un utilisateur existant.
     *
     * @param user l'utilisateur à sauvegarder
     */
    public void saveUser(User user) {
        userRepository.save(user);
    }

    /**
     * Sauvegarde un nouvel utilisateur en encodant son mot de passe.
     *
     * @param user le nouvel utilisateur à sauvegarder
     */
    public void saveNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    /**
     * Met à jour les informations d'un utilisateur.
     *
     * @param updatedUser les nouvelles informations de l'utilisateur
     * @param id          l'ID de l'utilisateur
     * @throws Exception si la mise à jour échoue
     */
    public void updateUser(RegisterRequestDTO updatedUser, Long id) throws Exception {
        User user = getUserById(id);
        user = mergeUpdateUser(user, updatedUser);
        saveUser(user);

        UserDetailsImplements updatedDetails = new UserDetailsImplements(user);
        UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
                updatedDetails, updatedDetails.getPassword(), updatedDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    /**
     * Met à jour les informations d'un utilisateur avec les données fournies.
     * Seuls les champs non nuls et modifiés seront mis à jour.
     *
     * @param existingUser l'utilisateur existant
     * @param updatedDTO   les nouvelles informations de l'utilisateur
     * @return l'utilisateur mis à jour
     */
    private User mergeUpdateUser(User existingUser, RegisterRequestDTO updatedDTO) {

        Optional.ofNullable(updatedDTO.getUsername())
                .filter(username -> isValidUpdateUser(username, existingUser.getUsername()))
                .ifPresent(existingUser::setUsername);

        Optional.ofNullable(updatedDTO.getEmail())
                .filter(email -> isValidUpdateUser(email, existingUser.getEmail()))
                .ifPresent(existingUser::setEmail);

        Optional.ofNullable(updatedDTO.getPassword())
                .filter(password -> !password.isBlank())
                .map(passwordEncoder::encode)
                .ifPresent(existingUser::setPassword);

        return existingUser;
    }

    /**
     * Ajoute un ami à la liste de connexions de l'utilisateur.
     *
     * @param userId       l'ID de l'utilisateur
     * @param connectionId l'ID de la connexion
     * @throws FriendAlreadyExistsException si la connexion existe déjà
     */
    @Transactional
    public void addFriend(Long userId, Long connectionId) throws FriendAlreadyExistsException {
        User user = getUserById(userId);
        User friend = getUserById(connectionId);

        if (!isFriend(user, friend)) {
            user.getFriends().add(friend);
            friend.getFriends().add(user);
            userRepository.save(friend);
            userRepository.save(user);
        } else {
            throw new FriendAlreadyExistsException("L'ami est déjà dans la liste.");
        }
    }

    /**
     * Supprime un ami de la liste de connexions de l'utilisateur.
     *
     * @param userId       l'ID de l'utilisateur
     * @param connectionId l'ID de la connexion
     * @throws ContactAlreadyExistException si la connexion n'existe pas
     */
    @Transactional
    public void deleteFriend(Long userId, Long connectionId) throws ContactAlreadyExistException {
        User user = getUserById(userId);
        User friend = getUserById(connectionId);

        if (isFriend(user, friend)) {
            user.getFriends().remove(friend);
            friend.getFriends().remove(user);
            userRepository.save(friend);
            userRepository.save(user);
        } else {
            throw new ContactAlreadyExistException("La relation n'existe pas");
        }
    }

    /**
     * Récupère la liste des amis d'un utilisateur.
     *
     * @param userId l'ID de l'utilisateur
     * @return une liste des connexions de l'utilisateur sous forme de DTO
     * @throws UserNotFoundException si l'utilisateur n'existe pas
     */
    @Transactional
    public List<UserConnectionsResponseDTO> getFriends(Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur introuvable"));

        return user.getFriends().stream()
                .map(friend -> new UserConnectionsResponseDTO(friend.getId(), friend.getUsername(), friend.getEmail()))
                .toList();
    }

    /**
     * Met à jour le solde du compte utilisateur après une transaction.
     *
     * @param sender   l'expéditeur
     * @param receiver le destinataire
     * @param amount   le montant de la transaction
     * @throws InsufficientBalanceException   si le solde est insuffisant
     * @throws SelfTransferredAmountException si l'expéditeur et le destinataire
     *                                        sont identiques
     */
    @Transactional
    public void updateBalance(User sender, User receiver, BigDecimal amount)
            throws InsufficientBalanceException, SelfTransferredAmountException {
        if (!isValidBalanceTransactionForSender(sender, amount)) {
            throw new InsufficientBalanceException("Solde insuffisant");
        }
        if (!isValidReceiver(sender, receiver)) {
            throw new SelfTransferredAmountException("Expéditeur et destinataire identiques");
        }
        if (!isPositiveAmount(amount)) {
            throw new InsufficientBalanceException("Le Montant doit être positive");
        }
        sender.setAccountBalance(sender.getAccountBalance().subtract(amount));
        receiver.setAccountBalance(receiver.getAccountBalance().add(amount));
        userRepository.save(sender);
        userRepository.save(receiver);
    }

    /**
     * Ajoute un montant au solde de l'utilisateur.
     *
     * @param user   l'utilisateur concerné
     * @param amount le montant à ajouter
     * @throws InsufficientBalanceException si le montant est invalide
     */
    @Transactional
    public void addBalance(User user, BigDecimal amount) throws InsufficientBalanceException {
        if (!isPositiveAmount(amount)) {
            throw new InsufficientBalanceException("Le montant doit être positif");
        }
        user.setAccountBalance(user.getAccountBalance().add(amount));
        userRepository.save(user);
    }

    // ------ Méthodes de validation : type booléen------
    private boolean isValidUpdateUser(String newValue, String existingValue) {

        return newValue != null && !newValue.isBlank() && !newValue.equals(existingValue);
    }

    private boolean isFriend(User user, User friend) {
        return user.getFriends().contains(friend) || friend.getFriends().contains(user);
    }

    private boolean isValidBalanceTransactionForSender(User sender, BigDecimal amount) {
        return sender.getAccountBalance().subtract(amount).compareTo(BigDecimal.ZERO) >= 0;
    }

    private boolean isValidReceiver(User sender, User receiver) {
        return !sender.getId().equals(receiver.getId());
    }

    private boolean isPositiveAmount(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }

}
