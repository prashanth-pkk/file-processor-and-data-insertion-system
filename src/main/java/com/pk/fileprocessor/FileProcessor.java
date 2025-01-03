package com.pk.fileprocessor;

import java.io.*;
import java.sql.SQLException;
import java.util.List;

public class FileProcessor {
    public static void main(String[] args) {
        String filePath = "src/main/resources/data.txt";  // Path to your data file

        // Reading file as List and processing the content
        System.out.println("\nReading file as List:");
        try {
            // Read all lines from the file
            List<String> fileLines = FileReaderUtil.readFileAsList(filePath);

            // Skip the header (first line) by starting from index 1
            for (int i = 1; i < fileLines.size(); i++) {
                String line = fileLines.get(i);  // Get each line of data
                String[] data = line.split(","); // Split by comma to separate values

                if (data.length == 5) {  // Check if the line contains exactly 5 columns
                    String customerName = null;
                    try {
                        // Parse data into respective fields
                        int customerId = Integer.parseInt(data[0].trim());
                        customerName = data[1].trim();
                        String productName = data[2].trim();
                        int productQuantity = Integer.parseInt(data[3].trim());
                        double price = Double.parseDouble(data[4].trim());

                        if (DataValidator.validateCustomerData(customerId, customerName)) {
                            DatabaseHandler.insertCustomerWithTransaction(customerName);
                            System.out.println("Processed customer: " + customerName);
                        }

                        if (DataValidator.validatePurchaseData(productName, productQuantity, price)) {
                            DatabaseHandler.insertPurchaseWithTransaction(customerId, productName, productQuantity, price);  // Insert purchases data
                            System.out.println("Processed customer: " + customerName + " purchasing: " + productName);
                        }

                    } catch (NumberFormatException e) {
                        System.err.println("Skipping invalid data (non-numeric): " + line);  // Invalid data (non-numeric)
                    } catch (SQLException e) {
                        System.err.println("Error inserting data for customer: " + customerName);  // Database insertion error
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("Skipping malformed line: " + line);  // Malformed data line
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file as list: " + e.getMessage());  // File read error
        }

        // Processing each line of the file (Using processFile method for demonstration)
        System.out.println("\nProcessing each line of the file:");
        try {
            FileReaderUtil.processFile(filePath, line -> {
                // Process and print each line (custom behavior can be defined here)
                System.out.println("Processed line: " + line);
            });
        } catch (IOException e) {
            System.err.println("Error processing the file: " + e.getMessage());  // File processing error
        }
    }
}
