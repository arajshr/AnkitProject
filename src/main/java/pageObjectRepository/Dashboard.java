package pageObjectRepository;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.WebDriverUtils;

public class Dashboard 
{

	WebDriverUtils utils = new WebDriverUtils();
		
	@FindBy(xpath = "//a[text() = 'FeedBack']") 
	private WebElement lnkFeedBack;
	
	@FindBy(xpath = "//a[text() = 'Sign Out']") 
	private WebElement lnkSignOut;
	
	@FindBy (xpath = "//a[text() = 'Master ']")
	private WebElement lnkMaster;
	
	@FindBy (xpath ="//a[text()='Users & Role']")
	private WebElement lnkUserAndRoles;
	
	@FindBy (xpath ="//a[text()='Insurances']")
	private WebElement lnkInsurances;
	
	@FindBy (xpath ="//a[text()='Allocation Rule']")
	private WebElement lnkAllocationRule;
	
	@FindBy (xpath ="//a[text()='Kickout Rule']")
	private WebElement lnkKickoutRule;
	
	@FindBy(xpath = "//a[text() = 'Transaction ']")
	private WebElement lnkTransaction;
	
	@FindBy(xpath = "//a[text() = 'Inbox']") 
	private WebElement lnkInbox;
	
	@FindBy(xpath = "//a[text() = 'Claim Tracer']") 
	private WebElement lnkClaimTracer;
	
	@FindBy(xpath = "//a[text() = 'Coding Inbox']") 
	private WebElement lnkCodingInbox;
	
	@FindBy(xpath = "//a[text() = 'Print & Mail Inbox']") 
	private WebElement lnkPrintAndMailInbox;
	
	@FindBy(xpath = "//a[text() = 'Payment Inbox']") 
	private WebElement lnkPaymentInbox;
	
	@FindBy(xpath = "//a[text() = 'Missing EOBS']") 
	private WebElement lnkMissingEOBS;
	
	@FindBy(xpath = "//a[text() = 'MissingEOBInbox']") 
	private WebElement lnkMissingEOBInbox;
	
	@FindBy(xpath = "//a[text() = 'Review & Approve']") 
	private WebElement lnkReviewAndApprove;
	
	@FindBy(xpath = "//a[text() = 'Inbox']") 
	private WebElement lnkCorrespondanceInbox;
	
	
	@FindBy(xpath = "//a[text() = 'DashBoard ']") 
	private WebElement lnkDashBoard;
	
	@FindBy(xpath = "//a[text() = 'Print n Mail Queue']") 
	private WebElement lnkPrintMailQueue;
	
	@FindBy(xpath = "//a[text() = 'User Worked Accounts' and contains(@href, 'TableChartUserWorkedAccounts')]") 
	private WebElement lnkUserWorkedAccounts_Dashboard;
	
	
	@FindBy(xpath = "//a[text() = 'Historical Report ']") 
	private WebElement lnkHistoricalReport;
	
	@FindBy(xpath = "//a[text() = 'My Production Details']") 
	private WebElement lnkMyProductionDetails;
	
	@FindBy(xpath = "//a[text() = 'User worked Accounts' and contains(@href, 'HistoricalReport/UserworkedAccounts')]") 
	private WebElement lnkUserWorkedAccounts;
	
	@FindBy(xpath = "//a[text() = 'QC worked Accounts']") 
	private WebElement lnkQCWorkedAccounts;
	
	@FindBy(xpath = "//a[text() = 'Account Resolve Type']") 
	private WebElement lnkAccountResolveType;
	
	@FindBy(xpath = "//a[text() = 'Agent Production']") 
	private WebElement lnkAgentProduction;
	
	@FindBy(xpath = "//a[text() = 'Coding and Payment Queue Report']") 
	private WebElement lnkCodingAndPaymentQueueReport;
	
	@FindBy(xpath = "//a[text() = 'Production Report']") 
	private WebElement lnkProductionReport;
	
	@FindBy(xpath = "//a[text() = 'User Quality Report']") 
	private WebElement lnkUserQualityReport;
	
	@FindBy(xpath = "//a[text() = 'First Pass Payment Report']") 
	private WebElement lnkFirstPassPaymentReport;
	
	@FindBy(xpath = "//a[text() = 'Error Pareto by Agent']") 
	private WebElement lnkErrorParetoAgent;
	
	@FindBy(xpath = "//a[text() = '120Report']") 
	private WebElement lnk120Report;
	
	@FindBy(xpath = "//a[text() = 'Revenue Per Visit']") 
	private WebElement lnkRevenuePerVisit;
	
	@FindBy(xpath = "//a[text() = 'Gross Collection & Net Collection Report']") 
	private WebElement lnkGrossNetCollection;
	
