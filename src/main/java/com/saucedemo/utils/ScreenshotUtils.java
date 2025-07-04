package com.saucedemo.utils;

import com.saucedemo.config.ConfigManager;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Screenshot utilities for capturing and managing screenshots
 */
public class ScreenshotUtils {
    private static final Logger logger = LogManager.getLogger(ScreenshotUtils.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    
    /**
     * Capture screenshot of entire page
     * @param driver WebDriver instance
     * @param screenshotName Name for the screenshot
     * @return Path to the screenshot file
     */
    public static String captureScreenshot(WebDriver driver, String screenshotName) {
        try {
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
            
            String timestamp = LocalDateTime.now().format(formatter);
            String fileName = screenshotName + "_" + timestamp + ".png";
            String screenshotPath = config.getScreenshotPath() + fileName;
            
            // Create directory if it doesn't exist
            File screenshotDir = new File(config.getScreenshotPath());
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }
            
            File destFile = new File(screenshotPath);
            FileUtils.copyFile(sourceFile, destFile);
            
            logger.info("Screenshot captured: {}", screenshotPath);
            return screenshotPath;
            
        } catch (IOException e) {
            logger.error("Failed to capture screenshot: {}", screenshotName, e);
            return null;
        }
    }
    
    /**
     * Capture screenshot for failed test
     * @param driver WebDriver instance
     * @param testName Test method name
     * @return Path to the screenshot file
     */
    public static String captureFailureScreenshot(WebDriver driver, String testName) {
        return captureScreenshot(driver, "FAILED_" + testName);
    }
    
    /**
     * Capture screenshot of specific web element
     * @param element WebElement to capture
     * @param screenshotName Name for the screenshot
     * @return Path to the screenshot file
     */
    public static String captureElementScreenshot(WebElement element, String screenshotName) {
        try {
            File sourceFile = element.getScreenshotAs(OutputType.FILE);
            
            String timestamp = LocalDateTime.now().format(formatter);
            String fileName = "ELEMENT_" + screenshotName + "_" + timestamp + ".png";
            String screenshotPath = config.getScreenshotPath() + fileName;
            
            // Create directory if it doesn't exist
            File screenshotDir = new File(config.getScreenshotPath());
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }
            
            File destFile = new File(screenshotPath);
            FileUtils.copyFile(sourceFile, destFile);
            
            logger.info("Element screenshot captured: {}", screenshotPath);
            return screenshotPath;
            
        } catch (IOException e) {
            logger.error("Failed to capture element screenshot: {}", screenshotName, e);
            return null;
        }
    }
    
    /**
     * Capture screenshot as base64 string
     * @param driver WebDriver instance
     * @return Base64 encoded screenshot string
     */
    public static String captureScreenshotAsBase64(WebDriver driver) {
        try {
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            String base64Screenshot = takesScreenshot.getScreenshotAs(OutputType.BASE64);
            
            logger.info("Screenshot captured as base64 string");
            return base64Screenshot;
            
        } catch (Exception e) {
            logger.error("Failed to capture screenshot as base64", e);
            return null;
        }
    }
    
    /**
     * Capture screenshot for test step
     * @param driver WebDriver instance
     * @param stepName Step name
     * @return Path to the screenshot file
     */
    public static String captureStepScreenshot(WebDriver driver, String stepName) {
        return captureScreenshot(driver, "STEP_" + stepName);
    }
    
    /**
     * Get screenshot file name with timestamp
     * @param baseName Base name for the screenshot
     * @return Full file name with timestamp
     */
    public static String getScreenshotFileName(String baseName) {
        String timestamp = LocalDateTime.now().format(formatter);
        return baseName + "_" + timestamp + ".png";
    }
    
    /**
     * Get full screenshot path
     * @param fileName Screenshot file name
     * @return Full path to screenshot
     */
    public static String getFullScreenshotPath(String fileName) {
        return config.getScreenshotPath() + fileName;
    }
    
    /**
     * Create screenshot directory if it doesn't exist
     */
    public static void createScreenshotDirectory() {
        File screenshotDir = new File(config.getScreenshotPath());
        if (!screenshotDir.exists()) {
            boolean created = screenshotDir.mkdirs();
            if (created) {
                logger.info("Screenshot directory created: {}", config.getScreenshotPath());
            } else {
                logger.error("Failed to create screenshot directory: {}", config.getScreenshotPath());
            }
        }
    }
    
    /**
     * Delete old screenshots (older than specified days)
     * @param daysOld Number of days
     */
    public static void deleteOldScreenshots(int daysOld) {
        File screenshotDir = new File(config.getScreenshotPath());
        if (!screenshotDir.exists()) {
            return;
        }
        
        long cutoffTime = System.currentTimeMillis() - (daysOld * 24L * 60L * 60L * 1000L);
        int deletedCount = 0;
        
        File[] files = screenshotDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.lastModified() < cutoffTime) {
                    if (file.delete()) {
                        deletedCount++;
                    }
                }
            }
        }
        
        logger.info("Deleted {} old screenshots older than {} days", deletedCount, daysOld);
    }
    
    /**
     * Check if screenshot file exists
     * @param fileName Screenshot file name
     * @return true if file exists, false otherwise
     */
    public static boolean screenshotExists(String fileName) {
        File file = new File(config.getScreenshotPath() + fileName);
        return file.exists();
    }
}