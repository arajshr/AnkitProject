package pageObjectRepository.transaction;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.Constants;
import configuration.WebDriverUtils;
import customDataType.Info;
import pageObjectRepository.Dashboard;

public class JuniorCallerInbox
{
	WebDriverUtils utils = new WebDriverUtils();
	Dashboard objDashboard;
	AnalystInbox objInbox;
	ClaimTransaction objClaimTransaction;
	
	
	
	@FindBy(xpath = "//li[contains(@ng-click , 'VCML')]")
	private WebElement imgVoiceMial;
	
	@FindBy(xpath = "//li[contains(@ng-click , 'NCALL')]")
	private WebElement imgNeedCall;
	
	@FindBy(xpath = "//li[contains(@ng-click , 'CLRSPRLSE')]")
	private WebElement imgClientEscalationRelease;
	
	@FindBy(xpath = "//li[contains(@ng-click , 'TSAVE')]")
	private WebElement imgTempSave;
	
	@FindBy(xpath = "//a[contains(@href,'##MEOB_with_icon_title')]")
	private WebElement imgMEOB;
	
	@FindBy(xpath = "//a[contains(@href,'##MEOB_PP_with_icon_title')]")
	private WebElement imgMEOBPPResponse;
	
	@FindBy(xpath = "//a[contains(@href,'##MEOB_FW_with_icon_title')]")
	private WebElement imgMEOBFallowup;
	
	
	
	
	@FindBy(xpath = "//select[@ng-model='ddlPractices']")
	private WebElement ddlPractices;
	
	
	
	
	@FindBy(xpath = "//table[@id='DataTables_Table_1']")
	private WebElement tblPriority;
	
	@FindBy(xpath = "//table[@id='DataTables_Table_2']")
	private WebElement tblRegular;
	
	@FindBy(xpath = "//table[@id='DataTables_Table_5']")
	private WebElement tblDenial;
	
	@FindBy(xpath = "//table[@id='DataTables_Table_14']")
	private WebElement tblClarification;
	
	@FindBy(xpath = "//table[@id='DataTables_Table_15']")
	private WebElement tblClientEscalation_Release;
	
	/*@FindBy(xpath = "//table[@id='DataTables_Table_9']")
	private WebElement tblVoiceCall;	*/
		
	@FindBy(xpath = "//table[@id='DataTables_Table_3']")
	private WebElement tblNeedCall;	
	
	@FindBy(xpath = "//table[@id='DataTables_Table_16']")
	private WebElement tblTempSave;
	
	@FindBy(xpath = "//table[@id='DataTables_Table_18']")
	private WebElement tblAppealsResponse;
	
	@FindBy(xpath = "//table[@id='DataTables_Table_19']")
	private WebElement tblMEOB;	
		
	@FindBy(xpath = "//table[@id='DataTables_Table_20']")
	private WebElement tblMEOB_PPResponse;	
		
	@FindBy(xpath = "//table[@id='DataTables_Table_21']")
	private WebElement tblMEOB_FollowUp;
	
	@FindBy(xpath = "//table[@id='DataTables_Table_22']")
	private WebElement tblFollowUp;
		
	@FindBy(xpath = "//table[@id='DataTables_Table_6']")
	private WebElement tblPayment;
	
	@FindBy(xpath = "//table[@id='DataTables_Table_7']")
	private WebElement tblCoding;
	
	@FindBy(xpath = "//table[@id='DataTables_Table_8']")
	private WebElement tblCredentialing;
	
	
	
	
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_1']")
	private WebElement txtSearch_Priority;
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_2']")
	private WebElement txtSearch_Regular;	
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_5']")
	private WebElement txtSearch_Denial;
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_14']")
	private WebElement txtSearch_Clarification;
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_15']")
	private WebElement txtSearch_ClientEscalation_Release;
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_3']")
	private WebElement txtSearch_NeedCall;
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_16']")
	private WebElement txtSearch_TempSave;
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_18']")
	private WebElement txtSearch_AppealsResponse;
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_19']")
	private WebElement txtSearch_MEOB;	
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_20']")
	private WebElement txtSearch_MEOB_PPResponse;
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_21']")
	private WebElement txtSearch_MEOB_FollowUp;
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_22']")
	private WebElement txtSearch_FollowUp;
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_6']")
	private WebElement txtSearch_Payment;
		
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_7']")
	private WebElement txtSearch_Coding;
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_8']")
	private WebElement txtSearch_Credentialing;
	
	
	
	
	
