module org.example.vehicleidentificationsystem {
    // Required JavaFX modules
    requires javafx.controls;
    requires javafx.fxml;

    // Required for database connectivity
    requires java.sql;

    // Required for PostgreSQL driver (if using module path)
    // If you get driver not found error, add:
    requires java.naming;

    // Open package for JavaFX FXML binding
    opens org.example.vehicleidentificationsystem to javafx.fxml;

    // Export package for external use
    exports org.example.vehicleidentificationsystem;
}