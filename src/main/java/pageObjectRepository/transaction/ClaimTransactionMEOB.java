package pageObjectRepository.transaction;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.WebDriverUtils;
import customDataType.Transaction;

public class ClaimTransactionMEOB 
{
	WebDriverUtils utils = new WebDriverUtils();
	
	
	


	/* ********  MEOB Transaction   ******** */
	
	@FindBy(id = "TxtCorrectPayerName")
	private WebElement txtCorrectPayerName;
	
	@FindBy(id = "TxtFileNameOfEOB")
	private WebElement txtFileNameOfEOB;
	
	@FindBy(id = "TxtFileLocationOfEOB")
	private WebElement txtFileLocationOfEOB;
	
	@FindBy(id = "TxtCallerComments")
	private WebElement txtCallerComments;
	
	@FindBy(id = "txtMEOBfollowupdays")
	private WebElement txtMEOBfollowupdays;
	
	
	String sDdlClaimStatusXpath = "//div[@id='largeModalMEOB']//select[@ng-model = 'ddlClaimStatus']";
	@FindBy(xpath = "//div[@id='largeModalMEOB']//select[@ng-model = 'ddlClaimStatus']")
	private WebElement ddlClaimStatus;
	
	@FindBy(xpath = "//div[@id='largeModalMEOB']//select[@ng-model = 'ddlActionCode']")
	private WebElement ddlActionCode;
	
	@FindBy(xpath = "//div[@id='largeModalMEOB']//select[@ng-model = 'ddlCallType']")
	private WebElement ddlResolveType;
	
	@FindBy(xpath = "//div[@id='largeModalMEOB']//textarea[@ng-model = 'Txtnotes']")
	private WebElement txtNotes;
	
	
	
	@FindBy(xpath = "//div[@id='largeModalMEOB']//button[@id= 'btnSave']")
	private WebElement btnSave;
	
	@FindBy(xpath = "//div[@id='largeModalMEOB']//button[@id= 'btnCancel']")
	private WebElement btnCancel;
	

	
	
	/*	ALERT	*/
	
	@FindBy(xpath="//div[@class = 'sweet-alert hideSweetAlert']/h2")
	private WebElement messageHeader;
	
	@FindBy(xpath = "//div[@class = 'sweet-alert hideSweetAlert']/h2/following-sibling::p")
	private WebElement messageDetails;
	
	
	
	@FindBy(xpath = "//div[@class = 'sweet-alert showSweetAlert visible']//button[text() = 'OK']")
	private WebElement btnOK;
		
	@FindBy(xpath = "//div[@id='headingTwo_1']/h4/a")
	private WebElement accordionHistory;
	
	
	
	
	
	/* DateTime Picker - FollowUp date	*/
	
	@FindBy(xpath = "(//div[@class = 'dtp-actual-month p80'])[4]")
	private WebElement dtp_Month;
	
	@FindBy(xpath = "(//a[@class = 'dtp-select-month-before'])[4]")
	private WebElement dtp_PreviousMonth;
	
	@FindBy(xpath = "(//a[@class = 'dtp-select-month-after'])[4]")
	private WebElement dtp_NextMonth;
	
	
	
	@FindBy(xpath = "(//div[@class = 'dtp-actual-year p80'])[4]") // [4] - for followup date in transaction
	private WebElement dtp_Year;
	
	@FindBy(xpath = "(//a[@class = 'dtp-select-year-before'])[4]")
	private WebElement dtp_PreviousYear;
	
	@FindBy(xpath = "(//a[@class = 'dtp-select-year-after'])[4]")
	private WebElement dtp_NextYear;
	
	
	/*@FindBy(xpath = "(//button[text() = 'OK'])[2]")
	private WebElement btn_DateOK;*/
	
	/* ********************************************************* */
	
	
	
	public void select_Date(String date)
	{
		try 
		{
			DateFormat inputDF  = new SimpleDateFormat("MM/dd/yy");
			
			int expMonth = Integer.parseInt(new SimpleDateFormat("MM").format(inputDF.parse(date)));
			int expDay = Integer.parseInt(new SimpleDateFormat("dd").format(inputDF.parse(date)));
			int expYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(inputDF.parse(date)));
						
			
			int actYear; 
			int actMonth;
			
					
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(dtp_Year));
			
			while(true)
			{				
				actYear = Integer.parseInt(dtp_Year.getAttribute("textContent"));
				if(expYear < actYear)
				{
					dtp_PreviousYear.click();
				}
				else if(expYear == actYear)
				{
					break;
				}
			}
			
			
			DateFormat inputDF2  = new SimpleDateFormat("MMM/dd/yy");			
			while(true)
			{	
				new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(dtp_Month));
				actMonth = Integer.parseInt(new SimpleDateFormat("MM").format(inputDF2.parse(dtp_Month.getText() +"/01/1900")));
				
