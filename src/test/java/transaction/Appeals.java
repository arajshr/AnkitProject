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
import customDataType.Info;
import customDataType.Transaction;
import pageObjectRepository.Dashboard;
import pageObjectRepository.Login;
import pageObjectRepository.transaction.AnalystInbox;
import pageObjectRepository.transaction.AppealsInbox;
import pageObjectRepository.transaction.ClaimTransaction;

public class Appeals extends Configuration
{
	WebDriverUtils utils = new WebDriverUtils();
	
	Login objLogin;
	AnalystInbox objInbox;
	ClaimTransaction objClaimTransaction;
	Dashboard objDashboard;
	AppealsInbox objAppeals;
	

	@Test(groups= {"REG"})
	public void verify_Appeals()
	{
		/*
		 * login as analyst
		 * select practice and move account from regular to appeals
		 * logout 
		 * 
		 * login - appeals
		 * verification
		 * submit account to appeals response and verify alert message
		 * logout
		 * 
		 * login - analyst
		 * verify in appeals response queue
		 * logout
		 */
		
		logger = extent.startTest("Verifying Appeals and Appeals response");
		
		int executableRowIndex = excel.isExecutable("verify_Appeals");		
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
		objAppeals = PageFactory.initElements(Configuration.driver, AppealsInbox.class);
		
		
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sUsername = login.get("ntlg");
		String sPassword = login.get("password");	
		
		// login
		objLogin.login(sUsername, sPassword);
		
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
		
		logger.log(LogStatus.INFO, "<b>Account moved to Appeals Inbox, "
											+ "Account [" + sAccountNumber + "], UID ["+ sUID +"]<br> <br>"
							
											+ "ClaimStatus ["+claimStatus+"], <br>"
											+ "ActionCode ["+actionCode+"], <br>"
											+ "ResolveType ["+resolveType+"], <br>"
											+ "Notes ["+notes+"]</b>");
		
		
		// Sign Out
		objDashboard.signOut();
			
		
		
		/*String sAccountNumber = "2142121-62953";
	 	String sUID = "";*/
		
		
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.APPEALS);
		String sAppealsUsername = login.get("ntlg");
		String sAppealsPassword = login.get("password");	
		
		// login - Credit Balance
		objLogin.login(sAppealsUsername, sAppealsPassword);
		
		// Navigate to inbox	
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		
		// select practice
		logger.log(LogStatus.INFO, "Navigate to Appeal inbox and Select practice [" + practice + "]");
		objAppeals.select_Practice(practice, false);
		objAppeals.click_Image_Appeals();
		
		
		// verify account
		Info search = objAppeals.search_PatientAccountNumber(sAccountNumber);
		Assert.assertTrue(search.getStatus(), search.getDescription());
		logger.log(LogStatus.INFO, search.getDescription());
		
		
		// submit account to appeal response
		String responseQueue = excel.readValue(sheet, executableRowIndex, "response");		
		data = excel.get_TransactionData(responseQueue);
		
		claimStatus = data.get("claim_status");
		actionCode = data.get("action_code");
		resolveType = data.get("resolve_type"); 
		notes = data.get("note");
		
		Transaction result = objAppeals.submit_AppealsResponse(sAccountNumber, claimStatus, actionCode, resolveType, notes);
		Assert.assertTrue(result.getStatus(), result.getDescription());
		logger.log(LogStatus.INFO, "<b>Account " + sAccountNumber + "  moved to Appeals response queue</b>");
		
		// Sign out
 		objDashboard.signOut();
 		
 		
 		
 		// login as analyst	
		objLogin.login(sUsername, sPassword);				
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		
		objInbox.select_Practice(practice);
		objInbox.click_Image_AppealResponse();
		logger.log(LogStatus.INFO, "Select practice [" + practice + "] and Navigate to Appeals Response");
		
		
		// verify account	
		Info search1 = objInbox.search_Account(sAccountNumber, Constants.Queues.APPEALS_RESPONSE);
		Assert.assertTrue(search1.getStatus(), search1.getDescription());
		logger.log(LogStatus.INFO, search1.getDescription());
				
		
		//	submit account - arclosed
	 	objInbox.open_PatientAccount(Constants.Queues.APPEALS_RESPONSE, "ACCOUNT_NUMBER", sAccountNumber);
	 	
	 	
	 	nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue2");	
		data = excel.get_TransactionData(nextQueue);
		
		claimStatus = data.get("claim_status");
		actionCode = data.get("action_code");
		resolveType = data.get("resolve_type"); 
		notes = data.get("note");
		
	 	Transaction rSubmit = objClaimTransaction.submit_Claim(sUID, claimStatus, actionCode, resolveType, notes);	 	
	 	Assert.assertTrue(rSubmit.getStatus(), rSubmit.getDescription());
		logger.log(LogStatus.INFO, "<b>Account moved from Appeals Response to AR Closed</b>");	
	}
}
