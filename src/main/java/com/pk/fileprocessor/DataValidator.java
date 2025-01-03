package com.pk.fileprocessor;

public class DataValidator {
    public static boolean validateCustomerData(int customerId, String customerName) {
        if (customerId < 0) {
            System.err.println("Invalid Customer ID: " + customerId);
            return false;
        }
        if (customerName == null || customerName.trim().isEmpty()) {
            System.err.println("Customer Name cannot be empty.");
            return false;
        }
        return true;
    }

    public static boolean validatePurchaseData(String productName, int quantity, double price) {
        if (productName == null || productName.trim().isEmpty()) {
            System.err.println("Product Name cannot be empty.");
            return false;
        }
        if (quantity <= 0) {
            System.err.println("Quantity must be greater than zero.");
            return false;
        }
        if (price <= 0) {
            System.err.println("Price must be greater than zero.");
            return false;
        }
        return true;
    }
}
