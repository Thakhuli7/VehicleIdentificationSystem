package org.example.vehicleidentificationsystem;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

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

    @FXML
    public void initialize() {
        updateStatus("Workshop Services module loaded");
    }

    @FXML
    private void searchServices() {
        updateStatus("Searching: " + searchField.getText());
    }

    @FXML
    private void showAddDialog() {
        updateStatus("Add service record feature coming soon");
    }

    @FXML
    private void editService() {
        updateStatus("Edit service feature coming soon");
    }

    @FXML
    private void deleteService() {
        updateStatus("Delete service feature coming soon");
    }

    @FXML
    private void viewInvoice() {
        updateStatus("View invoice feature coming soon");
    }

    @FXML
    private void showReminders() {
        updateStatus("Service reminders feature coming soon");
    }

    @FXML
    private void exportServices() {
        updateStatus("Export feature coming soon");
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) serviceTable.getScene().getWindow();
        stage.close();
    }

    private void updateStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
        System.out.println(message);
    }
}