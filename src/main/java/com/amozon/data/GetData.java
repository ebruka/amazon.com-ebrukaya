package com.amozon.data;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import com.amozon.util.DataFinder;

import java.net.URL;

import net.bytebuddy.agent.builder.AgentBuilder.Matchable.AbstractBase;

@com.mongodb.annotations.ThreadSafe
@javax.annotation.concurrent.ThreadSafe
@net.jcip.annotations.ThreadSafe
public class GetData {

	public static String FILESYSDATE;
	public static final int DEFAULT_WAIT = 40;
	public static final int DEFAULT_WAIT_LOADERBOX = 90;
	
	public static final String RESULT_FILE_PATH = "test-output" + File.separator + "TestOtomasyonRaporlari" + File.separator;



	public enum Url {
		 AMAZON_URL;
	}

	// Yüklenen dosyaların yolu
	public static final String SCREENSHOTS_PATH = RESULT_FILE_PATH + "TestOtomasyonRaporlari" + File.separator
			+ "Test Error ScreenShots" + File.separator;
}
