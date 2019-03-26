package pageObjectRepository.qc;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.WebDriverUtils;
import customDataType.Info;

public class QCA_AuditSheet
{
	WebDriverUtils utils = new WebDriverUtils();
	
	@FindBy(xpath="//*[contains(text(), 'Patient Acc No')]/following-sibling::div/label")
	private WebElement pateientAccountNo;
	
	@FindBy(xpath="//div[@class = 'sweet-alert hideSweetAlert']/h2")//div[@class = 'sweet-alert showSweetAlert visible']
	private WebElement messageHeader;
	
	@FindBy(xpath = "//div[@class = 'sweet-alert hideSweetAlert']/h2/following-sibling::p")
	private WebElement messageDetails;
	
	@FindBy(xpath = "//button[text() = 'OK' and @class = 'confirm']")
	private WebElement btnOK;
	
	@FindBy(xpath = "//div[@id='headingTwo_6']/h4/a")
	private WebElement headerHistory;
	
	@FindBy(xpath = "//div[@id='headingOne_6']/h4/a")
	private WebElement headerErrorDetails ;
	
	
	@FindBy(xpath = "//td[text() = 'Was the transaction screen verified']/following-sibling::td/select")
	private WebElement ddl_Transaction_Screen;
	
	@FindBy(xpath = "//td[text() = 'Was the Patient documents/claim form verified when required']/following-sibling::td/select")
	private WebElement ddl_Pateient_Document;
	
	@FindBy(xpath = "//td[contains(text() , 'eligibility information verified when needed')]/following-sibling::td/select")
	private WebElement ddl_Eligibility_Information;
	
	@FindBy(xpath = "//td[text() = 'Was the escalation verified in tracker']/following-sibling::td/select")
	private WebElement ddl_Escalation;
	
	@FindBy(xpath = "//td[text() = 'Did the collector verify if specific payer portal is available before placing the call']/following-sibling::td/select")
	private WebElement ddl_Payer_Portal;
	
	@FindBy(xpath = "//td[text() = 'Was all the CPT codes verified were processed/paid']/following-sibling::td/select")
	private WebElement ddl_CPT_Code;
	
	@FindBy(xpath = "//td[text() = 'Decision on calling Payer']/following-sibling::td/select")
	private WebElement ddl_Decision;
	
	@FindBy(xpath = "//td[text() = 'Whether EOB was requested from the payer when not available.']/following-sibling::td/select")
	private WebElement ddl_EOB_Request;
	
	@FindBy(xpath = "//td[text() = 'Was edits verified for claim before submission']/following-sibling::td/select")
	private WebElement ddl_Edits_Verified;
	
	@FindBy(xpath = "//td[text() = 'Was the correct letter/reconsideration form used for paper appeal based on the payer']/following-sibling::td/select")
	private WebElement ddl_Paper_Appeal;
	
	@FindBy(xpath = "//td[text() = 'Was the follow up date set in accordance to the scenario or client specifications']/following-sibling::td/select")
	private WebElement ddl_FollowUp_Date;
	
	@FindBy(xpath = "//td[text() = 'Was the complete claim information retrieved as per the scenario specific SOP guidelines']/following-sibling::td/select")
	private WebElement ddl_Claim_Information;
	
	@FindBy(xpath = "//td[text() = 'Was the claim refilled to appropriate payer']/following-sibling::td/select")
	private WebElement ddl_Claim_Refill;
	
	@FindBy(xpath = "//td[text() = 'Was the insurance profile updated accurately']/following-sibling::td/select")
	private WebElement ddl_Insurance_Profile;
	
	@FindBy(xpath = "//td[text() = 'Was the task pended to appropriate person with accurate information/comment']/following-sibling::td/select")
	private WebElement ddl_Pended_Task;
	
	@FindBy(xpath = "//td[contains(text() , 'Are the Codes modified before filing the claim as per client')]/following-sibling::td/select")
	private WebElement ddl_Code_Modification;
	
