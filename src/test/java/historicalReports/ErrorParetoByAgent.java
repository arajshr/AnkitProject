package historicalReports;

import java.util.HashMap;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.Constants;
import customDataType.Transaction;
import customDataType.ErrorByAgentReport;
import customDataType.Info;
import pageObjectRepository.Dashboard;
import pageObjectRepository.Login;
import pageObjectRepository.historicalReports.ErrorParetoByAgentReport;
import pageObjectRepository.qc.QCA_AuditSheet;
import pageObjectRepository.qc.QCDashboard;
import pageObjectRepository.qc.QCInbox;
import pageObjectRepository.transaction.AnalystInbox;
import pageObjectRepository.transaction.ClaimTransaction;

public class ErrorParetoByAgent extends Configuration
{
	Login objLogin;
	Dashboard objDashboard;
	AnalystInbox objInbox;
	ErrorParetoByAgentReport objReport;
	ClaimTransaction objClaimTransaction;	
	QCDashboard objQCDashboard;
	QCInbox objQCInbox;
	QCA_AuditSheet objQCA_AuditSheet;
	
	
	@Test(groups= {"REG"})
	public void verify_ErrorByAgent()
	{
		
		int executableRowIndex = excel.isExecutable("verify_ErrorByAgent");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
	/*	String client = excel.readValue(sheet, executableRowIndex, "client");
		String location = excel.readValue(sheet, executableRowIndex, "location");*/
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
		objReport = PageFactory.initElements(driver, ErrorParetoByAgentReport.class);
		objClaimTransaction = PageFactory.initElements(driver, ClaimTransaction.class);
		
		objQCDashboard = PageFactory.initElements(Configuration.driver, QCDashboard.class);
		objQCInbox = PageFactory.initElements(Configuration.driver, QCInbox.class);
		objQCA_AuditSheet = PageFactory.initElements(Configuration.driver, QCA_AuditSheet.class);
		
		
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.TL);
		String sTLUsername = login.get("ntlg");
		String sTLPassword = login.get("password");	

		// login - TL
		objLogin.login(sTLUsername, sTLPassword);
		
		objDashboard.navigateTo_HistoricalReport().navigateTo_ErrorParetoAgent_Report();	
		
		ErrorByAgentReport error1 = objReport.get_ErrorByAgent_Details(reason, "Initial");
		
		objDashboard.signOut();
		
		
		

		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");	

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
		
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		objInbox.select_Practice(practice);
		logger.log(LogStatus.INFO, "Navigate to Inbox and select practice [" + practice + "]");
		
		objInbox.click_Image_Regular(true);
		logger.log(LogStatus.INFO, "Navigate to REGULAR queue");		
			
		objInbox.open_PatientAccount(Constants.Queues.REGULAR);
		Transaction submitAccount= null;
		
		String sAccountNumber = objClaimTransaction.get_PatientAccountNumber();
		String sUID = objClaimTransaction.get_UID();
		double totalCharges = Double.parseDouble(objClaimTransaction.get_TotalCharges());
		String encounterId = objClaimTransaction.get_EncounterID();
		
			
		submitAccount = objClaimTransaction.submit_Claim(claimStatus, actionCode, resolveType, notes);
		Assert.assertTrue(submitAccount.getStatus(), "Account [" + sAccountNumber +"] not submitted!!");
		logger.log(LogStatus.INFO, "Claim submitted to QCA. Account ["+ sAccountNumber +"], UID ["+ sUID +"], Encounter ["+ encounterId +"]");	
		
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
		logger.log(LogStatus.INFO, "Navigate to QC Inbox, select Practice ["+ practice +"]");
		
	
		Info search = objQCInbox.search_UID(sUID);
		Assert.assertTrue(search.getStatus(), search.getDescription());
		logger.log(LogStatus.INFO, search.getDescription());
		
		objQCInbox.open_Encounter_QCA(sUID);
				
		objQCA_AuditSheet.select_Transaction_Screen("No");
		objQCA_AuditSheet.select_Decision_Calling_Payer("No");
		objQCA_AuditSheet.select_Appeal_Not_Done("No");
		objQCA_AuditSheet.select_Conclusion("No");
		
		
		
		
		
		
		Info status = objQCA_AuditSheet.select_Error_Category(category)
						.select_Error_SubCategory(subCategory)
						.select_Error_MicroCategory(microCategory)
						.select_Error_Behaviour(behaviour)
						.select_Error_ReasonForNC(reason)
						.set_Error_Comments(comments)
						.set_Error_CorrectiveActions(correctiveActions)
						.save_Audit();
		
		Assert.assertTrue(status.getStatus(), "Audit not saved");
		logger.log(LogStatus.INFO, "<b>Encounter audited</b>");
		
		objQCDashboard.signOut();
		
		
		
		
		int newEncounter = error1.getEncounters() + 1;
		double newDollarValue = error1.getDollarValue() + totalCharges;
		double newTotalDollarValue = error1.getTotalDollarValue() + totalCharges;
		
		double newPercent =  Math.round(newTotalDollarValue/newDollarValue * 100.0) / 100.0;
		

		// login - TL
		objLogin.login(sTLUsername, sTLPassword);
		
		objDashboard.navigateTo_HistoricalReport().navigateTo_ErrorParetoAgent_Report();
		
		ErrorByAgentReport error2 = objReport.get_ErrorByAgent_Details(reason, "Final");
		
		SoftAssert s = new SoftAssert();
		s.assertEquals(error2.getEncounters(), newEncounter, "Encounters, ");
		s.assertEquals(error2.getDollarValue(), newDollarValue, "Dollar, ");
		s.assertEquals(error2.getPercent(), newPercent, "Percent, ");
		s.assertAll();
		
		logger.log(LogStatus.INFO, "<b>Error Pareto By Agent Report verified</b>");
	}
}
