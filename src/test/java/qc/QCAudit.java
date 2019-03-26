package qc;

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
import pageObjectRepository.qc.QCA_AuditSheet;
import pageObjectRepository.qc.QCDashboard;
import pageObjectRepository.qc.QCInbox;
import pageObjectRepository.transaction.AnalystInbox;
import pageObjectRepository.transaction.ClaimTransaction;

public class QCAudit extends Configuration
{
	private WebDriverUtils utils = new WebDriverUtils();
	
	private Login objLogin;
	private AnalystInbox objInbox;
	private ClaimTransaction objClaimTransaction;
	private Dashboard objDashboard;
	
	
	QCDashboard objQCDashboard;
	QCInbox objQCInbox;
	QCA_AuditSheet objQCA_AuditSheet;
	
	@Test(groups= {"SMK", "REG"})
	public void verify_QCA()
	{
		
		logger = extent.startTest("Verifying QCA audit");
		
		
		int executableRowIndex = excel.isExecutable("verify_QCA");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		
		String practice = excel.readValue(sheet, executableRowIndex, "practice");		
		String nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue1");		
		HashMap<String, String> data = excel.get_TransactionData(nextQueue);
		
		String claimStatus = data.get("claim_status");
		String actionCode = data.get("action_code");
		String resolveType = data.get("resolve_type"); 
		String notes = data.get("note");
		
		
		
		objLogin = PageFactory.initElements(Configuration.driver, Login.class);
		objClaimTransaction = PageFactory.initElements(Configuration.driver, ClaimTransaction.class);
		objInbox = PageFactory.initElements(Configuration.driver, AnalystInbox.class);
		objDashboard = PageFactory.initElements(Configuration.driver, Dashboard.class);
		
		objQCDashboard = PageFactory.initElements(driver, QCDashboard.class);
		objQCInbox = PageFactory.initElements(driver, QCInbox.class);		
		objQCA_AuditSheet = PageFactory.initElements(driver, QCA_AuditSheet.class);
		
		


		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");	

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		logger.log(LogStatus.INFO, "Navigate to Inbox");
		
		logger.log(LogStatus.INFO, "Select practice [" + practice + "] and Navigate to REGULAR queue");
		objInbox.select_Practice(practice);
		objInbox.click_Image_Regular(true);
		
		
		
		
		//	open first available account
	 	objInbox.open_PatientAccount(Constants.Queues.REGULAR);			
	 	String sAccountNumber = objClaimTransaction.get_PatientAccountNumber();
	 	String sUID = objClaimTransaction.get_UID();
	 	
	 	//	submit and Assert
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
		login = excel.get_LoginByRole(Constants.Role.QCA);
		String sQCAUsername = login.get("ntlg");
		String sQCAPassword = login.get("password");	

		// login - QCA
		objLogin.login(sQCAUsername, sQCAPassword);
		
		objQCDashboard.navigateTo_QCInbox();
		objQCInbox.close_Accounts_For_Audit_Popup();
		
		objQCInbox.filter_QC_Accounts(practice); 
		logger.log(LogStatus.INFO, "Navigate to QC Inbox and Select Practice ["+ practice +"]");
		
		
		// search uid
		Info search = objQCInbox.search_UID(sUID);
		Assert.assertTrue(search.getStatus(), search.getDescription());
		logger.log(LogStatus.INFO, search.getDescription());
		
		
		objQCInbox.open_Encounter_QCA(sUID);
		
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
