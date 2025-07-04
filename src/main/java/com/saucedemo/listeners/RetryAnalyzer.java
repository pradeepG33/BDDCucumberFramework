package com.saucedemo.listeners;

import com.saucedemo.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retry Analyzer for implementing retry logic for failed tests
 */
public class RetryAnalyzer implements IRetryAnalyzer {
    private static final Logger logger = LogManager.getLogger(RetryAnalyzer.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    
    private int retryCount = 0;
    private final int maxRetryCount = config.getRetryCount();
    
    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            String testName = result.getMethod().getMethodName();
            
            logger.warn("Test '{}' failed. Retrying attempt {} of {}", 
                       testName, retryCount, maxRetryCount);
            
            // Log retry information to ExtentReports
            ExtentReportListener.logWarning(
                String.format("Test failed. Retrying attempt %d of %d", retryCount, maxRetryCount)
            );
            
            return true;
        }
        
        String testName = result.getMethod().getMethodName();
        logger.error("Test '{}' failed after {} retry attempts", testName, maxRetryCount);
        
        // Log final failure to ExtentReports
        ExtentReportListener.logFail(
            String.format("Test failed permanently after %d retry attempts", maxRetryCount)
        );
        
        return false;
    }
    
    /**
     * Get current retry count
     * @return Current retry count
     */
    public int getRetryCount() {
        return retryCount;
    }
    
    /**
     * Get maximum retry count
     * @return Maximum retry count
     */
    public int getMaxRetryCount() {
        return maxRetryCount;
    }
    
    /**
     * Reset retry count
     */
    public void resetRetryCount() {
        retryCount = 0;
    }
}