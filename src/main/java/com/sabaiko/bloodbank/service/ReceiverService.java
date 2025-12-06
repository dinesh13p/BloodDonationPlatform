package com.sabaiko.bloodbank.service;

import com.sabaiko.bloodbank.entity.BloodGroup;
import com.sabaiko.bloodbank.entity.ReceiverDetails;
import com.sabaiko.bloodbank.entity.User;
import com.sabaiko.bloodbank.repository.ReceiverDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ReceiverService {
    
    @Autowired
    private ReceiverDetailsRepository receiverDetailsRepository;
    
    public ReceiverDetails createReceiverDetails(User user, BloodGroup bloodGroupNeeded) {
        ReceiverDetails receiverDetails = new ReceiverDetails();
        receiverDetails.setUser(user);
        // Blood group is optional for receivers
        receiverDetails.setBloodGroupNeeded(bloodGroupNeeded);
        return receiverDetailsRepository.save(receiverDetails);
    }
    
    public Optional<ReceiverDetails> findByUser(User user) {
        return receiverDetailsRepository.findByUser(user);
    }
    
    @Transactional
    public void verifyReceiver(User user) {
        ReceiverDetails receiverDetails = receiverDetailsRepository.findByUser(user)
            .orElseThrow(() -> new RuntimeException("Receiver details not found"));
        receiverDetails.setVerified(true);
        receiverDetailsRepository.save(receiverDetails);
    }
    
    public List<ReceiverDetails> getAllReceivers() {
        return receiverDetailsRepository.findAll();
    }
}


