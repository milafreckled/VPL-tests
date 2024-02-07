package org.example.Tests;


import java.lang.reflect.Field;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.example.framework.LogWriter;
import  org.example.framework.ScreenshotWriter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class Listener implements ITestListener {

    private LogWriter traceWriter;
    private LogWriter exceptionsWriter;

    public Listener()
    {
        this.exceptionsWriter = new LogWriter("./target/logs", "exceptions.log");
        this.traceWriter      = new LogWriter("./target/logs", "trace.log");
    }

    @Override
    public void onTestStart(ITestResult testResult)
    {
        this.traceWriter.writeToLog("\n Test Name: " + testResult.getName());
    }


    @Override
    public void onTestSuccess(ITestResult testResult)
    {
        byte[] screenshotInfo = getScreenshot(testResult);

        saveScreenshot(screenshotInfo, "passed_" + testResult.getName());
    }


    @Override
    public void onTestFailure(ITestResult testResult)
    {
        byte[] screenshotInfo = getScreenshot(testResult);

        saveScreenshot(screenshotInfo, "failed_" + testResult.getName());

        saveExceptionInfo(testResult);
    }


    private byte[] getScreenshot(ITestResult testResult)
    {
        TakesScreenshot driver = (TakesScreenshot)driver(testResult);

        byte[] screenshotInfo = driver.getScreenshotAs(OutputType.BYTES);
        return screenshotInfo;
    }


    private void saveScreenshot(byte[] screenshotInfo,
                                String testName)
    {
        ScreenshotWriter writer = new ScreenshotWriter();

        writer.writeToFile(screenshotInfo, testName);
    }


    private void saveExceptionInfo(ITestResult testResult)
    {
        String testName = testResult.getName();

        String exceptionName = testResult.getThrowable().getMessage();

        String stackTrace = "";

        StackTraceElement[] stackElements =
                testResult.getThrowable().getStackTrace();

        for (int i = 0; i < stackElements.length - 1; i++)
            stackTrace = stackTrace + stackElements[i].toString() + "\n";

        this.exceptionsWriter.writeToLog(
                String.format(
                        "test = %s\n exception = %s\n stack trace = %s\n",
                        testName,
                        exceptionName,
                        stackTrace));
    }


    @SuppressWarnings("unchecked")
    private WebDriver driver(ITestResult result)
    {

        try {
            Class<?extends ITestResult> testClass =
                    (Class<? extends ITestResult>)result.getInstance().getClass();

            Class<?extends ITestResult> baseClass =
                    (Class<? extends ITestResult>)testClass.getSuperclass();

            Field field = baseClass.getDeclaredField("driver");

            WebDriver driver = (WebDriver)field.get(result.getInstance());

            return driver;
        }
        catch (
                SecurityException | NoSuchFieldException | IllegalAccessException | IllegalArgumentException e)
        {
            throw new IllegalStateException(
                    "could not get the driver!", e);
        }

    }

    @Override
    public void onTestSkipped(ITestResult result)
    {
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult r)
    {
    }

    @Override
    public void onStart(ITestContext context)
    {
    }

    @Override
    public void onFinish(ITestContext c)
    {
    }

}