package org.example.framework;

import java.net.MalformedURLException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverFactory {

    private static final String DRIVER_PATH = "./src/main/resources/";

    private DriverFactory()
    {
    }

    public static WebDriver getDriver(String name)
            throws MalformedURLException
    {
        if (!name.equals("chrome"))
            throw new RuntimeException(name + " is  not a valid driver name!");
        System.out.println(OS());
        if (isWindows())
        {
            System.setProperty("webdriver.chrome.driver",
                    DRIVER_PATH + "chromedriver.exe");

            return new ChromeDriver(options());
        }

        if (isUnix())
        {
            System.setProperty("webdriver.chrome.driver",
                    DRIVER_PATH + "chromedriver");

            return new ChromeDriver(headlessOptions());
        }

        throw new RuntimeException("unknown operating system!");
    }

    private static ChromeOptions options()
    {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1280,800");

        return options;
    }

    private static ChromeOptions headlessOptions()
    {
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--window-size=1280,800");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-setuid-sandbox");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        options.addArguments("--verbose");
        options.addArguments("--whitelisted-ips=");
        options.addArguments("--disable-extensions");
        options.addArguments("--remote-allow-origins=*");

        return options;
    }

    private static boolean isWindows()
    {
        return OS().indexOf("win") >= 0;
    }

    private static boolean isUnix()
    {

        String os = OS();

        return (
                os.indexOf("nix") >= 0 ||
                        os.indexOf("nux") >= 0 ||
                        os.indexOf("mac") >= 0 ||
                        os.indexOf("aix") > 0 );

    }

    private static String OS()
    {
        return System.getProperty("os.name").toLowerCase();
    }

}