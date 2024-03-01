package org.example.PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.example.framework.LogWriter;

import java.time.Duration;

public class ResultsPage
{

    private WebDriver driver;
    private WebDriverWait wait;

    private final LogWriter traceLogWriter;

    private static final By PAGINATION_TEXT_XPATH =
            By.xpath("//span[@data-key = 'pagination-text']");

    private static final By NEXT_PAGE_XPATH = By.className("pagination__next-chevron");

    private static final String PAGE_XPATH = "//a[@data-page = '%d']";

    public ResultsPage(WebDriver driver)
    {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        this.traceLogWriter = new LogWriter("./target/logs",
                "trace.log");
    }

    public boolean isDisplayed()
    {
        boolean result = wait.until(d -> d.getCurrentUrl()
                .contains("bibliocommons.com"));

        traceLogWriter.writeToLog("ResultsPage - isDisplayed()");

        return result;
    }


    public int totalCount()
    {
        wait.until(
                d -> d.findElement(PAGINATION_TEXT_XPATH).isDisplayed());

        WebElement countElement = driver.findElement(PAGINATION_TEXT_XPATH);
        String countText = countElement.getText();

        int i = countText.indexOf("of") + 3;
        int j = countText.indexOf("results") - 1;
        countText = countText.substring(i,  j);

        int count = Integer.parseInt(countText);

        traceLogWriter.writeToLog("ResultsPage - get TotalCount()");

        return count;
    }

    public String resultsPerPage()
    {
        wait.until(
                d -> d.findElement(PAGINATION_TEXT_XPATH).isDisplayed());

        WebElement countElement = driver.findElement(PAGINATION_TEXT_XPATH);
        String countText = countElement.getText();

        int i = countText.indexOf("of") - 1;
        countText = countText.substring(0, i);

        traceLogWriter.writeToLog("ResultsPage() - get ResultsPerPage()");

        return countText;
    }

    public ResultsPage goToPage(int pageNumber)
    {
        if (pageNumber <= 0)
            throw new InvalidArgumentException(
                    "pageNumber should be > 0");

        By pageLocator = By.xpath(
                String.format(PAGE_XPATH, pageNumber));

        wait.until(d -> d.findElement(pageLocator).isDisplayed());

        WebElement page = driver.findElement(pageLocator);
        page.click();

        traceLogWriter.writeToLog("ResultsPage - goToPage()");

        String expectedResults = resultsPerPage(pageNumber);
        waitUntilResultsPerPageIs(expectedResults);

        return new ResultsPage(driver);
    }


    public ResultsPage goToNextPage()
    {
        wait.until(
                d -> d.findElement(NEXT_PAGE_XPATH).isDisplayed());

        WebElement page = driver.findElement(NEXT_PAGE_XPATH);
        page.click();

        traceLogWriter.writeToLog("ResultsPage - goToNextPage()");

        String expectedResults = resultsPerPage(pageNumber());
        waitUntilResultsPerPageIs(expectedResults);

        return new ResultsPage(driver);
    }

    private void waitUntilResultsPerPageIs(String resultsPerPage)
    {
        wait.until(d -> d.findElement(PAGINATION_TEXT_XPATH)
                .getText().contains(resultsPerPage));
    }


    private String resultsPerPage(int pageNumber)
    {
        int from = (pageNumber - 1) * 10 + 1;
        int to = pageNumber * 10;

        return String.format("%d to %d", from, to);
    }

    public int pageNumber()
    {
        String url = driver.getCurrentUrl();

        if (!url.contains("page"))
            return 1;

        int i = url.indexOf("page=") + 5;
        int j = url.length();

        String pageValue = url.substring(i,  j);

        return Integer.parseInt(pageValue);
    }

}
