package com.sabaiko.bloodbank.repository;

import com.sabaiko.bloodbank.entity.User;
import com.sabaiko.bloodbank.entity.UserRole;
import com.sabaiko.bloodbank.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByRole(UserRole role);
    List<User> findByStatus(UserStatus status);
    List<User> findByRoleAndStatus(UserRole role, UserStatus status);
}


