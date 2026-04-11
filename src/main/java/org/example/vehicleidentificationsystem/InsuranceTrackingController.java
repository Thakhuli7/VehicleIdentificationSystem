package org.example.vehicleidentificationsystem;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

public class InsuranceTrackingController {

    @FXML private TableView<InsurancePolicy> policyTable;
    @FXML private TableColumn<InsurancePolicy, Integer> colPolicyId;
    @FXML private TableColumn<InsurancePolicy, String> colVehicleReg;
    @FXML private TableColumn<InsurancePolicy, String> colCompany;
    @FXML private TableColumn<InsurancePolicy, String> colPolicyNumber;
    @FXML private TableColumn<InsurancePolicy, String> colStartDate;
    @FXML private TableColumn<InsurancePolicy, String> colEndDate;
    @FXML private TableColumn<InsurancePolicy, Double> colPremium;
    @FXML private TableColumn<InsurancePolicy, String> colPolicyStatus;

    @FXML private TableView<Claim> claimTable;
    @FXML private TableColumn<Claim, Integer> colClaimId;
    @FXML private TableColumn<Claim, String> colPolicyNum;
    @FXML private TableColumn<Claim, String> colClaimDate;
    @FXML private TableColumn<Claim, Double> colClaimAmount;
    @FXML private TableColumn<Claim, Double> colApprovedAmount;
    @FXML private TableColumn<Claim, String> colClaimStatus;
    @FXML private TableColumn<Claim, String> colClaimType;

    @FXML private TextField policySearchField;
    @FXML private TextField claimSearchField;
    @FXML private Label statusLabel;
    @FXML private ListView<String> expiryListView;

    @FXML
    public void initialize() {
        updateStatus("Insurance Tracking module loaded");
        loadSampleExpiryAlerts();
    }

    private void loadSampleExpiryAlerts() {
        expiryListView.getItems().add("⚠️ Vehicle ABC123 - Insurance expires in 15 days");
        expiryListView.getItems().add("⚠️ Vehicle XYZ789 - Insurance expires in 22 days");
    }

    @FXML
    private void searchPolicies() {
        updateStatus("Searching policies: " + policySearchField.getText());
    }

    @FXML
    private void searchClaims() {
        updateStatus("Searching claims: " + claimSearchField.getText());
    }

    @FXML
    private void showAddPolicyDialog() {
        updateStatus("Add policy feature coming soon");
    }

    @FXML
    private void showAddClaimDialog() {
        updateStatus("Add claim feature coming soon");
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) policyTable.getScene().getWindow();
        stage.close();
    }

    private void updateStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
        System.out.println(message);
    }
}