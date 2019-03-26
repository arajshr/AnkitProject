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
import pageObjectRepository.dashboard.AppealsResolutionReport;
import pageObjectRepository.transaction.AnalystInbox;
import pageObjectRepository.transaction.ClaimTransaction;

public class AppealsResolution extends Configuration
{	
	WebDriverUtils utils = new WebDriverUtils();
	
	Login objLogin;
	AnalystInbox objAnalystInbox;
	ClaimTransaction objClaimTransaction;
	Dashboard objDashboard;
	AppealsResolutionReport objReport;
	
	
	/**
	 * login - TL
	 * get initial values from report
	 * sign out
	 * 
	 * login - Analyst
	 * submit account [should have payment data]
	 * sign out 
	 * 
	 * login - TL
	 * verify values in report
	 * sign out
	 */
	@Test
	public void verify_AppealsPercentOfResolution_Dashboard()
	{
		int executableRowIndex = excel.isExecutable("verify_AppealsPercentOfResolution_Dashboard");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		
		String practice = excel.readValue(sheet, executableRowIndex, "practice");
		String insurance = excel.readValue(sheet, executableRowIndex, "insurance");
		
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
		objReport = PageFactory.initElements(driver, AppealsResolutionReport.class);
		
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.TL);
		String sTLUsername = login.get("ntlg");
		String sTLPassword = login.get("password");	

		// login - TL
		objLogin.login(sTLUsername, sTLPassword);	
		objDashboard.navigateTo_Dashboard().navigateTo_Appeals_PercentOfResolution();	
		
		HashMap<String, Integer> expAppeals = objReport.get_AppealsReportData(insurance);
		
		utils.save_ScreenshotToReport("verify_Appeals_PercentOfResolution");		
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
		String sAccountNumber = objClaimTransaction.get_PatientAccountNumber();
		String sUID = objClaimTransaction.get_UID();
		
		/*Assert.assertTrue(openAccount.getStatus(), openAccount.getDescription());		
		logger.log(LogStatus.INFO,  "Open account [" + openAccount.getPateintAccountNumber() + "]");*/	
		
		boolean bPayment = objClaimTransaction.hasPayment();
				
		Transaction rSubmit = objClaimTransaction.submit_Claim(sUID, claimStatus, actionCode, resolveType, notes);
		Assert.assertTrue(rSubmit.getStatus(), "Cliam not saved. Account [" + sAccountNumber +"], UID ["+sUID+"]");
		logger.log(LogStatus.INFO, "<b>Claim moved to Appeals. Account Number ["+sAccountNumber+"], UID ["+sUID+"]</b>");
				
		expAppeals = objReport.update_AppealsReportData(expAppeals, rSubmit.getStatus() == true ? 1 : 0, bPayment == true ? 1 : 0);
		
		objDashboard.signOut();
		
		
			
		
		// login - TL
		objLogin.login(sTLUsername, sTLPassword);
		objDashboard.navigateTo_Dashboard().navigateTo_Appeals_PercentOfResolution();	
		
		HashMap<String, Integer> actAppeals = objReport.get_AppealsReportData(insurance);		
		utils.save_ScreenshotToReport("verify_Appeals_PercentOfResolution");	
		
		SoftAssert s = new SoftAssert();
		s.assertEquals(actAppeals.get("totalAppealsSent"), expAppeals.get("totalAppealsSent"));
		s.assertEquals(actAppeals.get("appealsInPPReport"), expAppeals.get("appealsInPPReport"));
		s.assertEquals(actAppeals.get("percentOfResolution"), expAppeals.get("percentOfResolution"));
		
		s.assertAll();		
	}

}
