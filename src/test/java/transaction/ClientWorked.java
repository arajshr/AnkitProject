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
import customDataType.Info;
import customDataType.Transaction;
import pageObjectRepository.Dashboard;
import pageObjectRepository.Login;
import pageObjectRepository.qc.QCDashboard;
import pageObjectRepository.qc.QCInbox;
import pageObjectRepository.transaction.AnalystInbox;
import pageObjectRepository.transaction.ClaimTracerPage;
import pageObjectRepository.transaction.ClaimTransaction;

public class ClientWorked extends Configuration
{
	
	Login objLogin;
	AnalystInbox objInbox;
	ClaimTransaction objClaimTransaction;
	Dashboard objDashboard;
	ClaimTracerPage objClaimTracer;
	
	
	QCDashboard objQCDashboard;
	QCInbox objQCInbox;
	
	
	@Test(groups= {"REG"})
	public void verify_ClientWorked()
	{
		logger = extent.startTest("Verifying 'CLIENT WORKED' action code for Azalea practice");
		
		int executableRowIndex = excel.isExecutable("verify_ClientWorked");		
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
		objClaimTracer = PageFactory.initElements(driver, ClaimTracerPage.class);
		
		objQCDashboard = PageFactory.initElements(driver, QCDashboard.class);
		objQCInbox = PageFactory.initElements(driver, QCInbox.class);
		
		
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.CALLER);
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
		
		logger.log(LogStatus.INFO, "<b>Account saved, "
								+ "Account [" + sPatientAccountNumber + "], UID ["+ sUID +"]<br> <br>"
				
								+ "ClaimStatus ["+claimStatus+"], <br>"
								+ "ActionCode ["+actionCode+"], <br>"
								+ "ResolveType ["+resolveType+"], <br>"
								+ "Notes ["+notes+"]</b>");	
		
		
		// verify account	
		Info search1 = objInbox.search_Account(sPatientAccountNumber, Constants.Queues.REGULAR);
		Assert.assertFalse(search1.getStatus(), search1.getDescription());
		logger.log(LogStatus.INFO, search1.getDescription());
				
		
		
		HashMap<String, String> currentQueue4 = new HashMap<>();
		currentQueue4.put("workedBy", "");
		currentQueue4.put("queue", queueName);   //ClientWorked
		claimTrace.add(currentQueue4);
		
	 	
		
		
		String column = "UID";
		String condition = "Equal to";
		String value = sUID;

		objDashboard.navigateTo_Transaction().navigateTo_ClaimTracer();
		objClaimTracer.add_SearchCondition(practice, column, condition, value);
		objClaimTracer.search(value);
		
		SoftAssert s = objClaimTracer.verify_ClaimTracer(claimTrace);
		s.assertAll();
		
		logger.log(LogStatus.INFO, "<b>Account verified in Claim Tracer</b>");
		
		
		
		objDashboard.signOut();
		
		
		
		
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.QCA);
		String sQCAUsername = login.get("ntlg");
		String sQCAPassword = login.get("password");	

		// login - QCA
		objLogin.login(sQCAUsername, sQCAPassword);
		
		objQCDashboard.navigateTo_QCInbox();
		objQCInbox.close_Accounts_For_Audit_Popup();
		
		objQCInbox.filter_QC_Accounts(practice); 
		logger.log(LogStatus.INFO, "Navigate to QC Inbox and Select Practice ["+ practice +"]");
		
		
		// search uid
		Info search = objQCInbox.search_UID(sUID, claimStatus, actionCode);
		Assert.assertFalse(search.getStatus(), search.getDescription());
		logger.log(LogStatus.INFO, search.getDescription());
		
	}
	
	
	
	
	
	public void verify_NoOfTouchesAzalea()
	{
		/* 
		 * login - analyst
		 * select practice Azalea and regular queue
		 * 
		 * open account with current ins same as primary ins
		 * move the account to follow up
		 * 
		 * switch to follow up queue 
		 * open same account and move to follow up again
		 * 
		 * */
	}
}
