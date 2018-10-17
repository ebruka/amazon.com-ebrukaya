package com.amozon.pageobject;

import org.openqa.selenium.By;

import com.amozon.util.DataFinder;

public class AmazonObjectRepository {

	public enum CommonPage {

        search,searchButton;

		public By getBy() {
			return DataFinder.getBy(this.getDeclaringClass().getSimpleName() + "." + this.name());
		}
	}
}