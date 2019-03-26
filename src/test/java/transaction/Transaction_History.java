	package transaction;

import java.util.HashMap;
import java.util.LinkedList;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.Constants;
import configuration.WebDriverUtils;
import customDataType.Transaction;
import customDataType.Info;
import pageObjectRepository.Dashboard;
import pageObjectRepository.Login;
import pageObjectRepository.transaction.AnalystInbox;
import pageObjectRepository.transaction.ClaimTransaction;
import pageObjectRepository.transaction.PaymentInbox;
import pageObjectRepository.transaction.ReviewAndApprove;

public class Transaction_History extends Configuration
{
	WebDriverUtils utils = new WebDriverUtils();
	
	Login objLogin;
	AnalystInbox objInbox;
	ClaimTransaction objClaimTransaction;
	Dashboard objDashboard;
	PaymentInbox objPaymentInbox;
	ReviewAndApprove objReviewAndApprove;
	
	
	@Test(groups= {"REG"})
	public void verify_Trasnsaction_History()
	{
		logger = extent.startTest("Verifying transaction history in claim transaction widow");
		
		int executableRowIndex = excel.isExecutable("verify_Trasnsaction_History");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		
		String practice = excel.readValue(sheet, executableRowIndex, "practice");	
		LinkedList<HashMap<String, String>> history = new LinkedList<>();
		
		objLogin = PageFactory.initElements(driver, Login.class);
		objClaimTransaction = PageFactory.initElements(driver, ClaimTransaction.class);
		objInbox = PageFactory.initElements(driver, AnalystInbox.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objPaymentInbox = PageFactory.initElements(driver, PaymentInbox.class);
		objReviewAndApprove = PageFactory.initElements(driver, ReviewAndApprove.class);
		
	
		
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");	

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
		
		// navigate to inbox
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		logger.log(LogStatus.INFO, "Navigate to Transaction -> Inbox");
				
		 //select practice and click regular queue
		objInbox.select_Practice(practice); 
		objInbox.click_Image_Regular(true);
	 	logger.log(LogStatus.INFO, "Select practice ["+ practice +"] and click REGULAR queue icon");		
		
	 	
	 	//	open first available account
	 	objInbox.open_PatientAccount(Constants.Queues.REGULAR);
	 	String sAccountNumber = objClaimTransaction.get_PatientAccountNumber();
		String totalCharges = objClaimTransaction.get_TotalCharges();				
		String sEncounterId = objClaimTransaction.get_EncounterID();
		String sUID = objClaimTransaction.get_UID();
		
		
		// submit account to Client Escalation		
		String nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue1");		
		HashMap<String, String> data = excel.get_TransactionData(nextQueue);
		
		String claimStatus = data.get("claim_status");
		String actionCode = data.get("action_code");
		String resolveType = data.get("resolve_type"); 
		String notes = data.get("note");
		
		Transaction submitAccount = objClaimTransaction.submit_Claim(sUID, claimStatus, actionCode, resolveType, notes);		
		
		// assert submission status	
		Assert.assertTrue(submitAccount.getStatus(), submitAccount.getDescription());
		logger.log(LogStatus.INFO, "<b>Account moved to Client Escalation, "
												+ "Account [" + sAccountNumber + "], UID ["+ sUID +"]<br> <br>"
								
												+ "ClaimStatus ["+claimStatus+"], <br>"
												+ "ActionCode ["+actionCode+"], <br>"
												+ "ResolveType ["+resolveType+"], <br>"
												+ "Notes ["+notes+"]</b>");	
		
		
		// update transaction history
		data.put("workedBy", objDashboard.getWorkedBy().getAttribute("textContent"));
		data.put("queue", "Regular");
		data.put("remarks", "");
		history.add(data);
				
		// sign out from Analyst	
		objDashboard.signOut();
		
		
		
		
		
		
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.TL);
		String sTLUsername = login.get("ntlg");
		String sTLPassword = login.get("password");	

		// login - TL
		objLogin.login(sTLUsername, sTLPassword);		
		
		// navigate to transaction -> revie and approve
		objDashboard.navigateTo_Transaction().navigateTo_Review_And_Approve();
		logger.log(LogStatus.INFO, " Navigate to Review and approve");
		
		// click client escalation tab
		objReviewAndApprove.click_Tab_ClientEscalation();
		
		// select practice	
		objReviewAndApprove.select_Practice(practice);
		logger.log(LogStatus.INFO, " Switched to Client Escalation tab and select practice ["+ practice +"]");
			
						
		// search and verify account			
		Info search = objReviewAndApprove.search_Account(sAccountNumber, totalCharges);		
		Assert.assertTrue(search.getStatus(), search.getDescription());
		//utils.captureScreenshot("ClientEscalationTab");
		
		// release client escalation
		String responseQueue = excel.readValue(sheet, executableRowIndex, "response");		
		data = excel.get_TransactionData(responseQueue);		
		String remarks = data.get("remarks");
		
		// assert
		Assert.assertTrue(objReviewAndApprove.submit_ClientEscalationResponse(sEncounterId, remarks)); //,totalCharges
				
		// sign out
		objDashboard.signOut();
		
		
		
		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
		
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();	
		logger.log(LogStatus.INFO, "Navigate to Transaction -> Inbox");	
		
		objInbox.select_Practice(practice);				
		objInbox.click_Image_ClientEscalationResponse();
		logger.log(LogStatus.INFO, "Select practice ["+ practice +"] and click Client Escalation Response queue");
		
		
		//	verify account presence, tlComments
		Info search1 = objInbox.search_ClientEscalationRelease(sAccountNumber, remarks);
		Assert.assertTrue(search1.getStatus(), search1.getDescription());
		logger.log(LogStatus.INFO, search1.getDescription());
				
		//	submit account - arclosed
	 	objInbox.open_PatientAccount(Constants.Queues.CLIENT_ESCALATION_RELEASE, "ACCOUNT_NUMBER", sAccountNumber);		
				
	 	
	 	// 	submit claim to payment inbox
	 	nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue2");		
		data = excel.get_TransactionData(nextQueue);
		
		claimStatus = data.get("claim_status");
		actionCode = data.get("action_code");
		resolveType = data.get("resolve_type"); 
		notes = data.get("note");	
		
		submitAccount = objClaimTransaction.submit_Claim(sUID, claimStatus, actionCode, resolveType, notes);
		
		// assert
		Assert.assertTrue(submitAccount.getStatus(), submitAccount.getDescription());
		logger.log(LogStatus.INFO, "<b>Account moved to Payment Inbox, "
												+ "Account [" + sAccountNumber + "], UID ["+ sUID +"]<br> <br>"
								
												+ "ClaimStatus ["+claimStatus+"], <br>"
												+ "ActionCode ["+actionCode+"], <br>"
												+ "ResolveType ["+resolveType+"], <br>"
												+ "Notes ["+notes+"]</b>");	
		
		
		// update transaction history
		data.put("workedBy", objDashboard.getWorkedBy().getAttribute("textContent"));
		data.put("queue", "clientescalation release");
		data.put("remarks", remarks);
		history.add(data);
		
		// sign out		
		objDashboard.signOut();
		
 		
 		
		
		
 		
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.PAYMENT);
		String sPaymentUsername = login.get("ntlg");
		String sPaymentPassword = login.get("password");	
		
