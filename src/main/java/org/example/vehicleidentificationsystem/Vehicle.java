package org.example.vehicleidentificationsystem;

import java.time.Year;

// Vehicle class - demonstrates encapsulation
public class Vehicle {
    private int vehicleId;
    private String registrationNumber;
    private String make;
    private String model;
    private int year;
    private String status;  // New field for vehicle status
    private String color;   // ADDED: Vehicle color
    private int mileage;    // ADDED: Vehicle mileage

    // Constructor 1: Original constructor
    public Vehicle(int vehicleId, String registrationNumber, String make, String model, int year) {
        this.vehicleId = vehicleId;
        this.registrationNumber = registrationNumber;
        this.make = make;
        this.model = model;
        this.year = year;
        this.status = calculateStatus();  // Auto-calculate status based on year
        this.color = "";
        this.mileage = 0;
    }

    // Constructor 2: Constructor with status parameter
    public Vehicle(int vehicleId, String registrationNumber, String make, String model, int year, String status) {
        this.vehicleId = vehicleId;
        this.registrationNumber = registrationNumber;
        this.make = make;
        this.model = model;
        this.year = year;
        this.status = status;
        this.color = "";
        this.mileage = 0;
    }

    // Getters and Setters (Encapsulation)
    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
        this.status = calculateStatus();  // Recalculate status when year changes
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // ADDED: Getter and Setter for color
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    // ADDED: Getter and Setter for mileage
    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    // Method to calculate vehicle status based on year
    private String calculateStatus() {
        int currentYear = Year.now().getValue();

        if (year == currentYear) {
            return "Brand New";
        } else if (year >= currentYear - 1) {
            return "Like New";
        } else if (year >= currentYear - 3) {
            return "Excellent";
        } else if (year >= currentYear - 5) {
            return "Good";
        } else if (year >= currentYear - 8) {
            return "Fair";
        } else {
            return "Old";
        }
    }

    // Method to get vehicle age
    public int getVehicleAge() {
        return Year.now().getValue() - year;
    }

    // Method to check if vehicle is vintage (over 15 years old)
    public boolean isVintage() {
        return getVehicleAge() > 15;
    }

    // Method to check if vehicle is recent (less than 3 years old)
    public boolean isRecent() {
        return getVehicleAge() < 3;
    }

    // toString method for easy display
    @Override
    public String toString() {
        return String.format("%s %s (%d) - %s", make, model, year, status);
    }

    // equals method for comparing vehicles
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Vehicle vehicle = (Vehicle) obj;
        return vehicleId == vehicle.vehicleId;
    }

    // hashCode method
    @Override
    public int hashCode() {
        return Integer.hashCode(vehicleId);
    }
}