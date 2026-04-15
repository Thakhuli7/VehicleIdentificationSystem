package org.example.vehicleidentificationsystem;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.*;

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
    private DatabaseConnection dbConnection;

    @FXML
    public void initialize() {
        dbConnection = new DatabaseConnection();

        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colLicense.setCellValueFactory(new PropertyValueFactory<>("driverLicense"));
        colLoyaltyPoints.setCellValueFactory(new PropertyValueFactory<>("loyaltyPoints"));

        loadCustomers();
    }

    private void loadCustomers() {
        try {
            customerList.clear();
            String query = "SELECT customer_id, name, address, phone, email, driver_license, loyalty_points FROM Customer";
            ResultSet rs = dbConnection.executeQuery(query);

            while (rs.next()) {
                Customer c = new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("driver_license"),
                        null, null,
                        rs.getInt("loyalty_points")
                );
                customerList.add(c);
            }
            customerTable.setItems(customerList);
            totalCustomersLabel.setText(String.valueOf(customerList.size()));
            updateStatus("Loaded " + customerList.size() + " customers");
        } catch (SQLException e) {
            updateStatus("Database error: " + e.getMessage());
        }
    }

    @FXML private void searchCustomers() { updateStatus("Search: " + searchField.getText()); }
    @FXML private void showAddDialog() { updateStatus("Add customer - coming soon"); }
    @FXML private void editCustomer() { updateStatus("Edit customer - coming soon"); }
    @FXML private void deleteCustomer() { updateStatus("Delete customer - coming soon"); }
    @FXML private void viewCustomerVehicles() { updateStatus("View vehicles - coming soon"); }
    @FXML private void viewCustomerQueries() { updateStatus("View queries - coming soon"); }
    @FXML private void exportCustomers() { updateStatus("Export - coming soon"); }

    private void updateStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
        System.out.println(message);
    }
}