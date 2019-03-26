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
import pageObjectRepository.Dashboard;
import pageObjectRepository.transaction.AnalystInbox;
import pageObjectRepository.transaction.ClaimTransaction;
import pageObjectRepository.transaction.ReviewAndApprove;

public class ClientEscalation extends Configuration
{
	WebDriverUtils utils = new WebDriverUtils();
	
	Login objLogin;
	AnalystInbox objAnalystInbox;
	ClaimTransaction objClaimTransaction;
	ReviewAndApprove objReviewAndApprove;	
	Dashboard objDashboard;
	
		

	
	@Test(groups= {"REG"})
	public void verify_ClientEscalation()
	{	
		logger = extent.startTest("Verify Client Escalation and Client Escalation Release");
		
		int executableRowIndex = excel.isExecutable("verify_ClientEscalation");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		
		String practice = excel.readValue(sheet, executableRowIndex, "practice");		
		String nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue1");		
		HashMap<String, String> data = excel.get_TransactionData(nextQueue);
		
		String claimStatus = data.get("claim_status");
		String actionCode = data.get("action_code");
		String resolveType = data.get("resolve_type"); 
		String notes = data.get("note");		
		
		
		
		objLogin = PageFactory.initElements(driver, Login.class);
		objClaimTransaction = PageFactory.initElements(driver, ClaimTransaction.class);
		objAnalystInbox = PageFactory.initElements(driver, AnalystInbox.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objReviewAndApprove = PageFactory.initElements(driver, ReviewAndApprove.class);
		
		
		
		
	
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");	

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		logger.log(LogStatus.INFO, "Navigate to Transaction -> Inbox");
		
		logger.log(LogStatus.INFO, "Select practice ["+ practice +"] and click REGULAR queue icon");	
		objAnalystInbox.select_Practice(practice); 
		objAnalystInbox.click_Image_Regular(true);
	 		
		
	 	
	 	//	open first available account
	 	objAnalystInbox.open_PatientAccount(Constants.Queues.REGULAR);
	 	utils.save_ScreenshotToReport("AccountInfo");
	 	
	 	String sAccountNumber = objClaimTransaction.get_PatientAccountNumber();
		String totalCharges = objClaimTransaction.get_TotalCharges();				
		String sEncounterId = objClaimTransaction.get_EncounterID();
		String sUID = objClaimTransaction.get_UID();
		
		// submit account to Client Escalation	
		Transaction submitAccount = objClaimTransaction.submit_Claim(sUID, claimStatus, actionCode, resolveType, notes);
							
		// assert submission status	
		Assert.assertTrue(submitAccount.getStatus(), submitAccount.getDescription());
		logger.log(LogStatus.INFO, "<b>Account moved to Client Escalation, "
											+ "Account [" + sAccountNumber + "], UID ["+ sUID +"]<br> <br>"
				
											+ "ClaimStatus ["+claimStatus+"], <br>"
											+ "ActionCode ["+actionCode+"], <br>"
											+ "ResolveType ["+resolveType+"], <br>"
											+ "Notes ["+notes+"]</b>");	
		
		// sign out from Analyst	
		objDashboard.signOut();
		
		
		
		
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.TL);
		String sTLUsername = login.get("ntlg");
		String sTLPassword = login.get("password");	

		// login - TL
		objLogin.login(sTLUsername, sTLPassword);		
		
		objDashboard.navigateTo_Transaction().navigateTo_Review_And_Approve();
		logger.log(LogStatus.INFO, " Navigate to Review and approve");
		objReviewAndApprove.click_Tab_ClientEscalation();
		
		// select practice	
		objReviewAndApprove.select_Practice(practice);
		logger.log(LogStatus.INFO, " Switched to Client Escalation tab and select practice ["+ practice +"]");
			
						
		// verify account			
		Info search = objReviewAndApprove.search_Account(sAccountNumber, totalCharges);		
		Assert.assertTrue(search.getStatus(), search.getDescription());
		logger.log(LogStatus.INFO, search.getDescription());
		
				
		String responseQueue = excel.readValue(sheet, executableRowIndex, "response");		
		data = excel.get_TransactionData(responseQueue);		
		String remarks = data.get("remarks");
		
		Assert.assertTrue(objReviewAndApprove.submit_ClientEscalationResponse(sEncounterId, remarks)); //,totalCharges
		
		// sign out
		objDashboard.signOut();
		
		
		
		
		
		
		
		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();	
		logger.log(LogStatus.INFO, "Navigate to Transaction -> Inbox");	
		
		objAnalystInbox.select_Practice(practice);				
		objAnalystInbox.click_Image_ClientEscalationResponse();
		logger.log(LogStatus.INFO, "Select practice ["+ practice +"] and click Client Escalation Response queue");		
		
		//	verify account presence, tlComments
		Info search1 = objAnalystInbox.search_ClientEscalationRelease(sAccountNumber, remarks);
		Assert.assertTrue(search1.getStatus(), search1.getDescription());
		logger.log(LogStatus.INFO, search1.getDescription());
		
		//	submit account - arclosed
	 	objAnalystInbox.open_PatientAccount(Constants.Queues.CLIENT_ESCALATION_RELEASE, "ACCOUNT_NUMBER", sAccountNumber);
	 	
	 	nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue2");		
		data = excel.get_TransactionData(nextQueue);
		
		claimStatus = data.get("claim_status");
		actionCode = data.get("action_code");
		resolveType = data.get("resolve_type"); 
		notes = data.get("note");
	 	
	 	Transaction rSubmit = objClaimTransaction.submit_Claim(sUID, claimStatus, actionCode, resolveType, notes);
	 	Assert.assertTrue(rSubmit.getStatus(), rSubmit.getDescription());
		logger.log(LogStatus.INFO, "<b>Account moved from Client Escalation Release to AR Closed</b>");
	
		
	}
	
