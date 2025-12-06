package com.sabaiko.bloodbank.repository;

import com.sabaiko.bloodbank.entity.DonationHistory;
import com.sabaiko.bloodbank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonationHistoryRepository extends JpaRepository<DonationHistory, Long> {
    List<DonationHistory> findByDonor(User donor);
    List<DonationHistory> findByReceiver(User receiver);
}


