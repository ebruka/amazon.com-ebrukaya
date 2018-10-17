package com.amazon.utiliy;

import java.util.HashMap;
import java.util.Map;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

public class ExtentTestManager {

	@SuppressWarnings("rawtypes")
	static Map extentTestMap = new HashMap();
	static ExtentReports extent = ExtentManager.getReporter();

	private ExtentTestManager() {
		throw new IllegalStateException("Utility class");
	}

	public static synchronized ExtentTest getTest() {
		return (ExtentTest) extentTestMap.get((int) (long) (Thread.currentThread().getId()));
	}

	public static synchronized void endTest() {
		extent.endTest((ExtentTest) extentTestMap.get((int) (long) (Thread.currentThread().getId())));
	}

	public static synchronized ExtentTest startTest(String testName) {
		return startTest(testName, "");
	}

	@SuppressWarnings("unchecked")
	public static synchronized ExtentTest startTest(String testName, String desc) {
		/*if(!extentTestMap.containsKey((int) (long) (Thread.currentThread().getId()))){
		ExtentTest test = extent.startTest(testName, desc);
		extentTestMap.put((int) (long) (Thread.currentThread().getId()), test);
		return (ExtentTest) extentTestMap.get((int) (long) (Thread.currentThread().getId()));
	}
	if(getTest().getTest().getName()!=testName){
		ExtentTest test = extent.startTest(testName, desc);
		extentTestMap.put((int) (long) (Thread.currentThread().getId()), test);
	}
	else{
		ExtentTest getTest = getTest();
		extentTestMap.remove((int) (long) (Thread.currentThread().getId()), getTest);
		extentTestMap.put((int) (long) (Thread.currentThread().getId()), getTest);
		
	}*/
		
		if (!extentTestMap.containsKey((int) (long) (Thread.currentThread().getId()))) {
			ExtentTest test = extent.startTest(testName, desc);
			extentTestMap.put((int) (long) (Thread.currentThread().getId()), test);
			return (ExtentTest) extentTestMap.get((int) (long) (Thread.currentThread().getId()));
				}
		if (!getTest().getTest().getName().equals(testName)) {
			ExtentTest test = extent.startTest(testName, desc);
			extentTestMap.put((int) (long) (Thread.currentThread().getId()), test);
		}
		return (ExtentTest) extentTestMap.get((int) (long) (Thread.currentThread().getId()));

	}
}