	@FindBy(xpath = "//a[text() = 'Client Priority Tracking Report']")  
	private WebElement lnkClientPriorityTracking_Report;
	
	@FindBy(xpath = "//a[text() = 'Client Priority Tracking']") 
	private WebElement lnkClientPriorityTracking_Dashboard;
	
	@FindBy(xpath = "//a[text() = 'Kick out report']") 
	private WebElement lnkKickoutReport;
	
	@FindBy(xpath = "//a[contains(text() , 'Appeals % of Resolution')]") 
	private WebElement lnkAppealsResolution ;
	
	@FindBy(xpath = "//a[text() = 'OutStanding Claims']") 
	private WebElement lnkOutStandingClaims;
	
	@FindBy(xpath = "//a[text() = 'Provider Credential Report']") 
	private WebElement lnkProviderCredential;
	
	@FindBy(xpath = "//a[text() = 'MEOB Production Report']") 
	private WebElement lnkMEOBProduction_Report;
	
	@FindBy(xpath = "//a[text() = 'Error Category Report']") 
	private WebElement lnkErrorCategory;
	
	
	
	
	@FindBy(xpath = "//a[text() = 'Rebuttal Review']") 
	private WebElement lnkRebuttalReview;
	
	@FindBy(xpath = "//a[text() = 'Sign Out']/preceding-sibling::h5/span") 
	private WebElement workedBy;
	
	@FindBy(xpath = "//a[text() = 'Sign Out']/preceding-sibling::span") 
	private WebElement role;
	
	public WebElement getWorkedBy() {
		return workedBy;
	}

	public WebElement getRole() {
		return role;
	}
	
	
	
	
	
	public MenuMaster navigateTo_Master()
	{
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		
		utils.moveMousePointerTo(lnkMaster);
		return new MenuMaster();
	}
	
