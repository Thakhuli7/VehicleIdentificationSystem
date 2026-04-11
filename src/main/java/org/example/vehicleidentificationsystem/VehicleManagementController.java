package org.example.vehicleidentificationsystem;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

public class VehicleManagementController {

    @FXML private TableView<Vehicle> vehicleTable;
    @FXML private TableColumn<Vehicle, Integer> colVehicleId;
    @FXML private TableColumn<Vehicle, String> colRegistration;
    @FXML private TableColumn<Vehicle, String> colMake;
    @FXML private TableColumn<Vehicle, String> colModel;
    @FXML private TableColumn<Vehicle, Integer> colYear;
    @FXML private TableColumn<Vehicle, String> colColor;
    @FXML private TableColumn<Vehicle, Integer> colMileage;
    @FXML private TableColumn<Vehicle, String> colStatus;

    @FXML private TextField searchField;
    @FXML private ComboBox<String> filterCombo;
    @FXML private Label statusLabel;
    @FXML private Label totalVehiclesCard;
    @FXML private Label activeVehiclesCard;
    @FXML private Label needsServiceCard;

    private ObservableList<Vehicle> vehicleList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        updateStatus("Vehicle Management module loaded");
        loadSampleData();
    }

    private void loadSampleData() {
        vehicleList.clear();
        vehicleList.add(new Vehicle(1, "ABC123", "Toyota", "Camry", 2024));
        vehicleList.add(new Vehicle(2, "XYZ789", "Honda", "Civic", 2023));
        vehicleList.add(new Vehicle(3, "DEF456", "Ford", "Mustang", 2022));
        vehicleTable.setItems(vehicleList);
        totalVehiclesCard.setText(String.valueOf(vehicleList.size()));
        activeVehiclesCard.setText(String.valueOf(vehicleList.size()));
        needsServiceCard.setText("0");
    }

    @FXML
    private void searchVehicles() {
        updateStatus("Searching: " + searchField.getText());
    }

    @FXML
    private void showAddDialog() {
        updateStatus("Add vehicle feature coming soon");
    }

    @FXML
    private void editVehicle() {
        updateStatus("Edit vehicle feature coming soon");
    }

    @FXML
    private void deleteVehicle() {
        updateStatus("Delete vehicle feature coming soon");
    }

    @FXML
    private void viewServiceHistory() {
        updateStatus("Service history feature coming soon");
    }

    @FXML
    private void viewInsurance() {
        updateStatus("Insurance view feature coming soon");
    }

    @FXML
    private void viewPoliceRecords() {
        updateStatus("Police records feature coming soon");
    }

    @FXML
    private void exportVehicles() {
        updateStatus("Export feature coming soon");
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) vehicleTable.getScene().getWindow();
        stage.close();
    }

    private void updateStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
        System.out.println(message);
    }
}