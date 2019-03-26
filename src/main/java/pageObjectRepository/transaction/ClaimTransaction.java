package pageObjectRepository.transaction;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import java.util.function.Function;
import java.util.stream.Collectors;
import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.Constants;
import configuration.WebDriverUtils;
import customDataType.Info;
import customDataType.Transaction;

public class ClaimTransaction
{

	WebDriverUtils utils = new WebDriverUtils();
	
	@FindBy(xpath = "//select[@ng-model = 'ddlClaimStatus']")
	private WebElement ddlClaimStatus;
	
	@FindBy(xpath = "//select[@ng-model = 'ddlActionCode']")
	private WebElement ddlActionCode;
	
	@FindBy(xpath = "//select[@ng-model = 'ddlCallType']")
	private WebElement ddlResolveType;
	
	@FindBy(id = "txtfollowupdays")
	private WebElement txtFollowupDays;
	
	@FindBy(xpath = "//textarea[@ng-model = 'Txtnotes']")
	private WebElement txtNotes;
	
	@FindBy(id = "btnSave")
	private WebElement btnSave;
	
	@FindBy(id = "btnCancel")
	private WebElement btnCancel;
	
	@FindBy(id = "btnTempSave")
	private WebElement btnTempSave;
	
	@FindBy(xpath = "//button[@id='btnTempSave']/following-sibling::button[@id='btnSave']")
	private WebElement btnTempSave_Submit;
	
	
	@FindBy(xpath = "//i[text() = 'help']")
	private WebElement icon_Clarifiacation;
	
	@FindBy(xpath = "//select[@ng-model = 'ddlReason']")
	private WebElement ddlReason_Clarification;

	@FindBy(id = "TxtComments")
	private WebElement txtComments_Clarification;
	
	@FindBy(xpath = "//div[@id = 'collapseOne_20']//button[@id = 'btnSave']")
	private WebElement btnSave_Clarification;
	
	@FindBy(xpath = "//div[@id = 'collapseOne_20']//button[text()= 'CANCEL']")
	private WebElement btnCancel_Clarification;
	
	
	@FindBy(xpath="//div[@class = 'sweet-alert hideSweetAlert']/h2")
	private WebElement messageHeader;
	
	@FindBy(xpath = "//div[@class = 'sweet-alert hideSweetAlert']/h2/following-sibling::p")
	private WebElement messageDetails;
	
	private String sBtnOK = "//div[@class = 'sweet-alert showSweetAlert visible']//button[text() = 'OK'";
	@FindBy(xpath = "//div[@class = 'sweet-alert showSweetAlert visible']//button[text() = 'OK']")
	private WebElement btnOK;
	
	@FindBy(xpath = "//input[@id='chkNeedCall']")
	private WebElement chkNeedCall;
	
	@FindBy(id = "txtPhoneno")
	private WebElement txtPhoneno;
	
	
	@FindBy(xpath = "//div[@id='headingTwo_1']/h4/a")
	private WebElement accordionHistory;
	
	@FindBy(xpath = "//a[text() = 'PAYMENT']")
	private WebElement tabPayment;
	
	@FindBy(xpath = "//a[text() = 'CLARIFICATION']")
	private WebElement tabClarification;
	
	
	
	
	/* DateTime Picker - FollowUp date	*/
	
	@FindBy(xpath = "//div[@class = 'dtp-actual-month p80']")
	private WebElement dtp_Month;
	
	@FindBy(xpath = "//a[@class = 'dtp-select-month-before']")
	private WebElement dtp_PreviousMonth;
	
	@FindBy(xpath = "//a[@class = 'dtp-select-month-after']")
	private WebElement dtp_NextMonth;
	
	
	
	@FindBy(xpath = "//div[@class = 'dtp-actual-year p80']")
	private WebElement dtp_Year;
	
	@FindBy(xpath = "//a[@class = 'dtp-select-year-before']")
	private WebElement dtp_PreviousYear;
	
	@FindBy(xpath = "//a[@class = 'dtp-select-year-after']")
	private WebElement dtp_NextYear;
	
	
	@FindBy(xpath = "//button[text() = 'OK']")
	private WebElement btn_DateOK;
	
	/* ********************************************************* */

	
	
	
	
	
	public WebElement getMessageHeader() 
	{
		return messageHeader;
	}

	public String getMessageDetails() 
	{
		return messageDetails.getAttribute("textContent");
	}
	
	
	
	@FindBy(id = "example")
	private WebElement tblEncounterList;
	
	
	
	
	public boolean verify_UID(String sUID) 
	{
		try 
		{
			tblEncounterList.findElement(By.xpath(".//tr//a[text() = '"+ sUID +"']"));
			return true;
		}
		catch (NoSuchElementException e) 
		{
			return false;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return false;
		}
	}
	
	
	public String get_TotalCharges() 
	{	
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		String totalCharges = Configuration.driver.findElement(By.xpath("//label[text() = 'Total Charged']/following-sibling::span[2]")).getAttribute("textContent").replace("$", "").replace(",", "");		

		return totalCharges;
		//return String.valueOf(Math.round(Float.parseFloat(totalCharges)));
	}	
	
	public String get_PatientAccountNumber() 
	{
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return Configuration.driver.findElement(By.xpath("//label[text() = 'Patient Acc No']/following-sibling::span[2]")).getAttribute("textContent");
	}
	
	public String get_EncounterID() 
	{
		return tblEncounterList.findElement(By.xpath("(.//tbody/tr/td[3])[1]")).getAttribute("textContent");		
	}
	
	public HashSet<String> get_EncounterList_From_ClaimTransaction() 
	{
		List<WebElement> listEnounters = Configuration.driver.findElements(By.xpath("//table[@id='example']//tbody/tr/td[3]"));
		HashSet<String> setEncounters = new HashSet<>();

		for (WebElement webElement : listEnounters) 
		{
			setEncounters.add(webElement.getText().trim());
		}
		return setEncounters;
	}
	
	public String get_UID() 
	{	
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return tblEncounterList.findElement(By.xpath(".//tr[1]//a")).getAttribute("textContent");
	}	