	public class MenuMaster
	{		
		public void navigateTo_Insurance() 
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(lnkInsurances)).click();
		}
			
		public void navigateTo_UsersAndRole()
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(lnkUserAndRoles)).click();
		}
		
		public void navigateTo_AllocationRule()
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(lnkAllocationRule)).click();
		}
		
		public void navigateTo_KickOutRule()
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(lnkKickoutRule)).click();
		}
	}
	
	
	
	public MenuDashboard navigateTo_Dashboard()
	{
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		utils.moveMousePointerTo(lnkDashBoard);
		
		return new MenuDashboard();
	}
	
	public class MenuDashboard
	{
		public void navigateTo_PrintAndMail_Queue()
		{
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(lnkPrintMailQueue)).click();
		}
		
		public void navigateTo_UserWorkedAccounts_Dashboard()
		{
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(lnkUserWorkedAccounts_Dashboard)).click();
		}
		
		public void navigateTo_ClientPriorityTracking_Dashhboard()
		{
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(lnkClientPriorityTracking_Dashboard)).click();
		}
		
		public void navigateTo_Appeals_PercentOfResolution()
		{
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(lnkAppealsResolution)).click();
		}
		
		public void navigateTo_OutStandingClaims()
		{
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(lnkOutStandingClaims)).click();
		}
		
		public void navigateTo_ProviderCredential()
		{
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(lnkProviderCredential)).click();
		}
		
		public void navigateTo_KickOutReport()
		{
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(lnkKickoutReport)).click();
		}		
		
	}
	
	
	
	
	
	
	public MenuHistoricalReports navigateTo_HistoricalReport()
	{
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		
		utils.moveMousePointerTo(lnkHistoricalReport);
		utils.wait(1000);
		
		return new MenuHistoricalReports();
	}
	
	public class MenuHistoricalReports
	{
		public void navigateTo_MyProductionDetails()
		{						
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(lnkMyProductionDetails)).click();
		}
		
		public void navigateTo_User_Worked_Accounts()
		{
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(lnkUserWorkedAccounts)).click();
		}
		
		public void navigateTo_QCWorkedAccounts_Report()
		{
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(lnkQCWorkedAccounts)).click();
		}
		
		public void navigateTo_AccountResolveType_Report()
		{
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(lnkAccountResolveType)).click();
			utils.wait_Until_InvisibilityOf_LoadingScreen();
		}
		
		public void navigateTo_AgentProduction_Report()
		{
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(lnkAgentProduction)).click();
			utils.wait_Until_InvisibilityOf_LoadingScreen();
		}
		
		public void navigateTo_CodingAndPaymentQueue_Report()
		{
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(lnkCodingAndPaymentQueueReport)).click();
			utils.wait_Until_InvisibilityOf_LoadingScreen();
		}
		
		public void navigateTo_Production_Report()
		{
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(lnkProductionReport)).click();
			utils.wait_Until_InvisibilityOf_LoadingScreen();
		}
		
		public void navigateTo_UserQuality_Report()
		{
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(lnkUserQualityReport)).click();
			utils.wait_Until_InvisibilityOf_LoadingScreen();
		}
		
		public void navigateTo_FirstPassPayment_Report()
		{
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(lnkFirstPassPaymentReport)).click();
			utils.wait_Until_InvisibilityOf_LoadingScreen();
		}
		
		public void navigateTo_ErrorParetoAgent_Report()
		{
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(lnkErrorParetoAgent)).click();
			utils.wait_Until_InvisibilityOf_LoadingScreen();
		}
		public void navigateTo_120Report()
		{
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(lnk120Report)).click();
			utils.wait_Until_InvisibilityOf_LoadingScreen();
		}
		public void navigateTo_RevenuePerVisit_Report()
		{
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(lnkRevenuePerVisit)).click();
			utils.wait_Until_InvisibilityOf_LoadingScreen();
		}
		public void navigateTo_GrossAndNetCollection_Report()
		{
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(lnkGrossNetCollection)).click();
			utils.wait_Until_InvisibilityOf_LoadingScreen();
		}
		
		public void navigateTo_ClientPriorityTracking_Report()
		{
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(lnkClientPriorityTracking_Report)).click();
			utils.wait_Until_InvisibilityOf_LoadingScreen();
		}
		
		public void navigateTo_MEOBProduction_Report()
		{
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(lnkMEOBProduction_Report)).click();
			utils.wait_Until_InvisibilityOf_LoadingScreen();
		}

		public void navigateTo_lnkErrorCategory_Report()
		{
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(lnkErrorCategory)).click();
		}
	}
	
	
	
	
	public MenuTransaction navigateTo_Transaction()
	{
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		utils.moveMousePointerTo(lnkTransaction);
		utils.wait(500);
		return new MenuTransaction();
	}
	
	public class MenuTransaction
	{
		public void navigateTo_Inbox()
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(lnkInbox)).click();
		}
		
		public void navigateTo_ClaimTracer()
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(lnkClaimTracer)).click();
		}
		
		public void navigateTo_Coding_Inbox()
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			
			long startTime = System.currentTimeMillis();
			
			lnkCodingInbox.click();
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			
			long endTime = System.currentTimeMillis();
			
			Configuration.logger.log(LogStatus.INFO, "Time to load coding inbox: " + (endTime - startTime)/1000 + " seconds");
			System.out.println("Time to load coding inbox: " + (endTime - startTime)/1000 + " sec");
			
			
			//new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(lnkCodingInbox)).click();
		}
		
		public void navigateTo_PrintAndMail_Inbox()
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(lnkPrintAndMailInbox)).click();
		}
		
		public void navigateTo_Payment_Inbox()
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			long startTime = System.currentTimeMillis();
			
			
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(lnkPaymentInbox)).click();			
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			
			long endTime = System.currentTimeMillis();
			
			Configuration.logger.log(LogStatus.INFO, "Time to load payment inbox: " + (endTime - startTime)/1000 + " seconds");
			System.out.println("Time to load payment inbox: " + (endTime - startTime)/1000 + " sec");
		}
		
		public void navigateTo_MissingEOB() 
		{			
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			new WebDriverWait(Configuration.driver, 20).until(ExpectedConditions.visibilityOf(lnkMissingEOBS)).click();
			
		}

		public void navigateTo_MissingEOBInbox() 
		{			
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			new WebDriverWait(Configuration.driver, 20).until(ExpectedConditions.visibilityOf(lnkMissingEOBInbox)).click();
			
		}
		
		public void navigateTo_Review_And_Approve()
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(lnkReviewAndApprove)).click();
		}
		
		public void navigateTo_Feedback() 
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(lnkFeedBack)).click();		
		}
		
		public void navigateTo_Correspondance_Inbox() 
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(lnkCorrespondanceInbox)).click();	
		}
		
		public void navigateTo_Rebuttal_Review() 
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			new WebDriverWait(Configuration.driver, 20).until(ExpectedConditions.visibilityOf(lnkRebuttalReview)).click();
		}
	}
	
		
	
	
	
	public void signOut()
	{
		try 
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();

			if (utils.isElementPresent(lnkSignOut)) 
			{
				new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(lnkSignOut)).click();
				Configuration.logger.log(LogStatus.INFO, "Sign Out");
			}
		} 
		catch (Exception e)
		{
			Configuration.logger.log(LogStatus.INFO, "Mehtod: signOut <br>" + e.getStackTrace());
		}
		
	}

	
}
