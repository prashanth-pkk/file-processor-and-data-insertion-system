package com.pk.fileprocessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

public class DatabaseHandler {

    private static String DB_URL;
    private static String USER;
    private static String PASSWORD;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Load properties from config.properties file
            try (InputStream input = DatabaseHandler.class.getClassLoader().getResourceAsStream("config.properties")) {
                if (input == null) {
                    System.out.println("Sorry, unable to find config.properties");
                }
                Properties prop = new Properties();
                prop.load(input);

                DB_URL = prop.getProperty("db.url");
                USER = prop.getProperty("db.username");
                PASSWORD = prop.getProperty("db.password");

                if (DB_URL == null || USER == null || PASSWORD == null) {
                    throw new IOException("Missing database configuration in config.properties");
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver not found: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String loadSQLQuery(String queryFileName) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(DatabaseHandler.class.getClassLoader().getResourceAsStream(queryFileName))))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString().trim();
    }

    public static void insertDataIntoDatabase(String data) throws SQLException {
        String query;
        try {
            query = loadSQLQuery("insertData.sql");
        } catch (IOException e) {
            throw new SQLException("Error loading SQL query", e);
        }
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, data);
            stmt.executeUpdate();
            System.out.println("Data inserted into database: " + data);

        } catch (SQLException e) {
            throw new SQLException("Error inserting data into database", e);
        }
    }

    public static void insertCustomerWithTransaction(String customerName) throws SQLException {
        String query;
        try {
            query = loadSQLQuery("insertCustomer.sql");
        } catch (IOException e) {
            throw new SQLException("Error loading SQL query for insertCustomer", e);
        }

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query)) {

            connection.setAutoCommit(false);

            stmt.setString(1, customerName); // Set only customer name, leave customer_id out
            stmt.executeUpdate();
            System.out.println("Customer inserted into database: " + customerName);

        } catch (SQLException e) {
            try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
                connection.rollback();
            }
            System.err.println("Error inserting customer, rolling back.");
            e.printStackTrace();
            throw new SQLException("Error inserting customer into database", e);
        }
    }
    
    public static void insertPurchaseWithTransaction(int customerId, String productName, int quantity, double price) throws SQLException {
        String query;
        try {
            query = loadSQLQuery("insertPurchase.sql");
        } catch (IOException e) {
            throw new SQLException("Error loading SQL query for insertPurchase", e);
        }
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query)) {

            connection.setAutoCommit(false);

            stmt.setInt(1, customerId);
            stmt.setString(2, productName);
            stmt.setInt(3, quantity);
            stmt.setDouble(4, price);
            stmt.executeUpdate();
            System.out.println("purchases inserted into database for customer ID: " + customerId);

        } catch (SQLException e) {
            try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
                connection.rollback();
            }
            System.err.println("Error inserting purchase, rolling back.");
            e.printStackTrace();

            throw new SQLException("Error inserting purchases into database", e);
        }
    }
}
