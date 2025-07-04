package com.saucedemo.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration Manager for handling application properties
 * Singleton pattern implementation for global configuration access
 */
public class ConfigManager {
    private static final Logger logger = LogManager.getLogger(ConfigManager.class);
    private static ConfigManager instance;
    private Properties properties;
    
    private static final String CONFIG_FILE_PATH = "src/test/resources/config/config.properties";
    
    private ConfigManager() {
        loadProperties();
    }
    
    /**
     * Get singleton instance of ConfigManager
     * @return ConfigManager instance
     */
    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }
    
    /**
     * Load properties from configuration file
     */
    private void loadProperties() {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(fis);
            logger.info("Configuration properties loaded successfully from: {}", CONFIG_FILE_PATH);
        } catch (IOException e) {
            logger.error("Failed to load configuration properties from: {}", CONFIG_FILE_PATH, e);
            throw new RuntimeException("Could not load configuration properties", e);
        }
    }
    
    /**
     * Get property value by key
     * @param key Property key
     * @return Property value
     */
    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            logger.warn("Property not found for key: {}", key);
        }
        return value;
    }
    
    /**
     * Get property value by key with default value
     * @param key Property key
     * @param defaultValue Default value if key not found
     * @return Property value or default value
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Get property as integer
     * @param key Property key
     * @return Integer value
     */
    public int getIntProperty(String key) {
        String value = getProperty(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.error("Invalid integer value for key: {} = {}", key, value);
            throw new IllegalArgumentException("Invalid integer property: " + key, e);
        }
    }
    
    /**
     * Get property as boolean
     * @param key Property key
     * @return Boolean value
     */
    public boolean getBooleanProperty(String key) {
        String value = getProperty(key);
        return Boolean.parseBoolean(value);
    }
    
    // Application Configuration Methods
    public String getAppUrl() {
        return getProperty("app.url");
    }
    
    public String getAppTitle() {
        return getProperty("app.title");
    }
    
    public String getBrowser() {
        return getProperty("browser", "chrome");
    }
    
    public boolean isHeadless() {
        return getBooleanProperty("headless");
    }
    
    public int getImplicitWait() {
        return getIntProperty("implicit.wait");
    }
    
    public int getExplicitWait() {
        return getIntProperty("explicit.wait");
    }
    
    public int getPageLoadTimeout() {
        return getIntProperty("page.load.timeout");
    }
    
    public String getEnvironment() {
        return getProperty("environment");
    }
    
    public String getTestDataPath() {
        return getProperty("test.data.path");
    }
    
    public String getScreenshotPath() {
        return getProperty("screenshot.path");
    }
    
    public String getReportsPath() {
        return getProperty("reports.path");
    }
    
    public String getExtentReportName() {
        return getProperty("extent.report.name");
    }
    
    public String getExtentReportTitle() {
        return getProperty("extent.report.title");
    }
    
    public String getExtentReportTheme() {
        return getProperty("extent.report.theme");
    }
    
    public int getRetryCount() {
        return getIntProperty("retry.count");
    }
    
    public int getThreadCount() {
        return getIntProperty("thread.count");
    }
    
    public String getParallelMode() {
        return getProperty("parallel.mode");
    }
    
    // User Credentials
    public String getStandardUser() {
        return getProperty("standard.user");
    }
    
    public String getLockedUser() {
        return getProperty("locked.user");
    }
    
    public String getProblemUser() {
        return getProperty("problem.user");
    }
    
    public String getPerformanceUser() {
        return getProperty("performance.user");
    }
    
    public String getErrorUser() {
        return getProperty("error.user");
    }
    
    public String getVisualUser() {
        return getProperty("visual.user");
    }
    
    public String getPassword() {
        return getProperty("password");
    }
    
    // Grid Configuration
    public boolean isGridEnabled() {
        return getBooleanProperty("grid.enabled");
    }
    
    public String getGridHubUrl() {
        return getProperty("grid.hub.url");
    }
    
    // Mobile Configuration
    public boolean isMobileEnabled() {
        return getBooleanProperty("mobile.enabled");
    }
    
    public String getDeviceName() {
        return getProperty("device.name");
    }
    
    public String getPlatformName() {
        return getProperty("platform.name");
    }
    
    public String getPlatformVersion() {
        return getProperty("platform.version");
    }
    
    /**
     * Reload properties from file
     */
    public void reloadProperties() {
        loadProperties();
        logger.info("Configuration properties reloaded");
    }
}