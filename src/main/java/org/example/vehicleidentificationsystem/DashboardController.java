package org.example.vehicleidentificationsystem;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.effect.DropShadow;
import javafx.util.Duration;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.application.Platform;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DashboardController {

    @FXML private Button loadButton;
    @FXML private Button refreshButton;
    @FXML private Button exportButton;
    @FXML private ProgressIndicator progressIndicator;
    @FXML private ProgressBar progressBar;
    @FXML private Pagination pagination;
    @FXML private TableView<Vehicle> vehicleTable;
    @FXML private ScrollPane vehicleScrollPane;
    @FXML private Label statusLabel;
    @FXML private Label totalVehiclesLabel;
    @FXML private Label newVehiclesLabel;
    @FXML private Label insuranceLabel;

    @FXML private TableColumn<Vehicle, Integer> colVehicleId;
    @FXML private TableColumn<Vehicle, String> colRegistrationNo;
    @FXML private TableColumn<Vehicle, String> colMake;
    @FXML private TableColumn<Vehicle, String> colModel;
    @FXML private TableColumn<Vehicle, Integer> colYear;
    @FXML private TableColumn<Vehicle, String> colStatus;

    @FXML private MenuItem exitMenuItem;
    @FXML private MenuItem customerMenuItem;
    @FXML private MenuItem vehicleMenuItem;
    @FXML private MenuItem workshopMenuItem;
    @FXML private MenuItem insuranceMenuItem;
    @FXML private MenuItem policeMenuItem;
    @FXML private MenuItem aboutMenuItem;
    @FXML private MenuItem reportsMenuItem;

    private ObservableList<Vehicle> vehicleList = FXCollections.observableArrayList();
    private DatabaseConnection dbConnection;
    private static final int ROWS_PER_PAGE = 5;

    @FXML
    public void initialize() {
        updateStatus("Initializing application...");
        dbConnection = new DatabaseConnection();
        updateStatus("Database connection established");

        setupTableColumns();
        setupUIEffects();
        setupMenuActions();
        setupPagination();
        loadVehicleData();
        loadDashboardStatistics();
        startRealTimeUpdates();

        updateStatus("Ready");
    }

    private void setupTableColumns() {
        colVehicleId.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        colRegistrationNo.setCellValueFactory(new PropertyValueFactory<>("registrationNumber"));
        colMake.setCellValueFactory(new PropertyValueFactory<>("make"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void setupUIEffects() {
        progressIndicator.setVisible(false);
        progressBar.setVisible(false);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(15);
        dropShadow.setOffsetX(5);
        dropShadow.setOffsetY(5);
        dropShadow.setColor(javafx.scene.paint.Color.rgb(0, 0, 0, 0.5));
        loadButton.setEffect(dropShadow);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1.5), loadButton);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.4);
        fadeTransition.setCycleCount(FadeTransition.INDEFINITE);
        fadeTransition.setAutoReverse(true);
        fadeTransition.play();

        loadButton.setOnAction(event -> refreshAllData());

        if (refreshButton != null) {
            refreshButton.setOnAction(event -> refreshAllData());
        }
        if (exportButton != null) {
            exportButton.setOnAction(event -> exportDashboardReport());
        }
    }

    private void setupMenuActions() {
        if (exitMenuItem != null) {
            exitMenuItem.setOnAction(e -> Platform.exit());
        }
        if (customerMenuItem != null) {
            customerMenuItem.setOnAction(e -> openModule("CustomerManagement.fxml", "Customer Management"));
        }
        if (vehicleMenuItem != null) {
            vehicleMenuItem.setOnAction(e -> openModule("VehicleManagement.fxml", "Vehicle Management"));
        }
        if (workshopMenuItem != null) {
            workshopMenuItem.setOnAction(e -> openModule("WorkshopServices.fxml", "Workshop Services"));
        }
        if (insuranceMenuItem != null) {
            insuranceMenuItem.setOnAction(e -> openModule("InsuranceTracking.fxml", "Insurance Tracking"));
        }
        if (policeMenuItem != null) {
            policeMenuItem.setOnAction(e -> openModule("PoliceRecords.fxml", "Police Records"));
        }
        if (aboutMenuItem != null) {
            aboutMenuItem.setOnAction(e -> showAboutDialog());
        }
        if (reportsMenuItem != null) {
            reportsMenuItem.setOnAction(e -> generateComprehensiveReport());
        }
    }

    private void setupPagination() {
        int totalPages = (int) Math.ceil((double) vehicleList.size() / ROWS_PER_PAGE);
        pagination.setPageCount(Math.max(totalPages, 1));
        pagination.setCurrentPageIndex(0);

        pagination.setPageFactory(pageIndex -> {
            VBox box = new VBox();
            int fromIndex = pageIndex * ROWS_PER_PAGE;
            int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, vehicleList.size());

            if (fromIndex < vehicleList.size()) {
                List<Vehicle> pageVehicles = new ArrayList<>(vehicleList.subList(fromIndex, toIndex));
                Platform.runLater(() -> {
                    vehicleTable.setItems(FXCollections.observableArrayList(pageVehicles));
                    updateStatus("Showing page " + (pageIndex + 1) + " of " + totalPages);
                });
            }
            return box;
        });
    }

    private void loadDashboardStatistics() {
        new Thread(() -> {
            try {
                ResultSet rs = dbConnection.executeQuery("SELECT COUNT(*) FROM Vehicle");
                if (rs.next()) {
                    int total = rs.getInt(1);
                    Platform.runLater(() -> totalVehiclesLabel.setText(String.valueOf(total)));
                }

                rs = dbConnection.executeQuery("SELECT COUNT(*) FROM Vehicle WHERE year >= 2024");
                if (rs.next()) {
                    int newCount = rs.getInt(1);
                    Platform.runLater(() -> newVehiclesLabel.setText(String.valueOf(newCount)));
                }

                rs = dbConnection.executeQuery("SELECT COUNT(*) FROM InsurancePolicy WHERE status = 'Active'");
                if (rs.next()) {
                    int insuranceCount = rs.getInt(1);
                    Platform.runLater(() -> insuranceLabel.setText(String.valueOf(insuranceCount)));
                }
            } catch (SQLException e) {
                Platform.runLater(() -> updateStatus("Stats error: " + e.getMessage()));
            }
        }).start();
    }

    private void startRealTimeUpdates() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.minutes(1), e -> loadDashboardStatistics()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void refreshAllData() {
        loadVehicleData();
        loadDashboardStatistics();
        updateStatus("All data refreshed");
        showAlert("Refresh Complete", "Dashboard data has been updated.");
    }

    @FXML private void refreshData() { refreshAllData(); }
    @FXML private void exportData() { exportDashboardReport(); }

    private void exportDashboardReport() {
        try {
            String filename = "dashboard_report_" + System.currentTimeMillis() + ".txt";
            java.io.FileWriter writer = new java.io.FileWriter(filename);
            writer.write("VEHICLE IDENTIFICATION SYSTEM REPORT\n");
            writer.write("Total Vehicles: " + totalVehiclesLabel.getText() + "\n");
            writer.write("New Vehicles: " + newVehiclesLabel.getText() + "\n");
            writer.close();
            showAlert("Export Successful", "Report saved to " + filename);
        } catch (Exception e) {
            showAlert("Export Failed", e.getMessage());
        }
    }

    private void generateComprehensiveReport() {
        showAlert("Report", "Comprehensive report generated!");
    }

    private void openModule(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/vehicleidentificationsystem/" + fxmlFile));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            showAlert("Error", "Could not open module: " + fxmlFile);
        }
    }

    private void loadVehicleData() {
        try {
            vehicleList.clear();
            ResultSet rs = dbConnection.executeQuery("SELECT * FROM Vehicle LIMIT 20");
            while (rs.next()) {
                Vehicle v = new Vehicle(
                        rs.getInt("vehicle_id"),
                        rs.getString("registration_number"),
                        rs.getString("make"),
                        rs.getString("model"),
                        rs.getInt("year")
                );
                v.setStatus(getVehicleStatus(rs.getInt("year")));
                vehicleList.add(v);
            }
            vehicleTable.setItems(vehicleList);
            setupPagination();
        } catch (SQLException e) {
            loadDummyData();
        }
    }

    private void loadDummyData() {
        vehicleList.clear();
        for (int i = 1; i <= 20; i++) {
            vehicleList.add(new Vehicle(i, "REG" + i, "Toyota", "Camry", 2020 + (i % 5)));
        }
        vehicleTable.setItems(vehicleList);
        setupPagination();
    }

    private String getVehicleStatus(int year) {
        int currentYear = LocalDate.now().getYear();
        if (year == currentYear) return "New";
        if (year >= currentYear - 2) return "Like New";
        if (year >= currentYear - 5) return "Good";
        return "Old";
    }

    private void updateStatus(String message) {
        Platform.runLater(() -> {
            if (statusLabel != null) statusLabel.setText(message);
            System.out.println(message);
        });
    }

    private void showAboutDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setContentText("Vehicle Identification System v2.0\nOOP2 Project");
        alert.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}