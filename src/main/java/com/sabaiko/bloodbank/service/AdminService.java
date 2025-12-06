package com.sabaiko.bloodbank.service;

import com.sabaiko.bloodbank.entity.DonationHistory;
import com.sabaiko.bloodbank.entity.User;
import com.sabaiko.bloodbank.entity.UserRole;
import com.sabaiko.bloodbank.entity.UserStatus;
import com.sabaiko.bloodbank.repository.DonationHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private DonorService donorService;
    
    @Autowired
    private ReceiverService receiverService;
    
    @Autowired
    private DonationHistoryRepository donationHistoryRepository;
    
    public List<User> getPendingUsers() {
        return userService.findByStatus(UserStatus.PENDING);
    }
    
    public List<User> getPendingDonors() {
        return userService.findByRoleAndStatus(UserRole.DONOR, UserStatus.PENDING);
    }
    
    public List<User> getPendingReceivers() {
        return userService.findByRoleAndStatus(UserRole.RECEIVER, UserStatus.PENDING);
    }
    
    @Transactional
    public void approveUser(Long userId) {
        userService.updateUserStatus(userId, UserStatus.APPROVED);
        User user = userService.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (user.getRole() == UserRole.RECEIVER) {
            receiverService.verifyReceiver(user);
        }
    }
    
    @Transactional
    public void rejectUser(Long userId) {
        userService.deleteUser(userId);
    }
    
    @Transactional
    public void restrictUser(Long userId) {
        userService.updateUserStatus(userId, UserStatus.RESTRICTED);
    }
    
    @Transactional
    public void addStarToDonor(Long donorId) {
        User donor = userService.findById(donorId)
            .orElseThrow(() -> new RuntimeException("Donor not found"));
        donorService.addStar(donor);
    }
    
    public List<User> getAllDonors() {
        return userService.findByRole(UserRole.DONOR);
    }
    
    public List<User> getAllReceivers() {
        return userService.findByRole(UserRole.RECEIVER);
    }
    
    public List<DonationHistory> getAllDonationHistory() {
        return donationHistoryRepository.findAll();
    }
}

