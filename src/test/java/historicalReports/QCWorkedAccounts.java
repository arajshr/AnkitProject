package historicalReports;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import pageObjectRepository.historicalReports.QCWorkedAccountsReport;
import pageObjectRepository.qc.QCA_AuditSheet;
import pageObjectRepository.qc.QCDashboard;
import pageObjectRepository.qc.QCInbox;
import pageObjectRepository.transaction.AnalystInbox;
import pageObjectRepository.transaction.ClaimTransaction;
import pageObjectRepository.transaction.FeedBack;
import pageObjectRepository.transaction.RebuttalReview;

public class QCWorkedAccounts extends Configuration
{	
	Login objLogin;
	AnalystInbox objInbox;
	ClaimTransaction objClaimTransaction;	
	QCDashboard objQCDashboard;
	QCInbox objQCInbox;
	QCA_AuditSheet objQCA_AuditSheet;	
	Dashboard objDashboard;
	FeedBack objFeedBack;
	RebuttalReview objRebuttalReview;
	QCWorkedAccountsReport objReport;
	
	String fromDate = new SimpleDateFormat("MM/dd/yy").format(new Date()); // -- MM/dd/yyyy
	ArrayList <String> checkList = new ArrayList<>();
	
	@Test(groups= {"REG"})
	public void verify_QCWorkedAccounts_Report()
	{
		logger = extent.startTest("Verifying QC Worked Accounts report");
		
		int executableRowIndex = excel.isExecutable("verify_QCWorkedAccounts_Report");		
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
		
		
				
		objLogin = PageFactory.initElements(driver, Login.class);
		objDashboard = PageFactory.initElements(Configuration.driver, Dashboard.class);
		objInbox = PageFactory.initElements(driver, AnalystInbox.class);
		objClaimTransaction = PageFactory.initElements(driver, ClaimTransaction.class);
		
		objQCDashboard = PageFactory.initElements(Configuration.driver, QCDashboard.class);
		objQCInbox = PageFactory.initElements(Configuration.driver, QCInbox.class);
		objQCA_AuditSheet = PageFactory.initElements(Configuration.driver, QCA_AuditSheet.class);
				
		objFeedBack = PageFactory.initElements(Configuration.driver, FeedBack.class);
		objRebuttalReview = PageFactory.initElements(driver, RebuttalReview.class);
		objReport = PageFactory.initElements(driver, QCWorkedAccountsReport.class);
		


		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");
		

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);	
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		objInbox.select_Practice(practice);
		logger.log(LogStatus.INFO, "Navigate to Inbox and select practice [" + practice + "]");
				
		objInbox.click_Image_Regular(true);
		
		objInbox.open_PatientAccount(Constants.Queues.REGULAR);
		Transaction submitAccount = null;
		
		String sAccountNumber = objClaimTransaction.get_PatientAccountNumber();		
		String sUID = objClaimTransaction.get_UID();
		String sEncounterID = objClaimTransaction.get_EncounterID();
		String sTotalCharges = objClaimTransaction.get_TotalCharges();
		
		submitAccount = objClaimTransaction.submit_Claim(claimStatus, actionCode, resolveType, notes);
		
				
		Assert.assertTrue(submitAccount.getStatus(), "Account [" + sAccountNumber +"] not submitted");
		logger.log(LogStatus.INFO, "<b>Claim submitted from Regular to QCA. Account ["+sAccountNumber+"], UID ["+sUID+"]</b>");	
		
		objDashboard.signOut();
		
		
		
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.QCA);
		String sQCAUsername = login.get("ntlg");
		String sQCAPassword = login.get("password");	
		String sQCAName = login.get("name");
		
		
		// login - QCA
		objLogin.login(sQCAUsername, sQCAPassword);
		
		objQCDashboard.navigateTo_QCInbox();
		objQCInbox.close_Accounts_For_Audit_Popup();
					
		
		objQCInbox.filter_QC_Accounts(practice, fromDate); 
		logger.log(LogStatus.INFO, "Navigate to QC Inbox, select Practice ["+ practice +"] and from date as ["+ fromDate +"]");					
		
		Info search2 = objQCInbox.search_UID(sUID);
		Assert.assertTrue(search2.getStatus(), search2.getDescription());
		logger.log(LogStatus.INFO, search2.getDescription());	
		
		objQCInbox.open_Encounter_QCA(sUID);
							
		Assert.assertTrue(objQCA_AuditSheet.save_Audit().getStatus(), "Account not saved");
		logger.log(LogStatus.INFO, "<b>Encounter audited  [No Errors]</b>");
		
		objDashboard.signOut();
		
		/*String sTotalCharges = "725.00";		
		String sEncounterID = "20181008-617";
		String sUID = "20181008-617";*/
		
				
				
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.TL);
		String sTLUsername = login.get("ntlg");
		String sTLPassword = login.get("password");	

		// login - TL
		objLogin.login(sTLUsername, sTLPassword);
		
		objDashboard.navigateTo_HistoricalReport().navigateTo_QCWorkedAccounts_Report();
		logger.log(LogStatus.INFO, "Navigate to QC Worked Accounts Report");
		
		double total = Math.round(Double.parseDouble(sTotalCharges) * 100.0) / 100.0;
		Assert.assertTrue(objReport.verify_Report(sEncounterID, fromDate, client, location, practice, sQCAName.trim(), Double.toString(total)));
		logger.log(LogStatus.PASS, "UID ["+ sUID +"] verified in QC Worked Accounts Report");					
		
	}
}
