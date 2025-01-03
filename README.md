#File Processor and Database Insertion System

This project is designed to process a file containing customer and purchase information and insert the data into a MySQL database with transaction management and error handling.

Features:

1. Reads customer and purchase data from a text file.
2. Validates and processes data before inserting into a MySQL database.
3. Uses transaction management to ensure data consistency across multiple related inserts (i.e., Customer and Purchase).  
4. Loads SQL queries from external .sql files for better maintainability.  
5. Implements error handling to gracefully handle any issues during data insertion.
Validates data format before insertion.

Run the Application:-   
mvn clean install  
mvn exec:java
