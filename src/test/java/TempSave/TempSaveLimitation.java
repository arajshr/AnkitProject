package TempSave;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
import pageObjectRepository.transaction.ReviewAndApprove;
import transaction.Appeals;

public class TempSaveLimitation extends Configuration
{
	WebDriverUtils utils = new WebDriverUtils();
	
	private Login objLogin;
	private AnalystInbox objInbox;
	private ClaimTransaction objClaimTransaction;
	private Dashboard objDashboard;
	private ReviewAndApprove objReviewAndApprove;
	private AppealsInbox objAppeals;
	
	@Test(groups = {"REG"}, priority = 1, enabled = false)
	public void verify_TempSaveLimit()
	{
		logger = extent.startTest("Verify Temp Save limitation [max 10 accounts]");
		
		int executableRowIndex = excel.isExecutable("verify_TempSaveLimit");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		// TEST DATA FROM EXCEL
		String practice = excel.readValue(sheet, executableRowIndex, "practice");		
		String nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue1");		
		HashMap<String, String> data = excel.get_TransactionData(nextQueue);
		
		String claimStatus = data.get("claim_status");
		String actionCode = data.get("action_code");
		String resolveType = data.get("resolve_type"); 
		String notes = data.get("note");
		
		String expMessage = excel.readValue(sheet, executableRowIndex, "expMsg");
		
	
		// PAGE INITIALIZATION
		objLogin = PageFactory.initElements(Configuration.driver, Login.class);
		objClaimTransaction = PageFactory.initElements(driver, ClaimTransaction.class);
		objInbox = PageFactory.initElements(driver, AnalystInbox.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		
		int iMaxLimitation = 10;
		
		
		
		
		/************************ GET NUMBER OF ACCOUNTS FROM TEMP SAVE QUEUE **************************/
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");	

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
				
		logger.log(LogStatus.INFO, "Navigate to Inbox and select practice ["+ practice +"]");
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		objInbox.select_Practice(practice);
		
		objInbox.click_Image_TempSave(false);
		logger.log(LogStatus.INFO, "Navigate to TEMP SAVE queue");
			
		int iAccountsInTempSave = objInbox.get_TotalAccounts(Constants.Queues.TEMP_SAVE);
		
		utils.scrollToBottomOfPage();
		utils.save_ScreenshotToReport("AccountsInTempSave");
		utils.scrollWindow(0, -500);
		
		logger.log(LogStatus.INFO, "<b>There are " + iAccountsInTempSave + " Encounter(s) in Temp Save</b>");
		
		
		/************************ SUBMIT ACCOUNT TO TEMP SAVE **************************/
		
		
		if(iAccountsInTempSave > iMaxLimitation)
		{
			// temp save queue has more than 10 accounts
		}
		else
		{
			logger.log(LogStatus.INFO, "Navigate to REGULAR queue");
			objInbox.click_Image_Regular(true);
			
			
			int iAccountsToMoveToTempSave = iMaxLimitation - iAccountsInTempSave + 1;
			
			
			for (int i = 0; i < iAccountsToMoveToTempSave;) 
			{
				
				objInbox.open_PatientAccount(Constants.Queues.REGULAR); //, "ACCOUNT_NUMBER", "20190118-006"
				
				String sAccountNumber = objClaimTransaction.get_PatientAccountNumber();
				int iUIDCount = objClaimTransaction.get_UIDCount();
				HashSet<String> setEncounters = objClaimTransaction.get_EncounterList_From_ClaimTransaction();
				
				
				// move account to temp save and assert values
				Info save = objClaimTransaction.tempSave(claimStatus, actionCode, resolveType, notes, true);
				
				i += iUIDCount;		
				
				if(save.getStatus() && i < iAccountsToMoveToTempSave)  // true and in limit
				{
					logger.log(LogStatus.INFO, "<b>Account moved to Temp Save queue. Account Number ["+ sAccountNumber +"], Encounter(s) "+ setEncounters +"</b>");
					logger.log(LogStatus.INFO, iUIDCount + " Encounter(s) moved to Temp Save. Total in queue: " + (i + iAccountsInTempSave));
					System.out.println(iUIDCount + " UID(s) moved to Temp Save. Total in queue: " + (i + iAccountsInTempSave));
					
				}
				else if(save.getStatus() && i >= iAccountsToMoveToTempSave)  // true and not in limit
				{
					logger.log(LogStatus.INFO, "<b>Account moved to Temp Save queue. Account Number ["+ sAccountNumber +"], Encounter(s) "+ setEncounters +"</b>");
					
					objInbox.click_Image_TempSave(false);
					logger.log(LogStatus.INFO, "Navigate to TEMP SAVE queue");
					utils.save_ScreenshotToReport("TEMPSAVE_LIMIT_EXCEED");
					
					logger.log(LogStatus.FAIL, "<b>Temp Save limit exceeded. "+ i + " UID(s) found in Temp Save</b>");
					
				}
				else if(!save.getStatus() && i >= iAccountsToMoveToTempSave)	// false and not in limit
				{
					logger.log(LogStatus.INFO, "<b>Temp Save limit verified </b>");
					
					
					if(!save.getDescription().toLowerCase().trim().equals(expMessage.toLowerCase().trim()))
					{
						logger.log(LogStatus.INFO, "<b>Alert message mismatch, expected ["+ expMessage +"] but foud ["+ save.getDescription() +"] </b>");
					}
					break;
					
				}
				else if(!save.getStatus() && i < iAccountsToMoveToTempSave)  // false and in limit
				{
					objInbox.click_Image_TempSave(false);
					logger.log(LogStatus.INFO, "Navigate to TEMP SAVE queue");
					
					utils.scrollToBottomOfPage();
					utils.save_ScreenshotToReport("TEMPSAVE_LIMIT");
					
					logger.log(LogStatus.FAIL, "<b>There are " + (i + iAccountsInTempSave) + " encounters yet alert displayed</b>");
					
					break;
				}
				
			}
			
		}
	}


	@Test(groups = {"REG"}, priority = 2, enabled = true)
	public void verify_TempSaveLimitFromTransactions()
	{
		logger = extent.startTest("Verify Temp Save limitation [max 10 accounts]");
		
		int executableRowIndex = excel.isExecutable("verify_TempSaveLimit");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		// TEST DATA FROM EXCEL
		String practice = excel.readValue(sheet, executableRowIndex, "practice");		
		String nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue1");
		
		String[] arrNextQueue = nextQueue.split(",");
		String sNQ_TempSave = arrNextQueue[0];
		String sNQ_Clarification = arrNextQueue[1];
		String sNQ_ClientEscalation = arrNextQueue[2];
		String sNQ_Appeals = arrNextQueue[3];
		
		
		
		String responseQueue = excel.readValue(sheet, executableRowIndex, "response");		
		String[] arrResponse = nextQueue.split(",");
		String sR_Clarification = arrNextQueue[0];
		String sR_ClientEscalation = arrNextQueue[1];
		String sR_Appeals = arrNextQueue[2];
		
		
		/*HashMap<String, String> data = excel.get_TransactionData(nextQueue);		
		String claimStatus = data.get("claim_status");
		String actionCode = data.get("action_code");
		String resolveType = data.get("resolve_type"); 
		String notes = data.get("note");*/
		
		String expMessage = excel.readValue(sheet, executableRowIndex, "expMsg");
		
	
		// PAGE INITIALIZATION
		objLogin = PageFactory.initElements(Configuration.driver, Login.class);
		objClaimTransaction = PageFactory.initElements(driver, ClaimTransaction.class);
		objInbox = PageFactory.initElements(driver, AnalystInbox.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objReviewAndApprove = PageFactory.initElements(driver, ReviewAndApprove.class);
		objAppeals = PageFactory.initElements(driver, AppealsInbox.class);
		
		
		int iMaxLimitation = 10;
		
		
		
		
		/************************ GET NUMBER OF ACCOUNTS FROM TEMP SAVE QUEUE **************************/
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");	

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
				
		logger.log(LogStatus.INFO, "Navigate to Inbox and select practice ["+ practice +"]");
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		objInbox.select_Practice(practice);
		
		objInbox.click_Image_TempSave(false);
		logger.log(LogStatus.INFO, "Navigate to TEMP SAVE queue");
			
		int iAccountsInTempSave = objInbox.get_TotalAccounts(Constants.Queues.TEMP_SAVE);
		
		utils.scrollToBottomOfPage();
		utils.save_ScreenshotToReport("AccountsInTempSave");
		utils.scrollWindow(0, -500);
		
		logger.log(LogStatus.INFO, "<b>There are " + iAccountsInTempSave + " Encounter(s) in Temp Save</b>");
		
		
		
		
		if(iAccountsInTempSave > iMaxLimitation)
		{
			logger.log(LogStatus.FAIL, "<b>Encounter(s) in Temp Save exceeds max limit [10 accounts]</b>");
		}
		else if(iAccountsInTempSave < iMaxLimitation)
		{
			logger.log(LogStatus.INFO, "Navigate to REGULAR queue");
			objInbox.click_Image_Regular(true);
			
			
			int iAccountsToMoveToTempSave = iMaxLimitation - iAccountsInTempSave;
			HashSet<String> setEncounters = new HashSet<>();
			
			HashMap<String, String> data = excel.get_TransactionData(sNQ_TempSave);
			
			String claimStatus = data.get("claim_status");
			String actionCode = data.get("action_code");
			String resolveType = data.get("resolve_type"); 
			String notes = data.get("note");
			
			
			
			for (int i = 0; i < iAccountsToMoveToTempSave;) 
			{				
				objInbox.open_PatientAccount(Constants.Queues.REGULAR); //, "ACCOUNT_NUMBER", "20190118-006"
				
				String sAccountNumber_TempSave = objClaimTransaction.get_PatientAccountNumber();
				int iUIDCount = objClaimTransaction.get_UIDCount();
				setEncounters.add(objClaimTransaction.get_EncounterID());
				
				
				// move account to temp save and assert values
				Info save = objClaimTransaction.tempSave(claimStatus, actionCode, resolveType, notes, true);
				Assert.assertTrue(save.getStatus(), save.getDescription() + "Account ["+ sAccountNumber_TempSave +"]");
				
				
				i += iUIDCount;
			}
			
			
			logger.log(LogStatus.INFO, "Encounter(s) "+ setEncounters +" moved to Temp Save queue");
			
		}
		
		
		
		//logger.log(LogStatus.INFO, "<b>/************************ CLARIFICATION RELEASE **************************/ </b>");
		
		/********************	CLARIFICATION	***********************/
					
		HashMap<String, String> data = excel.get_TransactionData(sNQ_Clarification);		
		String reason = data.get("reason");
		String comments = data.get("comments");
		String remarks = data.get("remarks");
		
		
		//	open first available account
		objInbox.open_PatientAccount(Constants.Queues.REGULAR);
		String sUID_Clarifiation = objClaimTransaction.get_UID();
		String sAccountNumber_Clarifiation = objClaimTransaction.get_PatientAccountNumber();
		
		//	submit to clarification
		Assert.assertTrue(objClaimTransaction.save_Clarification(reason, comments));		
		logger.log(LogStatus.INFO, "<b>Account [" + sAccountNumber_Clarifiation + "] moved to Clarification, Reason [" + reason + "] and Comments [" + comments + "]</b>");
		
		
		
		
		
		
		/********************	CLIENT ESCALATION	***********************/
		
		
		//		open first available account
	 	objInbox.open_PatientAccount(Constants.Queues.REGULAR);
	 	
	 	String sAccountNumber_ClientEsc = objClaimTransaction.get_PatientAccountNumber();
		String sUID_ClientEsc = objClaimTransaction.get_UID();
		String sEncounterID_ClientEsc = objClaimTransaction.get_EncounterID();
		String sTotalCharges_ClientEsc = objClaimTransaction.get_TotalCharges();
		
		
		data = excel.get_TransactionData(sNQ_TempSave);
			
		String claimStatus_CLESC = data.get("claim_status");
		String actionCode_CLESC = data.get("action_code");
		String resolveType_CLESC = data.get("resolve_type"); 
		String notes_CLESC = data.get("note");
			
			
		// submit account to Client Escalation	
		Transaction submitAccount = objClaimTransaction.submit_Claim(sUID_ClientEsc, claimStatus_CLESC, actionCode_CLESC, resolveType_CLESC, notes_CLESC);
							
		// assert submission status	
		Assert.assertTrue(submitAccount.getStatus(), submitAccount.getDescription());
		logger.log(LogStatus.INFO, "<b>Account moved to Client Escalation, "
											+ "Account [" + sAccountNumber_ClientEsc + "], UID ["+ sUID_ClientEsc +"]<br> <br>"
				
											+ "ClaimStatus ["+ claimStatus_CLESC +"], <br>"
											+ "ActionCode ["+ actionCode_CLESC +"], <br>"
											+ "ResolveType ["+ resolveType_CLESC +"], <br>"
											+ "Notes ["+ notes_CLESC +"]</b>");	
		
		
		
		
		
		/********************	APPEALS	 ***********************/
		
		
		//	open first available account
	 	objInbox.open_PatientAccount(Constants.Queues.REGULAR);			
	 	String sAccountNumber_Appeals = objClaimTransaction.get_PatientAccountNumber();
	 	String sUID_Appeals = objClaimTransaction.get_UID();
	 	
	 	
	 	data = excel.get_TransactionData(sNQ_TempSave);
		
		String claimStatus_APPEAL = data.get("claim_status");
		String actionCode_APPEAL = data.get("action_code");
		String resolveType_APPEAL = data.get("resolve_type"); 
		String notes_APPEAL = data.get("note");
		
		
	 	//	submit and Assert
		submitAccount = objClaimTransaction.submit_Claim(sUID_Appeals, claimStatus_APPEAL, actionCode_APPEAL, resolveType_APPEAL, notes_APPEAL);		
		Assert.assertTrue(submitAccount.getStatus(), submitAccount.getDescription());
		
		logger.log(LogStatus.INFO, "<b>Account moved to Appeals Inbox, "
											+ "Account [" + sAccountNumber_Appeals + "], UID ["+ sUID_Appeals +"]<br> <br>"
							
											+ "ClaimStatus ["+ claimStatus_APPEAL +"], <br>"
											+ "ActionCode ["+ actionCode_APPEAL +"], <br>"
											+ "ResolveType ["+ resolveType_APPEAL +"], <br>"
											+ "Notes ["+ notes_APPEAL +"]</b>");
		
		
		
		//	Follow Up
		
		//	Payment response
		
		//	Coding response
		
		//	Credentialing response
		
		
		
		
		
		
		
		
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.TL);
		String sTLUsername = login.get("ntlg");
		String sTLPassword = login.get("password");	

		// login - TL
		objLogin.login(sTLUsername, sTLPassword);		
		
		objDashboard.navigateTo_Transaction().navigateTo_Review_And_Approve();
		
		
		
		
		
		/********************	CLARIFICATION RELEASE	***********************/
		
		
		
		objReviewAndApprove.click_Tab_Clarification();
		logger.log(LogStatus.INFO, " Navigate to Review and approve and switch to Clarification tab");		
		
		//	verify account presence , reason and comments
		Info search = objReviewAndApprove.search_Account_Clarification(sUID_Clarifiation, reason, comments);
		Assert.assertTrue(search.getStatus(), search.getDescription());
		logger.log(LogStatus.INFO, search.getDescription());
		
		
		//	Assert clarification release
		Info save = objReviewAndApprove.submit_Clarification(remarks);
		Assert.assertTrue(save.getStatus(), save.getDescription());
		logger.log(LogStatus.INFO, "<b>Account moved to Clarification release with  remarks [" + remarks + "]</b>");
		
		
		
		
		/********************	CLIENT ESCALATION RELEASE	***********************/
		
		
		objReviewAndApprove.click_Tab_ClientEscalation();
		
		// select practice	
		objReviewAndApprove.select_Practice(practice);
		logger.log(LogStatus.INFO, " Switched to Client Escalation tab and select practice ["+ practice +"]");
			
						
		// verify account			
		Info searchEsc = objReviewAndApprove.search_Account(sAccountNumber_ClientEsc, sTotalCharges_ClientEsc);		
		Assert.assertTrue(searchEsc.getStatus(), searchEsc.getDescription());
		logger.log(LogStatus.INFO, search.getDescription());
		
				
		/*String responseQueue = excel.readValue(sheet, executableRowIndex, "response");		
		data = excel.get_TransactionData(responseQueue);		
		String remarks = data.get("remarks");*/
		
		Assert.assertTrue(objReviewAndApprove.submit_ClientEscalationResponse(sEncounterID_ClientEsc, remarks));
		
		
		// sign out
		objDashboard.signOut();
		
		
		
		
		
		
		/********************	APPEALS RESPONSE	***********************/
		
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
		Info searchAppeal = objAppeals.search_PatientAccountNumber(sAccountNumber_Appeals);
		Assert.assertTrue(searchAppeal.getStatus(), searchAppeal.getDescription());
		logger.log(LogStatus.INFO, searchAppeal.getDescription());
		
		
		// submit account to appeal response
		data = excel.get_TransactionData(sR_Appeals);
		
		String claimStatus_APPRES = data.get("claim_status");
		String actionCode_APPRES = data.get("action_code");
		String resolveType_APPRES = data.get("resolve_type"); 
		String notes_APPRES = data.get("note");
		
		Transaction result = objAppeals.submit_AppealsResponse(sAccountNumber_Appeals, claimStatus_APPRES, actionCode_APPRES, resolveType_APPRES, notes_APPRES);
		Assert.assertTrue(result.getStatus(), result.getDescription());
		logger.log(LogStatus.INFO, "<b>Account " + sAccountNumber_Appeals + "  moved to Appeals response queue</b>");
		
		// Sign out
 		objDashboard.signOut();
		 		
		 		
		
	}




}
