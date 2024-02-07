package org.example.framework;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;

public class ScreenshotWriter {

    private String folderPath = "./target/screenshots/";

    public ScreenshotWriter()
    {
    }

    public void writeToFile(byte[] screenshotInfo, String testName)
    {
        File screenshotsFolder = new File(folderPath);

        if (!screenshotsFolder.exists())
            screenshotsFolder.mkdir();

        try {
            FileOutputStream file = new FileOutputStream(
                    screenshotName(testName));

            file.write(screenshotInfo);
            file.close();
        }
        catch (Exception ex) {
            throw new IllegalStateException("cannot create screenshot!", ex);
        }
    }

    private String screenshotName(String testName)
    {
        String now = LocalDateTime.now().toString();

        now = now.replace(":", "_")
                .replace(";", "_")
                .replace(".", "_");

        return folderPath + testName + now + ".png";
    }

}