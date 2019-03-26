package pageObjectRepository.historicalReports;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.asserts.SoftAssert;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.WebDriverUtils;
import customDataType.Info;
import customDataType.Transaction;
import pageObjectRepository.Dashboard;
import pageObjectRepository.transaction.CodingInbox;
import pageObjectRepository.transaction.PaymentInbox;

public class MyProductionDetailsReport 
{	
	WebDriverUtils utils = new WebDriverUtils();
	
	@FindBy(xpath = "//select[@ng-model='ddlClient']")
	private WebElement ddlClient;
	
	@FindBy(xpath = "//select[@ng-model='ddllocation']")
	private WebElement ddlLocation;
	
	@FindBy(xpath = "//select[@ng-model='ddlPractice']")
	private WebElement ddlPractices;
	
	@FindBy (id = "btnShow")
	private WebElement btnShow;
	
	
	@FindBy(xpath = "//div[@id='DataTables_Table_0_filter']//input")
	private WebElement txtSearch;
	
	@FindBy(xpath = "//table[@id='DataTables_Table_0']")
	private WebElement tblProduction;
	
	
	
	public MyProductionDetailsReport select_Client(String visibleText)
	{
		utils.select(ddlClient, visibleText);
		return this;
	}
	
	public MyProductionDetailsReport select_Location(String visibleText)
	{
		utils.select(ddlLocation, visibleText);
		return this;
	}
	
	public MyProductionDetailsReport select_Practice(String visibleText)
	{
		utils.select(ddlPractices, visibleText);
		return this;
	}
	
	public void get_Report()
	{
		btnShow.click();		
		utils.wait_Until_InvisibilityOf_LoadingScreen();
	}

	public SoftAssert verify_Production(String patientAccount, String encounterID, String claimStatus, String actionCode, String notes) 
	{
		SoftAssert s = new SoftAssert();
		
		txtSearch.clear();
		txtSearch.sendKeys(encounterID);
		
		try
		{
			
			if(utils.isElementPresent(tblProduction, By.xpath(".//td[text() = '"+encounterID+"']")))
			{
				String actClaimStatus = tblProduction.findElement(By.xpath(".//td["+utils.getColumnIndex(tblProduction, "ClaimStatus Code")+"]")).getAttribute("textContent");
				String actActionCode = tblProduction.findElement(By.xpath(".//td["+utils.getColumnIndex(tblProduction, "Action Code")+"]")).getAttribute("textContent");
				String actNotes = tblProduction.findElement(By.xpath(".//td["+utils.getColumnIndex(tblProduction, "Notes")+"]")).getAttribute("textContent");
				
				s.assertEquals(actClaimStatus, claimStatus);
				s.assertEquals(actActionCode, actionCode);
				s.assertEquals(actNotes, notes);
			}
			else
			{
				Configuration.logger.log(LogStatus.FAIL, "Production detail not found for Encounter ID ["+encounterID+"]");
			}
		}
		catch (NoSuchElementException e) 
		{
			e.printStackTrace();
		}
		return s;
		
	}

	public void edit(String encounterID) 
	{
		try 
		{
			txtSearch.clear();
			txtSearch.sendKeys(encounterID);
			
			tblProduction.findElement(By.xpath("//td[text() = '"+encounterID+"']/following-sibling::td/div/button")).click();
			
		} 
		catch (Exception e) 
		{
			// TODO: handle exception
		}
		
	}

	public Transaction verify_InNextQueue(String queue, String sUID, String practice) 
	{
		Dashboard objDashboard = PageFactory.initElements(Configuration.driver, Dashboard.class);
		CodingInbox objCodingInbox = PageFactory.initElements(Configuration.driver, CodingInbox.class);
		PaymentInbox objPaymentTeamInbox = PageFactory.initElements(Configuration.driver, PaymentInbox.class);
		
		if(queue.equals("coding"))
		{
			//objDashboard.navigateTo_Transaction().navigateTo_Coding_Inbox();
			objCodingInbox.select_Practice(practice, false);
			Configuration.logger.log(LogStatus.INFO, "Navigate to Coding inbox and Select practice [" + practice + "]");
			
			Info searchResult = objCodingInbox.search_UID(sUID);			
			return (searchResult.getStatus() == true) ?  new Transaction(true, searchResult.getDescription()) :  new Transaction(false, searchResult.getDescription()); 			
		}
		else if(queue.equals("payment"))
		{
			objDashboard.navigateTo_Transaction().navigateTo_Payment_Inbox();		
			objPaymentTeamInbox.select_Practice(practice, false); 
			Configuration.logger.log(LogStatus.INFO, "Navigate to Payment Inbox and Select practice [" + practice+ "]");
			
			Info searchResult = objPaymentTeamInbox.search_UID(sUID);	
			return (searchResult.getStatus() == true) ?  new Transaction(true, searchResult.getDescription()) :  new Transaction(false, searchResult.getDescription());
			
			/*utils.captureScreenshot("PaymentTeam", "inbox_search");*/
		}
		else if(queue.equals("printAndMail"))
		{
			
		}
		else if(queue.equals("credentialing"))
		{
			
		}		
		else if(queue.equals("clientEscalation"))
		{
			
		}
		else if(queue.equals("clarification"))
		{
			
		}
		else if(queue.equals("arClosed"))
		{
			
		}
		return new Transaction(false, "<b>Next queue did not match");
	}
	
}
