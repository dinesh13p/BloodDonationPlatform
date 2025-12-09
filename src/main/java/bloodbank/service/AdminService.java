package bloodbank.service;

import bloodbank.entity.DonationHistory;
import bloodbank.entity.User;
import bloodbank.entity.UserRole;
import bloodbank.entity.UserStatus;
import bloodbank.repository.DonationHistoryRepository;
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
    
    public List<User> getRestrictedUsers() {
        return userService.findByStatus(UserStatus.RESTRICTED);
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
    public void deleteUser(Long userId) {
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
        return userService.findByRoleAndStatus(UserRole.DONOR, UserStatus.APPROVED);
    }
    
    public List<User> getAllReceivers() {
        return userService.findByRoleAndStatus(UserRole.RECEIVER, UserStatus.APPROVED);
    }
    
    public List<DonationHistory> getAllDonationHistory() {
        return donationHistoryRepository.findAll();
    }
}

