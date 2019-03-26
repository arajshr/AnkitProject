package CreditBalance;

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
import pageObjectRepository.Dashboard;
import pageObjectRepository.transaction.ClaimTransaction;
import pageObjectRepository.transaction.CreditBalanceInbox;
import pageObjectRepository.transaction.PaymentInbox;

public class Payment  extends Configuration
{
	WebDriverUtils utils = new WebDriverUtils();	
	
	Login objLogin;
	CreditBalanceInbox objInbox;
	ClaimTransaction objClaimTransaction;
	PaymentInbox objPaymentInbox;
	Dashboard objDashboard; 
	UserWorkedAccountsReport objUserWorkedAccounts; 
	
	
	
	
	@Test(groups= {"REG"})
	public void verify_Payment_In_CreditBalance()
	{
		/*
		 * login as credit balance
		 * select practice and move account to payment
		 * logout 
		 * 
		 * login - payment
		 * verification
		 * submit account to payment response and verify alert message
		 * logout
		 * 
		 * login - credit balance
		 * verify in payment response queue
		 * logout
		 * 
		 */
		
		logger = extent.startTest("Verify Payment and Payment response in Credit Balance Role");
		
		int executableRowIndex = excel.isExecutable("verify_Payment_In_CreditBalance");		
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
		objInbox = PageFactory.initElements(driver, CreditBalanceInbox.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objPaymentInbox = PageFactory.initElements(driver, PaymentInbox.class);
		objUserWorkedAccounts = PageFactory.initElements(driver, UserWorkedAccountsReport.class);
		


		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.CREDIT_BALANCE);
		String sCreditUsername = login.get("ntlg");
		String sCreditPassword = login.get("password");	
		
		// login - Credit Balance
		objLogin.login(sCreditUsername, sCreditPassword);
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		logger.log(LogStatus.INFO, "Navigate to Transaction -> Inbox");
		
		// select practice and select regular image link 
		objInbox.select_Practice(practice, false); 
		objInbox.click_Image_CreditBalance();
	 	logger.log(LogStatus.INFO, "Select practice ["+ practice +"] and click CREDIT BALANCE queue icon");
	 		 	
	 	
	 	Info result = objInbox.open_PatientAccount(Constants.Queues.CREDIT_BALANCE);
	 	Assert.assertTrue(result.getStatus(), result.getDescription());
	 	
		String sUID = objClaimTransaction.get_UID();
		String sAccountNumber = objClaimTransaction.get_PatientAccountNumber();
		//logger.log(LogStatus.INFO,  "Open account [" + openAccount.getPateintAccountNumber() + "]");
		
		
		// submit account to payment
		Transaction submitAccount = objClaimTransaction.submit_Claim(sUID, claimStatus, actionCode, resolveType, notes);
				
		// assert submission status
		Assert.assertTrue(submitAccount.getStatus(), "<b>Claim not saved to Payment. Account Number ["+sAccountNumber+"], UID ["+sUID+"]</b>");
		logger.log(LogStatus.INFO, "<b>Claim moved to Payment. Account Number ["+sAccountNumber+"], UID ["+sUID+"]</b>");	
		
		// sign out from Analyst
		objDashboard.signOut();
		
				
		
		
			
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.PAYMENT);
		String sPaymentUsername = login.get("ntlg");
		String sPaymentPassword = login.get("password");	
		
		// login - Payment
		objLogin.login(sPaymentUsername, sPaymentPassword);
		
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
		
		String responseQueue = excel.readValue(sheet, executableRowIndex, "response");		
		data = excel.get_TransactionData(responseQueue);
		
		claimStatus = data.get("claim_status");
		actionCode = data.get("action_code");
		resolveType = data.get("resolve_type");
		notes = data.get("note");
		
		Assert.assertTrue(objPaymentInbox.submit_PaymentResponse(sAccountNumber, claimStatus, actionCode, notes));
		logger.log(LogStatus.INFO, "<b>Claim moved to Payment response queue</b>");
		
		// Sign out
 		objDashboard.signOut();
 		
 		
 		
 		
 			
		// login - Credit Balance
		objLogin.login(sCreditUsername, sCreditPassword);	
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();	
		
		objInbox.select_Practice(practice, false);
		objInbox.click_Image_PaymentResponse();
		logger.log(LogStatus.INFO, "Select practice [" + practice + "] and Navigate to Payment Response");
		
		// verify account	
		Info search1 = objInbox.search_Account(sAccountNumber, Constants.Queues.PAYMENT_RESPONSE);
		Assert.assertTrue(search1.getStatus(), search1.getDescription());
		utils.save_ScreenshotToReport("PaymentResponse");
		
		
		//	submit account - arclosed
	 	objInbox.open_PatientAccount(Constants.Queues.PAYMENT_RESPONSE, "ACCOUNT_NUMBER", sAccountNumber);

	 	nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue2");		
		data = excel.get_TransactionData(nextQueue);
		
		claimStatus = data.get("claim_status");
		actionCode = data.get("action_code");
		resolveType = data.get("resolve_type"); 
		notes = data.get("note");
		
	 	Transaction rSubmit = objClaimTransaction.submit_Claim(sUID, claimStatus, actionCode, resolveType, notes);	 	
	 	Assert.assertTrue(rSubmit.getStatus(), rSubmit.getDescription());
		logger.log(LogStatus.INFO, "<b>Claim moved from Payment Response to AR Closed</b>");
			
	}	
	
}






