package qc;

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
import pageObjectRepository.Login;
import pageObjectRepository.historicalReports.QCWorkedAccountsReport;
import pageObjectRepository.Dashboard;
import pageObjectRepository.qc.QCA_AuditSheet;
import pageObjectRepository.qc.QCDashboard;
import pageObjectRepository.qc.QCInbox;
import pageObjectRepository.transaction.AnalystInbox;
import pageObjectRepository.transaction.ClaimTransaction;
import pageObjectRepository.transaction.FeedBack;
import pageObjectRepository.transaction.RebuttalReview;

public class Rebuttal_Review extends Configuration 
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
	QCWorkedAccountsReport objReport;
	
	
	ArrayList <String> checkList = new ArrayList<>();	
	
	
	@Test(groups= {"REG"})
	public void verify_RebuttalReview_Accept()
	{
		logger = extent.startTest("Accept agent's feedback in Rebuttal Review");
		
		int executableRowIndex = excel.isExecutable("verify_RebuttalReview_Accept");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		
		String practice = excel.readValue(sheet, executableRowIndex, "practice");
		String feedback = excel.readValue(sheet, executableRowIndex, "feedback");	
		
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
		objInbox.click_Image_Regular(true);
		logger.log(LogStatus.INFO, "Navigate to Inbox, select practice [" + practice + "] and click REGULAR queue");
		
		
		String[] claimIDs = submitAccount(claimStatus, actionCode, resolveType, notes); //{"4589811-5836", "4589811-5836"}; 
		
		if(claimIDs.length > 0)
		{
			
			String sUID = claimIDs[0]; 
			String sEncounterID = claimIDs[1]; 
			
			
			String tc_id = excel.readValue(sheet, executableRowIndex, "qcaErrorParameters");		
			HashMap<String, String> errorParameters = excel.get_QCAErrorParameters(tc_id);
			
			String category = errorParameters.get("category");
			String subCategory = errorParameters.get("subCategory");
			String microCategory = errorParameters.get("microCategory"); 
			String behaviour = errorParameters.get("behaviour");
			String reason = errorParameters.get("reason");
			String comments = errorParameters.get("comments"); 
			String correctiveActions = errorParameters.get("correctiveActions");
			
			audit_QC(sUID, practice, category, subCategory, microCategory, behaviour, reason,  comments,correctiveActions);
			
			feedback_reject(sEncounterID, checkList, practice, feedback, claimStatus, actionCode, notes, category, subCategory, microCategory, behaviour, reason,  comments,correctiveActions);
			
			rebuttalReview(sEncounterID, "ACCEPT", practice, feedback, comments, correctiveActions); // category, subCategory, microCategory, behaviour, reason,
			
			/*objDashboard.navigateTo_HistoricalReport().navigateTo_QC_Worked_Accounts();
			logger.log(LogStatus.INFO, "Navigate to QC Worked Accounts Report");					
			Assert.assertTrue(objReport.verify_Report(encounterID, fromDate));
			logger.log(LogStatus.PASS, "Encounter ["+ encounterID +"] verified in report");*/
			
		}
		else
			logger.log(LogStatus.ERROR, "<b>There are no accounts in REGULAR queue!!</b>"); 
		
	}
	
	
	
	@Test(enabled = false,groups= {"REG"})
	public void verify_RebuttalReview_Reject()
	{		
		

		int executableRowIndex = excel.isExecutable("verify_RebuttalReview_Reject");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		
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
		logger.log(LogStatus.INFO, "Navigate to Regular queue");

		String[] claimIDs = submitAccount(claimStatus, actionCode, resolveType, notes); 
		
		if(claimIDs.length > 0)
		{
			
			String sUID = claimIDs[0]; 
			String sEncounterID = claimIDs[1]; 
			
			
			String tc_id = excel.readValue(sheet, executableRowIndex, "qcaErrorParameters");		
			HashMap<String, String> errorParameters = excel.get_QCAErrorParameters(tc_id);
			
			String category = errorParameters.get("category");
			String subCategory = errorParameters.get("subCategory");
			String microCategory = errorParameters.get("microCategory"); 
			String behaviour = errorParameters.get("behaviour");
			String reason = errorParameters.get("reason");
			String comments = errorParameters.get("comments"); 
			String correctiveActions = errorParameters.get("correctiveActions");
			
			audit_QC(sUID, practice, category, subCategory, microCategory, behaviour, reason,  comments,correctiveActions);
			
			
			String feedback = excel.readValue(sheet, executableRowIndex, "feedback");			
			
			feedback_reject(sEncounterID, checkList,practice, feedback, claimStatus, actionCode, notes, category, subCategory, microCategory, behaviour, reason,  comments, correctiveActions);
			
			rebuttalReview(sEncounterID, "ACCEPT", practice, feedback, comments, correctiveActions);  // category, subCategory, microCategory, behaviour, reason,
			
			/*objDashboard.navigateTo_HistoricalReport().navigateTo_QC_Worked_Accounts();
			logger.log(LogStatus.INFO, "Navigate to QC Worked Accounts Report");					
			Assert.assertTrue(objReport.verify_Report(encounterID, fromDate));
			logger.log(LogStatus.PASS, "Encounter ["+ encounterID +"] verified in report");*/
			
		}
		else
			logger.log(LogStatus.ERROR, "<b>There are no accounts in REGULAR queue!!</b>"); 
				
	}
	
	
	
	private String[] submitAccount(String claimStatus, String actionCode, String resolveType, String notes)
	{
		Transaction rSubmit = null;	
		objInbox.open_PatientAccount(Constants.Queues.REGULAR);
		
		String sAccountNumber = objClaimTransaction.get_PatientAccountNumber();
		String sUID = objClaimTransaction.get_UID();
		String sEncounterID = objClaimTransaction.get_EncounterID();
				
		rSubmit = objClaimTransaction.submit_Claim(claimStatus, actionCode, resolveType, notes);
		
		Assert.assertTrue(rSubmit.getStatus(), rSubmit.getDescription());
		logger.log(LogStatus.INFO, "<b>Account moved to QCA, "
											+ "Account [" + sAccountNumber + "], UID ["+ sUID +"]<br> <br>"
							
											+ "ClaimStatus ["+claimStatus+"], <br>"
											+ "ActionCode ["+actionCode+"], <br>"
											+ "ResolveType ["+resolveType+"], <br>"
											+ "Notes ["+notes+"]</b>");
		
		objDashboard.signOut();
		
		return new String[] {sUID, sEncounterID};
		
		
	}
	
	private void audit_QC(String sUID, String practice, String category, String subCategory, String microCategory, String behaviour, String reason,  String comments,String correctiveActions) 
	{
				
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.QCA);
		String sQCAUsername = login.get("ntlg");
		String sQCAPassword = login.get("password");	

		// login - QCA
		objLogin.login(sQCAUsername, sQCAPassword);
		
		objQCDashboard.navigateTo_QCInbox();
		objQCInbox.close_Accounts_For_Audit_Popup();	
		objQCInbox.filter_QC_Accounts(practice, utils.getToday()); 
		logger.log(LogStatus.INFO, "Navigate to QC Inbox, select Practice ["+ practice +"] and from date as ["+ utils.getToday() +"]");
		
		Info search2 = objQCInbox.search_UID(sUID);
		Assert.assertTrue(search2.getStatus(), search2.getDescription());
		logger.log(LogStatus.INFO, search2.getDescription());							
		
		objQCInbox.open_Encounter_QCA(sUID);		 
		
		checkList.add(objQCA_AuditSheet.select_Transaction_Screen("No"));
		checkList.add(objQCA_AuditSheet.select_Decision_Calling_Payer("No"));
		checkList.add(objQCA_AuditSheet.select_Appeal_Not_Done("No"));
		checkList.add(objQCA_AuditSheet.select_Conclusion("No"));
		
		
		Info audit = objQCA_AuditSheet.select_Error_Category(category)
					.select_Error_SubCategory(subCategory)
					.select_Error_MicroCategory(microCategory)
					.select_Error_Behaviour(behaviour)
					.select_Error_ReasonForNC(reason)
					.set_Error_Comments(comments)
					.set_Error_CorrectiveActions(correctiveActions)
					.save_Audit();
			
		
		Assert.assertTrue(audit.getStatus(), audit.getDescription());
		logger.log(LogStatus.INFO, "<b>Encounter audited with following error details <br><br> "
											+ "Category ["+category+"], <br>"
											+ "Sub Category ["+subCategory+"], <br>"
											+ "Micro Category ["+microCategory+"],<br> "
											+ "Behaviour ["+behaviour+"],<br> "
											+ "Reason NC ["+reason+"],<br> "
											+ "Corrective Actions ["+correctiveActions+"],<br> "
											+ "Comments["+comments+"] <b>");
				
		objDashboard.signOut();
	}
	
	
	private void feedback_reject(String sEncounterID, ArrayList<String> checkList, String practice, String feedback, 
			String claimStatus, String actionCode, String notes, String category, String subCategory, String microCategory, String behaviour, String reason,  String comments,String correctiveActions)
	{
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");	

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
		
		objDashboard.navigateTo_Transaction().navigateTo_Feedback();
		objFeedBack.select_Practice(practice, false);
		logger.log(LogStatus.INFO, "Navigate to Transaction -> Feedback and select practice [" +practice +"]");
		
		
		Info search = objFeedBack.search_Encounter(sEncounterID, claimStatus, actionCode, notes, category, subCategory, microCategory, behaviour, reason,  comments, correctiveActions);
		Assert.assertTrue(search.getStatus(), search.getDescription());
		logger.log(LogStatus.INFO, search.getDescription());
		
		objFeedBack.verify_Error_CheckList(sEncounterID, checkList);
		
		if(objFeedBack.reject(sEncounterID, feedback))
		{
			logger.log(LogStatus.INFO, "<b>FeedBack rejected , Remarks ["+feedback+"]</b>");
		}
		else
		{
			logger.log(LogStatus.ERROR, "FeedBack reject comments not saved!!");
		}
		
		objDashboard.signOut();
		
	}
	
	private void rebuttalReview(String sEncounterId, String action, String practice, String feedback, String comments, String correctiveActions) //, String category, String subCategory, String microCategory, String behaviour, String reason
	{
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.TL);
		String sTLUsername = login.get("ntlg");
		String sTLPassword = login.get("password");	

		// login - TL
		objLogin.login(sTLUsername, sTLPassword);		
		
		objDashboard.navigateTo_Transaction().navigateTo_Rebuttal_Review();
		int iExpPendingCount = objRebuttalReview.close_Pending_Accounts(practice);
		
		objRebuttalReview.select_Practice(practice, false);
		logger.log(LogStatus.INFO, "Navigate to Transaction -> Rebuttal Review and select practice [" + practice +"]");
		
	
		objRebuttalReview.verify_PendingAccountsCount(iExpPendingCount,practice);
		
		
		
		if(objRebuttalReview.search_Encounter(sEncounterId, feedback, comments))
		{
			if(action.equals("ACCEPT"))
			{
				Assert.assertTrue(objRebuttalReview.accept(sEncounterId, feedback + " - TL"));
				
				
				
				if(objRebuttalReview.search_Encounter(sEncounterId, feedback, comments))
				{
					utils.save_ScreenshotToReport("EncounterNotRemovedAfterSubmission");
					logger.log(LogStatus.WARNING, "Encounter still displayed after submission");
				}
				else
				{
					
				}			
			}
			else if(action.equals("REJECT"))
			{
				Assert.assertTrue(objRebuttalReview.reject(sEncounterId, feedback + " - TL"));
				
				/* sign out from TL	*/
				objDashboard.signOut();
				
				
				/* verify in Analyst feedback*/
				
				// get login credentials from excel
				login = excel.get_LoginByRole(Constants.Role.ANALYST);
				String sAnalystUsername = login.get("ntlg");
				String sAnalystPassword = login.get("password");	

				// login - Analyst
				objLogin.login(sAnalystUsername, sAnalystPassword);
				
				objDashboard.navigateTo_Transaction().navigateTo_Feedback();
				objFeedBack.select_Practice(practice, false);
				logger.log(LogStatus.INFO, "Navigate to Transaction -> Feedback and select practice [" +practice +"]");
				
				
				Info result = objQCInbox.search_Encounter(sEncounterId);
				Assert.assertTrue(result.getStatus(), result.getDescription());
			}
		}
		else
		{
			utils.save_ScreenshotToReport("Rebuttal_search_Encounter");
			Configuration.logger.log(LogStatus.FAIL, "Encounter ID ["+ sEncounterId +"] not found at Rebuttal review");
		}
			
	}
}
