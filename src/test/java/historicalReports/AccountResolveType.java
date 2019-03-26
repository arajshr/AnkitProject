package historicalReports;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.Constants;
import configuration.WebDriverUtils;
import customDataType.Info;
import customDataType.Transaction;
import pageObjectRepository.Dashboard;
import pageObjectRepository.Login;
import pageObjectRepository.historicalReports.AccountResolveTypeReport;
import pageObjectRepository.transaction.AnalystInbox;
import pageObjectRepository.transaction.ClaimTransaction;

public class AccountResolveType extends Configuration
{
	WebDriverUtils utils = new WebDriverUtils();

	Login objLogin;
	Dashboard objDashboard;
	AccountResolveTypeReport objReport;
	AnalystInbox objAnalystInbox;
	ClaimTransaction objClaimTransaction;
 	
	String fromDate = new SimpleDateFormat("MM/dd/yy").format(new Date()); // -- MM/dd/yyyy
	
	@Test(groups= {"REG"})
	public void verify_AccountResolveType_Report()
	{
		/* get data from report	*/	
		/* capture resolve type and submit account	*/
		/* verify in report	*/
		
		

		int executableRowIndex = excel.isExecutable("verify_AccountResolveType_Report");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		String client = excel.readValue(sheet, executableRowIndex, "client");
		String location = excel.readValue(sheet, executableRowIndex, "location");
		String practice = excel.readValue(sheet, executableRowIndex, "practice");
		
		String nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue1");		
		HashMap<String, String> data = excel.get_TransactionData(nextQueue);
		
		String claimStatus = data.get("claim_status");
		String actionCode = data.get("action_code");
		String resolveType = data.get("resolve_type"); 
		String notes = data.get("note");
		
		
		
		objLogin = PageFactory.initElements(driver, Login.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objReport = PageFactory.initElements(driver, AccountResolveTypeReport.class);
		objAnalystInbox = PageFactory.initElements(driver, AnalystInbox.class);
		objClaimTransaction = PageFactory.initElements(driver, ClaimTransaction.class);
		


		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.TL);
		String sTLUsername = login.get("ntlg");
		String sTLPassword = login.get("password");	

		// login - TL
		objLogin.login(sTLUsername, sTLPassword);
		
		objDashboard.navigateTo_HistoricalReport().navigateTo_AccountResolveType_Report();
		logger.log(LogStatus.INFO, "Navigate to Account Resolve Type report");
		
		objReport.select_Client(client)		//.select_FromDate(fromDate)
				.select_Location(location)
				.select_Practice(practice)
				.get_Report();
		
		logger.log(LogStatus.INFO, "Set filter values as Client ["+ client +"], Location ["+ location +"], Practice ["+ practice +"] and From Date ["+ fromDate +"]");
		
		
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");
		
		
		String user = sAnalystUsername;
		int expTotal = 0, expCalled = 0, expWeb = 0, expSelfResolved = 0;		
		int actTotal = 0, actCalled = 0, actWeb = 0, actSelfResolved = 0;
		
		Info result = objReport.verify_User(user);
		
		if(result.getStatus())
		{
			expTotal = Integer.parseInt(objReport.get_Total_Worked_Accounts(user));
			expCalled = Integer.parseInt(objReport.get_Called_Accounts(user));
			expWeb = Integer.parseInt(objReport.get_Web_Accounts(user));
			expSelfResolved = Integer.parseInt(objReport.get_SelfResolved_Worked_Accounts(user));
			
			/*actCalled = expCalled;
			actWeb = expWeb;
			actSelfResolved = expSelfResolved;*/
		}
		
		utils.save_ScreenshotToReport("AccountResolveTypeReport");
		objDashboard.signOut();
		
		
			

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);	
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		objAnalystInbox.select_Practice(practice);
		objAnalystInbox.click_Image_Regular(true);
		
		Transaction openAccount = objAnalystInbox.open_PatientAccount(Constants.Queues.REGULAR);
		
		if(openAccount.getStatus())
		{
			String sAccountNumber = objClaimTransaction.get_PatientAccountNumber();
			String sUID = objClaimTransaction.get_UID();
			Transaction submitAccount = null;
			
			
			if(resolveType == null)
			{
				submitAccount = objClaimTransaction.submit_Claim(claimStatus, notes);
				expCalled += 1; 
				
			}
			else if(resolveType.equals("Website") || resolveType.equals("Self Resolved")) 
			{
				submitAccount = objClaimTransaction.submit_Claim(sUID, claimStatus, actionCode, resolveType, notes);
								
				expWeb = resolveType.equals("WebSite") ? expWeb += 1 : expWeb; 
				expSelfResolved = resolveType.equals("Self Resolved") ? expSelfResolved += 1 : expSelfResolved;
			}
			
			
			Assert.assertTrue(submitAccount.getStatus(), "Account [" + sAccountNumber +"] not submitted");
			logger.log(LogStatus.INFO, "<b>Claim saved. Account [" + sAccountNumber +"], UID [" + sUID + "]</b>");
						
			expTotal += 1;
			
			objDashboard.signOut();
			
			
		
			// login - TL
			objLogin.login(sTLUsername, sTLPassword);
			objDashboard.navigateTo_HistoricalReport().navigateTo_AccountResolveType_Report();
			logger.log(LogStatus.INFO, "Navigate to Account Resolve Type report");
			
			objReport.select_Client(client)
					.select_Location(location)
					.select_Practice(practice)
					.get_Report();
			
			
			actTotal = Integer.parseInt(objReport.get_Total_Worked_Accounts(user));
			actCalled = Integer.parseInt(objReport.get_Called_Accounts(user));
			actWeb = Integer.parseInt(objReport.get_Web_Accounts(user));
			actSelfResolved = Integer.parseInt(objReport.get_SelfResolved_Worked_Accounts(user));
			
			
			utils.save_ScreenshotToReport("AccountResolveTypeReport");
			
			SoftAssert s = new SoftAssert();
			s.assertEquals(actTotal, expTotal, "Toatl Worked Accounts do not match.. expected ["+ expTotal +"] but found ["+ actTotal +"]<br>");			
			s.assertEquals(actCalled, expCalled, "Called Accounts do not match.. expected ["+ expCalled +"] but found ["+ actCalled +"]<br>");
			s.assertEquals(actSelfResolved, expSelfResolved, "Self Resolved Accounts do not match.. expected ["+ expSelfResolved +"] but found ["+ actSelfResolved +"]<br>");
			s.assertEquals(actWeb, expWeb, "Website Accounts do not match.. expected ["+ expWeb +"] but found ["+ actWeb +"]<br>");
			
			s.assertAll();
			
		}
		else
		{
			logger.log(LogStatus.INFO, openAccount.getDescription());
		}

	}
}
