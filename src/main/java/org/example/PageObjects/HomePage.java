package org.example.PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.example.framework.LogWriter;

public class HomePage {

    private static final By SEARCH_BOX_ID    = By.id("edit-keyword");
    private static final By SEARCH_BUTTON_ID = By.id("edit-submit");

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final LogWriter traceLogWriter;

    public HomePage(WebDriver driver)
    {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 30);

        this.traceLogWriter = new LogWriter("./target/logs", "trace.log");
    }

    public ResultsPage searchFor(String keyword)
    {
        typeKeyword(keyword);
        executeSearch();

        traceLogWriter.writeToLog("HomePage - searchFor()");

        return goToResultsPage();
    }


    private ResultsPage goToResultsPage()
    {
        ResultsPage resultsPage = new ResultsPage(driver);

        traceLogWriter.writeToLog("HomePage - goToResultsPage()");

        if (!resultsPage.isDisplayed())
            throw new ElementNotVisibleException(
                    "Results Page is not displayed!");

        return resultsPage;
    }

    private void typeKeyword(String keyword)
    {
        wait.until(d -> d.findElement(SEARCH_BOX_ID).isEnabled());

        WebElement searchBox = driver.findElement(SEARCH_BOX_ID);
        searchBox.sendKeys(keyword);
    }

    private void executeSearch()
    {
        wait.until(d -> d.findElement(SEARCH_BUTTON_ID).isEnabled());

        WebElement searchButton = driver.findElement(SEARCH_BUTTON_ID);
        searchButton.click();
    }

}