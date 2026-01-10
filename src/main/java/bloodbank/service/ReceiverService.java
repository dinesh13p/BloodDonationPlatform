package bloodbank.service;

import bloodbank.dto.ReceiverUpdateDTO;
import bloodbank.entity.ReceiverDetails;
import bloodbank.entity.User;
import bloodbank.repository.ReceiverDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ReceiverService {

    @Autowired
    private ReceiverDetailsRepository receiverDetailsRepository;

    @Autowired
    private FileUploadService fileUploadService;

    public ReceiverDetails registerReceiverDetails(User user, String pan, String address, String province,
            String district, String palika, String wardNo, String bio, String profileImage) {
        ReceiverDetails details = new ReceiverDetails();
        details.setUser(user);
        details.setPan(pan);
        details.setAddress(address);
        details.setProvince(province);
        details.setDistrict(district);
        details.setPalika(palika);
        details.setWardNo(wardNo);
        details.setBio(bio);
        details.setProfileImage(profileImage);
        return receiverDetailsRepository.save(details);
    }

    public Optional<ReceiverDetails> findByUser(User user) {
        return receiverDetailsRepository.findByUser(user);
    }

    @Transactional
    public void verifyReceiver(User user) {
        ReceiverDetails details = receiverDetailsRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Receiver details not found"));
        details.setVerified(true);
        receiverDetailsRepository.save(details);
    }

    @Transactional
    public void updateProfileImage(User user, String imagePath) {
        ReceiverDetails details = receiverDetailsRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Receiver details not found"));

        if (details.getProfileImage() != null && !details.getProfileImage().isBlank()) {
            try {
                fileUploadService.deleteFile(details.getProfileImage());
            } catch (Exception e) {
                // Log warning
            }
        }

        details.setProfileImage(imagePath);
        receiverDetailsRepository.save(details);
    }

    public List<ReceiverDetails> getAllReceivers() {
        return receiverDetailsRepository.findAll()
                .stream()
                .filter(r -> r.getUser() != null && r.getUser().getStatus() == bloodbank.entity.UserStatus.APPROVED)
                .toList();
    }

    @Transactional
    public ReceiverDetails updateReceiverDetails(User user, ReceiverUpdateDTO updateDTO) {
        ReceiverDetails receiverDetails = receiverDetailsRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Receiver details not found"));

        if (updateDTO.getAddress() != null) {
            receiverDetails.setAddress(updateDTO.getAddress());
        }
        if (updateDTO.getProvince() != null) {
            receiverDetails.setProvince(updateDTO.getProvince());
        }
        if (updateDTO.getDistrict() != null) {
            receiverDetails.setDistrict(updateDTO.getDistrict());
        }
        if (updateDTO.getPalika() != null) {
            receiverDetails.setPalika(updateDTO.getPalika());
        }
        if (updateDTO.getWardNo() != null) {
            receiverDetails.setWardNo(updateDTO.getWardNo());
        }
        if (updateDTO.getBio() != null) {
            receiverDetails.setBio(updateDTO.getBio());
        }

        return receiverDetailsRepository.save(receiverDetails);
    }

    public List<ReceiverDetails> findByProvince(String province) {
        return receiverDetailsRepository.findByProvince(province);
    }

    public List<ReceiverDetails> findByDistrict(String district) {
        return receiverDetailsRepository.findByDistrict(district);
    }

    public List<ReceiverDetails> findByPalika(String palika) {
        return receiverDetailsRepository.findByPalika(palika);
    }

    public List<ReceiverDetails> findByWardNo(String wardNo) {
        return receiverDetailsRepository.findByWardNo(wardNo);
    }
}
