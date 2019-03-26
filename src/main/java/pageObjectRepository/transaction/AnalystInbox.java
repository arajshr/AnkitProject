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

public class AnalystInbox
{
	String tblRegularQueue = "DataTables_Table_0";
	
	WebDriverUtils utils = new WebDriverUtils();
	
	Dashboard objDashboard;
	AnalystInbox objInbox;
	ClaimTransaction objClaimTransaction;
	
	@FindBy(xpath = "//select[@ng-model='ddlPractices']")
	private WebElement ddlPractices;
	
	@FindBy(xpath = "//select[@ng-model='ddlKickout']")
	private WebElement ddlKickout;
	
	@FindBy(xpath = "//select[@ng-model='ddlPriorityfilenames']")
	private WebElement ddlPriorityFilename;
	
	
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
	
	@FindBy(xpath = "//li[contains(@ng-click , 'APPEAL')]")
	private WebElement imgAppealResponse;
	
	@FindBy(xpath = "//li[contains(@ng-click , 'CRDBAL')]")
	private WebElement imgCreditBalance;
	
	
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_1']")
	private WebElement txtSearch_Priority;
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_2']")
	private WebElement txtSearch_Regular;	
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_5']")
	private WebElement txtSearch_Denial;
		
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_3']")
	private WebElement txtSearch_CallerResponse;
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_6']")
	private WebElement txtSearch_Payment;
		
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_7']")
	private WebElement txtSearch_Coding;
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_8']")
	private WebElement txtSearch_Credentialing;
		
	/*@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_8']")
	private WebElement txtSearch_PrintAndMail;*/
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_14']")
	private WebElement txtSearch_Clarification;
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_15']")
	private WebElement txtSearch_ClientEscalation_Release;
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_16']")
	private WebElement txtSearch_TempSave;
	
	@FindBy(xpath = "//input[@type = 'search' and @aria-controls = 'DataTables_Table_18']")
	private WebElement txtSearch_AppealsResponse;
	
	
	
	
	
	
	@FindBy(xpath = "//table[@id='DataTables_Table_1']")
	private WebElement tblPriority;
	
	@FindBy(xpath = "//table[@id='DataTables_Table_2']")
	private WebElement tblRegular;
	
	@FindBy(xpath = "//table[@id='DataTables_Table_3']")
	private WebElement tblCallerResponse;	
	
	@FindBy(xpath = "//table[@id='DataTables_Table_5']")
	private WebElement tblDenial;
	
	@FindBy(xpath = "//table[@id='DataTables_Table_6']")
	private WebElement tblPayment;
	
	@FindBy(xpath = "//table[@id='DataTables_Table_7']")
	private WebElement tblCoding;
	
	@FindBy(xpath = "//table[@id='DataTables_Table_8']")
	private WebElement tblCredentialing;
	
	/*@FindBy(xpath = "//table[@id='DataTables_Table_8']")
	private WebElement tblPrintAndMail;*/
	
	/*@FindBy(xpath = "//table[@id='DataTables_Table_9']")
	private WebElement tblVoiceMail;*/
	
	@FindBy(xpath = "//table[@id='DataTables_Table_11']")
	private WebElement tblKickOut;
	
	/*@FindBy(xpath = "//table[@id='DataTables_Table_11']")
	private WebElement tblCreditBalance;*/
	
	@FindBy(xpath = "//table[@id='DataTables_Table_12']")
	private WebElement tblCorrespondance;
	
	@FindBy(xpath = "//table[@id='DataTables_Table_12']")
	private WebElement tblCreditBalance;
	
	@FindBy(xpath = "//table[@id='DataTables_Table_14']")
	private WebElement tblClarification;
	
	@FindBy(xpath = "//table[@id='DataTables_Table_15']")
	private WebElement tblClientEscalation_Release;
	
	@FindBy(xpath = "//table[@id='DataTables_Table_16']")
	private WebElement tblTempSave;
	
	@FindBy(xpath = "//table[@id='DataTables_Table_18']")
	private WebElement tblAppealsResponse;
	
	
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
	
	
		
	
	@FindBy(xpath = "//div[@id='DataTables_Table_2_length']/label/select")
	private WebElement ddl_Regular_Length;

	@FindBy(xpath = "//div[@id='DataTables_Table_1_length']/label/select")
	private WebElement ddl_Priority_Length;
		
	@FindBy(xpath = "//div[@id='DataTables_Table_5_length']/label/select")
	private WebElement ddl_Denial_Length;
	
	@FindBy(xpath = "//div[@id='DataTables_Table_16_length']/label/select")
	private WebElement ddl_TempSave_Length;
	

	/* ******************** PENDING WORKORDER SUMMARY *********************** */
	
	
	@FindBy(linkText = "Pending Workorder")
	private WebElement lnkWorkOderSummary;
	
	@FindBy(xpath = "//div[@id='WorkOrderSummaryModal']//button[text() = 'CANCEL']")
	private WebElement btn_Cancel_WorkOderSummary;
	
	@FindBy(xpath = "//div[@id='WorkOrderSummaryModal']")
	private WebElement div_WorkOrderSummaryModal;
	
	/* ******************** PENDING WORKORDER SUMMARY *********************** */
	
	
	
	
	/* ******************** ALERT *********************** */
	
	@FindBy(xpath="//div[@class = 'sweet-alert hideSweetAlert']/h2")
	private WebElement messageHeader;
	
	@FindBy(xpath = "//div[@class = 'sweet-alert hideSweetAlert']/h2/following-sibling::p")
	private WebElement messageDetails;
	
	private String sBtnOK = "//div[@class = 'sweet-alert showSweetAlert visible']//button[text() = 'OK'";
	@FindBy(xpath = "//div[@class = 'sweet-alert showSweetAlert visible']//button[text() = 'OK']")
	private WebElement btnOK;
	
	
	/* ******************** ALERT *********************** */
	
	
	
	
	/* ******************** CUSTOM FILTER *********************** */
	
