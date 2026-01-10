package bloodbank.controller;

import bloodbank.dto.DonorUpdateDTO;
import bloodbank.entity.DonorDetails;
import bloodbank.entity.ReceiverDetails;
import bloodbank.entity.User;
import bloodbank.service.DonorService;
import bloodbank.service.FileUploadService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/donor")
@PreAuthorize("hasRole('DONOR')")
public class DonorController {

    @Autowired
    private DonorService donorService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReceiverService receiverService;

    @Autowired
    private FileUploadService fileUploadService;

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @GetMapping("/dashboard")
    public String donorDashboard() {
        return "donor/donor-dashboard";
    }

    @GetMapping("/profile")
    public String profilePage(Model model) {
        User user = getCurrentUser();
        Optional<DonorDetails> details = donorService.findByUser(user);

        model.addAttribute("user", user);
        model.addAttribute("donorDetails", details.orElse(null));
        return "donor/donor-profile";
    }

    @GetMapping("/edit")
    public String editProfileForm(Model model) {
        User user = getCurrentUser();
        Optional<DonorDetails> donorDetails = donorService.findByUser(user);

        DonorUpdateDTO updateDTO = new DonorUpdateDTO();
        if (donorDetails.isPresent()) {
            DonorDetails d = donorDetails.get();
            updateDTO.setBloodGroup(d.getBloodGroup());
            updateDTO.setAddress(d.getAddress());
            updateDTO.setProvince(d.getProvince());
            updateDTO.setDistrict(d.getDistrict());
            updateDTO.setPalika(d.getPalika());
            updateDTO.setWardNo(d.getWardNo());
            updateDTO.setAvailability(d.getAvailability());
            updateDTO.setMedicalHistory(d.getMedicalHistory());
            updateDTO.setDateOfBirth(d.getDateOfBirth());
            updateDTO.setBio(d.getBio());
        }

        model.addAttribute("user", user);
        model.addAttribute("donorDetails", donorDetails.orElse(null));
        model.addAttribute("updateDTO", updateDTO);
        return "donor/edit-donor";
    }

    @PostMapping("/update")
    public String processProfileUpdate(@ModelAttribute DonorUpdateDTO updateDTO) {
        User user = getCurrentUser();

        if (updateDTO.getProfileImage() != null && !updateDTO.getProfileImage().isEmpty()) {
            try {
                String path = fileUploadService.uploadFile(updateDTO.getProfileImage(), "profiles");
                donorService.updateProfileImage(user, path);
            } catch (Exception e) {
                e.printStackTrace(); // In real app, log this properly
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

        donorService.updateDonorDetails(user, updateDTO);
        return "redirect:/donor/profile?success=true";
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

    @GetMapping("/search")
    public String searchUsers(@RequestParam(required = false) String query,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String address,
            Model model) {

        List<User> users = java.util.Collections.emptyList();

        if (query != null && !query.isEmpty()) {
            if ("username".equals(type)) {
                users = userService.searchByUsername(query);
            } else if ("fullname".equals(type)) {
                users = userService.searchByFullName(query);
            }
        }
        // Future: implement address search logic here

        model.addAttribute("users", users);
        model.addAttribute("query", query);
        model.addAttribute("type", type);
        return "donor/search-users";
    }

    @GetMapping("/search-receivers")
    public String searchReceivers(@RequestParam(value = "phone", required = false) String phone, Model model) {
        List<User> receivers = userService.findVerifiedReceivers(Optional.ofNullable(phone));
        Map<Long, ReceiverDetails> detailsMap = new HashMap<>();

        for (User r : receivers) {
            receiverService.findByUser(r).ifPresent(d -> detailsMap.put(r.getId(), d));
        }

        model.addAttribute("receivers", receivers);
        model.addAttribute("receiverDetailsMap", detailsMap);
        model.addAttribute("searchPhone", phone);
        model.addAttribute("hasSearchPhone", phone != null && !phone.isBlank());
        model.addAttribute("noMatch", phone != null && !phone.isBlank() && receivers.isEmpty());

        return "donor/search-receivers";
    }

    @PostMapping("/delete")
    public String deleteDonorAccount() {
        User user = getCurrentUser();
        userService.deleteUser(user.getId());
        SecurityContextHolder.clearContext();
        return "redirect:/auth/login?deleted=true";
    }
}
