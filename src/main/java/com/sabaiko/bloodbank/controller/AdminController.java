package com.sabaiko.bloodbank.controller;

import com.sabaiko.bloodbank.entity.User;
import com.sabaiko.bloodbank.entity.UserRole;
import com.sabaiko.bloodbank.service.AdminService;
import com.sabaiko.bloodbank.service.DonorService;
import com.sabaiko.bloodbank.service.ReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private AdminService adminService;
    
    @Autowired
    private DonorService donorService;
    
    @Autowired
    private ReceiverService receiverService;
    
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        model.addAttribute("pendingDonors", adminService.getPendingDonors());
        model.addAttribute("pendingReceivers", adminService.getPendingReceivers());
        model.addAttribute("allDonors", adminService.getAllDonors());
        model.addAttribute("allReceivers", adminService.getAllReceivers());
        return "admin/dashboard";
    }
    
    @GetMapping("/pending")
    public String showPendingUsers(Model model) {
        model.addAttribute("pendingUsers", adminService.getPendingUsers());
        return "admin/pending-users";
    }
    
    @GetMapping("/verified")
    public String showVerifiedUsers(Model model) {
        model.addAttribute("donors", donorService.getAllDonors());
        model.addAttribute("receivers", receiverService.getAllReceivers());
        return "admin/verified-users";
    }
    
    @PostMapping("/approve")
    public String approveUser(@RequestParam Long userId) {
        adminService.approveUser(userId);
        return "redirect:/admin/pending";
    }
    
    @PostMapping("/reject")
    public String rejectUser(@RequestParam Long userId) {
        adminService.rejectUser(userId);
        return "redirect:/admin/pending";
    }
    
    @PostMapping("/restrict")
    public String restrictUser(@RequestParam Long userId) {
        adminService.restrictUser(userId);
        return "redirect:/admin/dashboard";
    }
    
    @PostMapping("/add-star")
    public String addStarToDonor(@RequestParam Long donorId) {
        adminService.addStarToDonor(donorId);
        return "redirect:/admin/dashboard";
    }
}


