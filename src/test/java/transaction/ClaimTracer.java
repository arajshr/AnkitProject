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
import pageObjectRepository.transaction.AnalystInbox;
import pageObjectRepository.transaction.ClaimTracerPage;
import pageObjectRepository.transaction.ClaimTransaction;
import pageObjectRepository.transaction.CodingInbox;

public class ClaimTracer extends Configuration
{
	WebDriverUtils utils = new WebDriverUtils();
	
	Login objLogin;
	AnalystInbox objInbox;
	ClaimTransaction objClaimTransaction;
	Dashboard objDashboard;
	CodingInbox objCodingInbox;
	ClaimTracerPage objClaimTracer;
	
	@Test(groups= {"REG"})
	public void verify_ClaimTracer()
	{
		logger = extent.startTest("Cliam Tracer");
		
		int executableRowIndex = excel.isExecutable("verify_ClaimTracer");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		
		String practice = excel.readValue(sheet, executableRowIndex, "practice");		
		String nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue1");		
		HashMap<String, String> data = excel.get_TransactionData(nextQueue);
		
		String claimStatus = data.get("claim_status");
		String actionCode = data.get("action_code");
		String resolveType = data.get("resolve_type"); 
		String notes = data.get("note");
		String queueName = data.get("queueName");
		
		LinkedList<HashMap<String, String>> claimTrace = new LinkedList<>();
		
		objLogin = PageFactory.initElements(driver, Login.class);
		objClaimTransaction = PageFactory.initElements(driver, ClaimTransaction.class);
		objInbox = PageFactory.initElements(driver, AnalystInbox.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objCodingInbox = PageFactory.initElements(driver, CodingInbox.class);
		objClaimTracer = PageFactory.initElements(driver, ClaimTracerPage.class);
		
		
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");	

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
		
		
		HashMap<String, String> currentQueue1 = new HashMap<>();
		currentQueue1.put("workedBy", objDashboard.getWorkedBy().getAttribute("textContent"));
		currentQueue1.put("queue", "Regular");
		claimTrace.add(currentQueue1);
		
		
		// Navigate to Inbox
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		logger.log(LogStatus.INFO, "Navigate to Transaction -> Inbox");		
	 
		// select practice and click regular queue
		logger.log(LogStatus.INFO, "Select practice as ["+ practice +"] and click Regular queue");
		objInbox.select_Practice(practice); 	
	 	objInbox.click_Image_Regular(true);	 	
	 	
		
	 	// open account
	 	Transaction openAccount = objInbox.open_PatientAccount(Constants.Queues.REGULAR);	
	 	Assert.assertTrue(openAccount.getStatus(), openAccount.getDescription());
		
		// submit account
		String sUID = objClaimTransaction.get_UID();
		String sPatientAccountNumber = objClaimTransaction.get_PatientAccountNumber();
		
		Transaction submitAccount = objClaimTransaction.submit_Claim(sUID, claimStatus, actionCode, resolveType, notes);				
		Assert.assertTrue(submitAccount.getStatus(), submitAccount.getDescription());
		logger.log(LogStatus.INFO, "<b>Account moved to Coding Inbox, Account [" + sPatientAccountNumber + "], UID ["+ sUID +"] </b>");
		
		// sign out from analyst
		objDashboard.signOut();
		
		
		
		
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.CODING);
		String sCodingUsername = login.get("ntlg");
		String sCodingPassword = login.get("password");	
		
		// login - Coding
		objLogin.login(sCodingUsername, sCodingPassword);
		
		HashMap<String, String> currentQueue2 = new HashMap<>();
 		currentQueue2.put("workedBy", objDashboard.getWorkedBy().getAttribute("textContent"));
 		currentQueue2.put("queue", queueName);
		claimTrace.add(currentQueue2);
		
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
		queueName = data.get("queueName");
		
		Assert.assertTrue(objCodingInbox.submit_CodingResponse(sPatientAccountNumber, claimStatus, actionCode, notes));
		logger.log(LogStatus.INFO, "<b>Account " + sPatientAccountNumber + "  moved to Coding response queue</b>");
		
		// Sign out
		objDashboard.signOut();
			 		
 		
 		
		
		
 		
 		
 		

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
 		
 		HashMap<String, String> currentQueue3 = new HashMap<>();
 		currentQueue3.put("workedBy", objDashboard.getWorkedBy().getAttribute("textContent"));
 		currentQueue3.put("queue", queueName);
		claimTrace.add(currentQueue3);
 		
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		
		objInbox.select_Practice(practice);
		objInbox.click_Image_CodingResponse();
		logger.log(LogStatus.INFO, "Select practice [" + practice + "] and Navigate to Coding Response");
		
		
		// verify account	
		Info search1 = objInbox.search_Account(sPatientAccountNumber, Constants.Queues.CODING_RESPONSE);
		Assert.assertTrue(search1.getStatus(), search1.getDescription());
		logger.log(LogStatus.INFO, search1.getDescription());
				
		
		//		submit account - arclosed
	 	objInbox.open_PatientAccount(Constants.Queues.CODING_RESPONSE, "ACCOUNT_NUMBER", sPatientAccountNumber);
	 	
	 	
	 	nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue2");	
		data = excel.get_TransactionData(nextQueue);
		
		claimStatus = data.get("claim_status");
		actionCode = data.get("action_code");
		resolveType = data.get("resolve_type"); 
		notes = data.get("note");
		queueName = data.get("queueName");
		
		HashMap<String, String> currentQueue4 = new HashMap<>();
		currentQueue4.put("workedBy", objDashboard.getWorkedBy().getAttribute("textContent"));
		currentQueue4.put("queue", queueName);  // AR Closed
		claimTrace.add(currentQueue4);
		
	 	Transaction rSubmit = objClaimTransaction.submit_Claim(sUID, claimStatus, actionCode, resolveType, notes);	 	
	 	Assert.assertTrue(rSubmit.getStatus(), rSubmit.getDescription());
		logger.log(LogStatus.INFO, "<b>Account moved from Coding Response  to AR Closed</b>");	
		
		
		String column = "UID";
		String condition = "Equal to";
		String value = sUID;

		objDashboard.navigateTo_Transaction().navigateTo_ClaimTracer();
		objClaimTracer.add_SearchCondition(practice, column, condition, value);
		objClaimTracer.search(value);
		
		SoftAssert s = objClaimTracer.verify_ClaimTracer(claimTrace);
		s.assertAll();
		
		logger.log(LogStatus.INFO, "<b>Account Verified</b>");
		
		
	}
}
