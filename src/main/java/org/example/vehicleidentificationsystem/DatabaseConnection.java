package org.example.vehicleidentificationsystem;

import java.sql.*;

public class DatabaseConnection {
    private Connection connection;
    private String url;
    private String user;
    private String password;

    public DatabaseConnection() {
        // === YOUR NEON.TECH CONNECTION DETAILS ===
        this.url = "jdbc:postgresql://ep-long-rice-anm1d4ts-pooler.c-6.us-east-1.aws.neon.tech/neondb?sslmode=require&channel_binding=require";
        this.user = "neondb_owner";
        this.password = "npg_H2xzctQRavn5";
    }

    public Connection getConnection() throws SQLException {
        try {
            // Load PostgreSQL driver
            Class.forName("org.postgresql.Driver");

            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(url, user, password);
                System.out.println("✅ Connected to Neon.tech database successfully!");
            }
            return connection;
        } catch (ClassNotFoundException e) {
            System.err.println("❌ PostgreSQL JDBC Driver not found!");
            System.err.println("Make sure you added the dependency to pom.xml");
            e.printStackTrace();
            throw new SQLException("PostgreSQL Driver not found", e);
        } catch (SQLException e) {
            System.err.println("❌ Failed to connect to Neon.tech database");
            System.err.println("URL: " + url);
            System.err.println("User: " + user);
            System.err.println("Error: " + e.getMessage());
            throw e;
        }
    }

    public ResultSet executeQuery(String query) throws SQLException {
        Statement stmt = getConnection().createStatement();
        return stmt.executeQuery(query);
    }

    public int executeUpdate(String query) throws SQLException {
        Statement stmt = getConnection().createStatement();
        return stmt.executeUpdate(query);
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}