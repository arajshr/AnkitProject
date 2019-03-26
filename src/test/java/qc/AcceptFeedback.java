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

public class AcceptFeedback extends Configuration
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
	public void submit_QCAudit_AcceptFeedBack()
	{
		logger = extent.startTest("Accept and rework the account under Feedback queue in Agent login");

		int executableRowIndex = excel.isExecutable("submit_QCAudit_AcceptFeedBack");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		
		String practice = excel.readValue(sheet, executableRowIndex, "practice");		
		String nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue1");		
		HashMap<String, String> data = excel.get_TransactionData(nextQueue);
		
		
		
		
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
		
		//	select practice and open regular queue
		logger.log(LogStatus.INFO, "Navigate to Inbox and select practice [" + practice + "] and Click REGULAR queue");
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		objInbox.select_Practice(practice);
		objInbox.click_Image_Regular(true);

		nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue1");		
		data = excel.get_TransactionData(nextQueue);
		
		String claimStatus = data.get("claim_status");
		String actionCode = data.get("action_code");
		String resolveType = data.get("resolve_type"); 
		String notes = data.get("note");
		
		
		String[] claimIDs = submitAccount(claimStatus, actionCode, resolveType, notes);  //{"4589811-5844","2142121-63010"};
			
				
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
			
			
			nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue2");		
			data = excel.get_TransactionData(nextQueue);
			
			String claimStatus1 = data.get("claim_status");
			String actionCode1 = data.get("action_code");
			String notes1 = data.get("note");
			
			feedback_accept(sEncounterID, checkList, practice, claimStatus, actionCode, notes, category, subCategory, microCategory, behaviour, reason, comments, correctiveActions, claimStatus1, actionCode1, notes1);
			
			
			/*objLogin.login(Constants.mngrUserName, Constants.mngrPassword);
			objDashboard.navigateTo_HistoricalReport().navigateTo_QC_Worked_Accounts();
			logger.log(LogStatus.INFO, "Navigate to QC Worked Accounts Report");					
			Assert.assertTrue(objReport.verify_Report(encounterID, fromDate));
			logger.log(LogStatus.PASS, "Encounter ["+ encounterID +"] verified in report");*/
			
		}
		/*else
		{
			logger.log(LogStatus.ERROR, "There are no accounts in REGULAR queue!!");
		}*/
		
	}
	
	
	
	
	/**
	 * Returns Encounter Id when claim saved successfully from Regular queue
	 * @param transaction
	 * @return string value
	 */
	private String[] submitAccount(String claimStatus, String actionCode, String resolveType, String notes)
	{
		try
		{
			objInbox.open_PatientAccount(Constants.Queues.REGULAR);
			String sAccountNumber = objClaimTransaction.get_PatientAccountNumber();
			String sUID = objClaimTransaction.get_UID();
			String sEncounterID = objClaimTransaction.get_EncounterID();
			
		
			
			Transaction rSubmit = objClaimTransaction.submit_Claim(claimStatus, actionCode, resolveType, notes);		
			Assert.assertTrue(rSubmit.getStatus(), rSubmit.getDescription());
			logger.log(LogStatus.INFO, "<b>Account moved to QCA, "
					+ "Account [" + sAccountNumber + "], UID ["+ sUID +"]<br> <br>"
	
					+ "ClaimStatus ["+ claimStatus +"], <br>"
					+ "ActionCode ["+ actionCode +"], <br>"
					+ "ResolveType ["+ resolveType +"], <br>"
					+ "Notes ["+ notes +"]</b>");	
			
			objDashboard.signOut();
			
			return new String[] {sUID, sEncounterID};
		} 
		catch (Exception e)
		{
			Configuration.logger.log(LogStatus.INFO, "Mehtod: submitAccount" + e.getMessage());
		}
		
		return null;
	}
	
	/**
	 * Performs QC audit for given Encounter ID
	 * 
	 * @param sUID
	 */
	private void audit_QC(String sUID, String practice, String category, String subCategory, String microCategory, String behaviour, String reason,  String comments,String correctiveActions) 
	{
				
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.QCA);
		String sQCAUsername = login.get("ntlg");
		String sQCAPassword = login.get("password");	

		// login - QCA
		objLogin.login(sQCAUsername, sQCAPassword);
		
		objQCDashboard.navigateTo_QCInbox();
		logger.log(LogStatus.INFO, "Navigate to QC Inbox");
		
		objQCInbox.close_Accounts_For_Audit_Popup();
		
		objQCInbox.filter_QC_Accounts(practice, utils.getToday()); 
		logger.log(LogStatus.INFO, "Select Practice ["+ practice +"] and from date as ["+ utils.getToday() +"]");
		
		// search uid
		Info search2 = objQCInbox.search_UID(sUID);
		Assert.assertTrue(search2.getStatus(), search2.getDescription());
		logger.log(LogStatus.INFO, search2.getDescription());
		
		objQCInbox.open_Encounter_QCA(sUID);
		
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
			
		
		Assert.assertTrue(status.getStatus(), status.getDescription());
		logger.log(LogStatus.INFO, "<b>Claim audited with following error details <br><br> "
				+ "Category ["+category+"], <br>"
				+ "Sub Category ["+subCategory+"], <br>"
				+ "Micro Category ["+microCategory+"],<br> "
				+ "Behaviour ["+behaviour+"],<br> "
				+ "Reason NC ["+reason+"],<br> "
				+ "Corrective Actions ["+correctiveActions+"],<br> "
				+ "Comments["+comments+"] <b>");
		
		
		objQCDashboard.signOut();
	}
	
	/**
	 * Accepts the QC feedback and rework on the claim
	 * Veirfy Error Checklist
	 * 
	 * @param sEncounterId
	 * @param checkList2
	 */
	private void feedback_accept(String sEncounterId, ArrayList<String> checkList2, String practice, 
			String claimStatus, String actionCode, String notes, String category, String subCategory, String microCategory, String behaviour, String reason,  String comments, String correctiveActions,
			String claimStatus1, String actionCode1, String notes1	) 
	{
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");	

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
		
		/* navigate to feedback	*/
		objDashboard.navigateTo_Transaction().navigateTo_Feedback();
		objFeedBack.select_Practice(practice, false);
		logger.log(LogStatus.INFO, "Navigate to Transaction -> Feedback and select Practice [" + practice + "]");
		
	
		Info search = objFeedBack.search_Encounter(sEncounterId, claimStatus, actionCode, notes, category, subCategory, microCategory, behaviour, reason,  comments, correctiveActions);
		Assert.assertTrue(search.getStatus(), search.getDescription());
		logger.log(LogStatus.INFO, search.getDescription());
		
		objFeedBack.verify_Error_CheckList(sEncounterId, checkList);
		
		Assert.assertTrue(objFeedBack.accept(sEncounterId, claimStatus1, actionCode1, notes1));
		logger.log(LogStatus.INFO, "<b>Account resubmitted by Analyst at Feedback, "
				+ "Claim Status ["+ claimStatus1 + "], Action Code ["+ actionCode1 +"] and "+ " Note ["+ notes1 +"]</b>");	
				
	}

}





