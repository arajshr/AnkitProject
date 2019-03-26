package pageObjectRepository.transaction;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.WebDriverUtils;
import customDataType.Info;

public class FeedBack 
{
	
	WebDriverUtils utils = new WebDriverUtils();
	
	@FindBy(xpath = "//select[@ng-model='ddlPractices']")
	private WebElement ddlPractices;
	
	@FindBy(xpath = "//div[@id='DataTables_Table_0_filter']//input")
	private WebElement search_Encounter;
	
	@FindBy(xpath = "//button[@id='btnAccept']")
	private WebElement btnAccept;
	
	@FindBy(xpath = "//button[@id='btnReject']")
	private WebElement btnReject;
	
	
	@FindBy(xpath = "//table[@id='tblerrprmtrs']")
	private WebElement tblErrorParameters;
	
	@FindBy(xpath = "//div[@id='ErrorParametersModal']//button[text() = 'CANCEL']")
	private WebElement btnError_Cancel;
	
	
	@FindBy(id = "TxtComments")
	private WebElement txtReason_For_Rejection;
	
	@FindBy(xpath = "//h1[@id='ErrorModalLabel']/../following-sibling::div//button[@id = 'btnSave']")
	private WebElement btnSave_Rejection;
	
	@FindBy(xpath="//div[@class = 'sweet-alert hideSweetAlert']/h2")//div[@class = 'sweet-alert showSweetAlert visible']
	private WebElement messageHeader;
	
	@FindBy(xpath = "//div[@class = 'sweet-alert hideSweetAlert']/h2/following-sibling::p")
	private WebElement messageDetails;
	
	
	@FindBy(id = "DataTables_Table_0")
	private WebElement tbl_FeedBack;
	
	String stbl_Feedback = "DataTables_Table_0";
	
	By modal_backdrop = By.xpath("//div[@class = 'modal-backdrop fade']");
	
	
	public void select_Practice(String visibleText, boolean matchWholeText)
	{	
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		utils.select(ddlPractices, visibleText, matchWholeText);
		
	}
	
	/*public void select_Practice(String visibleText)
	{
		utils.select(ddlPractices, visibleText);
		utils.wait_Until_Invisibility_Of_LoadingScreen();
	}*/


