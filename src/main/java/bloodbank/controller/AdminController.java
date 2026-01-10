package bloodbank.controller;

import bloodbank.entity.User;
import bloodbank.service.AdminService;
import bloodbank.service.DonorService;
import bloodbank.service.ReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private DonorService donorService;

    @Autowired
    private ReceiverService receiverService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("pendingDonors", adminService.getPendingDonors());
        model.addAttribute("pendingReceivers", adminService.getPendingReceivers());

        model.addAttribute("allDonors", adminService.getAllDonors());
        model.addAttribute("allReceivers", adminService.getAllReceivers());
        model.addAttribute("restrictedUsers", adminService.getRestrictedUsers());

        return "admin/dashboard";
    }

    @GetMapping("/pending")
    public String pendingUsers(Model model) {
        model.addAttribute("pendingUsers", adminService.getPendingUsers());
        return "admin/pending-users";
    }

    @GetMapping("/verified")
    public String verifiedUsers(Model model) {
        model.addAttribute("donors", donorService.getAllDonors());
        model.addAttribute("receivers", receiverService.getAllReceivers());
        return "admin/verified-users";
    }

    @PostMapping("/approve")
    public String approve(@RequestParam("userId") Long userId) {
        adminService.approveUser(userId);
        return "redirect:/admin/pending";
    }

    @PostMapping("/reject")
    public String reject(@RequestParam("userId") Long userId) {
        adminService.rejectUser(userId);
        return "redirect:/admin/pending";
    }

    @PostMapping("/restrict")
    public String restrict(@RequestParam("userId") Long userId) {
        adminService.restrictUser(userId);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/unrestrict")
    public String unrestrict(@RequestParam("userId") Long userId) {
        adminService.unrestrictUser(userId);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/pending-details/{userId}")
    public String viewPendingDetails(@PathVariable("userId") Long userId, Model model) {
        User user = adminService.getPendingUser(userId);
        model.addAttribute("user", user);

        if (user.getRole() == bloodbank.entity.UserRole.DONOR) {
            donorService.findByUser(user).ifPresent(d -> model.addAttribute("donorDetails", d));
        } else if (user.getRole() == bloodbank.entity.UserRole.RECEIVER) {
            receiverService.findByUser(user).ifPresent(r -> model.addAttribute("receiverDetails", r));
        }

        return "admin/fragments/pending-details-modal :: details";
    }

    @GetMapping("/user-details/{userId}")
    public String viewUserDetails(@PathVariable("userId") Long userId, Model model) {
        User user = adminService.getUser(userId);
        model.addAttribute("user", user);

        if (user.getRole() == bloodbank.entity.UserRole.DONOR) {
            donorService.findByUser(user).ifPresent(d -> model.addAttribute("donorDetails", d));
        } else if (user.getRole() == bloodbank.entity.UserRole.RECEIVER) {
            receiverService.findByUser(user).ifPresent(r -> model.addAttribute("receiverDetails", r));
        }

        return "admin/fragments/pending-details-modal :: details";
    }

    @PostMapping("/delete")
    public String deleteUserAccount(@RequestParam("userId") Long userId) {
        adminService.deleteUser(userId);
        return "redirect:/admin/dashboard";
    }
}
