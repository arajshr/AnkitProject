package pageObjectRepository.transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.Constants;
import configuration.WebDriverUtils;
import configuration.Constants.Queues;
import customDataType.Transaction;
import customDataType.Info;
import pageObjectRepository.Dashboard;

public class SeniorCallerInbox
{
	String tblRegularQueue = "DataTables_Table_0";
	
	WebDriverUtils utils = new WebDriverUtils();
	
	Dashboard objDashboard;
	SeniorCallerInbox objInbox;
	ClaimTransaction objClaimTransaction;
	
	/* 	DROPDOWNS	*/
	
	@FindBy(xpath = "//select[@ng-model='ddlPractices']")
	private WebElement ddlPractices;
	
	@FindBy(xpath = "//select[@ng-model='ddlKickout']")
	private WebElement ddlKickout;
	
	@FindBy(xpath = "//div[@id='DataTables_Table_1_length']/label/select")
	private WebElement ddl_Regular_Length;

	@FindBy(xpath = "//div[@id='DataTables_Table_0_length']/label/select")
	private WebElement ddl_Priority_Length;
	
	
	
	/*	QUEUES - ICONS	*/
	
	@FindBy(xpath = "//li[contains(@ng-click , 'REG')]")
	private WebElement imgRegular;
	
	@FindBy(xpath = "//li[contains(@ng-click , 'PRI')]")
	private WebElement imgPriority;
	
	@FindBy(xpath = "//li[contains(@ng-click , 'KCK')]")
	private WebElement imgKickOut;
	
	@FindBy(xpath = "//li[contains(@ng-click , 'TSAVE')]")
	private WebElement imgTempSave;
	
	@FindBy(xpath = "//li[contains(@ng-click , 'JRCRES')]")
	private WebElement imgJrCallerResponse;
	
	@FindBy(xpath = "//li[contains(@ng-click , 'PRNTML_ANL')]")
	private WebElement imgPrintAndMailResponse;
	
	@FindBy(xpath = "//li[contains(@ng-click , 'PYMNT_ANL')]")
	private WebElement imgPaymentResponse;
	
	@FindBy(xpath = "//li[contains(@ng-click , 'CLRSPRLSE')]")
	private WebElement imgClientEscalationRelease;
	
	@FindBy(xpath = "//li[contains(@ng-click , 'CRDRLSE')]")
	private WebElement imgCredentialingResponse;
	
	@FindBy(xpath = "//li[contains(@ng-click , 'CDG_ANL')]")
	private WebElement imgCodingResponse;
	
	@FindBy(xpath = "//li[contains(@ng-click , 'CLR')]")
	private WebElement imgClarification;

	@FindBy(xpath = "//li[contains(@ng-click , 'DNL')]")
	private WebElement imgDenail;
	
	
		
	
	
	/* SEARCH TEXT FIELD */	
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_1']")
	private WebElement txtSearch_Regular;
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_0']")
	private WebElement txtSearch_Priority;
		
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_3']")
	private WebElement txtSearch_JrCallerResponse;
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_5']")
	private WebElement txtSearch_Payment;
		
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_6']")
	private WebElement txtSearch_Coding;
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_7']")
	private WebElement txtSearch_Credentialing;
		
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_8']")
	private WebElement txtSearch_PrintAndMail;
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_13']")
	private WebElement txtSearch_Clarification;
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_14']")
	private WebElement txtSearch_ClientEscalation_Release;
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_19']")
	private WebElement txtSearch_MEOB;
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_20']")
	private WebElement txtSearch_MEOBPPResponse;
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_21']")
	private WebElement txtSearch_MEOBFallowup;
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_22']")
	private WebElement txtSearch_TempSave;
	
	
	
	
	/* TABLES */
	
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
	
	
	/*	MEOB	*/
	
