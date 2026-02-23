package bloodbank.repository;

import bloodbank.entity.DonationHistory;
import bloodbank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DonationHistoryRepository extends JpaRepository<DonationHistory, Long> {
    List<DonationHistory> findByDonor(User donor);

    List<DonationHistory> findByReceiver(User receiver);

    @Transactional
    void deleteByDonor(User donor);

    @Transactional
    void deleteByReceiver(User receiver);
}