	public void cancel_ClientEscalationRelease()
	{
		int executableRowIndex = excel.isExecutable("cancel_ClientEscalationRelease");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		
		String practice = excel.readValue(sheet, executableRowIndex, "practice");		
		String nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue1");		
		HashMap<String, String> data = excel.get_TransactionData(nextQueue);
		
		String claimStatus = data.get("claim_status");
		String actionCode = data.get("action_code");
		String resolveType = data.get("resolve_type"); 
		String notes = data.get("note");
		
		objLogin = PageFactory.initElements(driver, Login.class);
		objClaimTransaction = PageFactory.initElements(driver, ClaimTransaction.class);
		objAnalystInbox = PageFactory.initElements(driver, AnalystInbox.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objReviewAndApprove = PageFactory.initElements(driver, ReviewAndApprove.class);
		
		
	
		
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");	

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		logger.log(LogStatus.INFO, "Navigate to Transaction -> Inbox");
		
		//select practice and select regular image link
		objAnalystInbox.select_Practice(practice); 
		objAnalystInbox.click_Image_Regular(true);
	 	logger.log(LogStatus.INFO, "Select practice ["+ practice +"] and click REGULAR queue icon");		
		
	 	
	 	Transaction openAccount = objAnalystInbox.open_PatientAccount(Constants.Queues.REGULAR);

		Assert.assertTrue(openAccount.getStatus(), openAccount.getDescription());
		String totalCharges = objClaimTransaction.get_TotalCharges();
		String sAccountNumber = objClaimTransaction.get_PatientAccountNumber();
		String sEncounterId = objClaimTransaction.get_EncounterID();
		String sUID = objClaimTransaction.get_UID();
		
		// submit account to Client escalation	
		Transaction submitAccount = objClaimTransaction.submit_Claim(sUID, claimStatus, actionCode, resolveType, notes);
							
		// assert submission status	
		Assert.assertTrue(submitAccount.getStatus(), submitAccount.getDescription());
		logger.log(LogStatus.INFO, "<b>Account moved to Client Escalation</b>");	
		
		// sign out from Analyst	
		objDashboard.signOut();
		
		
		
		
		
		
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.MANAGER);
		String sMngrUsername = login.get("ntlg");
		String sMngrPassword = login.get("password");	

		// login - Manager
		objLogin.login(sMngrUsername, sMngrPassword);
		// navigate to review and approve	
		objDashboard.navigateTo_Transaction().navigateTo_Review_And_Approve();
		logger.log(LogStatus.INFO, " Navigate to Review and approve");
		
		// switch to client escalation tab	
		objReviewAndApprove.click_Tab_ClientEscalation();
		
		// select practice
		objReviewAndApprove.select_Practice(practice);
		logger.log(LogStatus.INFO, " Switched to Client Escalation tab and select practice ["+ practice +"]");
			
						
		// verify account		
		Info search = objReviewAndApprove.search_Account(sAccountNumber, totalCharges);		
		Assert.assertTrue(search.getStatus(), search.getDescription());		
		logger.log(LogStatus.INFO, "<b>Account " + sAccountNumber + " present at Client Escalation!!</b>"); 
		utils.save_ScreenshotToReport("ClientEscalationTab");
		
		// cancel escalation release
		objReviewAndApprove.cancel_ClientEscalation_Response(sEncounterId);
		
		search = objReviewAndApprove.search_Account(sAccountNumber, totalCharges);		
		Assert.assertTrue(search.getStatus(), search.getDescription());
		
	}
}
