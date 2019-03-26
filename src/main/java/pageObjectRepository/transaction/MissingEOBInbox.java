package pageObjectRepository.transaction;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.Constants;
import configuration.WebDriverUtils;
import configuration.Constants.Queues;
import customDataType.Info;
import customDataType.Transaction;

public class MissingEOBInbox 
{	
	WebDriverUtils utils = new WebDriverUtils();
			
	// practice
	@FindBy(xpath = "//select[@ng-model='ddlPractices']")
	private WebElement ddlPractices;
	
	
	// queue icons
	@FindBy(xpath = "//li[contains(@ng-click , 'MEOB_CLR')]")
	private WebElement imgMEOB_CallerRespobnse;
	
	@FindBy(xpath = "//li[contains(@ng-click , 'MEOB_CER')]")
	private WebElement imgMEOB_ClientEscalationRelease;

	
	// tables
	@FindBy(xpath = "//table[@id='DataTables_Table_1']")
	private WebElement tblCallerResponse;
	
	@FindBy(xpath = "//table[@id='DataTables_Table_0']")
	private WebElement tblClientEscalationRelease;
	
	
	//search text field
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_1']")
	private WebElement txtSearch_CallerResponse;
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_0']")
	private WebElement txtSearch_ClientEscalationRelease;
	

	
	/* ********  MEOB Transaction   ******** */
	
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
	
	
	
	// alert popup
	@FindBy(xpath="//div[@class = 'sweet-alert hideSweetAlert']/h2")
	private WebElement messageHeader;
	
	@FindBy(xpath = "//div[@class = 'sweet-alert hideSweetAlert']/h2/following-sibling::p")
	private WebElement messageDetails;
	
	
	
	@FindBy(xpath = "//div[@class = 'sweet-alert showSweetAlert visible']//button[text() = 'OK']")
	private WebElement btnOK;
		
	@FindBy(xpath = "//div[@id='headingTwo_1']/h4/a")
	private WebElement accordionHistory;
	
	
	
	
	
	public void select_Practice(String visibleText, boolean matchWholeText)
	{		
		try 
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			utils.select(ddlPractices, visibleText, matchWholeText);		
			
			if(utils.isElementPresent(messageHeader))
			{
				String header = messageHeader.getAttribute("textContent").toUpperCase();
			 	String msg = messageDetails.getAttribute("textContent");
			 	
			 	if(header.equals("ERROR"))
				{
			 		Configuration.logger.log(LogStatus.FAIL, header + "_" + msg);
				}
			}
					
			utils.wait_Until_InvisibilityOf_LoadingScreen();			
		}
		catch (Exception e) 
		{		
			Configuration.logger.log(LogStatus.INFO, "Class: PaymentInbox, Mehtod: select_Practice <br>" + e.getMessage());
		}	
	}
	
	
	
	
	
	public void click_Image_CallerRespobnse()
	{
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(imgMEOB_CallerRespobnse)).click();
		utils.wait_Until_InvisibilityOf_LoadingScreen();		
	}
	
	public void click_Image_ClientEscalationRelease()
	{			
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(imgMEOB_ClientEscalationRelease)).click();
		utils.wait_Until_InvisibilityOf_LoadingScreen();
	}
	
	
	
	/**
	 * Returns true when given account number is present in given queue, else flase
	 * 
	 * @param accountNumber
	 * @param queue
	 * @return
	 */
	public Info search_Account(String accountNumber, Constants.Queues queue) 
	{
		utils.wait_Until_InvisibilityOf_LoadingScreen();		
		try
		{
			get_Search_Input(queue).sendKeys(accountNumber);			
			get_Queue_Table(queue).findElement(By.linkText(accountNumber));
			
			return new Info(true, "<b>Account/ Inventory found</b>");
		}
		catch (NoSuchElementException e)
		{
			//utils.captureScreenshot(queue.toString(), "search_Account_" + accountNumber);			
			return new Info(false, "<b>Account/ Inventory ["+ accountNumber +"] not found at " + queue + "</b>");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Info(false, e.getMessage());
		}
	}
	
	
	
	
	/**
	 * Open claim in given queue
	 * @param queue
	 * @param having
	 * @param value
	 * @return
	 */
	public Transaction open_PatientAccount(Queues queue, String having, String value)
	{
		try
		{
			WebElement linkAccount = null;
			utils.wait(100);
			
			if(having.equals("INVENTORYID")) //MEOB
			{
				linkAccount = get_Queue_Table(queue).findElement(By.xpath(".//td/div/a[text() = '"+ value +"']"));
			}		
						
			Info result = utils.isAccountLocked_MEOB(linkAccount);
			if(result.getStatus() == true) // account locked or can't open
			{
				return new Transaction(false, result.getDescription());
			}
			
			return new Transaction(true, "Claim opened");
		}		
		catch (Exception e) 
		{
			return new Transaction(false, "Class: SeniorCallerInbox, Mehtod: open_PatientAccount <br>" + e.getMessage());
		}
	}
	
	private WebElement get_Queue_Table(Constants.Queues queue) 
	{
		switch(queue)
		{
			case MEOB_CALLER_RESPONSE: return tblCallerResponse;
			case MEOB_CLIENTESCALATION: return tblClientEscalationRelease;			
			
			default:break;
		}
		return null;
	}
	
	private WebElement get_Search_Input(Constants.Queues queue) 
	{
		switch(queue)
		{			
			case MEOB_CALLER_RESPONSE: return txtSearch_CallerResponse;
			case MEOB_CLIENTESCALATION: return txtSearch_ClientEscalationRelease;
			
			default:break;
		}
		return null;
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
			
			/*txtCorrectPayerName.sendKeys(correctPayerName);
			txtFileNameOfEOB.sendKeys(fileNameOfEOB);
			txtFileLocationOfEOB.sendKeys(fileLocationOfEOB);
			txtCallerComments.sendKeys(callerComments);*/
						
			if(!followupDays.isEmpty())
			txtMEOBfollowupdays.sendKeys(followupDays);
				 
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
	
}
