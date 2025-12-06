package com.sabaiko.bloodbank.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "donor_details")
public class DonorDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "blood_group", nullable = false)
    private BloodGroup bloodGroup;
    
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "city")
    private String city;
    
    @Column(name = "state")
    private String state;
    
    @Column(name = "zip_code")
    private String zipCode;
    
    @Column(name = "availability")
    private Boolean availability = true;
    
    @Column(name = "medical_history", columnDefinition = "TEXT")
    private String medicalHistory;
    
    @Column(name = "profile_image")
    private String profileImage;
    
    @Column(name = "stars", nullable = false)
    private Integer stars = 0;
    
    @Column(name = "last_donation_date")
    private LocalDate lastDonationDate;
    
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
    
    public BloodGroup getBloodGroup() {
        return bloodGroup;
    }
    
    public void setBloodGroup(BloodGroup bloodGroup) {
        this.bloodGroup = bloodGroup;
    }
    
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
    public String getZipCode() {
        return zipCode;
    }
    
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    
    public Boolean getAvailability() {
        return availability;
    }
    
    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }
    
    public String getMedicalHistory() {
        return medicalHistory;
    }
    
    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }
    
    public String getProfileImage() {
        return profileImage;
    }
    
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
    
    public Integer getStars() {
        return stars;
    }
    
    public void setStars(Integer stars) {
        this.stars = stars;
    }
    
    public LocalDate getLastDonationDate() {
        return lastDonationDate;
    }
    
    public void setLastDonationDate(LocalDate lastDonationDate) {
        this.lastDonationDate = lastDonationDate;
    }
}


