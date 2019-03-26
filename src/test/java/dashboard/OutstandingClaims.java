package dashboard;

import java.util.HashMap;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.Constants;
import configuration.WebDriverUtils;
import customDataType.Transaction;
import pageObjectRepository.Dashboard;
import pageObjectRepository.Login;
import pageObjectRepository.dashboard.OutstandingClaimsReport;
import pageObjectRepository.transaction.AnalystInbox;
import pageObjectRepository.transaction.ClaimTransaction;

public class OutstandingClaims extends Configuration
{
	WebDriverUtils utils = new WebDriverUtils();
	
	Login objLogin;
	AnalystInbox objAnalystInbox;
	ClaimTransaction objClaimTransaction;
	Dashboard objDashboard;
	OutstandingClaimsReport objReport;
	
	@Test
	public void verify_OutStandingClaims_Dashboard()
	{
		int executableRowIndex = excel.isExecutable("verify_OutStandingClaims_Dashboard");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		String practice = excel.readValue(sheet, executableRowIndex, "practice");
		
		
		String nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue1");		
		HashMap<String, String> data = excel.get_TransactionData(nextQueue);
		
		String claimStatus = data.get("claim_status");
		String actionCode = data.get("action_code");
		String resolveType = data.get("resolve_type"); 
		String notes = data.get("note");
		
		
		objLogin = PageFactory.initElements(driver, Login.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objAnalystInbox = PageFactory.initElements(driver, AnalystInbox.class);
		objClaimTransaction = PageFactory.initElements(driver, ClaimTransaction.class);
		objReport = PageFactory.initElements(driver, OutstandingClaimsReport.class);
		


		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.TL);
		String sMngrUsername = login.get("ntlg");
		String sMngrPassword = login.get("password");	

		// login - Manager
		objLogin.login(sMngrUsername, sMngrPassword);
		objDashboard.navigateTo_Dashboard().navigateTo_OutStandingClaims();
		
		HashMap<String, Integer> expClaimsData = objReport.get_OutstandingClaims(nextQueue);
		
		utils.save_ScreenshotToReport("verify_OutStandingClaimsReport");		
		objDashboard.signOut();
		
		
		
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");	

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		Configuration.logger.log(LogStatus.INFO, "Navigate to Transaction -> Inbox");
		
		objAnalystInbox.select_Practice(practice);
		objAnalystInbox.click_Image_Regular(true);
	 	Configuration.logger.log(LogStatus.INFO, "Select practice ["+ practice +"] and click REGULAR queue icon");
	 	
		objAnalystInbox.open_PatientAccount(Constants.Queues.REGULAR);
		String accountNumber = objClaimTransaction.get_PatientAccountNumber();
		String sUID = objClaimTransaction.get_UID();
		/*Assert.assertTrue(openAccount.getStatus(), openAccount.getDescription());		
		logger.log(LogStatus.INFO,  "Open account [" + openAccount.getPateintAccountNumber() + "]");*/	
				
				
		Transaction rSubmit = objClaimTransaction.submit_Claim(sUID, claimStatus, actionCode, resolveType, notes);
		Assert.assertTrue(rSubmit.getStatus(), "Account [" + accountNumber +"] not submitted");
		logger.log(LogStatus.INFO, "<b>Account moved to Coding</b>");
				
		expClaimsData = objReport.update_OutStandingClaims(expClaimsData);
		
		objDashboard.signOut();
		
		
		// login - Manager
		objLogin.login(sMngrUsername, sMngrPassword);
		objDashboard.navigateTo_Dashboard().navigateTo_Appeals_PercentOfResolution();	
		
		utils.save_ScreenshotToReport("verify_OutStandingClaimsReport");	
		
		HashMap<String, Integer> actClaimsData = objReport.get_OutstandingClaims(nextQueue);
		
		SoftAssert s = new SoftAssert();
		s.assertEquals(actClaimsData.get("Value"), expClaimsData.get("Value"));
		s.assertEquals(actClaimsData.get("rowTotal"), expClaimsData.get("rowTotal"));
		s.assertEquals(actClaimsData.get("colTotal"), expClaimsData.get("colTotal"));
		
		s.assertAll();
	}
}
