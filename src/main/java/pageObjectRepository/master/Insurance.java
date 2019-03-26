package pageObjectRepository.master;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.WebDriverUtils;
import customDataType.Transaction;

public class Insurance 
{
	WebDriverUtils utils = new WebDriverUtils();
	
	@FindBy(xpath="//div[@class = 'sweet-alert hideSweetAlert']/h2")
	private WebElement messageType;
	
	@FindBy(xpath = "//div[@class = 'sweet-alert hideSweetAlert']/h2/following-sibling::p")
	private WebElement messageDetails;
	
	
	@FindBy(xpath = "//select[@ng-model='ddlPractices']")
	private WebElement ddlPractices;
	
	
	@FindBy(id = "txtInsCode")
	private WebElement txtInsCode;
	
	@FindBy(id = "TxtPayerID")
	private WebElement txtPayerID;
	
	@FindBy(id = "TxtPayerName")
	private WebElement txtPayerName;
	
	@FindBy(id = "TxtClearingHouse")
	private WebElement txtClearingHouse;
	
	@FindBy(id = "txtAddress")
	private WebElement txtAddress;
	
	@FindBy(id = "TxtState")
	private WebElement txtState;
	
	@FindBy(id = "TxtCont_Phone")
	private WebElement txtContact;
	
	@FindBy(xpath = "//select[@ng-model='ddlClaimType']")
	private WebElement ddlClaimType;
	
	@FindBy(xpath = "//select[@ng-model='ddlFilingLimit']")
	private WebElement ddlFilingLimit;
	
	@FindBy(xpath = "//select[@ng-model='ddlHCFAMed']")
	private WebElement ddlHCFAMed;
	
	@FindBy(xpath = "//select[@ng-model='ddlHCFASecMed']")
	private WebElement ddlHCFASecMed;
	
	@FindBy(xpath = "//select[@ng-model='ddlEDIEligibility']")
	private WebElement ddlEDIEligibility;
	
	@FindBy(xpath = "//select[@ng-model='ddlEDIClaimStatus']")
	private WebElement ddlEDIClaimStatus;
	
	@FindBy(xpath = "//select[@ng-model='ddlPayerWebsite']")
	private WebElement ddlPayerWebsite;
	
	@FindBy(id = "TxtPayer_Website")
	private WebElement txtPayerWebsite;
	
	@FindBy(id = "btnAdd")
	private WebElement btnAdd_Insurance;

	@FindBy(id = "DataTables_Table_0")
	private WebElement tbl_Insurance;
	
	@FindBy(xpath = "//div[@id='DataTables_Table_0_filter']//input")
	private WebElement txtSearch;
	
	
	public void select_Practice(String visibleText)
	{
		utils.select(ddlPractices, visibleText);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
	}
	
	public Insurance set_InsuranceCode(String insCode)
	{
		txtInsCode.sendKeys(insCode);
		return this;
	}
	
	public Insurance set_PayerName(String payerName)
	{
		txtPayerName.sendKeys(payerName);
		return this;
	}
	
	public Insurance set_Address(String address)
	{
		txtPayerName.sendKeys(address);
		return this;
	}
	
	public Insurance set_State(String state)
	{
		txtState.sendKeys(state);
		return this;
	}
	
	public Insurance set_ContactPhone(String contact)
	{
		txtContact.sendKeys(contact);
		return this;
	}
	
	public Insurance select_PayerWebsite(String payerWebsite)
	{
		utils.select(ddlPayerWebsite, payerWebsite);
		return this;
	}
	
	public Transaction add_Insurance() 
	{
		btnAdd_Insurance.click();
		
		File scrFile = ((TakesScreenshot)Configuration.driver).getScreenshotAs(OutputType.FILE);
	 	utils.wait_Until_InvisibilityOf_LoadingScreen();
	 	
	 	
	 	if(messageType.getAttribute("textContent").toUpperCase().equals("SUCCESS"))
		{
	 		return new Transaction(true, messageDetails.getAttribute("textContent"));
		}	 	
	 	else if(messageType.getAttribute("textContent").toUpperCase().equals("ERROR") || messageType.getAttribute("textContent").toUpperCase().equals("INFORMATION"))
		{
			utils.save_ScreenshotToReport("add_Insurance", scrFile);
			//Configuration.logger.log(LogStatus.FAIL, messageType.getAttribute("textContent") + "_" + messageDetails.getAttribute("textContent"));
		}
	 	
	 	return new Transaction(false, messageDetails.getAttribute("textContent"));
	 	
	}

	public boolean verify_Insurance(String insCode)
	{
		txtSearch.clear();
		txtSearch.sendKeys(insCode);
		
		
		try 
		{
			tbl_Insurance.findElement(By.xpath("//tbody//td[text() = '" + insCode + "']"));
			return true;
		} 
		catch (Exception e) 
		{
			utils.save_ScreenshotToReport("verify_Insurance");
			Configuration.logger.log(LogStatus.FAIL, messageType.getAttribute("textContent") + "_" + messageDetails.getAttribute("textContent"));
			return false;
		}
	}
}
