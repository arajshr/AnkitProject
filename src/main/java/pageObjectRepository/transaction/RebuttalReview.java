package pageObjectRepository.transaction;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.WebDriverUtils;

public class RebuttalReview 
{
	WebDriverUtils utils = new WebDriverUtils();
	
	@FindBy(xpath = "//select[@ng-model='ddlPractices']")
	private WebElement ddlPractices;
	
	@FindBy(xpath = "//div[@id='defaultModal']//button[text() = 'CLOSE']")
	private WebElement btn_Close_Pending_Accounts;
	
	@FindBy(xpath = "//div[@id='DataTables_Table_0_filter']//input")
	private WebElement search_Encounter;
	
	@FindBy(xpath = "//button[@id='btnAccept']")
	private WebElement btnAccept;
	
	@FindBy(xpath = "//button[@id='btnReject']")
	private WebElement btnReject;
	
	
	@FindBy(id = "TxtComments")
	private WebElement txtReason_To_Reject;
	
	@FindBy(xpath = "//div[@id='ErrorModal']//button[@id = 'btnSave']")
	private WebElement btnSave_Reject;
	
	
	@FindBy(xpath="//div[@class = 'sweet-alert hideSweetAlert']/h2")
	private WebElement messageHeader;
	
	@FindBy(xpath = "//div[@class = 'sweet-alert hideSweetAlert']/h2/following-sibling::p")
	private WebElement messageDetails;
	
	@FindBy(xpath = "//div[@id = 'ErrorAcceptModal']//textarea[@id='TxtComments']")
	private WebElement txtReason_To_Accept;
	
	@FindBy(xpath = "//div[@id = 'ErrorAcceptModal']//button[@id='btnSave']")
	private WebElement btnSave_Accept;
	
	
	@FindBy(xpath = "//div[@class = 'sweet-alert showSweetAlert visible']//button[text() = 'OK']")
	private WebElement btnOK;
	
	@FindBy(xpath = "//div[@id = 'ErrorAcceptModal']//button[@id='btnCancel']")
	private WebElement btnCancel_Accept;
	
	@FindBy(xpath = "//div[@id = 'ErrorModal']//button[@id='btnCancel']")
	private WebElement btnCancel_Reject;
	
