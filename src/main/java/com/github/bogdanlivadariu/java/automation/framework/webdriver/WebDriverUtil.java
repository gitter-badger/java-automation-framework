package com.github.bogdanlivadariu.java.automation.framework.webdriver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebDriverUtil {

    private static Logger logger = LogManager.getLogger(WebDriverUtil.class);

    /**
     * Execute js / jquery.
     * @param script
     * @return
     */
    public static Object executeJavaScript(String script) {
        JavascriptExecutor jsExecute = (JavascriptExecutor) WebDriverInstance.getDriver();
        return jsExecute.executeScript(script);
    }

    /**
     * Execute js / jquery.
     * @param script
     * @param element
     * @return
     */
    public static Object executeJavaScript(String script, WebElement element) {
        JavascriptExecutor jsExecute = (JavascriptExecutor) WebDriverInstance.getDriver();
        return jsExecute.executeScript(script, element);
    }

    /**
     * Wait for the page to be loaded, no js action in background.
     */
    public static void waitForPageLoaded() {
        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return executeJavaScript("return document.readyState").equals("complete");
            }
        };

        Wait<WebDriver> wait = new WebDriverWait(WebDriverInstance.getDriver(), 30);
        wait.until(expectation);
    }

    /**
     * Open a new browser window, and visit the URL provided parameter.
     * @param url
     */
    public static void openURL(String url) {
        logger.info("Opening URL: " + url);
        WebDriver webDriverInstance = WebDriverInstance.getDriver();

        String currentHandle = webDriverInstance.getWindowHandle();

        executeJavaScript("window.open();");
        // extract the handle of the new opened browser instance
        String newWindowHandle = null;
        for (String hwd : webDriverInstance.getWindowHandles()) {
            if (!hwd.equals(currentHandle)) {
                newWindowHandle = hwd;
                break;
            }
        }
        // switch to the new window
        webDriverInstance.switchTo().window(newWindowHandle);
        webDriverInstance.get(url);
        webDriverInstance.close();

        // switch back to the default window
        webDriverInstance.switchTo().window(currentHandle);
    }

    public static void switchToFrame(WebElement frameWebElement) {
        WebDriverInstance.getDriver().switchTo().frame(frameWebElement);
    }

    public static void switchToDefaultContent() {
        WebDriverInstance.getDriver().switchTo().defaultContent();
    }
}
