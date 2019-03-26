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
import customDataType.Info;
import customDataType.Transaction;
import pageObjectRepository.Dashboard;
import pageObjectRepository.Login;
import pageObjectRepository.historicalReports.UserWorkedAccountsReport;
import pageObjectRepository.transaction.AnalystInbox;
import pageObjectRepository.transaction.ClaimTransaction;
import pageObjectRepository.transaction.CorrespondanceInbox;
import pageObjectRepository.transaction.PaymentInbox;

public class Correspondance extends Configuration
{
	WebDriverUtils utils = new WebDriverUtils();
	
	Login objLogin;
	AnalystInbox objInbox;
	ClaimTransaction objClaimTransaction;	
	Dashboard objDashboard;
	CorrespondanceInbox objCorrespondance;
	PaymentInbox objPaymentInbox;
	UserWorkedAccountsReport objUserWorkedAccountsReport;
	
	
	@Test(groups= {"REG"})
	void verify_Correspondance()
	{
		logger = extent.startTest("Search and save account from Correspondance login and verify in transaction history");
		
		int executableRowIndex = excel.isExecutable("verify_Correspondance");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		
		String practice = excel.readValue(sheet, executableRowIndex, "practice");		
		String nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue1");		
		HashMap<String, String> data = excel.get_TransactionData(nextQueue);
		
		String claimStatus = data.get("claim_status");
		String actionCode = data.get("action_code");
		String resolveType = data.get("resolve_type"); 
		String notes = data.get("note");
		String queueName = data.get("queueName");
		
		LinkedList<HashMap<String, String>> history = new LinkedList<>();  //payment
		
		objLogin = PageFactory.initElements(Configuration.driver, Login.class);
		objClaimTransaction = PageFactory.initElements(Configuration.driver, ClaimTransaction.class);
		objInbox = PageFactory.initElements(Configuration.driver, AnalystInbox.class);
		objDashboard = PageFactory.initElements(Configuration.driver, Dashboard.class);
		objCorrespondance = PageFactory.initElements(Configuration.driver, CorrespondanceInbox.class);
		objPaymentInbox = PageFactory.initElements(driver, PaymentInbox.class);
		objUserWorkedAccountsReport = PageFactory.initElements(Configuration.driver, UserWorkedAccountsReport.class);
		
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");	

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		
		// select practice and select regular image link
		logger.log(LogStatus.INFO, "Navigate to Inbox, select practice ["+ practice +"] and click REGULAR queue");
		objInbox.select_Practice(practice); 
		objInbox.click_Image_Regular(true);
	 	
	 	objInbox.open_PatientAccount(Constants.Queues.REGULAR);				
		String sUID = objClaimTransaction.get_UID();
		String sAccountNumber = objClaimTransaction.get_PatientAccountNumber();
		
		utils.save_ScreenshotToReport("CorrespondanceClaimDetails");
		logger.log(LogStatus.INFO, "Get account details from claim transaction window,  Account ["+ sAccountNumber +"], UID ["+ sUID +"]");
		
		objClaimTransaction.close_ClaimTransaction();
					
		objDashboard.signOut();
					
					
			
		
		String column = "UID";
		String condition = "Equal to";
		String value = sUID	;
			
			
			
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.CORRESPONDANCE);
		String sCorrespondanceUsername = login.get("ntlg");
		String sCorrespondancePassword = login.get("password");	
		
		// login - Credit Balance
		objLogin.login(sCorrespondanceUsername, sCorrespondancePassword);
		
				
		//	add transaction history
		data.put("workedBy", objDashboard.getWorkedBy().getAttribute("textContent"));
		data.put("queue", "Correspondence");
		history.add(data);
		
		
		objDashboard.navigateTo_Transaction().navigateTo_Correspondance_Inbox();
		logger.log(LogStatus.INFO, "Navigate to Correspondance Inbox");
		