	@FindBy(xpath = "//table[@id='DataTables_Table_0']")
	WebElement tblTLReview;
	
	
	
	
	public boolean search_Encounter(String encounterID, String sAgentComments, String sQCComments) //, String sCorrectiveActions
	{
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		
		search_Encounter.clear();
		search_Encounter.sendKeys(encounterID);
		utils.wait(500);
		
		
		if(utils.isElementPresent(tblTLReview, By.xpath(".//td[text() = '" + encounterID + "']")))
		{						
			if(tblTLReview.findElements(By.xpath(".//td[text() = '"+ encounterID +"']")).size() > 1)
			{
				Configuration.logger.log(LogStatus.WARNING, "Multiple entries at Rebuttal review");
				utils.save_ScreenshotToReport("Rebuttal_MultipleEntries");
			}
			
			
			String actAgentComments = tblTLReview.findElement(By.xpath("//td[text() = '" + encounterID + "']/following-sibling::td[8]")).getAttribute("textContent");
			String actQCComments = tblTLReview.findElement(By.xpath("//td[text() = '" + encounterID + "']/following-sibling::td[9]")).getAttribute("textContent");
			
			
			if(!actAgentComments.equals(sAgentComments))
			{
				Configuration.logger.log(LogStatus.WARNING, "Agent Comments mismatch, expected [" + sAgentComments + "] but found [" + actAgentComments + "]");
			}
			
			if(!actQCComments.equals(sQCComments))
			{
				Configuration.logger.log(LogStatus.WARNING, "QC Comments mismatch, expected [" + sQCComments + "] but found [" + actQCComments + "]");
			}
			
			
			return true;
		}
		
		//utils.save_ScreenshotToReport("Rebuttal_search_Encounter");
		//Configuration.logger.log(LogStatus.FAIL, "Encounter ID ["+ encounterID +"] not found at Rebuttal review");
		return false;
	}
	
	
	public void select_Practice(String visibleText, boolean matchWholeText)
	{	
		utils.wait_Until_Select_Options_Populates(new Select(ddlPractices));
		utils.select(ddlPractices, visibleText, matchWholeText);
		
		utils.wait_Until_InvisibilityOf_LoadingScreen();
	}
	
	
	public int close_Pending_Accounts(String sPractice)
	{
		int iPendingCount = 0;
		
		try 
		{			
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			utils.save_ScreenshotToReport("RebuttalReview_PendingAccount");
			
			if(utils.isElementPresent(By.xpath("//div[@id = 'defaultModal']//td[text() = '"+ sPractice +"']/following-sibling::td[3]")))
			{
				iPendingCount = Integer.parseInt(Configuration.driver.findElement(By.xpath("//div[@id = 'defaultModal']//td[text() = '"+ sPractice +"']/following-sibling::td[3]")).getText());
				
			}
			
			
			Configuration.logger.log(LogStatus.INFO, "<b>Pending accounts for Practice ["+ sPractice +"]: " + iPendingCount + "</b>");
			utils.wait(1000);
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(btn_Close_Pending_Accounts)).click();
						
		} 
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, "Class: RebuttalReview, Mehtod: close_Pending_Accounts <br>" + e.getMessage());
		}
		
		return iPendingCount;
	}
	
	
	public void verify_PendingAccountsCount(int iExpPendingCount, String sPractice) 
	{
		try
		{
			WebElement element = Configuration.driver.findElement(By.id("DataTables_Table_0_info"));
			String arrInfo[] = element.getAttribute("textContent").split(" ");
			int iActPendingCount = Integer.parseInt(arrInfo[5].replace(",", ""));
			
			
			if(iExpPendingCount != iActPendingCount)
			{
				Configuration.logger.log(LogStatus.WARNING, "Pending account count mismatch for Practice ["+ sPractice +"], expected ["+ iExpPendingCount +"] but found ["+ iActPendingCount +"]");
				utils.save_ScreenshotToReport("Rebuttal_PendingAccounts_Error");
			}
		}
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, "Class: RebuttalReview, Mehtod: verify_PendingAccountsCount <br>" + e.getMessage());
		}
		
	}
	
	public boolean accept(String encounterID, String comments) 
	{
		btnAccept.click();      
				
		utils.wait(1000);
		txtReason_To_Accept.clear();
		txtReason_To_Accept.sendKeys(comments);
		btnSave_Accept.click();
		
		utils.wait(100);
		utils.save_ScreenshotToReport("RebuttalReviewAccept");
		
		if(utils.isElementPresent(btnOK) && btnOK.isDisplayed())
	 	{
	 		btnOK.click();
	 	}
		
		if(utils.isElementPresent(btnCancel_Accept) && btnCancel_Accept.isDisplayed())
	 	{
			utils.wait(200);
	 		btnCancel_Accept.click();
	 	}	
		
		
		if(messageHeader.getAttribute("textContent").toUpperCase().equals("SUCCESS"))
		{
			Configuration.logger.log(LogStatus.INFO, "<b>Analyst Feedback accepted at TL Review with comments ["+ comments +"]</b>");
			return true;
		}
	 	
	 	else
		{			
			Configuration.logger.log(LogStatus.FAIL, messageHeader.getAttribute("textContent") + "_" + messageDetails.getAttribute("textContent"));
		}
	 	return false;
	 	
	}


	public boolean reject(String encounterID, String comments) 
	{
		btnReject.click();
		
		
		utils.wait(100);
		txtReason_To_Reject.sendKeys(comments);
		btnSave_Reject.click();
		
		File scrFile = ((TakesScreenshot)Configuration.driver).getScreenshotAs(OutputType.FILE);

		if(utils.isElementPresent(btnOK) && btnOK.isDisplayed())
	 	{
	 		scrFile = ((TakesScreenshot)Configuration.driver).getScreenshotAs(OutputType.FILE);
	 		btnOK.click();
	 	}
		
		if(utils.isElementPresent(btnCancel_Reject) && btnCancel_Reject.isDisplayed())
	 	{
	 		btnCancel_Reject.click();
	 	}	
		
		if(messageHeader.getAttribute("textContent").toUpperCase().equals("SUCCESS"))
		{
			Configuration.logger.log(LogStatus.INFO, "Account rejected with comments ["+ comments +"]");
			return true;
		}
	 	
	 	else if(messageHeader.getAttribute("textContent").toUpperCase().equals("ERROR") || messageHeader.getAttribute("textContent").toUpperCase().equals("INFORMATION"))
		{
			utils.save_ScreenshotToReport("RebuttalReview_reject", scrFile);
			Configuration.logger.log(LogStatus.FAIL, messageHeader.getAttribute("textContent") + "_" + messageDetails.getAttribute("textContent"));
		}
	 	return false;
	}


	
	

}