	@FindBy(xpath = "//select[@ng-model='ddlMEOBPractices']")
	private WebElement ddlMEOBPractices;
	
	
	@FindBy(xpath = "//a[contains(@href,'##MEOB_with_icon_title')]")
	private WebElement imgMEOB;
	
	@FindBy(xpath = "//a[contains(@href,'##MEOB_PP_with_icon_title')]")
	private WebElement imgMEOBPPResponse;
	
	@FindBy(xpath = "//a[contains(@href,'##MEOB_FW_with_icon_title')]")
	private WebElement imgMEOBFallowup;
	
	
		
	
	/*	DOS - radio button	*/
	
	@FindBy(id = "radio120")
	private WebElement radio120;
	
	@FindBy(id = "radio90")
	private WebElement radio90;
	
	@FindBy(id = "radio60")
	private WebElement radio60;
	
	@FindBy(id = "radio30")
	private WebElement radio30;
	
	@FindBy(id = "radio0")
	private WebElement radio0;
	
	
	
	/*	PENDING WORK ORDER SUMMARY	*/

	@FindBy(linkText = "Pending Workorder")
	private WebElement lnkWorkOderSummary;
	
	@FindBy(xpath = "//div[@id='WorkOrderSummaryModal']//button[text() = 'CANCEL']")
	private WebElement btn_Cancel_WorkOderSummary;
	
	@FindBy(xpath = "//div[@id='WorkOrderSummaryModal']")
	private WebElement div_WorkOrderSummaryModal;
	
	
	
	/*	ALERT	*/
	
	@FindBy(xpath="//div[@class = 'sweet-alert hideSweetAlert']/h2")
	private WebElement messageHeader;
	