		// login - Payment
		objLogin.login(sPaymentUsername, sPaymentPassword);
		
		// Navigate to coding inbox	
		objDashboard.navigateTo_Transaction().navigateTo_Payment_Inbox();
		
		//	select practice	
		objPaymentInbox.select_Practice(practice, false);
		logger.log(LogStatus.INFO, "Navigate to Payment inbox and Select practice [" + practice + "]");
		
		// verify account	
		Info search2 = objPaymentInbox.search_UID(sUID);
		Assert.assertTrue(search2.getStatus(), search2.getDescription());
		logger.log(LogStatus.INFO, search2.getDescription());
		
		
		// submit account to payment response		
		responseQueue = excel.readValue(sheet, executableRowIndex, "response2");		
		data = excel.get_TransactionData(responseQueue);
		
		claimStatus = data.get("claim_status");
		actionCode = data.get("action_code");
		resolveType = data.get("resolve_type"); 
		notes = data.get("note");
		
		Assert.assertTrue(objPaymentInbox.submit_PaymentResponse(sAccountNumber, claimStatus, actionCode, notes));
		logger.log(LogStatus.INFO, "<b>Account moved to Payment Response, <br> <br>"
								
												+ "ClaimStatus ["+claimStatus+"], <br>"
												+ "ActionCode ["+actionCode+"], <br>"
												+ "ResolveType ["+resolveType+"], <br>"
												+ "Notes ["+notes+"]</b>");	
		
		
		// update transaction history
		data.put("workedBy", objDashboard.getWorkedBy().getAttribute("textContent"));
		data.put("queue", "payment");
		data.put("remarks", "");
		history.add(data);
		
		
		// Sign out	
 		objDashboard.signOut();
 		
 		
 		
 		// login - Analyst
 		objLogin.login(sAnalystUsername, sAnalystPassword);
 		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		
		objInbox.select_Practice(practice);
		objInbox.click_Image_PaymentResponse();
		logger.log(LogStatus.INFO, "Select practice [" + practice + "] and Navigate to Payment Response");
		
		// verify account	
		Info search3 = objInbox.search_Account(sAccountNumber, Constants.Queues.PAYMENT_RESPONSE);
		Assert.assertTrue(search3.getStatus(), search3.getDescription());
		logger.log(LogStatus.INFO, search3.getDescription());
		
		//	submit account - needCall
	 	objInbox.open_PatientAccount(Constants.Queues.PAYMENT_RESPONSE, "ACCOUNT_NUMBER", sAccountNumber);
	 	
	 	
	 	SoftAssert s = objClaimTransaction.verify_TransactionHistory(history);
	 	objClaimTransaction.close_ClaimTransaction();
	 	
	 	s.assertAll();
	 	
	 	logger.log(LogStatus.INFO, "<b>Transaction History verified</b>");
	 	
	}
}
