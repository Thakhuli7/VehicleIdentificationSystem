package org.example.vehicleidentificationsystem;

import java.time.LocalDate;

public class Claim {
    private int claimId;
    private int policyId;
    private String policyNumber;
    private LocalDate claimDate;
    private double claimAmount;
    private double approvedAmount;
    private String status;  // Pending, Approved, Rejected
    private String description;
    private String claimType;

    // Default Constructor
    public Claim() {}

    // Full Constructor
    public Claim(int claimId, int policyId, String policyNumber, LocalDate claimDate,
                 double claimAmount, double approvedAmount, String status,
                 String description, String claimType) {
        this.claimId = claimId;
        this.policyId = policyId;
        this.policyNumber = policyNumber;
        this.claimDate = claimDate;
        this.claimAmount = claimAmount;
        this.approvedAmount = approvedAmount;
        this.status = status;
        this.description = description;
        this.claimType = claimType;
    }

    // Getters and Setters
    public int getClaimId() { return claimId; }
    public void setClaimId(int claimId) { this.claimId = claimId; }

    public int getPolicyId() { return policyId; }
    public void setPolicyId(int policyId) { this.policyId = policyId; }

    public String getPolicyNumber() { return policyNumber; }
    public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }

    public LocalDate getClaimDate() { return claimDate; }
    public void setClaimDate(LocalDate claimDate) { this.claimDate = claimDate; }

    public double getClaimAmount() { return claimAmount; }
    public void setClaimAmount(double claimAmount) { this.claimAmount = claimAmount; }

    public double getApprovedAmount() { return approvedAmount; }
    public void setApprovedAmount(double approvedAmount) { this.approvedAmount = approvedAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getClaimType() { return claimType; }
    public void setClaimType(String claimType) { this.claimType = claimType; }

    // Helper method to get status color
    public String getStatusColor() {
        switch (status.toLowerCase()) {
            case "approved":
                return "#27ae60";
            case "rejected":
                return "#e74c3c";
            case "pending":
                return "#f39c12";
            default:
                return "#7f8c8d";
        }
    }

    // Helper method to check if claim is approved
    public boolean isApproved() {
        return "Approved".equalsIgnoreCase(status);
    }
}