		objCorrespondance.add_Search_Condition(practice, column, condition, value).search(sUID);
		logger.log(LogStatus.INFO, "Search with practice ["+ practice +"], and condition ["+ column +" "+ condition +" "+ value +"]" );
		
		objCorrespondance.open_PatientAccount(sUID);
					
		// submit account to print and mail inbox
		Transaction rSubmit = objClaimTransaction.submit_Claim(sUID, claimStatus, actionCode, resolveType, notes);
							
		// assert submission status	
		Assert.assertTrue(rSubmit.getStatus(), rSubmit.getDescription());	
		logger.log(LogStatus.INFO, "<b>Account moved to Payment Inbox, "
											+ "Account [" + sAccountNumber + "], UID ["+ sUID +"]<br> <br>"
							
											+ "ClaimStatus ["+claimStatus+"], <br>"
											+ "ActionCode ["+actionCode+"], <br>"
											+ "ResolveType ["+resolveType+"], <br>"
											+ "Notes ["+notes+"]</b>");
		
		
		objDashboard.signOut();
		
		
		
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.PAYMENT);
		String sPaymentUsername = login.get("ntlg");
		String sPaymentPassword = login.get("password");	
		
		// login - Payment
		objLogin.login(sPaymentUsername, sPaymentPassword);
		
		
		String responseQueue = excel.readValue(sheet, executableRowIndex, "response");		
		data = excel.get_TransactionData(responseQueue);
		
		claimStatus = data.get("claim_status");
		actionCode = data.get("action_code");
		resolveType = data.get("resolve_type"); 
		notes = data.get("note");
		
		data.put("workedBy", objDashboard.getWorkedBy().getAttribute("textContent"));
		data.put("queue", "payment");
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
		logger.log(LogStatus.INFO, "<b>Claim moved to Payment response queue <br><br> " 
											+ "ClaimStatus ["+ claimStatus +"], <br>" 
											+ "ActionCode ["+ actionCode +"], <br>" 
											+ "ResolveType ["+ resolveType +"], <br>" 
											+ "Notes ["+ notes +"]</b>");
		
		// Sign out
 		objDashboard.signOut();
 		
		
		
 	
 		// login - Analyst
 		objLogin.login(sAnalystUsername, sAnalystPassword);
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		logger.log(LogStatus.INFO, "Navigate to Transaction -> Inbox");
		
		/* select practice and select regular image link */
		objInbox.select_Practice(practice); 
		objInbox.click_Image_Regular(true);
	 	logger.log(LogStatus.INFO, "Select practice ["+ practice +"] and click REGULAR queue icon");
	 	
	 	Info search1 = objInbox.verify_AccountRemoved(sAccountNumber, sUID, Constants.Queues.REGULAR);
	 	Assert.assertTrue(search1.getStatus(), search1.getDescription());
		logger.log(LogStatus.INFO, search1.getDescription());
	 	
	 	
	 	objInbox.click_Image_PaymentResponse();
		logger.log(LogStatus.INFO, "Navigate to PAYMENT RESPONSE queue");
		
		// verify account	
		Info search2 = objInbox.search_Account(sAccountNumber, Constants.Queues.PAYMENT_RESPONSE);
		Assert.assertTrue(search2.getStatus(), search2.getDescription());
		logger.log(LogStatus.INFO, search2.getDescription());
		
		
		//	submit account - arclosed
	 	objInbox.open_PatientAccount(Constants.Queues.PAYMENT_RESPONSE, "ACCOUNT_NUMBER", sAccountNumber);
	 	
	 	
	 	//verify transaction history
	 	SoftAssert s = objClaimTransaction.verify_TransactionHistory(history);
	 	
	 	objClaimTransaction.close_ClaimTransaction();
		s.assertAll(); 	
		
		
		logger.log(LogStatus.INFO, "<b>Transaction history verified for Correspondance role</b>");
	 	
	 	
	}
}
