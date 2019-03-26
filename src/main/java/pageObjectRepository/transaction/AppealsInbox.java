package pageObjectRepository.transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.Constants;
import configuration.WebDriverUtils;
import configuration.Constants.Queues;
import customDataType.Info;
import customDataType.Transaction;

public class AppealsInbox 
{
	WebDriverUtils utils = new WebDriverUtils();
	
	@FindBy(xpath = "//select[@ng-model='ddlPractices']")
	private WebElement ddlPractices;
	
	
	
	@FindBy(xpath = "//li[contains(@ng-click , 'APPEAL')]")
	private WebElement imgAppeal;
	
	@FindBy(xpath = "//li[contains(@ng-click , 'TSAVE')]")
	private WebElement imgTempSave;
	
	@FindBy(xpath = "//li[contains(@ng-click , 'PYMNT_ANL')]")
	private WebElement imgPaymentResponse;
	
	@FindBy(xpath = "//li[contains(@ng-click , 'CLRSPRLSE')]")
	private WebElement imgClientEscalationRelease;
	
	@FindBy(xpath = "//li[contains(@ng-click , 'CDG_ANL')]")
	private WebElement imgCodingResponse;
	
	@FindBy(xpath = "//li[contains(@ng-click , 'CLR')]")
	private WebElement imgClarification;

	
	
	
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_17']")
	private WebElement txtSearch_Appeals;
	
	
	
	@FindBy(xpath = "//table[@id='DataTables_Table_17']")
	private WebElement tblAppeals;
		
	@FindBy(xpath = "//table[@id='DataTables_Table_14']")
	private WebElement tblClarification;
	
	@FindBy(xpath = "//table[@id='DataTables_Table_15']")
	private WebElement tblClientEscalation_Release;
	
	@FindBy(xpath = "//table[@id='DataTables_Table_16']")
	private WebElement tblTempSave;
	
	@FindBy(xpath = "//table[@id='DataTables_Table_6']")
	private WebElement tblPayment;
	
	@FindBy(xpath = "//table[@id='DataTables_Table_7']")
	private WebElement tblCoding;
	
	
	
	
	@FindBy(linkText = "Pending Workorder")
	private WebElement lnkWorkOderSummary;
	
	@FindBy(xpath = "//div[@id='WorkOrderSummaryModal']//button[text() = 'CANCEL']")
	private WebElement btn_Cancel_WorkOderSummary;
	
	@FindBy(xpath = "//div[@id='WorkOrderSummaryModal']")
	private WebElement div_WorkOrderSummaryModal;
	
	
	
	@FindBy(xpath="//div[@class = 'sweet-alert hideSweetAlert']/h2")
	private WebElement messageHeader;
	
	@FindBy(xpath = "//div[@class = 'sweet-alert hideSweetAlert']/h2/following-sibling::p")
	private WebElement messageDetails;
	
	
	
	

