package com.saucedemo.pages;

import com.saucedemo.config.ConfigManager;
import com.saucedemo.utils.ElementUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Base Page class containing common elements and methods
 */
public abstract class BasePage {
    protected static final Logger logger = LogManager.getLogger(BasePage.class);
    protected WebDriver driver;
    protected ConfigManager config;
    
    // Common locators
    protected static final By MENU_BUTTON = By.id("react-burger-menu-btn");
    protected static final By MENU_CLOSE_BUTTON = By.id("react-burger-cross-btn");
    protected static final By CART_BADGE = By.className("shopping_cart_badge");
    protected static final By CART_LINK = By.className("shopping_cart_link");
    
    // Menu items
    protected static final By MENU_ALL_ITEMS = By.id("inventory_sidebar_link");
    protected static final By MENU_ABOUT = By.id("about_sidebar_link");
    protected static final By MENU_LOGOUT = By.id("logout_sidebar_link");
    protected static final By MENU_RESET_APP = By.id("reset_sidebar_link");
    
    // Common elements using PageFactory
    @FindBy(id = "react-burger-menu-btn")
    protected WebElement menuButton;
    
    @FindBy(id = "react-burger-cross-btn")
    protected WebElement menuCloseButton;
    
    @FindBy(className = "shopping_cart_badge")
    protected WebElement cartBadge;
    
    @FindBy(className = "shopping_cart_link")
    protected WebElement cartLink;
    
    @FindBy(id = "inventory_sidebar_link")
    protected WebElement menuAllItems;
    
    @FindBy(id = "about_sidebar_link")
    protected WebElement menuAbout;
    
    @FindBy(id = "logout_sidebar_link")
    protected WebElement menuLogout;
    
    @FindBy(id = "reset_sidebar_link")
    protected WebElement menuResetApp;
    
    /**
     * Constructor
     * @param driver WebDriver instance
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.config = ConfigManager.getInstance();
        PageFactory.initElements(driver, this);
        logger.debug("Initialized page: {}", this.getClass().getSimpleName());
    }
    
    /**
     * Get page title
     * @return Page title
     */
    public String getPageTitle() {
        String title = driver.getTitle();
        logger.debug("Page title: {}", title);
        return title;
    }
    
    /**
     * Get current URL
     * @return Current URL
     */
    public String getCurrentUrl() {
        String url = driver.getCurrentUrl();
        logger.debug("Current URL: {}", url);
        return url;
    }
    
    /**
     * Open menu sidebar
     */
    public void openMenu() {
        try {
            ElementUtils.clickElement(driver, MENU_BUTTON);
            logger.info("Menu opened");
        } catch (Exception e) {
            logger.error("Failed to open menu", e);
            throw new RuntimeException("Failed to open menu", e);
        }
    }
    
    /**
     * Close menu sidebar
     */
    public void closeMenu() {
        try {
            ElementUtils.clickElement(driver, MENU_CLOSE_BUTTON);
            logger.info("Menu closed");
        } catch (Exception e) {
            logger.error("Failed to close menu", e);
            throw new RuntimeException("Failed to close menu", e);
        }
    }
    
    /**
     * Click on cart icon
     */
    public void clickCart() {
        try {
            ElementUtils.clickElement(driver, CART_LINK);
            logger.info("Cart clicked");
        } catch (Exception e) {
            logger.error("Failed to click cart", e);
            throw new RuntimeException("Failed to click cart", e);
        }
    }
    
    /**
     * Get cart badge count
     * @return Cart badge count as string, or empty string if no badge
     */
    public String getCartBadgeCount() {
        try {
            if (ElementUtils.isElementVisible(driver, CART_BADGE)) {
                String count = ElementUtils.getElementText(driver, CART_BADGE);
                logger.debug("Cart badge count: {}", count);
                return count;
            }
            return "";
        } catch (Exception e) {
            logger.debug("No cart badge visible");
            return "";
        }
    }
    
    /**
     * Get cart badge count as integer
     * @return Cart badge count as integer, or 0 if no badge
     */
    public int getCartBadgeCountAsInt() {
        String count = getCartBadgeCount();
        if (count.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(count);
        } catch (NumberFormatException e) {
            logger.error("Invalid cart badge count: {}", count);
            return 0;
        }
    }
    
