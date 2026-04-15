package org.example.vehicleidentificationsystem;

import java.time.LocalDate;

public class ServiceRecord {
    private int serviceId;
    private int vehicleId;
    private LocalDate serviceDate;
    private String serviceType;
    private String description;
    private double cost;
    private String mechanicName;
    private LocalDate nextServiceDate;
    private String serviceCenter;
    private String invoiceNumber;
    private String vehicleRegNumber;  // ADDED: For displaying vehicle registration in table

    // Constructor
    public ServiceRecord(int serviceId, int vehicleId, LocalDate serviceDate, String serviceType,
                         String description, double cost, String mechanicName, LocalDate nextServiceDate,
                         String serviceCenter, String invoiceNumber) {
        this.serviceId = serviceId;
        this.vehicleId = vehicleId;
        this.serviceDate = serviceDate;
        this.serviceType = serviceType;
        this.description = description;
        this.cost = cost;
        this.mechanicName = mechanicName;
        this.nextServiceDate = nextServiceDate;
        this.serviceCenter = serviceCenter;
        this.invoiceNumber = invoiceNumber;
        this.vehicleRegNumber = "";
    }

    // Getters and Setters
    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }

    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }

    public LocalDate getServiceDate() { return serviceDate; }
    public void setServiceDate(LocalDate serviceDate) { this.serviceDate = serviceDate; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }

    public String getMechanicName() { return mechanicName; }
    public void setMechanicName(String mechanicName) { this.mechanicName = mechanicName; }

    public LocalDate getNextServiceDate() { return nextServiceDate; }
    public void setNextServiceDate(LocalDate nextServiceDate) { this.nextServiceDate = nextServiceDate; }

    public String getServiceCenter() { return serviceCenter; }
    public void setServiceCenter(String serviceCenter) { this.serviceCenter = serviceCenter; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    // ADDED: Getter and Setter for vehicleRegNumber
    public String getVehicleRegNumber() { return vehicleRegNumber; }
    public void setVehicleRegNumber(String vehicleRegNumber) { this.vehicleRegNumber = vehicleRegNumber; }
}