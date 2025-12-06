package com.sabaiko.bloodbank.entity;

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
    
    @Enumerated(EnumType.STRING)
    @Column(name = "blood_group_needed")
    private BloodGroup bloodGroupNeeded;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "city")
    private String city;
    
    @Column(name = "state")
    private String state;
    
    @Column(name = "zip_code")
    private String zipCode;
    
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
    
    public BloodGroup getBloodGroupNeeded() {
        return bloodGroupNeeded;
    }
    
    public void setBloodGroupNeeded(BloodGroup bloodGroupNeeded) {
        this.bloodGroupNeeded = bloodGroupNeeded;
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
    
    public Boolean getVerified() {
        return verified;
    }
    
    public void setVerified(Boolean verified) {
        this.verified = verified;
    }
}


