package bloodbank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String home() {
        return "redirect:/auth/login";
    }

    @GetMapping("/about")
    public String about() {
        return "layout/about";
    }

    @GetMapping("/blog")
    public String blog() {
        return "layout/blog";
    }

    @GetMapping("/faq")
    public String faq() {
        return "layout/faq";
    }
}


