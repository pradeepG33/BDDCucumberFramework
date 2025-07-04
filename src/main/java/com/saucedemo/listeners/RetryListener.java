package com.saucedemo.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Retry Listener to apply RetryAnalyzer to all test methods automatically
 */
public class RetryListener implements IAnnotationTransformer {
    private static final Logger logger = LogManager.getLogger(RetryListener.class);
    
    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        // Apply RetryAnalyzer to all test methods that don't already have one
        if (annotation.getRetryAnalyzer() == null) {
            annotation.setRetryAnalyzer(RetryAnalyzer.class);
            
            if (testMethod != null) {
                logger.debug("RetryAnalyzer applied to test method: {}", testMethod.getName());
            }
        }
    }
}