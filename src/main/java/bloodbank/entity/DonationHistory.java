package bloodbank.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "donation_history")
public class DonationHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "donor_id", nullable = false)
    private User donor;
    
    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;
    
    @Column(name = "donation_date", nullable = false)
    private LocalDateTime donationDate;
    
    @Column(name = "verified_by_receiver", nullable = false)
    private Boolean verifiedByReceiver = false;
    
    @Column(name = "verified_by_admin", nullable = false)
    private Boolean verifiedByAdmin = false;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getDonor() {
        return donor;
    }
    
    public void setDonor(User donor) {
        this.donor = donor;
    }
    
    public User getReceiver() {
        return receiver;
    }
    
    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }
    
    public LocalDateTime getDonationDate() {
        return donationDate;
    }
    
    public void setDonationDate(LocalDateTime donationDate) {
        this.donationDate = donationDate;
    }
    
    public Boolean getVerifiedByReceiver() {
        return verifiedByReceiver;
    }
    
    public void setVerifiedByReceiver(Boolean verifiedByReceiver) {
        this.verifiedByReceiver = verifiedByReceiver;
    }
    
    public Boolean getVerifiedByAdmin() {
        return verifiedByAdmin;
    }
    
    public void setVerifiedByAdmin(Boolean verifiedByAdmin) {
        this.verifiedByAdmin = verifiedByAdmin;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
}


