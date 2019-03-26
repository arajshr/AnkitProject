package pageObjectRepository.historicalReports;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.WebDriverUtils;
import customDataType.Quality;
import pageObjectRepository.SearchDate;

public class QCWorkedAccountsReport 
{
	WebDriverUtils utils = new WebDriverUtils();
	SearchDate objSearchDate;
	
	@FindBy(xpath = "//select[@ng-model = 'ddlClient']") 
	private WebElement ddlClient;
	
	@FindBy(xpath = "//select[@ng-model = 'ddllocation']") 
	private WebElement ddllocation;
	
	@FindBy(xpath = "//select[@ng-model = 'ddlPractice']") 
	private WebElement ddlPractice;
	
	@FindBy(xpath = "//select[@ng-model = 'ddlUser']") 
	private WebElement ddlUser;
	
	@FindBy(id = "FromDate") 
	private WebElement txtFromDate;

	@FindBy(id = "ToDate") 
	private WebElement txtToDate;

	
	@FindBy(xpath = "(//div[@class = 'dtp-actual-month p80'])[1]")
	private WebElement dtpMonth;
	
	@FindBy(xpath = "(//a[@class = 'dtp-select-month-before'])[1]")
	private WebElement dtpFromDate_PreviousMonth;
	
	@FindBy(xpath = "(//div[@class = 'dtp-select-month-after'])[1]")
	private WebElement dtpFromDate_NextMonth;
	
	
	@FindBy(xpath = "(//div[@class = 'dtp-actual-year p80'])[1]")
	private WebElement dtpYear;
	
	@FindBy(xpath = "(//a[@class = 'dtp-select-year-before'])[1]")
	private WebElement dtpFromDate_PreviousYear;
	
	@FindBy(xpath = "(//div[@class = 'dtp-select-year-after'])[1]")
	private WebElement dtpFromDate_NextYear;
	
	@FindBy(xpath = "(//button[text() = 'OK'])[1]")
	private WebElement btn_FromDate_OK;
		
	@FindBy(id = "btnShow") 
	private WebElement btnShow;
	
	@FindBy(xpath = "//input[@title = 'Find Text in Report']")
	private WebElement txtFind;
	
	
	
	
	public void filter_QC_Accounts(String client, String location, String practice, String sUser, String fromDate)
	{
		utils.select(ddlClient, client);
		utils.select(ddllocation, location);
		utils.select(ddlPractice, practice, false);
		utils.select(ddlUser, sUser, false);
		
		
		new WebDriverWait(Configuration.driver, 5).until(ExpectedConditions.elementToBeClickable(btnShow)).click();
		
		
		
		
		
		/*new WebDriverWait(Configuration.driver, 5).until(ExpectedConditions.elementToBeClickable(txtFromDate)).click();
		
		DateFormat inputDF  = new SimpleDateFormat("MM/dd/yy");
		try 
		{
			int expMonth = Integer.parseInt(new SimpleDateFormat("MM").format(inputDF.parse(fromDate)));
			int expDay = Integer.parseInt(new SimpleDateFormat("dd").format(inputDF.parse(fromDate)));
			int expYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(inputDF.parse(fromDate)));
			
			//require month/year validation
			select_Search_FromDate(expYear, expMonth, expDay);
			
			new WebDriverWait(Configuration.driver, 5).until(ExpectedConditions.elementToBeClickable(btnShow)).click();
			//btnSearch.click();
			
		} 
		catch (ParseException e) 
		{			
			e.printStackTrace();
		}		*/
	}
		
	public void select_Search_FromDate(int expYear, int expMonth, int expDay) throws NumberFormatException, ParseException 
	{
		int actYear; 
		int actMonth;
				
		while(true)
		{
			actYear = Integer.parseInt(dtpYear.getAttribute("textContent"));
			if(expYear < actYear)
			{
				new WebDriverWait(Configuration.driver, 5).until(ExpectedConditions.elementToBeClickable(dtpFromDate_PreviousYear)).click();
				//dtpFromDate_PreviousYear.click();
			}
			else if(expYear == actYear)
			{
				break;
			}
		}
		
		
		DateFormat inputDF2  = new SimpleDateFormat("MMM/dd/yy");			
		while(true)
		{		
			//System.out.println(new SimpleDateFormat("MM").format(inputDF2.parse(dtpMonth.getText() +"/01/1900")));	
			
			actMonth = Integer.parseInt(new SimpleDateFormat("MM").format(inputDF2.parse(dtpMonth.getText() +"/01/1900")));
			
			if(expMonth < actMonth)
			{
				dtpFromDate_PreviousMonth.click();
			}
			else if(expMonth > actMonth)
			{
				dtpFromDate_NextMonth.click();;
			}
			else if(expMonth == actMonth)
				break;
		}
		
		String sDay = "";
		
		if(expDay>0 && expDay<10)
		{
			sDay = "0" + expDay;
		}
		else
		{
			sDay = String.valueOf(expDay);
		}
		
		Configuration.driver.findElement(By.xpath("//a[text() = '" + sDay + "']")).click();
		btn_FromDate_OK.click();
	}

	
	public boolean verify_Report(String sEncounterID, String fromDate, String  client, String location, String practice, String sUser, String sTotalCharges) 
	{
		try
		{
			
			filter_QC_Accounts(client, location, practice, sUser, fromDate);
			Configuration.logger.log(LogStatus.INFO, "Select Practice ["+ practice +"] and from date as ["+ fromDate +"]");
			
			Configuration.driver.switchTo().defaultContent();
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			new WebDriverWait(Configuration.driver, 30).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(0));
					
			
			
			if(utils.isElementPresent(By.xpath("//div[text() = 'No Data Available']")))
			{
				utils.save_ScreenshotToReport("QCreport_NoData");
				Configuration.logger.log(LogStatus.FAIL, "<b>No Data Available</b>");
				return false;
			}
			
			
			txtFind.sendKeys(sEncounterID, Keys.ENTER);
			utils.wait(1000);
			
			Alert a = utils.isAlertPresent();			
			if(a != null)// account not found
			{	
				Configuration.logger.log(LogStatus.FAIL, a.getText());
				a.accept();
				utils.wait(3000);
				return false;
			}
			else // account found
			{
				// to highlight account in screenshot
				//((JavascriptExecutor)Configuration.driver).executeScript("arguments[0].style.border='3px solid red'", elem);
				
				utils.save_ScreenshotToReport("QCWorkedAccountsReport");	
				return true;
			}	
		}
		catch (NoSuchElementException e)
		{
			Configuration.logger.log(LogStatus.ERROR, e.getMessage());
			/*utils.captureScreenshot("QCWorkedReport_NoSuchElementException");*/
			return false;			
		}
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.ERROR, e.getMessage());
			return false;			
		}
		finally 
		{
			Configuration.driver.switchTo().defaultContent();
		}
		
		
	}
}
