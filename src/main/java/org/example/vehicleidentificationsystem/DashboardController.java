package org.example.vehicleidentificationsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.effect.DropShadow;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.application.Platform;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DashboardController {

    // UI Components
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

    // Table Columns
    @FXML private TableColumn<Vehicle, Integer> colVehicleId;
    @FXML private TableColumn<Vehicle, String> colRegistrationNo;
    @FXML private TableColumn<Vehicle, String> colMake;
    @FXML private TableColumn<Vehicle, String> colModel;
    @FXML private TableColumn<Vehicle, Integer> colYear;
    @FXML private TableColumn<Vehicle, String> colStatus;

    // Menu Items
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

        DropShadow shadow = new DropShadow();
        shadow.setRadius(10);
        shadow.setOffsetX(3);
        shadow.setOffsetY(3);
        loadButton.setEffect(shadow);

        FadeTransition fade = new FadeTransition(Duration.seconds(1.5), loadButton);
        fade.setFromValue(1.0);
        fade.setToValue(0.4);
        fade.setCycleCount(FadeTransition.INDEFINITE);
        fade.setAutoReverse(true);
        fade.play();

        loadButton.setOnAction(event -> loadVehicleDataWithProgress());

        if (refreshButton != null) {
            refreshButton.setOnAction(event -> refreshData());
        }

        if (exportButton != null) {
            exportButton.setOnAction(event -> exportData());
        }
    }

    private void setupMenuActions() {
        if (exitMenuItem != null) {
            exitMenuItem.setOnAction(e -> {
                updateStatus("Exiting application...");
                Platform.exit();
            });
        }

        // UPDATED: Customer Module - Opens Customer Management Window
        if (customerMenuItem != null) {
            customerMenuItem.setOnAction(e -> openModule("CustomerManagement.fxml", "Customer Management"));
        }

        // UPDATED: Vehicle Module - Opens Vehicle Management Window
        if (vehicleMenuItem != null) {
            vehicleMenuItem.setOnAction(e -> openModule("VehicleManagement.fxml", "Vehicle Management"));
        }

        // UPDATED: Workshop Module - Opens Workshop Services Window
        if (workshopMenuItem != null) {
            workshopMenuItem.setOnAction(e -> openModule("WorkshopServices.fxml", "Workshop Services"));
        }

        // UPDATED: Insurance Module - Opens Insurance Tracking Window
        if (insuranceMenuItem != null) {
            insuranceMenuItem.setOnAction(e -> openModule("InsuranceTracking.fxml", "Insurance Tracking"));
        }

        // UPDATED: Police Module - Opens Police Records Window
        if (policeMenuItem != null) {
            policeMenuItem.setOnAction(e -> openModule("PoliceRecords.fxml", "Police Records"));
        }

        if (aboutMenuItem != null) {
            aboutMenuItem.setOnAction(e -> showAboutDialog());
        }

        if (reportsMenuItem != null) {
            reportsMenuItem.setOnAction(e -> showAlert("Reports", "Generate reports feature coming soon!"));
        }
    }

    // ========== NEW METHOD: Opens Module Windows ==========
    private void openModule(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle(title + " - Vehicle Identification System");
            stage.setScene(scene);
            stage.setMinWidth(900);
            stage.setMinHeight(600);
            stage.show();
            updateStatus("Opened " + title + " module");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not open module: " + fxmlFile + "\n" + e.getMessage());
            updateStatus("Error opening module: " + e.getMessage());
        }
    }

    private void setupPagination() {
        int totalPages = (int) Math.ceil((double) vehicleList.size() / ROWS_PER_PAGE);
        pagination.setPageCount(Math.max(totalPages, 1));

        pagination.setPageFactory(pageIndex -> {
            VBox box = new VBox(10);
            int fromIndex = pageIndex * ROWS_PER_PAGE;
            int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, vehicleList.size());

            if (fromIndex < vehicleList.size()) {
                List<Vehicle> pageVehicles = new ArrayList<>(vehicleList.subList(fromIndex, toIndex));
                Platform.runLater(() -> {
                    vehicleTable.setItems(FXCollections.observableArrayList(pageVehicles));
                    updateStatus("Showing page " + (pageIndex + 1) + " of " + pagination.getPageCount());
                });
            }

            return box;
        });
    }

    private void loadVehicleData() {
        try {
            updateStatus("Loading vehicle data from database...");
            vehicleList.clear();

            String query = "SELECT * FROM Vehicle LIMIT 20";
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
                v.setStatus(getVehicleStatus(rs.getInt("year")));
                vehicleList.add(v);
                count++;
            }

            vehicleTable.setItems(vehicleList);
            updateStatus("Loaded " + count + " vehicle records from DATABASE");

            // Refresh pagination with new data
            int totalPages = (int) Math.ceil((double) vehicleList.size() / ROWS_PER_PAGE);
            pagination.setPageCount(Math.max(totalPages, 1));
            pagination.setCurrentPageIndex(0);

            // Display first page
            if (!vehicleList.isEmpty()) {
                int toIndex = Math.min(ROWS_PER_PAGE, vehicleList.size());
                vehicleTable.setItems(FXCollections.observableArrayList(vehicleList.subList(0, toIndex)));
            }

            // Update statistics cards
            updateStatistics();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to load vehicles: " + e.getMessage());
            loadDummyData();
            updateStatus("Using dummy data - Database connection failed");
        }
    }

    private void loadVehicleDataWithProgress() {
        updateStatus("Starting data load process...");
        progressIndicator.setVisible(true);
        progressBar.setVisible(true);
        progressBar.setProgress(0);

        new Thread(() -> {
            try {
                for (int i = 0; i <= 100; i += 20) {
                    final int progressValue = i;
                    Platform.runLater(() -> progressBar.setProgress(progressValue / 100.0));
                    Thread.sleep(200);
                }

                Platform.runLater(() -> loadVehicleData());

                Platform.runLater(() -> {
                    progressIndicator.setVisible(false);
                    progressBar.setVisible(false);
                    showAlert("Success", "Vehicle data loaded successfully!");
                    updateStatus("Data load completed successfully");
                });

            } catch (InterruptedException e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    showAlert("Error", "Loading interrupted: " + e.getMessage());
                    updateStatus("Error: Loading interrupted");
                });
            }
        }).start();
    }

    private void loadDummyData() {
        updateStatus("Loading dummy vehicle data...");
        vehicleList.clear();

        String[] makes = {"Toyota", "Honda", "Ford", "BMW", "Mercedes", "Audi", "Tesla", "Hyundai"};
        String[] models = {"Camry", "Civic", "Mustang", "X5", "C-Class", "A4", "Model 3", "Elantra"};

        for (int i = 1; i <= 20; i++) {
            int year = 2020 + (i % 6);
            Vehicle v = new Vehicle(
                    i,
                    "REG" + String.format("%03d", i),
                    makes[i % makes.length],
                    models[i % models.length] + " " + year,
                    year
            );
            v.setStatus(getVehicleStatus(year));
            vehicleList.add(v);
        }

        vehicleTable.setItems(vehicleList);
        updateStatus("Loaded 20 dummy vehicle records");

        int totalPages = (int) Math.ceil((double) vehicleList.size() / ROWS_PER_PAGE);
        pagination.setPageCount(Math.max(totalPages, 1));
        pagination.setCurrentPageIndex(0);

        // Update statistics cards
        updateStatistics();
    }

    private String getVehicleStatus(int year) {
        int currentYear = java.time.Year.now().getValue();
        if (year == currentYear) {
            return "New";
        } else if (year >= currentYear - 2) {
            return "Like New";
        } else if (year >= currentYear - 5) {
            return "Good";
        } else {
            return "Old";
        }
    }

    private void updateStatus(String message) {
        Platform.runLater(() -> {
            if (statusLabel != null) {
                statusLabel.setText(message);
            }
            System.out.println(message);
        });
    }

    private void updateStatistics() {
        int total = vehicleList.size();
        int newCount = 0;
        for (Vehicle v : vehicleList) {
            String status = v.getStatus();
            if ("New".equals(status) || "Like New".equals(status)) {
                newCount++;
            }
        }

        final int finalTotal = total;
        final int finalNewCount = newCount;
        Platform.runLater(() -> {
            if (totalVehiclesLabel != null) {
                totalVehiclesLabel.setText(String.valueOf(finalTotal));
            }
            if (newVehiclesLabel != null) {
                newVehiclesLabel.setText(String.valueOf(finalNewCount));
            }
            if (insuranceLabel != null) {
                insuranceLabel.setText(String.valueOf(finalTotal));
            }
        });
    }

    @FXML
    private void refreshData() {
        loadVehicleDataWithProgress();
    }

    @FXML
    private void exportData() {
        try {
            StringBuilder data = new StringBuilder();
            data.append("ID,Registration,Make,Model,Year,Status\n");

            for (Vehicle v : vehicleList) {
                data.append(v.getVehicleId()).append(",")
                        .append(v.getRegistrationNumber()).append(",")
                        .append(v.getMake()).append(",")
                        .append(v.getModel()).append(",")
                        .append(v.getYear()).append(",")
                        .append(v.getStatus()).append("\n");
            }

            java.io.FileWriter writer = new java.io.FileWriter("vehicles_export_" + System.currentTimeMillis() + ".csv");
            writer.write(data.toString());
            writer.close();

            showAlert("Export Successful", "Data exported to CSV file");
            updateStatus("Data exported successfully");

        } catch (Exception e) {
            showAlert("Export Failed", e.getMessage());
            updateStatus("Export failed: " + e.getMessage());
        }
    }

    private void showAboutDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Vehicle Identification System");
        alert.setHeaderText("Vehicle Identification System v1.0");
        alert.setContentText("""
            Developed for OOP2 Project
            
            Features:
            • Vehicle Registration Management
            • Insurance Tracking
            • Police Records
            • Workshop Services
            • Customer Queries
            
            Technologies:
            • JavaFX for UI
            • PostgreSQL for Database
            • JDBC for Connectivity
            
            © 2026 All Rights Reserved
            """);
        alert.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}