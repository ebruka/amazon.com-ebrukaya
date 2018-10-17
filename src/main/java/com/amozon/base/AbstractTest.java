package com.amozon.base;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITest;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.ReporterConfig;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.amazon.utiliy.ExtentManager;
import com.amazon.utiliy.ExtentTestManager;
import com.amazon.utiliy.GetScreenShot;
import com.amazon.utiliy.log;
import com.amozon.data.GetData;
import com.amozon.util.Configuration;
import com.relevantcodes.extentreports.LogStatus;
import com.relevantcodes.extentreports.model.Log;
import com.thoughtworks.selenium.Selenium;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class AbstractTest {

	private boolean SEND_MAIL = true;
	protected RemoteWebDriver driver;
	protected static Configuration config = Configuration.getInstance();
	public static String reportFilePath;
	public static String sysdate = getSysDateCustom();
	public URL REMOTE_URL;

	@SuppressWarnings("static-access")
	public static class AutomationVariables {
		public static final String ENVIRONMENT = config.getEnv();// AbstractTest.ENVIRONMENT;
		public static final String MACHINE = config.getMachine();// AbstractTest.MACHINE;
		public static final String APP = config.getApp();
		public static final String TEXECKEY = config.getTexeckey();
		public static final String TPLANKEY = config.getTplankey();
		public static final String[] EMAILS = config.getEmails();
		public static final boolean ScreenShot = false;
	}

	@BeforeSuite
	public void StartSuite(ITestContext ctx) throws MalformedURLException {
		log.startTestCase(ctx.getCurrentXmlTest().getSuite().getName());
	}

	@BeforeTest
	public void StartTest(ITestContext ctx) throws IOException {
		log.startTestCase(ctx.getName());
	}

	@AfterTest
	public void StopTest(ITestContext ctx) {
		log.endTestCase(ctx.getName());
	}

	@BeforeMethod
	public void setUp2(ITestContext ctx) throws MalformedURLException {

		DOMConfigurator.configure("src/test/resources/Log4j.xml");

		ExtentManager.getReporter();
		reportFilePath = ExtentManager.REPORT_FILE_PATH;
		setUpBrowser();
		// killProcess();
	}

	@SuppressWarnings("deprecation")
	public void setUpBrowser() throws MalformedURLException {
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", chromePrefs);
		// options.addArguments("window-size=1024,768");
		DesiredCapabilities capability = DesiredCapabilities.chrome();
		capability.setCapability(ChromeOptions.CAPABILITY, options);
		capability.setBrowserName("chrome");
		// capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		capability.setJavascriptEnabled(true);

		// driver = new FirefoxDriver();
		// driver = new InternetExplorerDriver();
		driver = new ChromeDriver(capability);

		driver.manage().timeouts().pageLoadTimeout(GetData.DEFAULT_WAIT, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(GetData.DEFAULT_WAIT, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		log.info("Setup started");
		log.info("Chromedriver has been set up.");
	}
	/*
	 * public String extractScreenShot(WebDriverException e) { Throwable cause =
	 * e.getCause(); if (cause instanceof ScreenshotException) { return
	 * ((ScreenshotException) cause).getBase64EncodedScreenshot(); } return
	 * null; }
	 */

	/*
	 * @SuppressWarnings("deprecation")
	 * 
	 * @BeforeClass(alwaysRun = true) public void parrentTestStart() throws
	 * IOException { }
	 * 
	 * @AfterClass(alwaysRun = true) public void parrentTestEnd() {
	 * ExtentTestManager.endTest(); ExtentManager.getReporter().flush(); }
	 */

	@AfterMethod
	public void getResult(ITestResult result, Method method) throws IOException {
		String testName = method.getName();
		if (result.getStatus() == ITestResult.SKIP) {
			// String screenShotPath = GetScreenShot.capture(driver, testName +
			// "SKIP_" + getSysDateCustom());
			// ExtentTestManager.getTest().log(LogStatus.SKIP,
			// result.getThrowable());
			// ExtentTestManager.getTest().log(LogStatus.SKIP, "Snapshot below:
			// " + ExtentTestManager.getTest().
			// addBase64ScreenShot(screenShotPath));
			// List<Log> logs =
			// ExtentTestManager.getTest().getTest().getLogList();
			// for (Log log : logs)
			// log.setLogStatus(LogStatus.SKIP);
			ExtentTestManager.getTest().getTest().getLogList().clear();

			Dimension screenSize = driver.manage().window().getSize();
			double width = screenSize.getWidth();
			double height = screenSize.getHeight();
			// ExtentTestManager.getTest().log(LogStatus.INFO, (width + " x " +
			// height).replace(".0", ""));
			ExtentTestManager.getTest().log(LogStatus.INFO, "Tekrarlandi");

			ExtentTestManager.getTest().getTest().setStatus(LogStatus.INFO);
		}
		if (result.getStatus() == ITestResult.FAILURE) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, result.getThrowable());
			ExtentTestManager.getTest().log(LogStatus.FAIL, "Snapshot below: "
					+ ExtentTestManager.getTest().addBase64ScreenShot(GetScreenShot.capture(driver)));

		} else if (result.getStatus() == ITestResult.SUCCESS)
			ExtentTestManager.getTest().log(LogStatus.PASS, "Test Passed");

		ExtentTestManager.endTest();
		ExtentManager.getReporter().flush();

		result.getTestContext().getSkippedTests().removeResult(result);
		result.getTestContext().getSkippedConfigurations().removeResult(result);
		result.getTestContext().getFailedConfigurations().removeResult(result);

		// driver.manage().deleteAllCookies();

		driver.close();
		log.info("Driver has been quit.");
		log.info("Browser closed.");

		driver.quit();
		System.out.println(reportFilePath);
	}

	@AfterSuite
	
	private static void killProcess() {

		Runtime rt = Runtime.getRuntime();
		try {
			rt.exec("taskkill /f /im " + "chrome.exe");
			rt.exec("taskkill /f /im " + "chromedriver.exe");
			rt.exec("taskkill /f /im " + "conhost.exe");
		} catch (IOException e) {
			log.info("Processler Kill Edilememdi!!!");
		}
	}

	public static String getSysDateCustom() {

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd-HH;mm;ss");
		LocalDateTime now = LocalDateTime.now();
		String sysDate = dtf.format(now);

		return sysDate;
	}
}
