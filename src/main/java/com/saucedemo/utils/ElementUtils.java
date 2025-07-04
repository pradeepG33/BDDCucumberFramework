package com.saucedemo.utils;

import com.saucedemo.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Element utilities for enhanced WebElement interactions
 */
public class ElementUtils {
    private static final Logger logger = LogManager.getLogger(ElementUtils.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    
    /**
     * Get WebDriverWait instance
     * @param driver WebDriver instance
     * @return WebDriverWait instance
     */
    private static WebDriverWait getWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitWait()));
    }
    
    /**
     * Wait for element to be visible and return it
     * @param driver WebDriver instance
     * @param locator Element locator
     * @return WebElement
     */
    public static WebElement waitForElementToBeVisible(WebDriver driver, By locator) {
        try {
            WebElement element = getWait(driver).until(ExpectedConditions.visibilityOfElementLocated(locator));
            logger.debug("Element visible: {}", locator);
            return element;
        } catch (TimeoutException e) {
            logger.error("Element not visible within timeout: {}", locator);
            throw new TimeoutException("Element not visible: " + locator, e);
        }
    }
    
    /**
     * Wait for element to be clickable and return it
     * @param driver WebDriver instance
     * @param locator Element locator
     * @return WebElement
     */
    public static WebElement waitForElementToBeClickable(WebDriver driver, By locator) {
        try {
            WebElement element = getWait(driver).until(ExpectedConditions.elementToBeClickable(locator));
            logger.debug("Element clickable: {}", locator);
            return element;
        } catch (TimeoutException e) {
            logger.error("Element not clickable within timeout: {}", locator);
            throw new TimeoutException("Element not clickable: " + locator, e);
        }
    }
    
    /**
     * Wait for element presence and return it
     * @param driver WebDriver instance
     * @param locator Element locator
     * @return WebElement
     */
    public static WebElement waitForElementPresence(WebDriver driver, By locator) {
        try {
            WebElement element = getWait(driver).until(ExpectedConditions.presenceOfElementLocated(locator));
            logger.debug("Element present: {}", locator);
            return element;
        } catch (TimeoutException e) {
            logger.error("Element not present within timeout: {}", locator);
            throw new TimeoutException("Element not present: " + locator, e);
        }
    }
    
    /**
     * Wait for elements to be visible and return them
     * @param driver WebDriver instance
     * @param locator Element locator
     * @return List of WebElements
     */
    public static List<WebElement> waitForElementsToBeVisible(WebDriver driver, By locator) {
        try {
            List<WebElement> elements = getWait(driver).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
            logger.debug("Elements visible: {} (count: {})", locator, elements.size());
            return elements;
        } catch (TimeoutException e) {
            logger.error("Elements not visible within timeout: {}", locator);
            throw new TimeoutException("Elements not visible: " + locator, e);
        }
    }
    
    /**
     * Wait for element to disappear
     * @param driver WebDriver instance
     * @param locator Element locator
     * @return true if element disappears, false otherwise
     */
    public static boolean waitForElementToDisappear(WebDriver driver, By locator) {
        try {
            boolean disappeared = getWait(driver).until(ExpectedConditions.invisibilityOfElementLocated(locator));
            logger.debug("Element disappeared: {}", locator);
            return disappeared;
        } catch (TimeoutException e) {
            logger.error("Element did not disappear within timeout: {}", locator);
            return false;
        }
    }
    
    /**
     * Check if element is present
     * @param driver WebDriver instance
     * @param locator Element locator
     * @return true if present, false otherwise
     */
    public static boolean isElementPresent(WebDriver driver, By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
    
    /**
     * Check if element is visible
     * @param driver WebDriver instance
     * @param locator Element locator
     * @return true if visible, false otherwise
     */
    public static boolean isElementVisible(WebDriver driver, By locator) {
        try {
            WebElement element = driver.findElement(locator);
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }
    
    /**
     * Click element with retry mechanism
     * @param driver WebDriver instance
     * @param locator Element locator
     */
    public static void clickElement(WebDriver driver, By locator) {
        int attempts = 0;
        int maxAttempts = 3;
        
        while (attempts < maxAttempts) {
            try {
                WebElement element = waitForElementToBeClickable(driver, locator);
                element.click();
                logger.info("Element clicked successfully: {}", locator);
                return;
            } catch (StaleElementReferenceException | ElementClickInterceptedException e) {
                attempts++;
                logger.warn("Click attempt {} failed for element: {}, retrying...", attempts, locator);
                if (attempts >= maxAttempts) {
                    logger.error("Failed to click element after {} attempts: {}", maxAttempts, locator);
                    throw new RuntimeException("Failed to click element: " + locator, e);
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
    
    /**
     * Click element using JavaScript
     * @param driver WebDriver instance
     * @param locator Element locator
     */
    public static void clickElementJS(WebDriver driver, By locator) {
        try {
            WebElement element = waitForElementPresence(driver, locator);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", element);
            logger.info("Element clicked using JavaScript: {}", locator);
        } catch (Exception e) {
            logger.error("Failed to click element using JavaScript: {}", locator, e);
            throw new RuntimeException("Failed to click element with JS: " + locator, e);
        }
    }
    
    /**
     * Type text into element
     * @param driver WebDriver instance
     * @param locator Element locator
     * @param text Text to type
     */
    public static void typeText(WebDriver driver, By locator, String text) {
        try {
            WebElement element = waitForElementToBeVisible(driver, locator);
            element.clear();
            element.sendKeys(text);
            logger.info("Text entered in element {}: {}", locator, text);
        } catch (Exception e) {
            logger.error("Failed to type text in element: {}", locator, e);
            throw new RuntimeException("Failed to type text: " + locator, e);
        }
    }
    
    /**
     * Get text from element
     * @param driver WebDriver instance
     * @param locator Element locator
     * @return Element text
     */
    public static String getElementText(WebDriver driver, By locator) {
        try {
            WebElement element = waitForElementToBeVisible(driver, locator);
            String text = element.getText();
            logger.debug("Element text retrieved from {}: {}", locator, text);
            return text;
        } catch (Exception e) {
            logger.error("Failed to get text from element: {}", locator, e);
            throw new RuntimeException("Failed to get text: " + locator, e);
        }
    }
    
    /**
     * Get attribute value from element
     * @param driver WebDriver instance
     * @param locator Element locator
     * @param attributeName Attribute name
     * @return Attribute value
     */
    public static String getElementAttribute(WebDriver driver, By locator, String attributeName) {
        try {
            WebElement element = waitForElementToBeVisible(driver, locator);
            String attributeValue = element.getAttribute(attributeName);
            logger.debug("Element attribute {} retrieved from {}: {}", attributeName, locator, attributeValue);
            return attributeValue;
        } catch (Exception e) {
            logger.error("Failed to get attribute {} from element: {}", attributeName, locator, e);
            throw new RuntimeException("Failed to get attribute: " + locator, e);
        }
    }
    
    /**
     * Select dropdown option by visible text
     * @param driver WebDriver instance
     * @param locator Dropdown locator
     * @param visibleText Visible text to select
     */
    public static void selectDropdownByText(WebDriver driver, By locator, String visibleText) {
        try {
            WebElement dropdownElement = waitForElementToBeVisible(driver, locator);
            Select dropdown = new Select(dropdownElement);
            dropdown.selectByVisibleText(visibleText);
            logger.info("Dropdown option selected by text {}: {}", locator, visibleText);
        } catch (Exception e) {
            logger.error("Failed to select dropdown option by text: {}", locator, e);
            throw new RuntimeException("Failed to select dropdown: " + locator, e);
        }
    }
    
    /**
     * Select dropdown option by value
     * @param driver WebDriver instance
     * @param locator Dropdown locator
     * @param value Value to select
     */
    public static void selectDropdownByValue(WebDriver driver, By locator, String value) {
        try {
            WebElement dropdownElement = waitForElementToBeVisible(driver, locator);
            Select dropdown = new Select(dropdownElement);
            dropdown.selectByValue(value);
            logger.info("Dropdown option selected by value {}: {}", locator, value);
        } catch (Exception e) {
            logger.error("Failed to select dropdown option by value: {}", locator, e);
            throw new RuntimeException("Failed to select dropdown: " + locator, e);
        }
    }
    
    /**
     * Hover over element
     * @param driver WebDriver instance
     * @param locator Element locator
     */
    public static void hoverOverElement(WebDriver driver, By locator) {
        try {
            WebElement element = waitForElementToBeVisible(driver, locator);
            Actions actions = new Actions(driver);
            actions.moveToElement(element).perform();
            logger.info("Hovered over element: {}", locator);
        } catch (Exception e) {
            logger.error("Failed to hover over element: {}", locator, e);
            throw new RuntimeException("Failed to hover: " + locator, e);
        }
    }
    
    /**
     * Scroll to element
     * @param driver WebDriver instance
     * @param locator Element locator
     */
    public static void scrollToElement(WebDriver driver, By locator) {
        try {
            WebElement element = waitForElementPresence(driver, locator);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", element);
            logger.info("Scrolled to element: {}", locator);
        } catch (Exception e) {
            logger.error("Failed to scroll to element: {}", locator, e);
            throw new RuntimeException("Failed to scroll: " + locator, e);
        }
    }
    
    /**
     * Wait for text to be present in element
     * @param driver WebDriver instance
     * @param locator Element locator
     * @param text Expected text
     * @return true if text is present, false otherwise
     */
    public static boolean waitForTextToBePresentInElement(WebDriver driver, By locator, String text) {
        try {
            boolean textPresent = getWait(driver).until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
            logger.debug("Text '{}' present in element: {}", text, locator);
            return textPresent;
        } catch (TimeoutException e) {
            logger.error("Text '{}' not present in element within timeout: {}", text, locator);
            return false;
        }
    }
    
    /**
     * Get count of elements matching locator
     * @param driver WebDriver instance
     * @param locator Element locator
     * @return Count of elements
     */
    public static int getElementCount(WebDriver driver, By locator) {
        try {
            List<WebElement> elements = driver.findElements(locator);
            int count = elements.size();
            logger.debug("Element count for {}: {}", locator, count);
            return count;
        } catch (Exception e) {
            logger.error("Failed to get element count: {}", locator, e);
            return 0;
        }
    }
    
    /**
     * Check if element is enabled
     * @param driver WebDriver instance
     * @param locator Element locator
     * @return true if enabled, false otherwise
     */
    public static boolean isElementEnabled(WebDriver driver, By locator) {
        try {
            WebElement element = waitForElementPresence(driver, locator);
            boolean enabled = element.isEnabled();
            logger.debug("Element enabled status for {}: {}", locator, enabled);
            return enabled;
        } catch (Exception e) {
            logger.error("Failed to check if element is enabled: {}", locator, e);
            return false;
        }
    }
    
    /**
     * Check if element is selected (for checkboxes/radio buttons)
     * @param driver WebDriver instance
     * @param locator Element locator
     * @return true if selected, false otherwise
     */
    public static boolean isElementSelected(WebDriver driver, By locator) {
        try {
            WebElement element = waitForElementPresence(driver, locator);
            boolean selected = element.isSelected();
            logger.debug("Element selected status for {}: {}", locator, selected);
            return selected;
        } catch (Exception e) {
            logger.error("Failed to check if element is selected: {}", locator, e);
            return false;
        }
    }
}