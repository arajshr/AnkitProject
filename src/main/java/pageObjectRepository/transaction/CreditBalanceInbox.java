package pageObjectRepository.transaction;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.Constants;
import configuration.WebDriverUtils;
import configuration.Constants.Queues;
import customDataType.Info;
import customDataType.Transaction;

public class CreditBalanceInbox 
{	
	WebDriverUtils utils = new WebDriverUtils();
	
	
	@FindBy(xpath = "//select[@ng-model='ddlPractices']")
	private WebElement ddlPractices;
	
	
	
	@FindBy(xpath = "//li[contains(@ng-click , 'CRDBAL')]")
	private WebElement imgCreditBalance;
	
	@FindBy(xpath = "//li[contains(@ng-click , 'TSAVE')]")
	private WebElement imgTempSave;
	
	@FindBy(xpath = "//li[contains(@ng-click , 'PYMNT_ANL')]")
	private WebElement imgPaymentResponse;
	
	
	
	@FindBy(xpath = "//table[@id='DataTables_Table_12']")
	private WebElement tblCreditBalance;
	
	@FindBy(xpath = "//table[@id='DataTables_Table_16']")
	private WebElement tblTempSave;
		
	@FindBy(xpath = "//table[@id='DataTables_Table_6']")
	private WebElement tblPayment;
	
	
	
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_16']")
	private WebElement txtSearch_TempSave;
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_12']")
	private WebElement txtSearch_CreditBalance;
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_6']")
	private WebElement txtSearch_Payment;

	
	
	
	@FindBy(xpath="//div[@class = 'sweet-alert hideSweetAlert']/h2")
	private WebElement messageHeader;
	
	@FindBy(xpath = "//div[@class = 'sweet-alert hideSweetAlert']/h2/following-sibling::p")
	private WebElement messageDetails;
		
		
	
	
	public void select_Practice(String visibleText, boolean matchWholeText)
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
	
	
	
	public void click_Image_CreditBalance()
	{
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(imgCreditBalance)).click();
		
	}	
	
	public void click_Image_TempSave()
	{	
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(imgTempSave)).click();
	}
		
	public void click_Image_PaymentResponse()
	{
		try
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			imgPaymentResponse.click();
		}
		catch (NoSuchElementException e) 
		{
			utils.save_ScreenshotToReport("PMTRSP_IMG");
			Assert.assertTrue(false, "<b>Payement Response queue not found</b>");
			
		}
		catch (Exception e1) 
		{
			utils.save_ScreenshotToReport("PMTRSP_IMG");
			Assert.assertTrue(false, e1.getMessage());
		}
	}
	
	
	public Info open_PatientAccount(Queues queue)
	{ 	

		utils.wait_Until_InvisibilityOf_LoadingScreen();
		
		if(utils.isElementPresent(get_Queue_Table(queue), By.xpath(".//td[text() = 'No data available in table']")))
		{				
			utils.save_ScreenshotToReport("CreditBalance_NoData");
			return new Info(false, "<b>No data available in table</b>");
		}
		else
		{
			for(int index=1; index <= 10; index++)
			{
				WebElement linkAccount = get_Queue_Table(queue).findElement(By.xpath(".//tbody/tr["+ index +"]/td/a"));
				String account = linkAccount.getAttribute("textContent");
		 		
				if(utils.isAccountLocked(linkAccount).getStatus() == true)
				{
					Configuration.logger.log(LogStatus.INFO, "<b>Account ["+ account +"] - This Account Is Locked By The Other User</b>");
					continue;
				}
				
				return new Info(true, "Claim opened");
			}
		}
		return null;
	}
	
	public Transaction open_PatientAccount(Queues queue, String having, String value)
	{
		try
		{

			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			WebElement linkAccount = null;
			utils.wait(200);
			
			if(having.equals("ACCOUNT_NUMBER"))
			{
				linkAccount = get_Queue_Table(queue).findElement(By.xpath(".//td/a[text() = '"+value+"']"));
			}
			else if(having.equals("INSURANCE"))
			{
				txtSearch_CreditBalance.clear();
				txtSearch_CreditBalance.sendKeys(value);
				
				utils.wait(1000);				
				linkAccount = get_Queue_Table(queue).findElement(By.xpath(".//td[text() = '" + value + "']/preceding-sibling::td//a"));
			}
			if(utils.isAccountLocked(linkAccount).getStatus() == true)
			{
				return new Transaction(false, "<b>There are no accounts in " + queue + " queue</b>");
			}
			
			return new Transaction(true, "");
		}
		
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, e.getMessage());
			return new Transaction(false, "");
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
			WebElement element = get_Search_Input(queue);
			element.sendKeys(accountNumber);	
			
			Configuration.driver.findElement(By.linkText(accountNumber));
			return new Info(true, "Account found");
		}
		catch (NoSuchElementException e)
		{
			utils.save_ScreenshotToReport(queue.toString() + "search_" + accountNumber);			
			return new Info(false, "<b>Patient Account Number ["+ accountNumber +"] not found at " + queue + "</b>");
		}
		catch (Exception e) 
		{
			utils.save_ScreenshotToReport(queue.toString() + "search_" + accountNumber);
			
			e.printStackTrace();
			return new Info(false, "Class: CreditBalanceInbox, Mehtod: search_Account <br>" + e.getMessage());
		}
	}
	
	
	private WebElement get_Search_Input(Constants.Queues queue) 
	{
		switch(queue)
		{
			case CREDIT_BALANCE: return txtSearch_CreditBalance;
			case TEMP_SAVE: return txtSearch_TempSave;
			case PAYMENT_RESPONSE: return txtSearch_Payment;
			
			default:break;
		}
		return null;
	}
	
	private WebElement get_Queue_Table(Constants.Queues queue) 
	{
		switch(queue)
		{
			case CREDIT_BALANCE: return tblCreditBalance;
			case TEMP_SAVE: return tblTempSave;
			case PAYMENT_RESPONSE: return tblPayment;
			
			default:break;
		}
		return null;
	}
	
}
