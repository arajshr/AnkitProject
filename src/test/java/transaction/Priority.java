package transaction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
import pageObjectRepository.historicalReports.UserWorkedAccountsReport;
import pageObjectRepository.qc.QCA_AuditSheet;
import pageObjectRepository.qc.QCDashboard;
import pageObjectRepository.qc.QCInbox;
import pageObjectRepository.transaction.AnalystInbox;
import pageObjectRepository.transaction.ClaimTransaction;
import pageObjectRepository.transaction.ReviewAndApprove;
import winium.InventoryUpload;

public class Priority extends Configuration
{
	WebDriverUtils utils = new WebDriverUtils();	
	
	Login objLogin;
	AnalystInbox objInbox;
	ClaimTransaction objClaimTransaction;
	ReviewAndApprove objReviewAndApprove;	
	Dashboard objDashboard;
	UserWorkedAccountsReport objUserWorkedAccounts;
	
	
	QCDashboard objQCDashboard;
	QCInbox objQCInbox;
	QCA_AuditSheet objQCA_AuditSheet;
	
	
	
	@Test(enabled = false)
	public void verify_PriorityUpload()
	{
		logger = extent.startTest("Upload Priority file and verify in Priority queue");
		
		int executableRowIndex = excel.isExecutable("verify_PriorityUpload");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		String client = excel.readValue(sheet, executableRowIndex, "client");
		String location = excel.readValue(sheet, executableRowIndex, "location");
		String practice = excel.readValue(sheet, executableRowIndex, "practice");
		

		objLogin = PageFactory.initElements(driver, Login.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objInbox = PageFactory.initElements(driver, AnalystInbox.class);
		objClaimTransaction = PageFactory.initElements(driver, ClaimTransaction.class);
		
		
		
	

		// get login credentials for analyst
		HashMap<String, String>  login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sUsername = login.get("ntlg");
		String sPassword = login.get("password");
		
		//  login - analyst
		objLogin.login(sUsername, sPassword);
		
		// navigate to inbox
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		
		// select practice and click regular queue
		objInbox.select_Practice(practice, false);
		objInbox.click_Image_Regular(true);
		logger.log(LogStatus.INFO, "Navigate to Inbox and select practice ["+ practice +"] and click REGULAR queue");		
		
		
		String sPatientAccountNumber1 = objInbox.get_AccountNumber(Constants.Queues.REGULAR, 1);
		String sPatientAccountNumber2 = objInbox.get_AccountNumber(Constants.Queues.REGULAR, 2);
		String sPatientAccountNumber3 = objInbox.get_AccountNumber(Constants.Queues.REGULAR, 3);
		
				
		// open first available account
		Transaction resultOpen1 = objInbox.open_PatientAccount(Constants.Queues.REGULAR, "ACCOUNT_NUMBER", sPatientAccountNumber1);
		Assert.assertTrue(resultOpen1.getStatus(), resultOpen1.getDescription());
		
		// get UID to create priority file
		String sUID1 = objClaimTransaction.get_UID();		
		logger.log(LogStatus.INFO, "Open account ["+ sPatientAccountNumber1 +"] and get UID value. UID ["+ sUID1 +"]");
		
		objClaimTransaction.close_ClaimTransaction();
		
		
		
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		
		
		// open next available account
		Transaction resultOpen2 = objInbox.open_PatientAccount(Constants.Queues.REGULAR, "ACCOUNT_NUMBER", sPatientAccountNumber2);
		Assert.assertTrue(resultOpen2.getStatus(), resultOpen2.getDescription());
		
		// get UID to create priority file
		String sUID2 = objClaimTransaction.get_UID();		
		logger.log(LogStatus.INFO, "Open account ["+ sPatientAccountNumber2 +"] and get UID value. UID ["+ sUID2 +"]");
		
		objClaimTransaction.close_ClaimTransaction();
				
		
		
		
		List<String> lstUID = Arrays.asList(new String[]{sUID1, sUID2});		
		
		
		// create priority file
		Info resultCreate1 = utils.create_PriorityFile(lstUID, practice);
		Assert.assertTrue(resultCreate1.getStatus(), resultCreate1.getDescription());		
		
		String sFilePath1 = resultCreate1.getDescription();
		logger.log(LogStatus.INFO, "Priority file created for UID "+ lstUID +" <br><br> File path: " + sFilePath1);
		System.out.println("Priority file created for UID "+ lstUID +" <br><br> File path: " + sFilePath1);
		

		// upload priority
		InventoryUpload objUpload = new InventoryUpload();
		Info priority1 = objUpload.upload("Priority Report", client, location, practice, sFilePath1);
		Assert.assertTrue(priority1.getStatus(), priority1.getDescription());
		logger.log(LogStatus.INFO, "Priority - " + priority1.getDescription());
		

		// switch to priority tab
		objInbox.click_Image_Priority(false);
			
		
		
		// select file name and verify the account
		String fileName1 = sFilePath1.substring(sFilePath1.lastIndexOf("\\")+1, sFilePath1.lastIndexOf("."));
		objInbox.select_PriorityFileName(fileName1);
		
		Info searchAcc1 = objInbox.search_Account(sPatientAccountNumber1, Constants.Queues.PRIORITY);
		Assert.assertTrue(searchAcc1.getStatus(), searchAcc1.getDescription());	
		logger.log(LogStatus.INFO, searchAcc1.getDescription());
		
		Info searchAcc2 = objInbox.search_Account(sPatientAccountNumber2, Constants.Queues.PRIORITY);
		Assert.assertTrue(searchAcc2.getStatus(), searchAcc2.getDescription());	
		logger.log(LogStatus.INFO, searchAcc2.getDescription());
		
		
		
		
		
		
		
	
		
		
		
		/*String sPatientAccountNumber1 = "20181029-242";
		String sPatientAccountNumber2 = "20181029-243";
		String sPatientAccountNumber3 = "20181029-244";
		
		String sFilePath1 = "E:\\SVN\\branches\\ATOM\\src\\test\\resources\\Input\\Priority\\2018\\11\\20\\Priority_11_09.xlsx";
		String fileName1 = sFilePath1.substring(sFilePath1.lastIndexOf("\\")+1, sFilePath1.lastIndexOf("."));
		
		String sUID2 = "20181029-243";*/
		
		
		
		
		
		// navigate to regular, verify accounts not present, get uid for third account
		
		objInbox.click_Image_Regular(false);
		
		
		Info searchRegAcc1 = objInbox.search_Account(sPatientAccountNumber1, Constants.Queues.REGULAR);
		Assert.assertFalse(searchRegAcc1.getStatus(), searchRegAcc1.getDescription());	
		logger.log(LogStatus.INFO, searchRegAcc1.getDescription());
		
		Info searchRegAcc2 = objInbox.search_Account(sPatientAccountNumber2, Constants.Queues.REGULAR);
		Assert.assertFalse(searchRegAcc2.getStatus(), searchRegAcc2.getDescription());	
		logger.log(LogStatus.INFO, searchRegAcc2.getDescription());
		
		
		// clear search
		objInbox.clearSearch(Constants.Queues.REGULAR);
		
		
		// open third account
		Transaction resultOpen3 = objInbox.open_PatientAccount(Constants.Queues.REGULAR, "ACCOUNT_NUMBER", sPatientAccountNumber3);
		Assert.assertTrue(resultOpen3.getStatus(), resultOpen3.getDescription());
		
		// get UID to create priority file
		String sUID3 = objClaimTransaction.get_UID();		
		logger.log(LogStatus.INFO, "Open account ["+ sPatientAccountNumber3 +"] and get UID value. UID ["+ sUID3 +"]");
		
		objClaimTransaction.close_ClaimTransaction();
		
		
		
		// upload new priority file with old account
		lstUID =  Arrays.asList(new String[]{sUID2, sUID3});
		
		Info resultCreate2 = utils.create_PriorityFile(lstUID, practice);
		Assert.assertTrue(resultCreate2.getStatus(), resultCreate2.getDescription());		
		
		String sFilePath2 = resultCreate2.getDescription();
		logger.log(LogStatus.INFO, "Priority file created for UID "+ lstUID +" <br><br> File path: " + sFilePath2);
		System.out.println("Priority file created for UID "+ lstUID +" <br><br> File path: " + sFilePath2);
		
		
		
		objUpload = new InventoryUpload();
		Info priority2 = objUpload.upload("Priority Report", client, location, practice, sFilePath2);
		Assert.assertTrue(priority2.getStatus(), priority2.getDescription());
		logger.log(LogStatus.INFO, "Priority - " + priority2.getDescription());
		
		
		
		// verify account under new file name
		
		// switch to priority tab
		objInbox.click_Image_Priority(false);
			
		
		
		// select file name and verify the account
		String fileName2 = sFilePath2.substring(sFilePath2.lastIndexOf("\\")+1, sFilePath2.lastIndexOf("."));
		objInbox.select_PriorityFileName(fileName2);
		
		Info searchAcc3 = objInbox.search_Account(sPatientAccountNumber2, Constants.Queues.PRIORITY);
		Assert.assertTrue(searchAcc3.getStatus(), searchAcc3.getDescription());	
		logger.log(LogStatus.INFO, searchAcc3.getDescription());
		
		Info searchAcc4 = objInbox.search_Account(sPatientAccountNumber3, Constants.Queues.PRIORITY);
		Assert.assertTrue(searchAcc4.getStatus(), searchAcc4.getDescription());	
		logger.log(LogStatus.INFO, searchAcc4.getDescription());
		
		
		objInbox.select_PriorityFileName(fileName1);
		
		Info searchAcc5 = objInbox.search_Account(sPatientAccountNumber2, Constants.Queues.PRIORITY);
		Assert.assertFalse(searchAcc5.getStatus(), searchAcc5.getDescription());	
		logger.log(LogStatus.INFO, searchAcc5.getDescription());
	}
	
	
	@Test(groups= {"REG"})
	public void verify_Priority()
	{
		
		/*
		 * login - analyst
		 * navigate to priority queue 
		 * submit account
		 * sign out
		 * 
		 * login
		 * verify account
		 * submit response
		 * sign out
		 */
		 
		logger = extent.startTest("Save account from Priority queue and verify in next queue");
		
		int executableRowIndex = excel.isExecutable("verify_Priority");		
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
		objInbox = PageFactory.initElements(driver, AnalystInbox.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objReviewAndApprove = PageFactory.initElements(driver, ReviewAndApprove.class);
		
		objQCDashboard = PageFactory.initElements(Configuration.driver, QCDashboard.class);
		objQCInbox = PageFactory.initElements(Configuration.driver, QCInbox.class);
		objQCA_AuditSheet = PageFactory.initElements(Configuration.driver, QCA_AuditSheet.class);
	
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");	

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);	
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		logger.log(LogStatus.INFO, "Navigate to Transaction -> Inbox");
		
		//select practice and select regular image link
		logger.log(LogStatus.INFO, "Select practice ["+ practice +"] and click PRIORITY queue icon");
		objInbox.select_Practice(practice); 
		//objAnalystInbox.click_Image_Priority(true);
		
	 	
	 	// open account
		Transaction result = objInbox.open_PatientAccount(Constants.Queues.PRIORITY);
	 	Assert.assertTrue(result.getStatus(), result.getDescription());
				 
		String totalCharges = objClaimTransaction.get_TotalCharges();
		String sAccountNumber = objClaimTransaction.get_PatientAccountNumber();
		String sEncounterId = objClaimTransaction.get_EncounterID();
		String sUID = objClaimTransaction.get_UID();
		
		// submit account to print and mail inbox						
		Transaction rSubmit = objClaimTransaction.submit_Claim(sUID, claimStatus, actionCode, resolveType, notes);
							
		// assert submission status	
		Assert.assertTrue(rSubmit.getStatus(), rSubmit.getDescription());
		logger.log(LogStatus.INFO, "<b>Account moved to Credentialing, "
											+ "Account [" + sAccountNumber + "], UID ["+ sUID +"] <br> <br>"
											
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
		
		objInbox.select_Practice(practice);				
		objInbox.click_Image_ClientEscalationResponse();
		logger.log(LogStatus.INFO, "Select practice ["+ practice +"] and click Client Escalation Response queue");		
		
		//	verify account presence, tlComments
		Info search1 = objInbox.search_ClientEscalationRelease(sAccountNumber, remarks);
		Assert.assertTrue(search1.getStatus(), search1.getDescription());
		logger.log(LogStatus.INFO, search1.getDescription());
		
		//	submit account - arclosed
		objInbox.open_PatientAccount(Constants.Queues.CLIENT_ESCALATION_RELEASE, "ACCOUNT_NUMBER", sAccountNumber);
	 	
	 	nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue2");		
		data = excel.get_TransactionData(nextQueue);
		
		claimStatus = data.get("claim_status");
		actionCode = data.get("action_code");
		resolveType = data.get("resolve_type"); 
		notes = data.get("note");
	 	
	 	Transaction rSubmit1 = objClaimTransaction.submit_Claim(sUID, claimStatus, actionCode, resolveType, notes);
	 	Assert.assertTrue(rSubmit1.getStatus(), rSubmit1.getDescription());
		logger.log(LogStatus.INFO, "<b>Claim moved to QCA, ClaimStatus ["+claimStatus+"], <br> ActionCode ["+actionCode+"], <br> ResolveType ["+resolveType+"], <br> Notes ["+notes+"]</b>");
		 		
		// sign out	
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
		logger.log(LogStatus.INFO, "Navigate to QC Inbox, select Practice ["+ practice +"] and from date as ["+ utils.getToday() +"]");
		

		Info search2 = objQCInbox.search_Encounter(sEncounterId);
		Assert.assertTrue(search2.getStatus(), search2.getDescription());
		logger.log(LogStatus.INFO, search1.getDescription());
		
		
		objQCInbox.open_Encounter_QCA(sUID);
				
		String sComments = "test comments";
		Info saveAudit = objQCA_AuditSheet.set_Error_Comments(sComments).save_Audit();
		
		Assert.assertTrue(saveAudit.getStatus(), saveAudit.getDescription());
		logger.log(LogStatus.INFO, "Encounter audited, Comments ["+ sComments +"]");
				
		
	}

}
