package com.saucedemo.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.saucedemo.config.ConfigManager;
import com.saucedemo.utils.DriverManager;
import com.saucedemo.utils.ScreenshotUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ExtentReports listener for test reporting
 */
public class ExtentReportListener implements ITestListener {
    private static final Logger logger = LogManager.getLogger(ExtentReportListener.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    
    private static ExtentReports extentReports;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    
    static {
        setupExtentReports();
    }
    
    /**
     * Setup ExtentReports configuration
     */
    private static void setupExtentReports() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String reportPath = config.getReportsPath() + "ExtentReport_" + timestamp + ".html";
        
        // Create reports directory if it doesn't exist
        File reportsDir = new File(config.getReportsPath());
        if (!reportsDir.exists()) {
            reportsDir.mkdirs();
        }
        
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        
        // Configure Spark Reporter
        sparkReporter.config().setDocumentTitle(config.getExtentReportTitle());
        sparkReporter.config().setReportName(config.getExtentReportName());
        sparkReporter.config().setTheme(getTheme());
        sparkReporter.config().setTimeStampFormat("dd/MM/yyyy hh:mm:ss");
        
        // Custom CSS
        sparkReporter.config().setCss(getCustomCSS());
        
        // Initialize ExtentReports
        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);
        
        // Set system information
        setSystemInformation();
        
        logger.info("ExtentReports initialized with report path: {}", reportPath);
    }
    
    /**
     * Get theme based on configuration
     */
    private static com.aventstack.extentreports.reporter.configuration.Theme getTheme() {
        String theme = config.getExtentReportTheme();
        if ("dark".equalsIgnoreCase(theme)) {
            return com.aventstack.extentreports.reporter.configuration.Theme.DARK;
        }
        return com.aventstack.extentreports.reporter.configuration.Theme.STANDARD;
    }
    
    /**
     * Get custom CSS for report styling
     */
    private static String getCustomCSS() {
        return """
            .brand-logo {
                background-color: #2E8B57;
            }
            
            .nav-wrapper {
                background-color: #2E8B57 !important;
            }
            
            .card-panel {
                border-radius: 10px;
                box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            }
            
            .test-node {
                border-left: 4px solid #2E8B57;
            }
            
            .test-node.pass {
                border-left-color: #28a745;
            }
            
            .test-node.fail {
                border-left-color: #dc3545;
            }
            
            .test-node.skip {
                border-left-color: #ffc107;
            }
            """;
    }
    
    /**
     * Set system information in the report
     */
    private static void setSystemInformation() {
        extentReports.setSystemInfo("Application URL", config.getAppUrl());
        extentReports.setSystemInfo("Browser", config.getBrowser());
        extentReports.setSystemInfo("Environment", config.getEnvironment());
        extentReports.setSystemInfo("Operating System", System.getProperty("os.name"));
        extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
        extentReports.setSystemInfo("User", System.getProperty("user.name"));
        extentReports.setSystemInfo("Execution Mode", config.isHeadless() ? "Headless" : "GUI");
        extentReports.setSystemInfo("Grid Enabled", String.valueOf(config.isGridEnabled()));
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String testDescription = result.getMethod().getDescription();
        String className = result.getTestClass().getName();
        
        ExtentTest test = extentReports.createTest(testName, testDescription)
                .assignCategory(className)
                .assignAuthor("Automation Team");
        
        extentTest.set(test);
        
        logger.info("Test started: {}", testName);
        test.log(Status.INFO, MarkupHelper.createLabel("Test Started: " + testName, ExtentColor.BLUE));
        
        // Log test parameters if any
        Object[] parameters = result.getParameters();
        if (parameters.length > 0) {
            StringBuilder params = new StringBuilder("Test Parameters: ");
            for (int i = 0; i < parameters.length; i++) {
                params.append(parameters[i].toString());
                if (i < parameters.length - 1) {
                    params.append(", ");
                }
            }
            test.log(Status.INFO, params.toString());
        }
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ExtentTest test = extentTest.get();
        
        test.log(Status.PASS, MarkupHelper.createLabel("Test Passed: " + testName, ExtentColor.GREEN));
        
        long duration = result.getEndMillis() - result.getStartMillis();
        test.log(Status.INFO, "Test execution time: " + duration + " ms");
        
        logger.info("Test passed: {}", testName);
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ExtentTest test = extentTest.get();
        
        test.log(Status.FAIL, MarkupHelper.createLabel("Test Failed: " + testName, ExtentColor.RED));
        
        // Log failure reason
        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            test.log(Status.FAIL, "Error: " + throwable.getMessage());
            test.log(Status.DEBUG, throwable);
        }
        
        // Capture screenshot if WebDriver is available
        try {
            if (DriverManager.isDriverInitialized()) {
                String screenshotPath = ScreenshotUtils.captureFailureScreenshot(DriverManager.getDriver(), testName);
                if (screenshotPath != null) {
                    test.addScreenCaptureFromPath(screenshotPath, "Failure Screenshot");
                    test.log(Status.INFO, "Screenshot captured for failed test");
                }
            }
        } catch (Exception e) {
            logger.error("Failed to capture screenshot for failed test: {}", testName, e);
            test.log(Status.WARNING, "Failed to capture screenshot: " + e.getMessage());
        }
        
        long duration = result.getEndMillis() - result.getStartMillis();
        test.log(Status.INFO, "Test execution time: " + duration + " ms");
        
        logger.error("Test failed: {}", testName, throwable);
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ExtentTest test = extentTest.get();
        
        test.log(Status.SKIP, MarkupHelper.createLabel("Test Skipped: " + testName, ExtentColor.YELLOW));
        
        // Log skip reason
        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            test.log(Status.SKIP, "Skip reason: " + throwable.getMessage());
        }
        
        logger.warn("Test skipped: {}", testName);
    }
    
    /**
     * Get current ExtentTest instance
     * @return Current ExtentTest instance
     */
    public static ExtentTest getCurrentTest() {
        return extentTest.get();
    }
    
    /**
     * Log information to current test
     * @param message Log message
     */
    public static void logInfo(String message) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.log(Status.INFO, message);
        }
    }
    
    /**
     * Log pass message to current test
     * @param message Log message
     */
    public static void logPass(String message) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.log(Status.PASS, message);
        }
    }
    
    /**
     * Log fail message to current test
     * @param message Log message
     */
    public static void logFail(String message) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.log(Status.FAIL, message);
        }
    }
    
    /**
     * Log warning message to current test
     * @param message Log message
     */
    public static void logWarning(String message) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.log(Status.WARNING, message);
        }
    }
    
    /**
     * Add screenshot to current test
     * @param screenshotPath Path to screenshot
     * @param title Screenshot title
     */
    public static void addScreenshot(String screenshotPath, String title) {
        ExtentTest test = extentTest.get();
        if (test != null && screenshotPath != null) {
            try {
                test.addScreenCaptureFromPath(screenshotPath, title);
            } catch (Exception e) {
                logger.error("Failed to add screenshot to report: {}", screenshotPath, e);
            }
        }
    }
    
    /**
     * Flush the ExtentReports
     */
    public static void flushReports() {
        if (extentReports != null) {
            extentReports.flush();
            logger.info("ExtentReports flushed successfully");
        }
    }
    
    /**
     * Create a child test node
     * @param name Child test name
     * @param description Child test description
     * @return ExtentTest child node
     */
    public static ExtentTest createChildTest(String name, String description) {
        ExtentTest parentTest = extentTest.get();
        if (parentTest != null) {
            return parentTest.createNode(name, description);
        }
        return null;
    }
}