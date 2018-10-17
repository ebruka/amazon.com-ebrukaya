package com.amozon.repository;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.remote.RemoteWebDriver;

import com.amozon.pageobject.AmazonObjectRepository.*;

public class AmazonActions extends AmazonLibrary {

	public static String menu;

	// E2E dataları listeye ekleniyor.
	List<String> grupTanimlamaList = new ArrayList<String>();
	List<String> grupYetkiTanimlamaList = new ArrayList<String>();

	String txtTahsilatNo;

	public <T> AmazonActions(RemoteWebDriver driver) {

		super(driver);
	}

	public void ProductSearch(String productname) throws InterruptedException {

		sendKeys(CommonPage.search.getBy(), productname);
		click(CommonPage.searchButton.getBy());
		LogPASS("Product search success");
	}

	public void PriceCompare() throws InterruptedException {

		for (String[] urun : urunler) {
			LogINFO("Urun adi : " + urun[0] + " Fiyati : " + urun[1]);
		}
	}
}