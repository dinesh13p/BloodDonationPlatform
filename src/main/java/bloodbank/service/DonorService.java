package bloodbank.service;

import bloodbank.dto.DonorUpdateDTO;
import bloodbank.entity.BloodGroup;
import bloodbank.entity.DonorDetails;
import bloodbank.entity.User;
import bloodbank.repository.DonorDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DonorService {
    
    @Autowired
    private DonorDetailsRepository donorDetailsRepository;
    
    public DonorDetails createDonorDetails(User user, BloodGroup bloodGroup, String address, String province,
                                           String district, String palika, String wardNo, String bio) {
        DonorDetails donorDetails = new DonorDetails();
        donorDetails.setUser(user);
        donorDetails.setBloodGroup(bloodGroup);
        donorDetails.setAddress(address);
        donorDetails.setProvince(province);
        donorDetails.setDistrict(district);
        donorDetails.setPalika(palika);
        donorDetails.setWardNo(wardNo);
        donorDetails.setBio(bio);
        return donorDetailsRepository.save(donorDetails);
    }
    
    public Optional<DonorDetails> findByUser(User user) {
        return donorDetailsRepository.findByUser(user);
    }
    
    public List<DonorDetails> findByBloodGroup(BloodGroup bloodGroup) {
        return donorDetailsRepository.findByBloodGroup(bloodGroup);
    }
    
    public List<DonorDetails> findByBloodGroupAndAvailability(BloodGroup bloodGroup, Boolean availability) {
        return donorDetailsRepository.findByBloodGroupAndAvailability(bloodGroup, availability);
    }
    
    public List<DonorDetails> findByProvince(String province) {
        return donorDetailsRepository.findByProvince(province);
    }
    
    public List<DonorDetails> findByDistrict(String district) {
        return donorDetailsRepository.findByDistrict(district);
    }
    
    public List<DonorDetails> findByPalika(String palika) {
        return donorDetailsRepository.findByPalika(palika);
    }
    
    public List<DonorDetails> findByWardNo(String wardNo) {
        return donorDetailsRepository.findByWardNo(wardNo);
    }
    
    @Transactional
    public DonorDetails updateDonorDetails(User user, DonorUpdateDTO updateDTO) {
        DonorDetails donorDetails = donorDetailsRepository.findByUser(user)
            .orElseThrow(() -> new RuntimeException("Donor details not found"));
        
        if (updateDTO.getBloodGroup() != null) {
            donorDetails.setBloodGroup(updateDTO.getBloodGroup());
        }
        if (updateDTO.getAddress() != null) {
            donorDetails.setAddress(updateDTO.getAddress());
        }
        if (updateDTO.getProvince() != null) {
            donorDetails.setProvince(updateDTO.getProvince());
        }
        if (updateDTO.getDistrict() != null) {
            donorDetails.setDistrict(updateDTO.getDistrict());
        }
        if (updateDTO.getPalika() != null) {
            donorDetails.setPalika(updateDTO.getPalika());
        }
        if (updateDTO.getWardNo() != null) {
            donorDetails.setWardNo(updateDTO.getWardNo());
        }
        if (updateDTO.getAvailability() != null) {
            donorDetails.setAvailability(updateDTO.getAvailability());
        }
        if (updateDTO.getMedicalHistory() != null) {
            donorDetails.setMedicalHistory(updateDTO.getMedicalHistory());
        }
        if (updateDTO.getDateOfBirth() != null) {
            donorDetails.setDateOfBirth(updateDTO.getDateOfBirth());
        }
        if (updateDTO.getBio() != null) {
            donorDetails.setBio(updateDTO.getBio());
        }
        
        return donorDetailsRepository.save(donorDetails);
    }
    
    @Transactional
    public void updateProfileImage(User user, String imagePath) {
        DonorDetails donorDetails = donorDetailsRepository.findByUser(user)
            .orElseThrow(() -> new RuntimeException("Donor details not found"));
        donorDetails.setProfileImage(imagePath);
        donorDetailsRepository.save(donorDetails);
    }
    
    @Transactional
    public void addStar(User user) {
        DonorDetails donorDetails = donorDetailsRepository.findByUser(user)
            .orElseThrow(() -> new RuntimeException("Donor details not found"));
        donorDetails.setStars(donorDetails.getStars() + 1);
        donorDetailsRepository.save(donorDetails);
    }
    
    public List<DonorDetails> getAllDonors() {
        return donorDetailsRepository.findAll()
            .stream()
            .filter(d -> d.getUser() != null && d.getUser().getStatus() == bloodbank.entity.UserStatus.APPROVED)
            .toList();
    }
}


