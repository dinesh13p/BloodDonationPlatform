package bloodbank.service;

import bloodbank.dto.DonorUpdateDTO;
import bloodbank.entity.BloodGroup;
import bloodbank.entity.DonorDetails;
import bloodbank.entity.ReceiverDetails;
import bloodbank.entity.User;
import bloodbank.entity.UserStatus;
import bloodbank.repository.DonorDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DonorService {

    @Autowired
    private DonorDetailsRepository donorDetailsRepository;

    public DonorDetails createDonorDetails(User user, BloodGroup bloodGroup, String address, String province,
            String district, String palika, String wardNo, String bio) {
        DonorDetails donorDetails = new DonorDetails();
        donorDetails.setUser(user);
        donorDetails.setBloodGroup(bloodGroup);
        donorDetails.setAddress(address);
        donorDetails.setProvince(province);
        donorDetails.setDistrict(district);
        donorDetails.setPalika(palika);
        donorDetails.setWardNo(wardNo);
        donorDetails.setBio(bio);
        return donorDetailsRepository.save(donorDetails);
    }

    public Optional<DonorDetails> findByUser(User user) {
        return donorDetailsRepository.findByUser(user);
    }

    public List<DonorDetails> findByBloodGroup(BloodGroup bloodGroup) {
        return donorDetailsRepository.findByBloodGroup(bloodGroup);
    }

    public List<DonorDetails> findByBloodGroupAndAvailability(BloodGroup bloodGroup, Boolean availability) {
        return donorDetailsRepository.findByBloodGroupAndAvailability(bloodGroup, availability);
    }

    public List<DonorDetails> findByProvince(String province) {
        return donorDetailsRepository.findByProvince(province);
    }

    public List<DonorDetails> findByDistrict(String district) {
        return donorDetailsRepository.findByDistrict(district);
    }

    public List<DonorDetails> findByPalika(String palika) {
        return donorDetailsRepository.findByPalika(palika);
    }

    public List<DonorDetails> findByWardNo(String wardNo) {
        return donorDetailsRepository.findByWardNo(wardNo);
    }

    @Transactional
    public DonorDetails updateDonorDetails(User user, DonorUpdateDTO updateDTO) {
        DonorDetails donorDetails = donorDetailsRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Donor details not found"));

        if (updateDTO.getBloodGroup() != null) {
            donorDetails.setBloodGroup(updateDTO.getBloodGroup());
        }
        if (updateDTO.getAddress() != null) {
            donorDetails.setAddress(updateDTO.getAddress());
        }
        if (updateDTO.getProvince() != null) {
            donorDetails.setProvince(updateDTO.getProvince());
        }
        if (updateDTO.getDistrict() != null) {
            donorDetails.setDistrict(updateDTO.getDistrict());
        }
        if (updateDTO.getPalika() != null) {
            donorDetails.setPalika(updateDTO.getPalika());
        }
        if (updateDTO.getWardNo() != null) {
            donorDetails.setWardNo(updateDTO.getWardNo());
        }
        if (updateDTO.getAvailability() != null) {
            donorDetails.setAvailability(updateDTO.getAvailability());
        }
        if (updateDTO.getMedicalHistory() != null) {
            donorDetails.setMedicalHistory(updateDTO.getMedicalHistory());
        }
        if (updateDTO.getDateOfBirth() != null) {
            donorDetails.setDateOfBirth(updateDTO.getDateOfBirth());
        }
        if (updateDTO.getBio() != null) {
            donorDetails.setBio(updateDTO.getBio());
        }

        return donorDetailsRepository.save(donorDetails);
    }

    @Transactional
    public void updateProfileImage(User user, String imagePath) {
        DonorDetails donorDetails = donorDetailsRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Donor details not found"));
        donorDetails.setProfileImage(imagePath);
        donorDetailsRepository.save(donorDetails);
    }

    public List<DonorDetails> getAllDonors() {
        return donorDetailsRepository.findAll()
                .stream()
                .filter(d -> d.getUser() != null && d.getUser().getStatus() == bloodbank.entity.UserStatus.APPROVED)
                .toList();
    }

    /**
     * Find verified, available donors by blood group and sort them by proximity to
     * receiver's address.
     * Proximity score calculation:
     * - Same province: +1000
     * - Same district: +100
     * - Same palika: +10
     * - Same ward: +1
     * 
     * Results are sorted by score descending, then by fullName ascending.
     * 
     * @param bloodGroup      The required blood group
     * @param receiverDetails The receiver's details for proximity calculation
     * @return Sorted list of donors (nearest first)
     */
    public List<DonorDetails> findDonorsByBloodGroupSortedByProximity(BloodGroup bloodGroup,
            ReceiverDetails receiverDetails) {
        // Find all verified, available donors with the specified blood group
        List<DonorDetails> donors = donorDetailsRepository.findVerifiedAvailableDonorsByBloodGroup(
                bloodGroup,
                UserStatus.APPROVED);

        // If receiver has no address details, return unsorted list
        if (receiverDetails == null ||
                (receiverDetails.getProvince() == null || receiverDetails.getProvince().isBlank())) {
            return donors.stream()
                    .filter(d -> d.getUser() != null)
                    .sorted(Comparator.comparing(d -> d.getUser().getFullName()))
                    .collect(Collectors.toList());
        }

        String receiverProvince = receiverDetails.getProvince() != null ? receiverDetails.getProvince().trim() : "";
        String receiverDistrict = receiverDetails.getDistrict() != null ? receiverDetails.getDistrict().trim() : "";
        String receiverPalika = receiverDetails.getPalika() != null ? receiverDetails.getPalika().trim() : "";
        String receiverWardNo = receiverDetails.getWardNo() != null ? receiverDetails.getWardNo().trim() : "";

        // Compute proximity score and sort
        return donors.stream()
                .filter(donor -> donor.getUser() != null) // Filter out donors without user
                .map(donor -> {
                    // Calculate proximity score
                    int score = 0;

                    String donorProvince = donor.getProvince() != null ? donor.getProvince().trim() : "";
                    String donorDistrict = donor.getDistrict() != null ? donor.getDistrict().trim() : "";
                    String donorPalika = donor.getPalika() != null ? donor.getPalika().trim() : "";
                    String donorWardNo = donor.getWardNo() != null ? donor.getWardNo().trim() : "";

                    if (!receiverProvince.isEmpty() && receiverProvince.equalsIgnoreCase(donorProvince)) {
                        score += 1000;
                    }
                    if (!receiverDistrict.isEmpty() && receiverDistrict.equalsIgnoreCase(donorDistrict)) {
                        score += 100;
                    }
                    if (!receiverPalika.isEmpty() && receiverPalika.equalsIgnoreCase(donorPalika)) {
                        score += 10;
                    }
                    if (!receiverWardNo.isEmpty() && receiverWardNo.equalsIgnoreCase(donorWardNo)) {
                        score += 1;
                    }

                    return new DonorWithScore(donor, score);
                })
                .sorted(Comparator
                        .comparing((DonorWithScore d) -> -d.score) // Descending by score (negative for reverse)
                        .thenComparing(d -> d.donor.getUser() != null ? d.donor.getUser().getFullName() : "") // Ascending
                                                                                                              // by name
                )
                .map(d -> d.donor)
                .collect(Collectors.toList());
    }

    /**
     * Helper class to hold donor with its proximity score
     */
    private static class DonorWithScore {
        final DonorDetails donor;
        final int score;

        DonorWithScore(DonorDetails donor, int score) {
            this.donor = donor;
            this.score = score;
        }
    }
}