	@FindBy(id = "btnCustomfilter")
	private WebElement btnCustomFilter;
	
	@FindBy(xpath = "//select[@ng-model='ddlColumns']")
	private WebElement ddlColumns;

	@FindBy(xpath = "//select[@ng-model='ddlConditions']")
	private WebElement ddlConditions;

	@FindBy(id = "txtValue")
	private WebElement txtValue;

	@FindBy(id = "btnAdd")
	private WebElement btnAdd_CustomFilter;

	@FindBy(id = "btnSearch")
	private WebElement btnSearch_CustomFilter;

	@FindBy(id = "btnrefresh")
	private WebElement btnRefresh;
	
	/* ******************** CUSTOM FILTER *********************** */
	
	
	
	
	
	
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
			Configuration.logger.log(LogStatus.INFO, "Class: PaymentInbox, Mehtod: select_Practice <br>" + e.getMessage());
		}
	}
	
	
	public void select_Practice(String visibleText)
	{	
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		
		utils.select(ddlPractices, visibleText, false);  // should be true - check
		
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		
		//utils.select(ddl_Priority_Length,"10", true);
	}
	
	
	
	

	public void click_Image_Regular(boolean filterEntries)
	{
		try 
		{
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(imgRegular)).click();
			
			
			long startTime = System.currentTimeMillis();			
			utils.wait_Until_InvisibilityOf_LoadingScreen();			
			long endTime = System.currentTimeMillis();
			
			Configuration.logger.log(LogStatus.INFO, "<b>Time to load Regular queue: " + (endTime - startTime)/1000 + " seconds</b>");
			//System.out.println("Time to load Regular queue: " + (endTime - startTime)/1000 + " sec");
			
			
			if(filterEntries)
			{
				utils.select(ddl_Regular_Length,"10", true);
			}
			
			
			utils.wait_Until_InvisibilityOf_LoadingScreen();
		}
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, "Class: AnalystInbox, Mehtod: click_Image_Priority <br>" + e.getMessage());
		}
		
		
	}
	
	public void click_Image_Priority(boolean filterEntries)
	{
		try 
		{			
			
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(imgPriority)).click();
		
		
		long startTime = System.currentTimeMillis();				
		utils.wait_Until_InvisibilityOf_LoadingScreen();		
		long endTime = System.currentTimeMillis();
		
		Configuration.logger.log(LogStatus.INFO, "<b>Time to load Priority queue: " + (endTime - startTime)/1000 + " seconds</b>");
		//System.out.println("Time to load Priority queue: " + (endTime - startTime)/1000 + " sec");
		
		
		if(filterEntries)
			utils.select(ddl_Priority_Length,"10", true);
		}
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, "Class: AnalystInbox, Mehtod: click_Image_Priority <br>" + e.getMessage());
		}
	}
	
	public void click_Image_KickOut()
	{
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(imgKickOut)).click();
		utils.wait_Until_InvisibilityOf_LoadingScreen();		
	}
	
	public void click_Image_TempSave(boolean filterEntries)
	{	try 
		{		
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(imgTempSave)).click();
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			if(filterEntries)			
				utils.select(ddl_TempSave_Length,"10", true);
		}
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, "Class: AnalystInbox, Mehtod: click_Image_Priority <br>" + e.getMessage());
		}
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
	
	public void click_Image_Denail(boolean filterEntries)
	{
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(imgDenail)).click();
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		
		if(filterEntries)
			utils.select(ddl_Denial_Length,"10", true);
	}
	
	public void click_Image_AppealResponse()
	{
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(imgAppealResponse)).click();
		utils.wait_Until_InvisibilityOf_LoadingScreen();
	}
	
	public void click_Image_CreditBalance()
	{
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(imgCreditBalance)).click();
		utils.wait_Until_InvisibilityOf_LoadingScreen();
	}
	
	
	
	
	
	public String submit_Claim_From_RegularQueue(String accountNumber, String sheetName, String nextQueue)  // used in junior caller/ QC history
	{
		objClaimTransaction = PageFactory.initElements(Configuration.driver, ClaimTransaction.class);
		String AccountNumber = Configuration.driver.findElement(By.linkText(accountNumber)).getText();
		
		Configuration.driver.findElement(By.linkText(accountNumber)).click();
		
		HashMap<String, String> transaction = Configuration.excel.get_TransactionData(nextQueue);
		boolean transactionStatus = objClaimTransaction.submit_Claim(transaction.get("claim_status"), transaction.get("action_code"),transaction.get("resolve_type"), transaction.get("note")).getStatus();
		
		if(transactionStatus == true)
	 		return AccountNumber;
		else
	 	return "";		
	}
		
	public Transaction open_PatientAccount(Queues queue, String having, String value)
	{
		try
		{
			WebElement linkAccount = null;
			utils.wait(100);
			
			if(having.equals("ACCOUNT_NUMBER"))
			{
				linkAccount = get_Queue_Table(queue).findElement(By.xpath(".//td/a[text() = '"+value+"']"));
			}
			else if(having.equals("INSURANCE"))
			{
				txtSearch_Regular.clear();
				txtSearch_Regular.sendKeys(value);
				
				utils.wait(1000);				
				linkAccount = get_Queue_Table(queue).findElement(By.xpath(".//td[text() = '"+value+"']/preceding-sibling::td//a"));
			}			
						
			Info result = utils.isAccountLocked(linkAccount);			
			if(result.getStatus() == true) // locked
			{
				return new Transaction(false, "<b>"+result.getDescription()+"</b>");
			}
			
			return new Transaction(true, "Account opened");
		}
		
		catch (Exception e) 
		{
			return new Transaction(false,  "Class: AnalystInbox, Mehtod: open_PatientAccount <br>" + e.getMessage());
		}
	}
	
	public Transaction open_PatientAccount(Queues queue)
	{ 	
		try 
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			if(utils.isElementPresent(get_Queue_Table(queue), By.xpath(".//td[text() = 'No data available in table']")))
			{	
				utils.save_ScreenshotToReport(queue + "_NoData");
				return new Transaction(false, "<b>No data available in table</b>");
			}
			else
			{
				for(int index=1; index <= 10; index++)
				{
					WebElement linkAccount = get_Queue_Table(queue).findElement(By.xpath(".//tbody/tr["+ index +"]/td/a"));
					String account = linkAccount.getAttribute("textContent");
			 		
					Info result = utils.isAccountLocked(linkAccount);				
					if(result.getStatus() == true) // locked
					{
						Configuration.logger.log(LogStatus.INFO, "<b>Account ["+ account +"] - This Account Is Locked By The Other User</b>");
						continue;
					}
					
					return new Transaction(true, "<b>Claim opened</b>");
				}
				
				return new Transaction(false, "<b>Accounts are locked</b>");
			}
		}
		catch (Exception e) 
		{
			return new Transaction(false,  "Class: AnalystInbox, Mehtod: open_PatientAccount <br>" + e.getMessage());
		}
		
	}	
	
	public Transaction open_PatientAccount(Queues queue, int rowIndex)
	{ 	
		try
		{
			WebElement linkAccount = get_Queue_Table(queue).findElement(By.xpath(".//tbody/tr["+ rowIndex+"]/td/a"));
	 		String account = linkAccount.getAttribute("textContent");
	 		
			Info result = utils.isAccountLocked(linkAccount);			
			if(result.getStatus() == true) // locked
			{
				return new Transaction(false, "<b>Account ["+ account +"] - This Account Is Locked By The Other User</b>");
			}
				
			return new Transaction(true, "Account opened");
			
		} 
		catch (Exception e) 
		{
			return new Transaction(false,  "Class: AnalystInbox, Mehtod: open_PatientAccount <br>" + e.getMessage());
		}
 				
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

	
	public String get_AccountNumber(Queues queue, int index) 
	{
		try 
		{
			String sAccountNuber = get_Queue_Table(queue).findElement(By.xpath(".//tbody/tr["+ index +"]/td/a")).getText();
			return sAccountNuber;
		}
		catch (Exception e) 
		{
			return "";
		}
		
	}

	
	
	private int split_Info(By by)
	{
		WebElement element = Configuration.driver.findElement(by);
		String arrInfo[] = element.getAttribute("textContent").split(" ");
		return Integer.parseInt(arrInfo[5].replace(",", ""));
	}
		
	private int get_Count(Constants.Queues queue)
	{
		try 
		{
			List<WebElement> lstRows = new ArrayList<>();
			Set<String> setAccounts = new HashSet<>();
			
			//get paginate div for passed queue table
			WebElement wbTable = get_Queue_Table(queue);
			WebElement divPaginate = wbTable.findElement(By.xpath("./following-sibling::div[contains(@id, 'paginate')]"));			
			
			int pages = 0;
			
			try 
			{
				// get number of pages from paginate div
				
				if(utils.isElementPresent(divPaginate, By.xpath("(./span/a)[last()]")))
				{
					pages = Integer.parseInt(divPaginate.findElement(By.xpath("(./span/a)[last()]")).getText());
				}
				else
				{
					pages = 0;
				}
			}
			catch (NoSuchElementException e)
			{
				
			}
			catch (NumberFormatException e) 
			{
				System.out.println("NumberFormatException - " + divPaginate.findElement(By.xpath("(./span/a)[last()]")).getText());
			}
			
			
								
					
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
			
			// scroll to top of the page
			utils.scrollWindow(0, -4000);
			
			// log mismatch accounts
			return setAccounts.size();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return 0;
		
		
		
		
		/*List<WebElement> lstLinks = table.findElements(By.tagName("a"));
		Set<String> setAccountNumbers = new HashSet<>();
		
		for (WebElement link : lstLinks) 
		{
			setAccountNumbers.add(link.getAttribute("textContent"));
		}
		return setAccountNumbers.size();*/
	}
		
	public int get_TotalAccounts_WorkOrderSummary(final Queues queue) 
	{		
		try
		{			
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			switch(queue)
			{			
				case REGULAR:	if(!radio120.isSelected()) 
									click_Aging_Category_120();
								
								utils.wait_Until_InvisibilityOf_LoadingScreen();
								int count120 =  get_Count(Constants.Queues.REGULAR);
								
								click_Aging_Category_91_120();
								utils.wait_Until_InvisibilityOf_LoadingScreen();								
								int count91 =  get_Count(Constants.Queues.REGULAR);
								
								click_Aging_Category_61_90();
								utils.wait_Until_InvisibilityOf_LoadingScreen();								
								int count61 =  get_Count(Constants.Queues.REGULAR);
								
								
								click_Aging_Category_31_60();
								utils.wait_Until_InvisibilityOf_LoadingScreen();								
								int count31 =  get_Count(Constants.Queues.REGULAR);
								
								click_Aging_Category_0_30();
								utils.wait_Until_InvisibilityOf_LoadingScreen();								
								int count0 =  get_Count(Constants.Queues.REGULAR);
								
								
								if(!radio120.isSelected()) 
									click_Aging_Category_120();
								
								return (count120 + count91 + count61 + count31 + count0);
						


				case PRIORITY: 	return get_Count(Constants.Queues.PRIORITY);
				case DENIAL: 	if(!radio120.isSelected()) 
									click_Aging_Category_120();
									count120 = get_Count(Constants.Queues.DENIAL);
						
									click_Aging_Category_91_120();
									utils.wait_Until_InvisibilityOf_LoadingScreen();								
									count91 = get_Count(Constants.Queues.DENIAL);
									
									click_Aging_Category_61_90();
									utils.wait_Until_InvisibilityOf_LoadingScreen();								
									count61 = get_Count(Constants.Queues.DENIAL);
									
									
									click_Aging_Category_31_60();
									utils.wait_Until_InvisibilityOf_LoadingScreen();								
									count31 = get_Count(Constants.Queues.DENIAL);
									
									click_Aging_Category_0_30();
									utils.wait_Until_InvisibilityOf_LoadingScreen();								
									count0 = get_Count(Constants.Queues.DENIAL);
									
									if(!radio120.isSelected()) 
										click_Aging_Category_120();
									
									return (count120 + count91 + count61 + count31 + count0);
					
					
				case KICKOUT:	if(!radio120.isSelected()) 
								click_Aging_Category_120();
								count120 = get_Count(Constants.Queues.KICKOUT);
					
								click_Aging_Category_91_120();
								utils.wait_Until_InvisibilityOf_LoadingScreen();								
								count91 = get_Count(Constants.Queues.KICKOUT);
								
								click_Aging_Category_61_90();
								utils.wait_Until_InvisibilityOf_LoadingScreen();								
								count61 = get_Count(Constants.Queues.KICKOUT);
								
								
								click_Aging_Category_31_60();
								utils.wait_Until_InvisibilityOf_LoadingScreen();								
								count31 = get_Count(Constants.Queues.KICKOUT);
								
								click_Aging_Category_0_30();
								utils.wait_Until_InvisibilityOf_LoadingScreen();								
								count0 = get_Count(Constants.Queues.KICKOUT);
								
								
								return (count120 + count91 + count61 + count31 + count0);
							
				
				case TEMP_SAVE: return get_Count(Constants.Queues.TEMP_SAVE);
				
				case NEEDCALL_RESPONSE: return get_Count(Constants.Queues.NEEDCALL_RESPONSE);
				
				case CLARIFICATION: return get_Count(Constants.Queues.CLARIFICATION);
				
				case CLIENT_ESCALATION_RELEASE: return get_Count(Constants.Queues.CLIENT_ESCALATION_RELEASE);
				
				case CREDENTIALING_RESPONSE: return get_Count(Constants.Queues.CREDENTIALING_RESPONSE);
				
				
				case CODING_RESPONSE: return get_Count(Constants.Queues.CODING_RESPONSE);
				
				case PRINT_AND_MAIL_RESPONSE: get_Count(Constants.Queues.PRINT_AND_MAIL_RESPONSE);
				
				case PAYMENT_RESPONSE: return get_Count(Constants.Queues.PAYMENT_RESPONSE);
				
				case APPEALS_RESPONSE: return get_Count(Constants.Queues.APPEALS_RESPONSE);
				
				case CREDIT_BALANCE: return get_Count(Constants.Queues.CREDIT_BALANCE);
								
				
				default: System.out.println("Not found, ["+queue+"] "); break;
			}			
		}
		catch (NoSuchElementException e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	

	public float get_Pateient_InsBalace(Constants.Queues queue, String accountNumber)
	{
		By findBy = By.xpath(".//a[text() = '"+accountNumber+"']/../following-sibling::td[3]");		
		get_Queue_Table(queue).findElement(findBy).getAttribute("textContent");
		
		return Float.parseFloat(Configuration.driver.findElement(findBy).getAttribute("textContent").replace("$", "").replaceAll(",", ""));		
	}
		
	public Info verify_AccountRemoved(String accountNumber, String sUID, Constants.Queues queue) 
	{
		utils.wait_Until_InvisibilityOf_LoadingScreen();		
		try
		{
			get_Search_Input(queue).sendKeys(accountNumber);
			
			
			// if account is present, check for UID
			if(utils.isElementPresent(tblPayment,By.xpath(".//a[text() = '"+ accountNumber +"']")))
			{
				open_PatientAccount(queue, "ACCOUNT_NUMBER", accountNumber);
				utils.wait_Until_InvisibilityOf_LoadingScreen();
				
				utils.save_ScreenshotToReport(queue.toString() + "_verify_Account_Removed");
				
				ClaimTransaction objCliam = PageFactory.initElements(Configuration.driver, ClaimTransaction.class);
				boolean isFound = objCliam.verify_UID(sUID);
				objCliam.close_ClaimTransaction();
				
				if(isFound)  //UID is present
				{
					return new Info(false, "<b>Account ["+ accountNumber +"], UID ["+ sUID +"] found</b>");
				}
			}
			utils.save_ScreenshotToReport(queue.toString() + "_verify_Account_Removed");
			return new Info(true, "<b>Account ["+ accountNumber +"], UID ["+ sUID +"] not found</b>");
			
		}
		catch (Exception e)
		{	
			return new Info(false, "Class: AnalystInbox , Mehtod: verify_AccountRemoved <br>" + e.getMessage());
		}
	}
	
	
	public void select_PriorityFileName(String sFileName) 
	{
		try 
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			utils.select(ddlPriorityFilename, sFileName, false);
			Configuration.logger.log(LogStatus.INFO, "<b>Select priority file ["+ sFileName +"]</b>");
		}
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.FAIL, "Class: AnalystInbox, Method name: select_PriorityFileName<br>" + e.getMessage());
		}
		
	}
	
	
	public void clearSearch(Queues queue) 
	{
		try 
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			WebElement element = get_Search_Input(queue);
			element.clear();
			
		}
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.FAIL, "Class: AnalystInbox, Method name: clearSearch<br>" + e.getMessage());
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
			element.clear();
			element.sendKeys(accountNumber);	
			
			WebElement wbTable = get_Queue_Table(queue);
			
			
			utils.save_ScreenshotToReport("AnalystInboxSearch_" + queue);
			
			if(utils.isElementPresent(wbTable, By.xpath(".//a[text() = '"+ accountNumber +"']"))) 
			{
				return new Info(true, "<b>Account ["+ accountNumber +"] found at " + queue + "</b>");
			}
			return new Info(false, "<b>Account ["+ accountNumber +"] not found at " + queue + "</b>");
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Info(false,  "Class: AnalystInbox, Mehtod: search_TempSave <br>" + e.getMessage());
		}
	}
	
	
	/**
	 * Returns true when Patient Account Number is found in TempSave 
	 *  
	 * @param pateintAccountNumber
	 * @return 
	 * @return boolean value
	 */
	public Info search_TempSave(String pateintAccountNumber, String sEncounterID, String claimStatus, String actionCode, String resolveType, String notes, String sDOS, HashMap<String, String> details) 
	{
		try 
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			txtSearch_TempSave.clear();
			txtSearch_TempSave.sendKeys(pateintAccountNumber);
			
			utils.save_ScreenshotToReport("TempSaveSearch");
			
			if(utils.isElementPresent(tblTempSave, By.xpath("//a[text() = '"+ pateintAccountNumber +"']/../following-sibling::td[text() = '"+ sEncounterID +"']"))) 
			{
				
				
				WebElement wbRow = tblTempSave.findElement(By.xpath("//a[text() = '"+ pateintAccountNumber + "']/../following-sibling::td[text() = '"+ sEncounterID +"']/.."));
				String actClaimStaus = wbRow.findElement(By.xpath("./td[9]/div[1]")).getAttribute("textContent");
				String actActionCode = wbRow.findElement(By.xpath("./td[10]/div[1]")).getAttribute("textContent");
				String actResolveType = wbRow.findElement(By.xpath("./td[11]/div[1]")).getAttribute("textContent");
				String actNotes = wbRow.findElement(By.xpath("./td[12]/div[1]")).getAttribute("textContent"); 
				
				
				String actPatientName = wbRow.findElement(By.xpath("./td[2]")).getAttribute("textContent");
				String actDOS = wbRow.findElement(By.xpath("./td[4]")).getAttribute("textContent");
				String actInsBalance = wbRow.findElement(By.xpath("./td[5]")).getAttribute("textContent");
				String actPolicyID = wbRow.findElement(By.xpath("./td[6]")).getAttribute("textContent");
				String actInsurance = wbRow.findElement(By.xpath("./td[7]")).getAttribute("textContent");
				//String actPendingDays = wbRow.findElement(By.xpath("./td[8]")).getAttribute("textContent");
				
				String expPatientName = details.get("PatientName");
				//String expDOB = details.get("DOB");
				String expInsBalance = details.get("InsBalance");
				String expPolicyID = details.get("PolicyID");
				String expInsurance = details.get("Insurance");
				
				
				
				
				StringBuilder strError = new StringBuilder();
				
				strError.append((actClaimStaus.equals(claimStatus)) ? "" : "<br>Claim Status, expected ["+ claimStatus +"] but found ["+ actClaimStaus +"]");
				strError.append((actActionCode.equals(actionCode)) ? "" : "<br>Action Code, expected ["+ actionCode +"] but found ["+ actActionCode +"]");
				strError.append((actResolveType.equals(resolveType)) ? "" : "<br>WorkedBy, expected ["+ resolveType +"] but found ["+ actResolveType +"]");				
				strError.append((actNotes.equals(notes)) ? "" : "<br>Notes, expected ["+ notes +"] but found ["+ actNotes +"]");
				
				
				strError.append((actPatientName.equals(expPatientName)) ? "" : "<br>Patient Name, expected ["+ expPatientName +"] but found ["+ actPatientName +"]");
				strError.append((actDOS.equals(sDOS)) ? "" : "<br>DOB, expected ["+ sDOS +"] but found ["+ actDOS +"]");
				strError.append((actInsBalance.equals(expInsBalance)) ? "" : "<br>InsBalance, expected ["+ expInsBalance +"] but found ["+ actInsBalance +"]");				
				strError.append((actPolicyID.equals(expPolicyID)) ? "" : "<br>PolicyID, expected ["+ expPolicyID +"] but found ["+ actPolicyID +"]");
				strError.append((actInsurance.equals(expInsurance)) ? "" : "<br>Insurance, expected ["+ expInsurance +"] but found ["+ actInsurance +"]");
				
				
				if(strError.length() > 0)
					Configuration.logger.log(LogStatus.FAIL, "<b>Following values do not match in Temp Save table</b> <br><br>" + strError.toString());
				else
					Configuration.logger.log(LogStatus.INFO, "Transaction details are verified in Temp Save grid");
				
				
				return new Info(true, "<b>Patient Account ["+ pateintAccountNumber +"], Encounter ID ["+ sEncounterID +"] found in TempSave</b>");
			}
			return new Info(false, "<b>Patient Account ["+ pateintAccountNumber +"], Encounter ID ["+ sEncounterID +"] not found in TempSave</b>");
			
		} 
		catch (Exception e) 
		{
			return new Info(false, "Class: AnalystInbox, Mehtod: search_TempSave <br>" + e.getMessage());
		}
	}

	
	/**
	 * Returns true when Patient Account Number is found in Client Escalation Release and 
	 * Verifies TL Comments 
	 * 
	 * @param patientAccountNumber
	 * @param tlComments
	 * @return boolean value
	 */
	public Info search_Clarification(String patientAccountNumber, String tlComments) 
	{
		try 
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			txtSearch_Clarification.clear();
			txtSearch_Clarification.sendKeys(patientAccountNumber);
			
			utils.save_ScreenshotToReport("search_Clarification");
			
			if(utils.isElementPresent(tblClarification, By.xpath(".//a[text() = '"+ patientAccountNumber +"']")))
			{
				/*String actTlComments = tblClarification.findElement(By.xpath("//a[text() = '"+patientAccountNumber+"']/../following-sibling::td[6]")).getAttribute("textContent");
				if(!actTlComments.equals(tlComments))
				{
					Configuration.logger.log(LogStatus.WARNING, "TL Comment mismatch, expected["+tlComments+"] but found ["+actTlComments+"]");
				}*/
				
				return new Info(true, "<b>Patient Account ["+ patientAccountNumber +"] found at Clarification</b>");
			}
			return new Info(false, "<b>Patient Account ["+ patientAccountNumber +"] found at Clarification</b>");
		} 
		catch (Exception e) 
		{
			return new Info(false, "Class: AnalystInbox, Mehtod: search_Clarification <br>" + e.getMessage());
		}
		
		
	}

	
	public Info search_ClientEscalationRelease(String patientAccountNumber, String tlComments) 
	{
		try 
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			txtSearch_ClientEscalation_Release.clear();
			txtSearch_ClientEscalation_Release.sendKeys(patientAccountNumber);
			
			utils.save_ScreenshotToReport("search_ClientEscalationRelease");
			
			if(utils.isElementPresent(tblClientEscalation_Release, By.xpath(".//a[text() = '"+ patientAccountNumber +"']")))
			{
				/*String actTlComments = tblClientEscalation_Release.findElement(By.xpath("//a[text() = '"+patientAccountNumber+"']/../following-sibling::td[6]")).getAttribute("textContent");
				if(!actTlComments.equals(tlComments))
				{
					Configuration.logger.log(LogStatus.WARNING, "TL Comment mismatch, expected["+tlComments+"] but found ["+actTlComments+"]");
				}*/
				
				return new Info(true, "<b>Patient Account ["+ patientAccountNumber +"] found at Client Escalation Release</b>");
			}
			
			return new Info(false, "<b>Patient Account ["+ patientAccountNumber +"] found at Client Escalation Release</b>");
		} 
		catch (Exception e) 
		{
			return new Info(false, "Class: AnalystInbox, Mehtod: search_ClientEscalationRelease <br>" + e.getMessage());
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
			//case PRINT_AND_MAIL_RESPONSE: return tblPrintAndMail;
			case PAYMENT_RESPONSE: return tblPayment;
			case TEMP_SAVE: return tblTempSave;
			case NEEDCALL_RESPONSE: return tblCallerResponse;
			case CLIENT_ESCALATION_RELEASE: return tblClientEscalation_Release;
			case CLARIFICATION: return tblClarification;
			case DENIAL: return tblDenial;
			case KICKOUT: return tblKickOut;
			case APPEALS_RESPONSE: return tblAppealsResponse;
			case CREDIT_BALANCE: return tblCreditBalance; 
			
			default:break;
		}
		return null;
	}
	
	private WebElement get_Search_Input(Constants.Queues queue) 
	{
		switch(queue)
		{
			case REGULAR: return txtSearch_Regular;
			case PRIORITY: return txtSearch_Priority;
			case DENIAL: return txtSearch_Denial;
			case CODING_RESPONSE: return txtSearch_Coding;
			case CREDENTIALING_RESPONSE: return txtSearch_Credentialing;
			/*case PRINT_AND_MAIL_RESPONSE: return txtSearch_PrintAndMail;*/
			case PAYMENT_RESPONSE: return txtSearch_Payment;
			case NEEDCALL_RESPONSE: return txtSearch_CallerResponse;
			case CLIENT_ESCALATION_RELEASE: return txtSearch_ClientEscalation_Release;
			case CLARIFICATION: return txtSearch_Clarification;
			case APPEALS_RESPONSE: return txtSearch_AppealsResponse;
			
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


	public void select_KickOutRule(String visibleText) 
	{
		try 
		{
			utils.select(ddlKickout, visibleText);
			utils.wait_Until_InvisibilityOf_LoadingScreen();
		} 
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.WARNING, "Method Name: select_KickOutRule<br>" + e.getMessage());
		}
		
		
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
		
	public int get_TotalAccounts(final Queues queue)  // for regular/ denail/ kickout dos check
	{		
		try
		{	
			/*new FluentWait<WebDriver>(Configuration.driver)
	        .withTimeout(5, TimeUnit.SECONDS)
	        .pollingEvery(1000, TimeUnit.MILLISECONDS)
	        .until(new Predicate<WebDriver>() {

	            public boolean apply(WebDriver d) {
	                 List<WebElement> rawList = get_Queue_Table(queue).findElements(By.xpath(".//td/a"));
	                 return (rawList.size() > 0);
	            }
	        });*/
			
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			switch(queue)
			{
				case REGULAR:	
								
								utils.wait_Until_InvisibilityOf_LoadingScreen();
								int count120 = split_Info(By.id("DataTables_Table_2_info"));
								
								return (count120); 
											
				
				
				case PRIORITY: return split_Info(By.id("DataTables_Table_1_info"));
				case DENIAL: return split_Info(By.id("DataTables_Table_5_info"));
				
				case KICKOUT:	
								count120 = split_Info(By.id("DataTables_Table_11_info"));
								
								return (count120);
								
				
				case TEMP_SAVE: return split_Info(By.id("DataTables_Table_16_info"));
				
				case NEEDCALL_RESPONSE: return split_Info(By.id("DataTables_Table_3_info"));
				case CLARIFICATION: return split_Info(By.id("DataTables_Table_14_info"));
				case CLIENT_ESCALATION_RELEASE: return split_Info(By.id("DataTables_Table_15_info"));
				case CREDENTIALING_RESPONSE: return split_Info(By.id("DataTables_Table_8_info"));
				
				case CODING_RESPONSE: return split_Info(By.id("DataTables_Table_6_info"));
				/*case PRINT_AND_MAIL_RESPONSE: return split_Info(By.id("DataTables_Table_8_info"));*/
				case PAYMENT_RESPONSE: return split_Info(By.id("DataTables_Table_5_info"));
				
				
				default:break;
			}
			
			
		}
		catch (NoSuchElementException e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	

	/**
	 * checks for nagative InsBalance
	 * @param queue
	 */
	public void verify_NegativeInsBalance(Constants.Queues queue) 
	{
		try 
		{
			List<WebElement> lstRows = new ArrayList<>();
			HashMap<String, String> mapInsBalance = new HashMap<>();
			
			//get paginate div for passed queue table 
			WebElement divPaginate = get_Queue_Table(queue).findElement(By.xpath("./following-sibling::div[contains(@id, 'paginate')]"));			
					
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
				
				utils.wait(100);
				// get patient account number and insBalance form each row
				lstRows = get_Queue_Table(queue).findElements(By.xpath(".//tbody/tr"));
				
				for (WebElement row : lstRows) 
				{
					String insBalance = row.findElement(By.xpath("./td[4]")).getText().replace("$", "");
					
					// verify insbalance >= 0
					if(Float.parseFloat(insBalance) >= 0)  // NOT WORKING
					{
						mapInsBalance.put(row.findElement(By.xpath("./td[1]")).getText(), row.findElement(By.xpath("./td[4]")).getText());   //PatientAccountNumber, insBalance
					}						
				}
			}
			
			// log mismatch accounts
			if(mapInsBalance.size() > 0)
			{
				Configuration.logger.log(LogStatus.ERROR, "<b>InsBalance greater than or equal to zero for following Credit Balance accounts</b></br>" + mapInsBalance);
			}
			else
			{
				Configuration.logger.log(LogStatus.INFO, "<b> InsBalance verified</b>");
			}
		}
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, "Class: AnalystInbox, Mehtod:  <br>" + e.getMessage());
		}
		
	}

	
	
	/**
	 * Open custom filter 
	 */
	public void open_CustomFilter()
	{
		try
		{
			btnCustomFilter.click();
		}
		catch (Exception e)
		{
			Configuration.logger.log(LogStatus.INFO, "Class: AnalystInbox, Mehtod: open_CustomFilter <br>" + e.getMessage());
		}
	}
	
	
	public void add_Condition(String sColumn, String sCondition, String sValue) 
	{
		try
		{
			Configuration.logger.log(LogStatus.INFO, "<b>Add search condition, Column ["+ sColumn +"], Condition ["+ sCondition +"], Value ["+ sValue +"]</b>");
			
			utils.select(ddlColumns, sColumn);
			utils.select(ddlConditions, sCondition);
			txtValue.sendKeys(sValue);
			
			btnAdd_CustomFilter.click();
			
			
			// verify search condition in below table
			String newCondition = "";
			switch (sCondition) 
			{
				case "Equal to":	newCondition = "=";					break;
									
				case "Not equal to":	newCondition = "<>";			break;
				
				case "Greater than":	newCondition = ">";				break;
				
				case "Less than":	newCondition = "<";					break;
				
				case "Greater than or qual to":	newCondition = ">=";	break;
				
				case "Less than or equal to":	newCondition = "<=";	break;
			
			}
			
			if(!utils.isElementPresent(By.xpath("//div[@id = 'tseacrchCondition']//td[text() = '"+sColumn+"']/following-sibling::td[text() = '"+ newCondition +"']/following-sibling::td[text() = '"+ sValue+"']")))
			{
				Configuration.logger.log(LogStatus.ERROR, "<b>Search condition not found in table <br><br> Column ["+ sColumn +"], Condition ["+ sCondition +"], Value ["+ sValue +"]</b>");
			}
		}
		catch (Exception e)
		{
			Configuration.logger.log(LogStatus.INFO, "Class: AnalystInbox, Mehtod: open_CustomFilter <br>" + e.getMessage());
		}		
	}
	
	public void search_CustomFilter() 
	{
		try 
		{
			btnSearch_CustomFilter.click();
			utils.wait_Until_InvisibilityOf_LoadingScreen();
		}
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, "Class: AnalystInbox, Mehtod: search_CustomFilter <br>" + e.getMessage());
		}		
	}


	public void verify_CustomFilter(String sColumn, String sCondition, String sValue) 
	{
		try 
		{
			Set<String> sError = new HashSet<>();
			
			if(sColumn.equals("Patient_Account_No"))
			{
				Set<String> setAccountNumbers = get_AllUniqueAccountNumbers(Constants.Queues.REGULAR);
				
				for (String accountNumber : setAccountNumbers) 
				{
					// add mismatch account number into set
					if(!accountNumber.equals(sValue))
					{
						sError.add(accountNumber);
					}
				}
				
				if(sError.size() > 0)
				{
					utils.save_ScreenshotToReport("Custom_Filter_Patient_Account_Number_Error");
					Assert.fail("Following Patient Account Number mismatch, " + sError);
					
				}
			}
		}
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, "Class: AnalystInbox, Mehtod: verify_CustomFilter <br>" + e.getMessage());
		}
		
	}
	
	
	
	

	private Set<String> get_AllUniqueAccountNumbers(Constants.Queues sQueue) 
	{
		try 
		{
			Set<String> setAccountNumbers = new HashSet<>();
			List<WebElement> lstWbPatientAccountNumber = get_Queue_Table(sQueue).findElements(By.xpath(".//td[1]"));
			for (WebElement wbAccount : lstWbPatientAccountNumber) 
			{
				setAccountNumbers.add(wbAccount.getText());
			}
			return setAccountNumbers;
		}
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, "Class: AnalystInbox, Mehtod: get_AllUniqueAccountNumbers <br>" + e.getMessage());

		}
		return null;
		
	}


	public void verify_Allocation_Rule(ArrayList<String> lstAccountNumbers) 
	{
		try 
		{
			 List<WebElement> lstWbAccNumbers = get_Queue_Table(Constants.Queues.REGULAR).findElements(By.xpath(".//a"));
			 ArrayList<String> lstAccountNumbersfromTable = new ArrayList<>();
			 
			 for (WebElement wbAccount : lstWbAccNumbers) 
			 {
				 lstAccountNumbersfromTable.add(wbAccount.getAttribute("textContent"));
			 }
			 
			 
			 if(lstAccountNumbers.equals(lstAccountNumbersfromTable))
			 {
				 System.out.println("Pass");
			 }
			 else
			 {
				 System.out.println("Fail");
			 }
			 
			
		}
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, "Class: AnalystInbox, Mehtod: verify_Allocation_Rule <br>" + e.getMessage());
		}
		
	}


	public List<String> get_MatchingAccounts(Queues sQueue, List<String> lstAccountsFromExcel) 
	{
		try
		{
			List<WebElement> lstWbPatientAccountNumber = get_Queue_Table(sQueue).findElements(By.xpath(".//td[1]"));
			
			ArrayList<String> lstAccountNumbersfromTable = new ArrayList<>();
			for (WebElement wbAccount : lstWbPatientAccountNumber) 
			{
				lstAccountNumbersfromTable.add(wbAccount.getText());
			}
			
			
			List<String> common = new ArrayList<String>(lstAccountsFromExcel);
			common.retainAll(lstAccountNumbersfromTable);
			
			return common;
		}
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, "Class: AnalystInbox, Mehtod: get_MatchingAccounts <br>" + e.getMessage());
		}
		return null;
	}


	public HashMap<String, String> getAccountDetailsFromRegularTable(String sAccountNumber) 
	{
		try
		{
			WebElement wbRow = tblRegular.findElement(By.xpath(".//td/a[text() = '"+ sAccountNumber +"']/../.."));
			
			String sPatientName = wbRow.findElement(By.xpath("./td[2]")).getText();
			String sDOB = wbRow.findElement(By.xpath("./td[3]")).getText();
			String sInsBalance = wbRow.findElement(By.xpath("./td[4]")).getText();
			String sPolicyID = wbRow.findElement(By.xpath("./td[5]")).getText();
			String sInsurance = wbRow.findElement(By.xpath("./td[6]")).getText();
			
			
			HashMap<String, String> details = new HashMap<>();
			details.put("PatientName", sPatientName);
			details.put("DOB", sDOB);
			details.put("InsBalance", sInsBalance);
			details.put("PolicyID", sPolicyID);
			details.put("Insurance", sInsurance);
			
			return details;
		}
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, "Class: AnalystInbox, Mehtod: getAccountDetailsFromRegularTable <br>" + e.getMessage());
		}
		return null;
		
	}


	public void edit_InLineEdit_TempSaveTransactionInputs(String sAccountNumber, String sEncounterID, String claimStatus, String actionCode, String resolveType, String notes) 
	{
		try 
		{
			tblTempSave.findElement(By.xpath("//td[text() = '"+ sEncounterID +"']/following-sibling::td//button[@title = 'Edit']")).click();
			
			
			WebElement ddlEditClaimStatus = tblTempSave.findElement(By.xpath("//td[text() = '"+ sEncounterID +"']/following-sibling::td//select[@ng-model = 'row.ClaimStatusCode']"));
			WebElement ddlEditActionCode = tblTempSave.findElement(By.xpath("//td[text() = '"+ sEncounterID +"']/following-sibling::td//select[@ng-model = 'row.ActionCode']"));
			WebElement ddlEditCallType = tblTempSave.findElement(By.xpath("//td[text() = '"+ sEncounterID +"']/following-sibling::td//select[@ng-model = 'row.CallType']"));
			WebElement txtEditNotes = tblTempSave.findElement(By.xpath("//td[text() = '"+ sEncounterID +"']/following-sibling::td//textarea"));
			
			utils.select(ddlEditClaimStatus, claimStatus);
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			utils.select(ddlEditActionCode, actionCode);
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			utils.select(ddlEditCallType, resolveType);
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			txtEditNotes.sendKeys(notes);			
		}
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, "Class: AnalystInbox, Mehtod: edit_TempSaveTransactionInputs <br>" + e.getMessage());

		}		
	}
	
	
	
	public void cancel_InLineEdit(String sAccountNumber, String sEncounterID) 
	{
		try 
		{
			tblTempSave.findElement(By.xpath("//td[text() = '"+ sEncounterID +"']/following-sibling::td//button[@title = 'Cancel']")).click();
			utils.wait_Until_InvisibilityOf_LoadingScreen();
		}
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, "Class: AnalystInbox, Mehtod: cancel_TempSaveTransactionInputs <br>" + e.getMessage());

		}		
	}


	public Info save_InLineEdit(String sAccountNumber, String sEncounterID)
	{
		try 
		{
			tblTempSave.findElement(By.xpath("//td[text() = '"+ sEncounterID +"']/following-sibling::td//button[@title = 'Submit']")).click();
			
			
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

		 	String header = messageHeader.getAttribute("textContent").toUpperCase();
		 	String msg = messageDetails.getAttribute("textContent");
		 	
		 	if(header.equals("SUCCESS"))
			{
		 		return new Info(true, "Saved successfully");
			}
		 	
		 	else
			{
		 		return new Info(false, header + " " + msg);
			}
		}
		catch (Exception e) 
		{
			return new Info(false, "Class: AnalystInbox, Mehtod: save_InLineEdit <br>" + e.getMessage());

		}
		
	}

	


	



	
	


	
	
}

