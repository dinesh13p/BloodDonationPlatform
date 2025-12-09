package bloodbank.controller;

import bloodbank.entity.BloodGroup;
import bloodbank.entity.DonorDetails;
import bloodbank.service.DonorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {

    @Autowired
    private DonorService donorService;

    @GetMapping("/search")
    public String searchBlood(
            @RequestParam(required = false) String bloodGroup,
            @RequestParam(required = false) String province,
            @RequestParam(required = false) String district,
            @RequestParam(required = false) String palika,
            @RequestParam(required = false) String wardNo,
            Model model) {

        List<DonorDetails> donors = donorService.getAllDonors().stream()
                .filter(DonorDetails::getAvailability)
                .toList();

        if (bloodGroup != null && !bloodGroup.isBlank()) {
            try {
                BloodGroup group = BloodGroup.valueOf(bloodGroup);
                donors = donors.stream()
                        .filter(d -> d.getBloodGroup() == group)
                        .toList();
            } catch (IllegalArgumentException ignored) {
                // ignore invalid blood group
            }
        }

        if (province != null && !province.isBlank()) {
            String value = province.trim();
            donors = donors.stream()
                    .filter(d -> value.equalsIgnoreCase(d.getProvince()))
                    .toList();
        }
        if (district != null && !district.isBlank()) {
            String value = district.trim();
            donors = donors.stream()
                    .filter(d -> value.equalsIgnoreCase(d.getDistrict()))
                    .toList();
        }
        if (palika != null && !palika.isBlank()) {
            String value = palika.trim();
            donors = donors.stream()
                    .filter(d -> value.equalsIgnoreCase(d.getPalika()))
                    .toList();
        }
        if (wardNo != null && !wardNo.isBlank()) {
            String value = wardNo.trim();
            donors = donors.stream()
                    .filter(d -> value.equalsIgnoreCase(d.getWardNo()))
                    .toList();
        }

        model.addAttribute("bloodGroups", BloodGroup.values());
        model.addAttribute("donors", donors);
        model.addAttribute("selectedBloodGroup", bloodGroup);
        model.addAttribute("selectedProvince", province);
        model.addAttribute("selectedDistrict", district);
        model.addAttribute("selectedPalika", palika);
        model.addAttribute("selectedWardNo", wardNo);
        return "layout/search";
    }
}

