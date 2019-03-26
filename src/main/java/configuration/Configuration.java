package configuration;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import db.connectDB;
import excelLibrary.ExcelReader;
import pageObjectRepository.Login;
import pageObjectRepository.Dashboard;

public class Configuration {
	
	WebDriverUtils utils = new WebDriverUtils();
	public static ExcelReader excel;
	
	public static WebDriver driver;  //when non-static throws NullPointerException
	//public static InventoryUpload objUpload;
	
	public static connectDB db = null;
	public static ExtentReports extent;
	public static ExtentTest logger;
	
	Login objLogin;
	Dashboard objDashboard;
	

	@BeforeSuite(groups= {"REG", "SMK"})
	public void startRepot() throws InterruptedException
	{
		/*String loadStatus = utils.loadData();
		if(loadStatus.startsWith("ERROR"))
		{
			throw new SkipException(loadStatus);
		}
		else if(loadStatus.startsWith("SUCCESS"))
		{
			System.out.println("File uploaded successfully..!");
		}*/
		
		extent = new ExtentReports(Constants.reportFolderPath);
		extent.loadConfig(new File(Constants.configFilePath));
		
		
		ActiveScreen objActive = new ActiveScreen();
		Thread th = new Thread(objActive);
		th.setDaemon(true);
		th.start();
	}
	
	@BeforeTest(groups= {"REG", "SMK"})
	public void launchBrowser() throws IOException
	{	
		db = new connectDB();
		excel = new ExcelReader(Constants.xlFilePath);
		
		//objUpload = new InventoryUpload();
		
		switch (Constants.browser) 
		{
			case "firefox":	driver = new FirefoxDriver();
							driver.manage().window().maximize();
							break;
							
			case "chrome":	System.setProperty("webdriver.chrome.driver", "./src//test//resources//chromedriver.exe");
							ChromeOptions chromeOptions = new ChromeOptions();
							chromeOptions.addArguments("--start-maximized");
							driver = new ChromeDriver(chromeOptions);
							break;
			
				case "ie":	System.setProperty("webdriver.ie.driver", "./src//test//resources//IEDriverServer.exe");
							driver = new InternetExplorerDriver();
							break;
			
				default:	break;
		}
		
		driver.get(Constants.url);					
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
	}

	@BeforeMethod(groups= {"REG", "SMK"})
	public void login(Method result)
	{	
		int executableRowIndex = excel.isExecutable(result.getName());	
		
		if(executableRowIndex == 0)
		{
			throw new SkipException("Not executable");
		}
		else if(executableRowIndex < 0)
		{
			System.out.println("RUN_MODE not set - Method ["+result.getName()+"]");
			throw new SkipException("RUN_MODE not set - Method ["+result.getName()+"]");
		}
		
		
		//logger = extent.startTest(result.getName());
	}
	
	@AfterMethod(groups= {"REG", "SMK"})
	public void endLogger(ITestResult result)
	{
		if(result.getStatus() == ITestResult.FAILURE)
		{
			
			if(result.getName().equals("remove_User"))
				utils.save_ScreenshotToReport("removeUser_Login");
				
			logger.log(LogStatus.FAIL, "<b>" + result.getThrowable().getMessage().replace("expected [true] but found [false]", "")
																				 .replace("expected [false] but found [true]", "") + "</b>"); 
			
		}
		
		
		objDashboard = PageFactory.initElements(Configuration.driver, Dashboard.class);
		objDashboard.signOut();
		
		
		extent.endTest(logger);
		extent.flush();		
	}	

	
	@AfterTest(groups= {"REG", "SMK"})
	public void closeBrowser()
	{	
		driver.quit();
	}
	
	
	@AfterSuite(groups= {"REG", "SMK"})
	public void endReport()
	{	
		db.close();
		System.out.println("Connection closed");

		extent.close();		
	}
}