	@FindBy(xpath = "//td[text() = 'Was the balance billed to patient EOB/SOP']/following-sibling::td/select")
	private WebElement ddl_Balance;
	
	@FindBy(xpath = "//td[text() = 'Was the claim sent in correct mode/format (electronic/paper']/following-sibling::td/select")
	private WebElement ddl_Claim_Format;
	
	@FindBy(xpath = "//td[text() = 'Was the EOB downloaded / Manual remittance created when required']/following-sibling::td/select")
	private WebElement ddl_EOB_Download;
	
	@FindBy(xpath = "//td[text() = 'Was the issue Escalated to supervisor']/following-sibling::td/select")
	private WebElement ddl_Issue_Escalation;
	
	@FindBy(xpath = "//td[text() = 'Posting Deductible']/following-sibling::td/select")
	private WebElement ddl_Posting_Deductible;
	
	@FindBy(xpath = "//td[text() = 'Appeal Not Done ']/following-sibling::td/select")
	private WebElement ddl_Appeal_Not_Done;
	
	@FindBy(xpath = "//td[text() = 'Incorrect Appeal Sent']/following-sibling::td/select")
	private WebElement ddl_Incorrect_Appeal_Sent;
	
	@FindBy(xpath = "//td[text() = 'Tickler or Follow-up not set']/following-sibling::td/select")
	private WebElement ddl_Tickler_NotSet;
	
	@FindBy(xpath = "//td[text() = 'Was voice mail protocol followed when necessary']/following-sibling::td/select")
	private WebElement ddl_Voice_Mail_Protocol;
	
	@FindBy(xpath = "//td[text() = 'Was the Action details available']/following-sibling::td/select")
	private WebElement ddl_Action;
	
	@FindBy(xpath = "//td[text() = 'Was the Research details available']/following-sibling::td/select")
	private WebElement ddl_Research;
	
	@FindBy(xpath = "//td[text() = 'Was the Conclusion details available']/following-sibling::td/select")
	private WebElement ddl_Conclusion;
	
	@FindBy(xpath = "//td[text() = 'No Task Notes']/following-sibling::td/select")
	private WebElement ddl_No_Task_Notes;
	
	
	/*  Error category elements */
	
	@FindBy(xpath = "//select[@ng-model = 'ddlErrorCategory']")
	private WebElement ddlErrorCategory;
	
	@FindBy(xpath = "//select[@ng-model = 'ddlErrorSubCategory']")
	private WebElement ddlErrorSubCategory;
	
	@FindBy(xpath = "//select[@ng-model = 'ddlErrorMicroCategory']")
	private WebElement ddlErrorMicroCategory;
	
	@FindBy(xpath = "//select[@ng-model = 'ddlBehaviour']")
	private WebElement ddlBehaviour;
	
	@FindBy(xpath = "//select[@ng-model = 'ddlReasonForNC']")
	private WebElement ddlReasonForNC;
	
	@FindBy(id = "TxtCorrectiveActions")
	private WebElement txtCorrectiveActions;
	
	@FindBy(id = "TxtComments")
	private WebElement txtComments;
	
	
	@FindBy(id = "btnSave")
	private WebElement btnSave;
	
	@FindBy(xpath = "//button[text()='CANCEL']")
	private WebElement btnCancel;
	
	
	/* Error Accuracy	*/
	@FindBy(xpath = "//td[text() = 'Accuracy']/following-sibling::td")
	private WebElement tdAccuracy;
	
	@FindBy(xpath = "//td[text() = 'Error']/following-sibling::td")
	private WebElement tdError;
	
	@FindBy(xpath = "(//div[@id = 'collapseOne_20'])[3]//td[text() = 'Reasearch']/following-sibling::td")
	private WebElement tdResearch;
	
	@FindBy(xpath = "(//div[@id = 'collapseOne_20'])[3]//td[text() = 'Decision']/following-sibling::td")
	private WebElement tdDecision;
	
