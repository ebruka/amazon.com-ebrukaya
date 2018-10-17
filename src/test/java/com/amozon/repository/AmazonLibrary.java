package com.amozon.repository;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import com.amazon.utiliy.log;
import com.amozon.base.AbstractPage;
import com.amozon.data.GetData;
import com.amozon.pageobject.AmazonObjectRepository.*;
import com.amozon.util.DataFinder;
import com.graphbuilder.struc.LinkedList;



public class AmazonLibrary extends AbstractPage {

	List<String[]> urunler = new ArrayList();

	public <T> AmazonLibrary(RemoteWebDriver driver) {
		super(driver);
	}

	public void Control(boolean statement, String onTrue, String onFalse) {
		if (statement == true) {
			LogPASS(onTrue);
		} else {
			LogFAIL(onFalse);
			Assert.assertTrue(false);
		}
	}

	public void ControlWarning(boolean statement, String onTrue, String onFalse) {
		if (statement == true)
			LogPASS(onTrue);
		else
			LogWARNING(onFalse);
	}

	/**
	 * Ürün serch ekranından sonra ürün seçimi için kullanılan fonksiyon
	 * 
	 * @param index
	 *            : .inci elementi secer.
	 * @throws InterruptedException
	 */
	public void ProductFinder(int index) throws InterruptedException {
		String baslik = getTextOfElement(By.xpath("//li[@class='s-result-item celwidget  '][" + index + "]//h2"));
		String[] fiyat = getTextOfElement(By.xpath("//li[@class='s-result-item celwidget  '][" + index + "]//span[@class='sx-price sx-price-large']"))
				.split(" ");
		String fiyatBirlesik = (fiyat[0] + fiyat[1] + "." + fiyat[2]);
		String[] addList = { baslik, fiyatBirlesik };
		urunler.add(addList);

		click(By.xpath("//li[@class='s-result-item celwidget  '][" + index + "]//h2"));

		Control(baslik.contains(getTextOfElement(By.id("titleSection"))), "Baslik kontrolu basarili",
				"Baslik kontrolu hatali");
		Control(fiyatBirlesik.contains(getTextOfElement(By.xpath("//span[@id='priceblock_ourprice']"))),
				"Fiyat kontrolu basarili", "Fiyat kontrolu hatali");
	}

}