	@FindBy(xpath = "//div[@class = 'sweet-alert hideSweetAlert']/h2/following-sibling::p")
	private WebElement messageDetails;
	
	
	
	
	/**
	 * Selects AR practice
	 * @param visibleText
	 * @param matchWholeText
	 */
	public void select_ARPractice(String visibleText, boolean matchWholeText)
	{		
		try 
		{
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
			utils.select(ddl_Priority_Length,"10", true);
			
		}
		catch (Exception e) 
		{		
			Configuration.logger.log(LogStatus.INFO, "Class: SeniorCallerInbox, Mehtod: select_ARPractice <br>" + e.getMessage());
		}	
	}
	
	
	/**
	 * Selects MEOB practice
	 * @param visibleText
	 * @param matchWholeText
	 */
	public void select_MEOBPractice(String visibleText, boolean matchWholeText)
	{		
		try 
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			utils.select(ddlMEOBPractices, visibleText, matchWholeText);		
			
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
			/*utils.select(ddl_Priority_Length,"10", true);*/
			
		}
		catch (Exception e) 
		{		
			Configuration.logger.log(LogStatus.INFO, "Class: SeniorCallerInbox, Mehtod: select_MEOBPractice <br>" + e.getMessage());
		}	
	}
	
	

	public void click_Image_Regular(boolean entries)
	{
		
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(imgRegular));
		
		//long startTime = System.currentTimeMillis();		
		imgRegular.click();
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		/*long endTime = System.currentTimeMillis();
		
		Configuration.logger.log(LogStatus.INFO, "Time to open account: " + (endTime - startTime)/1000 + " seconds");
		System.out.println("Time to load regular queue: " + (endTime - startTime)/1000 + " sec");*/
		
		
		if(entries)
			utils.select(ddl_Regular_Length,"10", true);
	}
	
	public void click_Image_Priority()
	{
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(imgPriority)).click();
		utils.wait_Until_InvisibilityOf_LoadingScreen();		
	}
	
	public void click_Image_KickOut()
	{
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(imgKickOut)).click();
		utils.wait_Until_InvisibilityOf_LoadingScreen();		
	}
	
	public void click_Image_TempSave()
	{	
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(imgTempSave)).click();
		utils.wait_Until_InvisibilityOf_LoadingScreen();
	}
	
	
	public void click_Image_JrCallerResponse()
	{
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(imgJrCallerResponse)).click();
		utils.wait_Until_InvisibilityOf_LoadingScreen();
	}
	
	public void click_Image_PrintAndMailResponse()
	{
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(imgPrintAndMailResponse)).click();
		utils.wait_Until_InvisibilityOf_LoadingScreen();
	}
	
	public void click_Image_PaymentResponse()
	{
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(imgPaymentResponse)).click();
		utils.wait_Until_InvisibilityOf_LoadingScreen();
	}
	
	public void click_Image_CodingResponse()
	{
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(imgCodingResponse)).click();
		utils.wait_Until_InvisibilityOf_LoadingScreen();
	}
		
	public void click_Image_CredentialingResponse()
	{
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(imgCredentialingResponse)).click();
		utils.wait_Until_InvisibilityOf_LoadingScreen();
	}
	
	public void click_Image_ClientEscalationResponse()
	{
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(imgClientEscalationRelease)).click();
		utils.wait_Until_InvisibilityOf_LoadingScreen();
	}
	
	public void click_Image_Clarification()
	{
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(imgClarification)).click();
		utils.wait_Until_InvisibilityOf_LoadingScreen();
	}
	
	public void click_Image_Denail()
	{
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(imgDenail)).click();
		utils.wait_Until_InvisibilityOf_LoadingScreen();
	}
	
	
	
	public void click_image_MEOB() {
		
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(imgMEOB)).click();
		utils.wait_Until_InvisibilityOf_LoadingScreen();		
	}
	
	public void click_image_MEOBPPResponse() {
		
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(imgMEOBPPResponse)).click();
	}
	
	public void click_image_MEOBFollowUp() {
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(imgMEOBFallowup)).click();
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
			
			if(having.equals("ACCOUNT_NUMBER")) //	regular
			{
				linkAccount = get_Queue_Table(queue).findElement(By.xpath(".//td/a[text() = '"+value+"']"));
			}
			else if(having.equals("INSURANCE"))
			{
				txtSearch_Regular.clear();
				txtSearch_Regular.sendKeys(value);
				
				
				/*if(!utils.isElementPresent(get_Queue_Table(queue), By.xpath(".//td[text() = '"+value+"']/preceding-sibling::td//a")))
				{
					utils.captureScreenshot("MEOBInJrCaller");
					Assert.fail("<b>Inventory ["+value+"] not found in MEOB queue</>");
					
				}*/
				
				utils.wait(1000);				
				linkAccount = get_Queue_Table(queue).findElement(By.xpath(".//td[text() = '"+value+"']/preceding-sibling::td//a"));
			}
			else if(having.equals("INVENTORYID"))	//MEOB   
			{
				WebElement txtSearch = get_Search_Input(queue);
				txtSearch.sendKeys(value);
				
				
				if(!utils.isElementPresent(get_Queue_Table(queue), By.xpath(".//td/div/a[text() = '"+value+"']")))
				{
					utils.save_ScreenshotToReport("MEOBInJrCaller");
					Assert.fail("<b>Inventory ["+value+"] not found in MEOB queue</>");
					
				}
				
				linkAccount = get_Queue_Table(queue).findElement(By.xpath(".//td/div/a[text() = '"+value+"']"));
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
	
	public String get_AccountNumber(Queues queue, int index) 
	{
		return get_Queue_Table(queue).findElement(By.xpath(".//tbody/tr["+ index +"]/td/a")).getText();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void click_Aging_Category_120()
	{
 		JavascriptExecutor executor = (JavascriptExecutor)Configuration.driver;
 		executor.executeScript("arguments[0].click();", radio120);
		
 		utils.wait_Until_InvisibilityOf_LoadingScreen();
	}
	
	public void click_Aging_Category_91_120()
	{
 		JavascriptExecutor executor = (JavascriptExecutor)Configuration.driver;
 		executor.executeScript("arguments[0].click();", radio90);
		
 		utils.wait_Until_InvisibilityOf_LoadingScreen();
	}
	
	public void click_Aging_Category_61_90() 
	{
		
		JavascriptExecutor executor = (JavascriptExecutor)Configuration.driver;
 		executor.executeScript("arguments[0].click();", radio60);
		
 		utils.wait_Until_InvisibilityOf_LoadingScreen();
	}
	
	public void click_Aging_Category_31_60()
	{
		
		JavascriptExecutor executor = (JavascriptExecutor)Configuration.driver;
 		executor.executeScript("arguments[0].click();", radio30);
 		
 		utils.wait_Until_InvisibilityOf_LoadingScreen();	
	}
	
	public void click_Aging_Category_0_30()
	{
		
		JavascriptExecutor executor = (JavascriptExecutor)Configuration.driver;
 		executor.executeScript("arguments[0].click();", radio0);
		
 		utils.wait_Until_InvisibilityOf_LoadingScreen();
	}

	
	

	
	

	
	
	public int get_Count(WebElement table)
	{
		
		List<WebElement> lstLinks = table.findElements(By.tagName("a"));
		Set<String> setAccountNumbers = new HashSet<>();
		
		for (WebElement link : lstLinks) 
		{
			setAccountNumbers.add(link.getAttribute("textContent"));
		}
		return setAccountNumbers.size();
	}
	
	
	

	

	public float get_Pateient_InsBalace(Constants.Queues queue, String accountNumber)
	{
		By findBy = By.xpath(".//a[text() = '"+accountNumber+"']/../following-sibling::td[3]");		
		get_Queue_Table(queue).findElement(findBy).getAttribute("textContent");
		
		return Float.parseFloat(Configuration.driver.findElement(findBy).getAttribute("textContent").replace("$", "").replaceAll(",", ""));		
	}
		
	public boolean verify_AccountRemoved(String accountNumber, String sUID, Constants.Queues queue) 
	{
		utils.wait_Until_InvisibilityOf_LoadingScreen();		
		try
		{
			get_Search_Input(queue).sendKeys(accountNumber);						
			Configuration.driver.findElement(By.linkText(accountNumber));
			
			
			open_PatientAccount(queue, "ACCOUNT_NUMBER", accountNumber);			
			ClaimTransaction objCliam = PageFactory.initElements(Configuration.driver, ClaimTransaction.class);
			boolean isFound = objCliam.verify_UID(sUID);
			objCliam.close_ClaimTransaction();
			
			if(isFound)  //UID is present
			{
				Configuration.logger.log(LogStatus.ERROR, "<b>Account ["+ accountNumber +"], UID ["+ sUID +"] found</b>");
				return false;
			}
			
			Configuration.logger.log(LogStatus.INFO, "<b>Account ["+ accountNumber +"], UID ["+ sUID +"] not found</b>");
			return true;
			
			
		}
		catch (NoSuchElementException e)
		{	
			Configuration.logger.log(LogStatus.INFO, "<b>Account ["+ accountNumber +"] not found</b>");
			
			return true;
		}
		finally
		{
			utils.save_ScreenshotToReport(queue.toString()+"_verify_Account_Removed");
			
		}
	}
	
	
	
	
	
	
	/**
	 * Returns true when Patient Account Number is found in TempSave 
	 *  
	 * @param pateintAccountNumber
	 * @return boolean value
	 */
	public void search_TempSave(String pateintAccountNumber) 
	{
		
		txtSearch_TempSave.clear();
		txtSearch_TempSave.sendKeys(pateintAccountNumber);
		
		if(!utils.isElementPresent(tblTempSave, By.xpath(".//td/a[text() = '"+pateintAccountNumber+"']")))
		{
			//return true;
			utils.save_ScreenshotToReport("search_TempSave");
			Assert.fail("Account ["+ pateintAccountNumber +"] not found at Temp Save");
		}
		
		
		
		//Configuration.logger.log(LogStatus.FAIL, "Account ["+ pateintAccountNumber +"] not found at Temp Save");
		//return false;
	}

	
	/**
	 * Returns true when Patient Account Number is found in Client Escalation Release and 
	 * Verifies TL Comments 
	 * 
	 * @param patientAccountNumber
	 * @param tlComments
	 * @return boolean value
	 */
	public boolean search_Clarification(String patientAccountNumber, String tlComments) 
	{
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		
		txtSearch_Clarification.clear();
		txtSearch_Clarification.sendKeys(patientAccountNumber);
		
		if(utils.isElementPresent(tblClarification, By.xpath(".//a[text() = '"+patientAccountNumber+"']")))
		{
			String actTlComments = tblClarification.findElement(By.xpath("//a[text() = '"+patientAccountNumber+"']/../following-sibling::td[6]")).getAttribute("textContent");
			if(!actTlComments.equals(tlComments))
			{
				Configuration.logger.log(LogStatus.WARNING, "TL Comment mismatch, expected["+tlComments+"] but found ["+actTlComments+"]");
			}
			
			return true;
		}
		
		utils.save_ScreenshotToReport("search_Clarification");
		Configuration.logger.log(LogStatus.FAIL, "Pateient Account ["+ patientAccountNumber +"] not found at Clarification ");
		return false;
	}

	
	public boolean search_ClientEscalationRelease(String patientAccountNumber, String tlComments) 
	{
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		
		txtSearch_ClientEscalation_Release.clear();
		txtSearch_ClientEscalation_Release.sendKeys(patientAccountNumber);
		
		if(utils.isElementPresent(tblClientEscalation_Release, By.xpath(".//a[text() = '"+patientAccountNumber+"']")))
		{
			String actTlComments = tblClientEscalation_Release.findElement(By.xpath("//a[text() = '"+patientAccountNumber+"']/../following-sibling::td[6]")).getAttribute("textContent");
			if(!actTlComments.equals(tlComments))
			{
				Configuration.logger.log(LogStatus.WARNING, "TL Comment mismatch, expected["+tlComments+"] but found ["+actTlComments+"]");
			}
			
			return true;
		}
		
		utils.save_ScreenshotToReport("search_ClientEscalationRelease");
		Configuration.logger.log(LogStatus.FAIL, "Pateient Account ["+ patientAccountNumber +"] not found at ClientEscalation Release ");
		return false;
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
			
			case MEOB: return tblMEOB;
			case MEOB_PP_RESPONSE: return tblMEOB_PPResponse;
			case MEOB_FOLLOWUP: return tblMEOB_FollowUp;
			
			default:break;
		}
		return null;
	}
	
	private WebElement get_Search_Input(Constants.Queues queue) 
	{
		switch(queue)
		{
			case REGULAR: return txtSearch_Regular;
			//case PRIORITY: return 
			case CODING_RESPONSE: return txtSearch_Coding;
			case CREDENTIALING_RESPONSE: return txtSearch_Credentialing;
			case PRINT_AND_MAIL_RESPONSE: return txtSearch_PrintAndMail;
			case PAYMENT_RESPONSE: return txtSearch_Payment;
			case NEEDCALL_RESPONSE: return txtSearch_JrCallerResponse;
			case CLIENT_ESCALATION_RELEASE: return txtSearch_ClientEscalation_Release;
			case CLARIFICATION: return txtSearch_Clarification;
			case MEOB: return txtSearch_MEOB;
			case MEOB_PP_RESPONSE: return txtSearch_MEOBPPResponse;
			case MEOB_FOLLOWUP: return txtSearch_MEOBFallowup;
			
			default:break;
		}
		return null;
	}

	
	public HashMap<String, String> get_PendingWorkOrder() 
	{
		utils.wait_Until_InvisibilityOf_LoadingScreen();		
		HashMap<String, String> mapSummary = new HashMap<>();
		
		
		try 
		{
			//open work order summary 
			lnkWorkOderSummary.click();
			utils.wait_Until_InvisibilityOf_LoadingScreen();		
			
			
			if(utils.isElementPresent(messageHeader) && messageHeader.isDisplayed())
			{
				String header = messageHeader.getAttribute("textContent").toUpperCase();
				
			 	if(header.equals("ERROR") || header.equals("INFORMATION"))
				{
					Configuration.logger.log(LogStatus.INFO, "<b>" + messageDetails.getAttribute("textContent") + "</b>");
					return mapSummary;
				}
			}
			
			
			
			
			
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(div_WorkOrderSummaryModal));			
			utils.save_ScreenshotToReport("PendingWorkOrder");
			
					
						
			//get rows in table
			List<WebElement> summaryRows = div_WorkOrderSummaryModal.findElements(By.xpath(".//tbody/tr"));
						
			//loop through the rows and get cell values
			for (WebElement row : summaryRows) 
			{					
				List<WebElement> lstCells = row.findElements(By.xpath("./td"));				
				mapSummary.put(lstCells.get(0).getAttribute("textContent").trim(), lstCells.get(1).getAttribute("textContent").trim());
			}
			
			btn_Cancel_WorkOderSummary.click();
		} 
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.WARNING, "Method name: get_PendingWorkOrder<br>" + e.getMessage());
		}		
		return mapSummary;
	}





	public ArrayList<String> get_AllPractices() 
	{
		try 
		{
			return utils.get_Options(ddlPractices);
		}
		catch (Exception e) 
		{
			// TODO: handle exception
		}
		return null;
		
	}

	
}





