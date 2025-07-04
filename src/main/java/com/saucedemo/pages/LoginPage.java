package com.saucedemo.pages;

import com.saucedemo.utils.ElementUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Login Page class containing login-related elements and methods
 */
public class LoginPage extends BasePage {
    private static final Logger logger = LogManager.getLogger(LoginPage.class);
    
    // Locators
    private static final By USERNAME_FIELD = By.id("user-name");
    private static final By PASSWORD_FIELD = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By ERROR_MESSAGE = By.cssSelector("[data-test='error']");
    private static final By ERROR_CLOSE_BUTTON = By.cssSelector(".error-button");
    private static final By LOGIN_LOGO = By.className("login_logo");
    private static final By LOGIN_CONTAINER = By.className("login_container");
    private static final By LOGIN_BOT_IMAGE = By.className("bot_column");
    private static final By LOGIN_CREDENTIALS_TEXT = By.id("login_credentials");
    private static final By LOGIN_PASSWORD_TEXT = By.className("login_password");
    
    // Page Factory elements
    @FindBy(id = "user-name")
    private WebElement usernameField;
    
    @FindBy(id = "password")
    private WebElement passwordField;
    
    @FindBy(id = "login-button")
    private WebElement loginButton;
    
    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;
    
    @FindBy(css = ".error-button")
    private WebElement errorCloseButton;
    
    @FindBy(className = "login_logo")
    private WebElement loginLogo;
    
    @FindBy(id = "login_credentials")
    private WebElement loginCredentialsText;
    
    @FindBy(className = "login_password")
    private WebElement loginPasswordText;
    
    /**
     * Constructor
     * @param driver WebDriver instance
     */
    public LoginPage(WebDriver driver) {
        super(driver);
    }
    
    @Override
    public void waitForPageToLoad() {
        waitForElementToBeVisible(LOGIN_LOGO);
        waitForElementToBeVisible(USERNAME_FIELD);
        waitForElementToBeVisible(PASSWORD_FIELD);
        waitForElementToBeVisible(LOGIN_BUTTON);
        logger.info("Login page loaded successfully");
    }
    
    @Override
    public boolean isPageLoaded() {
        try {
            return isElementVisible(LOGIN_LOGO) &&
                   isElementVisible(USERNAME_FIELD) &&
                   isElementVisible(PASSWORD_FIELD) &&
                   isElementVisible(LOGIN_BUTTON);
        } catch (Exception e) {
            logger.error("Error checking if login page is loaded", e);
            return false;
        }
    }
    
    @Override
    public String getPageUrl() {
        return config.getAppUrl();
    }
    
    /**
     * Enter username
     * @param username Username to enter
     */
    public void enterUsername(String username) {
        try {
            typeText(USERNAME_FIELD, username);
            logger.info("Username entered: {}", username);
        } catch (Exception e) {
            logger.error("Failed to enter username: {}", username, e);
            throw new RuntimeException("Failed to enter username", e);
        }
    }
    
    /**
     * Enter password
     * @param password Password to enter
     */
    public void enterPassword(String password) {
        try {
            typeText(PASSWORD_FIELD, password);
            logger.info("Password entered");
        } catch (Exception e) {
            logger.error("Failed to enter password", e);
            throw new RuntimeException("Failed to enter password", e);
        }
    }
    
    /**
     * Click login button
     */
    public void clickLoginButton() {
        try {
            clickElement(LOGIN_BUTTON);
            logger.info("Login button clicked");
        } catch (Exception e) {
            logger.error("Failed to click login button", e);
            throw new RuntimeException("Failed to click login button", e);
        }
    }
    
    /**
     * Perform login with username and password
     * @param username Username
     * @param password Password
     */
    public void login(String username, String password) {
        try {
            enterUsername(username);
            enterPassword(password);
            clickLoginButton();
            logger.info("Login attempted with username: {}", username);
        } catch (Exception e) {
            logger.error("Login failed for username: {}", username, e);
            throw new RuntimeException("Login failed", e);
        }
    }
    
    /**
     * Get error message text
     * @return Error message text
     */
    public String getErrorMessage() {
        try {
            if (isElementVisible(ERROR_MESSAGE)) {
                String errorText = getElementText(ERROR_MESSAGE);
                logger.info("Error message displayed: {}", errorText);
                return errorText;
            }
            return "";
        } catch (Exception e) {
            logger.debug("No error message visible");
            return "";
        }
    }
    
    /**
     * Check if error message is displayed
     * @return true if error message is visible, false otherwise
     */
    public boolean isErrorMessageDisplayed() {
        boolean displayed = isElementVisible(ERROR_MESSAGE);
        logger.debug("Error message displayed: {}", displayed);
        return displayed;
    }
    
    /**
     * Close error message
     */
    public void closeErrorMessage() {
        try {
            if (isElementVisible(ERROR_CLOSE_BUTTON)) {
                clickElement(ERROR_CLOSE_BUTTON);
                logger.info("Error message closed");
            }
        } catch (Exception e) {
            logger.error("Failed to close error message", e);
            throw new RuntimeException("Failed to close error message", e);
        }
    }
    
    /**
     * Clear username field
     */
    public void clearUsername() {
        try {
            ElementUtils.waitForElementToBeVisible(driver, USERNAME_FIELD).clear();
            logger.info("Username field cleared");
        } catch (Exception e) {
            logger.error("Failed to clear username field", e);
            throw new RuntimeException("Failed to clear username field", e);
        }
    }
    
