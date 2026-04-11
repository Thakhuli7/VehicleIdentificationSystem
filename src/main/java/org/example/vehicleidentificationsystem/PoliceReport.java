package org.example.vehicleidentificationsystem;

import java.time.LocalDate;

public class PoliceReport {
    private int reportId;
    private int vehicleId;
    private String vehicleRegNumber;
    private LocalDate reportDate;
    private String reportType;
    private String description;
    private String officerName;
    private String stationName;
    private String caseNumber;
    private String resolutionStatus;

    // Default Constructor
    public PoliceReport() {}

    // Full Constructor
    public PoliceReport(int reportId, int vehicleId, String vehicleRegNumber, LocalDate reportDate,
                        String reportType, String description, String officerName,
                        String stationName, String caseNumber, String resolutionStatus) {
        this.reportId = reportId;
        this.vehicleId = vehicleId;
        this.vehicleRegNumber = vehicleRegNumber;
        this.reportDate = reportDate;
        this.reportType = reportType;
        this.description = description;
        this.officerName = officerName;
        this.stationName = stationName;
        this.caseNumber = caseNumber;
        this.resolutionStatus = resolutionStatus;
    }

    // Getters and Setters
    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
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

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOfficerName() {
        return officerName;
    }

    public void setOfficerName(String officerName) {
        this.officerName = officerName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getResolutionStatus() {
        return resolutionStatus;
    }

    public void setResolutionStatus(String resolutionStatus) {
        this.resolutionStatus = resolutionStatus;
    }

    // Helper method to check if report is resolved
    public boolean isResolved() {
        return "Resolved".equalsIgnoreCase(resolutionStatus) || "Closed".equalsIgnoreCase(resolutionStatus);
    }

    // Helper method to get report type icon
    public String getReportTypeIcon() {
        switch (reportType) {
            case "Accident":
                return "💥";
            case "Theft":
                return "🔒";
            case "Vandalism":
                return "🎨";
            case "Stolen":
                return "🚗🔑";
            default:
                return "📋";
        }
    }

    @Override
    public String toString() {
        return reportType + " Report #" + reportId + " - " + caseNumber;
    }
}