/*public Transaction open_PatientAccount(Queues queue)
{ 	
	
	if(utils.isElementPresent(get_Queue_Table(queue), By.xpath(".//td[text() = 'No data available in table']")))
	{				
		return new Transaction(false, "<b>No data available in table</b>");
	}
	else
	{
		for(int index=1; index <= 10; index++)
		{
			WebElement linkAccount = get_Queue_Table(queue).findElement(By.xpath(".//tbody/tr["+ index +"]/td/a"));
			String account = linkAccount.getAttribute("textContent");
	 		
			if(utils.isAccountLocked(linkAccount) == true)
			{
				Configuration.logger.log(LogStatus.INFO, "<b>Account ["+ account +"] - This Account Is Locked By The Other User</b>");
				continue;
			}
			
			return new Transaction(true, "Claim opened");
		}
	}
	return null;
}	

public Transaction open_PatientAccount(Queues queue, int rowIndex)
{ 		
		WebElement linkAccount = get_Queue_Table(queue).findElement(By.xpath(".//tbody/tr["+ rowIndex+"]/td/a"));
		String account = linkAccount.getAttribute("textContent");
		
	if(utils.isAccountLocked(linkAccount) == true)
	{
		Configuration.logger.log(LogStatus.INFO, "<b>Account ["+ account +"] - This Account Is Locked By The Other User</b>");
		return new Transaction(false, "");
	}
	else
	{
		if(Configuration.driver.findElements(By.xpath("//table[@id='example']//a")).size() > 1)
		{
			objClaimTransaction = PageFactory.initElements(Configuration.driver, ClaimTransaction.class);
			objClaimTransaction.close_Claim_Transaction();
			return new Result(account, false, "");
		}
	}
		
	return new Transaction(true, "");		
}




public void verify_Allocation_Rule( List<String> rules, String user)
{
	objClaimTransaction = PageFactory.initElements(Configuration.driver, ClaimTransaction.class);		
	//PayerName Equal To Medicare WI Locality 99 New

	int tempIndex = 0;
	
	System.out.println(user);
	Configuration.logger.log(LogStatus.INFO, "User: " + user);
	Configuration.logger.log(LogStatus.INFO, "Allocation rules: " + rules.toString());
    
	
	
	int accountCount =0;// get_TotalAccounts(Constants.Queues.REGULAR);
	if(accountCount > 0) //check for all accounts
	{   			   			
			
		for(int i=1; i<=accountCount; i++)
		{			
			String accountNumber = Configuration.driver.findElement(By.xpath("//table[@id = 'example12']/tbody/tr["+ i +"]/td/a")).getText(); //change table id
			Configuration.logger.log(LogStatus.INFO, "Account Number: " + accountNumber);
			
			
			
			if(open_PatientAccount(Constants.Queues.REGULAR, i).getStatus())
			{   		
				ArrayList<String> UID_List = null;
				
				tempIndex = 0;   
				String rule = rules.get(0);
				
				 get first rule
				String column = getRuleColumn(rule);	
				String condition = getRuleCondition(rule);	
				String value = getRuleValue(rule);		
				//System.out.println(column + "," + condition + "," + value);
				
				
				switch (column)
				{
					case "PayerName":			UID_List = objClaimTransaction.verify_AllocationRule_Insurance(condition, value);
					break;

					case "Ins_Balance":			UID_List = objClaimTransaction.verify_AllocationRule_InsuranceBalance(condition, Float.parseFloat(value));
					break;
					
					case "Units_Charged":		UID_List = objClaimTransaction.verify_AllocationRule_UnitsCharged(condition, Float.parseFloat(value));
					break;
					
					case "Total_Charges":		UID_List = objClaimTransaction.verify_AllocationRule_Total_Charges(condition, Float.parseFloat(value));
					break;
						
					default:
					break;
				}
				
				
				
				if(UID_List.size() > 0) //mismatch encounters
				{
					ArrayList<String> UID_List_New = new ArrayList<String>();
					
					
					while(true) //check for other rules
					{
						tempIndex++; //next condition
						if(tempIndex < rules.size()) //when there are more rules to check
						{
							column = getRuleColumn(rules.get(tempIndex));	
				    		condition = getRuleCondition(rules.get(tempIndex));	
				    		value = getRuleValue(rules.get(tempIndex));
				    		
				    		
				    		switch (column)
							{
								case "PayerName":			UID_List_New =  objClaimTransaction.verify_AllocationRule_Insurance(condition, value, UID_List);
								break;

								case "Ins_Balance":			UID_List_New = objClaimTransaction.verify_AllocationRule_InsuranceBalance(condition, Float.parseFloat(value), UID_List);
								break;
									
								default:
								break;
							}
							
				    		
				    		if(UID_List.size() > 0)
				    		{
				    			UID_List = UID_List_New;
				    		}
				    		else if(!(UID_List.size() > 0))
				    		{
				    			break;
				    		}
							
						}
						else //no more rules to check
						{	    								
							System.out.println("Following UID do not match with any rules");
							System.out.println(UID_List.toString());
							
							Configuration.logger.log(LogStatus.INFO, "Following UID do not match with any alloted rules");
							Configuration.logger.log(LogStatus.INFO, UID_List.toString());
							//Configuration.logger.log(LogStatus.INFO, Integer.toString(UID_List.size())	);
							break;
						}
					}
				}
				
				objClaimTransaction.close_ClaimTransaction();
				utils.wait_Until_Invisibility_Of_LoadingScreen();
				}
		}
	}
	else
	{
		System.out.println("No accounts to work with!!");
		Configuration.logger.log(LogStatus.INFO, "No accounts to work with!!");
	}    		
      		
}

public String getRuleColumn(String rule)
{
	return rule.substring(0, rule.indexOf(" "));		
}	
public String getRuleCondition(String rule)
{
	int firstSplit = rule.indexOf(" ");
	int secondSplit = rule.indexOf(" ", firstSplit + 1);
	
	
	return rule.substring(firstSplit+1, secondSplit);		
}
public String getRuleValue(String rule)
{
	int firstSplit = rule.indexOf(" ");
	int secondSplit = rule.indexOf(" ", firstSplit + 1);					
	return rule.substring(secondSplit+1);		
}*/

