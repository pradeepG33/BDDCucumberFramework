package com.saucedemo.utils;

import com.saucedemo.config.ConfigManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * WebDriver Manager for handling browser instances
 * Supports multiple browsers and parallel execution using ThreadLocal
 */
public class DriverManager {
    private static final Logger logger = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final ConfigManager config = ConfigManager.getInstance();
    
    /**
     * Initialize WebDriver based on browser configuration
     * @param browserName Browser name (chrome, firefox, edge, safari)
     */
    public static void initializeDriver(String browserName) {
        if (browserName == null) {
            browserName = config.getBrowser();
        }
        
        logger.info("Initializing {} driver", browserName);
        
        WebDriver driver;
        
        if (config.isGridEnabled()) {
            driver = createRemoteDriver(browserName);
        } else {
            driver = createLocalDriver(browserName);
        }
        
        // Configure driver timeouts
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(config.getPageLoadTimeout()));
        driver.manage().window().maximize();
        
        driverThreadLocal.set(driver);
        logger.info("{} driver initialized successfully", browserName);
    }
    
    /**
     * Create local WebDriver instance
     * @param browserName Browser name
     * @return WebDriver instance
     */
    private static WebDriver createLocalDriver(String browserName) {
        switch (browserName.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                return new ChromeDriver(getChromeOptions());
                
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                return new FirefoxDriver(getFirefoxOptions());
                
            case "edge":
                WebDriverManager.edgedriver().setup();
                return new EdgeDriver(getEdgeOptions());
                
            case "safari":
                return new SafariDriver();
                
            default:
                logger.error("Unsupported browser: {}", browserName);
                throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }
    }
    
    /**
     * Create remote WebDriver instance for Selenium Grid
     * @param browserName Browser name
     * @return WebDriver instance
     */
    private static WebDriver createRemoteDriver(String browserName) {
        try {
            URL gridUrl = new URL(config.getGridHubUrl());
            
            switch (browserName.toLowerCase()) {
                case "chrome":
                    return new RemoteWebDriver(gridUrl, getChromeOptions());
                    
                case "firefox":
                    return new RemoteWebDriver(gridUrl, getFirefoxOptions());
                    
                case "edge":
                    return new RemoteWebDriver(gridUrl, getEdgeOptions());
                    
                default:
                    logger.error("Unsupported browser for grid execution: {}", browserName);
                    throw new IllegalArgumentException("Unsupported browser for grid: " + browserName);
            }
        } catch (MalformedURLException e) {
            logger.error("Invalid Grid Hub URL: {}", config.getGridHubUrl(), e);
            throw new RuntimeException("Invalid Grid Hub URL", e);
        }
    }
    
    /**
     * Get Chrome options
     * @return ChromeOptions
     */
    private static ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        
        if (config.isHeadless()) {
            options.addArguments("--headless");
        }
        
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--remote-allow-origins=*");
        
        // Performance optimizations
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("profile.managed_default_content_settings.images", 2);
        options.setExperimentalOption("prefs", prefs);
        
        return options;
    }
    
    /**
     * Get Firefox options
     * @return FirefoxOptions
     */
    private static FirefoxOptions getFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        
        if (config.isHeadless()) {
            options.addArguments("--headless");
        }
        
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        
        return options;
    }
    
    /**
     * Get Edge options
     * @return EdgeOptions
     */
    private static EdgeOptions getEdgeOptions() {
        EdgeOptions options = new EdgeOptions();
        
        if (config.isHeadless()) {
            options.addArguments("--headless");
        }
        
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-allow-origins=*");
        
        return options;
    }
    
    /**
     * Get current thread's WebDriver instance
     * @return WebDriver instance
     */
    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            logger.error("WebDriver not initialized for current thread");
            throw new RuntimeException("WebDriver not initialized. Call initializeDriver() first.");
        }
        return driver;
    }
    
    /**
     * Quit current thread's WebDriver and remove from ThreadLocal
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
                logger.info("WebDriver quit successfully");
            } catch (Exception e) {
                logger.error("Error while quitting WebDriver", e);
            } finally {
                driverThreadLocal.remove();
            }
        }
    }
    
    /**
     * Close current browser window
     */
    public static void closeDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.close();
                logger.info("WebDriver closed successfully");
            } catch (Exception e) {
                logger.error("Error while closing WebDriver", e);
            }
        }
    }
    
    /**
     * Check if WebDriver is initialized for current thread
     * @return true if initialized, false otherwise
     */
    public static boolean isDriverInitialized() {
        return driverThreadLocal.get() != null;
    }
    
    /**
     * Get current browser name
     * @return Browser name
     */
    public static String getCurrentBrowser() {
        WebDriver driver = getDriver();
        return ((RemoteWebDriver) driver).getCapabilities().getBrowserName();
    }
    
    /**
     * Refresh current page
     */
    public static void refreshPage() {
        getDriver().navigate().refresh();
        logger.info("Page refreshed");
    }
    
    /**
     * Navigate back
     */
    public static void navigateBack() {
        getDriver().navigate().back();
        logger.info("Navigated back");
    }
    
    /**
     * Navigate forward
     */
    public static void navigateForward() {
        getDriver().navigate().forward();
        logger.info("Navigated forward");
    }
}