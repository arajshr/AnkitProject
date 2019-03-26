package historicalReports;

import java.util.ArrayList;
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
import customDataType.Info;
import customDataType.Quality;
import customDataType.Transaction;
import pageObjectRepository.Dashboard;
import pageObjectRepository.Login;
import pageObjectRepository.historicalReports.SimpleQualityReport;
import pageObjectRepository.qc.QCA_AuditSheet;
import pageObjectRepository.qc.QCDashboard;
import pageObjectRepository.qc.QCInbox;
import pageObjectRepository.transaction.AnalystInbox;
import pageObjectRepository.transaction.ClaimTransaction;
import pageObjectRepository.transaction.FeedBack;
import pageObjectRepository.transaction.RebuttalReview;

public class SimpleQuality extends Configuration
{
	WebDriverUtils utils = new WebDriverUtils();
	
	Login objLogin;
	AnalystInbox objInbox;
	ClaimTransaction objClaimTransaction;	
	
	QCDashboard objQCDashboard;
	QCInbox objQCInbox;
	QCA_AuditSheet objQCA_AuditSheet;
	Dashboard objDashboard;
	FeedBack objFeedBack;
	RebuttalReview objRebuttalReview;
	SimpleQualityReport objReport;
	
	@Test(groups= {"REG"})
	public void verify_SimpleQuality_Report()
	{		
		logger = extent.startTest("Verifying User Quality report");
		
		int executableRowIndex = excel.isExecutable("verify_SimpleQuality_Report");		
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
		
		String tc_id = excel.readValue(sheet, executableRowIndex, "qcaErrorParameters");		
		HashMap<String, String> errorParameters = excel.get_QCAErrorParameters(tc_id);
		
		String category = errorParameters.get("category");
		String subCategory = errorParameters.get("subCategory");
		String microCategory = errorParameters.get("microCategory"); 
		String behaviour = errorParameters.get("behaviour");
		String reason = errorParameters.get("reason");
		String comments = errorParameters.get("comments"); 
		String correctiveActions = errorParameters.get("correctiveActions");
		
		
		
		
		objLogin = PageFactory.initElements(driver, Login.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objInbox = PageFactory.initElements(driver, AnalystInbox.class);
		objClaimTransaction = PageFactory.initElements(driver, ClaimTransaction.class);
		
		objQCDashboard = PageFactory.initElements(driver, QCDashboard.class);
		objQCInbox = PageFactory.initElements(driver, QCInbox.class);
		objQCA_AuditSheet = PageFactory.initElements(driver, QCA_AuditSheet.class);
		objReport = PageFactory.initElements(driver, SimpleQualityReport.class);
		
		
		
		
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.TL);
		String sTLUsername = login.get("ntlg");
		String sTLPassword = login.get("password");	
				
		// login - TL
		objLogin.login(sTLUsername, sTLPassword);
		
		objDashboard.navigateTo_HistoricalReport().navigateTo_UserQuality_Report();
		logger.log(LogStatus.INFO, "Naviagte to Reports -> User Quality Report");
		
		// get reprt
		objReport.select_Client(client)
				.select_Location(location)
				.select_Practice(practice)
				.get_Report();
		
		
		
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");
		String sAnalystName = login.get("name");
				
				
		
		logger.log(LogStatus.INFO, "Get initial values from Report for User ["+ sAnalystName +"]");
		utils.save_ScreenshotToReport("UserQualityReport_Final");
		
				
		Quality expQuality = objReport.get_Quality_Details(sAnalystName);		
		logger.log(LogStatus.INFO, "Initial values from report,  "
											+ "Worked - " + expQuality.getWorked()  
											+ ", Audited - " + expQuality.getAudited()  
											+ ", Error - " + expQuality.getError() 
											+ ", Error Percent - " +  expQuality.getErrorPercent() 
											+ ", Accuracy - " + expQuality.getAccuracy());
		
		System.out.println(expQuality.getWorked()  + ", " + expQuality.getAudited()  + ", " + expQuality.getError() + ", " +  expQuality.getErrorPercent() + ", " + expQuality.getAccuracy());
		
		
		
		objDashboard.signOut();
				
		
		
			

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);	
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		objInbox.select_Practice(practice);
		logger.log(LogStatus.INFO, "Navigate to Inbox and select practice [" + practice + "]");		
		
		objInbox.click_Image_Regular(true);
		logger.log(LogStatus.INFO, "Navigate to Regular queue");
				
		
		Transaction openAccount = objInbox.open_PatientAccount(Constants.Queues.REGULAR);
		String sAccountNumber1 = objClaimTransaction.get_PatientAccountNumber();
		String sUId1 = objClaimTransaction.get_UID();
		
		Assert.assertTrue(openAccount.getStatus(), openAccount.getDescription());
		//logger.log(LogStatus.INFO,  "Open account [" + sAccountNumber1 + "]");		
		
		Transaction rSubmit = objClaimTransaction.submit_Claim(claimStatus, actionCode, resolveType, notes);
		Assert.assertTrue(rSubmit.getStatus(), "Account [" + sAccountNumber1 + "] not submitted");
		logger.log(LogStatus.INFO, "<b>Account [" + sAccountNumber1 + "], UID ["+ sUId1 +"] moved to QCA</b>");	
		
		
		
		openAccount = objInbox.open_PatientAccount(Constants.Queues.REGULAR);
		String sAccountNumber2 = objClaimTransaction.get_PatientAccountNumber();
		String sUId2 = objClaimTransaction.get_UID();
		
		Assert.assertTrue(openAccount.getStatus(), openAccount.getDescription());
		//logger.log(LogStatus.INFO,  "Open account [" + sAccountNumber2 + "]");		
		
