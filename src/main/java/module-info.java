module org.example.vehicleidentificationsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;

    opens org.example.vehicleidentificationsystem to javafx.fxml;
    exports org.example.vehicleidentificationsystem;
}