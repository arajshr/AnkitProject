package qc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

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
import pageObjectRepository.Dashboard;
import pageObjectRepository.Login;
import pageObjectRepository.qc.QCA_AuditSheet;
import pageObjectRepository.qc.QCDashboard;
import pageObjectRepository.qc.QCInbox;
import pageObjectRepository.transaction.AnalystInbox;
import pageObjectRepository.transaction.ClaimTransaction;
import pageObjectRepository.transaction.CodingInbox;

public class QC_History extends Configuration
{
	
	WebDriverUtils utils = new WebDriverUtils();
	
	Login objLogin;
	AnalystInbox objInbox;
	ClaimTransaction objClaimTransaction;
	Dashboard objDashboard;
	CodingInbox objCodingInbox;
	QCDashboard objQCDashboard;
	QCInbox objQCInbox;
	QCA_AuditSheet objQCA_AuditSheet;
	
	String fromDate = new SimpleDateFormat("MM/dd/yy").format(new Date()); // -- MM/dd/yyyy
	
	@Test(groups= {"REG"})
	public void verify_QCHistory()
	{
		logger = extent.startTest("Verifying transaction history in QCA Audit Sheet window");
		
		int executableRowIndex = excel.isExecutable("verify_QCHistory");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		
		String practice = excel.readValue(sheet, executableRowIndex, "practice");		
		String nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue1");		
		HashMap<String, String> data = excel.get_TransactionData(nextQueue);
		
		String claimStatus = data.get("claim_status");
		String actionCode = data.get("action_code");
		String resolveType = data.get("resolve_type"); 
		String notes = data.get("note");
		
		
		LinkedList<HashMap<String, String>> history = new LinkedList<>();
		
		objLogin = PageFactory.initElements(driver, Login.class);
		objClaimTransaction = PageFactory.initElements(Configuration.driver, ClaimTransaction.class);
		objInbox = PageFactory.initElements(Configuration.driver, AnalystInbox.class);
		objDashboard = PageFactory.initElements(Configuration.driver, Dashboard.class);
		objCodingInbox = PageFactory.initElements(Configuration.driver, CodingInbox.class);
		
		
		objQCDashboard = PageFactory.initElements(Configuration.driver, QCDashboard.class);
		objQCInbox = PageFactory.initElements(Configuration.driver, QCInbox.class);
		objQCA_AuditSheet = PageFactory.initElements(Configuration.driver, QCA_AuditSheet.class);
		
		//String expAccountNumber = "1532";
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");	

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
		
		data.put("workedBy", objDashboard.getWorkedBy().getAttribute("textContent"));
		history.add(data);
				
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		logger.log(LogStatus.INFO, "Navigate to Transaction -> Inbox");		
	 	
	 	logger.log(LogStatus.INFO, "Select practice as ["+ practice +"] and click Regular queue");
		objInbox.select_Practice(practice); 	
	 	objInbox.click_Image_Regular(true);	
		
	 	
	 	objInbox.open_PatientAccount(Constants.Queues.REGULAR);	
	 	String sAccountNumber = objClaimTransaction.get_PatientAccountNumber();
	 	String sEncounterId = objClaimTransaction.get_EncounterID();
		String sUID = objClaimTransaction.get_UID();
		
		Transaction submitAccount = objClaimTransaction.submit_Claim(claimStatus, actionCode, resolveType, notes);
		Assert.assertTrue(submitAccount.getStatus(), submitAccount.getDescription());
		logger.log(LogStatus.INFO, "<b>Account moved to Coding Inbox, "
											+ "Account [" + sAccountNumber + "], UID ["+ sUID +"]<br> <br>"
							
											+ "ClaimStatus ["+claimStatus+"], <br>"
											+ "ActionCode ["+actionCode+"], <br>"
											+ "ResolveType ["+resolveType+"], <br>"
											+ "Notes ["+notes+"]</b>");
		
		objDashboard.signOut();
		
 		
 		
		
		
 		
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.CODING);
		String sCodingUsername = login.get("ntlg");
		String sCodingPassword = login.get("password");	
		
		// login - Coding
		objLogin.login(sCodingUsername, sCodingPassword);
		
		// Navigate to coding inbox	
		//objDashboard.navigateTo_Transaction().navigateTo_Coding_Inbox();
		
