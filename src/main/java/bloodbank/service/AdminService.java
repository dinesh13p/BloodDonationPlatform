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
        userService.changeUserStatus(userId, UserStatus.APPROVED);
        userService.findById(userId)
                .filter(u -> u.getRole() == UserRole.RECEIVER)
                .ifPresent(receiverService::verifyReceiver);
    }

    @Transactional
    public void rejectUser(Long userId) {
        userService.deleteUser(userId);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        donationHistoryRepository.deleteByDonor(user);
        donationHistoryRepository.deleteByReceiver(user);

        userService.deleteUser(userId);
    }

    @Transactional
    public void restrictUser(Long userId) {
        userService.changeUserStatus(userId, UserStatus.RESTRICTED);
    }

    @Transactional
    public void unrestrictUser(Long userId) {
        userService.changeUserStatus(userId, UserStatus.APPROVED);
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

    public User getPendingUser(Long userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getStatus() != UserStatus.PENDING) {
            throw new RuntimeException("User is not in PENDING status");
        }
        return user;
    }

    public User getUser(Long userId) {
        return userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
