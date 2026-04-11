package org.example.vehicleidentificationsystem;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

public class CustomerManagementController {

    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, Integer> colCustomerId;
    @FXML private TableColumn<Customer, String> colName;
    @FXML private TableColumn<Customer, String> colPhone;
    @FXML private TableColumn<Customer, String> colEmail;
    @FXML private TableColumn<Customer, String> colAddress;
    @FXML private TableColumn<Customer, String> colLicense;
    @FXML private TableColumn<Customer, Integer> colLoyaltyPoints;

    @FXML private TextField searchField;
    @FXML private Label statusLabel;
    @FXML private Label totalCustomersLabel;

    private ObservableList<Customer> customerList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        updateStatus("Customer Management module loaded");
        loadSampleData();
    }

    // FIXED: Updated to use correct constructor (id, name, phone, email)
    private void loadSampleData() {
        customerList.clear();
        // Using the 4-parameter constructor that matches your Customer class
        customerList.add(new Customer(1, "John Smith", "555-0101", "john@email.com"));
        customerList.add(new Customer(2, "Sarah Johnson", "555-0102", "sarah@email.com"));
        customerList.add(new Customer(3, "Michael Brown", "555-0103", "michael@email.com"));
        customerList.add(new Customer(4, "Emily Davis", "555-0104", "emily@email.com"));
        customerList.add(new Customer(5, "David Wilson", "555-0105", "david@email.com"));

        customerTable.setItems(customerList);
        totalCustomersLabel.setText(String.valueOf(customerList.size()));
    }

    @FXML
    private void searchCustomers() {
        updateStatus("Searching: " + searchField.getText());
    }

    @FXML
    private void showAddDialog() {
        updateStatus("Add customer feature coming soon");
    }

    @FXML
    private void editCustomer() {
        updateStatus("Edit customer feature coming soon");
    }

    @FXML
    private void deleteCustomer() {
        updateStatus("Delete customer feature coming soon");
    }

    @FXML
    private void viewCustomerVehicles() {
        updateStatus("View vehicles feature coming soon");
    }

    @FXML
    private void viewCustomerQueries() {
        updateStatus("View queries feature coming soon");
    }

    @FXML
    private void exportCustomers() {
        updateStatus("Export feature coming soon");
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) customerTable.getScene().getWindow();
        stage.close();
    }

    private void updateStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
        System.out.println(message);
    }
}