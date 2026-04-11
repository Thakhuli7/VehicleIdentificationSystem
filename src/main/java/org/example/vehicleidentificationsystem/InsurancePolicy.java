package org.example.vehicleidentificationsystem;

import java.time.LocalDate;

public class InsurancePolicy {
    private int policyId;
    private int vehicleId;
    private String vehicleRegNumber;
    private String insuranceCompany;
    private String policyNumber;
    private LocalDate startDate;
    private LocalDate endDate;
    private double premiumAmount;
    private String coverageDetails;
    private String policyType;
    private String status;

    // Default Constructor
    public InsurancePolicy() {}

    // Full Constructor
    public InsurancePolicy(int policyId, int vehicleId, String vehicleRegNumber, String insuranceCompany,
                           String policyNumber, LocalDate startDate, LocalDate endDate,
                           double premiumAmount, String coverageDetails, String policyType, String status) {
        this.policyId = policyId;
        this.vehicleId = vehicleId;
        this.vehicleRegNumber = vehicleRegNumber;
        this.insuranceCompany = insuranceCompany;
        this.policyNumber = policyNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.premiumAmount = premiumAmount;
        this.coverageDetails = coverageDetails;
        this.policyType = policyType;
        this.status = status;
    }

    // Getters and Setters
    public int getPolicyId() { return policyId; }
    public void setPolicyId(int policyId) { this.policyId = policyId; }

    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }

    public String getVehicleRegNumber() { return vehicleRegNumber; }
    public void setVehicleRegNumber(String vehicleRegNumber) { this.vehicleRegNumber = vehicleRegNumber; }

    public String getInsuranceCompany() { return insuranceCompany; }
    public void setInsuranceCompany(String insuranceCompany) { this.insuranceCompany = insuranceCompany; }

    public String getPolicyNumber() { return policyNumber; }
    public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public double getPremiumAmount() { return premiumAmount; }
    public void setPremiumAmount(double premiumAmount) { this.premiumAmount = premiumAmount; }

    public String getCoverageDetails() { return coverageDetails; }
    public void setCoverageDetails(String coverageDetails) { this.coverageDetails = coverageDetails; }

    public String getPolicyType() { return policyType; }
    public void setPolicyType(String policyType) { this.policyType = policyType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Helper method to check if policy is active
    public boolean isActive() {
        return "Active".equalsIgnoreCase(status) && LocalDate.now().isBefore(endDate);
    }

    // Helper method to check if policy is expiring soon (within 30 days)
    public boolean isExpiringSoon() {
        return LocalDate.now().plusDays(30).isAfter(endDate) && !LocalDate.now().isAfter(endDate);
    }

    // Helper method to get days until expiration
    public long getDaysUntilExpiration() {
        if (LocalDate.now().isAfter(endDate)) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), endDate);
    }
}