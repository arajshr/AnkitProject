package pageObjectRepository.historicalReports;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.WebDriverUtils;
import pageObjectRepository.SearchDate;

public class ProductionReport 
{
	WebDriverUtils utils = new WebDriverUtils();
	
	@FindBy (id = "FromDate")
	private WebElement dtpFromDate;
	
	@FindBy (id = "ToDate")
	private WebElement dtpToDate;
	
	@FindBy(xpath = "//select[@ng-model='ddlClient']")
	private WebElement ddlClient;
	
	@FindBy(xpath = "//select[@ng-model='ddllocation']")
	private WebElement ddlLocation;
	
	@FindBy(xpath = "//select[@ng-model='ddlPractice']")
	private WebElement ddlPractices;
	
	@FindBy(xpath = "//select[@ng-model='ddlUser']")
	private WebElement ddlUser;
		
	@FindBy (id = "btnShow")
	private WebElement btnShow;
	
	
	
	public ProductionReport select_FromDate(String fromDate)
	{		
		dtpFromDate.click();
		PageFactory.initElements(Configuration.driver, SearchDate.class).select_FromDate(fromDate);
		return this;
	}
		
	public ProductionReport select_Client(String visibleText)
	{
		utils.select(ddlClient, visibleText);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return this;
	}
	
	public ProductionReport select_Location(String visibleText)
	{
		utils.select(ddlLocation, visibleText);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return this;
	}
	
	public ProductionReport select_Practice(String visibleText)
	{
		utils.select(ddlPractices, visibleText);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return this;
	}
	
	public ProductionReport select_User(String visibleText)
	{
		utils.select(ddlUser, visibleText);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return this;
	}
	
	public void get_Report()
	{
		btnShow.click();		
		utils.wait_Until_InvisibilityOf_LoadingScreen();
	}
	
	
	public boolean verify_User(String user)
	{
		try
		{
			Configuration.driver.switchTo().frame(0);
			Configuration.driver.findElement(By.xpath("//div[text() = '"+ user +"']")).getAttribute("textContent");
			Configuration.driver.switchTo().defaultContent();
			
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		Configuration.logger.log(LogStatus.INFO, "User ["+user+"] no found in report");
		return false;
	}
	
	
	public String get_Production(String user)
	{		
		try 
		{
			Configuration.driver.switchTo().frame(0);
			String production = Configuration.driver.findElement(By.xpath("//div[text() = '"+user+"']/../following-sibling::td[1]")).getAttribute("textContent");
			Configuration.driver.switchTo().defaultContent();
			
			return production;			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;		
	}
	
	public String get_Target(String user)
	{		
		try 
		{
			Configuration.driver.switchTo().frame(0);
			String target = Configuration.driver.findElement(By.xpath("//div[text() = '"+user+"']/../following-sibling::td[2]")).getAttribute("textContent");
			Configuration.driver.switchTo().defaultContent();
			
			return target;			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;		
	}
	
	public String get_Production_Percent(String user)
	{		
		try 
		{
			Configuration.driver.switchTo().frame(0);
			String productionPercent = Configuration.driver.findElement(By.xpath("//div[text() = '"+user+"']/../following-sibling::td[3]")).getAttribute("textContent");
			Configuration.driver.switchTo().defaultContent();
			
			return productionPercent;
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;		
	}
	
}