	public void click_Image_Appeals()
	{	
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(imgAppeal)).click();
		utils.wait_Until_InvisibilityOf_LoadingScreen();
	}

	public void click_Image_TempSave()
	{			
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(imgTempSave)).click();
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
	
	
	
	
	public void select_Practice(String visibleText, boolean matchWholeText)
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
	}	
	

	public ArrayList<String> get_AllPractices() 
	{
		try 
		{
			return utils.get_Options(ddlPractices);
		}
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, "Class: AppealsInbox, Mehtod: get_AllPractices <br>" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * Searchs Claim based on Account Number in Payment Inbox
	 * @param sAccountNumber
	 * 
	 * @return true when Account Number found else false
	 */
	public Info search_PatientAccountNumber(String sAccountNumber) 
	{
		try 
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			txtSearch_Appeals.clear();
			txtSearch_Appeals.sendKeys(sAccountNumber);
			
			utils.save_ScreenshotToReport("AppealsinboxSearch");
			
			if(utils.isElementPresent(By.xpath("//a[text() = '"+ sAccountNumber +"']"))) 
			{
				return new Info(true, "<b>Patient Account ["+ sAccountNumber +"] found</b>");
			}
			return new Info(false, "<b>Patient Account ["+ sAccountNumber +"] not found</b>");
			
		} 
		catch (Exception e) 
		{
			return new Info(false, "Class: AppealsInbox, Mehtod: search_PatientAccountNumber <br>" + e.getMessage());
		}	
	}
	
	
	/**
	 * Searchs Claim based on UID in Appeals Inbox
	 * @param UID
	 * 
	 * @return true when UID found else false
	 */
	public boolean search_UID(String UID) 
	{
		try 
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			txtSearch_Appeals.clear();
			txtSearch_Appeals.sendKeys(UID);			
			
			if(utils.isElementPresent(tblAppeals, By.xpath(".//td[text() = '" + UID + "']")))
			{
				return true;
			}
			
			/*utils.captureScreenshot("Payment", "search_UID_" + UID);
			Configuration.logger.log(LogStatus.FAIL, "UID ["+ UID +"] not found at Payment inbox");*/						
		} 
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, "Class: AppealsInbox, Mehtod: search_UID <br>" + e.getMessage());
		}		
		return false;
	}


	public Transaction submit_AppealsResponse(String sAccountNumber, String claimStatus, String actionCode, String resolveType, String notes)
	{
		try 
		{
			ClaimTransaction objClaimTransaction = PageFactory.initElements(Configuration.driver, ClaimTransaction.class);
			
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(By.xpath("//td/a[text() = '" + sAccountNumber + "']"))).click();
			utils.wait_Until_InvisibilityOf_LoadingScreen();
					
		 	return objClaimTransaction.submit_Claim(claimStatus, actionCode, resolveType, notes);
			
		}
		catch (Exception e)
		{		
			return new Transaction(false, "Class: AppealsInbox, Mehtod: submit_AppealsResponse <br>" + e.getMessage());
		}
	}


	
	public HashMap<String, String> get_PendingWorkOrder(String sPractice) 
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
			utils.save_ScreenshotToReport("PendingWorkOrder_" + sPractice);
			
						
			//get rows in table
			List<WebElement> summaryRows = div_WorkOrderSummaryModal.findElements(By.xpath(".//tbody/tr"));
						
			//loop through the rows and get cell values
			for (WebElement row : summaryRows) 
			{					
				List<WebElement> lstCells = row.findElements(By.xpath("./td"));				
				mapSummary.put(lstCells.get(0).getAttribute("textContent").trim(), lstCells.get(6).getAttribute("textContent").trim());
			}
			
			btn_Cancel_WorkOderSummary.click();
		} 
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.WARNING, "Method name: get_PendingWorkOrder<br>" + e.getMessage());
		}		
		return mapSummary;
	}
	
	public int get_TotalAccounts_WorkOrderSummary(final Queues queue) 
	{		
		try
		{			
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			switch(queue)
			{
				case TEMP_SAVE: return get_Count(Constants.Queues.TEMP_SAVE);				
				case NEEDCALL_RESPONSE: return get_Count(Constants.Queues.NEEDCALL_RESPONSE);				
				case CLARIFICATION: return get_Count(Constants.Queues.CLARIFICATION);				
				case CLIENT_ESCALATION_RELEASE: return get_Count(Constants.Queues.CLIENT_ESCALATION_RELEASE);				
				case APPEALS: return get_Count(Constants.Queues.APPEALS);				
				
				case CODING_RESPONSE: return get_Count(Constants.Queues.CODING_RESPONSE);				
				case PRINT_AND_MAIL_RESPONSE: get_Count(Constants.Queues.PRINT_AND_MAIL_RESPONSE);				
				case PAYMENT_RESPONSE: return get_Count(Constants.Queues.PAYMENT_RESPONSE);
								
				
				default: System.out.println("Not found, ["+queue+"] "); break;
			}
		}
		catch (NoSuchElementException e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	public int get_Count(Constants.Queues queue)
	{
		try 
		{
			List<WebElement> lstRows = new ArrayList<>();
			Set<String> setAccounts = new HashSet<>();
			
			//get paginate div for passed queue table
			WebElement wbTable = get_Queue_Table(queue);
			WebElement divPaginate = wbTable.findElement(By.xpath("./following-sibling::div[contains(@id, 'paginate')]"));			
						
			// get number of pages from paginate div
			int pages = Integer.parseInt(divPaginate.findElement(By.xpath("(./span/a)[last()]")).getText());					
					
			// scroll to bottom of the page
			utils.scrollWindow(0, 4000);
						
			// iterate through the pages
			for (int i = 1; i <= pages; i++) 
			{
				try 
				{
					WebElement wbPage = divPaginate.findElement(By.xpath("./span/a[text() = '" + i + "']"));
					wbPage.click();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				
				// get patient account number from each row
				lstRows = wbTable.findElements(By.xpath(".//tr"));
				for (int j = 1; j < lstRows.size(); j++)
				{
					//System.out.println(wbTable.findElement(By.xpath(".//tr["+j+"]/td[1]/a")).getText());
					setAccounts.add(wbTable.findElement(By.xpath(".//tr["+j+"]/td[1]/a")).getText());
				}
			}
			
			// log mismatch accounts
			return setAccounts.size();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	private WebElement get_Queue_Table(Constants.Queues queue) 
	{
		switch(queue)
		{
			case APPEALS: return tblAppeals;
			case CODING_RESPONSE: return tblCoding;
			case PAYMENT_RESPONSE: return tblPayment;
			case TEMP_SAVE: return tblTempSave;
			case CLIENT_ESCALATION_RELEASE: return tblClientEscalation_Release;
			case CLARIFICATION: return tblClarification;
			// add followUP
			
			default:break;
		}
		return null;
	}
}
