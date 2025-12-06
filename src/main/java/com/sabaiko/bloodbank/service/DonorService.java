package com.sabaiko.bloodbank.service;

import com.sabaiko.bloodbank.dto.DonorUpdateDTO;
import com.sabaiko.bloodbank.entity.BloodGroup;
import com.sabaiko.bloodbank.entity.DonorDetails;
import com.sabaiko.bloodbank.entity.User;
import com.sabaiko.bloodbank.repository.DonorDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DonorService {
    
    @Autowired
    private DonorDetailsRepository donorDetailsRepository;
    
    public DonorDetails createDonorDetails(User user, BloodGroup bloodGroup) {
        DonorDetails donorDetails = new DonorDetails();
        donorDetails.setUser(user);
        donorDetails.setBloodGroup(bloodGroup);
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
    
    public List<DonorDetails> findByCity(String city) {
        return donorDetailsRepository.findByCity(city);
    }
    
    public List<DonorDetails> findByState(String state) {
        return donorDetailsRepository.findByState(state);
    }
    
    @Transactional
    public DonorDetails updateDonorDetails(User user, DonorUpdateDTO updateDTO) {
        DonorDetails donorDetails = donorDetailsRepository.findByUser(user)
            .orElseThrow(() -> new RuntimeException("Donor details not found"));
        
        if (updateDTO.getAddress() != null) {
            donorDetails.setAddress(updateDTO.getAddress());
        }
        if (updateDTO.getCity() != null) {
            donorDetails.setCity(updateDTO.getCity());
        }
        if (updateDTO.getState() != null) {
            donorDetails.setState(updateDTO.getState());
        }
        if (updateDTO.getZipCode() != null) {
            donorDetails.setZipCode(updateDTO.getZipCode());
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
        if (updateDTO.getBloodGroup() != null) {
            donorDetails.setBloodGroup(updateDTO.getBloodGroup());
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
        return donorDetailsRepository.findAll();
    }
}


