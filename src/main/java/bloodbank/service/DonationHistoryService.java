package bloodbank.service;

import bloodbank.entity.DonationHistory;
import bloodbank.entity.User;
import bloodbank.repository.DonationHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DonationHistoryService {
    
    @Autowired
    private DonationHistoryRepository donationHistoryRepository;
    
    @Autowired
    private DonorService donorService;
    
    public DonationHistory createDonationHistory(User donor, User receiver) {
        DonationHistory history = new DonationHistory();
        history.setDonor(donor);
        history.setReceiver(receiver);
        history.setDonationDate(LocalDateTime.now());
        return donationHistoryRepository.save(history);
    }
    
    @Transactional
    public void verifyByReceiver(Long donationId) {
        DonationHistory history = donationHistoryRepository.findById(donationId)
            .orElseThrow(() -> new RuntimeException("Donation history not found"));
        history.setVerifiedByReceiver(true);
        donationHistoryRepository.save(history);
    }
    
    @Transactional
    public void verifyByAdmin(Long donationId) {
        DonationHistory history = donationHistoryRepository.findById(donationId)
            .orElseThrow(() -> new RuntimeException("Donation history not found"));
        history.setVerifiedByAdmin(true);
        donationHistoryRepository.save(history);
        
        // Add star to donor
        donorService.addStar(history.getDonor());
    }
    
    public List<DonationHistory> getDonorHistory(User donor) {
        return donationHistoryRepository.findByDonor(donor);
    }
    
    public List<DonationHistory> getReceiverHistory(User receiver) {
        return donationHistoryRepository.findByReceiver(receiver);
    }
}


