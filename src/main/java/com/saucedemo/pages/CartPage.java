package com.saucedemo.pages;

import com.saucedemo.utils.ElementUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

/**
 * Cart Page class containing cart-related elements and methods
 */
public class CartPage extends BasePage {
    private static final Logger logger = LogManager.getLogger(CartPage.class);
    
    // Locators
    private static final By PAGE_TITLE = By.className("title");
    private static final By CART_LIST = By.className("cart_list");
    private static final By CART_ITEMS = By.className("cart_item");
    private static final By CART_ITEM_NAME = By.className("inventory_item_name");
    private static final By CART_ITEM_DESC = By.className("inventory_item_desc");
    private static final By CART_ITEM_PRICE = By.className("inventory_item_price");
    private static final By CART_QUANTITY = By.className("cart_quantity");
    private static final By REMOVE_BUTTONS = By.cssSelector("[data-test^='remove']");
    private static final By CONTINUE_SHOPPING_BUTTON = By.id("continue-shopping");
    private static final By CHECKOUT_BUTTON = By.id("checkout");
    private static final By CART_ITEM_LABEL = By.className("cart_item_label");
    private static final By CART_FOOTER = By.className("cart_footer");
    private static final By QTY_LABEL = By.className("cart_quantity_label");
    private static final By DESC_LABEL = By.className("cart_desc_label");
    
    // Specific cart item locators (dynamic)
    private static final String REMOVE_BUTTON_TEMPLATE = "[data-test='remove-%s']";
    private static final String CART_ITEM_LINK_TEMPLATE = "[data-test='item-%s-title-link']";
    
    // Page Factory elements
    @FindBy(className = "title")
    private WebElement pageTitle;
    
    @FindBy(className = "cart_list")
    private WebElement cartList;
    
    @FindBy(id = "continue-shopping")
    private WebElement continueShoppingButton;
    
    @FindBy(id = "checkout")
    private WebElement checkoutButton;
    
    @FindBy(className = "cart_quantity_label")
    private WebElement qtyLabel;
    
    @FindBy(className = "cart_desc_label")
    private WebElement descLabel;
    
    /**
     * Constructor
     * @param driver WebDriver instance
     */
    public CartPage(WebDriver driver) {
        super(driver);
    }
    
    @Override
    public void waitForPageToLoad() {
        waitForElementToBeVisible(PAGE_TITLE);
        waitForElementToBeVisible(CART_LIST);
        waitForElementToBeVisible(CONTINUE_SHOPPING_BUTTON);
        waitForElementToBeVisible(CHECKOUT_BUTTON);
        logger.info("Cart page loaded successfully");
    }
    
    @Override
    public boolean isPageLoaded() {
        try {
            return isElementVisible(PAGE_TITLE) &&
                   isElementVisible(CART_LIST) &&
                   isElementVisible(CONTINUE_SHOPPING_BUTTON) &&
                   isElementVisible(CHECKOUT_BUTTON) &&
                   getPageTitleText().equals("Your Cart");
        } catch (Exception e) {
            logger.error("Error checking if cart page is loaded", e);
            return false;
        }
    }
    
    @Override
    public String getPageUrl() {
        return config.getAppUrl() + "/cart.html";
    }
    
    /**
     * Get page title text
     * @return Page title text
     */
    public String getPageTitleText() {
        try {
            return getElementText(PAGE_TITLE);
        } catch (Exception e) {
            logger.error("Failed to get page title text", e);
            return "";
        }
    }
    
