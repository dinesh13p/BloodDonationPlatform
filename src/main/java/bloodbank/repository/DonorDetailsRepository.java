package bloodbank.repository;

import bloodbank.entity.BloodGroup;
import bloodbank.entity.DonorDetails;
import bloodbank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonorDetailsRepository extends JpaRepository<DonorDetails, Long> {
    Optional<DonorDetails> findByUser(User user);
    List<DonorDetails> findByBloodGroup(BloodGroup bloodGroup);
    List<DonorDetails> findByBloodGroupAndAvailability(BloodGroup bloodGroup, Boolean availability);
    List<DonorDetails> findByProvince(String province);
    List<DonorDetails> findByDistrict(String district);
    List<DonorDetails> findByPalika(String palika);
    List<DonorDetails> findByWardNo(String wardNo);
}


