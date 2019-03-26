package pageObjectRepository.transaction;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
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

public class CodingInbox 
{
	WebDriverUtils utils = new WebDriverUtils();
	
	ClaimTransaction objClaimTransaction;
	
	@FindBy(xpath = "//select[@ng-model='ddlPractices']")
	private WebElement ddlPractices;
	
	@FindBy(xpath = "//div[@id='DataTables_Table_0_filter']//input")
	private WebElement txtSearch;
	
	@FindBy(xpath = "//table[@id='DataTables_Table_0']")
	private WebElement tblCoding;
	
	@FindBy(xpath = "//div[@class='dataTables_info']")
	private WebElement divEntries;
	
	
	
	@FindBy(linkText = "WorkOrder Summary")
	private WebElement lnkWorkOderSummary;
	
	@FindBy(xpath = "//div[@id='WorkOrderSummaryModal']//button[text() = 'CANCEL']")
	private WebElement btn_Cancel_WorkOderSummary;
	
	@FindBy(xpath = "//div[@id='WorkOrderSummaryModal']")
	private WebElement div_WorkOrderSummaryModal;
	
	
	
	@FindBy(xpath="//div[@class = 'sweet-alert hideSweetAlert']/h2")
	private WebElement messageHeader;
	
	@FindBy(xpath = "//div[@class = 'sweet-alert hideSweetAlert']/h2/following-sibling::p")
	private WebElement messageDetails;
	
	
	
	
	
	
	public Info select_Practice(String visibleText, boolean matchWholeText)
	{		
		try 
		{
			utils.select(ddlPractices, visibleText, matchWholeText);
			File srcFile = ((TakesScreenshot)Configuration.driver).getScreenshotAs(OutputType.FILE);
			utils.wait(2000);
			
			Info info = null;
			
			if(utils.isElementPresent(messageHeader))
			{
				String header = messageHeader.getAttribute("textContent").toUpperCase();
			 	String msg = messageDetails.getAttribute("textContent");
			 	
			 	if(header.equals("ERROR"))
				{
			 		utils.save_ScreenshotToReport(visibleText + "_" + msg, srcFile);
			 		info = new Info(false, "<b>" + header + "_" + msg + "</b>");
				}
			}
			else
			{
				info = new Info(true, "Practice selected");
			}
		 	
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			
			
			return info;
		}
		catch (Exception e) 
		{		
			return new Info(false, "Class: CodingInbox, Mehtod: select_Practice <br>" + e.getMessage());
		}
	}
	
	/**
	 * Returns true when Account present in Coding Inbox
	 * 
	 * @param sAccountNumber
	 * @return boolean value
	 */
	public Info search_Encounter(String sEcounterID) 
	{
		try 
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			txtSearch.clear();
			txtSearch.sendKeys(sEcounterID);
			
			
			utils.save_ScreenshotToReport("Coding_search_Account_" + sEcounterID);
			
			if(utils.isElementPresent(By.xpath("//a[text() = '"+ sEcounterID +"']"))) 
			{
				return new Info(true, "<b>Ecounter ["+ sEcounterID +"] found in Coding inbox</b>");
			}
			return new Info(false, "<b>Ecounter ["+ sEcounterID +"] not found in Coding inbox</b>");
			
		} 
		catch (Exception e) 
		{
			return new Info(false, "Class: CodingInbox, Mehtod: search_UID <br>" + e.getMessage());	
		}
	}
	
	public Info search_UID(String sUID) 
	{
		try 
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			txtSearch.clear();
			txtSearch.sendKeys(sUID);
			
			utils.save_ScreenshotToReport("Coding_search_UID_" + sUID);
			
			if(utils.isElementPresent(By.xpath("//td[text() = '"+ sUID +"']"))) 
			{
				return new Info(true, "<b>UID ["+ sUID +"] found in Coding inbox</b>");
			}
			return new Info(false, "<b>UID ["+ sUID +"] not found in Coding inbox</b>");
		} 
		catch (Exception e) 
		{
			return new Info(false, "Class: CodingInbox, Mehtod: search_UID <br>" + e.getMessage());	
		}
	}

	
	/**
	 * Submits account to coding response queue
	 * 
	 * @param accountNumber
	 * @return
	 */
	public boolean submit_CodingResponse(String sUID, String claimStatus, String actionCode, String notes) 
	{
		try
		{
			objClaimTransaction = PageFactory.initElements(Configuration.driver, ClaimTransaction.class);
			
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(By.xpath("//td[text() = '"+sUID+"']/preceding-sibling::td/a"))).click();
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
				 		
			return objClaimTransaction.submit_Claim(claimStatus, actionCode, notes);
		}
		catch (Exception e)
		{
			Configuration.logger.log(LogStatus.INFO, "Class: CodingInbox, Mehtod: submit_Coding_Response" + e.getMessage());
			return false;
		}
		
	}

	public int get_TotalAccounts_CodingInbox() 
	{
		String[] tokens = divEntries.getAttribute("textContent").split("\\s+");
		int length = tokens.length;
		
		try
		{
			return Integer.parseInt(tokens[length-2]);
		}
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.WARNING, "Class: CodingInbox, Method: get_TotalAccounts_CodingInbox<br>" + e.getMessage());
		}
		return 0;
	}

	public ArrayList<String> get_AllPractices() 
	{
		try 
		{
			return utils.get_Options(ddlPractices);
		}
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, "Class: CodingInbox, Mehtod: get_AllPractices <br>" + e.getMessage());
		}
		return null;
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
			
			File srcFile = utils.captureScreenshot();
			
			if(utils.isElementPresent(messageHeader)) // && messageHeader.isDisplayed()
			{
				String header = messageHeader.getAttribute("textContent").toUpperCase();
				
			 	if(header.equals("ERROR") || header.equals("INFORMATION"))
				{
			 		utils.save_ScreenshotToReport("NoAccountsWOS", srcFile);
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
			Configuration.logger.log(LogStatus.WARNING, "Class: CodingInbox, Method: get_PendingWorkOrder<br>" + e.getMessage());
		}
		finally
		{
			btn_Cancel_WorkOderSummary.click();
		}
		return mapSummary;
	}
	
	
	public int get_TotalAccounts_WorkOrderSummary() 
	{		
		try
		{
			List<WebElement> lstRows = new ArrayList<>();
			Set<String> setAccounts = new HashSet<>();
			
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			//get paginate div for passed queue table
			WebElement divPaginate = tblCoding.findElement(By.xpath("./following-sibling::div[contains(@id, 'paginate')]"));			
						
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
				lstRows = tblCoding.findElements(By.xpath(".//tr"));
				for (int j = 1; j < lstRows.size(); j++)
				{
					setAccounts.add(tblCoding.findElement(By.xpath(".//tr["+j+"]/td[1]/a")).getText());
				}
			}
			
			// log mismatch accounts
			return setAccounts.size();
			
		}
		catch (Exception e)
		{
			Configuration.logger.log(LogStatus.WARNING, "Class: CodingInbox, Method: get_TotalAccounts_WorkOrderSummary<br>" + e.getMessage());
		}
		return 0;
		
	}
	
}
