package bloodbank.service;

import bloodbank.entity.User;
import bloodbank.entity.UserRole;
import bloodbank.entity.UserStatus;
import bloodbank.repository.DonorDetailsRepository;
import bloodbank.repository.ReceiverDetailsRepository;
import bloodbank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DonorDetailsRepository donorDetailsRepository;

    @Autowired
    private ReceiverDetailsRepository receiverDetailsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        if (user.getStatus() == UserStatus.PENDING || user.getStatus() == UserStatus.RESTRICTED) {
            throw new UsernameNotFoundException("Account not active: " + user.getStatus());
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    public List<User> findByStatus(UserStatus status) {
        return userRepository.findByStatus(status);
    }

    public List<User> findByRoleAndStatus(UserRole role, UserStatus status) {
        return userRepository.findByRoleAndStatus(role, status);
    }

    @Transactional
    public void changeUserStatus(Long userId, UserStatus status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(status);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresent(user -> {
            if (user.getRole() == UserRole.DONOR) {
                donorDetailsRepository.findByUser(user).ifPresent(donorDetailsRepository::delete);
            } else if (user.getRole() == UserRole.RECEIVER) {
                receiverDetailsRepository.findByUser(user).ifPresent(receiverDetailsRepository::delete);
            }
            userRepository.delete(user);
        });
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> searchByUsername(String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username);
    }

    public List<User> searchByFullName(String fullName) {
        return userRepository.findByFullNameContainingIgnoreCase(fullName);
    }

    /**
     * Find verified receivers, optionally filtered by phone number.
     * If phone is provided, returns exact match (0 or 1 result).
     * If phone is not provided or empty, returns all verified receivers.
     */
    public List<User> findVerifiedReceivers(Optional<String> phone) {
        if (phone.isPresent() && !phone.get().isBlank()) {
            return userRepository.findByPhoneAndRoleAndStatus(
                    phone.get().trim(),
                    UserRole.RECEIVER,
                    UserStatus.APPROVED).map(List::of).orElse(List.of());
        } else {
            return userRepository.findByRoleAndStatus(UserRole.RECEIVER, UserStatus.APPROVED);
        }
    }
}