	@FindBy(xpath = "(//div[@id = 'collapseOne_20'])[3]//td[text() = 'Action']/following-sibling::td")
	private WebElement tdAction;
	
	@FindBy(xpath = "(//div[@id = 'collapseOne_20'])[3]//td[text() = 'Documentation']/following-sibling::td")
	private WebElement tdDocumentation;
	
	
	
	public String getError()
	{
		String errorPaercent = tdError.getAttribute("textContent");
		return errorPaercent;
	}
	
	
	/* checklist */
		
	public String select_Transaction_Screen(String value)
	{
		utils.select(ddl_Transaction_Screen, value);
		return ddl_Transaction_Screen.findElement(By.xpath("./../preceding-sibling::td[1]")).getAttribute("textContent").trim();	
	}
	
	public String select_Decision_Calling_Payer(String value)
	{
		utils.select(ddl_Decision, value);
		return ddl_Decision.findElement(By.xpath("./../preceding-sibling::td[1]")).getAttribute("textContent").trim();	
	}
	
	public String select_Appeal_Not_Done(String value)
	{
		utils.select(ddl_Appeal_Not_Done, value);		
		return ddl_Appeal_Not_Done.findElement(By.xpath("./../preceding-sibling::td[1]")).getAttribute("textContent").trim();
	}
	
	
	public String select_Conclusion(String value)
	{
		utils.select(ddl_Conclusion, value);		
		return ddl_Conclusion.findElement(By.xpath("./../preceding-sibling::td[1]")).getAttribute("textContent").trim();
	}
	

	
	
	
	
	
	/* Error categories */
	
	public QCA_AuditSheet select_Error_Category(String value)
	{
		utils.select(ddlErrorCategory, value);
		return this;
	}
	
	public QCA_AuditSheet select_Error_SubCategory(String value)
	{
		utils.select(ddlErrorSubCategory, value);
		return this;
	}
	
	public QCA_AuditSheet select_Error_MicroCategory(String value)
	{
		utils.select(ddlErrorMicroCategory, value);
		return this;
	}
	
	public QCA_AuditSheet select_Error_Behaviour(String value)
	{
		utils.select(ddlBehaviour, value);
		return this;
	}
	
	public QCA_AuditSheet select_Error_ReasonForNC(String value)
	{
		utils.select(ddlReasonForNC, value);
		return this;
	}
	
	public QCA_AuditSheet set_Error_Comments(String value)
	{
		txtComments.sendKeys(value);
		return this;
	}
	
	public QCA_AuditSheet set_Error_CorrectiveActions(String value)
	{
		txtCorrectiveActions.sendKeys(value);
		return this;
	}
	
	
	
	public Info save_Audit()
	{
		try
		{	
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			new WebDriverWait(Configuration.driver, 5).until(ExpectedConditions.elementToBeClickable(btnSave)).click();
			
			utils.wait(100);
		 	utils.save_ScreenshotToReport("SaveAudit");
			
			if(utils.isElementPresent(btnOK) && btnOK.isDisplayed())
			{
				btnOK.click();	
			}
			
			
			if(messageHeader.getAttribute("textContent").toUpperCase().equals("SUCCESS"))
			{
				return new Info(true, "Audit saved");
			}
			else
			{
				return new Info(false, messageHeader.getAttribute("textContent") + "_" + messageDetails.getAttribute("textContent"));
			}
		}
		catch (Exception e) 
		{
			return new Info(false, "Class: QCA_AuditSheet, Mehtod: save_Audit <br>" + e.getMessage());
		}
	}

	public void close_Transaction() 
	{
		btnCancel.click();		
	}

