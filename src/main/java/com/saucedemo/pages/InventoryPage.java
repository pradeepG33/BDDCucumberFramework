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
 * Inventory Page class containing product-related elements and methods
 */
public class InventoryPage extends BasePage {
    private static final Logger logger = LogManager.getLogger(InventoryPage.class);
    
    // Locators
    private static final By PAGE_TITLE = By.className("title");
    private static final By INVENTORY_CONTAINER = By.id("inventory_container");
    private static final By INVENTORY_LIST = By.className("inventory_list");
    private static final By PRODUCT_SORT_CONTAINER = By.className("product_sort_container");
    private static final By INVENTORY_ITEMS = By.className("inventory_item");
    private static final By INVENTORY_ITEM_NAME = By.className("inventory_item_name");
    private static final By INVENTORY_ITEM_DESC = By.className("inventory_item_desc");
    private static final By INVENTORY_ITEM_PRICE = By.className("inventory_item_price");
    private static final By INVENTORY_ITEM_IMG = By.className("inventory_item_img");
    private static final By ADD_TO_CART_BUTTONS = By.cssSelector("[data-test^='add-to-cart']");
    private static final By REMOVE_BUTTONS = By.cssSelector("[data-test^='remove']");
    private static final By BACK_TO_PRODUCTS_BUTTON = By.id("back-to-products");
    
    // Specific product locators (dynamic)
    private static final String ADD_TO_CART_BUTTON_TEMPLATE = "[data-test='add-to-cart-%s']";
    private static final String REMOVE_BUTTON_TEMPLATE = "[data-test='remove-%s']";
    private static final String PRODUCT_LINK_TEMPLATE = "[data-test='item-%s-title-link']";
    private static final String PRODUCT_IMAGE_TEMPLATE = "[data-test='item-%s-img-link']";
    
    // Page Factory elements
    @FindBy(className = "title")
    private WebElement pageTitle;
    
    @FindBy(className = "product_sort_container")
    private WebElement sortDropdown;
    
    @FindBy(className = "inventory_list")
    private WebElement inventoryList;
    
    @FindBy(id = "back-to-products")
    private WebElement backToProductsButton;
    
    /**
     * Constructor
     * @param driver WebDriver instance
     */
    public InventoryPage(WebDriver driver) {
        super(driver);
    }
    
    @Override
    public void waitForPageToLoad() {
        waitForElementToBeVisible(PAGE_TITLE);
        waitForElementToBeVisible(INVENTORY_CONTAINER);
        waitForElementToBeVisible(PRODUCT_SORT_CONTAINER);
        logger.info("Inventory page loaded successfully");
    }
    
    @Override
    public boolean isPageLoaded() {
        try {
            return isElementVisible(PAGE_TITLE) &&
                   isElementVisible(INVENTORY_CONTAINER) &&
                   isElementVisible(PRODUCT_SORT_CONTAINER) &&
                   getPageTitle().equals("Swag Labs");
        } catch (Exception e) {
            logger.error("Error checking if inventory page is loaded", e);
            return false;
        }
    }
    
