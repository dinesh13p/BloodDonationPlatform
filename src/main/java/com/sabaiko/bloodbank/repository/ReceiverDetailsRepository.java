package com.sabaiko.bloodbank.repository;

import com.sabaiko.bloodbank.entity.ReceiverDetails;
import com.sabaiko.bloodbank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReceiverDetailsRepository extends JpaRepository<ReceiverDetails, Long> {
    Optional<ReceiverDetails> findByUser(User user);
}


