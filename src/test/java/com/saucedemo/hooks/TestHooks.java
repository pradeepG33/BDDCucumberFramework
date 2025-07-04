package com.saucedemo.hooks;

import com.saucedemo.config.ConfigManager;
import com.saucedemo.listeners.ExtentReportListener;
import com.saucedemo.listeners.ScreenshotListener;
import com.saucedemo.utils.DriverManager;
import com.saucedemo.utils.ScreenshotUtils;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Cucumber Hooks for test setup and teardown
 */
public class TestHooks {
    private static final Logger logger = LogManager.getLogger(TestHooks.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    
    @BeforeAll
    public static void globalSetup() {
        logger.info("========== STARTING TEST EXECUTION ==========");
        logger.info("Environment: {}", config.getEnvironment());
        logger.info("Browser: {}", config.getBrowser());
        logger.info("Application URL: {}", config.getAppUrl());
        logger.info("Headless Mode: {}", config.isHeadless());
        logger.info("Grid Enabled: {}", config.isGridEnabled());
        
        // Create necessary directories
        ScreenshotUtils.createScreenshotDirectory();
        
        // Clean up old screenshots
        ScreenshotListener.cleanupOldScreenshots();
        
        logger.info("Global setup completed successfully");
    }
    
    @Before(order = 1)
    public void setUp(Scenario scenario) {
        logger.info("========== STARTING SCENARIO: {} ==========", scenario.getName());
        
        try {
            // Initialize WebDriver
            String browser = System.getProperty("browser", config.getBrowser());
            DriverManager.initializeDriver(browser);
            
            logger.info("WebDriver initialized successfully for scenario: {}", scenario.getName());
            
            // Log scenario information to ExtentReports
            ExtentReportListener.logInfo("Scenario started: " + scenario.getName());
            ExtentReportListener.logInfo("Browser: " + browser);
            ExtentReportListener.logInfo("Environment: " + config.getEnvironment());
            
        } catch (Exception e) {
            logger.error("Failed to initialize WebDriver for scenario: {}", scenario.getName(), e);
            ExtentReportListener.logFail("Failed to initialize WebDriver: " + e.getMessage());
            throw new RuntimeException("WebDriver initialization failed", e);
        }
    }
    
    @Before(value = "@login", order = 2)
    public void setUpForLoginTests(Scenario scenario) {
        logger.info("Setting up for login test scenario: {}", scenario.getName());
        
        try {
            // Navigate to login page
            DriverManager.getDriver().get(config.getAppUrl());
            logger.info("Navigated to application URL: {}", config.getAppUrl());
            
            ExtentReportListener.logInfo("Navigated to login page");
            
        } catch (Exception e) {
            logger.error("Failed to navigate to login page for scenario: {}", scenario.getName(), e);
            ExtentReportListener.logFail("Failed to navigate to login page: " + e.getMessage());
            throw new RuntimeException("Navigation to login page failed", e);
        }
    }
    
    @Before(value = "@inventory", order = 2)
    public void setUpForInventoryTests(Scenario scenario) {
        logger.info("Setting up for inventory test scenario: {}", scenario.getName());
        
        try {
            // Login with standard user for inventory tests
            DriverManager.getDriver().get(config.getAppUrl());
            
            // Perform quick login for inventory tests
            performQuickLogin();
            
            logger.info("Logged in successfully for inventory test scenario: {}", scenario.getName());
            ExtentReportListener.logInfo("Logged in with standard user for inventory tests");
            
        } catch (Exception e) {
            logger.error("Failed to setup for inventory test scenario: {}", scenario.getName(), e);
            ExtentReportListener.logFail("Failed to setup for inventory tests: " + e.getMessage());
            throw new RuntimeException("Inventory test setup failed", e);
        }
    }
    
    @Before(value = "@cart", order = 2)
    public void setUpForCartTests(Scenario scenario) {
        logger.info("Setting up for cart test scenario: {}", scenario.getName());
        
        try {
            // Login with standard user for cart tests
            DriverManager.getDriver().get(config.getAppUrl());
            
            // Perform quick login for cart tests
            performQuickLogin();
            
            logger.info("Logged in successfully for cart test scenario: {}", scenario.getName());
            ExtentReportListener.logInfo("Logged in with standard user for cart tests");
            
        } catch (Exception e) {
            logger.error("Failed to setup for cart test scenario: {}", scenario.getName(), e);
            ExtentReportListener.logFail("Failed to setup for cart tests: " + e.getMessage());
            throw new RuntimeException("Cart test setup failed", e);
        }
    }
    
    @Before(value = "@checkout", order = 2)
    public void setUpForCheckoutTests(Scenario scenario) {
        logger.info("Setting up for checkout test scenario: {}", scenario.getName());
        
        try {
            // Login with standard user for checkout tests
            DriverManager.getDriver().get(config.getAppUrl());
            
            // Perform quick login for checkout tests
            performQuickLogin();
            
            logger.info("Logged in successfully for checkout test scenario: {}", scenario.getName());
            ExtentReportListener.logInfo("Logged in with standard user for checkout tests");
            
        } catch (Exception e) {
            logger.error("Failed to setup for checkout test scenario: {}", scenario.getName(), e);
            ExtentReportListener.logFail("Failed to setup for checkout tests: " + e.getMessage());
            throw new RuntimeException("Checkout test setup failed", e);
        }
    }
    
    @After(order = 2)
    public void tearDown(Scenario scenario) {
        logger.info("========== FINISHING SCENARIO: {} ==========", scenario.getName());
        
        try {
            if (DriverManager.isDriverInitialized()) {
                
                // Capture screenshot based on scenario status
                if (scenario.isFailed()) {
                    logger.warn("Scenario failed: {}", scenario.getName());
                    
                    String screenshotPath = ScreenshotUtils.captureFailureScreenshot(
                        DriverManager.getDriver(), 
                        "FAILED_" + scenario.getName().replaceAll("\\s+", "_")
                    );
                    
                    if (screenshotPath != null) {
                        // Attach screenshot to Cucumber report
                        byte[] screenshot = ScreenshotUtils.captureScreenshotAsBase64(DriverManager.getDriver()).getBytes();
                        scenario.attach(screenshot, "image/png", "Failure Screenshot");
                        
                        // Log to ExtentReports
                        ExtentReportListener.logFail("Scenario failed: " + scenario.getName());
                        ExtentReportListener.addScreenshot(screenshotPath, "Failure Screenshot");
                    }
                } else {
                    logger.info("Scenario passed: {}", scenario.getName());
                    
                    String screenshotPath = ScreenshotUtils.captureScreenshot(
                        DriverManager.getDriver(), 
                        "SUCCESS_" + scenario.getName().replaceAll("\\s+", "_")
                    );
                    
                    if (screenshotPath != null) {
                        // Attach screenshot to Cucumber report
                        byte[] screenshot = ScreenshotUtils.captureScreenshotAsBase64(DriverManager.getDriver()).getBytes();
                        scenario.attach(screenshot, "image/png", "Success Screenshot");
                        
                        // Log to ExtentReports
                        ExtentReportListener.logPass("Scenario passed: " + scenario.getName());
                        ExtentReportListener.addScreenshot(screenshotPath, "Success Screenshot");
                    }
                }
                
                // Reset app state if needed
                try {
                    if (scenario.getSourceTagNames().contains("@reset_app_state")) {
                        resetAppState();
                        logger.info("App state reset after scenario: {}", scenario.getName());
                    }
                } catch (Exception e) {
                    logger.warn("Failed to reset app state after scenario: {}", scenario.getName(), e);
                }
            }
            
        } catch (Exception e) {
            logger.error("Error during scenario teardown: {}", scenario.getName(), e);
        } finally {
            // Always quit the driver
            DriverManager.quitDriver();
            logger.info("WebDriver quit successfully for scenario: {}", scenario.getName());
        }
    }
    
    @After(value = "@cleanup_cart", order = 1)
    public void cleanupCart(Scenario scenario) {
        logger.info("Cleaning up cart after scenario: {}", scenario.getName());
        
        try {
            if (DriverManager.isDriverInitialized()) {
                // Reset app state to clear cart
                resetAppState();
                logger.info("Cart cleaned up successfully");
                ExtentReportListener.logInfo("Cart cleaned up after test");
            }
        } catch (Exception e) {
            logger.warn("Failed to cleanup cart after scenario: {}", scenario.getName(), e);
            ExtentReportListener.logWarning("Failed to cleanup cart: " + e.getMessage());
        }
    }
    
    @AfterAll
    public static void globalTearDown() {
        logger.info("========== TEST EXECUTION COMPLETED ==========");
        
        try {
            // Flush ExtentReports
            ExtentReportListener.flushReports();
            logger.info("ExtentReports flushed successfully");
            
            // Additional cleanup if needed
            logger.info("Global teardown completed successfully");
            
        } catch (Exception e) {
            logger.error("Error during global teardown", e);
        }
    }
    
    /**
     * Perform quick login with standard user
     */
    private void performQuickLogin() {
        try {
            DriverManager.getDriver().findElement(org.openqa.selenium.By.id("user-name"))
                .sendKeys(config.getStandardUser());
            DriverManager.getDriver().findElement(org.openqa.selenium.By.id("password"))
                .sendKeys(config.getPassword());
            DriverManager.getDriver().findElement(org.openqa.selenium.By.id("login-button"))
                .click();
            
            // Wait for inventory page to load
            org.openqa.selenium.support.ui.WebDriverWait wait = 
                new org.openqa.selenium.support.ui.WebDriverWait(DriverManager.getDriver(), 
                    java.time.Duration.ofSeconds(config.getExplicitWait()));
            wait.until(org.openqa.selenium.support.ui.ExpectedConditions
                .visibilityOfElementLocated(org.openqa.selenium.By.className("inventory_container")));
            
            logger.debug("Quick login performed successfully");
            
        } catch (Exception e) {
            logger.error("Failed to perform quick login", e);
            throw new RuntimeException("Quick login failed", e);
        }
    }
    
    /**
     * Reset application state
     */
    private void resetAppState() {
        try {
            // Open menu
            DriverManager.getDriver().findElement(org.openqa.selenium.By.id("react-burger-menu-btn"))
                .click();
            
            // Wait for menu to open
            Thread.sleep(1000);
            
            // Click reset app state
            DriverManager.getDriver().findElement(org.openqa.selenium.By.id("reset_sidebar_link"))
                .click();
            
            // Close menu
            DriverManager.getDriver().findElement(org.openqa.selenium.By.id("react-burger-cross-btn"))
                .click();
            
            logger.debug("App state reset successfully");
            
        } catch (Exception e) {
            logger.error("Failed to reset app state", e);
            throw new RuntimeException("Reset app state failed", e);
        }
    }
    
    /**
     * Capture step screenshot
     * @param stepName Step name
     */
    public static void captureStepScreenshot(String stepName) {
        try {
            if (DriverManager.isDriverInitialized()) {
                String screenshotPath = ScreenshotListener.captureStepScreenshot(stepName);
                logger.debug("Step screenshot captured: {}", stepName);
            }
        } catch (Exception e) {
            logger.warn("Failed to capture step screenshot: {}", stepName, e);
        }
    }
    
    /**
     * Log step information
     * @param stepDescription Step description
     */
    public static void logStep(String stepDescription) {
        logger.info("STEP: {}", stepDescription);
        ExtentReportListener.logInfo("STEP: " + stepDescription);
    }
    
    /**
     * Log test data being used
     * @param dataDescription Data description
     * @param data Actual data
     */
    public static void logTestData(String dataDescription, Object data) {
        logger.info("TEST DATA - {}: {}", dataDescription, data);
        ExtentReportListener.logInfo(String.format("TEST DATA - %s: %s", dataDescription, data));
    }
    
    /**
     * Log assertion result
     * @param assertion Assertion description
     * @param result Result of assertion
     */
    public static void logAssertion(String assertion, boolean result) {
        if (result) {
            logger.info("ASSERTION PASSED: {}", assertion);
            ExtentReportListener.logPass("ASSERTION PASSED: " + assertion);
        } else {
            logger.error("ASSERTION FAILED: {}", assertion);
            ExtentReportListener.logFail("ASSERTION FAILED: " + assertion);
        }
    }
}