    @Override
    public String getPageUrl() {
        return config.getAppUrl() + "/inventory.html";
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
     * Select sorting option
     * @param sortOption Sort option (az, za, lohi, hilo)
     */
    public void selectSortOption(String sortOption) {
        try {
            ElementUtils.selectDropdownByValue(driver, PRODUCT_SORT_CONTAINER, sortOption);
            logger.info("Sort option selected: {}", sortOption);
        } catch (Exception e) {
            logger.error("Failed to select sort option: {}", sortOption, e);
            throw new RuntimeException("Failed to select sort option", e);
        }
    }
    
    /**
     * Get all product names
     * @return List of product names
     */
    public List<String> getAllProductNames() {
        try {
            List<WebElement> nameElements = driver.findElements(INVENTORY_ITEM_NAME);
            List<String> productNames = new ArrayList<>();
            
            for (WebElement element : nameElements) {
                productNames.add(element.getText());
            }
            
            logger.info("Retrieved {} product names", productNames.size());
            return productNames;
        } catch (Exception e) {
            logger.error("Failed to get product names", e);
            throw new RuntimeException("Failed to get product names", e);
        }
    }
    
    /**
     * Get all product prices
     * @return List of product prices as strings
     */
    public List<String> getAllProductPrices() {
        try {
            List<WebElement> priceElements = driver.findElements(INVENTORY_ITEM_PRICE);
            List<String> productPrices = new ArrayList<>();
            
            for (WebElement element : priceElements) {
                productPrices.add(element.getText());
            }
            
            logger.info("Retrieved {} product prices", productPrices.size());
            return productPrices;
        } catch (Exception e) {
            logger.error("Failed to get product prices", e);
            throw new RuntimeException("Failed to get product prices", e);
        }
    }
    
    /**
     * Get all product prices as double values
     * @return List of product prices as double
     */
    public List<Double> getAllProductPricesAsDouble() {
        List<String> priceStrings = getAllProductPrices();
        List<Double> prices = new ArrayList<>();
        
        for (String priceStr : priceStrings) {
            try {
                // Remove $ symbol and convert to double
                String numericPrice = priceStr.replace("$", "");
                prices.add(Double.parseDouble(numericPrice));
            } catch (NumberFormatException e) {
                logger.error("Failed to parse price: {}", priceStr);
            }
        }
        
        return prices;
    }
    
    /**
     * Get all product descriptions
     * @return List of product descriptions
     */
    public List<String> getAllProductDescriptions() {
        try {
            List<WebElement> descElements = driver.findElements(INVENTORY_ITEM_DESC);
            List<String> descriptions = new ArrayList<>();
            
            for (WebElement element : descElements) {
                descriptions.add(element.getText());
            }
            
            logger.info("Retrieved {} product descriptions", descriptions.size());
            return descriptions;
        } catch (Exception e) {
            logger.error("Failed to get product descriptions", e);
            throw new RuntimeException("Failed to get product descriptions", e);
        }
    }
    
    /**
     * Get count of products displayed
     * @return Number of products
     */
    public int getProductCount() {
        try {
            int count = ElementUtils.getElementCount(driver, INVENTORY_ITEMS);
            logger.info("Product count: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("Failed to get product count", e);
            return 0;
        }
    }
    
    /**
     * Add product to cart by name
     * @param productName Product name
     */
    public void addProductToCartByName(String productName) {
        try {
            // Find the product and click its add to cart button
            List<WebElement> items = driver.findElements(INVENTORY_ITEMS);
            
            for (WebElement item : items) {
                WebElement nameElement = item.findElement(By.className("inventory_item_name"));
                if (nameElement.getText().equals(productName)) {
                    WebElement addButton = item.findElement(By.cssSelector("[data-test^='add-to-cart']"));
                    addButton.click();
                    logger.info("Product added to cart: {}", productName);
                    return;
                }
            }
            
            throw new RuntimeException("Product not found: " + productName);
        } catch (Exception e) {
            logger.error("Failed to add product to cart: {}", productName, e);
            throw new RuntimeException("Failed to add product to cart: " + productName, e);
        }
    }
    
    /**
     * Add product to cart by inventory item name (kebab-case)
     * @param inventoryItemName Inventory item name in kebab-case
     */
    public void addProductToCartByInventoryName(String inventoryItemName) {
        try {
            String addButtonLocator = String.format(ADD_TO_CART_BUTTON_TEMPLATE, inventoryItemName);
            clickElement(By.cssSelector(addButtonLocator));
            logger.info("Product added to cart by inventory name: {}", inventoryItemName);
        } catch (Exception e) {
            logger.error("Failed to add product to cart by inventory name: {}", inventoryItemName, e);
            throw new RuntimeException("Failed to add product to cart: " + inventoryItemName, e);
        }
    }
    
    /**
     * Remove product from cart by inventory item name
     * @param inventoryItemName Inventory item name in kebab-case
     */
    public void removeProductFromCartByInventoryName(String inventoryItemName) {
        try {
            String removeButtonLocator = String.format(REMOVE_BUTTON_TEMPLATE, inventoryItemName);
            clickElement(By.cssSelector(removeButtonLocator));
            logger.info("Product removed from cart by inventory name: {}", inventoryItemName);
        } catch (Exception e) {
            logger.error("Failed to remove product from cart by inventory name: {}", inventoryItemName, e);
            throw new RuntimeException("Failed to remove product from cart: " + inventoryItemName, e);
        }
    }
    
    /**
     * Check if product is in cart by inventory item name
     * @param inventoryItemName Inventory item name in kebab-case
     * @return true if in cart (remove button visible), false otherwise
     */
    public boolean isProductInCart(String inventoryItemName) {
        try {
            String removeButtonLocator = String.format(REMOVE_BUTTON_TEMPLATE, inventoryItemName);
            boolean inCart = isElementVisible(By.cssSelector(removeButtonLocator));
            logger.debug("Product '{}' in cart: {}", inventoryItemName, inCart);
            return inCart;
        } catch (Exception e) {
            logger.debug("Product '{}' not in cart", inventoryItemName);
            return false;
        }
    }
    
    /**
     * Click on product name link
     * @param inventoryItemName Inventory item name in kebab-case
     */
    public void clickProductName(String inventoryItemName) {
        try {
            String productLinkLocator = String.format(PRODUCT_LINK_TEMPLATE, inventoryItemName);
            clickElement(By.cssSelector(productLinkLocator));
            logger.info("Clicked on product name: {}", inventoryItemName);
        } catch (Exception e) {
            logger.error("Failed to click product name: {}", inventoryItemName, e);
            throw new RuntimeException("Failed to click product name: " + inventoryItemName, e);
        }
    }
    
    /**
     * Click on product image
     * @param inventoryItemName Inventory item name in kebab-case
     */
    public void clickProductImage(String inventoryItemName) {
        try {
            String productImageLocator = String.format(PRODUCT_IMAGE_TEMPLATE, inventoryItemName);
            clickElement(By.cssSelector(productImageLocator));
            logger.info("Clicked on product image: {}", inventoryItemName);
        } catch (Exception e) {
            logger.error("Failed to click product image: {}", inventoryItemName, e);
            throw new RuntimeException("Failed to click product image: " + inventoryItemName, e);
        }
    }
    
    /**
     * Add multiple products to cart
     * @param productNames List of product names
     */
    public void addMultipleProductsToCart(List<String> productNames) {
        for (String productName : productNames) {
            addProductToCartByName(productName);
        }
        logger.info("Added {} products to cart", productNames.size());
    }
    
    /**
     * Add multiple products to cart by inventory names
     * @param inventoryItemNames List of inventory item names
     */
    public void addMultipleProductsToCartByInventoryNames(List<String> inventoryItemNames) {
        for (String inventoryName : inventoryItemNames) {
            addProductToCartByInventoryName(inventoryName);
        }
        logger.info("Added {} products to cart by inventory names", inventoryItemNames.size());
    }
    
    /**
     * Get current sort option
     * @return Current sort option value
     */
    public String getCurrentSortOption() {
        try {
            return ElementUtils.getElementAttribute(driver, PRODUCT_SORT_CONTAINER, "value");
        } catch (Exception e) {
            logger.error("Failed to get current sort option", e);
            return "";
        }
    }
    
    /**
     * Verify products are sorted by name ascending
     * @return true if sorted correctly, false otherwise
     */
    public boolean verifyProductsSortedByNameAscending() {
        List<String> productNames = getAllProductNames();
        for (int i = 0; i < productNames.size() - 1; i++) {
            if (productNames.get(i).compareToIgnoreCase(productNames.get(i + 1)) > 0) {
                logger.error("Products not sorted by name ascending at position {}: '{}' > '{}'", 
                           i, productNames.get(i), productNames.get(i + 1));
                return false;
            }
        }
        logger.info("Products verified as sorted by name ascending");
        return true;
    }
    
    /**
     * Verify products are sorted by name descending
     * @return true if sorted correctly, false otherwise
     */
    public boolean verifyProductsSortedByNameDescending() {
        List<String> productNames = getAllProductNames();
        for (int i = 0; i < productNames.size() - 1; i++) {
            if (productNames.get(i).compareToIgnoreCase(productNames.get(i + 1)) < 0) {
                logger.error("Products not sorted by name descending at position {}: '{}' < '{}'", 
                           i, productNames.get(i), productNames.get(i + 1));
                return false;
            }
        }
        logger.info("Products verified as sorted by name descending");
        return true;
    }
    
    /**
     * Verify products are sorted by price ascending
     * @return true if sorted correctly, false otherwise
     */
    public boolean verifyProductsSortedByPriceAscending() {
        List<Double> prices = getAllProductPricesAsDouble();
        for (int i = 0; i < prices.size() - 1; i++) {
            if (prices.get(i) > prices.get(i + 1)) {
                logger.error("Products not sorted by price ascending at position {}: {} > {}", 
                           i, prices.get(i), prices.get(i + 1));
                return false;
            }
        }
        logger.info("Products verified as sorted by price ascending");
        return true;
    }
    
    /**
     * Verify products are sorted by price descending
     * @return true if sorted correctly, false otherwise
     */
    public boolean verifyProductsSortedByPriceDescending() {
        List<Double> prices = getAllProductPricesAsDouble();
        for (int i = 0; i < prices.size() - 1; i++) {
            if (prices.get(i) < prices.get(i + 1)) {
                logger.error("Products not sorted by price descending at position {}: {} < {}", 
                           i, prices.get(i), prices.get(i + 1));
                return false;
            }
        }
        logger.info("Products verified as sorted by price descending");
        return true;
    }
    
    /**
     * Check if back to products button is visible
     * @return true if visible, false otherwise
     */
    public boolean isBackToProductsButtonVisible() {
        return isElementVisible(BACK_TO_PRODUCTS_BUTTON);
    }
    
    /**
     * Click back to products button
     */
    public void clickBackToProducts() {
        try {
            clickElement(BACK_TO_PRODUCTS_BUTTON);
            logger.info("Clicked back to products button");
        } catch (Exception e) {
            logger.error("Failed to click back to products button", e);
            throw new RuntimeException("Failed to click back to products button", e);
        }
    }
    
    /**
     * Get product info by name
     * @param productName Product name
     * @return Map containing product information
     */
    public String getProductPriceByName(String productName) {
        try {
            List<WebElement> items = driver.findElements(INVENTORY_ITEMS);
            
            for (WebElement item : items) {
                WebElement nameElement = item.findElement(By.className("inventory_item_name"));
                if (nameElement.getText().equals(productName)) {
                    WebElement priceElement = item.findElement(By.className("inventory_item_price"));
                    String price = priceElement.getText();
                    logger.debug("Price for '{}': {}", productName, price);
                    return price;
                }
            }
            
            throw new RuntimeException("Product not found: " + productName);
        } catch (Exception e) {
            logger.error("Failed to get product price for: {}", productName, e);
            throw new RuntimeException("Failed to get product price: " + productName, e);
        }
    }
    
    /**
     * Check if all add to cart buttons are visible
     * @return true if all are visible, false otherwise
     */
    public boolean areAllAddToCartButtonsVisible() {
        try {
            List<WebElement> addButtons = driver.findElements(ADD_TO_CART_BUTTONS);
            int productCount = getProductCount();
            boolean allVisible = addButtons.size() == productCount;
            logger.debug("Add to cart buttons visible: {} out of {}", addButtons.size(), productCount);
            return allVisible;
        } catch (Exception e) {
            logger.error("Failed to check add to cart buttons visibility", e);
            return false;
        }
    }
}