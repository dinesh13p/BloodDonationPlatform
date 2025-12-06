package com.sabaiko.bloodbank.repository;

import com.sabaiko.bloodbank.entity.BloodGroup;
import com.sabaiko.bloodbank.entity.DonorDetails;
import com.sabaiko.bloodbank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonorDetailsRepository extends JpaRepository<DonorDetails, Long> {
    Optional<DonorDetails> findByUser(User user);
    List<DonorDetails> findByBloodGroup(BloodGroup bloodGroup);
    List<DonorDetails> findByBloodGroupAndAvailability(BloodGroup bloodGroup, Boolean availability);
    List<DonorDetails> findByCity(String city);
    List<DonorDetails> findByState(String state);
}


