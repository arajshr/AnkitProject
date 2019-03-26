package dashboard;

import java.io.IOException;
import java.rmi.server.ObjID;
import java.util.HashMap;
import java.util.List;

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
import excelLibrary.ExcelReader;
import pageObjectRepository.Dashboard;
import pageObjectRepository.Login;
import pageObjectRepository.dashboard.ProviderCredentialReport;
import pageObjectRepository.transaction.AnalystInbox;
import pageObjectRepository.transaction.ClaimTransaction;

public class ProviderCredential extends Configuration 
{
WebDriverUtils utils = new WebDriverUtils();
	
	Login objLogin;
	AnalystInbox objAnalystInbox;
	ClaimTransaction objClaimTransaction;
	Dashboard objDashboard;
	ProviderCredentialReport objReport;
	
	@Test
	public void verify_ProviderCredential_Dashboard()
	{
		/* 
		 * Conditions:
		 * Inventory item should have valid Provider Credential mapped to it  
		 * Claim should be moved to credentialing [entry in agent transaction]
		 * 
		 * */
		logger = extent.startTest("Verifying Provider Credential report");
		
		int executableRowIndex = excel.isExecutable("verify_ProviderCredential_Dashboard");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		String client = excel.readValue(sheet, executableRowIndex, "client");
		String location = excel.readValue(sheet, executableRowIndex, "location");
		String practice = excel.readValue(sheet, executableRowIndex, "practice");
		
		String nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue1");
		HashMap<String, String> data = excel.get_TransactionData(nextQueue);
		
		String claimStatus = data.get("claim_status");
		String actionCode = data.get("action_code");
		String resolveType = data.get("resolve_type"); 
		String notes = data.get("note");
		
		
		String inputFilePath = excel.readValue(sheet, executableRowIndex, "firstFilepath");
		
		
		objLogin = PageFactory.initElements(driver, Login.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objReport = PageFactory.initElements(driver, ProviderCredentialReport.class);
		objAnalystInbox = PageFactory.initElements(driver, AnalystInbox.class);
		objClaimTransaction = PageFactory.initElements(driver, ClaimTransaction.class);
		
		
		
		
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");	

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		Configuration.logger.log(LogStatus.INFO, "Navigate to Transaction -> Inbox");
		
		objAnalystInbox.select_Practice(practice, false);
		objAnalystInbox.click_Image_Regular(false);
	 	Configuration.logger.log(LogStatus.INFO, "Select practice ["+ practice +"] and click REGULAR queue icon");
	 	
		objAnalystInbox.open_PatientAccount(Constants.Queues.REGULAR);
		String sAccountNumber = objClaimTransaction.get_PatientAccountNumber();
		String sUID = objClaimTransaction.get_UID();
		String sProvider = objClaimTransaction.get_ProviderName();
		String sPayer = objClaimTransaction.get_PayerName();
		String sDOSStart = objClaimTransaction.get_DOSStart();
		String sDOSEnd = objClaimTransaction.get_DOSEnd();
		
		logger.log(LogStatus.INFO, "Get claim details. Account Number ["+ sAccountNumber +"], UID ["+ sUID +"], Provider Name ["+ sProvider +"]");
		utils.save_ScreenshotToReport("GetProviderName");
		
		objClaimTransaction.close_ClaimTransaction();
		// if provider name is empty
		if(sProvider.isEmpty())
		{
			utils.save_ScreenshotToReport("ProviderNameEmpty");
			Assert.assertTrue(false, "<b>Provider Name is empty</b>");
		}
		
		// get encounter ids from excel for both MRNs
		ExcelReader eInput;
		List<String> lstAccounts = null; 
		try 
		{
			eInput = new ExcelReader(inputFilePath);
			lstAccounts = eInput.getAccountsForProviderName(sProvider);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			Assert.fail();
		}
		
		
		
		logger.log(LogStatus.INFO, "Get account for Payer from excel");
		
		// compare with account in regular to get account count
		List<String> lstExpAccounts = objAnalystInbox.get_MatchingAccounts(Constants.Queues.REGULAR, lstAccounts);
		
		
		
		objDashboard.signOut();
				
		
		
		
		
		
		
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.TL);
		String sTLUsername = login.get("ntlg");
		String sTLPassword = login.get("password");	

		// login - TL
		objLogin.login(sTLUsername, sTLPassword);		
		objDashboard.navigateTo_Dashboard().navigateTo_ProviderCredential();
		logger.log(LogStatus.INFO, "Navigate to Dashboard -> Provider Credential Report");
		
		logger.log(LogStatus.INFO, "Get initial values from report for Provider ["+ sProvider +"]");
		int expNoOfAccounts = objReport.get_Report(client, location, practice, sProvider, sPayer, sDOSStart, sDOSEnd);
		
		utils.save_ScreenshotToReport("ProviderCredentialReport_Initial");		
		objDashboard.signOut();
		
		
		
		
		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		Configuration.logger.log(LogStatus.INFO, "Navigate to Transaction -> Inbox");
		
		objAnalystInbox.select_Practice(practice, false);
		objAnalystInbox.click_Image_Regular(true);
	 	Configuration.logger.log(LogStatus.INFO, "Select practice ["+ practice +"] and click REGULAR queue icon");
	 	
		objAnalystInbox.open_PatientAccount(Constants.Queues.REGULAR, "ACCOUNT_NUMBER", sAccountNumber);
						
		Transaction rSubmit = objClaimTransaction.submit_Claim(sUID, claimStatus, actionCode, resolveType, notes);
		Assert.assertTrue(rSubmit.getStatus(), "<b>Claim not saved. Account ["+sAccountNumber+"], UID ["+sUID+"]</b>");
		logger.log(LogStatus.INFO, "<b>Account moved to Credentialing, "
												+ "Account [" + sAccountNumber + "], UID ["+ sUID +"]<br> <br>"
								
												+ "ClaimStatus ["+claimStatus+"], <br>"
												+ "ActionCode ["+actionCode+"], <br>"
												+ "ResolveType ["+resolveType+"], <br>"
												+ "Notes ["+notes+"]</b>");
		
		expNoOfAccounts += lstExpAccounts.size();
		objDashboard.signOut();
		
		//int expNoOfAccounts = 1;
		
		// login - TL
		objLogin.login(sTLUsername, sTLPassword);			
		objDashboard.navigateTo_Dashboard().navigateTo_ProviderCredential();
		
		int actNoOfAccounts = objReport.get_Report(client, location, practice, sProvider, sPayer, sDOSStart, sDOSEnd);
		utils.save_ScreenshotToReport("ProviderCredentialReport_Final");	
		
		SoftAssert s = new SoftAssert();
		s.assertEquals(actNoOfAccounts, expNoOfAccounts);
		s.assertAll();
		
		logger.log(LogStatus.PASS, "<b>Provider Credential Report verified</b>");
		
		
		objDashboard.signOut();
		
		
		
		
		
		
		
		
		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		Configuration.logger.log(LogStatus.INFO, "Navigate to Transaction -> Inbox");
		
		objAnalystInbox.select_Practice(practice, false);
		objAnalystInbox.click_Image_Regular(true);
	 	Configuration.logger.log(LogStatus.INFO, "Select practice ["+ practice +"] and click REGULAR queue icon");
	 	
	 	
	 	for (String accountNumber : lstExpAccounts) 
	 	{
	 		 // verify account	
			Info search1 = objAnalystInbox.search_Account(accountNumber, Constants.Queues.REGULAR);
			if(search1.getStatus())
			{
				logger.log(LogStatus.FAIL, "<b>Account ["+ accountNumber +"] found at REGULAR</b>");
			}
			
		}
	 	
		 
	}
}
