package bloodbank.controller;

import bloodbank.dto.DonorUpdateDTO;
import bloodbank.entity.BloodGroup;
import bloodbank.entity.DonorDetails;
import bloodbank.entity.User;
import bloodbank.service.DonorService;
import bloodbank.service.FileUploadService;
import bloodbank.service.UserService;
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

import java.util.List;
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
        model.addAttribute("updateDTO", new DonorUpdateDTO());
        model.addAttribute("bloodGroups", BloodGroup.values());
        return "donor/edit-donor";
    }

    @PostMapping("/update")
    public String updateProfile(@RequestParam(required = false) String address,
            @RequestParam(required = false) String province,
            @RequestParam(required = false) String district,
            @RequestParam(required = false) String palika,
            @RequestParam(required = false) String wardNo,
            @RequestParam(required = false) Boolean availability,
            @RequestParam(required = false) String medicalHistory,
            @RequestParam(required = false) String dateOfBirth,
            @RequestParam(required = false) String bloodGroup,
            @RequestParam(required = false) MultipartFile profileImage,
            @RequestParam(required = false) String bio) {
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
        updateDTO.setProvince(province);
        updateDTO.setDistrict(district);
        updateDTO.setPalika(palika);
        updateDTO.setWardNo(wardNo);
        updateDTO.setAvailability(availability);
        updateDTO.setMedicalHistory(medicalHistory);
        if (dateOfBirth != null && !dateOfBirth.isEmpty()) {
            updateDTO.setDateOfBirth(java.time.LocalDate.parse(dateOfBirth));
        }
        if (bloodGroup != null && !bloodGroup.isEmpty()) {
            try {
                updateDTO.setBloodGroup(BloodGroup.valueOf(bloodGroup));
            } catch (IllegalArgumentException ignored) {
                // ignore invalid blood group input to keep existing value
            }
        }
        updateDTO.setBio(bio);

        donorService.updateDonorDetails(user, updateDTO);
        return "redirect:/donor/profile";
    }

    @GetMapping("/search")
    public String searchUsers(@RequestParam(required = false) String query,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String address,
            Model model) {
        List<User> users = java.util.Collections.emptyList();

        if (query != null && !query.isEmpty()) {
            if ("username".equals(type)) {
                users = userService.searchByUsername(query);
            } else if ("fullname".equals(type)) { // Organization name is effectively Full Name for receivers or we can
                                                  // search generic
                users = userService.searchByFullName(query);
            }
        } else if (address != null && !address.isEmpty()) {
            // For simplicity, searching users by address string match in future updates
            // currently falling back to generic user search
        }

        model.addAttribute("users", users);
        model.addAttribute("query", query);
        model.addAttribute("type", type);
        return "donor/search-users";
    }

    @PostMapping("/delete")
    public String deleteAccount() {
        User user = getCurrentUser();
        userService.deleteUser(user.getId());
        SecurityContextHolder.clearContext();
        return "redirect:/auth/login?deleted=true";
    }
}