    /**
     * Get count of items in cart
     * @return Number of items in cart
     */
    public int getCartItemCount() {
        try {
            int count = ElementUtils.getElementCount(driver, CART_ITEMS);
            logger.info("Cart item count: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("Failed to get cart item count", e);
            return 0;
        }
    }
    
    /**
     * Check if cart is empty
     * @return true if cart is empty, false otherwise
     */
    public boolean isCartEmpty() {
        boolean empty = getCartItemCount() == 0;
        logger.debug("Cart empty: {}", empty);
        return empty;
    }
    
    /**
     * Get all item names in cart
     * @return List of item names
     */
    public List<String> getCartItemNames() {
        try {
            List<WebElement> nameElements = driver.findElements(CART_ITEM_NAME);
            List<String> itemNames = new ArrayList<>();
            
            for (WebElement element : nameElements) {
                itemNames.add(element.getText());
            }
            
            logger.info("Retrieved {} cart item names", itemNames.size());
            return itemNames;
        } catch (Exception e) {
            logger.error("Failed to get cart item names", e);
            throw new RuntimeException("Failed to get cart item names", e);
        }
    }
    
    /**
     * Get all item prices in cart
     * @return List of item prices
     */
    public List<String> getCartItemPrices() {
        try {
            List<WebElement> priceElements = driver.findElements(CART_ITEM_PRICE);
            List<String> itemPrices = new ArrayList<>();
            
            for (WebElement element : priceElements) {
                itemPrices.add(element.getText());
            }
            
            logger.info("Retrieved {} cart item prices", itemPrices.size());
            return itemPrices;
        } catch (Exception e) {
            logger.error("Failed to get cart item prices", e);
            throw new RuntimeException("Failed to get cart item prices", e);
        }
    }
    
    /**
     * Get all item descriptions in cart
     * @return List of item descriptions
     */
    public List<String> getCartItemDescriptions() {
        try {
            List<WebElement> descElements = driver.findElements(CART_ITEM_DESC);
            List<String> descriptions = new ArrayList<>();
            
            for (WebElement element : descElements) {
                descriptions.add(element.getText());
            }
            
            logger.info("Retrieved {} cart item descriptions", descriptions.size());
            return descriptions;
        } catch (Exception e) {
            logger.error("Failed to get cart item descriptions", e);
            throw new RuntimeException("Failed to get cart item descriptions", e);
        }
    }
    
    /**
     * Get all item quantities in cart
     * @return List of quantities
     */
    public List<String> getCartItemQuantities() {
        try {
            List<WebElement> qtyElements = driver.findElements(CART_QUANTITY);
            List<String> quantities = new ArrayList<>();
            
            for (WebElement element : qtyElements) {
                quantities.add(element.getText());
            }
            
            logger.info("Retrieved {} cart item quantities", quantities.size());
            return quantities;
        } catch (Exception e) {
            logger.error("Failed to get cart item quantities", e);
            throw new RuntimeException("Failed to get cart item quantities", e);
        }
    }
    
    /**
     * Remove item from cart by inventory name
     * @param inventoryItemName Inventory item name in kebab-case
     */
    public void removeItemFromCart(String inventoryItemName) {
        try {
            String removeButtonLocator = String.format(REMOVE_BUTTON_TEMPLATE, inventoryItemName);
            clickElement(By.cssSelector(removeButtonLocator));
            logger.info("Item removed from cart: {}", inventoryItemName);
        } catch (Exception e) {
            logger.error("Failed to remove item from cart: {}", inventoryItemName, e);
            throw new RuntimeException("Failed to remove item from cart: " + inventoryItemName, e);
        }
    }
    
    /**
     * Remove item from cart by item name
     * @param itemName Item name as displayed
     */
    public void removeItemFromCartByName(String itemName) {
        try {
            List<WebElement> cartItems = driver.findElements(CART_ITEMS);
            
            for (WebElement cartItem : cartItems) {
                WebElement nameElement = cartItem.findElement(By.className("inventory_item_name"));
                if (nameElement.getText().equals(itemName)) {
                    WebElement removeButton = cartItem.findElement(By.cssSelector("[data-test^='remove']"));
                    removeButton.click();
                    logger.info("Item removed from cart by name: {}", itemName);
                    return;
                }
            }
            
            throw new RuntimeException("Item not found in cart: " + itemName);
        } catch (Exception e) {
            logger.error("Failed to remove item from cart by name: {}", itemName, e);
            throw new RuntimeException("Failed to remove item from cart: " + itemName, e);
        }
    }
    
    /**
     * Click on continue shopping button
     */
    public void clickContinueShopping() {
        try {
            clickElement(CONTINUE_SHOPPING_BUTTON);
            logger.info("Continue shopping button clicked");
        } catch (Exception e) {
            logger.error("Failed to click continue shopping button", e);
            throw new RuntimeException("Failed to click continue shopping button", e);
        }
    }
    
    /**
     * Click on checkout button
     */
    public void clickCheckout() {
        try {
            clickElement(CHECKOUT_BUTTON);
            logger.info("Checkout button clicked");
        } catch (Exception e) {
            logger.error("Failed to click checkout button", e);
            throw new RuntimeException("Failed to click checkout button", e);
        }
    }
    
    /**
     * Check if item exists in cart by name
     * @param itemName Item name
     * @return true if item exists, false otherwise
     */
    public boolean isItemInCart(String itemName) {
        try {
            List<String> cartItems = getCartItemNames();
            boolean inCart = cartItems.contains(itemName);
            logger.debug("Item '{}' in cart: {}", itemName, inCart);
            return inCart;
        } catch (Exception e) {
            logger.debug("Item '{}' not found in cart", itemName);
            return false;
        }
    }
    
    /**
     * Check if multiple items exist in cart
     * @param itemNames List of item names
     * @return true if all items exist, false otherwise
     */
    public boolean areItemsInCart(List<String> itemNames) {
        List<String> cartItems = getCartItemNames();
        for (String itemName : itemNames) {
            if (!cartItems.contains(itemName)) {
                logger.debug("Item '{}' not found in cart", itemName);
                return false;
            }
        }
        logger.debug("All items found in cart: {}", itemNames);
        return true;
    }
    
    /**
     * Get item price by name
     * @param itemName Item name
     * @return Item price
     */
    public String getItemPriceByName(String itemName) {
        try {
            List<WebElement> cartItems = driver.findElements(CART_ITEMS);
            
            for (WebElement cartItem : cartItems) {
                WebElement nameElement = cartItem.findElement(By.className("inventory_item_name"));
                if (nameElement.getText().equals(itemName)) {
                    WebElement priceElement = cartItem.findElement(By.className("inventory_item_price"));
                    String price = priceElement.getText();
                    logger.debug("Price for '{}': {}", itemName, price);
                    return price;
                }
            }
            
            throw new RuntimeException("Item not found in cart: " + itemName);
        } catch (Exception e) {
            logger.error("Failed to get item price by name: {}", itemName, e);
            throw new RuntimeException("Failed to get item price: " + itemName, e);
        }
    }
    
    /**
     * Get item quantity by name
     * @param itemName Item name
     * @return Item quantity
     */
    public String getItemQuantityByName(String itemName) {
        try {
            List<WebElement> cartItems = driver.findElements(CART_ITEMS);
            
            for (WebElement cartItem : cartItems) {
                WebElement nameElement = cartItem.findElement(By.className("inventory_item_name"));
                if (nameElement.getText().equals(itemName)) {
                    WebElement qtyElement = cartItem.findElement(By.className("cart_quantity"));
                    String quantity = qtyElement.getText();
                    logger.debug("Quantity for '{}': {}", itemName, quantity);
                    return quantity;
                }
            }
            
            throw new RuntimeException("Item not found in cart: " + itemName);
        } catch (Exception e) {
            logger.error("Failed to get item quantity by name: {}", itemName, e);
            throw new RuntimeException("Failed to get item quantity: " + itemName, e);
        }
    }
    
    /**
     * Click on item name link
     * @param inventoryItemName Inventory item name in kebab-case
     */
    public void clickItemName(String inventoryItemName) {
        try {
            String itemLinkLocator = String.format(CART_ITEM_LINK_TEMPLATE, inventoryItemName);
            clickElement(By.cssSelector(itemLinkLocator));
            logger.info("Clicked on item name: {}", inventoryItemName);
        } catch (Exception e) {
            logger.error("Failed to click item name: {}", inventoryItemName, e);
            throw new RuntimeException("Failed to click item name: " + inventoryItemName, e);
        }
    }
    
    /**
     * Click on item name link by display name
     * @param itemName Item display name
     */
    public void clickItemNameByDisplayName(String itemName) {
        try {
            List<WebElement> cartItems = driver.findElements(CART_ITEMS);
            
            for (WebElement cartItem : cartItems) {
                WebElement nameElement = cartItem.findElement(By.className("inventory_item_name"));
                if (nameElement.getText().equals(itemName)) {
                    nameElement.click();
                    logger.info("Clicked on item name by display name: {}", itemName);
                    return;
                }
            }
            
            throw new RuntimeException("Item not found in cart: " + itemName);
        } catch (Exception e) {
            logger.error("Failed to click item name by display name: {}", itemName, e);
            throw new RuntimeException("Failed to click item name: " + itemName, e);
        }
    }
    
    /**
     * Remove all items from cart
     */
    public void removeAllItemsFromCart() {
        try {
            List<WebElement> removeButtons = driver.findElements(REMOVE_BUTTONS);
            int itemCount = removeButtons.size();
            
            // Remove items in reverse order to avoid stale element issues
            for (int i = itemCount - 1; i >= 0; i--) {
                // Re-fetch remove buttons each time to avoid stale elements
                List<WebElement> currentRemoveButtons = driver.findElements(REMOVE_BUTTONS);
                if (i < currentRemoveButtons.size()) {
                    currentRemoveButtons.get(i).click();
                    logger.debug("Removed item {}", i + 1);
                }
            }
            
            logger.info("Removed all {} items from cart", itemCount);
        } catch (Exception e) {
            logger.error("Failed to remove all items from cart", e);
            throw new RuntimeException("Failed to remove all items from cart", e);
        }
    }
    
    /**
     * Calculate total price of items in cart
     * @return Total price as double
     */
    public double calculateTotalPrice() {
        try {
            List<String> prices = getCartItemPrices();
            double total = 0.0;
            
            for (String priceStr : prices) {
                // Remove $ symbol and convert to double
                String numericPrice = priceStr.replace("$", "");
                total += Double.parseDouble(numericPrice);
            }
            
            logger.info("Calculated total price: ${}", total);
            return total;
        } catch (Exception e) {
            logger.error("Failed to calculate total price", e);
            throw new RuntimeException("Failed to calculate total price", e);
        }
    }
    
    /**
     * Check if continue shopping button is enabled
     * @return true if enabled, false otherwise
     */
    public boolean isContinueShoppingButtonEnabled() {
        return ElementUtils.isElementEnabled(driver, CONTINUE_SHOPPING_BUTTON);
    }
    
    /**
     * Check if checkout button is enabled
     * @return true if enabled, false otherwise
     */
    public boolean isCheckoutButtonEnabled() {
        return ElementUtils.isElementEnabled(driver, CHECKOUT_BUTTON);
    }
    
    /**
     * Get continue shopping button text
     * @return Button text
     */
    public String getContinueShoppingButtonText() {
        try {
            return getElementText(CONTINUE_SHOPPING_BUTTON);
        } catch (Exception e) {
            logger.error("Failed to get continue shopping button text", e);
            return "";
        }
    }
    
    /**
     * Get checkout button text
     * @return Button text
     */
    public String getCheckoutButtonText() {
        try {
            return getElementText(CHECKOUT_BUTTON);
        } catch (Exception e) {
            logger.error("Failed to get checkout button text", e);
            return "";
        }
    }
    
    /**
     * Verify cart page elements are present
     * @return true if all elements are present, false otherwise
     */
    public boolean verifyCartPageElements() {
        boolean elementsPresent = isElementPresent(PAGE_TITLE) &&
                                 isElementPresent(CART_LIST) &&
                                 isElementPresent(CONTINUE_SHOPPING_BUTTON) &&
                                 isElementPresent(CHECKOUT_BUTTON);
        
        logger.info("Cart page elements verification: {}", elementsPresent);
        return elementsPresent;
    }
    
    /**
     * Get quantity label text
     * @return Quantity label text
     */
    public String getQuantityLabelText() {
        try {
            return getElementText(QTY_LABEL);
        } catch (Exception e) {
            logger.error("Failed to get quantity label text", e);
            return "";
        }
    }
    
    /**
     * Get description label text
     * @return Description label text
     */
    public String getDescriptionLabelText() {
        try {
            return getElementText(DESC_LABEL);
        } catch (Exception e) {
            logger.error("Failed to get description label text", e);
            return "";
        }
    }
}