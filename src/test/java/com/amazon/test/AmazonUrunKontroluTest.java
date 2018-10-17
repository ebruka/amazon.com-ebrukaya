package com.amazon.test;

//Yapıldı
import java.lang.reflect.Method;
import java.sql.SQLException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.amazon.utiliy.ExtentTestManager;
import com.amozon.base.AbstractTest;
import com.amozon.data.GetData.*;
import com.amozon.repository.AmazonActions;
import com.amozon.util.DataFinder;
import com.relevantcodes.extentreports.LogStatus;

public class AmazonUrunKontroluTest extends AbstractTest {

	AmazonActions a;

	@BeforeMethod
	public void Before(Method method) {
		ExtentTestManager.startTest(method.getName());
		a = new AmazonActions(driver);
	}

	@Test
	public void Amazon_UrunKontroluTest() throws InterruptedException, SQLException {

		a.navigateTo(Url.AMAZON_URL);
		a.ProductSearch("Samsung Galaxy S9");
		a.ProductFinder(1);
		a.ProductSearch("iphone 7");
		a.ProductFinder(1);
		a.PriceCompare();
		ExtentTestManager.getTest().log(LogStatus.PASS, "Test Passed");
	}

}
