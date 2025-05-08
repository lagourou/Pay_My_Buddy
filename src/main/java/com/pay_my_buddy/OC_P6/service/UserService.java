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
import com.pay_my_buddy.OC_P6.exception.FriendAlreadyExistsException;
import com.pay_my_buddy.OC_P6.exception.InsufficientBalanceException;
import com.pay_my_buddy.OC_P6.exception.EntityNotFoundException;
import com.pay_my_buddy.OC_P6.exception.SelfTransferredAmountException;
import com.pay_my_buddy.OC_P6.exception.UserNotFoundException;
import com.pay_my_buddy.OC_P6.exception.ContactAlreadyExistException;
import com.pay_my_buddy.OC_P6.model.User;
import com.pay_my_buddy.OC_P6.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getUserById(Long id) throws EntityNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));

        return user;
    }

    public User getUserByEmail(String email) throws UserNotFoundException {
        System.out.println("Recherche utilisateur avec email : " + email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur introuvable"));
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void saveNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void updateUser(RegisterRequestDTO updatedUser, Long id) throws Exception {
        User user = getUserById(id);
        user = mergeUpdateUser(user, updatedUser);
        saveUser(user);

        UserDetailsImplements updatedDetails = new UserDetailsImplements(user);
        UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
                updatedDetails, updatedDetails.getPassword(), updatedDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

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

    private boolean isValidUpdateUser(String newValue, String existingValue) {

        return newValue != null && !newValue.isBlank() && !newValue.equals(existingValue);
    }

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

    private boolean isFriend(User user, User friend) {
        return user.getFriends().contains(friend) || friend.getFriends().contains(user);
    }

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

    @Transactional
    public List<UserConnectionsResponseDTO> getFriends(Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur introuvable"));

        return user.getFriends().stream()
                .map(friend -> new UserConnectionsResponseDTO(friend.getId(), friend.getUsername(), friend.getEmail()))
                .toList();
    }

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

    private boolean isValidBalanceTransactionForSender(User sender, BigDecimal amount) {
        return sender.getAccountBalance().subtract(amount).compareTo(BigDecimal.ZERO) >= 0;
    }

    private boolean isValidReceiver(User sender, User receiver) {
        return !sender.getId().equals(receiver.getId());
    }

    private boolean isPositiveAmount(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }

    @Transactional
    public void addBalance(User user, BigDecimal amount) throws InsufficientBalanceException {
        if (!isPositiveAmount(amount)) {
            throw new InsufficientBalanceException("Le montant doit être positif");
        }
        user.setAccountBalance(user.getAccountBalance().add(amount));
        userRepository.save(user);
    }

}