	public String get_InsBalance() 
	{	
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return tblEncounterList.findElement(By.xpath(".//tr[1]/td[7]")).getAttribute("textContent").replace("$", "").replace(",", "");
	}
	
	public String get_ProviderName() 
	{
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return Configuration.driver.findElement(By.xpath("//label[text() = 'Ren Provider Name']/following-sibling::span[2]")).getAttribute("textContent");
	}
	
	public String get_PayerName() 
	{
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return tblEncounterList.findElement(By.xpath(".//tr[1]/td[6]")).getAttribute("textContent");
	}
	public String get_DOSStart() 
	{
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return tblEncounterList.findElement(By.xpath(".//tr[1]/td[5]")).getAttribute("textContent");
	}
	
	public String get_DOSEnd() 
	{
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return Configuration.driver.findElement(By.xpath("//label[text() = 'DOS End']/following-sibling::span[2]")).getAttribute("textContent");
	}
	
	public int get_UIDCount() 
	{
		utils.wait_Until_InvisibilityOf_LoadingScreen();

		return tblEncounterList
				.findElements(By.xpath(".//tr/td[2]"))
				.size(); /* get uid count */
	}
	
	
	
	
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
			
			btn_DateOK.click();
		}		
		catch (Exception e) {
			Configuration.logger.log(LogStatus.WARNING, "Mehtod Name: select_Date<br>" + e.getMessage());
		}
	}
	
	
	/**
	 * Saves claim by taking claimStatus, actionCode, resolveType, notes and set followUpDate to next day
	 * @param claimStatus
	 * @param actionCode
	 * @param resolveType
	 * @param notes
	 * 
	 * @return object of Transaction class with status(true/ false) and description
	 */
	public Transaction submit_Claim(String sUID, String claimStatus, String actionCode, String resolveType, String notes)
	{
		try
		{	 		
	 		Date dt = new Date();
	 		Calendar c = Calendar.getInstance(); 
	 		c.setTime(dt); 
	 		c.add(Calendar.DATE, 1);
	 		dt = c.getTime();
	 		
	 		
	 		String sFollowUpDate = new SimpleDateFormat("MM/dd/yy").format(dt); // -- MM/dd/yyyy	
	 		return submit_Claim(sUID, claimStatus, actionCode, resolveType, notes, sFollowUpDate);	 		
		}
		catch (Exception e) 
		{
			return new Transaction(false, "Mehtod: submit_Claim<br>" + e.getMessage());
		}		
	}
	
	
	/**
	 * Saves claim by taking claimStatus, actionCode, resolveType, notes and followUpDate
	 * @param claimStatus
	 * @param actionCode
	 * @param resolveType
	 * @param notes
	 * @param followUpDate
	 * 
	 * @return object of Transaction class with status(true/ false) and description
	 */
	public Transaction submit_Claim(String sUID, String claimStatus, String actionCode, String resolveType, String notes, String followUpDate) // used in regular queue, check encounter
	{
		try
		{
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[@ng-model = 'ddlClaimStatus']")));
						
			try // check UID in encounter list table
			{
				//WebElement chkEncounter = Configuration.driver.findElement(By.xpath("//table[@id='example']//tbody/tr//input[@ng-model = 'checkAll']")); 
				WebElement chkEncounter = tblEncounterList.findElement(By.xpath(".//a[text() = '" + sUID + "']/../preceding-sibling::td/input")); 
				((JavascriptExecutor)Configuration.driver).executeScript("arguments[0].click();", chkEncounter);
			}
			catch (Exception e) 
			{
				//e.printStackTrace();
			}
	 		
	 		
	 		//	 fill input fields
	 		utils.wait(200);
			utils.select(ddlClaimStatus, claimStatus);
	 		utils.select(ddlActionCode, actionCode);
	 		utils.select(ddlResolveType, resolveType); 		
	 		
	 		/*txtFollowupDays.click();	 			
	 		select_Date(followUpDate);*/
	 		
	 		txtNotes.clear();
	 		txtNotes.sendKeys(notes);
	 		
	 		utils.wait_Until_InvisibilityOf_LoadingScreen();
		 	btnSave.click();
		 	
		 			 	
		 	
		 	
			/* ************ get time taken to load queue on submission *********************** */
		 	
		 	long startTime = System.currentTimeMillis();
		 	
		 	
		 	utils.wait(200);
		 	utils.save_ScreenshotToReport("SaveClaim");
		 	utils.wait_Until_InvisibilityOf_LoadingScreen();
		 	
			
			long endTime = System.currentTimeMillis();
			
			Configuration.logger.log(LogStatus.INFO, "<b>Time to load queue after submission: " + (endTime - startTime)/1000 + " second(s)</b>");
			System.out.println("Time to load queue after submission: " + (endTime - startTime)/1000 + " sec");
			
		 	
			/* ************ get time taken to load queue on submission *********************** */
		 	
		 	
		 	
		 	if(utils.isElementPresent(btnOK) )
		 	{
		 		if(btnOK.isDisplayed())
		 		{
		 			utils.save_ScreenshotToReport("SaveClaim_Error");
		 			btnOK.click();
		 		}
		 	}
		 	
		 	new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class = 'sweet-overlay']")));
		 	if(utils.isElementPresent(btnCancel) && btnCancel.isDisplayed())
		 	{		 		
		 		btnCancel.click();
		 	}
		 	
		 	
		 	
		 	String header = messageHeader.getAttribute("textContent").toUpperCase();
		 	//String msg = messageDetails.getAttribute("textContent");
		 	
		 	if(header.equals("SUCCESS"))
			{
				return new Transaction(true, "Account saved");
			}
		 	
		 	return new Transaction(false, "<b>" + messageDetails.getAttribute("textContent") + "</b>");
		}
		catch (Exception e) 
		{
		 	return new Transaction(false, "Mehtod: submit_Claim<br>" + e.getMessage());
		}	
	}

	
	/**
	 * Saves claim by taking claimStatus, actionCode, notes (used to submit response from respective login)
	 * @param claimStatus
	 * @param actionCode
	 * @param notes
	 * 
	 * @return object of Transaction class with status(true/ false) and description
	 */
	public boolean submit_Claim(String claimStatus, String actionCode, String notes)  // response no check box
	{
		try
		{
			
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[@ng-model = 'ddlClaimStatus']")));
			
			//No check box present
			
			/*WebElement chkEncounter = Configuration.driver.findElement(By.xpath("//table[@id='example']//tbody/tr[1]//input[@ng-model = 'chkAccounts']")); 
			((JavascriptExecutor)Configuration.driver).executeScript("arguments[0].click();", chkEncounter);*/
			
			
			utils.select(ddlClaimStatus, claimStatus);
	 		utils.select(ddlActionCode, actionCode);
	 		
	 		
	 		txtNotes.clear(); 
	 		txtNotes.sendKeys(notes);
		 	btnSave.click();
		 	
		 	utils.wait(200);
		 	utils.save_ScreenshotToReport("SaveResponse");
		 	utils.wait_Until_InvisibilityOf_LoadingScreen();
		 		 	
		 	
		 	if(utils.isElementPresent(By.xpath(sBtnOK)) )
		 	{
		 		if(btnOK.isDisplayed())
		 		{
		 			utils.save_ScreenshotToReport("SaveResponse_Error");
		 			btnOK.click();
		 		}
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
		 		return true;
			}
		 	
		 	else
			{
				Configuration.logger.log(LogStatus.FAIL, header + "_" + msg);
			}
		 	
		 	return false;
		}
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, "Class: ClaimTransaction, Mehtod: submit_Claim <br>" + e.getMessage());
			return false;
		}
				
		
	}
	
	
	/**
	 * Moves claim to Jr Caller queue (when NeedCall checkbox is selected) and verifies response message (SUCCESS/ ERROR)
	 * @param claimStatus	
	 * @param notes
	 * 
	 * @return object of Transaction class with status(true/ false) and description
	 */
	public Transaction submit_Claim(String claimStatus, String notes)  //used in regular, priority for NeedCall
	{
		try 
		{
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[@ng-model = 'ddlClaimStatus']")));			
			 	
			
			try // check first UID in encounter list table
			{
				WebElement chkEncounter = tblEncounterList.findElement(By.xpath(".//tbody/tr[1]//input[@ng-model = 'chkAccounts']")); 
				((JavascriptExecutor)Configuration.driver).executeScript("arguments[0].click();", chkEncounter);
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
	 		
	 	
	 		WebElement needCall = Configuration.driver.findElement(By.id("chkNeedCall"));
	 		JavascriptExecutor executor = (JavascriptExecutor)Configuration.driver;
	 		executor.executeScript("arguments[0].click();", needCall);
	 		
	 		utils.wait(500);
	 				
	 		utils.select(ddlClaimStatus, claimStatus);
	 		txtNotes.sendKeys(notes);
	 		
	 		
	 		utils.wait_Until_InvisibilityOf_LoadingScreen();
		 	btnSave.click();
		 	utils.wait(100);
		 	
		 	
		 	utils.save_ScreenshotToReport("SaveClaimToNeedCall");
		 	utils.wait_Until_InvisibilityOf_LoadingScreen();
		 	
		 	
		 	if(utils.isElementPresent(By.xpath(sBtnOK)) )
		 	{
		 		if(btnOK.isDisplayed())
		 		{
		 			utils.save_ScreenshotToReport("SaveClaimToNeedCall_Error");
		 			btnOK.click();
		 		}
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
				return new Transaction(true, "Account saved");
			}
		 	
		 	return new Transaction(false, "<b>" + header + "_" + msg + "</b>");
		} 
		catch (Exception e) 
		{		
			return new Transaction(false, "Class: ClaimTransaction, Mehtod: submit_Claim" + e.getMessage());
		}
		
	}

	
	/**
	 * Saves claim by taking claimStatus, actionCode, resolve type, notes (used to submit response from analyst inbox)
	 * @param claimStatus
	 * @param actionCode
	 * @param resolveType
	 * @param notes
	 * @return 
	 */
	public Transaction submit_Claim(String claimStatus, String actionCode, String resolveType, String notes) // in analyst inbox, to submit response
	{
		try 
		{
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[@ng-model = 'ddlClaimStatus']")));
			
	 		
	 		//	 fill input fields
	 		utils.wait(200);
			utils.select(ddlClaimStatus, claimStatus);
	 		utils.select(ddlActionCode, actionCode);
	 		utils.select(ddlResolveType, resolveType); 		
	 	
	 		txtNotes.clear();
	 		txtNotes.sendKeys(notes);
	 		btnSave.click();
		 	utils.wait(500);
		 			 	
		 	
		 	
			/* ************ get time taken to load queue on submission *********************** */
		 	
		 	long startTime = System.currentTimeMillis();
		 	
		 	
		 	utils.save_ScreenshotToReport("SaveClaim");
		 	utils.wait_Until_InvisibilityOf_LoadingScreen();
		 	
			
			long endTime = System.currentTimeMillis();
			
			Configuration.logger.log(LogStatus.INFO, "<b>Time to load queue after submission: " + (endTime - startTime)/1000 + " second(s)</b>");
			System.out.println("Time to load queue after submission: " + (endTime - startTime)/1000 + " sec");
			
		 	
			/* ************ get time taken to load queue on submission *********************** */
		 	
		 	
		 	
		 	if(utils.isElementPresent(btnOK) )
		 	{
		 		if(btnOK.isDisplayed())
		 		{
		 			utils.save_ScreenshotToReport("SaveClaim_Error");
		 			btnOK.click();
		 		}
		 	}
		 	
		 	new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class = 'sweet-overlay']")));
		 	if(utils.isElementPresent(btnCancel) && btnCancel.isDisplayed())
		 	{		 		
		 		btnCancel.click();
		 	}
		 	
		 	
		 	
		 	String header = messageHeader.getAttribute("textContent").toUpperCase();
		 	//String msg = messageDetails.getAttribute("textContent");
		 	
		 	if(header.equals("SUCCESS"))
			{
				return new Transaction(true, "");
			}
		 	
		 	return new Transaction(false, "<b>" + messageDetails.getAttribute("textContent") + "</b>");
		}
		catch (Exception e) 
		{
		 	return new Transaction(false, "Mehtod: submit_Claim<br>" + e.getMessage());
		}
	}

	
	
	
	/**
	 * Moves account to Temp Save queue and verifies response message (SUCCESS/ ERROR)
	 * @param claimStatus
	 * @param actionCode
	 * @param resolveType
	 * @param notes
	 * @return 
	 */
	public Info tempSave(String claimStatus, String actionCode, String resolveType, String notes,  boolean selectAllEncounters) 
	{
		try 
		{
			//wait for client dropdown to appear
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[@ng-model = 'ddlClaimStatus']")));
			
						
			if (selectAllEncounters) 
			{
				WebElement chkEncounter = tblEncounterList.findElement(By.xpath("//input[@id = 'checkAll']"));
				((JavascriptExecutor) Configuration.driver).executeScript("arguments[0].click();", chkEncounter);
			}
			else
			{
				//	select first encounter
				WebElement chkEncounter = Configuration.driver.findElement(By.xpath("//table[@id='example']//tbody/tr[1]//input[@ng-model = 'chkAccounts']")); 
				((JavascriptExecutor)Configuration.driver).executeScript("arguments[0].click();", chkEncounter);
				
			}
			
			
	 		
			// select dropdown values and tempsave
			utils.select(ddlClaimStatus, claimStatus);
	 		utils.select(ddlActionCode, actionCode);
	 		utils.select(ddlResolveType, resolveType);	 		
	 		txtNotes.clear(); 
	 		txtNotes.sendKeys(notes);
	 		
	 		utils.wait(100);
	 		btnTempSave.click();
		 
	 		utils.save_ScreenshotToReport("TempSave");	
	 		utils.wait_Until_InvisibilityOf_LoadingScreen();

	 		
	 		if(utils.isElementPresent(By.xpath(sBtnOK)) )
		 	{
		 		if(btnOK.isDisplayed())
		 		{
		 			utils.save_ScreenshotToReport("TempSave_Error");
		 			btnOK.click();
		 		}
		 	}
	 		
	 		if(utils.isElementPresent(btnCancel) && btnCancel.isDisplayed())
		 	{
		 		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class = 'sweet-overlay']")));
		 		btnCancel.click();
		 	}
	 		
	 		
	 		utils.wait_Until_InvisibilityOf_LoadingScreen();
	 		
	 		String header = messageHeader.getAttribute("textContent").toUpperCase();
		 	String msg = messageDetails.getAttribute("textContent");
		 	
		 	if(header.toUpperCase().equals("SUCCESS"))
			{
		 		return new Info(true, "<b>Account moved to TempSave queue</b>");
			}
		 	
		 	else
			{				
				if(utils.isElementPresent(btnCancel) && btnCancel.isDisplayed())
			 	{
			 		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(btnCancel)).click();
			 	}				
			}
		 	
		 	return new Info(false, msg);
		}
		catch (Exception e) 
		{
			return new Info(false,  "Class: ClaimTransaction, Method: tempSave <br>" + e.getMessage());
		}
	}

	
	/**
	 * Verifies whether Claim Status, Action Code and Notes are reflected to Temp Save queue 
	 * @param claimStatus
	 * @param actionCode
	 * @param resolveType
	 * @param notes
	 * @return 
	 */
	public void verify_TempSaveTransactionInputs(String claimStatus, String actionCode, String resolveType, String notes) 
	{
		try
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(ddlClaimStatus));
			
			utils.wait(2000);
			utils.save_ScreenshotToReport("TempSaveTransactionInputs");
			
			
			// verify
			String actActionCode = new Select(ddlActionCode).getFirstSelectedOption().getText();
			String expActionCode = actionCode.trim();  // removed toLowerCase

			String actClaimStatus = new Select(ddlClaimStatus).getFirstSelectedOption().getText();
			String expClaimStatus = claimStatus.trim();

			String actNotes = txtNotes.getAttribute("value");
			String expNotes = notes.trim();

			StringBuffer sError = new StringBuffer();

			
			if (!actActionCode.equals(expActionCode)) 
			{
				sError.append("Action Code, expected [" + expActionCode + "] but found [" + actActionCode+ "]<br>");
			}
			if (!actClaimStatus.equals(expClaimStatus)) 
			{
				sError.append("Claim Status, expected [" + expClaimStatus + "] but found ["+ actClaimStatus + "]<br>");
			}
			if (!actNotes.equals(expNotes)) 
			{
				sError.append("Notes, expected [" + expNotes + "] but found [" + actNotes + "]<br>");
			}

			if(sError.length() > 0)
				Configuration.logger.log(LogStatus.FAIL, "<b>Following values do not match in claim transaction under Temp Save queue</b> <br><br>" + sError.toString());
			else
				Configuration.logger.log(LogStatus.INFO, "<b>Transaction inputs are retained in claim transaction windox</b>");
			
			
			
			utils.wait_Until_InvisibilityOf_LoadingScreen();
		}
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.ERROR, "Class: ClaimTransaction, Method: verify_TempSave_Transaction_Inputs <br>" + e.getMessage());
		}
	}
	
	
	
	public ArrayList<String> verify_AllocationRule_Insurance(String condition, String value)
	{
		int encounterCount = tblEncounterList.findElements(By.xpath("./tbody/tr/td[2]/a")).size();
		ArrayList<String> UID_Mismatch_List = new ArrayList<String>();
		
		for(int i=1; i<=encounterCount;i++)
		{
			String UID = tblEncounterList.findElement(By.xpath("./tbody/tr["+i+"]/td[2]")).getAttribute("textContent");
			String payer = tblEncounterList.findElement(By.xpath("./tbody/tr["+i+"]/td[6]")).getAttribute("textContent");
			
			switch (condition) 
			{
				case "=":				if(!payer.equals(value))
										{
											UID_Mismatch_List.add(UID);
										}
										break;
						
				case "LIKE":			if((value.charAt(0) == '%') && (value.charAt(value.length()-1) == '%')) //contains
										{
											value = value.replaceAll("%", "");
											if(!payer.contains(value))
												UID_Mismatch_List.add(UID);
										}
										else if(value.charAt(value.length()-1) == '%')
										{
											value = value.replace("%", "");
											if(!payer.startsWith(value))
												UID_Mismatch_List.add(UID);	
										}
										break;
						
				case "IN":				List<String> values = Arrays.asList((value.substring(1,value.length()-1)).split(","));
										if(!values.contains(payer)) 
										{
											UID_Mismatch_List.add(UID);
										}
										break;
										
				case "NOT EQUAL TO":	break;
					
					
				default:				break;
			}
		}
		
		return UID_Mismatch_List;
	}

	public ArrayList<String> verify_AllocationRule_Insurance(String condition, String value, ArrayList<String> UID_List) 
	{		
		ArrayList<String> UID_Mismatch_List = new ArrayList<String>();
		String payer;
		
		for(int i=1; i<=UID_List.size();i++)
		{
			payer = tblEncounterList.findElement(By.xpath("./tbody/tr["+i+"]/td[6]")).getAttribute("textContent");
			
			switch (condition) 
			{
				case "=":				if(!payer.equals(value))
										{
											UID_Mismatch_List.add(UID_List .get(i));	
										}
										break;
						
				case "LIKE":			if((value.charAt(0) == '%') && (value.charAt(value.length()-1) == '%')) //contains
										{
											value = value.replaceAll("%", "");
											if(!payer.contains(value))
												UID_Mismatch_List.add(UID_List .get(i));
										}
										else if(value.charAt(value.length()-1) == '%')
										{
											value = value.replace("%", "");
											if(!payer.startsWith(value))
												UID_Mismatch_List.add(UID_List .get(i));
										}
										break;
						
				case "IN":				List<String> values = Arrays.asList((value.substring(1,value.length()-1)).split(","));
										if(!values.contains(payer)) 
										{
											UID_Mismatch_List.add(UID_List .get(i));
										}
										break;
										
				case "NOT EQUAL TO":	break;
					
					
				default:				break;
			}		
		}	
		
		return UID_Mismatch_List;
	}
	
	public ArrayList<String> verify_AllocationRule_InsuranceBalance(String condition, float value)
	{
		int encounterCount = tblEncounterList.findElements(By.xpath("./tbody/tr/td[2]/a")).size();
		ArrayList<String> UID_Mismatch_List = new ArrayList<String>();
		String UID, insBalance;
		
			
		
		for(int i=1; i<=encounterCount;i++)
		{
			 UID = tblEncounterList.findElement(By.xpath("./tbody/tr["+i+"]/td[2]")).getAttribute("textContent");
			 insBalance = tblEncounterList.findElement(By.xpath("./tbody/tr["+i+"]/td[7]")).getAttribute("textContent");
			 
			 
			switch (condition) 
			{
				case "=":	if(Float.parseFloat(insBalance.replace("$", "")) != (value))
							{
								UID_Mismatch_List.add(UID);
							}
							break;
										
				case "<>":	if(Float.parseFloat(insBalance.replace("$", "")) == (value))
							{
								UID_Mismatch_List.add(UID);					
							}
							break;
				
				case ">":	if(Float.parseFloat(insBalance.replace("$", "")) <= (value))
							{
								UID_Mismatch_List.add(UID);					
							}
							break;
				
				case "<":	if(Float.parseFloat(insBalance.replace("$", "")) >= (value))
							{
								UID_Mismatch_List.add(UID);					
							}
							break;
				
				case ">=":	if(Float.parseFloat(insBalance.replace("$", "")) < (value))
							{
								UID_Mismatch_List.add(UID);					
							}
							break;
				
				case "<=":	if(Float.parseFloat(insBalance.replace("$", "")) > (value))
							{
								UID_Mismatch_List.add(UID);					
							}
							break;
							
				default:    break;
			}
		}
		
		return UID_Mismatch_List;
	}

	public ArrayList<String> verify_AllocationRule_InsuranceBalance(String condition, float value, ArrayList<String> UID_List) 
	{		
		ArrayList<String> UID_Mismatch_List = new ArrayList<String>();
		String insBalance;
		
		for(int i=1; i<=UID_List.size();i++)
		{			 
			 insBalance = tblEncounterList.findElement(By.xpath("./tbody/tr["+i+"]/td[7]")).getAttribute("textContent");
			 
			 
			switch (condition) 
			{
				case "=":	if(Float.parseFloat(insBalance.replace("$", "")) != (value))
							{
								UID_Mismatch_List.add(UID_List .get(i));
							}
							break;
										
				case "<>":	if(Float.parseFloat(insBalance.replace("$", "")) == (value))
							{
								UID_Mismatch_List.add(UID_List .get(i));					
							}
							break;
				
				case ">":	if(Float.parseFloat(insBalance.replace("$", "")) <= (value))
							{
								UID_Mismatch_List.add(UID_List .get(i));					
							}
							break;
				
				case "<":	if(Float.parseFloat(insBalance.replace("$", "")) >= (value))
							{
								UID_Mismatch_List.add(UID_List .get(i));					
							}
							break;
				
				case ">=":	if(Float.parseFloat(insBalance.replace("$", "")) < (value))
							{
								UID_Mismatch_List.add(UID_List .get(i));					
							}
							break;
				
				case "<=":	if(Float.parseFloat(insBalance.replace("$", "")) > (value))
							{
								UID_Mismatch_List.add(UID_List .get(i));					
							}
							break;
							
				default:    break;
			}
		}
		
		return UID_Mismatch_List;
		
		
	}
	
	
	public ArrayList<String> verify_AllocationRule_UnitsCharged(String condition, float value) 
	{
		try 
		{
			int uidCount = tblEncounterList.findElements(By.xpath("./tbody/tr/td[2]/a")).size();
			ArrayList<String> UID_Mismatch_List = new ArrayList<String>();
			String UID, unitsCharged;
			
			
			for(int i=1; i<=uidCount;i++)
			{
				//find uid
				tblEncounterList.findElement(By.xpath("./tbody/tr["+i+"]/td[2]")).click();
				utils.wait_Until_InvisibilityOf_LoadingScreen();
				
				UID = tblEncounterList.findElement(By.xpath("./tbody/tr["+i+"]/td[2]")).getAttribute("textContent");
				unitsCharged = Configuration.driver.findElement(By.xpath("//label[text() = 'Units Charged']/following-sibling::span[2]")).getAttribute("textContent");
				
				System.out.println(UID + " ," + unitsCharged);
				
				switch (condition) 
				{
					case "=":	if(Float.parseFloat(unitsCharged.replace("$", "")) != (value))
								{
									UID_Mismatch_List.add(UID_Mismatch_List .get(i));
								}
								break;
											
					case "<>":	if(Float.parseFloat(unitsCharged.replace("$", "")) == (value))
								{
									UID_Mismatch_List.add(UID_Mismatch_List .get(i));					
								}
								break;
					
					case ">":	if(Float.parseFloat(unitsCharged.replace("$", "")) <= (value))
								{
									UID_Mismatch_List.add(UID_Mismatch_List .get(i));					
								}
								break;
					
					case "<":	if(Float.parseFloat(unitsCharged.replace("$", "")) >= (value))
								{
									UID_Mismatch_List.add(UID_Mismatch_List .get(i));					
								}
								break;
					
					case ">=":	if(Float.parseFloat(unitsCharged.replace("$", "")) < (value))
								{
									UID_Mismatch_List.add(UID_Mismatch_List .get(i));					
								}
								break;
					
					case "<=":	if(Float.parseFloat(unitsCharged.replace("$", "")) > (value))
								{
									UID_Mismatch_List.add(UID_Mismatch_List .get(i));					
								}
								break;
								
					default:    break;
				}
				
			}
			return UID_Mismatch_List;
			
		}
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, "Class: ClaimnTransaction, Mehtod: verify_AllocationRule_UnitsCharged <br>" + e.getMessage());
		}
		return null;
	}
	
	public ArrayList<String> verify_AllocationRule_Total_Charges(String condition, float parseFloat) 
	{
		/*ArrayList<String> UID_Mismatch_List = new ArrayList<String>();
		String UID, totalCharges;
		
			
		
		//find uid
		UID = Configuration.driver.findElement(By.xpath("//table[@id='example']/tbody/tr["+i+"]/td[2]")).getAttribute("textContent");
		totalCharges = Configuration.driver.findElement(By.xpath("//label[text() = 'Units Charged']/following-sibling::span[2]")).getAttribute("textContent");*/
			 
		
		return null;
		
	}
	
	
	
	
	
	
	public void close_ClaimTransaction()
	{
		try
		{
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(btnCancel)).click();			
		}
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, "Mehtod: close_ClaimTransaction<br>" + e.getMessage());
		}
	}
	
	public void verify_InsBalance(String pateintAccountNumber)
	{
		AnalystInbox objInbox = PageFactory.initElements(Configuration.driver, AnalystInbox.class);
		
		float sum_Ins_Balance = get_Sum_Ins_Balance();
		float ins_Balance = objInbox.get_Pateient_InsBalace(Constants.Queues.REGULAR, pateintAccountNumber);
		
		if(sum_Ins_Balance != ins_Balance)
		{
			Configuration.logger.log(LogStatus.FAIL,  "Insurance Balance do not verify <br> expected ["+ ins_Balance +"] but sum equals ["+ sum_Ins_Balance +"] for Patient_Account ["+ pateintAccountNumber +"]");
		}
		else
		{
			Configuration.logger.log(LogStatus.INFO,  "<b>Insurance Balance verified</b>");
		}
	}
	
	public HashMap<String, Long> verify_DOS(String accountNumber, String category) 
	{
		HashMap<String, Long> dos_List = new HashMap<String, Long>();
		try 
		{				
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			Date today = sdf.parse(sdf.format(new Date()));			
			int mismatchCount = 0;
			
			int encounterCount = tblEncounterList.findElements(By.xpath("./tbody/tr/td[2]/a")).size();
			
			for(int i=1; i<=encounterCount;i++)
			{
				String encounter = tblEncounterList.findElement(By.xpath("./tbody/tr["+i+"]/td[3]")).getAttribute("textContent");
				String dos = tblEncounterList.findElement(By.xpath("./tbody/tr["+i+"]/td[5]")).getAttribute("textContent");						
				Date dosDate = sdf.parse(dos);			
				
				Configuration.logger.log(LogStatus.INFO, "Account Number: " + accountNumber + "; DOS: " + dos);
				
				long diff = TimeUnit.DAYS.convert(today.getTime() - dosDate.getTime(), TimeUnit.MILLISECONDS);					
								
			    if(diff <120 && category.equals("120+"))
			    {
			    	Configuration.logger.log(LogStatus.FAIL, "Encounter_ID: " + encounter + " Aging: " + diff + "days "); //status: DOS is less than 120
			    	mismatchCount++;
			    }
			    else if((diff<91 || diff>120 ) && category.equals("91-120"))
			    {
			    	Configuration.logger.log(LogStatus.FAIL, "Encounter_ID: " + encounter + " Aging: " + diff + "days "); 
			    	mismatchCount++;
			    }
			    else if((diff<61 || diff>90 ) && category.equals("61-90"))
			    {
			    	Configuration.logger.log(LogStatus.FAIL, "Encounter_ID: " + encounter + " Aging: " + diff + "days ");
			    	mismatchCount++;
			    }
			    else if((diff<31 || diff>60 ) && category.equals("31-60"))
			    {
			    	Configuration.logger.log(LogStatus.FAIL, "Encounter_ID" + encounter + " Aging: " + diff + "days ");
			    	mismatchCount++;
			    }
			    else if((diff<0 || diff>30 ) && category.equals("0-30"))
			    {
			    	Configuration.logger.log(LogStatus.FAIL, "Encounter_ID: " + encounter + " Aging: " + diff + "days ");
			    	mismatchCount++;
			    }				
			}
			if(mismatchCount == 0)
			{
				Configuration.logger.log(LogStatus.INFO, "<b>DOS verified [" + category + "]</b>");
			}
		} 
		catch (Exception e) 
		{			
			e.printStackTrace();
		}
		return dos_List;
	}

	

	/*public Map<String, Long> get_EncounterList_From_ClaimTransaction() 
	{
		List<WebElement> listEnounters = tblEncounterList.findElements(By.xpath(".//tbody/tr/td[3]")); 	
		List<String> listStringEncounters = new ArrayList<>();
						
		for (WebElement webElement : listEnounters) 
		{
			listStringEncounters.add(webElement.getText());
		}
		
		 Map<String, Long> result =	listStringEncounters.stream()
				 .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		
		 //Configuration.logger.log(LogStatus.INFO, "Encounter list - " + result);
		 
		return result;
	}*/
	
	
	

	public float get_Sum_Ins_Balance()
	{
		int encounterCount = tblEncounterList.findElements(By.xpath("./tbody/tr/td[2]")).size();		//encounter table
		HashSet<String> insBalance = new HashSet<String>();
		
		float sumIns_Balance = 0;
		for(int i=1; i<=encounterCount;i++)
		{
			String encounter = tblEncounterList.findElement(By.xpath("./tbody/tr["+i+"]/td[3]")).getAttribute("textContent");
			String balance = tblEncounterList.findElement(By.xpath("./tbody/tr["+i+"]/td[7]")).getAttribute("textContent");
								
			if(!insBalance.contains(encounter))
			{
				try 
				{	
					//System.out.println(encounter + ", " + balance );
					
					insBalance.add(encounter);
					sumIns_Balance += Float.parseFloat(balance.replace("$", "").replace(",", ""));
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}			
		}
		
		return (float) Math.round(sumIns_Balance * 100) / 100;
	}


	/**
	 * Returns true when Clarification submitted successfully 
	 * @param reason
	 * @param comments
	 * @return boolean value
	 */
	public boolean save_Clarification(String reason, String comments) 
	{
		try
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(icon_Clarifiacation)).click();
			
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(ddlReason_Clarification));
			utils.select(ddlReason_Clarification, reason);
			txtComments_Clarification.sendKeys(comments);
			btnSave_Clarification.click();
			utils.wait(200);
			
			utils.save_ScreenshotToReport("SaveClarification");
			
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			if(utils.isElementPresent(btnCancel) && btnCancel.isDisplayed())
		 	{
		 		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class = 'sweet-overlay']")));
		 		btnCancel.click();
		 	}
			
			 if(messageHeader.getAttribute("textContent").toUpperCase().equals("SUCCESS"))
			{
				return true;
			}
			 else 
			{				
				Configuration.logger.log(LogStatus.ERROR, messageHeader.getAttribute("textContent") + "_" + messageDetails.getAttribute("textContent"));
				btnCancel_Clarification.click();
			}
		}
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, "Class: ClaimTransaction, Mehtod: save_Clarification<br>" + e.getMessage());
		}
		return false;
	}

	public void getPaymentInfo() 
	{
		//find elements		
	}

	public boolean hasPayment()
	{
		try 
		{
			accordionHistory.click();			
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(tabPayment)).click();
			
			if(Configuration.driver.findElements(By.xpath("//div[@id = 'history_payment']//table/tbody/tr")).size() >= 0)
	        {
	        	return true;
	        }	
		} 
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, "Mehtod: hasPayment<br>" + e.getMessage());
		}
		finally 
		{
			tabPayment.click();
		}
		
		
		return false;
		
		
		
       
	}

	public SoftAssert verify_TransactionHistory(LinkedList<HashMap<String, String>> history) 
	{
		try 
		{
			SoftAssert s = new SoftAssert();
		
			accordionHistory.click();
			utils.wait(200);
			utils.scrollWindow(btnSave);
			utils.wait(200);
			
			utils.save_ScreenshotToReport("TransactionHistory");
			
			for(int i=0; i<history.size();i++)
			{
				HashMap<String, String> value = history.get(i);			
				//System.out.println("Row - " + (i+1) );
				
				String actQueue = Configuration.driver.findElement(By.xpath("//div[@id='history_trans']//tbody/tr["+(i+1)+"]/td[2]")).getAttribute("textContent").toLowerCase().trim();			
				String expQueue = value.get("queue").toLowerCase().trim();
				
				String actWorkedBy = Configuration.driver.findElement(By.xpath("//div[@id='history_trans']//tbody/tr["+(i+1)+"]/td[3]")).getAttribute("textContent").toLowerCase().trim();			
				String expWorkedBy = value.get("workedBy").toLowerCase().trim();
				
				String actActionCode = Configuration.driver.findElement(By.xpath("//div[@id='history_trans']//tbody/tr["+(i+1)+"]/td[4]")).getAttribute("textContent").toLowerCase().trim();			
				String expActionCode = value.get("action_code").toLowerCase().trim();
				
				String actClaimStatus = Configuration.driver.findElement(By.xpath("//div[@id='history_trans']//tbody/tr["+(i+1)+"]/td[5]")).getAttribute("textContent").toLowerCase().trim();			
				String expClaimStatus = value.get("claim_status").toLowerCase().trim();
				
				String actNotes = Configuration.driver.findElement(By.xpath("//div[@id='history_trans']//tbody/tr["+(i+1)+"]/td[7]")).getAttribute("textContent").toLowerCase().trim();			
				String expNotes = value.get("note").toLowerCase().trim();
				
				s.assertEquals(actQueue, expQueue, "</br>Row["+(i+1)+"], Queue mismatch, ");				
				s.assertEquals(actWorkedBy, expWorkedBy, "</br>Row["+(i+1)+"] WorkedBy mismatch, ");
				s.assertEquals(actActionCode, expActionCode, "</br>Row["+(i+1)+"] Action Code mismatch,");
				s.assertEquals(actClaimStatus, expClaimStatus, "</br>Row["+(i+1)+"] Claim Status mismatch, ");
				s.assertEquals(actNotes, expNotes, "</br>Row["+(i+1)+"] Notes mismatch, ");
				
				//s.assertAll();
			}
			
			accordionHistory.click();		
			utils.wait(500);
			return s;
			
			/*if(btnCancel.isDisplayed())			
				btnCancel.click();*/
			
			
		} 
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, "Mehtod: verify_TransactionHistory<br>" + e.getMessage());
			return null;
		}
		
		
	}

	public void verify_ClarificationHistory(String sReason, String sComments, String sPostedBy, String sClarificatioDetails, String sClarifiedBy) 
	{
		try 
		{
			accordionHistory.click();
			utils.wait(200);
			
			utils.scrollWindow(btnSave);
			utils.wait(200);
			
			
			tabClarification.click();
			utils.wait(1000);
			
			utils.save_ScreenshotToReport("ClarificationHistory");
			
			
			String sActReason = Configuration.driver.findElement(By.xpath(".//*[@id='history_Clarification']//td[2]")).getText();
			String sActComments = Configuration.driver.findElement(By.xpath(".//*[@id='history_Clarification']//td[3]")).getText();
			String sActPostedBy = Configuration.driver.findElement(By.xpath(".//*[@id='history_Clarification']//td[4]")).getText();
			String sActPostedOn = Configuration.driver.findElement(By.xpath(".//*[@id='history_Clarification']//td[5]")).getText();
			
			String sActClarificationsDetails = Configuration.driver.findElement(By.xpath(".//*[@id='history_Clarification']//td[6]")).getText();
			String sActClarifiedBy = Configuration.driver.findElement(By.xpath(".//*[@id='history_Clarification']//td[7]")).getText();			
			String sActClarifiedOn = Configuration.driver.findElement(By.xpath(".//*[@id='history_Clarification']//td[8]")).getText();
			
			StringBuilder strError = new StringBuilder();
			
			strError.append((sActReason.equals(sReason)) ? "" : "<br>Reason mismatch, expected ["+ sReason +"] but found ["+ sActReason +"]");
			strError.append((sActComments.equals(sComments)) ? "" : "<br>Comments mismatch, expected ["+ sComments +"] but found ["+ sActComments +"]");
			strError.append((sActPostedBy.equals(sPostedBy)) ? "" : "<br>PostedBy mismatch, expected ["+ sPostedBy +"] but found ["+ sActPostedBy +"]");
			strError.append((sActPostedOn.equals(utils.getToday())) ? "" : "<br>PostedOn mismatch, expected ["+ utils.getToday() +"] but found ["+ sActPostedOn +"]");
			strError.append((sActClarificationsDetails.equals(sClarificatioDetails)) ? "" : "<br>Clarifications Details mismatch, expected ["+ sClarificatioDetails +"] but found ["+ sActClarificationsDetails +"]");
			strError.append((sActClarifiedBy.equals(sClarifiedBy)) ? "" : "<br>Reason mismatch, expected ["+ sClarifiedBy +"] but found ["+ sActClarifiedBy +"]");
			strError.append((sActClarifiedOn.equals(utils.getToday())) ? "" : "<br>Comments mismatch, expected ["+ utils.getToday() +"] but found ["+ sActClarifiedOn +"]");
			
		
			if(strError.length() > 0)
				Configuration.logger.log(LogStatus.FAIL, strError.toString());
			
			else
				Configuration.logger.log(LogStatus.INFO, "<b>Clarification details verified</b>");
			
			
			accordionHistory.click();		
			utils.wait(500);
			
		}
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, "Mehtod: verify_ClarificationComments<br>" + e.getMessage());
		}
		
	}

	public void verify_TempSaveHistory(String queue, String workedBy, String actionCode, String claimStatus, String notes)
	{
		try
		{			
			accordionHistory.click();
			utils.wait(200);
			
			utils.scrollWindow(btnSave);
			utils.wait(200);
			
			utils.save_ScreenshotToReport("TempSaveHistory");
			
			
				
			List<WebElement> lastRow = Configuration.driver.findElements(By.xpath("(//div[@id='history_trans']//table/tbody/tr)[last()]//td"));			
	
			String sActDate = lastRow.get(0).getText();
			String sActQueue = lastRow.get(1).getText();
			String sActWorkedBy = lastRow.get(2).getText();
			String sActActionCode = lastRow.get(3).getText();
			String sActClaimStatus = lastRow.get(4).getText();
			String sActNotes = lastRow.get(6).getText();
			
			
			StringBuilder strError = new StringBuilder();
			
			strError.append((sActDate.equals(utils.getToday())) ? "" : "<br>Date mismatch, expected ["+ utils.getToday() +"] but found ["+ sActDate +"]");
			strError.append((sActQueue.equals(queue)) ? "" : "<br>Queue mismatch, expected ["+ queue +"] but found ["+ sActQueue +"]");
			strError.append((sActWorkedBy.equals(workedBy)) ? "" : "<br>WorkedBy mismatch, expected ["+ workedBy +"] but found ["+ sActWorkedBy +"]");
			strError.append((sActActionCode.equals(actionCode)) ? "" : "<br>Action Code mismatch, expected ["+ actionCode +"] but found ["+ sActActionCode +"]");
			strError.append((sActClaimStatus.equals(claimStatus)) ? "" : "<br>Claim Status Details mismatch, expected ["+ claimStatus +"] but found ["+ sActClaimStatus +"]");
			strError.append((sActNotes.equals(notes)) ? "" : "<br>Notes mismatch, expected ["+ notes +"] but found ["+ sActNotes +"]");
			
			
			if(strError.length() > 0)
				Configuration.logger.log(LogStatus.FAIL, strError.toString());
			
			else
				Configuration.logger.log(LogStatus.INFO, "<b>TempSave details verified in History</b>");
				
			
			accordionHistory.click();		
			utils.wait(500);
			
		}
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, "Mehtod: verify_ClarificationComments<br>" + e.getMessage());
		}
	}

	

	

	

	

	
	

	

	

	

}
