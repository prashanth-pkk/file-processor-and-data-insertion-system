package com.pk.fileprocessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileReaderUtil {
    public static String readFileAsString(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new IOException("Error reading the file: " + filePath, e);
        }
        return content.toString();
    }

    public static List<String> readFileAsList(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new IOException("Error reading the file: " + filePath, e);
        }
        return lines;
    }

    public static void processFile(String filePath, LineProcessor lineProcessor) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineProcessor.process(line);
            }
        } catch (IOException e) {
            throw new IOException("Error processing the file: " + filePath, e);
        }
    }

    public static void main(String[] args) {

        try {
            String fileContent = FileReaderUtil.readFileAsString("src/main/resources/data.txt");
            System.out.println("File content as String: ");
            System.out.println(fileContent);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        try {
            List<String> lines = FileReaderUtil.readFileAsList("src/main/resources/data.txt");
            System.out.println("File content as List:");
            for (String line : lines) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        try {
            FileReaderUtil.processFile("src/main/resources/data.txt", line -> {
                System.out.println("Processing line: " + line);
            });
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
