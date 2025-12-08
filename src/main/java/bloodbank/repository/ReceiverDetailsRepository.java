package bloodbank.repository;

import bloodbank.entity.ReceiverDetails;
import bloodbank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReceiverDetailsRepository extends JpaRepository<ReceiverDetails, Long> {
    Optional<ReceiverDetails> findByUser(User user);

    // Search methods
    List<ReceiverDetails> findByProvince(String province);

    List<ReceiverDetails> findByDistrict(String district);

    List<ReceiverDetails> findByPalika(String palika);

    List<ReceiverDetails> findByWardNo(String wardNo);
}
