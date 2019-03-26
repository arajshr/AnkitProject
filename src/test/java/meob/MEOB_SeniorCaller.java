package meob;

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
import pageObjectRepository.historicalReports.MEOB_ProductionReport;
import pageObjectRepository.transaction.ClaimTransaction;
import pageObjectRepository.transaction.ClaimTransactionMEOB;
import pageObjectRepository.transaction.MissingEOBInbox;
import pageObjectRepository.transaction.MissisngEOBS;
import pageObjectRepository.transaction.ReviewAndApprove;
import pageObjectRepository.transaction.SeniorCallerInbox;

public class MEOB_SeniorCaller extends Configuration
{
	WebDriverUtils utils=new WebDriverUtils();
	
	Login objLogin;
	Dashboard objDashboard;
	MissisngEOBS  objMissisngEOBS;
	SeniorCallerInbox objInbox;
	ClaimTransactionMEOB objClaimTransactionMEOB;
	ClaimTransaction objClaimTransaction;
	MissingEOBInbox objMEOBInbox;
	ReviewAndApprove objReviewAndApprove;	
	MEOB_ProductionReport objReport;
	
	@Test(enabled = true, priority = 1)
	public void verify_PaymentPoster() 
	{
		logger = extent.startTest("Verifying Payment Poster and Payment Poster response for MEOB");
		
		int executableRowIndex=excel.isExecutable("verify_PaymentPoster");
		XSSFSheet sheet=excel.setSheet("TestData");
		
		String client = excel.readValue(sheet, executableRowIndex, "client");
		String location = excel.readValue(sheet, executableRowIndex, "location");
		String practice = excel.readValue(sheet, executableRowIndex, "practice");
		
		String nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue1");		
		HashMap<String, String> data = excel.get_TransactionData(nextQueue);
		
		String claimStatus = data.get("claim_status");
		String actionCode = data.get("action_code");
		String resolveType = data.get("resolve_type"); 
		String notes = data.get("note");
		String callerComments = data.get("comments");
		
		
		String testId=excel.readValue(sheet, executableRowIndex, "meob");
		HashMap<String, String> data1=excel.get_MEOBData(testId);		
		
		String sPayerName = data1.get("Payer_Name");
		String sModeOfPayment = data1.get("Mode_Payment");
		String sEFTCheck = data1.get("EFT/Check");
		String sTotalPaidAmount = data1.get("Total_Paid");
		String sPendingAmount = data1.get("Pending_Amount");
		String sFileName = data1.get("File_Name");
		String sFileLocation = data1.get("File_Location");
		String sRemarks = data1.get("Remarks");
		
		
		objLogin = PageFactory.initElements(driver, Login.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objMissisngEOBS = PageFactory.initElements(driver, MissisngEOBS.class);
		objInbox = PageFactory.initElements(driver, SeniorCallerInbox.class);
		objClaimTransaction = PageFactory.initElements(driver, ClaimTransaction.class);
		objClaimTransactionMEOB = PageFactory.initElements(driver, ClaimTransactionMEOB.class);
		objMEOBInbox = PageFactory.initElements(driver, MissingEOBInbox.class);
		objReport = PageFactory.initElements(driver, MEOB_ProductionReport.class);
		
		
		// login as payment to create MEOB		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.PAYMENT);
		String sPaymentUsername = login.get("ntlg");
		String sPaymentPassword = login.get("password");	
		
		// login - Payment
		objLogin.login(sPaymentUsername, sPaymentPassword);
					
		objDashboard.navigateTo_Transaction().navigateTo_MissingEOB();
		logger.log(LogStatus.INFO, "Navigate to Missing EOB and select practice");
		
		objMissisngEOBS.select_Practice(practice,false);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		
		
		//Create MEOB		
		objMissisngEOBS.set_PayerName(sPayerName)
						.set_ModeofPayment(sModeOfPayment)
						.set_EFTCheck(sEFTCheck)
						.set_TotalPaidAmount(sTotalPaidAmount)
						.set_PendingAmounttobePosted(sPendingAmount)
						.set_FileNameoftheCheckcopy(sFileName)
						.set_FileLocationoftheCheckcopy(sFileLocation)
						.set_Remarks(sRemarks);
				
				
		logger.log(LogStatus.INFO, "Set values as <br> Payer Name ["+ sPayerName +"], Mode Of Payment ["+ sModeOfPayment +"], EFT Check ["+ sEFTCheck +"], Total Paid Amount ["+ sTotalPaidAmount +"], "
								+ "Pending AmountTo Be Posted ["+ sPendingAmount +"], File Name Of Check Copy ["+ sFileName +"], File Location Of Check Copy ["+ sFileLocation +"], "
								+ "Remarks ["+ sRemarks +"]");		
		
		
		// create meob entry
		Info createMEOB = objMissisngEOBS.add_meob();
				
		// assert		
		Assert.assertTrue(createMEOB.getStatus(), createMEOB.getDescription());
		
		// get inventory id if saved successfully
		String sInventoryId = createMEOB.getDescription();
				
		logger.log(LogStatus.INFO, "<b>MEOB Entry created. Inventory ID [" + sInventoryId + "]</b>");
				
		// sign out
		objDashboard.signOut();
			
		
		//String sInventoryId = "2174";
		
		
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.CALLER);
		String sSrUsername = login.get("ntlg");
		String sSrPassword = login.get("password");	
		
		// login as Sr caller
		objLogin.login(sSrUsername, sSrPassword);
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		logger.log(LogStatus.INFO, "Navigate to Inbox, Select practice and click MEOB queue");
		
		objInbox.select_MEOBPractice(practice, false);
		objInbox.click_image_MEOB();
		
		// open account
		objInbox.open_PatientAccount(Constants.Queues.MEOB, "INVENTORYID", sInventoryId);
		
		// move claim to payment poster
		Transaction result = objClaimTransactionMEOB.submit_MEOB(claimStatus, actionCode, resolveType, notes, sPayerName, sFileName, sFileLocation, callerComments, "");
		Assert.assertTrue(result.getStatus(), result.getDescription());
		logger.log(LogStatus.INFO, "<b>MEOB Entry moved to Payment[Caller Response queue]</b>");
		
		// sign out
		objDashboard.signOut();
		
		

				
		// login - Payment
		objLogin.login(sPaymentUsername, sPaymentPassword);
		
		objDashboard.navigateTo_Transaction().navigateTo_MissingEOBInbox();
		logger.log(LogStatus.INFO, "Navigate to Inbox");
		
		objMEOBInbox.select_Practice(practice, false);
		objMEOBInbox.click_Image_CallerRespobnse();
		logger.log(LogStatus.INFO, "Select practice and click Caller Response queue");
		
		// open claim
		objMEOBInbox.open_PatientAccount(Constants.Queues.MEOB_CALLER_RESPONSE, "INVENTORYID", sInventoryId);
		
		
		nextQueue = excel.readValue(sheet, executableRowIndex, "response");		
		data = excel.get_TransactionData(nextQueue);
		
		claimStatus = data.get("claim_status");
		actionCode = data.get("action_code");
		resolveType = data.get("resolve_type"); 
		notes = data.get("note");
						
		// move to caller
		result = objMEOBInbox.submit_MEOB(claimStatus, actionCode, resolveType, notes, "");
		Assert.assertTrue(result.getStatus(), result.getDescription());
		logger.log(LogStatus.INFO, "<b>MEOB Entry moved to Caller[MEOB_PP_Response queue]</b>");
		
		// sign out
		objDashboard.signOut();
		
		
		
				
		
		// login as Sr caller
		objLogin.login(sSrUsername, sSrPassword);
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		logger.log(LogStatus.INFO, "Navigate to Inbox");
		
		objInbox.select_MEOBPractice(practice, false);
		objInbox.click_image_MEOBPPResponse();
		logger.log(LogStatus.INFO, "Select practice and click MEOB_PP_Response queue");
		
		// open account
		objInbox.open_PatientAccount(Constants.Queues.MEOB_PP_RESPONSE, "INVENTORYID", sInventoryId);
		
		// move claim to payment poster
		
		nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue1");	 // submit back to payment poster	
		data = excel.get_TransactionData(nextQueue);
		
		claimStatus = data.get("claim_status");
		actionCode = data.get("action_code");
		resolveType = data.get("resolve_type"); 
		notes = data.get("note");
		
		result = objClaimTransactionMEOB.submit_MEOB(claimStatus, actionCode, resolveType, notes, sPayerName, sFileName, sFileLocation, callerComments, "");
		Assert.assertTrue(result.getStatus(), result.getDescription());
		logger.log(LogStatus.INFO, "<b>MEOB Entry moved to Payment[Caller Response queue]</b>");
		
		// sign out
		objDashboard.signOut();
		
		
		
		
		// login - Payment
		objLogin.login(sPaymentUsername, sPaymentPassword);	
		
		objDashboard.navigateTo_Transaction().navigateTo_MissingEOBInbox();
		logger.log(LogStatus.INFO, "Navigate to Inbox");
		
		objMEOBInbox.select_Practice(practice, false);
		objMEOBInbox.click_Image_CallerRespobnse();
		logger.log(LogStatus.INFO, "Select practice and click Caller Response queue");
		
		// open claim
		objMEOBInbox.open_PatientAccount(Constants.Queues.MEOB_CALLER_RESPONSE, "INVENTORYID", sInventoryId);
		
		
		// close payment
		nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue2");		
		data = excel.get_TransactionData(nextQueue);
		
		claimStatus = data.get("claim_status");
		actionCode = data.get("action_code");
		resolveType = data.get("resolve_type"); 
		notes = data.get("note");
						
		result = objMEOBInbox.submit_MEOB(claimStatus, actionCode, resolveType, notes, "");
		Assert.assertTrue(result.getStatus(), result.getDescription());
		logger.log(LogStatus.INFO, "<b>MEOB Entry moved to Payment Closed</b>");
		
		// sign out
		objDashboard.signOut();
				
				
		
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.MANAGER);
		String sMngrUsername = login.get("ntlg");
		String sMngrPassword = login.get("password");	