	public void verify_Accuracy_Calculation() 
	{
		SoftAssert s = new SoftAssert();		
		Configuration.logger.log(LogStatus.INFO, "Pateient Account Number : " + pateientAccountNo.getText());
		
		utils.save_ScreenshotToReport("QCA_Accuracy_Calculation");
		
		
		float expError = Float.parseFloat(tdResearch.getText()) + Float.parseFloat(tdDecision.getText()) + Float.parseFloat(tdAction.getText()) +Float.parseFloat(tdDocumentation.getText());
		float expAccuracy = (float) (100.0 - expError);
		
		/*System.out.println("expected -- " + expAccuracy + ", " + expError);		
		System.out.println("actual -- " + tdAccuracy.getText() +", "+ tdError.getText());*/
		
		
		s.assertEquals(Float.parseFloat(tdAccuracy.getText()), expAccuracy, "Accuracy % ");
		s.assertEquals(Float.parseFloat(tdError.getText()), expError, "Error % ");		
		
		close_Transaction();		
		s.assertAll();
		
		Configuration.logger.log(LogStatus.PASS, "Accuracy and Error percentage is calculated as expected");
	}

	public void verify_History(LinkedList<HashMap<String, String>> history)
	{
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		
		headerHistory.click();
		utils.wait(200);
		
		utils.save_ScreenshotToReport("QCATransactionHistory");
		
		boolean status = true;
		
		int iRows = history.size();
		for(int i=0; i<iRows;i++)
		{
			HashMap<String, String> value = history.get(i);			
			//System.out.println("Row - " + (i+1) );
			
			String actWorkedBy = Configuration.driver.findElement(By.xpath("//div[@id='history_trans']//tbody/tr["+(i+1)+"]/td[2]")).getAttribute("textContent");			
			String expWorkedBy = value.get("workedBy");
			
			if(!actWorkedBy.equalsIgnoreCase(expWorkedBy))
			{	
				Configuration.logger.log(LogStatus.ERROR, "WorkedBy do not match in Row["+(i+1)+"] , expected ["+expWorkedBy+"] but found ["+actWorkedBy+"]");
				System.out.println("WorkedBy do not match in Row["+(i+1)+"] , expected ["+expWorkedBy+"] but found ["+actWorkedBy+"]");
				status = false;
			}
		}
		
		if (status)
		{
			Configuration.logger.log(LogStatus.INFO, "<b>Transaction history verified in Audit sheet</b>");
		}
		
		
		headerHistory.click();
		utils.wait(200);
				
		headerErrorDetails.click();
		utils.wait(200);
	}
	
	
	
}









/*public QCA_AuditSheet select_Pateient_Document(String value)
{
	utils.select(ddl_Pateient_Document, value);
	return this;
}

public QCA_AuditSheet select_Eligibility_Information(String value)
{
	utils.select(ddl_Eligibility_Information, value);
	return this;
}

public QCA_AuditSheet select_Escalation(String value)
{
	utils.select(ddl_Escalation, value);
	return this;
}

public QCA_AuditSheet select_Payer_Portal(String value)
{
	utils.select(ddl_Payer_Portal, value);
	return this;
}

public QCA_AuditSheet select_CPT_Code(String value)
{
	utils.select(ddl_CPT_Code, value);
	return this;
}

public QCA_AuditSheet select_Decision(String value)
{
	utils.select(ddl_Decision, value);
	return this;
}

public QCA_AuditSheet select_EOB_Request(String value)
{
	utils.select(ddl_EOB_Request, value);
	return this;
}

public QCA_AuditSheet select_Edits_Verified(String value)
{
	utils.select(ddl_Edits_Verified, value);
	return this;
}

public QCA_AuditSheet select_Paper_Appeal(String value)
{
	utils.select(ddl_Paper_Appeal, value);
	return this;
}
public QCA_AuditSheet select_FollowUp_Date(String value)
{
	utils.select(ddl_FollowUp_Date, value);
	return this;
}
public QCA_AuditSheet select_Claim_Information(String value)
{
	utils.select(ddl_Claim_Information, value);
	return this;
}*/
