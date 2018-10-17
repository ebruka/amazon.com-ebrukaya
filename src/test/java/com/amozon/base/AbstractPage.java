package com.amozon.base;

import java.beans.Statement;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
//import org.openqa.selenium.security.UserAndPassword;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.retry.annotation.Retryable;

import com.amazon.utiliy.ExtentTestManager;
import com.amazon.utiliy.GetScreenShot;
import com.amazon.utiliy.log;
import com.amozon.base.AbstractTest;
import com.amozon.base.AbstractTest.AutomationVariables;
import com.amozon.data.GetData;
import com.amozon.data.GetData.*;
import com.amozon.util.Configuration;
import com.amozon.util.DataFinder;
import com.relevantcodes.extentreports.LogStatus;

import jxl.read.biff.BiffException;

public class AbstractPage {

	protected RemoteWebDriver driver;
	protected Configuration config;
	protected WebDriverWait wait, waitZero, waitLoader;
	protected static final Logger logger = LogManager.getLogger(AbstractTest.class.getName());

	public AbstractPage(RemoteWebDriver driver) {

		this.driver = driver;
		this.config = Configuration.getInstance();
		this.wait = new WebDriverWait(driver, GetData.DEFAULT_WAIT);
		this.waitZero = new WebDriverWait(driver, 0);
		this.waitLoader = new WebDriverWait(driver, GetData.DEFAULT_WAIT_LOADERBOX);// 90
		DOMConfigurator.configure("src/test/resources/Log4j.xml");
	}

