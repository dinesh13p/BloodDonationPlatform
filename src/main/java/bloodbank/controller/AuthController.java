package bloodbank.controller;

import bloodbank.dto.RegistrationDTO;
import bloodbank.entity.BloodGroup;
import bloodbank.entity.User;
import bloodbank.entity.UserRole;
import bloodbank.service.DonorService;
import bloodbank.service.ReceiverService;
import bloodbank.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    @GetMapping("/login")
    public String showLoginPage() {
        // If user is already authenticated, redirect to appropriate dashboard
        org.springframework.security.core.Authentication auth = 
            org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            String role = auth.getAuthorities().iterator().next().getAuthority();
            return switch (role) {
                case "ROLE_ADMIN" -> "redirect:/admin/dashboard";
                case "ROLE_DONOR" -> "redirect:/donor/dashboard";
                case "ROLE_RECEIVER" -> "redirect:/receiver/dashboard";
                default -> "auth/login";
            };
        }
        
        return "auth/login";
    }
    
    @GetMapping("/register/donor")
    public String showDonorRegistrationForm(Model model) {
        model.addAttribute("registrationDTO", new RegistrationDTO());
        model.addAttribute("bloodGroups", BloodGroup.values());
        return "auth/register-donor";
    }
    
    @PostMapping("/register/donor")
    public String registerDonor(@Valid @ModelAttribute RegistrationDTO registrationDTO, 
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
        
        registrationDTO.setAddress(composeAddress(
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
        
        user = userService.createUser(user);
        donorService.createDonorDetails(
            user,
            registrationDTO.getBloodGroup(),
            registrationDTO.getAddress(),
            registrationDTO.getProvince(),
            registrationDTO.getDistrict(),
            registrationDTO.getPalika(),
            registrationDTO.getWardNo(),
            registrationDTO.getBio()
        );
        
        String encodedUsername = URLEncoder.encode(registrationDTO.getUsername(), StandardCharsets.UTF_8);
        return "redirect:/auth/login?registered=true&username=" + encodedUsername;
    }
    
    @GetMapping("/register/receiver")
    public String showReceiverRegistrationForm(Model model) {
        model.addAttribute("registrationDTO", new RegistrationDTO());
        model.addAttribute("bloodGroups", BloodGroup.values());
        return "auth/register-receiver";
    }
    
    @PostMapping("/register/receiver")
    public String registerReceiver(@Valid @ModelAttribute RegistrationDTO registrationDTO, 
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

        registrationDTO.setAddress(composeAddress(
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
        
        user = userService.createUser(user);
        receiverService.createReceiverDetails(
            user,
            registrationDTO.getPan(),
            registrationDTO.getAddress(),
            registrationDTO.getProvince(),
            registrationDTO.getDistrict(),
            registrationDTO.getPalika(),
            registrationDTO.getWardNo(),
            registrationDTO.getBio()
        );
        
        String encodedUsername = URLEncoder.encode(registrationDTO.getUsername(), StandardCharsets.UTF_8);
        return "redirect:/auth/login?registered=true&username=" + encodedUsername;
    }

    private String composeAddress(String province, String district, String palika, String wardNo) {
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
        String composed = builder.toString();
        return composed.isBlank() ? "Not Provided" : composed;
    }
}


