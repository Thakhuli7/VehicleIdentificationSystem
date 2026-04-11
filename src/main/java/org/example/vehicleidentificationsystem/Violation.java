package org.example.vehicleidentificationsystem;

import java.time.LocalDate;

public class Violation {
    private int violationId;
    private int vehicleId;
    private String vehicleRegNumber;
    private LocalDate violationDate;
    private String violationType;
    private double fineAmount;
    private String status;  // Paid, Unpaid, Disputed
    private String location;
    private String officerName;
    private LocalDate paidDate;

    // Default Constructor
    public Violation() {}

    // Full Constructor
    public Violation(int violationId, int vehicleId, String vehicleRegNumber, LocalDate violationDate,
                     String violationType, double fineAmount, String status, String location,
                     String officerName, LocalDate paidDate) {
        this.violationId = violationId;
        this.vehicleId = vehicleId;
        this.vehicleRegNumber = vehicleRegNumber;
        this.violationDate = violationDate;
        this.violationType = violationType;
        this.fineAmount = fineAmount;
        this.status = status;
        this.location = location;
        this.officerName = officerName;
        this.paidDate = paidDate;
    }

    // Simplified Constructor
    public Violation(int violationId, String vehicleRegNumber, LocalDate violationDate,
                     String violationType, double fineAmount, String status, String location) {
        this.violationId = violationId;
        this.vehicleRegNumber = vehicleRegNumber;
        this.violationDate = violationDate;
        this.violationType = violationType;
        this.fineAmount = fineAmount;
        this.status = status;
        this.location = location;
    }

    // Getters and Setters
    public int getViolationId() {
        return violationId;
    }

    public void setViolationId(int violationId) {
        this.violationId = violationId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    public LocalDate getViolationDate() {
        return violationDate;
    }

    public void setViolationDate(LocalDate violationDate) {
        this.violationDate = violationDate;
    }

    public String getViolationType() {
        return violationType;
    }

    public void setViolationType(String violationType) {
        this.violationType = violationType;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOfficerName() {
        return officerName;
    }

    public void setOfficerName(String officerName) {
        this.officerName = officerName;
    }

    public LocalDate getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(LocalDate paidDate) {
        this.paidDate = paidDate;
    }

    // Helper method to check if violation is paid
    public boolean isPaid() {
        return "Paid".equalsIgnoreCase(status);
    }

    // Helper method to mark as paid
    public void markAsPaid() {
        this.status = "Paid";
        this.paidDate = LocalDate.now();
    }

    // Helper method to get status color code for UI
    public String getStatusColor() {
        if ("Paid".equalsIgnoreCase(status)) {
            return "#27ae60";  // Green
        } else if ("Unpaid".equalsIgnoreCase(status)) {
            return "#e74c3c";  // Red
        } else {
            return "#f39c12";  // Orange for Disputed
        }
    }

    // Helper method to get violation type icon
    public String getViolationIcon() {
        switch (violationType.toLowerCase()) {
            case "speeding":
                return "🏎️💨";
            case "parking":
                return "🅿️❌";
            case "red light":
                return "🚦";
            case "no seatbelt":
                return "🪑⚠️";
            case "drunk driving":
                return "🍺🚗";
            default:
                return "📜";
        }
    }

    @Override
    public String toString() {
        return violationType + " - $" + fineAmount + " (" + status + ")";
    }
}
