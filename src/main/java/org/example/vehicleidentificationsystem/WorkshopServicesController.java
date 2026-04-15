package org.example.vehicleidentificationsystem;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.*;
import java.time.LocalDate;

public class WorkshopServicesController {

    @FXML private TableView<ServiceRecord> serviceTable;
    @FXML private TableColumn<ServiceRecord, Integer> colServiceId;
    @FXML private TableColumn<ServiceRecord, String> colVehicleReg;
    @FXML private TableColumn<ServiceRecord, String> colServiceDate;
    @FXML private TableColumn<ServiceRecord, String> colServiceType;
    @FXML private TableColumn<ServiceRecord, String> colDescription;
    @FXML private TableColumn<ServiceRecord, Double> colCost;
    @FXML private TableColumn<ServiceRecord, String> colMechanic;
    @FXML private TableColumn<ServiceRecord, String> colNextService;

    @FXML private TextField searchField;
    @FXML private Label statusLabel;
    @FXML private Label totalRecordsLabel;

    private ObservableList<ServiceRecord> serviceList = FXCollections.observableArrayList();
    private DatabaseConnection dbConnection;

    @FXML
    public void initialize() {
        dbConnection = new DatabaseConnection();

        // Set up table columns
        colServiceId.setCellValueFactory(new PropertyValueFactory<>("serviceId"));
        colVehicleReg.setCellValueFactory(new PropertyValueFactory<>("vehicleRegNumber"));
        colServiceDate.setCellValueFactory(new PropertyValueFactory<>("serviceDate"));
        colServiceType.setCellValueFactory(new PropertyValueFactory<>("serviceType"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        colMechanic.setCellValueFactory(new PropertyValueFactory<>("mechanicName"));
        colNextService.setCellValueFactory(new PropertyValueFactory<>("nextServiceDate"));

        updateStatus("Workshop Services module loaded");
        loadServiceRecords();
    }

    private void loadServiceRecords() {
        try {
            serviceList.clear();
            String query = "SELECT s.service_id, s.service_date, s.service_type, s.description, " +
                    "s.cost, s.mechanic_name, s.next_service_date, v.registration_number " +
                    "FROM ServiceRecord s " +
                    "JOIN Vehicle v ON s.vehicle_id = v.vehicle_id " +
                    "ORDER BY s.service_date DESC";
            ResultSet rs = dbConnection.executeQuery(query);

            int count = 0;
            while (rs.next()) {
                ServiceRecord record = new ServiceRecord(
                        rs.getInt("service_id"),
                        0, // vehicle_id (not displayed)
                        rs.getDate("service_date") != null ? rs.getDate("service_date").toLocalDate() : null,
                        rs.getString("service_type"),
                        rs.getString("description"),
                        rs.getDouble("cost"),
                        rs.getString("mechanic_name"),
                        rs.getDate("next_service_date") != null ? rs.getDate("next_service_date").toLocalDate() : null,
                        "",
                        ""
                );
                record.setVehicleRegNumber(rs.getString("registration_number"));
                serviceList.add(record);
                count++;
            }

            serviceTable.setItems(serviceList);
            totalRecordsLabel.setText(String.valueOf(count));
            updateStatus("Loaded " + count + " service records from database");

        } catch (SQLException e) {
            updateStatus("Database error: " + e.getMessage());
            loadSampleRecords();
        }
    }

    private void loadSampleRecords() {
        serviceList.clear();

        // Sample data for testing
        ServiceRecord s1 = new ServiceRecord(1, 1, LocalDate.of(2024, 3, 15), "Oil Change",
                "Regular oil change and filter", 89.99, "Tom Wilson", LocalDate.of(2024, 9, 15), "", "");
        s1.setVehicleRegNumber("ABC123");

        ServiceRecord s2 = new ServiceRecord(2, 1, LocalDate.of(2024, 6, 20), "Tire Rotation",
                "Rotated all 4 tires", 49.99, "Tom Wilson", LocalDate.of(2024, 12, 20), "", "");
        s2.setVehicleRegNumber("ABC123");

        ServiceRecord s3 = new ServiceRecord(3, 2, LocalDate.of(2024, 2, 10), "Brake Service",
                "Front brake pads replaced", 299.99, "Alice Chen", LocalDate.of(2024, 8, 10), "", "");
        s3.setVehicleRegNumber("XYZ789");

        serviceList.addAll(s1, s2, s3);
        serviceTable.setItems(serviceList);
        totalRecordsLabel.setText(String.valueOf(serviceList.size()));
        updateStatus("Loaded " + serviceList.size() + " sample records");
    }

    @FXML
    private void searchServices() {
        String keyword = searchField.getText().toLowerCase();
        if (keyword.isEmpty()) {
            loadServiceRecords();
            return;
        }

        ObservableList<ServiceRecord> filtered = FXCollections.observableArrayList();
        for (ServiceRecord s : serviceList) {
            if (s.getVehicleRegNumber().toLowerCase().contains(keyword) ||
                    s.getServiceType().toLowerCase().contains(keyword) ||
                    s.getMechanicName().toLowerCase().contains(keyword)) {
                filtered.add(s);
            }
        }
        serviceTable.setItems(filtered);
        updateStatus("Found " + filtered.size() + " records matching '" + keyword + "'");
    }

    @FXML
    private void showAddDialog() {
        updateStatus("Add service record feature coming soon");
    }

    @FXML
    private void editService() {
        ServiceRecord selected = serviceTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            updateStatus("Edit service record: " + selected.getServiceId());
        } else {
            showAlert("No Selection", "Please select a service record to edit.");
        }
    }

    @FXML
    private void deleteService() {
        ServiceRecord selected = serviceTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Delete Service Record");
            confirm.setHeaderText("Delete service record #" + selected.getServiceId() + "?");
            confirm.setContentText("This action cannot be undone.");

            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    serviceList.remove(selected);
                    serviceTable.setItems(serviceList);
                    totalRecordsLabel.setText(String.valueOf(serviceList.size()));
                    updateStatus("Deleted service record: " + selected.getServiceId());
                }
            });
        } else {
            showAlert("No Selection", "Please select a service record to delete.");
        }
    }

    @FXML
    private void viewInvoice() {
        ServiceRecord selected = serviceTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            updateStatus("View invoice for service #" + selected.getServiceId());
            showAlert("Invoice", "Service Invoice #" + selected.getServiceId() +
                    "\nVehicle: " + selected.getVehicleRegNumber() +
                    "\nService: " + selected.getServiceType() +
                    "\nDate: " + selected.getServiceDate() +
                    "\nCost: $" + selected.getCost() +
                    "\nMechanic: " + selected.getMechanicName());
        } else {
            showAlert("No Selection", "Please select a service record to view invoice.");
        }
    }

    @FXML
    private void showReminders() {
        int dueCount = 0;
        for (ServiceRecord s : serviceList) {
            if (s.getNextServiceDate() != null &&
                    s.getNextServiceDate().isBefore(LocalDate.now().plusDays(30))) {
                dueCount++;
            }
        }
        showAlert("Service Reminders", dueCount + " vehicles need service within the next 30 days.");
        updateStatus("Service reminders: " + dueCount + " vehicles due soon");
    }

    @FXML
    private void exportServices() {
        try {
            StringBuilder data = new StringBuilder();
            data.append("ID,Vehicle Reg,Service Date,Service Type,Description,Cost,Mechanic,Next Service\n");

            for (ServiceRecord s : serviceTable.getItems()) {
                data.append(s.getServiceId()).append(",")
                        .append(s.getVehicleRegNumber()).append(",")
                        .append(s.getServiceDate()).append(",")
                        .append(s.getServiceType()).append(",")
                        .append(s.getDescription()).append(",")
                        .append(s.getCost()).append(",")
                        .append(s.getMechanicName()).append(",")
                        .append(s.getNextServiceDate()).append("\n");
            }

            String filename = "services_export_" + System.currentTimeMillis() + ".csv";
            java.io.FileWriter writer = new java.io.FileWriter(filename);
            writer.write(data.toString());
            writer.close();

            showAlert("Export Successful", "Data exported to " + filename);
            updateStatus("Exported " + serviceTable.getItems().size() + " records to CSV");

        } catch (Exception e) {
            showAlert("Export Failed", e.getMessage());
        }
    }

    @FXML
    private void closeWindow() {
        javafx.stage.Stage stage = (javafx.stage.Stage) serviceTable.getScene().getWindow();
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