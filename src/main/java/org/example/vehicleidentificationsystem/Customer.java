package org.example.vehicleidentificationsystem;

import java.time.LocalDate;

public class Customer {
    private int customerId;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String driverLicense;
    private LocalDate dateOfBirth;
    private LocalDate registrationDate;
    private int loyaltyPoints;

    // Constructor 1: Basic (4 parameters)
    public Customer(int customerId, String name, String phone, String email) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = "";
        this.driverLicense = "";
        this.dateOfBirth = null;
        this.registrationDate = LocalDate.now();
        this.loyaltyPoints = 0;
    }

    // Constructor 2: Full (9 parameters)
    public Customer(int customerId, String name, String address, String phone, String email,
                    String driverLicense, LocalDate dateOfBirth, LocalDate registrationDate, int loyaltyPoints) {
        this.customerId = customerId;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.driverLicense = driverLicense;
        this.dateOfBirth = dateOfBirth;
        this.registrationDate = registrationDate;
        this.loyaltyPoints = loyaltyPoints;
    }

    // Default Constructor
    public Customer() {}

    // Getters and Setters
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDriverLicense() { return driverLicense; }
    public void setDriverLicense(String driverLicense) { this.driverLicense = driverLicense; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }

    public int getLoyaltyPoints() { return loyaltyPoints; }
    public void setLoyaltyPoints(int loyaltyPoints) { this.loyaltyPoints = loyaltyPoints; }
}