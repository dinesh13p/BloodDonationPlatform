package bloodbank.repository;

import bloodbank.entity.BloodGroup;
import bloodbank.entity.DonorDetails;
import bloodbank.entity.User;
import bloodbank.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    
    /**
     * Find all verified, available donors with the specified blood group.
     * Only returns donors whose user status is APPROVED and availability is true.
     */
    @Query("SELECT d FROM DonorDetails d JOIN d.user u WHERE d.bloodGroup = :bloodGroup " +
           "AND d.availability = true " +
           "AND u.status = :status")
    List<DonorDetails> findVerifiedAvailableDonorsByBloodGroup(
        @Param("bloodGroup") BloodGroup bloodGroup,
        @Param("status") UserStatus status
    );
}


