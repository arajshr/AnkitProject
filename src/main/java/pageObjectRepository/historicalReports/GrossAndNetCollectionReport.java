package pageObjectRepository.historicalReports;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

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

public class GrossAndNetCollectionReport 
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
	
	
	public GrossAndNetCollectionReport select_FromDate(String fromDate)
	{
		dtpFromDate.click();
		PageFactory.initElements(Configuration.driver, SearchDate.class).select_FromDate(fromDate);		
		return this;
	}
	
	public GrossAndNetCollectionReport select_ToDate(String toDate)
	{
		dtpToDate.click();
		PageFactory.initElements(Configuration.driver, SearchDate.class).select_ToDate(toDate);		
		return this;
	}
		
	public GrossAndNetCollectionReport select_Client(String client)
	{
		utils.select(ddlClient, client);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return this;
	}
	
	public GrossAndNetCollectionReport select_Location(String location)
	{
		utils.select(ddlLocation, location);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return this;
	}
	
	public GrossAndNetCollectionReport select_Practice(String practice)
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

	
	public void verify_Report(String fromDate, String toDate)
	{
		WebDriverUtils utils = new WebDriverUtils();
		
		try
		{
			DateFormat inputDF  = new SimpleDateFormat("dd/MM/yy");
			
			ExcelHelper ePayment = new ExcelHelper(Constants.xlPayment);
			HashMap<String, Double> totalPayment = ePayment.get_Payment_GrossReport("Sheet1", inputDF.parse(fromDate), inputDF.parse(toDate));			
			ArrayList<String> lstUID = ePayment.get_UID_GrossReport("Sheet1", inputDF.parse(fromDate), inputDF.parse(toDate));
			
			ExcelHelper eAging = new ExcelHelper(Constants.xlAging);
			double billedAmount = eAging.get_BilledAmount_GrossRepoet("Sheet1", lstUID);
			
			String sBilledAmount = "0";
			String sTotalPayment = "0";
			String sAdjustment = "0";
			String sGCR = "0";
			String sNCR = "0";
			
			//Configuration.driver.switchTo().frame(0);
			
			if(utils.isElementPresent(By.xpath("//div[contains(@id , 'oReportDiv')]//tr[3]/td[1]/div")))
			{
				sBilledAmount = Configuration.driver.findElement(By.xpath("//div[contains(@id,'oReportDiv')]//tr[3]/td[1]/div")).getAttribute("textContent").replaceAll("$", "").replaceAll(",", "");
			}
			
			if(utils.isElementPresent(By.xpath("//div[contains(@id , 'oReportDiv')]//tr[3]/td[2]/div")))
			{
				sTotalPayment = Configuration.driver.findElement(By.xpath("//div[contains(@id,'oReportDiv')]//tr[3]/td[2]/div")).getAttribute("textContent").replaceAll("$", "").replaceAll(",", "");
			}
			
			if(utils.isElementPresent(By.xpath("//div[contains(@id , 'oReportDiv')]//tr[3]/td[3]/div")))
			{
				sAdjustment = Configuration.driver.findElement(By.xpath("//div[contains(@id,'oReportDiv')]//tr[3]/td[3]/div")).getAttribute("textContent").replace("$ ", "").replaceAll(",", "").trim();
			}
		
			
			if(utils.isElementPresent(By.xpath("//div[contains(@id , 'oReportDiv')]//tr[3]/td[4]/div")))
			{
				sGCR = Configuration.driver.findElement(By.xpath("//div[contains(@id,'oReportDiv')]//tr[3]/td[4]/div")).getAttribute("textContent").replace("%", "");
			}
			
			if(utils.isElementPresent(By.xpath("//div[contains(@id , 'oReportDiv')]//tr[3]/td[5]/div")))
			{
				sNCR = Configuration.driver.findElement(By.xpath("//div[contains(@id,'oReportDiv')]//tr[3]/td[5]/div")).getAttribute("textContent").replace("%", "");
			}
			
			
			
			
			SoftAssert s = new SoftAssert();
			s.assertEquals(Double.parseDouble(sBilledAmount), billedAmount);
			s.assertEquals(Double.parseDouble(sTotalPayment), totalPayment.get("paidAmount"));
			s.assertEquals(Double.parseDouble(sAdjustment), totalPayment.get("adjustment"));
			
			s.assertEquals(Double.parseDouble(sGCR), (totalPayment.get("paidAmount")/billedAmount*100));
			s.assertEquals(Double.parseDouble(sNCR), (totalPayment.get("adjustment")/billedAmount*100));
			
			s.assertAll();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			Configuration.driver.switchTo().defaultContent();
		}
		
	}
}
