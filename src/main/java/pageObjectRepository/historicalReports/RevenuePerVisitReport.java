package pageObjectRepository.historicalReports;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.asserts.SoftAssert;

import configuration.Configuration;
import configuration.Constants;
import configuration.WebDriverUtils;
import excelLibrary.ExcelHelper;
import pageObjectRepository.SearchDate;

public class RevenuePerVisitReport 
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
		
	@FindBy (id = "btnShow")
	private WebElement btnShow;
	
	
	
	public RevenuePerVisitReport select_FromDate(String fromDate)
	{
		dtpFromDate.click();
		PageFactory.initElements(Configuration.driver, SearchDate.class).select_FromDate(fromDate);		
		return this;
	}
	
	public RevenuePerVisitReport select_ToDate(String toDate)
	{
		dtpToDate.click();
		PageFactory.initElements(Configuration.driver, SearchDate.class).select_ToDate(toDate);		
		return this;
	}
		
	public RevenuePerVisitReport select_Client(String client)
	{
		utils.select(ddlClient, client);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return this;
	}
	
	public RevenuePerVisitReport select_Location(String location)
	{
		utils.select(ddlLocation, location);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return this;
	}
	
	public RevenuePerVisitReport select_Practice(String practice)
	{
		utils.select(ddlPractices, practice);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return this;
	}
	
	public void get_Report()
	{
		btnShow.click();		
		utils.wait_Until_InvisibilityOf_LoadingScreen();
	}
	
	
	
	public SoftAssert verify_Report(String fromDate, String toDate)
	{
		try 
		{		
			DateFormat inputDF  = new SimpleDateFormat("dd/MM/yy");
			
			ExcelHelper ePayment = new ExcelHelper(Constants.xlPayment);
			ArrayList<Double> paidAmount = ePayment.get_FirstPassPayment_PaidAmount("Sheet1", inputDF.parse(fromDate), inputDF.parse(toDate));
			
			double expTotalPayment = 0;
			int expEncounters = paidAmount.size();
			
			
			for(double amount : paidAmount)
			{
				expTotalPayment += amount;
			}
		
			double expRevenuePerVisit = expTotalPayment/expEncounters;
			
			
			Configuration.driver.switchTo().frame(0);
			
			String actTotalPayment = Configuration.driver.findElement(By.xpath("//td/div[text() = 'Total Payment Received']/../../following-sibling::tr/td[1]")).getAttribute("textContent").replace("$", "").replace(",", "");
			String actEncounters =  Configuration.driver.findElement(By.xpath("//td/div[text() = 'Total Payment Received']/../../following-sibling::tr/td[2]")).getAttribute("textContent");
			String actRevenuePerVisit = Configuration.driver.findElement(By.xpath("//td/div[text() = 'Total Payment Received']/../../following-sibling::tr/td[3]")).getAttribute("textContent").replace("$", "").replace(",", "");
			
			
			
			
			SoftAssert s = new SoftAssert();
			s.assertEquals(Double.parseDouble(actTotalPayment), expTotalPayment, "Total Payment, ");
			s.assertEquals(actEncounters, expEncounters, "Nimber of Encounters, ");
			s.assertEquals(Double.parseDouble(actRevenuePerVisit), expRevenuePerVisit, "Revenue Per Visit, ");
			return s;
			
			
		} 
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		finally
		{
			Configuration.driver.switchTo().defaultContent();			
		}
		return null;
	}
}
