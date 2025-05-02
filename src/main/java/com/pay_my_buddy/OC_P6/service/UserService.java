package com.pay_my_buddy.OC_P6.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pay_my_buddy.OC_P6.dto.RegisterRequestDTO;
import com.pay_my_buddy.OC_P6.dto.UserConnectionsResponseDTO;
import com.pay_my_buddy.OC_P6.model.User;
import com.pay_my_buddy.OC_P6.model.UserConnections;
import com.pay_my_buddy.OC_P6.repository.UserConnectionsRepository;
import com.pay_my_buddy.OC_P6.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserConnectionsRepository userConnectionsRepository;
    private final PasswordEncoder passwordEncoder;

    public User getUserById(Long id) throws Exception {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception("Utilisateur introuvable"));

        return user;

    }

    public User getUserByEmail(String email) throws Exception {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("Utilisateur introuvable"));
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void saveNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void updateUser(RegisterRequestDTO updatedUser, Long id) throws Exception {
        User user = getUserByEmail(updatedUser.getEmail());
        user = mergeUpdateUser(user, updatedUser);
        saveUser(user);
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
    public void addFriend(Long userId, Long connectionId) throws Exception {
        User user = getUserById(userId);
        User friend = getUserById(connectionId);

        if(!isFriend(user, friend)) {
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

    private static class FriendAlreadyExistsException extends Exception {
        public FriendAlreadyExistsException(String message) {
            super(message);
        }
    }

    @Transactional
    public void deleteFriend(Long userId, Long connectionId) throws Exception {
        User user = getUserById(userId);
        User friend = getUserById(connectionId);

        if(isFriend(user, friend)) {
            user.getFriends().remove(friend);
            friend.getFriends().remove(user);
            userRepository.save(friend);
            userRepository.save(user);
        } else {
            throw new ContactAlreadyExistException("La relation n'existe pas");
        }
    }
    private static class ContactAlreadyExistException extends Exception {
        public ContactAlreadyExistException(String message) {
            super(message);
        }
    }

    @Transactional
    public List<UserConnectionsResponseDTO> getFriends(Long userId) {
        UserConnections userConnections = userConnectionsRepository.findConnectionsByUserId(userId)
    .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

    User user = userConnections.getConnection();
    return user.getFriends().stream().map(friend -> new UserConnectionsResponseDTO(friend.getId(), friend.getUsername(), friend.getEmail()))
    .toList();
    }

    @Transactional
    public void updateBalance(User sender, User receiver, BigDecimal amount) throws InsufficientBalanceException, SelfTransferredAmountException {
        if(!isValidBalanceTransactionForSender(sender, amount)) {
            throw new InsufficientBalanceException("Solde insuffisant");
        }
        if(!isValidReceiver(sender, receiver)) {
            throw new SelfTransferredAmountException("Expéditeur et destinataire identiques");
        }
        if(!isPositiveAmount(amount)) {
            throw new InsufficientBalanceException("Le Montant doit être positive");
        }
        sender.setAccountBalance(sender.getAccountBalance().subtract(amount));
        receiver.setAccountBalance(receiver.getAccountBalance().add(amount));
        userRepository.save(sender);
        userRepository.save(receiver);
    }

    private boolean isValidBalanceTransactionForSender(User sender, BigDecimal amount) {
        return sender.getAccountBalance().subtract(amount).compareTo(BigDecimal.ZERO) > 0;
    }
    private static class InsufficientBalanceException extends Exception {
        public InsufficientBalanceException(String message) {
            super(message);
        }
    }
    private boolean isValidReceiver(User sender, User receiver) {
        return !sender.getId().equals(receiver.getId());
    }
    private static class SelfTransferredAmountException extends Exception {
        public SelfTransferredAmountException(String message) {
            super(message);
        }
    }
    
    private boolean isPositiveAmount(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }
    @Transactional
    public void addBalance(User user, BigDecimal amount) throws InsufficientBalanceException {
        if (!isPositiveAmount(amount)) {
            throw new InsufficientBalanceException("Amount must be positive");
        }
        user.setAccountBalance(user.getAccountBalance().add(amount));
        userRepository.save(user);
    }
    


}