		// login - Manager
		objLogin.login(sMngrUsername, sMngrPassword);
		objDashboard.navigateTo_HistoricalReport().navigateTo_MEOBProduction_Report();
				
		// verify
		objReport.getReport(client, location, practice, ""); 
		objReport.verifyReport(sInventoryId);
	}
	
	
	@Test(enabled = true, priority = 2)
	public void verify_MEOB_ClientEscalation() 
	{
		logger = extent.startTest("Verifying Client Escalation and Client Escalation response for MEOB");
		
		int executableRowIndex=excel.isExecutable("verify_MEOB_ClientEscalation");
		XSSFSheet sheet=excel.setSheet("TestData");
		
		String practice=excel.readValue(sheet, executableRowIndex, "practice");
		
		String nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue1");		
		HashMap<String, String> data = excel.get_TransactionData(nextQueue);
		
		String claimStatus = data.get("claim_status");
		String actionCode = data.get("action_code");
		String resolveType = data.get("resolve_type"); 
		String notes = data.get("note");
		String callerComments = data.get("comments");
		
		
		String testId=excel.readValue(sheet, executableRowIndex, "meob");
		HashMap<String, String> data1=excel.get_MEOBData(testId);		
		
		String sPayerName = data1.get("Payer_Name");
		String sModeOfPayment = data1.get("Mode_Payment");
		String sEFTCheck = data1.get("EFT/Check");
		String sTotalPaidAmount = data1.get("Total_Paid");
		String sPendingAmount = data1.get("Pending_Amount");
		String sFileName = data1.get("File_Name");
		String sFileLocation = data1.get("File_Location");
		String sRemarks = data1.get("Remarks");
		
		
		objLogin = PageFactory.initElements(driver, Login.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objMissisngEOBS = PageFactory.initElements(driver, MissisngEOBS.class);
		objInbox = PageFactory.initElements(driver, SeniorCallerInbox.class);
		objClaimTransaction = PageFactory.initElements(driver, ClaimTransaction.class);
		objClaimTransactionMEOB = PageFactory.initElements(driver, ClaimTransactionMEOB.class);
		objMEOBInbox = PageFactory.initElements(driver, MissingEOBInbox.class);
		objReviewAndApprove = PageFactory.initElements(driver, ReviewAndApprove.class);
		
		
		// login as payment to create MEOB		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.PAYMENT);
		String sPaymentUsername = login.get("ntlg");
		String sPaymentPassword = login.get("password");	
		
		// login - Payment
		objLogin.login(sPaymentUsername, sPaymentPassword);
					
		objDashboard.navigateTo_Transaction().navigateTo_MissingEOB();
		logger.log(LogStatus.INFO, "Navigate to Missing EOB and select practice");
		
		objMissisngEOBS.select_Practice(practice,false);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		
		
		//Create MEOB		
		objMissisngEOBS.set_PayerName(sPayerName)
						.set_ModeofPayment(sModeOfPayment)
						.set_EFTCheck(sEFTCheck)
						.set_TotalPaidAmount(sTotalPaidAmount)
						.set_PendingAmounttobePosted(sPendingAmount)
						.set_FileNameoftheCheckcopy(sFileName)
						.set_FileLocationoftheCheckcopy(sFileLocation)
						.set_Remarks(sRemarks);
				
				
		logger.log(LogStatus.INFO, "Set values as <br> Payer Name ["+ sPayerName +"], Mode Of Payment ["+ sModeOfPayment +"], EFT Check ["+ sEFTCheck +"], Total Paid Amount ["+ sTotalPaidAmount +"], "
								+ "Pending AmountTo Be Posted ["+ sPendingAmount +"], File Name Of Check Copy ["+ sFileName +"], File Location Of Check Copy ["+ sFileLocation +"], "
								+ "Remarks ["+ sRemarks +"]");		
		
		// assert
		Info createMEOB = objMissisngEOBS.add_meob();
		Assert.assertTrue(createMEOB.getStatus(), createMEOB.getDescription());
		logger.log(LogStatus.INFO, "<b>MEOB created</b>");
		
		// get inventory id if saved successfully
		String sInventoryId = createMEOB.getDescription();
		
		// sign out
		objDashboard.signOut();
		
		
		//String sInventoryId = "2192";
		
		
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.CALLER);
		String sSrUsername = login.get("ntlg");
		String sSrPassword = login.get("password");	
		
		// login as Sr caller
		objLogin.login(sSrUsername, sSrPassword);
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		logger.log(LogStatus.INFO, "Navigate to Inbox");
		
		objInbox.select_MEOBPractice(practice, false);
		objInbox.click_image_MEOB();
		logger.log(LogStatus.INFO, "Select practice and click MEOB queue");
		
		// open account
		objInbox.open_PatientAccount(Constants.Queues.MEOB, "INVENTORYID", sInventoryId);
		
		// move claim to payment poster
		Transaction result = objClaimTransactionMEOB.submit_MEOB(claimStatus, actionCode, resolveType, notes, sPayerName, sFileName, sFileLocation, callerComments, "");
		Assert.assertTrue(result.getStatus(), result.getDescription());
		
		// sign out
		objDashboard.signOut();
		
		
		
		
		
		
		// login - Payment
		objLogin.login(sPaymentUsername, sPaymentPassword);	
		
		objDashboard.navigateTo_Transaction().navigateTo_MissingEOBInbox();
		logger.log(LogStatus.INFO, "Navigate to Inbox");
		
		objMEOBInbox.select_Practice(practice, false);
		objMEOBInbox.click_Image_CallerRespobnse();
		logger.log(LogStatus.INFO, "Select practice and click Caller Response queue");
		
		// open claim
		objMEOBInbox.open_PatientAccount(Constants.Queues.MEOB_CALLER_RESPONSE, "INVENTORYID", sInventoryId);
		
		
		nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue2");		
		data = excel.get_TransactionData(nextQueue);
		
		claimStatus = data.get("claim_status");
		actionCode = data.get("action_code");
		resolveType = data.get("resolve_type"); 
		notes = data.get("note");
		
		// move inventory to client escalation
		result = objClaimTransactionMEOB.submit_MEOB(claimStatus, actionCode, resolveType, notes, "");
		Assert.assertTrue(result.getStatus(), result.getDescription());
				
		// sign out
		objDashboard.signOut();
		
		
		
		
		
		// login as tl	and navigate to meob client escalation
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.TL);
		String sTLUsername = login.get("ntlg");
		String sTLPassword = login.get("password");	

		// login - TL
		objLogin.login(sTLUsername, sTLPassword);		
		
		objDashboard.navigateTo_Transaction().navigateTo_Review_And_Approve();
		logger.log(LogStatus.INFO, " Navigate to Review and approve");
		objReviewAndApprove.click_Tab_MEOBClientEscalation();
		
		// select practice	
		objReviewAndApprove.select_MEOBPractice(practice);
		logger.log(LogStatus.INFO, " Switch to MEOB Client Escalation tab and select practice ["+ practice +"]");
			
						
		// verify account			
		Info search = objReviewAndApprove.search_Inventory(sInventoryId);
		Assert.assertTrue(search.getStatus(), search.getDescription());
		//utils.captureScreenshot("ClientEscalationTab");
						
		String responseQueue = excel.readValue(sheet, executableRowIndex, "response");		
		data = excel.get_TransactionData(responseQueue);		
		String sTLRemarks = data.get("remarks");
		
		// submit response
		result = objReviewAndApprove.submit_MEOBClientEscalationResponse(sInventoryId, sTLRemarks);
		Assert.assertTrue(result.getStatus(), result.getDescription());
		logger.log(LogStatus.INFO, "Navigate to Inbox");		
		
		// sign out
		objDashboard.signOut();
		
		
		
		// login - payment
		objLogin.login(sPaymentUsername, sPaymentPassword);
		
		objDashboard.navigateTo_Transaction().navigateTo_MissingEOBInbox();
		logger.log(LogStatus.INFO, "Navigate to Inbox");
		
		objMEOBInbox.select_Practice(practice, false);
		objMEOBInbox.click_Image_ClientEscalationRelease();
		logger.log(LogStatus.INFO, "Select practice and click Client Escalation Release queue");
		
		objMEOBInbox.open_PatientAccount(Constants.Queues.MEOB_CLIENTESCALATION, "INVENTORYID", sInventoryId);
		
		nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue3");		
		data = excel.get_TransactionData(nextQueue);
		
		claimStatus = data.get("claim_status");
		actionCode = data.get("action_code");
		resolveType = data.get("resolve_type"); 
		notes = data.get("note");
		
		// move inventory to client escalation
		result = objClaimTransactionMEOB.submit_MEOB(claimStatus, actionCode, resolveType, notes, "");
		Assert.assertTrue(result.getStatus(), result.getDescription());
		
	}
	
	
	@Test(enabled = true, priority = 3)
	public void verify_MEOB_SubFollowUp() 
	{
		
		logger = extent.startTest("Verifying Follow up for MEOB");
		
		int executableRowIndex=excel.isExecutable("verify_MEOB_SubFollowUp");
		XSSFSheet sheet=excel.setSheet("TestData");
		
		String practice=excel.readValue(sheet, executableRowIndex, "practice");
		
		String nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue1");		
		HashMap<String, String> data = excel.get_TransactionData(nextQueue);
		
		String claimStatus = data.get("claim_status");
		String actionCode = data.get("action_code");
		String resolveType = data.get("resolve_type"); 
		String notes = data.get("note");
		String callerComments = data.get("comments");
		
		String testId=excel.readValue(sheet, executableRowIndex, "meob");
		HashMap<String, String> data1=excel.get_MEOBData(testId);		
		
		String sPayerName = data1.get("Payer_Name");
		String sModeOfPayment = data1.get("Mode_Payment");
		String sEFTCheck = data1.get("EFT/Check");
		String sTotalPaidAmount = data1.get("Total_Paid");
		String sPendingAmount = data1.get("Pending_Amount");
		String sFileName = data1.get("File_Name");
		String sFileLocation = data1.get("File_Location");
		String sRemarks = data1.get("Remarks");
		
		String followUpDate = utils.getNextDay();
		
		objLogin = PageFactory.initElements(driver, Login.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objMissisngEOBS = PageFactory.initElements(driver, MissisngEOBS.class);
		objInbox = PageFactory.initElements(driver, SeniorCallerInbox.class);
		objClaimTransaction = PageFactory.initElements(driver, ClaimTransaction.class);
		objClaimTransactionMEOB = PageFactory.initElements(driver, ClaimTransactionMEOB.class);
		objMEOBInbox = PageFactory.initElements(driver, MissingEOBInbox.class);
		
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.PAYMENT);
		String sPaymentUsername = login.get("ntlg");
		String sPaymentPassword = login.get("password");
				
		// login as payment to create MEOB		
		objLogin.login(sPaymentUsername, sPaymentPassword);
					
		objDashboard.navigateTo_Transaction().navigateTo_MissingEOB();
		logger.log(LogStatus.INFO, "Navigate to Missing EOB and select practice");
		
		objMissisngEOBS.select_Practice(practice,false);
		
		
		//Create MEOB		
		objMissisngEOBS.set_PayerName(sPayerName)
						.set_ModeofPayment(sModeOfPayment)
						.set_EFTCheck(sEFTCheck)
						.set_TotalPaidAmount(sTotalPaidAmount)
						.set_PendingAmounttobePosted(sPendingAmount)
						.set_FileNameoftheCheckcopy(sFileName)
						.set_FileLocationoftheCheckcopy(sFileLocation)
						.set_Remarks(sRemarks);
				
				
		logger.log(LogStatus.INFO, "Set values as <br> Payer Name ["+ sPayerName +"], Mode Of Payment ["+ sModeOfPayment +"], EFT Check ["+ sEFTCheck +"], Total Paid Amount ["+ sTotalPaidAmount +"], "
								+ "Pending AmountTo Be Posted ["+ sPendingAmount +"], File Name Of Check Copy ["+ sFileName +"], File Location Of Check Copy ["+ sFileLocation +"], "
								+ "Remarks ["+ sRemarks +"]");		
		
		
		// create meob entry
		Info createMEOB = objMissisngEOBS.add_meob();
				
		// assert		
		Assert.assertTrue(createMEOB.getStatus(), createMEOB.getDescription());
		
		// get inventory id if saved successfully
		String sInventoryId = createMEOB.getDescription();
				
		logger.log(LogStatus.INFO, "<b>MEOB Entry created. Inventory ID [" + sInventoryId + "]</b>");
				
		// sign out
		objDashboard.signOut();
				
		
		//String sInventoryId= "2195";
		
		
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.CALLER);
		String sSrUsername = login.get("ntlg");
		String sSrPassword = login.get("password");	
		
		// login as Sr caller
		objLogin.login(sSrUsername, sSrPassword);	
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		logger.log(LogStatus.INFO, "Navigate to Inbox");
		
		objInbox.select_MEOBPractice(practice, false);
		objInbox.click_image_MEOB();
		logger.log(LogStatus.INFO, "Select practice and click MEOB queue");
		
		// open account
		objInbox.open_PatientAccount(Constants.Queues.MEOB, "INVENTORYID", sInventoryId);
		
		// move claim to payment poster
		Transaction result = objClaimTransactionMEOB.submit_MEOB(claimStatus, actionCode, resolveType, notes, sPayerName, sFileName, sFileLocation, callerComments, followUpDate);
		Assert.assertTrue(result.getStatus(), result.getDescription());
		logger.log(LogStatus.INFO, "<b>MEOB Entry moved to FollowUp queue with Follow up date ["+ followUpDate +"]</b>");
		
		
		
		// search inventory id
		objInbox.click_image_MEOBFollowUp();
		Info search = objInbox.search_Account(sInventoryId, Constants.Queues.MEOB_FOLLOWUP);
		if(search.getStatus()) // should be false
		{
			utils.save_ScreenshotToReport("MEOBFollowUp");
			logger.log(LogStatus.FAIL, search.getDescription());
		}		
		else
		{
			logger.log(LogStatus.INFO, search.getDescription());
		}
		
		// sign out
		objDashboard.signOut();
		
		
		
		// login - payment
		objLogin.login(sPaymentUsername, sPaymentPassword);
		
		objDashboard.navigateTo_Transaction().navigateTo_MissingEOBInbox();
		logger.log(LogStatus.INFO, "Navigate to Inbox");
		
		objMEOBInbox.select_Practice(practice, false);
		objMEOBInbox.click_Image_CallerRespobnse();
		logger.log(LogStatus.INFO, "Select practice and click Caller Response queue");
		
		// search inventory
		objMEOBInbox.search_Account(sInventoryId, Constants.Queues.MEOB_CALLER_RESPONSE);
		 search = objInbox.search_Account(sInventoryId, Constants.Queues.MEOB_FOLLOWUP);
		if(search.getStatus()) // should be false
		{
			utils.save_ScreenshotToReport("MEOBFollowUp_CallerResponse");
			logger.log(LogStatus.FAIL, search.getDescription());
		}		
		else
		{
			logger.log(LogStatus.INFO, search.getDescription());
		}
	}
	
	
}
