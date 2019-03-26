package transaction;

import java.io.File;
import java.io.IOException;
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
import pageObjectRepository.transaction.AnalystInbox;
import pageObjectRepository.transaction.ClaimTransaction;
import pageObjectRepository.transaction.ReviewAndApprove;

public class Upload extends Configuration
{
	Login objLogin;
	Dashboard objDashboard;
	AnalystInbox objInbox;
	ClaimTransaction objClaimTransaction;
	ReviewAndApprove objReviewAndApprove;	
	
	@Test
	public void verify_WhenAccountUploadedMultipleTimes() throws IOException
	{
		/*
		 * upload an account (Account1)
		 * login as analyst
		 * select practice and move account from regular to credentialing
		 * logout 
		 * 
		 * login - TL
		 * verify Account1
		 * logout
		 * 
		 * upload another account (Account2)
		 * 
		 * upload (Account1) again
		 * 
		 * login - analyst
		 * verify Account1 in regular queue
		 * logout
		 * 
		 * login - TL
		 * verify Account1 in credentialing
		 * logout
		 * 
		 */
		
		WebDriverUtils utils = new WebDriverUtils();
		//ExcelReader reader = new ExcelReader(Constants.xlFilePath);
		int executableRowIndex = excel.isExecutable("verify_WhenAccountUploadedMultipleTimes");
		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		String firstFilePathFromExcel = excel.readValue(sheet, executableRowIndex,"firstFilepath");
		String secondFilePathFromExcel = excel.readValue(sheet, executableRowIndex,"secondFilePath");
		String autoITExecutable = excel.readValue(sheet, executableRowIndex,"autoItExecutablePath"); // add space when combining
		
		
		String firstFilePath = new File(firstFilePathFromExcel).getAbsolutePath(); 
		String secondFilePath = new File(secondFilePathFromExcel).getAbsolutePath();
		
				
		String nextQueue = excel.readValue(sheet, executableRowIndex,"nextQueue1");		
		HashMap<String, String> data_credentialing = excel.get_TransactionData(nextQueue);
		
		String claimStatus = data_credentialing.get("claim_status");
		String actionCode = data_credentialing.get("action_code");
		String resolveType = data_credentialing.get("resolve_type"); 
		String notes = data_credentialing.get("note");
		
		
		/*String client = excel.readValue(sheet, executableRowIndex,"client");
		String location = excel.readValue(sheet, executableRowIndex,"location");*/
		String practice = excel.readValue(sheet, executableRowIndex,"practice"); 
		
		objLogin = PageFactory.initElements(driver, Login.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objInbox = PageFactory.initElements(Configuration.driver, AnalystInbox.class);
		objClaimTransaction = PageFactory.initElements(driver, ClaimTransaction.class);
		objReviewAndApprove = PageFactory.initElements(driver, ReviewAndApprove.class);
		
		
	 	
		
		/* ******************** UPLOAD 1 ******************** */
	 	
		
		// upload account1
		Transaction uploadStatus1 = utils.uploadInventory(autoITExecutable + " " +firstFilePath);				
		Assert.assertTrue(uploadStatus1.getStatus(), uploadStatus1.getDescription());
		logger.log(LogStatus.INFO, "<b>File uploaded successfully</b>");
		System.out.println("File1 uploaded");
			
		
		//get inventory id from db
		HashMap<String, String> importFile1 = db.get_LastImportFileDetails();		
		//assert filename
			
		
		
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");	

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		logger.log(LogStatus.INFO, "Navigate to Transaction -> Inbox");
		
		// select practice and click regular
		
		objInbox.select_Practice(practice); 
		objInbox.click_Image_Regular(true);
	 	logger.log(LogStatus.INFO, "Select practice ["+ practice +"] and click REGULAR queue icon");
						
		// submit account to payment
	 	objInbox.open_PatientAccount(Constants.Queues.REGULAR);	 	
		
	 	String sUID = objClaimTransaction.get_UID();
		String sAccountNumber = objClaimTransaction.get_PatientAccountNumber();
		String sTotalCharges = objClaimTransaction.get_TotalCharges();
		
		Transaction rSubmit = objClaimTransaction.submit_Claim(sUID, claimStatus, actionCode, resolveType, notes);
				
		// assert submission status
		Assert.assertTrue(rSubmit.getStatus(), rSubmit.getDescription());
		logger.log(LogStatus.INFO, "<b>Claim moved to Credentialing. Account [" + sAccountNumber + "], UID ["+ sUID +"]</b>");	
		
		// sign out from Analyst
		objDashboard.signOut();
		
		
		
		
		
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.TL);
		String sTLUsername = login.get("ntlg");
		String sTLPassword = login.get("password");	

		// login - TL
		objLogin.login(sTLUsername, sTLPassword);		
		
		// navigate to review and approve	
		objDashboard.navigateTo_Transaction().navigateTo_Review_And_Approve();
		logger.log(LogStatus.INFO, " Navigate to Review and approve");
		
		// switch to client escalation tab	and select practice	
		objReviewAndApprove.click_Tab_Credentialing();
		objReviewAndApprove.select_Practice(practice);
		logger.log(LogStatus.INFO, "Switched to Credentialing tab and select practice ["+ practice +"]");
		
					
		// verify account			
		Info search = objReviewAndApprove.search_Account(sAccountNumber, sTotalCharges);
		Assert.assertTrue(search.getStatus(), search.getDescription());	
		Configuration.logger.log(LogStatus.INFO, "<b>Claim moved to Credentialing Response</b>");
		
		//Sign out
		objDashboard.signOut();
			
		
		
		
		
		
		/* ******************** UPLOAD 2 ******************** */
		
		
		//change uploaded date of previous upload
		db.update_ImportFileDetails_UploadDate(importFile1.get("importFileID"));
		
		
		// upload account2
		Transaction loadStatus2 = utils.uploadInventory(autoITExecutable + " " +secondFilePath);						
		Assert.assertTrue(loadStatus2.getStatus(), loadStatus2.getDescription());
		logger.log(LogStatus.INFO, "<b>File uploaded successfully</b>");
		System.out.println("File2 uploaded");
		
		
		//get importFile id from db
		HashMap<String, String> importFile2 = db.get_LastImportFileDetails();
				
		
		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		logger.log(LogStatus.INFO, "Navigate to Transaction -> Inbox");
		
		// select practice and click regular
		objInbox.select_Practice(practice); 
		objInbox.click_Image_Regular(true);
	 	logger.log(LogStatus.INFO, "Select practice ["+ practice +"] and click REGULAR queue icon");
		
	 	// verify account
	 	Info searchRegular = objInbox.search_Account(sAccountNumber, Constants.Queues.REGULAR);
	 	Assert.assertFalse(searchRegular.getStatus(), searchRegular.getDescription());
	 	
	 //	int totalAccount = objInbox.get_TotalAccounts(Constants.Queues.REGULAR);
	 	utils.save_ScreenshotToReport("regularSearch");
	 	
	 	
		// sign out from Analyst
		objDashboard.signOut();
		
		
		
		
		
		
		
		/* ******************** UPLOAD 1 AGAIN ******************** */
		
		
		//change uploaded date of previous upload
		db.update_ImportFileDetails_UploadDate(importFile2.get("importFileID"));
		
		
		
		// upload account1 again
		Transaction loadStatus3 = utils.uploadInventory(autoITExecutable + " " + firstFilePath);						
		Assert.assertTrue(loadStatus3.getStatus(), loadStatus3.getDescription());
		logger.log(LogStatus.INFO, "<b>File uploaded successfully</b>");
		System.out.println("File3 uploaded");
		
		
		//get inventory id from db
		//HashMap<String, String> importFile3 = db.get_LastImportFileDetails();
				

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		logger.log(LogStatus.INFO, "Navigate to Transaction -> Inbox");
		
		// select practice and click regular
		objInbox.select_Practice(practice); 
		objInbox.click_Image_Regular(true);
	 	logger.log(LogStatus.INFO, "Select practice ["+ practice +"] and click REGULAR queue icon");
		
	 	// verify account
	 	searchRegular = objInbox.search_Account(sAccountNumber, Constants.Queues.REGULAR);
	 	//Assert.assertTrue(searchRegular.isFound(), searchRegular.getDescription());	 
	 
	 	utils.save_ScreenshotToReport("regularSearch_ForFirstAccount");
	 	
	 	
	 	// submit account to coding
	 	objInbox.open_PatientAccount(Constants.Queues.REGULAR, "ACCOUNT_NUMBER", sAccountNumber);
	 		 	
	 	
	 	/*nextQueue = excel.readValue(sheet, executableRowIndex,"nextQueue1");		
		HashMap<String, String> data = excel.get_TransactionData(nextQueue);
		
		claimStatus = data.get("claim_status");
		actionCode = data.get("action_code");
		resolveType = data.get("resolve_type"); 
		notes = data.get("note");
			 	
		objClaimTransaction.submit_Claim(claimStatus, actionCode, resolveType, notes);*/
	 	
	 	
		objClaimTransaction.close_ClaimTransaction();
		
		
	 	
		// sign out from Analyst
		objDashboard.signOut();
		
		
		// login - TL
		objLogin.login(sTLUsername, sTLPassword);		
		
		// navigate to review and approve	
		objDashboard.navigateTo_Transaction().navigateTo_Review_And_Approve();
		logger.log(LogStatus.INFO, " Navigate to Review and approve");
		
		// switch to client escalation tab	and select practice	
		objReviewAndApprove.click_Tab_Credentialing();
		objReviewAndApprove.select_Practice(practice);
		logger.log(LogStatus.INFO, "Switched to Credentialing tab and select practice ["+ practice +"]");
		
					
		// verify account			
		search = objReviewAndApprove.search_Account(sAccountNumber, sTotalCharges);
		utils.save_ScreenshotToReport("nextQueueSearch_ForFirstAccount");
		Configuration.logger.log(LogStatus.ERROR, "<b>Claim found at Credentialing Queue</b>");
				
	}

}
