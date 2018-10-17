package com.amazon.utiliy;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.amozon.data.*;
import com.relevantcodes.extentreports.LogStatus;

public class GetScreenShot {

	/*
	 * ExtentTestManager.getTest().log(LogStatus.FAIL, "Snapshot below: " +
	 * ExtentTestManager.getTest().
	 * addBase64ScreenShot(GetScreenShot.capture(driver)));
	 * 
	 * ExtentTestManager.getTest().log(LogStatus.FAIL, "Snapshot below: " +
	 * ExtentTestManager.getTest().addScreenCapture(screenShotPath));
	 */

	public static String capture(WebDriver driver, String screenShotName) throws IOException {

		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		screenShotName = StringUtils.isEmpty(screenShotName) ? "dosyabulunamadi" : screenShotName;
		String dest = GetData.RESULT_FILE_PATH + "Test Error ScreenShots" + File.separator + screenShotName + ".png";
		File destination = new File(dest);
		FileUtils.copyFile(source, destination);

		return dest;
	}

	public static String capture(WebDriver driver) throws IOException {

		TakesScreenshot ts = (TakesScreenshot) driver;

		return "data:image/png;base64, " + ts.getScreenshotAs(OutputType.BASE64);
	}
}