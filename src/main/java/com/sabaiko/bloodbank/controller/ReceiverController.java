package com.sabaiko.bloodbank.controller;

import com.sabaiko.bloodbank.entity.BloodGroup;
import com.sabaiko.bloodbank.entity.DonorDetails;
import com.sabaiko.bloodbank.entity.ReceiverDetails;
import com.sabaiko.bloodbank.entity.User;
import com.sabaiko.bloodbank.service.DonorService;
import com.sabaiko.bloodbank.service.DonationHistoryService;
import com.sabaiko.bloodbank.service.ReceiverService;
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

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/receiver")
public class ReceiverController {
    
    @Autowired
    private ReceiverService receiverService;
    
    @Autowired
    private DonorService donorService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private DonationHistoryService donationHistoryService;
    
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userService.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    @GetMapping("/profile")
    public String showProfile(Model model) {
        User user = getCurrentUser();
        Optional<ReceiverDetails> receiverDetails = receiverService.findByUser(user);
        
        model.addAttribute("user", user);
        model.addAttribute("receiverDetails", receiverDetails.orElse(null));
        return "receiver/receiver-profile";
    }
    
    @GetMapping("/search")
    public String searchDonors(@RequestParam(required = false) String bloodGroup,
                               @RequestParam(required = false) String city,
                               @RequestParam(required = false) String state,
                               Model model) {
        List<DonorDetails> donors;
        
        if (bloodGroup != null && !bloodGroup.isEmpty()) {
            try {
                BloodGroup bg = BloodGroup.valueOf(bloodGroup);
                if (city != null && !city.isEmpty()) {
                    donors = donorService.findByCity(city).stream()
                        .filter(d -> d.getBloodGroup() == bg && d.getAvailability())
                        .toList();
                } else if (state != null && !state.isEmpty()) {
                    donors = donorService.findByState(state).stream()
                        .filter(d -> d.getBloodGroup() == bg && d.getAvailability())
                        .toList();
                } else {
                    donors = donorService.findByBloodGroupAndAvailability(bg, true);
                }
            } catch (IllegalArgumentException e) {
                donors = donorService.getAllDonors().stream()
                    .filter(d -> d.getAvailability())
                    .toList();
            }
        } else {
            donors = donorService.getAllDonors().stream()
                .filter(d -> d.getAvailability())
                .toList();
        }
        
        model.addAttribute("donors", donors);
        model.addAttribute("bloodGroups", BloodGroup.values());
        model.addAttribute("selectedBloodGroup", bloodGroup);
        model.addAttribute("selectedCity", city);
        model.addAttribute("selectedState", state);
        return "receiver/search-donor";
    }
    
    @PostMapping("/verify-donation")
    public String verifyDonation(@RequestParam Long donorId) {
        User receiver = getCurrentUser();
        User donor = userService.findById(donorId)
            .orElseThrow(() -> new RuntimeException("Donor not found"));
        
        donationHistoryService.createDonationHistory(donor, receiver);
        donationHistoryService.verifyByReceiver(
            donationHistoryService.getReceiverHistory(receiver).get(0).getId()
        );
        
        return "redirect:/receiver/search?verified=true";
    }
    
    @PostMapping("/delete")
    public String deleteAccount() {
        User user = getCurrentUser();
        userService.deleteUser(user.getId());
        SecurityContextHolder.clearContext();
        return "redirect:/auth/login?deleted=true";
    }
}

