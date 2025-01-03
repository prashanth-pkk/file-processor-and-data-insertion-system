package com.pk.fileprocessor;

import java.io.*;
import java.sql.SQLException;
import java.util.List;

public class FileProcessor {
    public static void main(String[] args) {
        String filePath = "src/main/resources/data.txt";

        System.out.println("\nReading file as List:");
        try {
            List<String> fileLines = FileReaderUtil.readFileAsList(filePath);

            for (int i = 1; i < fileLines.size(); i++) {
                String line = fileLines.get(i);
                String[] data = line.split(",");

                if (data.length == 5) {
                    String customerName = null;
                    try {
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
                            DatabaseHandler.insertPurchaseWithTransaction(customerId, productName, productQuantity, price);
                            System.out.println("Processed customer: " + customerName + " purchasing: " + productName);
                        }

                    } catch (NumberFormatException e) {
                        System.err.println("Skipping invalid data (non-numeric): " + line);
                    } catch (SQLException e) {
                        System.err.println("Error inserting data for customer: " + customerName);
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("Skipping malformed line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file as list: " + e.getMessage());
        }
        System.out.println("\nProcessing each line of the file:");
        try {
            FileReaderUtil.processFile(filePath, line -> {
                System.out.println("Processed line: " + line);
            });
        } catch (IOException e) {
            System.err.println("Error processing the file: " + e.getMessage());
        }
    }
}