		rSubmit = objClaimTransaction.submit_Claim(claimStatus, actionCode, resolveType, notes);
		Assert.assertTrue(rSubmit.getStatus(), "Account [" + sAccountNumber2 + "] not submitted");
		logger.log(LogStatus.INFO, "<b>Account [" + sAccountNumber2 + "], UID ["+ sUId2+"] moved to QCA</b>");	
		
		
		objDashboard.signOut();
		
		
		
		/*String sAccountNumber = "20181008-601";
		String sUId = "20181008-601";*/
		


		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.QCA);
		String sQCAUsername = login.get("ntlg");
		String sQCAPassword = login.get("password");	

		// login - QCA
		objLogin.login(sQCAUsername, sQCAPassword);
		
		objQCDashboard.navigateTo_QCInbox();
		logger.log(LogStatus.INFO, "Navigate to QC Inbox");
		
		objQCInbox.close_Accounts_For_Audit_Popup();		
		objQCInbox.filter_QC_Accounts(practice); 
		logger.log(LogStatus.INFO, "Get Accounts for Practice ["+ practice +"]");

		// search and open first account
		Info search1 = objQCInbox.search_UID(sUId1);
		Assert.assertTrue(search1.getStatus(), search1.getDescription());
		logger.log(LogStatus.INFO, search1.getDescription());
		objQCInbox.open_Encounter_QCA(sUId1);
		
		// set error parameters
		ArrayList <String> checkList = new ArrayList<>();		
		checkList.add(objQCA_AuditSheet.select_Transaction_Screen("No"));
		checkList.add(objQCA_AuditSheet.select_Decision_Calling_Payer("No"));
		checkList.add(objQCA_AuditSheet.select_Appeal_Not_Done("No"));
		checkList.add(objQCA_AuditSheet.select_Conclusion("No"));
		
		
		Info status = objQCA_AuditSheet.select_Error_Category(category)
						.select_Error_SubCategory(subCategory)
						.select_Error_MicroCategory(microCategory)
						.select_Error_Behaviour(behaviour)
						.select_Error_ReasonForNC(reason)
						.set_Error_Comments(comments)
						.set_Error_CorrectiveActions(correctiveActions)
						.save_Audit();
		
		Assert.assertTrue(status.getStatus(), "QC Audit not saved");
		logger.log(LogStatus.INFO, "<b>UID [" + sUId1 +"] audited  [with error parameters ]</b>");
		
		
		
		// search and open second account
		Info search2 = objQCInbox.search_UID(sUId2);
		Assert.assertTrue(search2.getStatus(), search2.getDescription());
		logger.log(LogStatus.INFO, search2.getDescription());
		objQCInbox.open_Encounter_QCA(sUId2);
		
		status = objQCA_AuditSheet.set_Error_Comments(comments)
					.save_Audit();
		
		Assert.assertTrue(status.getStatus(), "QC Audit not saved");
		logger.log(LogStatus.INFO, "<b>UID [" + sUId2 +"] audited  [No errors]</b>");
		
		
		
		expQuality.update_Quality(2, 2, 1);
		System.out.println(expQuality.getWorked()  + ", " + expQuality.getAudited()  + ", " + expQuality.getError() + ", " +  expQuality.getErrorPercent() + ", " + expQuality.getAccuracy());
		
		
		objQCDashboard.signOut();
		
		
		
		
		/*Quality expQuality = new Quality(5, 2, 2, 100.00, 0.00);
		
		expQuality.update_Quality(2, 2, 1);*/
		
		
		// login - TL
		objLogin.login(sTLUsername, sTLPassword);
		
		objDashboard.navigateTo_HistoricalReport().navigateTo_UserQuality_Report();
		logger.log(LogStatus.INFO, "Naviagte to User Quality Report");
		
		objReport.select_Client(client)
				.select_Location(location)
				.select_Practice(practice)
				.get_Report();
		
		Quality actQuality =  objReport.get_Quality_Details(sAnalystName);
		utils.save_ScreenshotToReport("UserQualityReport_Final");
		
		
		logger.log(LogStatus.INFO, "Expected values from report,  "
				+ "Worked - " + expQuality.getWorked()  
				+ ", Audited - " + expQuality.getAudited()  
				+ ", Error - " + expQuality.getError() 
				+ ", Error Percent - " +  expQuality.getErrorPercent() 
				+ ", Accuracy - " + expQuality.getAccuracy());
		
		
		logger.log(LogStatus.INFO, "Actual values from report,  "
				+ "Worked - " + actQuality.getWorked()  
				+ ", Audited - " + actQuality.getAudited()  
				+ ", Error - " + actQuality.getError() 
				+ ", Error Percent - " +  actQuality.getErrorPercent() 
				+ ", Accuracy - " + actQuality.getAccuracy());
		
		System.out.println(actQuality.getWorked()  + ", " + actQuality.getAudited()  + ", " + actQuality.getError() + ", " +  actQuality.getErrorPercent() + ", " + actQuality.getAccuracy());
		
		
		SoftAssert s = new SoftAssert();
		s.assertEquals(actQuality.getWorked(), expQuality.getWorked());
		s.assertEquals(actQuality.getAudited(),expQuality.getAudited());
		s.assertEquals(actQuality.getError(),expQuality.getError());
		s.assertEquals(actQuality.getErrorPercent(),expQuality.getErrorPercent());
		s.assertEquals(actQuality.getAccuracy(),expQuality.getAccuracy());
		s.assertAll();
		
		logger.log(LogStatus.PASS, "<b>User Quality Report verified</b>");
		
	}
}
