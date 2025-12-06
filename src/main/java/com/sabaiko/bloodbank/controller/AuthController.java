package com.sabaiko.bloodbank.controller;

import com.sabaiko.bloodbank.dto.RegistrationDTO;
import com.sabaiko.bloodbank.entity.BloodGroup;
import com.sabaiko.bloodbank.entity.User;
import com.sabaiko.bloodbank.entity.UserRole;
import com.sabaiko.bloodbank.service.DonorService;
import com.sabaiko.bloodbank.service.ReceiverService;
import com.sabaiko.bloodbank.service.UserService;
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
                case "ROLE_DONOR" -> "redirect:/donor/profile";
                case "ROLE_RECEIVER" -> "redirect:/receiver/profile";
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
        
        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setPassword(registrationDTO.getPassword());
        user.setEmail(registrationDTO.getEmail());
        user.setFullName(registrationDTO.getFullName());
        user.setPhone(registrationDTO.getPhone());
        user.setRole(UserRole.DONOR);
        
        user = userService.createUser(user);
        donorService.createDonorDetails(user, registrationDTO.getBloodGroup());
        
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
        
        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setPassword(registrationDTO.getPassword());
        user.setEmail(registrationDTO.getEmail());
        user.setFullName(registrationDTO.getFullName());
        user.setPhone(registrationDTO.getPhone());
        user.setRole(UserRole.RECEIVER);
        
        user = userService.createUser(user);
        receiverService.createReceiverDetails(user, registrationDTO.getBloodGroupNeeded());
        
        String encodedUsername = URLEncoder.encode(registrationDTO.getUsername(), StandardCharsets.UTF_8);
        return "redirect:/auth/login?registered=true&username=" + encodedUsername;
    }
}


