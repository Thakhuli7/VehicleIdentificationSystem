package org.example.vehicleidentificationsystem;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.*;

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
    private DatabaseConnection dbConnection;

    @FXML
    public void initialize() {
        dbConnection = new DatabaseConnection();

        // Set up table columns
        colVehicleId.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        colRegistration.setCellValueFactory(new PropertyValueFactory<>("registrationNumber"));
        colMake.setCellValueFactory(new PropertyValueFactory<>("make"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        colMileage.setCellValueFactory(new PropertyValueFactory<>("mileage"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Setup filter combo
        filterCombo.getItems().addAll("All", "Active", "Inactive", "Needs Service");
        filterCombo.setValue("All");
        filterCombo.setOnAction(e -> filterVehicles());

        updateStatus("Vehicle Management module loaded");
        loadVehiclesFromDatabase();
    }

    private void loadVehiclesFromDatabase() {
        try {
            vehicleList.clear();
            String query = "SELECT * FROM Vehicle ORDER BY vehicle_id";
            ResultSet rs = dbConnection.executeQuery(query);

            int count = 0;
            while (rs.next()) {
                Vehicle v = new Vehicle(
                        rs.getInt("vehicle_id"),
                        rs.getString("registration_number"),
                        rs.getString("make"),
                        rs.getString("model"),
                        rs.getInt("year")
                );
                v.setColor(rs.getString("color"));
                v.setMileage(rs.getInt("mileage"));
                v.setStatus(rs.getString("status"));
                vehicleList.add(v);
                count++;
            }

            vehicleTable.setItems(vehicleList);
            updateStatistics();
            updateStatus("Loaded " + count + " vehicles from database");

        } catch (SQLException e) {
            updateStatus("Database error: " + e.getMessage());
            loadSampleVehicles();
        }
    }

    private void loadSampleVehicles() {
        vehicleList.clear();

        vehicleList.add(new Vehicle(1, "ABC123", "Toyota", "Camry", 2024));
        vehicleList.add(new Vehicle(2, "XYZ789", "Honda", "Civic", 2023));
        vehicleList.add(new Vehicle(3, "DEF456", "Ford", "Mustang", 2022));
        vehicleList.add(new Vehicle(4, "GHI789", "BMW", "X5", 2024));
        vehicleList.add(new Vehicle(5, "JKL012", "Mercedes", "C-Class", 2023));

        vehicleTable.setItems(vehicleList);
        updateStatistics();
        updateStatus("Loaded " + vehicleList.size() + " sample vehicles");
    }

    private void filterVehicles() {
        String filter = filterCombo.getValue();
        if (filter == null || filter.equals("All")) {
            vehicleTable.setItems(vehicleList);
            return;
        }

        ObservableList<Vehicle> filtered = FXCollections.observableArrayList();
        for (Vehicle v : vehicleList) {
            if (filter.equals("Active") && "Active".equals(v.getStatus())) {
                filtered.add(v);
            } else if (filter.equals("Needs Service") && v.getMileage() > 30000) {
                filtered.add(v);
            } else if (filter.equals("Inactive") && !"Active".equals(v.getStatus())) {
                filtered.add(v);
            }
        }
        vehicleTable.setItems(filtered);
        updateStatus("Showing " + filtered.size() + " vehicles filtered by: " + filter);
    }

    private void updateStatistics() {
        int total = vehicleList.size();
        int active = 0;
        int needsService = 0;

        for (Vehicle v : vehicleList) {
            if ("Active".equals(v.getStatus())) {
                active++;
            }
            if (v.getMileage() > 30000) {
                needsService++;
            }
        }

        totalVehiclesCard.setText(String.valueOf(total));
        activeVehiclesCard.setText(String.valueOf(active));
        needsServiceCard.setText(String.valueOf(needsService));
    }

    @FXML
    private void searchVehicles() {
        String keyword = searchField.getText().toLowerCase();
        if (keyword.isEmpty()) {
            loadVehiclesFromDatabase();
            return;
        }

        ObservableList<Vehicle> filtered = FXCollections.observableArrayList();
        for (Vehicle v : vehicleList) {
            if (v.getRegistrationNumber().toLowerCase().contains(keyword) ||
                    v.getMake().toLowerCase().contains(keyword) ||
                    v.getModel().toLowerCase().contains(keyword)) {
                filtered.add(v);
            }
        }
        vehicleTable.setItems(filtered);
        updateStatus("Found " + filtered.size() + " vehicles matching '" + keyword + "'");
    }

    @FXML
    private void showAddDialog() {
        updateStatus("Add vehicle feature coming soon");
    }

    @FXML
    private void editVehicle() {
        Vehicle selected = vehicleTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            updateStatus("Edit vehicle: " + selected.getRegistrationNumber());
        } else {
            showAlert("No Selection", "Please select a vehicle to edit.");
        }
    }

    @FXML
    private void deleteVehicle() {
        Vehicle selected = vehicleTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Delete Vehicle");
            confirm.setHeaderText("Delete " + selected.getRegistrationNumber() + "?");
            confirm.setContentText("This action cannot be undone.");

            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    vehicleList.remove(selected);
                    vehicleTable.setItems(vehicleList);
                    updateStatistics();
                    updateStatus("Deleted vehicle: " + selected.getRegistrationNumber());
                }
            });
        } else {
            showAlert("No Selection", "Please select a vehicle to delete.");
        }
    }

    @FXML
    private void viewServiceHistory() {
        Vehicle selected = vehicleTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            updateStatus("View service history for: " + selected.getRegistrationNumber());
            showAlert("Service History", "Service records for " + selected.getRegistrationNumber());
        } else {
            showAlert("No Selection", "Please select a vehicle.");
        }
    }

    @FXML
    private void viewInsurance() {
        Vehicle selected = vehicleTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            updateStatus("View insurance for: " + selected.getRegistrationNumber());
            showAlert("Insurance Info", "Insurance details for " + selected.getRegistrationNumber());
        } else {
            showAlert("No Selection", "Please select a vehicle.");
        }
    }

    @FXML
    private void viewPoliceRecords() {
        Vehicle selected = vehicleTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            updateStatus("View police records for: " + selected.getRegistrationNumber());
            showAlert("Police Records", "Police records for " + selected.getRegistrationNumber());
        } else {
            showAlert("No Selection", "Please select a vehicle.");
        }
    }

    @FXML
    private void exportVehicles() {
        try {
            StringBuilder data = new StringBuilder();
            data.append("ID,Registration,Make,Model,Year,Color,Mileage,Status\n");

            for (Vehicle v : vehicleTable.getItems()) {
                data.append(v.getVehicleId()).append(",")
                        .append(v.getRegistrationNumber()).append(",")
                        .append(v.getMake()).append(",")
                        .append(v.getModel()).append(",")
                        .append(v.getYear()).append(",")
                        .append(v.getColor()).append(",")
                        .append(v.getMileage()).append(",")
                        .append(v.getStatus()).append("\n");
            }

            String filename = "vehicles_export_" + System.currentTimeMillis() + ".csv";
            java.io.FileWriter writer = new java.io.FileWriter(filename);
            writer.write(data.toString());
            writer.close();

            showAlert("Export Successful", "Data exported to " + filename);
            updateStatus("Exported " + vehicleTable.getItems().size() + " vehicles to CSV");

        } catch (Exception e) {
            showAlert("Export Failed", e.getMessage());
        }
    }

    @FXML
    private void closeWindow() {
        javafx.stage.Stage stage = (javafx.stage.Stage) vehicleTable.getScene().getWindow();
        stage.close();
    }

    private void updateStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
        System.out.println(message);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}