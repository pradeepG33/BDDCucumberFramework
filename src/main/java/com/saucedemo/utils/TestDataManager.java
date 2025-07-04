package com.saucedemo.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Test Data Manager for handling JSON and Excel test data
 */
public class TestDataManager {
    private static final Logger logger = LogManager.getLogger(TestDataManager.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TEST_DATA_PATH = "src/test/resources/testdata/";
    
    /**
     * Read JSON data from file
     * @param fileName JSON file name
     * @return JsonNode object
     */
    public static JsonNode readJsonData(String fileName) {
        try {
            File file = new File(TEST_DATA_PATH + fileName);
            JsonNode jsonNode = objectMapper.readTree(file);
            logger.info("Successfully read JSON data from: {}", fileName);
            return jsonNode;
        } catch (IOException e) {
            logger.error("Failed to read JSON data from: {}", fileName, e);
            throw new RuntimeException("Could not read JSON data: " + fileName, e);
        }
    }
    
    /**
     * Get user data from JSON
     * @param userType Type of user (standard_user, locked_out_user, etc.)
     * @return Map containing user data
     */
    public static Map<String, Object> getUserData(String userType) {
        JsonNode usersData = readJsonData("users.json");
        JsonNode userData = usersData.get("users").get(userType);
        
        if (userData == null) {
            logger.error("User data not found for: {}", userType);
            throw new IllegalArgumentException("User data not found: " + userType);
        }
        
        Map<String, Object> userMap = new HashMap<>();
        userData.fields().forEachRemaining(entry -> {
            userMap.put(entry.getKey(), entry.getValue().asText());
        });
        
        logger.info("Retrieved user data for: {}", userType);
        return userMap;
    }
    
    /**
     * Get invalid user data from JSON
     * @param invalidUserType Type of invalid user
     * @return Map containing invalid user data
     */
    public static Map<String, Object> getInvalidUserData(String invalidUserType) {
        JsonNode usersData = readJsonData("users.json");
        JsonNode invalidUserData = usersData.get("invalid_users").get(invalidUserType);
        
        if (invalidUserData == null) {
            logger.error("Invalid user data not found for: {}", invalidUserType);
            throw new IllegalArgumentException("Invalid user data not found: " + invalidUserType);
        }
        
        Map<String, Object> userMap = new HashMap<>();
        invalidUserData.fields().forEachRemaining(entry -> {
            userMap.put(entry.getKey(), entry.getValue().asText());
        });
        
        logger.info("Retrieved invalid user data for: {}", invalidUserType);
        return userMap;
    }
    
    /**
     * Get product data from JSON
     * @return List of product maps
     */
    public static List<Map<String, Object>> getProductData() {
        JsonNode productsData = readJsonData("products.json");
        JsonNode products = productsData.get("products");
        
        List<Map<String, Object>> productList = new ArrayList<>();
        
        for (JsonNode product : products) {
            Map<String, Object> productMap = new HashMap<>();
            product.fields().forEachRemaining(entry -> {
                if (entry.getValue().isNumber()) {
                    productMap.put(entry.getKey(), entry.getValue().asDouble());
                } else {
                    productMap.put(entry.getKey(), entry.getValue().asText());
                }
            });
            productList.add(productMap);
        }
        
        logger.info("Retrieved {} products from test data", productList.size());
        return productList;
    }
    
    /**
     * Get sorting options from JSON
     * @return List of sorting option maps
     */
    public static List<Map<String, Object>> getSortingOptions() {
        JsonNode productsData = readJsonData("products.json");
        JsonNode sortingOptions = productsData.get("sorting_options");
        
        List<Map<String, Object>> sortingList = new ArrayList<>();
        
        for (JsonNode option : sortingOptions) {
            Map<String, Object> optionMap = new HashMap<>();
            option.fields().forEachRemaining(entry -> {
                optionMap.put(entry.getKey(), entry.getValue().asText());
            });
            sortingList.add(optionMap);
        }
        
        logger.info("Retrieved {} sorting options from test data", sortingList.size());
        return sortingList;
    }
    
    /**
     * Get expected sorted products
     * @param sortType Sort type (name_asc, name_desc, price_asc, price_desc)
     * @return List of expected sorted values
     */
    public static List<Object> getExpectedSortedProducts(String sortType) {
        JsonNode productsData = readJsonData("products.json");
        JsonNode expectedSorted = productsData.get("expected_sorted_products").get(sortType);
        
        List<Object> expectedList = new ArrayList<>();
        for (JsonNode item : expectedSorted) {
            if (item.isNumber()) {
                expectedList.add(item.asDouble());
            } else {
                expectedList.add(item.asText());
            }
        }
        
        logger.info("Retrieved expected sorted products for: {}", sortType);
        return expectedList;
    }
    
    /**
     * Get valid checkout data from JSON
     * @return List of valid checkout data maps
     */
    public static List<Map<String, Object>> getValidCheckoutData() {
        JsonNode checkoutData = readJsonData("checkout.json");
        JsonNode validData = checkoutData.get("valid_checkout_data");
        
        List<Map<String, Object>> checkoutList = new ArrayList<>();
        
        for (JsonNode data : validData) {
            Map<String, Object> dataMap = new HashMap<>();
            data.fields().forEachRemaining(entry -> {
                dataMap.put(entry.getKey(), entry.getValue().asText());
            });
            checkoutList.add(dataMap);
        }
        
        logger.info("Retrieved {} valid checkout data sets", checkoutList.size());
        return checkoutList;
    }
    
    /**
     * Get invalid checkout data from JSON
     * @return List of invalid checkout data maps
     */
    public static List<Map<String, Object>> getInvalidCheckoutData() {
        JsonNode checkoutData = readJsonData("checkout.json");
        JsonNode invalidData = checkoutData.get("invalid_checkout_data");
        
        List<Map<String, Object>> checkoutList = new ArrayList<>();
        
        for (JsonNode data : invalidData) {
            Map<String, Object> dataMap = new HashMap<>();
            data.fields().forEachRemaining(entry -> {
                dataMap.put(entry.getKey(), entry.getValue().asText());
            });
            checkoutList.add(dataMap);
        }
        
        logger.info("Retrieved {} invalid checkout data sets", checkoutList.size());
        return checkoutList;
    }
    
    /**
     * Get checkout flow data from JSON
     * @return Map containing checkout flow data
     */
    public static Map<String, Object> getCheckoutFlowData() {
        JsonNode checkoutData = readJsonData("checkout.json");
        JsonNode flowData = checkoutData.get("checkout_flow");
        
        Map<String, Object> flowMap = new HashMap<>();
        flowData.fields().forEachRemaining(entry -> {
            if (entry.getValue().isNumber()) {
                flowMap.put(entry.getKey(), entry.getValue().asDouble());
            } else {
                flowMap.put(entry.getKey(), entry.getValue().asText());
            }
        });
        
        logger.info("Retrieved checkout flow data");
        return flowMap;
    }
    
    /**
     * Read Excel data from file
     * @param fileName Excel file name
     * @param sheetName Sheet name
     * @return List of Maps containing row data
     */
    public static List<Map<String, String>> readExcelData(String fileName, String sheetName) {
        List<Map<String, String>> data = new ArrayList<>();
        
        try (FileInputStream fis = new FileInputStream(TEST_DATA_PATH + fileName);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet not found: " + sheetName);
            }
            
            Row headerRow = sheet.getRow(0);
            List<String> headers = new ArrayList<>();
            
            for (Cell cell : headerRow) {
                headers.add(cell.getStringCellValue());
            }
            
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Map<String, String> rowData = new HashMap<>();
                
                for (int j = 0; j < headers.size(); j++) {
                    Cell cell = row.getCell(j);
                    String cellValue = "";
                    
                    if (cell != null) {
                        switch (cell.getCellType()) {
                            case STRING:
                                cellValue = cell.getStringCellValue();
                                break;
                            case NUMERIC:
                                cellValue = String.valueOf(cell.getNumericCellValue());
                                break;
                            case BOOLEAN:
                                cellValue = String.valueOf(cell.getBooleanCellValue());
                                break;
                            default:
                                cellValue = "";
                        }
                    }
                    
                    rowData.put(headers.get(j), cellValue);
                }
                data.add(rowData);
            }
            
            logger.info("Successfully read {} rows from Excel file: {}", data.size(), fileName);
            
        } catch (IOException e) {
            logger.error("Failed to read Excel data from: {}", fileName, e);
            throw new RuntimeException("Could not read Excel data: " + fileName, e);
        }
        
        return data;
    }
    
    /**
     * Get all user types from JSON
     * @return List of user types
     */
    public static List<String> getAllUserTypes() {
        JsonNode usersData = readJsonData("users.json");
        JsonNode users = usersData.get("users");
        
        List<String> userTypes = new ArrayList<>();
        users.fieldNames().forEachRemaining(userTypes::add);
        
        logger.info("Retrieved {} user types", userTypes.size());
        return userTypes;
    }
    
    /**
     * Get all invalid user types from JSON
     * @return List of invalid user types
     */
    public static List<String> getAllInvalidUserTypes() {
        JsonNode usersData = readJsonData("users.json");
        JsonNode invalidUsers = usersData.get("invalid_users");
        
        List<String> invalidUserTypes = new ArrayList<>();
        invalidUsers.fieldNames().forEachRemaining(invalidUserTypes::add);
        
        logger.info("Retrieved {} invalid user types", invalidUserTypes.size());
        return invalidUserTypes;
    }
}