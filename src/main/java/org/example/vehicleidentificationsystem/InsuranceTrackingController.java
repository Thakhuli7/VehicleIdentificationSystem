package org.example.vehicleidentificationsystem;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.*;

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

    private ObservableList<InsurancePolicy> policyList = FXCollections.observableArrayList();
    private ObservableList<Claim> claimList = FXCollections.observableArrayList();
    private DatabaseConnection dbConnection;

    @FXML
    public void initialize() {
        dbConnection = new DatabaseConnection();

        // Set up policy table columns
        colPolicyId.setCellValueFactory(new PropertyValueFactory<>("policyId"));
        colVehicleReg.setCellValueFactory(new PropertyValueFactory<>("vehicleRegNumber"));
        colCompany.setCellValueFactory(new PropertyValueFactory<>("insuranceCompany"));
        colPolicyNumber.setCellValueFactory(new PropertyValueFactory<>("policyNumber"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        colPremium.setCellValueFactory(new PropertyValueFactory<>("premiumAmount"));
        colPolicyStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Set up claim table columns
        colClaimId.setCellValueFactory(new PropertyValueFactory<>("claimId"));
        colPolicyNum.setCellValueFactory(new PropertyValueFactory<>("policyNumber"));
        colClaimDate.setCellValueFactory(new PropertyValueFactory<>("claimDate"));
        colClaimAmount.setCellValueFactory(new PropertyValueFactory<>("claimAmount"));
        colApprovedAmount.setCellValueFactory(new PropertyValueFactory<>("approvedAmount"));
        colClaimStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colClaimType.setCellValueFactory(new PropertyValueFactory<>("claimType"));

        updateStatus("Insurance Tracking module loaded");
        loadPolicies();
        loadClaims();
        loadExpiryAlerts();
    }

    private void loadPolicies() {
        try {
            policyList.clear();
            String query = "SELECT p.policy_id, p.insurance_company, p.policy_number, " +
                    "p.start_date, p.end_date, p.premium_amount, p.policy_type, p.status, " +
                    "v.registration_number " +
                    "FROM InsurancePolicy p " +
                    "JOIN Vehicle v ON p.vehicle_id = v.vehicle_id " +
                    "ORDER BY p.policy_id";
            ResultSet rs = dbConnection.executeQuery(query);

            while (rs.next()) {
                InsurancePolicy policy = new InsurancePolicy();
                policy.setPolicyId(rs.getInt("policy_id"));
                policy.setVehicleRegNumber(rs.getString("registration_number"));
                policy.setInsuranceCompany(rs.getString("insurance_company"));
                policy.setPolicyNumber(rs.getString("policy_number"));
                policy.setStartDate(rs.getDate("start_date").toLocalDate());
                policy.setEndDate(rs.getDate("end_date").toLocalDate());
                policy.setPremiumAmount(rs.getDouble("premium_amount"));
                policy.setStatus(rs.getString("status"));
                policyList.add(policy);
            }

            policyTable.setItems(policyList);
            updateStatus("Loaded " + policyList.size() + " insurance policies");

        } catch (SQLException e) {
            updateStatus("Database error: " + e.getMessage());
        }
    }

    private void loadClaims() {
        try {
            claimList.clear();
            String query = "SELECT c.claim_id, c.claim_date, c.claim_amount, " +
                    "c.approved_amount, c.status, c.claim_type, p.policy_number " +
                    "FROM Claim c " +
                    "JOIN InsurancePolicy p ON c.policy_id = p.policy_id " +
                    "ORDER BY c.claim_date DESC";
            ResultSet rs = dbConnection.executeQuery(query);

            while (rs.next()) {
                Claim claim = new Claim();
                claim.setClaimId(rs.getInt("claim_id"));
                claim.setPolicyNumber(rs.getString("policy_number"));
                claim.setClaimDate(rs.getDate("claim_date").toLocalDate());
                claim.setClaimAmount(rs.getDouble("claim_amount"));
                claim.setApprovedAmount(rs.getDouble("approved_amount"));
                claim.setStatus(rs.getString("status"));
                claim.setClaimType(rs.getString("claim_type"));
                claimList.add(claim);
            }

            claimTable.setItems(claimList);
            updateStatus("Loaded " + claimList.size() + " claims");

        } catch (SQLException e) {
            updateStatus("Database error: " + e.getMessage());
        }
    }

    private void loadExpiryAlerts() {
        expiryListView.getItems().clear();
        try {
            String query = "SELECT v.registration_number, p.end_date, p.insurance_company " +
                    "FROM InsurancePolicy p " +
                    "JOIN Vehicle v ON p.vehicle_id = v.vehicle_id " +
                    "WHERE p.end_date BETWEEN CURRENT_DATE AND CURRENT_DATE + INTERVAL '30 days'";
            ResultSet rs = dbConnection.executeQuery(query);

            while (rs.next()) {
                String alert = "⚠️ Vehicle " + rs.getString("registration_number") +
                        " - Insurance expires on " + rs.getDate("end_date") +
                        " (" + rs.getString("insurance_company") + ")";
                expiryListView.getItems().add(alert);
            }

            if (expiryListView.getItems().isEmpty()) {
                expiryListView.getItems().add("✅ No insurance policies expiring soon");
            }

        } catch (SQLException e) {
            expiryListView.getItems().add("⚠️ Unable to load expiry alerts");
        }
    }

    @FXML
    private void searchPolicies() {
        String keyword = policySearchField.getText().toLowerCase();
        if (keyword.isEmpty()) {
            policyTable.setItems(policyList);
            return;
        }

        ObservableList<InsurancePolicy> filtered = FXCollections.observableArrayList();
        for (InsurancePolicy p : policyList) {
            if (p.getVehicleRegNumber().toLowerCase().contains(keyword) ||
                    p.getInsuranceCompany().toLowerCase().contains(keyword) ||
                    p.getPolicyNumber().toLowerCase().contains(keyword)) {
                filtered.add(p);
            }
        }
        policyTable.setItems(filtered);
        updateStatus("Found " + filtered.size() + " policies matching '" + keyword + "'");
    }

    @FXML
    private void searchClaims() {
        String keyword = claimSearchField.getText().toLowerCase();
        if (keyword.isEmpty()) {
            claimTable.setItems(claimList);
            return;
        }

        ObservableList<Claim> filtered = FXCollections.observableArrayList();
        for (Claim c : claimList) {
            if (c.getPolicyNumber().toLowerCase().contains(keyword) ||
                    c.getClaimType().toLowerCase().contains(keyword) ||
                    c.getStatus().toLowerCase().contains(keyword)) {
                filtered.add(c);
            }
        }
        claimTable.setItems(filtered);
        updateStatus("Found " + filtered.size() + " claims matching '" + keyword + "'");
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
        javafx.stage.Stage stage = (javafx.stage.Stage) policyTable.getScene().getWindow();
        stage.close();
    }

    private void updateStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
        System.out.println(message);
    }
}