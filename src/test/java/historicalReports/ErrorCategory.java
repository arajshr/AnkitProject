package historicalReports;

import java.util.ArrayList;
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
import pageObjectRepository.historicalReports.ErrorCategoryReport;
import pageObjectRepository.historicalReports.SimpleQualityReport;
import pageObjectRepository.qc.QCA_AuditSheet;
import pageObjectRepository.qc.QCDashboard;
import pageObjectRepository.qc.QCInbox;
import pageObjectRepository.transaction.AnalystInbox;
import pageObjectRepository.transaction.ClaimTransaction;

public class ErrorCategory extends Configuration
{
	WebDriverUtils utils = new WebDriverUtils();
	
	Login objLogin;	
	Dashboard objDashboard;
	ErrorCategoryReport objReport;
	
	AnalystInbox objInbox;
	ClaimTransaction objClaimTransaction;	
	
	QCDashboard objQCDashboard;
	QCInbox objQCInbox;
	QCA_AuditSheet objQCA_AuditSheet;
	
	@Test
	public void verify_ErrorCategoryReport_Historical()
	{
		/* 
		 * login - TL
		 * get initial values from report
		 * 
		 * login - analyst
		 * move an account to qca
		 * 
		 * login - qca
		 * set error parameters and save the account
		 * 
		 * login - TL
		 * get values from report and verify
		 * 
		 * */
		
		logger = extent.startTest("Verifying Error Category report");
		
		int executableRowIndex = excel.isExecutable("verify_ErrorCategoryReport_Historical");		
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
		objReport = PageFactory.initElements(driver, ErrorCategoryReport.class);
		objInbox = PageFactory.initElements(driver, AnalystInbox.class);
		objClaimTransaction = PageFactory.initElements(driver, ClaimTransaction.class);
		
		objQCDashboard = PageFactory.initElements(driver, QCDashboard.class);
		objQCInbox = PageFactory.initElements(driver, QCInbox.class);
		objQCA_AuditSheet = PageFactory.initElements(driver, QCA_AuditSheet.class);
		
		
		
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.TL);
		String sMngrUsername = login.get("ntlg");
		String sMngrPassword = login.get("password");	

		// login - Manager
		objLogin.login(sMngrUsername, sMngrPassword);
		objDashboard.navigateTo_HistoricalReport().navigateTo_lnkErrorCategory_Report();
		logger.log(LogStatus.INFO, "Naviagte to Reports -> Error Category Report");
		
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");
		String sAnalystName = login.get("name");
				
		logger.log(LogStatus.INFO, "Get initial values from report for User ["+ sAnalystName +"]");		
		HashMap<String, Integer> expValues = objReport.getReport(client, location, practice, sAnalystName, category, subCategory, microCategory, reason);
		
		utils.save_ScreenshotToReport("ErrorCategory_Initial");		
		objDashboard.signOut();
		
		
		
		
		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);	
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		objInbox.select_Practice(practice);
		logger.log(LogStatus.INFO, "Navigate to Inbox and select practice [" + practice + "] and click REGULAR queue");		
		
		objInbox.click_Image_Regular(true);
		
		
		Transaction openAccount = objInbox.open_PatientAccount(Constants.Queues.REGULAR);
		String sAccountNumber1 = objClaimTransaction.get_PatientAccountNumber();
		String sUId1 = objClaimTransaction.get_UID();
		
		Assert.assertTrue(openAccount.getStatus(), openAccount.getDescription());
		//logger.log(LogStatus.INFO,  "Open account [" + sAccountNumber1 + "]");		
		
		Transaction rSubmit = objClaimTransaction.submit_Claim(claimStatus, actionCode, resolveType, notes);
		Assert.assertTrue(rSubmit.getStatus(), "Account [" + sAccountNumber1 + "] not submitted");
		logger.log(LogStatus.INFO, "<b>Account moved to Coding Inbox, "
											+ "Account [" + sAccountNumber1 + "], UID ["+ sUId1 +"]<br> <br>"
							
											+ "ClaimStatus ["+claimStatus+"], <br>"
											+ "ActionCode ["+actionCode+"], <br>"
											+ "ResolveType ["+resolveType+"], <br>"
											+ "Notes ["+notes+"]</b>");
		
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
		Info search2 = objQCInbox.search_UID(sUId1);
		Assert.assertTrue(search2.getStatus(), search2.getDescription());
		logger.log(LogStatus.INFO, search2.getDescription());
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
		logger.log(LogStatus.INFO, "<b>Claim audited with following error details <br><br> "
											+ "Category ["+category+"], <br>"
											+ "Sub Category ["+subCategory+"], <br>"
											+ "Micro Category ["+microCategory+"],<br> "
											+ "Behaviour ["+behaviour+"],<br> "
											+ "Reason NC ["+reason+"],<br> "
											+ "Corrective Actions ["+correctiveActions+"],<br> "
											+ "Comments["+comments+"] <b>");
		
		
		objReport.update_ReportDetails(expValues, 1);
		
		objQCDashboard.signOut();
		
		
		
		
		// login - Manager
		objLogin.login(sMngrUsername, sMngrPassword);
		objDashboard.navigateTo_HistoricalReport().navigateTo_lnkErrorCategory_Report();
		logger.log(LogStatus.INFO, "Naviagte to Reports -> Error Category Report");
				
		HashMap<String, Integer> actValues = objReport.getReport(client, location, practice, sAnalystName, category, subCategory, microCategory, reason);
		utils.save_ScreenshotToReport("ErrorCategory_Final");
		
		objReport.verify_Report(expValues, actValues).assertAll();
		logger.log(LogStatus.PASS, "<b>Error Category Report verified</b>");
		
	}

}
