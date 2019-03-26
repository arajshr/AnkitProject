package pageObjectRepository.transaction;

import java.io.File;
import java.text.DecimalFormat;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.WebDriverUtils;
import customDataType.Info;
import customDataType.Transaction;

public class ReviewAndApprove 
{	
	WebDriverUtils utils = new WebDriverUtils();
	
	@FindBy(xpath = "//li[@id = 'CLRSP']/a")
	private WebElement lnkClientEscalation;
	
	@FindBy(xpath = "//li[@id = 'CRD']/a")
	private WebElement lnkCredentialing;

	@FindBy(xpath = "//li[@id = 'CRDHLD']/following-sibling::li")
	private WebElement lnkClarification;
	
	@FindBy(xpath = "//li[@id = 'MEOBCLREA']/a")
	private WebElement lnkMEOBClientEscalation;
	
	
	
	@FindBy(xpath = "//select[@ng-model='ddlPractice']")
	private WebElement ddlPractices;
	
	@FindBy(xpath = "//select[@ng-model='ddlPracticeslist']")
	private WebElement ddlMEOBPractices;
	
	
	
	@FindBy(xpath = "//input[@aria-controls='example']")
	private WebElement txtSearch ; // common for both CLSP and CREDLING
	
	@FindBy(xpath = "//input[@aria-controls='DataTables_Table_0']")
	private WebElement txtSearch_Clarification ;
	
	@FindBy(xpath = "//input[@aria-controls='DataTables_Table_1']")
	private WebElement txtSearch_MEOB ;
	
	
	@FindBy(id = "txtMEOBComments")
	private WebElement txtComments_MEOB;
	
	@FindBy(xpath = ".//*[@id='MEOBCLRECACCOUNT_with_icon_title']//button[text() = 'Release']")
	private WebElement btnRelease_MEOB;		
	
	
	@FindBy(id = "TxtComments")
	private WebElement txtComments;
	
	@FindBy(xpath = "//button[text() = 'Release']")
	private WebElement btnRelease;		
	
	@FindBy(xpath = "//button[text() = 'Yes']")
	private WebElement btnYes;
	
	@FindBy(xpath = "//button[text() = 'No']")
	private WebElement btnNo;
	
	
	@FindBy(id = "TxtTLComments")
	private WebElement txtComments_Clarification;
	
	@FindBy(id = "btnSave")
	private WebElement btnSave_Clarification;
	

	
	@FindBy(xpath="//div[@class = 'sweet-alert hideSweetAlert']/h2")
	private WebElement messageHeader;
	
	@FindBy(xpath = "//div[@class = 'sweet-alert hideSweetAlert']/h2/following-sibling::p")
	private WebElement messageDetails;
	
	@FindBy(xpath = "//button[text() = 'OK']")
	private WebElement btnOK;
	
	
	
	@FindBy(id = "example")
	private WebElement tblReview ; // common for both CLSP and CREDLING
	
	@FindBy(id = "DataTables_Table_0")
	private WebElement tblClarification ;
	
