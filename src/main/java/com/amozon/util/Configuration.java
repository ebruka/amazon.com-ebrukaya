package com.amozon.util;

import java.awt.List;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.Properties;

import org.jfree.util.Log;

import com.amazon.utiliy.ExtentTestManager;
import com.relevantcodes.extentreports.LogStatus;

public class Configuration {

	private static Configuration instance;

	private Properties configProps = new Properties();

	private static String machine;
	private static String env;
	private static String app;
	private static String emails;
	private static String texeckey;
	private static String tplankey;

	public static Configuration getInstance() {

		if (instance == null)
			createInstance();

		return instance;
	}

	private static synchronized void createInstance() {

		if (instance == null)
			instance = new Configuration();
	}

	private Configuration() {

		InputStream is = null;
		try {

			is = ClassLoader.getSystemResourceAsStream("config.properties");
			configProps.load(is);

			setMachine(configProps.getProperty("server"));
			setEnv(configProps.getProperty("env"));
			setApp(configProps.getProperty("app"));
			setEmails(configProps.getProperty("emails"));
			setTexeckey(configProps.getProperty("texeckey"));
			setTplankey(configProps.getProperty("tplankey"));

		} catch (Exception e) {
			Log.error("config.properties dosyası ile alakalı sorun yaşandı.");
			ExtentTestManager.getTest().log(LogStatus.ERROR, "config.properties dosyası ile alakalı sorun yaşandı.");
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					Log.error("config.properties dosyası kapatılamadı.");
					ExtentTestManager.getTest().log(LogStatus.ERROR, "config.properties dosyası kapatılamadı.");
				}
		}
	}

	public static String getApp() {
		app = app.replace(".xml", "").replace("testng", "").replace("for", "");
		if (app.contains("tng"))
			app = "Extent";
		app = app.substring(0, 1).toUpperCase() + app.substring(1, app.length()).toLowerCase();
		return app;
	}

	public static void setApp(String app) {
		Configuration.app = app;
	}

	public static String[] getEmails() {
		if (emails.equals("${env.REPORT_EMAILS}"))
			return null;
		return emails.trim().split(";");
	}

	public static void setEmails(String emails) {
		Configuration.emails = emails;
	}

	public static String getEnv() {
		return env;
	}

	public static void setEnv(String env) {
		Configuration.env = env;
	}

	public static String getMachine() {
		return machine;
	}

	public static void setMachine(String machine) {
		Configuration.machine = machine;
	}

	public static String getConfluencePageId() {
		String pageId = "";
		switch (app + env) {
		case "KisPProd":
			pageId = "15860954";
			break;
		case "MinibisPProd":
			pageId = "16974410";
			break;
		case "RopPProd":
			pageId = "16974414";
			break;
		case "RopTest":
			pageId = "16974416";
			break;
		default:
			break;
		}
		return pageId;
	}

	public static String getTexeckey() {
		if (texeckey.equals("${texeckey}"))
			return "";
		return texeckey;
	}

	public static void setTexeckey(String texeckey) {
		Configuration.texeckey = texeckey;
	}

	public static String getTplankey() {
		if (tplankey.equals("${tplankey}"))
			return "";
		return tplankey;
	}

	public static void setTplankey(String tplankey) {
		Configuration.tplankey = tplankey;
	}
}
