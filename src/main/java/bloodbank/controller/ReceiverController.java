package bloodbank.controller;

import bloodbank.dto.ReceiverUpdateDTO;
import bloodbank.entity.BloodGroup;
import bloodbank.entity.DonorDetails;
import bloodbank.entity.ReceiverDetails;
import bloodbank.entity.User;
import bloodbank.service.DonorService;
import bloodbank.service.DonationHistoryService;
import bloodbank.service.ReceiverService;
import bloodbank.service.FileUploadService;
import bloodbank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Collections;
import java.util.ArrayList;
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

    @Autowired
    private FileUploadService fileUploadService;

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @GetMapping("/dashboard")
    public String receiverDashboard() {
        return "receiver/receiver-dashboard";
    }

    @GetMapping("/profile")
    public String profilePage(Model model) {
        User user = getCurrentUser();
        Optional<ReceiverDetails> details = receiverService.findByUser(user);

        model.addAttribute("user", user);
        model.addAttribute("receiverDetails", details.orElse(null));
        return "receiver/receiver-profile";
    }

    @GetMapping("/search")
    public String searchDonors(@RequestParam(value = "bloodGroup", required = false) String bloodGroup,
            Model model) {
        List<DonorDetails> donors = new ArrayList<>();
        String error = null;

        User currentUser = getCurrentUser();
        ReceiverDetails receiverDetails = receiverService.findByUser(currentUser).orElse(null);

        if (bloodGroup != null && !bloodGroup.isEmpty()) {
            try {
                BloodGroup bg = BloodGroup.valueOf(bloodGroup);
                donors = donorService.findDonorsByBloodGroupSortedByProximity(bg, receiverDetails);
            } catch (IllegalArgumentException e) {
                error = "Invalid blood group selected.";
            }
        }

        if (receiverDetails != null) {
            model.addAttribute("receiverAddress", receiverDetails.composeAddress());
        }

        model.addAttribute("donors", donors);
        model.addAttribute("bloodGroups", BloodGroup.values());
        model.addAttribute("selectedBloodGroup", bloodGroup);
        model.addAttribute("errorMessage", error);

        boolean hasAddress = receiverDetails != null &&
                receiverDetails.getProvince() != null &&
                !receiverDetails.getProvince().isBlank();
        model.addAttribute("hasReceiverAddress", hasAddress);

        return "receiver/search-donor";
    }

    @PostMapping("/verify-donation")
    public String verifyDonation(@RequestParam("donorId") Long donorId) {
        User receiver = getCurrentUser();
        User donor = userService.findById(donorId)
                .orElseThrow(() -> new RuntimeException("Donor not found"));

        donationHistoryService.createDonationHistory(donor, receiver);
        // Verify immediately
        Long historyId = donationHistoryService.getReceiverHistory(receiver).get(0).getId();
        donationHistoryService.verifyByReceiver(historyId);

        return "redirect:/receiver/search?verified=true";
    }

    @GetMapping("/edit")
    public String editProfileForm(Model model) {
        User user = getCurrentUser();
        Optional<ReceiverDetails> receiverDetails = receiverService.findByUser(user);

        ReceiverUpdateDTO updateDTO = new ReceiverUpdateDTO();
        if (receiverDetails.isPresent()) {
            ReceiverDetails d = receiverDetails.get();
            updateDTO.setAddress(d.getAddress());
            updateDTO.setProvince(d.getProvince());
            updateDTO.setDistrict(d.getDistrict());
            updateDTO.setPalika(d.getPalika());
            updateDTO.setWardNo(d.getWardNo());
            updateDTO.setBio(d.getBio());
        }

        model.addAttribute("user", user);
        model.addAttribute("receiverDetails", receiverDetails.orElse(null));
        model.addAttribute("updateDTO", updateDTO);
        return "receiver/edit-receiver";
    }

    @PostMapping("/update")
    public String submitProfileUpdate(@ModelAttribute ReceiverUpdateDTO updateDTO) {
        User user = getCurrentUser();

        if (updateDTO.getProfileImage() != null && !updateDTO.getProfileImage().isEmpty()) {
            try {
                String path = fileUploadService.uploadFile(updateDTO.getProfileImage(), "profiles");
                receiverService.updateProfileImage(user, path);
            } catch (Exception e) {
                // Log error
            }
        }

        if (updateDTO.getAddress() == null || updateDTO.getAddress().isBlank()) {
            updateDTO.setAddress(formatAddress(
                    updateDTO.getProvince(),
                    updateDTO.getDistrict(),
                    updateDTO.getPalika(),
                    updateDTO.getWardNo(),
                    null));
        }

        receiverService.updateReceiverDetails(user, updateDTO);
        return "redirect:/receiver/profile?success=true";
    }

    @GetMapping("/search-users")
    public String searchUsers(@RequestParam(required = false) String query,
            @RequestParam(required = false) String type,
            Model model) {

        List<User> users = Collections.emptyList();

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
    public String deleteReceiverAccount() {
        User user = getCurrentUser();
        userService.deleteUser(user.getId());
        SecurityContextHolder.clearContext();
        return "redirect:/auth/login?deleted=true";
    }

    private String formatAddress(String province, String district, String palika, String wardNo, String fallback) {
        StringBuilder sb = new StringBuilder();
        if (province != null && !province.isBlank())
            sb.append(province.trim());

        if (district != null && !district.isBlank()) {
            if (sb.length() > 0)
                sb.append(", ");
            sb.append(district.trim());
        }

        if (palika != null && !palika.isBlank()) {
            if (sb.length() > 0)
                sb.append(", ");
            sb.append(palika.trim());
        }

        if (wardNo != null && !wardNo.isBlank()) {
            if (sb.length() > 0)
                sb.append(" - ");
            sb.append("Ward ").append(wardNo.trim());
        }

        if (sb.length() == 0 && fallback != null && !fallback.isBlank()) {
            sb.append(fallback.trim());
        }

        return sb.length() == 0 ? "Not Provided" : sb.toString();
    }
}
