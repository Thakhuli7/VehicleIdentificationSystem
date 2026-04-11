package org.example.vehicleidentificationsystem;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

public class PoliceRecordsController {

    @FXML private TableView<Violation> violationTable;
    @FXML private TableColumn<Violation, Integer> colId;
    @FXML private TableColumn<Violation, String> colVehicleReg;
    @FXML private TableColumn<Violation, String> colViolationType;
    @FXML private TableColumn<Violation, Double> colFine;
    @FXML private TableColumn<Violation, String> colStatus;

    @FXML
    public void initialize() {
        // Set up cell value factories
        colId.setCellValueFactory(new PropertyValueFactory<>("violationId"));
        colVehicleReg.setCellValueFactory(new PropertyValueFactory<>("vehicleRegNumber"));
        colViolationType.setCellValueFactory(new PropertyValueFactory<>("violationType"));
        colFine.setCellValueFactory(new PropertyValueFactory<>("fineAmount"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadSampleData();
    }

    private void loadSampleData() {
        ObservableList<Violation> violations = FXCollections.observableArrayList();

        Violation v1 = new Violation();
        v1.setViolationId(1);
        v1.setVehicleRegNumber("ABC123");
        v1.setViolationType("Speeding");
        v1.setFineAmount(150.00);
        v1.setStatus("Unpaid");

        Violation v2 = new Violation();
        v2.setViolationId(2);
        v2.setVehicleRegNumber("XYZ789");
        v2.setViolationType("Parking");
        v2.setFineAmount(45.00);
        v2.setStatus("Paid");

        violations.addAll(v1, v2);
        violationTable.setItems(violations);
    }
}