				if(expMonth < actMonth)
				{
					dtp_PreviousMonth.click();
				}
				else if(expMonth > actMonth)
				{
					dtp_NextMonth.click();;
				}
				else if(expMonth == actMonth)
					break;
			}
			
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text() = '" + ((expDay>0 && expDay<10) ? "0" + expDay : String.valueOf(expDay)) + "']"))).click();
		}		
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.WARNING, "Mehtod Name: select_Date<br>" + e.getMessage());
		}
	}
	

	
	
	
	public Transaction submit_MEOB(String claimStatus, String actionCode, String resolveType, String notes, 
			String correctPayerName, String fileNameOfEOB, String fileLocationOfEOB, String callerComments, String followupDays) 
	{
		try
		{
			//new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(sDdlClaimStatusXpath)));
			
			//   fill input fields
			utils.wait(100);
			utils.select(ddlClaimStatus, claimStatus);
			utils.select(ddlActionCode, actionCode);
			utils.select(ddlResolveType, resolveType);
			
			txtCorrectPayerName.sendKeys(correctPayerName);
			txtFileNameOfEOB.sendKeys(fileNameOfEOB);
			txtFileLocationOfEOB.sendKeys(fileLocationOfEOB);
			txtCallerComments.sendKeys(callerComments);
						
			if(!followupDays.isEmpty())
			{
				txtMEOBfollowupdays.click();
				select_Date(followupDays);
			}
				 
			txtNotes.clear();
			txtNotes.sendKeys(notes);
			
			// save
			btnSave.click();
			
			
			File scrFile = ((TakesScreenshot)Configuration.driver).getScreenshotAs(OutputType.FILE);
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			
			if((utils.isElementPresent(btnOK)) && (btnOK.isDisplayed()))
			{			
				scrFile = ((TakesScreenshot)Configuration.driver).getScreenshotAs(OutputType.FILE);
				btnOK.click();
			}
			
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class = 'sweet-overlay']")));
			if(utils.isElementPresent(btnCancel) && btnCancel.isDisplayed())
			{                          
				btnCancel.click();
			}
			
			
			
			String header = messageHeader.getAttribute("textContent").toUpperCase();
			String msg = messageDetails.getAttribute("textContent");
			
			if(header.equals("SUCCESS"))
			{
				return new Transaction(true, "MEOB saved");
			}
			
			else if(header.equals("ERROR") || header.equals("INFORMATION"))
			{
				utils.save_ScreenshotToReport("submit_MEOB", scrFile);
			}
			
			return new Transaction(false, "<b>" + msg + "</b>");
		}
		catch (Exception e) 
		{
			return new Transaction(false, "Class: ClaimTransactionMEOB, Mehtod: submit_MEOB <br>" + e.getMessage());
		}      
	}
	
	
	
	
	public Transaction submit_MEOB(String claimStatus, String actionCode, String resolveType, String notes, String followupDays) 
	{
		try
		{
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(sDdlClaimStatusXpath)));
			
			//   fill input fields
			utils.wait(100);
			utils.select(ddlClaimStatus, claimStatus);
			utils.select(ddlActionCode, actionCode);
			utils.select(ddlResolveType, resolveType);
												
			if(!followupDays.isEmpty())
			txtMEOBfollowupdays.sendKeys(followupDays);
				 
			txtNotes.clear();
			txtNotes.sendKeys(notes);
			
			// save
			btnSave.click();
			
			
			File scrFile = ((TakesScreenshot)Configuration.driver).getScreenshotAs(OutputType.FILE);
			utils.save_ScreenshotToReport("submit_MEOB", scrFile);
			
			
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			
			if((utils.isElementPresent(btnOK)) && (btnOK.isDisplayed()))
			{			
				scrFile = ((TakesScreenshot)Configuration.driver).getScreenshotAs(OutputType.FILE);
				btnOK.click();
			}
			
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class = 'sweet-overlay']")));
			if(utils.isElementPresent(btnCancel) && btnCancel.isDisplayed())
			{                          
				btnCancel.click();
			}
			
			
			
			String header = messageHeader.getAttribute("textContent").toUpperCase();
			String msg = messageDetails.getAttribute("textContent");
			
			if(header.equals("SUCCESS"))
			{
				return new Transaction(true, "MEOB saved");
			}
			
			return new Transaction(false, "<b>" + msg + "</b>");
		}
		catch (Exception e) 
		{
			return new Transaction(false, "Class: ClaimTransactionMEOB, Mehtod: submit_MEOB <br>" + e.getMessage());
		}      
	}
	
}
