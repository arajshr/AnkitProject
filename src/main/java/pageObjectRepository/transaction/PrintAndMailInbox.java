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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.WebDriverUtils;
import customDataType.Info;

public class PrintAndMailInbox
{
	WebDriverUtils utils = new WebDriverUtils();
	ClaimTransaction objClaimTransaction;
	
	@FindBy(xpath = "//select[@ng-model='ddlPractices']")
	private WebElement ddlPractices;
	
	@FindBy(xpath = "//table[@id='DataTables_Table_0']")
	private WebElement tblPrintAndMail;
	
	@FindBy(xpath = "//label[contains(text(), 'Search')]/input")
	private WebElement txtSearch;
	
	
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
		utils.select(ddlPractices, visibleText, matchWholeText);
		Info info = null;
		
		if(utils.isElementPresent(messageHeader))
		{
			String header = messageHeader.getAttribute("textContent").toUpperCase();
		 	String msg = messageDetails.getAttribute("textContent");
		 	
		 	if(header.equals("ERROR"))
			{
		 		info = new Info(false, header + "_" + msg);
			}
		 	else
		 	{
		 		info = new Info(true, header + "_" + msg);
		 	}
		}
	 	
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return info;
	}
	
	public boolean verifyPatientAccountNumber(String UID)
	{
		try
		{	
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			txtSearch.clear();
			txtSearch.sendKeys(UID);
			
			if(utils.isElementPresent(By.xpath("//td["+ utils.getColumnIndex(tblPrintAndMail, "UID") +"][text() = '" + UID + "']")))
				return true;
			else
				throw new NoSuchElementException("Element not found");
		}
		catch (NoSuchElementException e)
		{
			//e.printStackTrace();
			return false;
		}
	}

	public boolean submit_PrintAndMail_Response(String accountNumber) 
	{
		return false;	
		
		/*objClaimTransaction = PageFactory.initElements(Configuration.driver, ClaimTransaction.class);
		
		WebElement  account = Configuration.driver.findElement(By.xpath("//td[text() = '"+ UID +"']/preceding-sibling::td//a"));
		String AccountNumber = account.getText();
		account.click();
		
		HashMap<String, String> transaction = Configuration.excel.getExcelData(Constants.sheetTransaction, "printAndMailResponse");	 		
	 	boolean transactionStatus = objClaimTransaction.submit_Claim(transaction.get("claim_status"), transaction.get("action_code"), transaction.get("note"));	 		
		
	 	if(transactionStatus == false)
	 		AccountNumber = "";
	 
	 	return AccountNumber;
	 	*/
	 	
	 	
	 	
	 	
		
		/*new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(By.xpath("//td[text() = '"+accountNumber+"']/preceding-sibling::td/a"))).click();
		utils.wait_Until_Invisibility_Of_LoadingScreen();
		
		HashMap<String, String> transaction = Configuration.excel.getExcelData(Constants.sheetTransaction, "codingResponse");	 		
		return objClaimTransaction.submit_Claim(transaction.get("claim_status"), transaction.get("action_code"), transaction.get("note"));*/
	
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
			WebElement divPaginate = tblPrintAndMail.findElement(By.xpath("./following-sibling::div[contains(@id, 'paginate')]"));			
						
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
				lstRows = tblPrintAndMail.findElements(By.xpath(".//tr"));
				for (int j = 1; j < lstRows.size(); j++)
				{
					setAccounts.add(tblPrintAndMail.findElement(By.xpath(".//tr["+j+"]/td[1]/a")).getText());
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