	/**
	 * navigate to url
	 */
	public void navigateTo(Url url) {
		try {
			driver.get(DataFinder.getUrl(url));
			driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
			Wait(500);
			log.info("Web application launched");
			LogPASS("Web application launched");
		} catch (Exception e) {
			log.error("Error while getting app url : " + e);
			LogFAIL("Error while getting app url : " + e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Use this method to find element by cssSelector
	 * 
	 * @param by
	 * @param index
	 * @return A WebElement, or an empty if nothing matches
	 * @throws InterruptedException
	 */
	@Retryable(maxAttempts = 1)
	protected WebElement findElement(By by, int... index) throws InterruptedException {
		// driver.manage().timeouts().implicitlyWait(GetData.DEFAULT_WAIT,
		// TimeUnit.SECONDS);

		WebElement element = null;
		untilElementAppear(by);
		try {
			if (index.length == 0)
				element = driver.findElement(by);
			else
				element = driver.findElements(by).get(index[0]);

			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);arguments[0].focus();",
					element);
			// ((JavascriptExecutor)
			// driver).executeScript("arguments[0].focus();", element);
			// wait.until(ExpectedConditions.visibilityOf(element));
			wait.until(ExpectedConditions.elementToBeClickable(element));
		} catch (Exception e) {
			log.error("Error while clicking webelement : " + e);
			LogFAIL("Error while clicking webelement : " + e);

			throw new RuntimeException(e);
		}
		return element;
	}

	public void waitForElement(By by, int... index) throws InterruptedException {
		waitLoaderBox();
		findElement(by, index);
	}

	public void waitLoaderBox() {
		waitLoaderBox(GetData.DEFAULT_WAIT_LOADERBOX);// 90
	}

	public void waitLoaderBox(int time) {

		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		if (driver.findElements(By.xpath("//div[starts-with(@class,'loader')]")).size() != 0) {
			driver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
			driver.findElement(By.xpath("//div[@class='loader' and @style='display: none;']"));
			driver.findElement(By.xpath("//div[@class='loader-box' and @style='display: none;']"));
		}
		driver.manage().timeouts().implicitlyWait(GetData.DEFAULT_WAIT, TimeUnit.SECONDS);
	}

	/**
	 * Use this method to find elements by cssSelector
	 * 
	 * @param by
	 * @return A list of WebElements, or an empty if nothing matches
	 */
	protected List<WebElement> findElements(By by) {
		List<WebElement> webElements = null;
		untilElementAppear(by);
		try {
			webElements = driver.findElements(by);
		} catch (Exception e) {
			log.error("Error while listing webelements by css selector : " + e);
			LogFAIL("Error while listing webelements by css selector : " + e);
			log.info("Error while listing webelements by css selector : " + e);

			throw new RuntimeException(e);
		}
		return webElements;
	}

	/**
	 * Use this method click to element
	 * 
	 * @param by
	 * @param index
	 * @throws InterruptedException
	 */
	protected void click(By by, int... index) throws InterruptedException {

		WebElement element;
		try {
			element = findElement(by, index);
			String elemText = element.getText();
			element.click();
			LogPASS("Click Button : " + elemText);

		} catch (Exception e) {
			log.error("Error while clicking webelement : " + e);
			LogFAIL("Error while clicking webelement : " + e);
			log.info("Error while clicking webelement : " + e);

			throw new RuntimeException(e);
		}
	}

	/**
	 * Use this method click to element
	 * 
	 * @param by
	 * @param index
	 * @throws InterruptedException
	 */
	protected void click(By by, boolean clickable) throws InterruptedException {
		try {
			if (!clickable)
				click(by);
			else {
				wait.until(ExpectedConditions.visibilityOfElementLocated(by));
				WebElement elem = wait.until(ExpectedConditions.visibilityOf(driver.findElement(by)));
				String elemText = elem.getText();
				elem.click();
				LogPASS("Click Button : " + elemText);
			}
		} catch (Exception e) {
			log.error("Error while clicking webelement : " + e);
			LogFAIL("Error while clicking webelement : " + e);
			log.info("Error while clicking webelement : " + e);

			throw new RuntimeException(e);
		}
	}

	/**
	 * Use this method to simulate typing into an element if it is enable. Send
	 * enter if pressEnter is true, do nothing otherwise. Note : Before sending
	 * operation, element is cleared.
	 * 
	 * @param by
	 * @param text
	 * @param pressEnter
	 * @throws InterruptedException
	 */
	protected void sendKeys(By by, String text, boolean pressEnter, int... index) throws InterruptedException {

		WebElement element = null;
		String elemText = null;
		try {
			element = findElement(by, index);
			if (element.isEnabled()) {
				elemText = element.getText();
				element.clear();
				element.sendKeys(text);
				if (pressEnter) {
					waitLoaderBox();
					element.sendKeys(Keys.ENTER);
				}
			}
			LogPASS("Value : " + text + " - SendKeys : " + elemText);
		} catch (Exception e) {
			log.error("Error while filling field : " + e);
			LogFAIL("Error while filling field : " + e);

			throw new RuntimeException(e);
		}
	}

	/**
	 * Use this method to simulate typing into an element if it is enable. Send
	 * enter if pressEnter is true, do nothing otherwise. Note : Before sending
	 * operation, element is cleared.
	 * 
	 * @param by
	 * @param text
	 * @param pressEnter
	 * @throws InterruptedException
	 */
	protected void sendKeys(By by, Keys key, int... index) throws InterruptedException {
		WebElement element = null;
		String elemText = null;
		try {
			element = findElement(by, index);
			if (element.isEnabled()) {
				elemText = element.getText();
				element.sendKeys(key);
			}
			LogPASS("Value : " + key.toString() + " - SendKeys : " + elemText);
		} catch (Exception e) {
			log.error("Error while filling field : " + e);
			LogFAIL("Error while filling field : " + e);

			throw new RuntimeException(e);
		}
	}

	/**
	 * Use this method to simulate typing into an element if it is enable. Note
	 * : Before sending operation, element is cleared.
	 * 
	 * @param by
	 * @param text
	 * @throws InterruptedException
	 */
	protected void sendKeys(By by, String text, int... index) throws InterruptedException {
		sendKeys(by, text, false, index);
	}

	protected void selectCombobox(By by, String value) throws InterruptedException {
		WebElement element = findElement(by);
		String elemText = null;
		try {
			if (element.isEnabled()) {
				elemText = element.getText();
				Select selectBox = new Select(driver.findElement(by));
				selectBox.selectByValue(value);
			}
			LogPASS("Value : " + value + " - SelectComboBox : " + elemText);
		} catch (Exception e) {
			log.error("Error while filling field : " + e);
			LogFAIL("Error while filling field : " + e);

			throw new RuntimeException(e);
		}
	}

	protected void moveToElement(By by) {
		try {
			Actions action = new Actions(driver);
			WebElement we = driver.findElement(by);
			action.moveToElement(we).build().perform();
		} catch (Exception e) {
			log.error("Error while filling field : " + e);
			LogFAIL("Error while filling field : " + e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Get the visible (i.e. not hidden by CSS) innerText of this element.
	 * 
	 * @param by
	 * @param index
	 * @return The innerText of this element.
	 * @throws InterruptedException
	 */
	protected String getTextOfElement(By by, int... index) throws InterruptedException {

		String text = null;
		untilElementAppear(by);

		try {
			if (index.length == 0)
				text = driver.findElement(by).getText();
			else
				text = driver.findElements(by).get(index[0]).getText();

			// TODO as400 için eklenecek text = text.replace(" ", "");
		} catch (Exception e) {
			log.error("Error while getting text of element : " + e);
			LogFAIL("Error while getting text of element : " + e);

			throw new RuntimeException(e);
		}
		return text;
	}

	@SuppressWarnings("finally")
	protected String getTextOfElement(WebElement elem) {
		String text = null;
		try {
			text = elem.getText();
		} finally {
			return text;
		}
	}

	protected String getValueOfElement(By by, int... index) {
		String value = null;

		try {
			if (index.length == 0) {
				value = driver.findElement(by).getAttribute("value");
			} else {
				value = driver.findElements(by).get(index[0]).getAttribute("value");
			}
		} catch (Exception e) {
			log.error("Error while getting text of element : " + e);
			LogFAIL("Error while getting text of element : " + e);

			throw new RuntimeException(e);
		}
		return value;
	}

	/**
	 * Wait until element appears
	 * 
	 * @param by
	 * @param index
	 */
	protected void untilElementAppear(By by) {
		try {
			// waitLoaderBox(90);// , 40
			// Thread.sleep(1000);
			// driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
			// wait.until(ExpectedConditions.presenceOfElementLocated(by));
		} catch (Exception e) {
			log.error("Error while waiting until element appears : " + e);
			LogFAIL("Error while waiting until element appears : " + e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Wait until element disappears
	 * 
	 * @param by
	 */
	protected void untilElementDisappear(By by) {
		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
		} catch (Exception e) {
			log.error("Error while waiting until element disappears : " + e);
			LogFAIL("Error while waiting until element disappears : " + e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Return true if element exist, false otherwise.
	 * 
	 * @param by
	 * @param index
	 * @return True if element exists, false otherwise.
	 */
	protected boolean isElementExist(By by) {
		return isElementExist(by, 15);
	}

	protected boolean isElementExist(By by, int timeSeconds) {

		driver.manage().timeouts().implicitlyWait(timeSeconds, TimeUnit.SECONDS);
		boolean isExist = driver.findElements(by).size() > 0;
		driver.manage().timeouts().implicitlyWait(GetData.DEFAULT_WAIT, TimeUnit.SECONDS);

		return isExist;
	}

	public String getProperty(By by, String expectedPropertyName, int... index) {
		WebElement elem;

		if (index.length == 0)
			elem = driver.findElement(by);
		else
			elem = driver.findElements(by).get(index[0]);

		return elem.getAttribute(expectedPropertyName);
	}

	public String randomNumber(int length) {
		Random r = new Random();
		List<Integer> digits = new ArrayList<Integer>();
		String number = "";

		for (int i = 0; i < length - 1; i++) {
			digits.add(i);
		}

		for (int i = length - 1; i > 0; i--) {
			int randomDigit = r.nextInt(i);
			number += digits.get(randomDigit);
			digits.remove(randomDigit);
		}
		number = "1" + number;

		return number;
	}

	public String getSysDate() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		LocalDateTime now = LocalDateTime.now();
		// log.info(dtf.format(now)); // 2016/11/16 12:08:43
		return dtf.format(now);
	}

	public void ClearText(By by, int... index) throws InterruptedException {

		WebElement element = null;

		element = findElement(by, index);
		element.sendKeys(Keys.CONTROL + "a");
		element.sendKeys(Keys.DELETE);
		LogPASS("Text Box silindi");
	}

	protected boolean isEnable(By by, int... index) throws InterruptedException {
		WebElement element;
		// untilElementAppear(by);
		try {
			if (index.length == 0)
				element = driver.findElement(by);
			else
				element = driver.findElements(by).get(index[0]);

			if (element.isEnabled()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			log.error("Error while clicking webelement : " + e);
			LogFAIL("Error while clicking webelement : " + e);
			log.info("Error while clicking webelement : " + e);
			throw new RuntimeException(e);
		}
	}

	public void Wait(int millisecond) throws InterruptedException {
		Thread.sleep(millisecond);
	}

	public void LogPASS(String massege) {
		// if (!AutomationVariables.ScreenShot) {
		ExtentTestManager.getTest().log(LogStatus.PASS, massege);
		log.info(massege);
		// } else {
		// String screenShotPath;
		// try {
		// screenShotPath = GetScreenShot.capture(driver);
		// ExtentTestManager.getTest().log(LogStatus.PASS,
		// massege +
		// ExtentTestManager.getTest().addBase64ScreenShot(screenShotPath));
		// } catch (IOException e) {
		// }
		// }
	}

	public void LogFATAL(String massege) {
		ExtentTestManager.getTest().log(LogStatus.FATAL, massege);
	}

	public void LogFAIL(String massege) {
		ExtentTestManager.getTest().log(LogStatus.FAIL, massege);
	}

	public void LogERROR(String massege) {
		ExtentTestManager.getTest().log(LogStatus.ERROR, massege);
	}

	public void LogINFO(String massege) {
		ExtentTestManager.getTest().log(LogStatus.INFO, massege);
		log.info(massege);
	}

	public void LogWARNING(String massege) {
		String screenShotPath;
		try {
			screenShotPath = GetScreenShot.capture(driver);
			ExtentTestManager.getTest().log(LogStatus.WARNING,
					massege + "\nSnapshot below: " + ExtentTestManager.getTest().addBase64ScreenShot(screenShotPath));
		} catch (IOException e) {
		}
	}
}
