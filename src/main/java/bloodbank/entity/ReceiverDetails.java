package bloodbank.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "receiver_details")
public class ReceiverDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "pan", unique = true)
    private String pan;

    @Column(name = "address")
    private String address;

    @Column(name = "province")
    private String province;

    @Column(name = "district")
    private String district;

    @Column(name = "palika")
    private String palika;

    @Column(name = "ward_no")
    private String wardNo;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "verified", nullable = false)
    private Boolean verified = false;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPalika() {
        return palika;
    }

    public void setPalika(String palika) {
        this.palika = palika;
    }

    public String getWardNo() {
        return wardNo;
    }

    public void setWardNo(String wardNo) {
        this.wardNo = wardNo;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    /**
     * Helper method to compose address from separate fields
     * Format: "Province, District, Palika - Ward X"
     */
    public String composeAddress() {
        StringBuilder sb = new StringBuilder();
        if (province != null && !province.isBlank())
            sb.append(province.trim());

        if (district != null && !district.isBlank()) {
            if (sb.length() > 0)
                sb.append(", ");
            sb.append(district.trim());
        }

        if (palika != null && !palika.isBlank()) {
            if (sb.length() > 0)
                sb.append(", ");
            sb.append(palika.trim());
        }

        if (wardNo != null && !wardNo.isBlank()) {
            if (sb.length() > 0)
                sb.append(" - ");
            sb.append("Ward ").append(wardNo.trim());
        }

        if (sb.length() == 0 && address != null && !address.isBlank()) {
            sb.append(address.trim());
        }

        return sb.length() == 0 ? "Not Provided" : sb.toString();
    }
}
