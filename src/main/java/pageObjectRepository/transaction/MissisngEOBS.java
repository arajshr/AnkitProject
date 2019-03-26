package pageObjectRepository.transaction;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.WebDriverUtils;
import customDataType.Info;

public class MissisngEOBS extends Configuration 
{
	
	WebDriverUtils utils=new WebDriverUtils();

	
	@FindBy(xpath="//div[@class = 'sweet-alert hideSweetAlert']/h2")
	private WebElement messageHeader;
	
	@FindBy(xpath = "//div[@class = 'sweet-alert hideSweetAlert']/h2/following-sibling::p")
	private WebElement messageDetails;
	
	
	
	@FindBy(xpath = "//select[@ng-model='ddlPractices']")
	private WebElement ddlPractices;
	
	@FindBy(id = "txtPayerNameInCheck")
	private WebElement txtPayerName;	
	
	@FindBy(xpath = "//select[@ng-model='ddlPaymentMode']")
	private WebElement ddlPaymentMode;
	
	@FindBy(id = "txtPaymentDate")
	private WebElement txtPaymentDate;	
	
	@FindBy(id = "txtDepositDate")
	private WebElement txtDepositDate;	
	
	@FindBy(id = "txtEFT")
	private WebElement txtEFTCheck;	
	
	@FindBy(id = "txtTotalPaidAmt")
	private WebElement txtTotalPaidAmt;
	
	@FindBy(id = "txtPendingAmt")
	private WebElement txtPendingAmttobeposted;
	
	@FindBy(id = "txtFilenameOfTheCheckCopy")
	private WebElement txtFilenameOfTheCheckCopy;
	
	@FindBy(id = "txtFileLocationOfTheCheckCopy")
	private WebElement txtFileLocationOfTheCheckCopy;
	
	@FindBy(id = "txtRemarks")
	private WebElement txtRemarks;
	
	
	@FindBy(id = "btnAdd")
	//@FindBy(xpath = "//select[ng-click='AddMissingEOBDetails()']")
	private WebElement btnAddMissingEOBDetails;
	
	
	/* DateTime Picker - Payment Date /Deposit  date	*/
	
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
	
	@FindBy(xpath = "//button[@ng-hide = 'editingData[row.InventoryID]']")
	private WebElement btneditingData;
	
	@FindBy(xpath = "//button[@id = 'btnUpdate']")
	private WebElement btnUpdate;

	
   /* ************************************** */
	

	
	
	
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
			Configuration.logger.log(LogStatus.INFO, "Class: MissisngEOBS, Mehtod: select_Practice <br>" + e.getMessage());
		}
	}
	
	
	
	public MissisngEOBS set_PayerName(String payerName) 
	{
		txtPayerName.sendKeys(payerName);
		return this;
	}
	
	public MissisngEOBS set_ModeofPayment(String ModeofPayment) 
	{
		utils.select(ddlPaymentMode,ModeofPayment);
		return this;
	}
	
	public MissisngEOBS set_PaymentDate(String PaymentDate) 
	{
		txtPaymentDate.sendKeys(PaymentDate);
		return this;
	}
	
	public MissisngEOBS set_DepositDate(String DepositDate) 
	{
		txtDepositDate.sendKeys(DepositDate);
		return this;
	}
		
	public MissisngEOBS set_EFTCheck(String EFTCheck)
	{
		txtEFTCheck.sendKeys(EFTCheck);
		return this;
	}
	public MissisngEOBS set_TotalPaidAmount(String TotalPaidAmount) 
	{
		txtTotalPaidAmt.sendKeys(TotalPaidAmount);
		return this;
	}
	
	public MissisngEOBS set_PendingAmounttobePosted(String PendingAmounttobePosted) 
	{
		txtPendingAmttobeposted.sendKeys(PendingAmounttobePosted);
		return this;
	}
	
	public MissisngEOBS set_FileNameoftheCheckcopy(String FileNameoftheCheckcopy) 
	{
		txtFilenameOfTheCheckCopy.sendKeys(FileNameoftheCheckcopy);
		return this;
	}
	
	public MissisngEOBS set_FileLocationoftheCheckcopy(String FileLocationoftheCheckcopy) 
	{
		txtFileLocationOfTheCheckCopy.sendKeys(FileLocationoftheCheckcopy);
		return this;
	}
	
	public MissisngEOBS set_Remarks(String Remarks) 
	{
		txtRemarks.sendKeys(Remarks);
		return this;
	}
	
	
	
	
	/**
	 * 
	 * @return
	 */
	 public Info add_meob() 
	 {		 
		 try 
		 {
			 btnAddMissingEOBDetails.click();
			 utils.wait(200);
			 
			 utils.save_ScreenshotToReport("add_MEOB");
			 utils.wait_Until_InvisibilityOf_LoadingScreen();
			 
			 if(messageHeader.getAttribute("textContent").toUpperCase().equals("SUCCESS"))
			 {
				 
				 String inventoryID = Configuration.driver.findElement(By.xpath("//table/tbody/tr[1]/td[1]")).getAttribute("textContent");				 
				 return new Info(true, inventoryID);
				 
			 }
			 else
			 {
				
				 return new Info(false, messageDetails.getAttribute("textContent"));
			 }
		 }
		catch (Exception e) 
		{
			return new Info(false, "Class: MissingEOBS, Mehtod: add_meob <br>" + e.getMessage());
		}
	 }
		
		
	
	
	
	
	
	
	/*
	public WebElement getMessageHeader() {
		
		return messageHeader;
		
	}
	public String getMessageDetails() {
		
		return messageDetails.getAttribute("textContent");
		
	}
   
public boolean verify_PayerNameinCheckEFT(String PayerName ) {
	
	try {
	Configuration.driver.findElement(By.xpath("//th[text() = '"+PayerName+"']"));
	 return true;
	}
	catch (NoSuchElementException e)
	{
		return false;
	}
	catch(Exception e)
	{
	return false;
}*/


/*public void edit_MEOb(String PayerName ) {
	new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(btneditingData)).click();
	
	//WebElement ddlEdit_Role = tblConfigured_User.findElement(By.xpath(".//td[text() = '"+employeeId+"']/following-sibling::td//select"));		
	new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(btneditingData));
    Utils.select(btnAddMissingEOBDetails, PayerName);
}*/


}
