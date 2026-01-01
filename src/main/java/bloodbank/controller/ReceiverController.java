package bloodbank.controller;

import bloodbank.dto.ReceiverUpdateDTO;
import bloodbank.entity.BloodGroup;
import bloodbank.entity.DonorDetails;
import bloodbank.entity.ReceiverDetails;
import bloodbank.entity.User;
import bloodbank.service.DonorService;
import bloodbank.service.DonationHistoryService;
import bloodbank.service.ReceiverService;
import bloodbank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/receiver")
@PreAuthorize("hasRole('RECEIVER')")
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

    @GetMapping("/dashboard")
    public String showDashboard() {
        return "receiver/receiver-dashboard";
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
    public String searchDonors(@RequestParam(value = "bloodGroup", required = false) String bloodGroup,
            Model model) {
        List<DonorDetails> donors = new java.util.ArrayList<>();
        String errorMessage = null;
        
        // Get current receiver's details for proximity calculation
        User currentUser = getCurrentUser();
        Optional<ReceiverDetails> receiverDetailsOpt = receiverService.findByUser(currentUser);
        ReceiverDetails receiverDetails = receiverDetailsOpt.orElse(null);
        
        // Blood group is required for search
        if (bloodGroup != null && !bloodGroup.isEmpty()) {
            try {
                BloodGroup bg = BloodGroup.valueOf(bloodGroup);
                // Use proximity-based sorting
                donors = donorService.findDonorsByBloodGroupSortedByProximity(bg, receiverDetails);
            } catch (IllegalArgumentException e) {
                errorMessage = "Invalid blood group selected.";
            }
        }
        
        // Add receiver address info for display
        if (receiverDetails != null) {
            model.addAttribute("receiverAddress", receiverDetails.composeAddress());
        }
        
        model.addAttribute("donors", donors);
        model.addAttribute("bloodGroups", BloodGroup.values());
        model.addAttribute("selectedBloodGroup", bloodGroup);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("hasReceiverAddress", receiverDetails != null && 
            receiverDetails.getProvince() != null && !receiverDetails.getProvince().isBlank());
        return "receiver/search-donor";
    }

    @PostMapping("/verify-donation")
    public String verifyDonation(@RequestParam("donorId") Long donorId) {
        User receiver = getCurrentUser();
        User donor = userService.findById(donorId)
                .orElseThrow(() -> new RuntimeException("Donor not found"));

        donationHistoryService.createDonationHistory(donor, receiver);
        donationHistoryService.verifyByReceiver(
                donationHistoryService.getReceiverHistory(receiver).get(0).getId());

        return "redirect:/receiver/search?verified=true";
    }

    @GetMapping("/edit")
    public String showEditForm(Model model) {
        User user = getCurrentUser();
        Optional<ReceiverDetails> receiverDetails = receiverService.findByUser(user);

        model.addAttribute("user", user);
        model.addAttribute("receiverDetails", receiverDetails.orElse(null));
        model.addAttribute("updateDTO", new ReceiverUpdateDTO());
        return "receiver/edit-receiver";
    }

    @PostMapping("/update")
    public String updateProfile(@ModelAttribute ReceiverUpdateDTO updateDTO,
            @RequestParam(required = false) org.springframework.web.multipart.MultipartFile profileImage,
            @RequestParam(required = false) String province,
            @RequestParam(required = false) String district,
            @RequestParam(required = false) String palika,
            @RequestParam(required = false) String wardNo) {
        User user = getCurrentUser();

        // Handle profile image if needed, for now just update details
        // Note: FileUploadService would be needed here if image upload is supported for
        // Receiver
        updateDTO.setProvince(province);
        updateDTO.setDistrict(district);
        updateDTO.setPalika(palika);
        updateDTO.setWardNo(wardNo);
        updateDTO.setAddress(composeAddress(province, district, palika, wardNo, updateDTO.getAddress()));
        receiverService.updateReceiverDetails(user, updateDTO);
        return "redirect:/receiver/profile";
    }

    @GetMapping("/search-users")
    public String searchUsers(@RequestParam(required = false) String query,
            @RequestParam(required = false) String type,
            Model model) {
        List<User> users = java.util.Collections.emptyList();

        if (query != null && !query.isEmpty()) {
            if ("username".equals(type)) {
                users = userService.searchByUsername(query);
            } else if ("fullname".equals(type)) {
                users = userService.searchByFullName(query);
            }
        }

        model.addAttribute("users", users);
        model.addAttribute("query", query);
        model.addAttribute("type", type);
        return "receiver/search-users";
    }

    @PostMapping("/delete")
    public String deleteAccount() {
        User user = getCurrentUser();
        userService.deleteUser(user.getId());
        SecurityContextHolder.clearContext();
        return "redirect:/auth/login?deleted=true";
    }

    private String composeAddress(String province, String district, String palika, String wardNo, String fallbackAddress) {
        StringBuilder builder = new StringBuilder();
        if (province != null && !province.isBlank()) {
            builder.append(province.trim());
        }
        if (district != null && !district.isBlank()) {
            if (builder.length() > 0) builder.append(", ");
            builder.append(district.trim());
        }
        if (palika != null && !palika.isBlank()) {
            if (builder.length() > 0) builder.append(", ");
            builder.append(palika.trim());
        }
        if (wardNo != null && !wardNo.isBlank()) {
            if (builder.length() > 0) builder.append(" - ");
            builder.append("Ward ").append(wardNo.trim());
        }
        if (builder.length() == 0 && fallbackAddress != null && !fallbackAddress.isBlank()) {
            builder.append(fallbackAddress.trim());
        }
        return builder.length() == 0 ? "Not Provided" : builder.toString();
    }
}