    /**
     * Check if cart badge is visible
     * @return true if cart badge is visible, false otherwise
     */
    public boolean isCartBadgeVisible() {
        boolean visible = ElementUtils.isElementVisible(driver, CART_BADGE);
        logger.debug("Cart badge visible: {}", visible);
        return visible;
    }
    
    /**
     * Navigate to All Items page via menu
     */
    public void navigateToAllItems() {
        try {
            openMenu();
            ElementUtils.clickElement(driver, MENU_ALL_ITEMS);
            logger.info("Navigated to All Items");
        } catch (Exception e) {
            logger.error("Failed to navigate to All Items", e);
            throw new RuntimeException("Failed to navigate to All Items", e);
        }
    }
    
    /**
     * Navigate to About page via menu
     */
    public void navigateToAbout() {
        try {
            openMenu();
            ElementUtils.clickElement(driver, MENU_ABOUT);
            logger.info("Navigated to About page");
        } catch (Exception e) {
            logger.error("Failed to navigate to About page", e);
            throw new RuntimeException("Failed to navigate to About page", e);
        }
    }
    
    /**
     * Logout via menu
     */
    public void logout() {
        try {
            openMenu();
            ElementUtils.clickElement(driver, MENU_LOGOUT);
            logger.info("Logged out successfully");
        } catch (Exception e) {
            logger.error("Failed to logout", e);
            throw new RuntimeException("Failed to logout", e);
        }
    }
    
    /**
     * Reset app state via menu
     */
    public void resetAppState() {
        try {
            openMenu();
            ElementUtils.clickElement(driver, MENU_RESET_APP);
            logger.info("App state reset");
        } catch (Exception e) {
            logger.error("Failed to reset app state", e);
            throw new RuntimeException("Failed to reset app state", e);
        }
    }
    
    /**
     * Wait for page to load
     */
    public abstract void waitForPageToLoad();
    
    /**
     * Verify page is loaded correctly
     * @return true if page is loaded, false otherwise
     */
    public abstract boolean isPageLoaded();
    
    /**
     * Get page URL
     * @return Expected page URL
     */
    public abstract String getPageUrl();
    
    /**
     * Navigate to this page
     */
    public void navigateToPage() {
        String url = getPageUrl();
        driver.get(url);
        logger.info("Navigated to page: {}", url);
        waitForPageToLoad();
    }
    
    /**
     * Refresh current page
     */
    public void refreshPage() {
        driver.navigate().refresh();
        logger.info("Page refreshed");
        waitForPageToLoad();
    }
    
    /**
     * Navigate back
     */
    public void navigateBack() {
        driver.navigate().back();
        logger.info("Navigated back");
    }
    
    /**
     * Navigate forward
     */
    public void navigateForward() {
        driver.navigate().forward();
        logger.info("Navigated forward");
    }
    
    /**
     * Check if element is present on page
     * @param locator Element locator
     * @return true if element is present, false otherwise
     */
    protected boolean isElementPresent(By locator) {
        return ElementUtils.isElementPresent(driver, locator);
    }
    
    /**
     * Check if element is visible on page
     * @param locator Element locator
     * @return true if element is visible, false otherwise
     */
    protected boolean isElementVisible(By locator) {
        return ElementUtils.isElementVisible(driver, locator);
    }
    
    /**
     * Get text from element
     * @param locator Element locator
     * @return Element text
     */
    protected String getElementText(By locator) {
        return ElementUtils.getElementText(driver, locator);
    }
    
    /**
     * Click element
     * @param locator Element locator
     */
    protected void clickElement(By locator) {
        ElementUtils.clickElement(driver, locator);
    }
    
    /**
     * Type text into element
     * @param locator Element locator
     * @param text Text to type
     */
    protected void typeText(By locator, String text) {
        ElementUtils.typeText(driver, locator, text);
    }
    
    /**
     * Wait for element to be visible
     * @param locator Element locator
     * @return WebElement
     */
    protected WebElement waitForElementToBeVisible(By locator) {
        return ElementUtils.waitForElementToBeVisible(driver, locator);
    }
    
    /**
     * Wait for element to be clickable
     * @param locator Element locator
     * @return WebElement
     */
    protected WebElement waitForElementToBeClickable(By locator) {
        return ElementUtils.waitForElementToBeClickable(driver, locator);
    }
}