    /**
     * Clear password field
     */
    public void clearPassword() {
        try {
            ElementUtils.waitForElementToBeVisible(driver, PASSWORD_FIELD).clear();
            logger.info("Password field cleared");
        } catch (Exception e) {
            logger.error("Failed to clear password field", e);
            throw new RuntimeException("Failed to clear password field", e);
        }
    }
    
    /**
     * Clear both username and password fields
     */
    public void clearCredentials() {
        clearUsername();
        clearPassword();
        logger.info("Credentials cleared");
    }
    
    /**
     * Get username field placeholder text
     * @return Placeholder text
     */
    public String getUsernamePlaceholder() {
        try {
            return ElementUtils.getElementAttribute(driver, USERNAME_FIELD, "placeholder");
        } catch (Exception e) {
            logger.error("Failed to get username placeholder", e);
            return "";
        }
    }
    
    /**
     * Get password field placeholder text
     * @return Placeholder text
     */
    public String getPasswordPlaceholder() {
        try {
            return ElementUtils.getElementAttribute(driver, PASSWORD_FIELD, "placeholder");
        } catch (Exception e) {
            logger.error("Failed to get password placeholder", e);
            return "";
        }
    }
    
    /**
     * Get login button text
     * @return Login button text
     */
    public String getLoginButtonText() {
        try {
            return ElementUtils.getElementAttribute(driver, LOGIN_BUTTON, "value");
        } catch (Exception e) {
            logger.error("Failed to get login button text", e);
            return "";
        }
    }
    
    /**
     * Check if username field is enabled
     * @return true if enabled, false otherwise
     */
    public boolean isUsernameFieldEnabled() {
        return ElementUtils.isElementEnabled(driver, USERNAME_FIELD);
    }
    
    /**
     * Check if password field is enabled
     * @return true if enabled, false otherwise
     */
    public boolean isPasswordFieldEnabled() {
        return ElementUtils.isElementEnabled(driver, PASSWORD_FIELD);
    }
    
    /**
     * Check if login button is enabled
     * @return true if enabled, false otherwise
     */
    public boolean isLoginButtonEnabled() {
        return ElementUtils.isElementEnabled(driver, LOGIN_BUTTON);
    }
    
    /**
     * Get login logo text
     * @return Logo text
     */
    public String getLoginLogoText() {
        try {
            return getElementText(LOGIN_LOGO);
        } catch (Exception e) {
            logger.error("Failed to get login logo text", e);
            return "";
        }
    }
    
    /**
     * Get accepted usernames text from login page
     * @return Accepted usernames text
     */
    public String getAcceptedUsernames() {
        try {
            if (isElementVisible(LOGIN_CREDENTIALS_TEXT)) {
                return getElementText(LOGIN_CREDENTIALS_TEXT);
            }
            return "";
        } catch (Exception e) {
            logger.error("Failed to get accepted usernames text", e);
            return "";
        }
    }
    
    /**
     * Get password information text from login page
     * @return Password information text
     */
    public String getPasswordInfo() {
        try {
            if (isElementVisible(LOGIN_PASSWORD_TEXT)) {
                return getElementText(LOGIN_PASSWORD_TEXT);
            }
            return "";
        } catch (Exception e) {
            logger.error("Failed to get password info text", e);
            return "";
        }
    }
    
    /**
     * Check if bot image is displayed
     * @return true if bot image is visible, false otherwise
     */
    public boolean isBotImageDisplayed() {
        return isElementVisible(LOGIN_BOT_IMAGE);
    }
    
    /**
     * Verify login page elements are present
     * @return true if all elements are present, false otherwise
     */
    public boolean verifyLoginPageElements() {
        boolean elementsPresent = isElementPresent(LOGIN_LOGO) &&
                                 isElementPresent(USERNAME_FIELD) &&
                                 isElementPresent(PASSWORD_FIELD) &&
                                 isElementPresent(LOGIN_BUTTON) &&
                                 isElementPresent(LOGIN_CREDENTIALS_TEXT) &&
                                 isElementPresent(LOGIN_PASSWORD_TEXT);
        
        logger.info("Login page elements verification: {}", elementsPresent);
        return elementsPresent;
    }
    
    /**
     * Get current entered username
     * @return Current username value
     */
    public String getCurrentUsername() {
        try {
            return ElementUtils.getElementAttribute(driver, USERNAME_FIELD, "value");
        } catch (Exception e) {
            logger.error("Failed to get current username", e);
            return "";
        }
    }
    
    /**
     * Get current entered password
     * @return Current password value
     */
    public String getCurrentPassword() {
        try {
            return ElementUtils.getElementAttribute(driver, PASSWORD_FIELD, "value");
        } catch (Exception e) {
            logger.error("Failed to get current password", e);
            return "";
        }
    }
    
    /**
     * Validate error message matches expected text
     * @param expectedError Expected error message
     * @return true if error matches, false otherwise
     */
    public boolean validateErrorMessage(String expectedError) {
        String actualError = getErrorMessage();
        boolean matches = actualError.equals(expectedError);
        logger.info("Error message validation - Expected: '{}', Actual: '{}', Matches: {}", 
                   expectedError, actualError, matches);
        return matches;
    }
}