		// select practice	
		objCodingInbox.select_Practice(practice, false);
		logger.log(LogStatus.INFO, "Navigate to Coding inbox and Select practice [" + practice + "]");
		
		// verify account	
		Info search = objCodingInbox.search_UID(sUID);
		Assert.assertTrue(search.getStatus(), search.getDescription());
		logger.log(LogStatus.INFO, search.getDescription());
		
		
		// submit account to coding response 		
		String responseQueue = excel.readValue(sheet, executableRowIndex, "response");		
		data = excel.get_TransactionData(responseQueue);
		
		claimStatus = data.get("claim_status");
		actionCode = data.get("action_code");
		resolveType = data.get("resolve_type"); 
		notes = data.get("note");
		
		data.put("workedBy", objDashboard.getWorkedBy().getAttribute("textContent"));
		history.add(data);
				
		Assert.assertTrue(objCodingInbox.submit_CodingResponse(sAccountNumber, claimStatus, actionCode, notes));
		logger.log(LogStatus.INFO, "<b>Account moved to Coding response queue</b>");
		
		// Sign out from Manager role	
 		objDashboard.signOut();
 		
 		
 		
 		
		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		
		objInbox.select_Practice(practice);
		objInbox.click_Image_CodingResponse();
		logger.log(LogStatus.INFO, "Select practice [" + practice + "] and Navigate to Coding Response");
		
		
		// verify account	
		Info search1 = objInbox.search_Account(sAccountNumber, Constants.Queues.CODING_RESPONSE);
		Assert.assertTrue(search1.getStatus(), search1.getDescription());
		logger.log(LogStatus.INFO, search1.getDescription());
		
		
		nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue2");	
		data = excel.get_TransactionData(nextQueue);
		
		claimStatus = data.get("claim_status");
		actionCode = data.get("action_code");
		resolveType = data.get("resolve_type"); 
		notes = data.get("note");
		
		data.put("workedBy", objDashboard.getWorkedBy().getAttribute("textContent"));
		history.add(data);		
		
		
		objInbox.open_PatientAccount(Constants.Queues.CODING_RESPONSE, "ACCOUNT_NUMBER", sAccountNumber);	
	 	
		Transaction rSubmit = objClaimTransaction.submit_Claim(claimStatus, actionCode, resolveType, notes);
		Assert.assertTrue(rSubmit.getStatus(), rSubmit.getDescription());
		logger.log(LogStatus.INFO, "<b>Account moved to QCA <br> <br> ClaimStatus ["+claimStatus+"], <br> ActionCode ["+actionCode+"], <br> ResolveType ["+resolveType+"], <br> Notes ["+notes+"]</b>");		
 		
		// sign out	
		objDashboard.signOut();
		
		
		
		

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
		logger.log(LogStatus.INFO, "Select Practice ["+ practice +"] and from date as ["+ fromDate +"]");
		
		// search uid
		Info search2 = objQCInbox.search_UID(sUID);
		Assert.assertTrue(search2.getStatus(), search2.getDescription());
		logger.log(LogStatus.INFO, search2.getDescription());
				
		objQCInbox.open_Encounter_QCA(sUID);		
		logger.log(LogStatus.INFO, "Open Encounter ID ["+ sEncounterId +"]");
		
		// transaction history
		objQCA_AuditSheet.verify_History(history);
		
		
		
		objQCA_AuditSheet.select_Transaction_Screen("No");
		objQCA_AuditSheet.select_Decision_Calling_Payer("No");
		objQCA_AuditSheet.select_Appeal_Not_Done("No");
		objQCA_AuditSheet.select_Conclusion("No");
		
		
		String tc_id = excel.readValue(sheet, executableRowIndex, "qcaErrorParameters");		
		HashMap<String, String> errorParameters = excel.get_QCAErrorParameters(tc_id);
		
		String category = errorParameters.get("category");
		String subCategory = errorParameters.get("subCategory");
		String microCategory = errorParameters.get("microCategory"); 
		String behaviour = errorParameters.get("behaviour");
		String reason = errorParameters.get("reason");
		String comments = errorParameters.get("comments"); 
		String correctiveActions = errorParameters.get("correctiveActions");
		
		
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
	}
	
}
