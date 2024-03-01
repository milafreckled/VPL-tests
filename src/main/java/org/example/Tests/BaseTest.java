package org.example.Tests;

import java.net.MalformedURLException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import  org.example.PageObjects.HomePage;
import org.example.framework.DriverFactory;
import org.example.framework.LogWriter;

// testNg https://www.jetbrains.com/help/idea/testng.html#add-testng-to-project
// selenium: https://www.jetbrains.com/help/idea/selenium.html#review_test_results
public class BaseTest {

    public WebDriver driver;

    private static final String URL = "http://www.vpl.ca";

    private LogWriter traceLogWriter = new LogWriter("./target/logs", "trace.log");

    @BeforeMethod
    public void setUp() throws MalformedURLException
    {
        String browserName = System.getenv("BROWSER");
        System.out.println(System.getenv("BROWSER"));
        this.driver        = DriverFactory.getDriver(browserName);
    }

    @AfterMethod
    public void tearDown()
    {
        driver.quit();
    }

    public HomePage openHomePage()
    {
        driver.navigate().to(URL);

        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(d -> d.getCurrentUrl().contains("vpl.ca"));

        traceLogWriter.writeToLog("Open Home Page");

        return new HomePage(driver);
    }

}