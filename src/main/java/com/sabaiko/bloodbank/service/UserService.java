package com.sabaiko.bloodbank.service;

import com.sabaiko.bloodbank.entity.User;
import com.sabaiko.bloodbank.entity.UserRole;
import com.sabaiko.bloodbank.entity.UserStatus;
import com.sabaiko.bloodbank.repository.UserRepository;
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
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        
        if (user.getStatus() == UserStatus.PENDING || user.getStatus() == UserStatus.RESTRICTED) {
            throw new UsernameNotFoundException("User account is not approved");
        }
        
        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getUsername())
            .password(user.getPassword())
            .roles(user.getRole().name())
            .build();
    }
    
    public User createUser(User user) {
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
    public void updateUserStatus(Long userId, UserStatus status) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(status);
        userRepository.save(user);
    }
    
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}

