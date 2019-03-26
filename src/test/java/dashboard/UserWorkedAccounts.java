package dashboard;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.Constants;
import configuration.WebDriverUtils;
import customDataType.Transaction;
import pageObjectRepository.Dashboard;
import pageObjectRepository.Login;
import pageObjectRepository.dashboard.UserWorkedAccountsReport;
import pageObjectRepository.transaction.AnalystInbox;
import pageObjectRepository.transaction.ClaimTransaction;

public class UserWorkedAccounts extends Configuration
{
	WebDriverUtils utils = new WebDriverUtils();
	Login objLogin;
	Dashboard objDashboard;
	UserWorkedAccountsReport objReport;
	AnalystInbox objAnalystInbox;
	ClaimTransaction objClaimTransaction;
	
	
	/**
	 * login - TL
	 * get
	 * 
	 */
	@Test
	public void verifiy_UserWorkedAccounts_Dashboard()
	{
		logger = extent.startTest("Verifying User Worked Accounts report");
		
		int executableRowIndex = excel.isExecutable("verifiy_UserWorkedAccounts_Dashboard");		
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
		objReport = PageFactory.initElements(driver, UserWorkedAccountsReport.class);
		objAnalystInbox = PageFactory.initElements(driver, AnalystInbox.class);
		objClaimTransaction = PageFactory.initElements(driver, ClaimTransaction.class);
		
		
		
		
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");
		String sAnalystName = login.get("name");	

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		logger.log(LogStatus.INFO, "Navigate to Transaction -> Inbox");
				
		objAnalystInbox.select_Practice(practice); 
		objAnalystInbox.click_Image_Regular(true);
	 	logger.log(LogStatus.INFO, "Select practice ["+ practice +"] and click REGULAR queue icon");		
				
	 	objAnalystInbox.open_PatientAccount(Constants.Queues.REGULAR);
		String sAccountNumber = objClaimTransaction.get_PatientAccountNumber();
		String sUID = objClaimTransaction.get_UID();
				
		Transaction submitAccount = objClaimTransaction.submit_Claim(sUID, claimStatus, actionCode, resolveType, notes);
		Assert.assertTrue(submitAccount.getStatus(), "Account [" + sAccountNumber +"] not submitted!!");
		logger.log(LogStatus.INFO, "<b>Account ["+ sAccountNumber +"], UID ["+ sUID +"] moved to Client Escalation</b>");
		
		/*String sAccountNumber = "20181008-604";*/
		
		String sWorkedBy = objDashboard.getWorkedBy().getAttribute("textContent");
	 	
		ArrayList<String> temp = new ArrayList<>();
		temp.add(sWorkedBy);
		temp.add(claimStatus);
		temp.add(actionCode);
				
		ArrayList<ArrayList<String>> history = new ArrayList<>();
		history.add(temp);
		
		
		objDashboard.signOut();
		
		
		
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.TL);
		String sTLUsername = login.get("ntlg");
		String sTLPassword = login.get("password");	

		// login - TL
		objLogin.login(sTLUsername, sTLPassword);		
		
		objDashboard.navigateTo_Dashboard().navigateTo_UserWorkedAccounts_Dashboard();
		logger.log(LogStatus.INFO, "Navigate to Dashboard -> User Worked Accounts");
		
				
		objReport.getReport(client, location, practice, sAnalystName);;		
		//utils.captureScreenshot("UWA_Dashboard");
		
		objReport.verifyReport(sAccountNumber, history);
		
		logger.log(LogStatus.PASS, "<b>User Worked Accounts Report verified</b>");
		
	}
}
