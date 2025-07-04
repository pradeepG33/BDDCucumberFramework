package com.saucedemo.listeners;

import com.saucedemo.utils.DriverManager;
import com.saucedemo.utils.ScreenshotUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Screenshot Listener for capturing screenshots on test events
 */
public class ScreenshotListener implements ITestListener {
    private static final Logger logger = LogManager.getLogger(ScreenshotListener.class);
    
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.debug("Screenshot listener - Test started: {}", testName);
        
        // Ensure screenshot directory exists
        ScreenshotUtils.createScreenshotDirectory();
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        
        try {
            if (DriverManager.isDriverInitialized()) {
                String screenshotPath = ScreenshotUtils.captureScreenshot(DriverManager.getDriver(), "SUCCESS_" + testName);
                if (screenshotPath != null) {
                    logger.info("Success screenshot captured for test: {}", testName);
                    
                    // Add screenshot to ExtentReports
                    ExtentReportListener.addScreenshot(screenshotPath, "Test Success Screenshot");
                }
            }
        } catch (Exception e) {
            logger.error("Failed to capture success screenshot for test: {}", testName, e);
        }
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        
        try {
            if (DriverManager.isDriverInitialized()) {
                String screenshotPath = ScreenshotUtils.captureFailureScreenshot(DriverManager.getDriver(), testName);
                if (screenshotPath != null) {
                    logger.info("Failure screenshot captured for test: {}", testName);
                    
                    // Add screenshot to ExtentReports
                    ExtentReportListener.addScreenshot(screenshotPath, "Test Failure Screenshot");
                    
                    // Log screenshot path for debugging
                    ExtentReportListener.logInfo("Failure screenshot saved at: " + screenshotPath);
                }
            }
        } catch (Exception e) {
            logger.error("Failed to capture failure screenshot for test: {}", testName, e);
            ExtentReportListener.logWarning("Failed to capture failure screenshot: " + e.getMessage());
        }
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        
        try {
            if (DriverManager.isDriverInitialized()) {
                String screenshotPath = ScreenshotUtils.captureScreenshot(DriverManager.getDriver(), "SKIPPED_" + testName);
                if (screenshotPath != null) {
                    logger.info("Skipped test screenshot captured for test: {}", testName);
                    
                    // Add screenshot to ExtentReports
                    ExtentReportListener.addScreenshot(screenshotPath, "Test Skipped Screenshot");
                }
            }
        } catch (Exception e) {
            logger.error("Failed to capture skipped test screenshot for test: {}", testName, e);
        }
    }
    
    /**
     * Capture screenshot for specific test step
     * @param stepName Step name
     * @return Screenshot path
     */
    public static String captureStepScreenshot(String stepName) {
        try {
            if (DriverManager.isDriverInitialized()) {
                String screenshotPath = ScreenshotUtils.captureStepScreenshot(DriverManager.getDriver(), stepName);
                if (screenshotPath != null) {
                    logger.debug("Step screenshot captured: {}", stepName);
                    
                    // Add screenshot to ExtentReports
                    ExtentReportListener.addScreenshot(screenshotPath, "Step: " + stepName);
                    return screenshotPath;
                }
            }
        } catch (Exception e) {
            logger.error("Failed to capture step screenshot: {}", stepName, e);
        }
        return null;
    }
    
    /**
     * Capture screenshot with custom name
     * @param customName Custom screenshot name
     * @return Screenshot path
     */
    public static String captureCustomScreenshot(String customName) {
        try {
            if (DriverManager.isDriverInitialized()) {
                String screenshotPath = ScreenshotUtils.captureScreenshot(DriverManager.getDriver(), customName);
                if (screenshotPath != null) {
                    logger.debug("Custom screenshot captured: {}", customName);
                    
                    // Add screenshot to ExtentReports
                    ExtentReportListener.addScreenshot(screenshotPath, customName);
                    return screenshotPath;
                }
            }
        } catch (Exception e) {
            logger.error("Failed to capture custom screenshot: {}", customName, e);
        }
        return null;
    }
    
    /**
     * Clean up old screenshots
     */
    public static void cleanupOldScreenshots() {
        try {
            // Delete screenshots older than 7 days
            ScreenshotUtils.deleteOldScreenshots(7);
            logger.info("Old screenshots cleanup completed");
        } catch (Exception e) {
            logger.error("Failed to cleanup old screenshots", e);
        }
    }
}