	@FindBy(id = "DataTables_Table_1")
	private WebElement tblMEOBClientEscalation ;
	
	
	
	
	public void select_Practice(String practice) 
	{
		try 
		{
			utils.select(ddlPractices, practice, false);		
			
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
			Configuration.logger.log(LogStatus.INFO, "Class: ReviewAndApprove, Mehtod: select_Practice <br>" + e.getMessage());
		}	
	}
	
	
	public void select_MEOBPractice(String practice) 
	{
		try 
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			utils.select(ddlMEOBPractices, practice, false);		
			
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
			Configuration.logger.log(LogStatus.INFO, "Class: ReviewAndApprove, Mehtod: select_MEOBPractice <br>" + e.getMessage());
		}	
	}

	
	
	
	
	
	
	private By page_loader = By.xpath("//div[@class = 'page-loader-wrapper']");
	
	
	public void click_Tab_Credentialing() 
	{
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(page_loader));
		lnkCredentialing.click();	
	}
	
	public void click_Tab_ClientEscalation() 
	{
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(page_loader));		
		lnkClientEscalation.click();		
	}
	
	public void click_Tab_Clarification() 
	{
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(page_loader));		
		lnkClarification.click();		
	}
	
	public void click_Tab_MEOBClientEscalation() 
	{
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(page_loader));		
		lnkMEOBClientEscalation.click();		
	}

	
	
	
	public Info search_Account(String accountNumber, String totalCharges)// common for both CLSP and CREDLING
	{
		try
		{	
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			txtSearch.clear();
			txtSearch.sendKeys(accountNumber);
			utils.wait(2000);
			
			//utils.captureScreenshot("ReviewAndApprove_search_Account");
			
			
			/*if((!totalCharges.contains(".")) || (totalCharges.endsWith(".00")))
			{
				totalCharges = Integer.toString(((int)Float.parseFloat(totalCharges)));
			}*/
			
			//System.out.println(new DecimalFormat("#.00").format(Float.parseFloat(totalCharges)));
			
			String xpath = "//td[text() = '"+accountNumber+"']/following-sibling::td[text() = '$ "+  new DecimalFormat("#.00").format(Float.parseFloat(totalCharges))  +"']";
			
			if(utils.isElementPresent(By.xpath(xpath)))
			{
				if(Configuration.driver.findElements(By.xpath(xpath)).size() > 1)
					Configuration.logger.log(LogStatus.WARNING, "<b>Multiple entries</b>");
				
				return new Info(true, "<b>Account [" + accountNumber + "] found</b>");
			}
			
			return new Info(false, "<b>Account [" + accountNumber + "] not found</b>");
		}
		catch (Exception e) 
		{
			return new Info(false, "Class: ReviewAndApprove, Mehtod: search_Account <br>" + e.getMessage());
		}
	}
	
	public boolean submit_CredentialingResponse(String encounter, String sRemarks)
	{
		try
		{
			WebElement chkEncounter =  Configuration.driver.findElement(By.xpath("//td[text() = '"+encounter+"']/preceding-sibling::td/input"));		
			((JavascriptExecutor)Configuration.driver).executeScript("arguments[0].click();", chkEncounter);		
				 
			txtComments.sendKeys(sRemarks);			
			btnRelease.click();
						
			utils.wait(1000);
			new WebDriverWait(Configuration.driver, 20).until(ExpectedConditions.visibilityOf(btnYes)).click();		
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			File scrFile = ((TakesScreenshot)Configuration.driver).getScreenshotAs(OutputType.FILE);
			if(utils.isElementPresent(By.xpath("//button[text() = 'OK']")))
			{
				new WebDriverWait(Configuration.driver, 20).until(ExpectedConditions.visibilityOf(btnOK));				
				btnOK.click();
			}
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			//String qwqw = messageHeader.getAttribute("textContent");
			
			/*if(messageHeader.getAttribute("textContent").toUpperCase().contains("DONE"))
			{
				Configuration.logger.log(LogStatus.INFO, "<b>Account moved to Credentialing Response</b>");
				return true;
			}
			else*/ if(messageHeader.getAttribute("textContent").toUpperCase().equals("ERROR"))
			{
				utils.save_ScreenshotToReport("submit_Credentialing_Response", scrFile);
				Configuration.logger.log(LogStatus.FAIL, messageHeader.getAttribute("textContent") + "_" + messageDetails.getAttribute("textContent"));
				return false;
			}						
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean submit_ClientEscalationResponse(String encounter, String tlRemarks) 
	{
		try
		{				
			//Configuration.driver.findElement(By.xpath("//td[text() = '"+totalCharges+"']/..//td[text() = '"+accountNumber+"']/preceding-sibling::td/input"));
			WebElement chkEncounter =  Configuration.driver.findElement(By.xpath("//td[text() = '"+encounter+"']/preceding-sibling::td/input"));		
			((JavascriptExecutor)Configuration.driver).executeScript("arguments[0].click();", chkEncounter);
		
			txtComments.sendKeys(tlRemarks);
			btnRelease.click();
			
			utils.wait(1000);
			new WebDriverWait(Configuration.driver, 20).until(ExpectedConditions.visibilityOf(btnYes)).click();		
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			//File scrFile = ((TakesScreenshot)Configuration.driver).getScreenshotAs(OutputType.FILE);
			if(utils.isElementPresent(By.xpath("//button[text() = 'OK']")))
			{
				new WebDriverWait(Configuration.driver, 20).until(ExpectedConditions.visibilityOf(btnOK));
				utils.save_ScreenshotToReport("EscalationRelease");
				btnOK.click();
			}
			
			
			Configuration.logger.log(LogStatus.INFO, "<b>Account moved to Client Escalation Response</b>");
			return true;
			
			
			// NO ALERT DISPLAYED
			
			/*String header = messageHeader.getAttribute("textContent");
			
			utils.wait_Until_Invisibility_Of_LoadingScreen();
			if(header.toUpperCase().contains("DONE"))
			{
				Configuration.logger.log(LogStatus.INFO, "<b>Account moved to Client Escalation Response</b>");
				return true;
			}
			else if(messageHeader.getAttribute("textContent").toUpperCase().equals("ERROR"))
			{
				utils.captureScreenshot("submit_ClientEscalation_Response", scrFile);
				Configuration.logger.log(LogStatus.FAIL, messageHeader.getAttribute("textContent") + "_" + messageDetails.getAttribute("textContent"));
			}	*/		
		}
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, "Mehtod: submit_ClientEscalationResponse" + e.getMessage());
			return false;
		}
		
		
	}
	
	public void cancel_ClientEscalation_Response(String encounter)
	{
		try
		{
			//click check box
			WebElement chkEncounter =  Configuration.driver.findElement(By.xpath("//td[text() = '"+encounter+"']/preceding-sibling::td/input"));		
			((JavascriptExecutor)Configuration.driver).executeScript("arguments[0].click();", chkEncounter);
		
			//release account
			txtComments.sendKeys("remarks");
			btnRelease.click();
			
			
			
			
			//utils.wait(1000);
			new WebDriverWait(Configuration.driver, 20).until(ExpectedConditions.visibilityOf(btnYes)).click();		
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			//File scrFile = ((TakesScreenshot)Configuration.driver).getScreenshotAs(OutputType.FILE);
			
			
			if(utils.isElementPresent(By.xpath("//button[text() = 'OK']")))
			{
				new WebDriverWait(Configuration.driver, 20).until(ExpectedConditions.visibilityOf(btnOK));				
				btnOK.click();
			}
			
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			if(messageHeader.getAttribute("textContent").toUpperCase().contains("DONE"))
			{
				Configuration.logger.log(LogStatus.INFO, "<b>Account moved to Client Escalation Response</b>");
				
			}
			/*else if(messageHeader.getAttribute("textContent").toUpperCase().equals("ERROR"))
			{
				utils.captureScreenshot("cancel_ClientEscalation_Response", scrFile);
				Configuration.logger.log(LogStatus.FAIL, messageHeader.getAttribute("textContent") + "_" + messageDetails.getAttribute("textContent"));
			}*/	
		}
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, "Mehtod: cancel_ClientEscalation_Response" + e.getMessage());
		}
		
	}
	
	

	
	public Info search_Account_Clarification(String UID, String reason, String comments) 
	{
		try 
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			txtSearch_Clarification.clear();
			txtSearch_Clarification.sendKeys(UID);
			
			utils.save_ScreenshotToReport("Clarification_search_Account");
			
			if(utils.isElementPresent(tblClarification, By.xpath(".//td[text() = '"+ UID +"']")))
			{
				String actReason = tblClarification.findElement(By.xpath("//td[text() = '"+ UID +"']/following-sibling::td[6]")).getAttribute("textContent");
				String actComments = tblClarification.findElement(By.xpath("//td[text() = '"+ UID +"']/following-sibling::td[7]")).getAttribute("textContent");
				
				
				if(!actReason.equals(reason))
				{
					Configuration.logger.log(LogStatus.WARNING, "Reason mismatch, expected ["+reason+"] but found ["+actReason+"]");
				}
				
				if(!actComments.equals(comments))
				{
					Configuration.logger.log(LogStatus.WARNING, "Comment mismatch, expected ["+comments+"] but found ["+actComments+"]");
				}
				
				return new Info(true, "<b>UID ["+ UID +"] found in Clarification release</b>");
			}			
			
			return new Info(false, "<b>UID ["+ UID +"] not found in Clarification release</b>");
		} 
		catch (Exception e) 
		{
			return new Info(false, "Class: ReviewAndApprove, Mehtod: search_Account_Clarification <br>" + e.getMessage());
		}
		
		
	}

	public Info submit_Clarification(String remarks) 
	{
		try 
		{
			txtComments_Clarification.sendKeys(remarks);
			btnSave_Clarification.click();
			utils.wait(200); 
			
			utils.save_ScreenshotToReport("SubmitClarification");
			if(messageHeader.getAttribute("textContent").toUpperCase().equals("SUCCESS"))
			{
				return new Info(true, "Clarification saved");
			}
			
			return new Info(false, messageHeader.getAttribute("textContent") + "_" + messageDetails.getAttribute("textContent"));
		} 
		catch (Exception e) 
		{
			return new Info(false, "Class: ReviewAndApprove, Mehtod: submit_Clarification <br>" + e.getMessage());
		}
		
		
	}
	
	
	
	
	public Info search_Inventory(String inventoryId)// meob
	{
		try
		{			
			txtSearch_MEOB.clear();
			txtSearch_MEOB.sendKeys(inventoryId);	
			
						
			Configuration.driver.findElement(By.xpath("//td[text() = '" + inventoryId + "']"));
			return new Info(true, "Inventory [" + inventoryId + "] found");
				
		}
		catch (NoSuchElementException e) 
		{
			utils.save_ScreenshotToReport("meob_CLESCRSP");
			return new Info(false, "<b>Inventory [" + inventoryId + "] not found</b>");
		}
		catch (Exception e) 
		{
			return new Info(false,  "Class: ReviewAndApprove, Mehtod: search_Inventory <br>" + e.getMessage());
		}
	}
	
	
	
	public Transaction submit_MEOBClientEscalationResponse(String inventoryId, String sRemarks)
	{
		try
		{
			// select check box
			WebElement chkEncounter =  Configuration.driver.findElement(By.xpath("//td[text() = '"+inventoryId+"']/preceding-sibling::td/input"));		
			((JavascriptExecutor)Configuration.driver).executeScript("arguments[0].click();", chkEncounter);		
			
			// enter comments and release
			txtComments_MEOB.sendKeys(sRemarks);			
			btnRelease_MEOB.click();
			
			
			utils.wait(1000);
			new WebDriverWait(Configuration.driver, 20).until(ExpectedConditions.visibilityOf(btnYes)).click();		
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			
			File scrFile = ((TakesScreenshot)Configuration.driver).getScreenshotAs(OutputType.FILE);
			if(utils.isElementPresent(btnOK) && btnOK.isDisplayed())
			{								
				btnOK.click();
			}
			
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			if(messageHeader.getAttribute("textContent").toUpperCase().equals("ERROR"))
			{
				utils.save_ScreenshotToReport("meob_ClientEscalation_Response", scrFile);
				return new Transaction(false, messageHeader.getAttribute("textContent") + "_" + messageDetails.getAttribute("textContent"));
			}						
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Transaction(false, "Class:  , Mehtod:  <br>" + e.getMessage());
		}
		return new Transaction(true, "Client Escalation response saved");
	}
}
