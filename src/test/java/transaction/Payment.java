package transaction;

import java.util.HashMap;
import java.util.LinkedList;

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
import pageObjectRepository.Dashboard;
import pageObjectRepository.transaction.AnalystInbox;
import pageObjectRepository.transaction.ClaimTransaction;
import pageObjectRepository.transaction.PaymentInbox;

public class Payment  extends Configuration
{
	WebDriverUtils utils = new WebDriverUtils();	
	
	Login objLogin;
	AnalystInbox objInbox;
	ClaimTransaction objClaimTransaction;
	PaymentInbox objPaymentInbox;
	Dashboard objDashboard; 
	UserWorkedAccountsReport objUserWorkedAccounts; 
	
	
	
	
	@Test(groups= {"REG"})
	public void verify_Payment()
	{
		/*
		 * login as analyst
		 * select practice and move account from regular to payment
		 * logout 
		 * 
		 * login - payment
		 * verification
		 * submit account to payment response and verify alert message
		 * logout
		 * 
		 * login - analyst
		 * verify in payment response queue
		 * logout
		 */
		
		logger = extent.startTest("Verifying Payment and Payment response");
		
		int executableRowIndex = excel.isExecutable("verify_Payment");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		
		String practice = excel.readValue(sheet, executableRowIndex, "practice");		
		String nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue1");		
		HashMap<String, String> data = excel.get_TransactionData(nextQueue);
		
		String claimStatus = data.get("claim_status");
		String actionCode = data.get("action_code");
		String resolveType = data.get("resolve_type"); 
		String notes = data.get("note");
		
		LinkedList<HashMap<String, String>> history = new LinkedList<>();
		
		
		
		objLogin = PageFactory.initElements(driver, Login.class);
		objClaimTransaction = PageFactory.initElements(driver, ClaimTransaction.class);
		objInbox = PageFactory.initElements(driver, AnalystInbox.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objPaymentInbox = PageFactory.initElements(driver, PaymentInbox.class);
		objUserWorkedAccounts = PageFactory.initElements(driver, UserWorkedAccountsReport.class);
		
		
		
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");	

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
		
		//	add transaction history
		data.put("workedBy", objDashboard.getWorkedBy().getAttribute("textContent"));
		history.add(data);
		
		
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		logger.log(LogStatus.INFO, "Navigate to Transaction -> Inbox");
		
		// select practice and select regular image link 
		logger.log(LogStatus.INFO, "Select practice ["+ practice +"] and click REGULAR queue icon");
		objInbox.select_Practice(practice); 
		objInbox.click_Image_Regular(true);
	 	
	 	
	 	objInbox.open_PatientAccount(Constants.Queues.REGULAR);
	 	
		String sUID = objClaimTransaction.get_UID();
		String sAccountNumber = objClaimTransaction.get_PatientAccountNumber();		
		
		// submit account to payment
		Transaction submitAccount = objClaimTransaction.submit_Claim(sUID, claimStatus, actionCode, resolveType, notes);		
		Assert.assertTrue(submitAccount.getStatus(), submitAccount.getDescription());
				
		logger.log(LogStatus.INFO, "<b>Account moved to Payment Inbox, "
											+ "Account [" + sAccountNumber + "], UID ["+ sUID +"]<br> <br>"
							
											+ "ClaimStatus ["+claimStatus+"], <br>"
											+ "ActionCode ["+actionCode+"], <br>"
											+ "ResolveType ["+resolveType+"], <br>"
											+ "Notes ["+notes+"]</b>");	
		
		// sign out from Analyst
		objDashboard.signOut();
		
				
		
		/*String sUID = "2142121-66546";
		String sAccountNumber = "4589811-9346";*/
		
			
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.PAYMENT);
		String sPaymentUsername = login.get("ntlg");
		String sPaymentPassword = login.get("password");	
		
		// login - Payment
		objLogin.login(sPaymentUsername, sPaymentPassword);
		
		
		//	add transaction history
		
		String responseQueue = excel.readValue(sheet, executableRowIndex, "response");		
		data = excel.get_TransactionData(responseQueue);
		
		claimStatus = data.get("claim_status");
		actionCode = data.get("action_code");
		resolveType = data.get("resolve_type"); 
		notes = data.get("note");
		
		
		data.put("workedBy", objDashboard.getWorkedBy().getAttribute("textContent"));
		history.add(data);
				
		
		
		// Navigate to payment team inbox	
		objDashboard.navigateTo_Transaction().navigateTo_Payment_Inbox();
		
		// select practice	
		objPaymentInbox.select_Practice(practice, false); 
		logger.log(LogStatus.INFO, "Navigate to Payment Inbox and Select practice [" + practice+ "]");
		
		// verify account							
		Info search = objPaymentInbox.search_UID(sUID);
		Assert.assertTrue(search.getStatus(), search.getDescription());
		logger.log(LogStatus.INFO, search.getDescription());
		
		
		// submit account to Payment response	 
		Assert.assertTrue(objPaymentInbox.submit_PaymentResponse(sAccountNumber, claimStatus, actionCode, notes));
		logger.log(LogStatus.INFO, "<b>Account moved to Payment response queue</b>");
		
		// Sign out from Manager role	
 		objDashboard.signOut();
 		
 		
 		
 		
 		// login - Analyst
 		objLogin.login(sAnalystUsername, sAnalystPassword);
 		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();	
		
		objInbox.select_Practice(practice);
		objInbox.click_Image_PaymentResponse();
		logger.log(LogStatus.INFO, "Select practice [" + practice + "] and Navigate to Payment Response");
		
		// verify account	
		Info search1 = objInbox.search_Account(sAccountNumber, Constants.Queues.PAYMENT_RESPONSE);
		Assert.assertTrue(search1.getStatus(), search1.getDescription());
		logger.log(LogStatus.INFO, search1.getDescription());
		
		
		//	submit account - arclosed
	 	objInbox.open_PatientAccount(Constants.Queues.PAYMENT_RESPONSE, "ACCOUNT_NUMBER", sAccountNumber);
	 	
	 	//verify transaction history
	 	objClaimTransaction.verify_TransactionHistory(history);
	 	
	 	
	 	nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue2");		
		data = excel.get_TransactionData(nextQueue);
		
		claimStatus = data.get("claim_status");
		actionCode = data.get("action_code");
		resolveType = data.get("resolve_type"); 
		notes = data.get("note");
		
	 	Transaction rSubmit = objClaimTransaction.submit_Claim(claimStatus, actionCode, resolveType, notes);	 	
	 	Assert.assertTrue(rSubmit.getStatus(), rSubmit.getDescription());
		logger.log(LogStatus.INFO, "<b>Account moved from Payment Response to AR Closed</b>");
			

		
	}	
	
}






