package transaction;

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
import customDataType.Info;
import pageObjectRepository.Login;
import pageObjectRepository.historicalReports.UserWorkedAccountsReport;
import pageObjectRepository.transaction.AnalystInbox;
import pageObjectRepository.transaction.ClaimTransaction;
import pageObjectRepository.transaction.CodingInbox;
import pageObjectRepository.Dashboard;

public class Coding extends Configuration
{
	WebDriverUtils utils = new WebDriverUtils();
	
	Login objLogin;
	AnalystInbox objInbox;
	ClaimTransaction objClaimTransaction;
	Dashboard objDashboard;
	CodingInbox objCodingInbox;
	UserWorkedAccountsReport objUserWorkedAccounts;
	
	@Test(groups= {"REG"})
	public void verify_Coding()
	{
		/*
		 * login as analyst
		 * select practice and move account from regular to coding
		 * logout 
		 * 
		 * login - coding
		 * verification
		 * submit account to coding response and verify alert message
		 * logout
		 * 
		 * login - analyst
		 * verify in coding response queue
		 * logout
		 */
		
		logger = extent.startTest("Verifying Coding and Coding response");
		
		int executableRowIndex = excel.isExecutable("verify_Coding");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		
		String practice = excel.readValue(sheet, executableRowIndex, "practice");		
		String nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue1");		
		HashMap<String, String> data = excel.get_TransactionData(nextQueue);
		
		String claimStatus = data.get("claim_status");
		String actionCode = data.get("action_code");
		String resolveType = data.get("resolve_type"); 
		String notes = data.get("note");
		
		
		

 		objLogin = PageFactory.initElements(Configuration.driver, Login.class);
		objClaimTransaction = PageFactory.initElements(Configuration.driver, ClaimTransaction.class);
		objInbox = PageFactory.initElements(Configuration.driver, AnalystInbox.class);
		objDashboard = PageFactory.initElements(Configuration.driver, Dashboard.class);
		objCodingInbox = PageFactory.initElements(Configuration.driver, CodingInbox.class);
		objUserWorkedAccounts = PageFactory.initElements(Configuration.driver, UserWorkedAccountsReport.class);
		
	
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");	

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		logger.log(LogStatus.INFO, "Navigate to Transaction -> Inbox");
		
	 	//	select practice
		logger.log(LogStatus.INFO, "Select practice as ["+ practice +"] and click Regular queue");
		objInbox.select_Practice(practice); 	
	 	objInbox.click_Image_Regular(true);	 	
	 	
		
	 	//	open first available account
	 	objInbox.open_PatientAccount(Constants.Queues.REGULAR);			
	 	String sAccountNumber = objClaimTransaction.get_PatientAccountNumber();
	 	String sUID = objClaimTransaction.get_UID();
	 	
	 	//	submit and Assert
		Transaction submitAccount = objClaimTransaction.submit_Claim(sUID, claimStatus, actionCode, resolveType, notes);		
		Assert.assertTrue(submitAccount.getStatus(), submitAccount.getDescription());
		
		logger.log(LogStatus.INFO, "<b>Account moved to Coding Inbox, "
											+ "Account [" + sAccountNumber + "], UID ["+ sUID +"]<br> <br>"
							
											+ "ClaimStatus ["+claimStatus+"], <br>"
											+ "ActionCode ["+actionCode+"], <br>"
											+ "ResolveType ["+resolveType+"], <br>"
											+ "Notes ["+notes+"]</b>");
		
		
		objDashboard.signOut();
		
		
		
	 	
		
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.CODING);
		String sCodingUsername = login.get("ntlg");
		String sCodingPassword = login.get("password");	
		
		// login - Coding
		objLogin.login(sCodingUsername, sCodingPassword);
		
		// Navigate to coding inbox	
		objDashboard.navigateTo_Transaction().navigateTo_Coding_Inbox();
		
		// select practice	
		objCodingInbox.select_Practice(practice, false);
		logger.log(LogStatus.INFO, "Navigate to Coding inbox and Select practice [" + practice + "]");
		
		// verify account	
		Info search = objCodingInbox.search_UID(sUID);
		Assert.assertTrue(search.getStatus(), search.getDescription());
		logger.log(LogStatus.INFO, search.getDescription());
		
		
		// submit account to coding response 
		
		String responseQueue = excel.readValue(sheet, executableRowIndex, "response");		
		data = excel.get_TransactionData(responseQueue);
		
		claimStatus = data.get("claim_status");
		actionCode = data.get("action_code");
		resolveType = data.get("resolve_type"); 
		notes = data.get("note");
		
		Assert.assertTrue(objCodingInbox.submit_CodingResponse(sAccountNumber, claimStatus, actionCode, notes));
		logger.log(LogStatus.INFO, "<b>Account " + sAccountNumber + "  moved to Coding response queue</b>");
		
		// Sign out
 		objDashboard.signOut();
	 		
	 			 		
	 		
 	
 		// login - Analyst
 		objLogin.login(sAnalystUsername, sAnalystPassword);
 		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		
		objInbox.select_Practice(practice);
		objInbox.click_Image_CodingResponse();
		logger.log(LogStatus.INFO, "Select practice [" + practice + "] and Navigate to Coding Response");
		
		
		// verify account	
		Info search1 = objInbox.search_Account(sAccountNumber, Constants.Queues.CODING_RESPONSE);
		Assert.assertTrue(search1.getStatus(), search1.getDescription());
		logger.log(LogStatus.INFO, search1.getDescription());	
		
		//		submit account - arclosed
	 	objInbox.open_PatientAccount(Constants.Queues.CODING_RESPONSE, "ACCOUNT_NUMBER", sAccountNumber);
	 	
	 	
	 	nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue2");	
		data = excel.get_TransactionData(nextQueue);
		
		claimStatus = data.get("claim_status");
		actionCode = data.get("action_code");
		resolveType = data.get("resolve_type"); 
		notes = data.get("note");
		
	 	Transaction rSubmit = objClaimTransaction.submit_Claim(claimStatus, actionCode, resolveType, notes);	 	
	 	Assert.assertTrue(rSubmit.getStatus(), rSubmit.getDescription());
		logger.log(LogStatus.INFO, "<b>Account moved from Coding Response to AR Closed</b>");	
	
		
	}
}