/*objQCDashboard.navigateTo_QCInbox();
logger.log(LogStatus.INFO, "Navigate to QC Inbox");

objQCInbox.filter_QC_Accounts(practice, fromDate); //MM/dd/yyyy
logger.log(LogStatus.INFO, "Select Practice ["+ practice +"] and from date as ["+ fromDate +"]");

String encounterID = objQCInbox.get_Encounter_By_AgentName();		
Assert.assertTrue((objQCInbox.search_Encounter(encounterID)));		
logger.log(LogStatus.INFO, "Open Encounter ID ["+ encounterID +"]");	

objQCInbox.open_Encounter_QCA(encounterID);

ArrayList <String> checkList = new ArrayList<>();

checkList.add(objQCA_AuditSheet.select_Transaction_Screen("No"));
checkList.add(objQCA_AuditSheet.select_Decision_Calling_Payer("No"));
checkList.add(objQCA_AuditSheet.select_Appeal_Not_Done("No"));
checkList.add(objQCA_AuditSheet.select_Conclusion("No"));
	

	boolean status = objQCA_AuditSheet.select_Error_Category(qcErrors.get("category"))
			.select_Error_SubCategory(qcErrors.get("subCategory"))
			.select_Error_MicroCategory(qcErrors.get("microCategory"))
			.select_Error_Behaviour(qcErrors.get("behaviour"))
			.select_Error_ReasonForNC(qcErrors.get("reason"))
			.set_Error_Comments(qcErrors.get("comments"))
			.set_Error_CorrectiveActions(qcErrors.get("correctiveActions"))
			.save_Audit();
	

	if(status)
	{
		logger.log(LogStatus.INFO, "Encounter ID ["+ encounterID +"] audited [subitted with errors to Analyst]");
	}
	else
	{
		logger.log(LogStatus.ERROR, "Account not saved at QC!!");
	}
	
	
objQCDashboard.signOut();
logger.log(LogStatus.INFO, "Sign out from QCA");



 login as Analyst	
objLogin.login(Constants.userName, Constants.password);		
logger.log(LogStatus.INFO, "Logged in as Analyst");

 navigate to feedback	
objDashboard.navigateTo_Transaction().navigateTo_Feedback();
logger.log(LogStatus.INFO, "Navigate to transaction -> Feedback");

objFeedBack.select_Practice(practice);
logger.log(LogStatus.INFO, "Select Practice ["+ practice +"]");


if(objFeedBack.search_Encounter(encounterID))
{
	HashMap<String, String> data1 = ExcelReader.getExcelData(Constants.sheetTransaction, "feedbackAccept");		
	
	objFeedBack.verify_Error_CheckList(encounterID, checkList);
	
	Assert.assertTrue(objFeedBack.accept(encounterID, data1.get("claim_status"), data1.get("action_code"),data1.get("note")));
	logger.log(LogStatus.PASS, "Account resubmitted by Analyst at Feedback");	
}
else
{
	Assert.assertTrue(false);		
}*/
