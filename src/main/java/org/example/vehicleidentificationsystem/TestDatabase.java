package org.example.vehicleidentificationsystem;

import java.sql.*;

public class TestDatabase {
    public static void main(String[] args) {
        DatabaseConnection db = new DatabaseConnection();

        try {
            Connection conn = db.getConnection();
            System.out.println("✅ Database connection successful!");

            ResultSet rs = db.executeQuery("SELECT * FROM Vehicle");

            System.out.println("\n📋 Vehicle Records:");
            System.out.println("ID | Registration | Make | Model | Year");
            System.out.println("----------------------------------------");

            while (rs.next()) {
                System.out.println(rs.getInt("vehicle_id") + " | " +
                        rs.getString("registration_number") + " | " +
                        rs.getString("make") + " | " +
                        rs.getString("model") + " | " +
                        rs.getInt("year"));
            }

            db.closeConnection();

        } catch (SQLException e) {
            System.err.println("❌ Database test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}