	@FindBy(xpath="//div[@class = 'sweet-alert hideSweetAlert']/h2")
	private WebElement messageHeader;
	
	@FindBy(xpath = "//div[@class = 'sweet-alert hideSweetAlert']/h2/following-sibling::p")
	private WebElement messageDetails;
	
	
	
	public void select_Practice(String visibleText, boolean matchWholeText)
	{
		utils.select(ddlPractices, visibleText);
		
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

	public void click_Image_VoiceCall() 
	{
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(imgVoiceMial)).click();
		utils.wait_Until_InvisibilityOf_LoadingScreen();
	}
	
	public void click_Image_NeedCall() 
	{
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(imgNeedCall)).click();
		utils.wait_Until_InvisibilityOf_LoadingScreen();		
	}
	
	public void click_Image_ClientEscalationRelease() 
	{
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(imgClientEscalationRelease)).click();
		utils.wait_Until_InvisibilityOf_LoadingScreen();
	}
	
	public void click_Image_TempSave() 
	{
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(imgTempSave)).click();
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		
	}
	
	public void click_image_MEOB() 
	{
		
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(imgMEOB)).click();
		
	}
	
	public void click_image_MEOBPPResponse() {
		
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(imgMEOBPPResponse)).click();
	}
	
	public void click_image_MEOBFollowUp() {
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(imgMEOBFallowup)).click();
	}
	
	
	
	
	
	
	
	public Info search_Account(String accountNumber, Constants.Queues queue) 
	{			
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		
		try
		{
			WebElement element = get_Search_Input(queue);			
			element.sendKeys(accountNumber);	
			
			utils.save_ScreenshotToReport("JrCallerInboxSearch_" + queue);
			
			
			
			if(utils.isElementPresent(get_Queue_Table(queue), By.xpath(".//a[text() = '"+ accountNumber +"']"))) 
			{
				return new Info(true, "<b>Patient Account ["+ accountNumber +"] found at " + queue + "</b>");
			}
			return new Info(false, "<b>Patient Account ["+ accountNumber +"] not found at " + queue + "</b>");
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Info(false, e.getMessage());
		}
	}

	
	private WebElement get_Queue_Table(Constants.Queues queue) 
	{
		switch(queue)
		{
			case REGULAR: return tblRegular;
			case PRIORITY: return tblPriority;
			case CODING_RESPONSE: return tblCoding;
			case CREDENTIALING_RESPONSE: return tblCredentialing;
			case PAYMENT_RESPONSE: return tblPayment;
			case TEMP_SAVE: return tblTempSave;
			case CLIENT_ESCALATION_RELEASE: return tblClientEscalation_Release;
			case CLARIFICATION: return tblClarification;
			case DENIAL: return tblDenial;
			case APPEALS_RESPONSE: return tblAppealsResponse;
			case NEED_CALL: return tblNeedCall;
			
			default:break;
		}
		return null;
	}
	
	private WebElement get_Search_Input(Constants.Queues queue) 
	{
		switch(queue)
		{
			case NEED_CALL: return txtSearch_NeedCall;
			case CLIENT_ESCALATION_RELEASE: return txtSearch_ClientEscalation_Release;
			case TEMP_SAVE: return txtSearch_TempSave;				
			
			case MEOB: return txtSearch_MEOB;
			case MEOB_PP_RESPONSE: return txtSearch_MEOB_PPResponse;
			case MEOB_FOLLOWUP: return txtSearch_MEOB_FollowUp;
			
			case FOLLOW_UP: return txtSearch_FollowUp;
			
			default:break;
		}
		return null;
	}
		
		

	public boolean submit_JrCaller_Response(String accountNumber,String claimStatus, String actionCode, String notes)
	{
		
		objClaimTransaction = PageFactory.initElements(Configuration.driver, ClaimTransaction.class);
		
		Configuration.driver.findElement(By.linkText(accountNumber)).click();
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		 		
		return objClaimTransaction.submit_Claim(claimStatus, actionCode, notes);	
				
					
	//	transaction = Configuration.excel.getExcelData(Constants.sheetTransaction, "voiceCallResponse");	 		
				
		
	 		
	}
	
}