	public Info search_Encounter(String encounterID, String claimStatus, String actionCode, String notes, 
														String category, String subCategory, String microCategory, String behaviour, String reason,  String comments, String correctiveActions)
	{
		try 
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			search_Encounter.clear();
			search_Encounter.sendKeys(encounterID);
			utils.wait(2000);
			
			utils.save_ScreenshotToReport("Search_Encounter");
			
			if(utils.isElementPresent(tbl_FeedBack, By.xpath(".//td[text() = '" + encounterID + "']")))
			{				
				List<WebElement> currentRow = tbl_FeedBack.findElements(By.xpath(".//tr/td"));				
				
				String actActionCode = currentRow.get(2).getAttribute("textContent");				
				String actClaimStatus = currentRow.get(3).getAttribute("textContent");				
				String actNotes = currentRow.get(4).getAttribute("textContent");
				/*String actTLComments = currentRow.get(12).getAttribute("textContent");*/
				
				String actErrorCategory = currentRow.get(5).getAttribute("textContent");
				String actSubErrorCategory = currentRow.get(6).getAttribute("textContent");
				String actMicroErrorCategory = currentRow.get(7).getAttribute("textContent");				
				
				String actNC = currentRow.get(8).getAttribute("textContent");
				String actCorrectiveAction = currentRow.get(9).getAttribute("textContent");
				String actQcComments = currentRow.get(10).getAttribute("textContent");					
				
				
				
				StringBuilder error = new StringBuilder();				
				
				error.append(actClaimStatus.equals(claimStatus) ? "" : "Claim Status mismatch, expected [ "+ claimStatus + "] but found [" + actClaimStatus + "]<br>");
				error.append(actActionCode.equals(actionCode) ? "" : "Action Code mismatch, expected [" + actionCode + "] but found [" + actActionCode + "]<br>");				
				error.append(actNotes.equals(notes) ? "" : "Notes mismatch, expected [" + notes + "] but found [" + actNotes + "]<br>");
				
				error.append(actErrorCategory.equals(category) ? "" : "Error Category mismatch, expected [" + category + "] but found [" + actErrorCategory + "]<br>");
				error.append(actSubErrorCategory.equals(subCategory) ? "" : "Sub Error Category mismatch, expected [" + actSubErrorCategory + "] but found [" + subCategory + "]<br>");
				error.append(actMicroErrorCategory.equals(microCategory) ? "" : "Micro Error Category mismatch, expected [" + actMicroErrorCategory +"] but found [" + microCategory + "]<br>");
				error.append(actNC.equals(reason) ? "" : "NC mismatch, expected [" + actNC + "] but found [" + reason + "]<br>");
				error.append(actCorrectiveAction.equals(correctiveActions) ? "" : "Corrective Action mismatch, expected [" + actCorrectiveAction + "] but found [" + correctiveActions + "]<br>");
				error.append(actQcComments.equals(comments) ? "" : "QC comments mismatch, expected [" + actQcComments + "] but found [" + comments + "]<br>");
				
				/*error.append(actTLComments.equals(tlComments) ? "" : "TL comments mismatch, expected ["+ tlComments +"] but found ["+ actTLComments +"]<br>");*/
					
				if(error.length() > 0)
				{
					Configuration.logger.log(LogStatus.WARNING, error.toString());
				}
				return new Info(true, "<b>Encounter ID ["+ encounterID +"] found at Feedback</b>");
			}
			
			return new Info(false, "<b>Encounter ID ["+ encounterID +"] not found at Feedback</b>");
		}
		catch (Exception e) 
		{
			return new Info(false, "Class: FeedbBack, Method: search_Encounter <br>" + e.getMessage());
		}
	}
	
	public void verify_Error_CheckList(String encounterID, ArrayList<String> checkList) 
	{
		
		tbl_FeedBack.findElement(By.xpath(".//td[text() = '"+encounterID+"']/following-sibling::td/a")).click();
		boolean verified = true; 
		
		utils.wait(1000);
		utils.save_ScreenshotToReport("ErrorParameters");
		
		for(int i=0; i<checkList.size(); i++)
		{
			try
			{
				tblErrorParameters.findElement(By.xpath("//td[contains(text() , '" +checkList.get(i)+ "')]"));
			}
			catch (NoSuchElementException e) 
			{
				//System.out.println("Checklist item [" + checkList.get(i) + "] not found");
				Configuration.logger.log(LogStatus.ERROR, "Checklist item [" + checkList.get(i) + "] not found");
				verified = false;
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		utils.wait(2000);
		btnError_Cancel.click();
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(By.id("ErrorParametersModal")));
		
		
		if(verified)
		{
			Configuration.logger.log(LogStatus.INFO, "<b>Error Parameters verified</b>");
		}
	}
	
	
	public boolean accept(String encounterID, String claimStatus, String actionCode, String notes)  
	{
		try 
		{
			//new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(modal_backdrop));
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(btnAccept)).click();			
			utils.wait_Until_InvisibilityOf_LoadingScreen();
	
			ClaimTransaction objClaimTransaction = PageFactory.initElements(Configuration.driver, ClaimTransaction.class); 
			return objClaimTransaction.submit_Claim(claimStatus, actionCode, notes);
		} 
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, "Mehtod: " + e.getMessage());
			return false;
		}
	}


	public boolean reject(String encounterID, String comments) 
	{
		
		try 
		{
			//new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(modal_backdrop));
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(btnReject)).click();
			
			utils.wait(500);
			txtReason_For_Rejection.clear();
			txtReason_For_Rejection.sendKeys(comments);
			btnSave_Rejection.click();
			
			File scrFile = ((TakesScreenshot)Configuration.driver).getScreenshotAs(OutputType.FILE);
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			if(messageHeader.getAttribute("textContent").toUpperCase().equals("SUCCESS"))
			{
				return true;
			}
		 	
		 	else if(messageHeader.getAttribute("textContent").equals("ERROR") || messageHeader.getAttribute("textContent").toUpperCase().equals("INFORMATION"))
			{
				utils.save_ScreenshotToReport("Feedback_reject", scrFile);
				Configuration.logger.log(LogStatus.ERROR, messageHeader.getAttribute("textContent") + "_" + messageDetails.getAttribute("textContent"));
			}
		} 
		catch (Exception e) 
		{
			// TODO: handle exception
		}
		
		
	 	return false;
	}
}
