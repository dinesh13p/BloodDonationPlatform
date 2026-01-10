package bloodbank.controller;

import bloodbank.dto.RegistrationDTO;
import bloodbank.entity.BloodGroup;
import bloodbank.entity.User;
import bloodbank.entity.UserRole;
import bloodbank.service.DonorService;
import bloodbank.service.ReceiverService;
import bloodbank.service.UserService;
import bloodbank.service.FileUploadService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private DonorService donorService;

    @Autowired
    private ReceiverService receiverService;

    @Autowired
    private FileUploadService fileUploadService;

    @GetMapping("/login")
    public String loginPage() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            String role = auth.getAuthorities().iterator().next().getAuthority();
            if ("ROLE_ADMIN".equals(role))
                return "redirect:/admin/dashboard";
            if ("ROLE_DONOR".equals(role))
                return "redirect:/donor/dashboard";
            if ("ROLE_RECEIVER".equals(role))
                return "redirect:/receiver/dashboard";
        }
        return "auth/login";
    }

    @GetMapping("/register/donor")
    public String donorRegisterPage(Model model) {
        model.addAttribute("registrationDTO", new RegistrationDTO());
        model.addAttribute("bloodGroups", BloodGroup.values());
        return "auth/register-donor";
    }

    @PostMapping("/register/donor")
    public String handleDonorRegistration(@Valid @ModelAttribute RegistrationDTO registrationDTO,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("bloodGroups", BloodGroup.values());
            return "auth/register-donor";
        }

        if (userService.findByUsername(registrationDTO.getUsername()).isPresent()) {
            model.addAttribute("error", "Username already exists");
            model.addAttribute("bloodGroups", BloodGroup.values());
            return "auth/register-donor";
        }

        if (userService.findByEmail(registrationDTO.getEmail()).isPresent()) {
            model.addAttribute("error", "Email already exists");
            model.addAttribute("bloodGroups", BloodGroup.values());
            return "auth/register-donor";
        }

        registrationDTO.setAddress(formatAddress(
                registrationDTO.getProvince(),
                registrationDTO.getDistrict(),
                registrationDTO.getPalika(),
                registrationDTO.getWardNo()));

        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setPassword(registrationDTO.getPassword());
        user.setEmail(registrationDTO.getEmail());
        user.setFullName(registrationDTO.getFullName());
        user.setPhone(registrationDTO.getPhone());
        user.setRole(UserRole.DONOR);

        String profileImagePath = null;
        if (registrationDTO.getProfileImage() != null && !registrationDTO.getProfileImage().isEmpty()) {
            try {
                profileImagePath = fileUploadService.uploadFile(registrationDTO.getProfileImage(), "profiles");
            } catch (Exception e) {
                // Ignore upload failure for now
            }
        }

        user = userService.registerUser(user);
        donorService.registerDonorDetails(
                user,
                registrationDTO.getBloodGroup(),
                registrationDTO.getAddress(),
                registrationDTO.getProvince(),
                registrationDTO.getDistrict(),
                registrationDTO.getPalika(),
                registrationDTO.getWardNo(),
                registrationDTO.getBio(),
                profileImagePath);

        String encodedUsername = URLEncoder.encode(registrationDTO.getUsername(), StandardCharsets.UTF_8);
        return "redirect:/auth/login?registered=true&username=" + encodedUsername;
    }

    @GetMapping("/register/receiver")
    public String receiverRegisterPage(Model model) {
        model.addAttribute("registrationDTO", new RegistrationDTO());
        model.addAttribute("bloodGroups", BloodGroup.values());
        return "auth/register-receiver";
    }

    @PostMapping("/register/receiver")
    public String handleReceiverRegistration(@Valid @ModelAttribute RegistrationDTO registrationDTO,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("bloodGroups", BloodGroup.values());
            return "auth/register-receiver";
        }

        if (registrationDTO.getPan() == null || registrationDTO.getPan().isBlank()) {
            model.addAttribute("error", "PAN is required");
            model.addAttribute("bloodGroups", BloodGroup.values());
            return "auth/register-receiver";
        }

        if (userService.findByUsername(registrationDTO.getUsername()).isPresent()) {
            model.addAttribute("error", "Username already exists");
            model.addAttribute("bloodGroups", BloodGroup.values());
            return "auth/register-receiver";
        }

        if (userService.findByEmail(registrationDTO.getEmail()).isPresent()) {
            model.addAttribute("error", "Email already exists");
            model.addAttribute("bloodGroups", BloodGroup.values());
            return "auth/register-receiver";
        }

        registrationDTO.setAddress(formatAddress(
                registrationDTO.getProvince(),
                registrationDTO.getDistrict(),
                registrationDTO.getPalika(),
                registrationDTO.getWardNo()));

        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setPassword(registrationDTO.getPassword());
        user.setEmail(registrationDTO.getEmail());
        user.setFullName(registrationDTO.getFullName());
        user.setPhone(registrationDTO.getPhone());
        user.setRole(UserRole.RECEIVER);

        String profileImagePath = null;
        if (registrationDTO.getProfileImage() != null && !registrationDTO.getProfileImage().isEmpty()) {
            try {
                profileImagePath = fileUploadService.uploadFile(registrationDTO.getProfileImage(), "profiles");
            } catch (Exception e) {
                // Ignore
            }
        }

        user = userService.registerUser(user);
        receiverService.registerReceiverDetails(
                user,
                registrationDTO.getPan(),
                registrationDTO.getAddress(),
                registrationDTO.getProvince(),
                registrationDTO.getDistrict(),
                registrationDTO.getPalika(),
                registrationDTO.getWardNo(),
                registrationDTO.getBio(),
                profileImagePath);

        String encodedUsername = URLEncoder.encode(registrationDTO.getUsername(), StandardCharsets.UTF_8);
        return "redirect:/auth/login?registered=true&username=" + encodedUsername;
    }

    private String formatAddress(String province, String district, String palika, String wardNo) {
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

        String res = sb.toString();
        return res.isBlank() ? "Not Provided" : res;
    }
}
