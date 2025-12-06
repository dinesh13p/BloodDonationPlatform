package com.sabaiko.bloodbank.controller;

import com.sabaiko.bloodbank.dto.DonorUpdateDTO;
import com.sabaiko.bloodbank.entity.BloodGroup;
import com.sabaiko.bloodbank.entity.DonorDetails;
import com.sabaiko.bloodbank.entity.User;
import com.sabaiko.bloodbank.service.DonorService;
import com.sabaiko.bloodbank.service.FileUploadService;
import com.sabaiko.bloodbank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Controller
@RequestMapping("/donor")
public class DonorController {
    
    @Autowired
    private DonorService donorService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private FileUploadService fileUploadService;
    
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userService.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    @GetMapping("/profile")
    public String showProfile(Model model) {
        User user = getCurrentUser();
        Optional<DonorDetails> donorDetails = donorService.findByUser(user);
        
        model.addAttribute("user", user);
        model.addAttribute("donorDetails", donorDetails.orElse(null));
        return "donor/donor-profile";
    }
    
    @GetMapping("/edit")
    public String showEditForm(Model model) {
        User user = getCurrentUser();
        Optional<DonorDetails> donorDetails = donorService.findByUser(user);
        
        model.addAttribute("user", user);
        model.addAttribute("donorDetails", donorDetails.orElse(null));
        model.addAttribute("bloodGroups", BloodGroup.values());
        model.addAttribute("updateDTO", new DonorUpdateDTO());
        return "donor/edit-donor";
    }
    
    @PostMapping("/update")
    public String updateProfile(@RequestParam(required = false) String address,
                               @RequestParam(required = false) String city,
                               @RequestParam(required = false) String state,
                               @RequestParam(required = false) String zipCode,
                               @RequestParam(required = false) Boolean availability,
                               @RequestParam(required = false) String medicalHistory,
                               @RequestParam(required = false) String dateOfBirth,
                               @RequestParam(required = false) String bloodGroup,
                               @RequestParam(required = false) MultipartFile profileImage) {
        User user = getCurrentUser();
        
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                String imagePath = fileUploadService.uploadFile(profileImage, "profiles");
                donorService.updateProfileImage(user, imagePath);
            } catch (Exception e) {
                // Handle error
            }
        }
        
        DonorUpdateDTO updateDTO = new DonorUpdateDTO();
        updateDTO.setAddress(address);
        updateDTO.setCity(city);
        updateDTO.setState(state);
        updateDTO.setZipCode(zipCode);
        updateDTO.setAvailability(availability);
        updateDTO.setMedicalHistory(medicalHistory);
        if (dateOfBirth != null && !dateOfBirth.isEmpty()) {
            updateDTO.setDateOfBirth(java.time.LocalDate.parse(dateOfBirth));
        }
        if (bloodGroup != null && !bloodGroup.isEmpty()) {
            try {
                updateDTO.setBloodGroup(BloodGroup.valueOf(bloodGroup));
            } catch (IllegalArgumentException e) {
                // Invalid blood group, ignore
            }
        }
        
        donorService.updateDonorDetails(user, updateDTO);
        return "redirect:/donor/profile";
    }
    
    @PostMapping("/delete")
    public String deleteAccount() {
        User user = getCurrentUser();
        userService.deleteUser(user.getId());
        SecurityContextHolder.clearContext();
        return "redirect:/auth/login?deleted=true";
    }
}

