package transaction;

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
import pageObjectRepository.transaction.AnalystInbox;
import pageObjectRepository.transaction.ClaimTransaction;
import pageObjectRepository.transaction.ReviewAndApprove;

public class Credentialing extends Configuration
{
	
	WebDriverUtils utils = new WebDriverUtils();	
	
	Login objLogin;
	AnalystInbox objAnalystInbox;
	ClaimTransaction objClaimTransaction;
	ReviewAndApprove objReviewAndApprove;	
	Dashboard objDashboard;
	UserWorkedAccountsReport objUserWorkedAccounts;
	
	
	@Test(groups = {"REG"})
	public void verify_Credentialing()
	{
		logger = extent.startTest("Verify Credentialing and Credentialing Release");
		
		int executableRowIndex = excel.isExecutable("verify_Credentialing");		
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
		objAnalystInbox = PageFactory.initElements(driver, AnalystInbox.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objReviewAndApprove = PageFactory.initElements(driver, ReviewAndApprove.class);
		
	
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");	

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		logger.log(LogStatus.INFO, "Navigate to Transaction -> Inbox");
		
		//select practice and select regular image link
		logger.log(LogStatus.INFO, "Select practice ["+ practice +"] and click REGULAR queue icon");
		objAnalystInbox.select_Practice(practice); 
		objAnalystInbox.click_Image_Regular(true);
	 					
	 	
	 	Transaction openAccount = objAnalystInbox.open_PatientAccount(Constants.Queues.REGULAR);			
		Assert.assertTrue(openAccount.getStatus(), openAccount.getDescription());
		
		String totalCharges = objClaimTransaction.get_TotalCharges();
		String sAccountNumber = objClaimTransaction.get_PatientAccountNumber();
		String sEncounterId = objClaimTransaction.get_EncounterID();
		String sUID = objClaimTransaction.get_UID();
					
		
		/* submit account to credentialing	*/					
		Transaction rSubmit = objClaimTransaction.submit_Claim(sUID, claimStatus, actionCode, resolveType, notes);
							
		/* assert submission status	*/
		Assert.assertTrue(rSubmit.getStatus(), rSubmit.getDescription());
		logger.log(LogStatus.INFO, "<b>Account moved to Credentialing, "
											+ "Account [" + sAccountNumber + "], UID ["+ sUID +"] <br> <br>"
											
											+ "ClaimStatus ["+claimStatus+"], <br>"
											+ "ActionCode ["+actionCode+"], <br>"
											+ "ResolveType ["+resolveType+"], <br>"
											+ "Notes ["+notes+"]</b>");	
		
		/* sign out from Analyst	*/
		objDashboard.signOut();
	
		
		
		
		
			
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.MANAGER);
		String sMngrUsername = login.get("ntlg");
		String sMngrPassword = login.get("password");	

		// login - Manager
		objLogin.login(sMngrUsername, sMngrPassword);
		
		/* navigate to review and approve	*/
		objDashboard.navigateTo_Transaction().navigateTo_Review_And_Approve();
		logger.log(LogStatus.INFO, " Navigate to Review and approve");
		
		/* switch to credentialing tab	*/
		objReviewAndApprove.click_Tab_Credentialing();
		
		/* select practice	*/
		objReviewAndApprove.select_Practice(practice);
		logger.log(LogStatus.INFO, "Switched to Credentialing tab and select practice ["+ practice +"]");
		
					
		/* verify account	*/		
		Info search = objReviewAndApprove.search_Account(sAccountNumber, totalCharges);
		Assert.assertTrue(search.getStatus(), search.getDescription());
		logger.log(LogStatus.INFO, search.getDescription());
		
		
		
		
		
		String responseQueue = excel.readValue(sheet, executableRowIndex, "response");		
		data = excel.get_TransactionData(responseQueue);		
		String remarks = data.get("remarks");
		
		Assert.assertTrue(objReviewAndApprove.submit_CredentialingResponse(sEncounterId, remarks)); //,totalCharges
		Configuration.logger.log(LogStatus.INFO, "<b>Account moved to Credentialing Response</b>");
		objDashboard.signOut();
		
		
		
		
	
		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();	
		logger.log(LogStatus.INFO, "Navigate to Transaction -> Inbox");	
		
		objAnalystInbox.select_Practice(practice);				
		objAnalystInbox.click_Image_CredentialingResponse();
		logger.log(LogStatus.INFO, "Select practice [" + practice +"] and Navigate to  Credentialing Response");								
		
		search = objAnalystInbox.search_Account(sAccountNumber, Constants.Queues.CREDENTIALING_RESPONSE);
		Assert.assertTrue(search.getStatus(), search.getDescription());
		logger.log(LogStatus.INFO, search.getDescription());
		
		
	}

}
