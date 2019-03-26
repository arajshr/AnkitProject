package transaction;

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
import pageObjectRepository.Dashboard;
import pageObjectRepository.Login;
import pageObjectRepository.transaction.AnalystInbox;
import pageObjectRepository.transaction.ClaimTransaction;
import pageObjectRepository.transaction.ReviewAndApprove;

public class Clarification extends Configuration
{
	WebDriverUtils utils = new WebDriverUtils();	
	
	Login objLogin;
	AnalystInbox objInbox;
	ClaimTransaction objClaimTransaction;
	ReviewAndApprove objReviewAndApprove;	
	Dashboard objDashboard;
	
	
	@Test(groups = {"REG"})
	public void verify_Clarification()
	{
		logger = extent.startTest("Verify Clarification and Clarification Release");
		
		int executableRowIndex = excel.isExecutable("verify_Clarification");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		
		String practice = excel.readValue(sheet, executableRowIndex, "practice");		
		String nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue1");		
		HashMap<String, String> data = excel.get_TransactionData(nextQueue);
		
		String reason = data.get("reason");
		String comments = data.get("comments");
		String remarks = data.get("remarks"); 
		
		
		
		
		
		objLogin = PageFactory.initElements(driver, Login.class);
		objClaimTransaction = PageFactory.initElements(driver, ClaimTransaction.class);
		objInbox = PageFactory.initElements(driver, AnalystInbox.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objReviewAndApprove = PageFactory.initElements(driver, ReviewAndApprove.class);
		
	
	
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");	

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
		
		String postedBy = objDashboard.getWorkedBy().getAttribute("textContent");
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		logger.log(LogStatus.INFO, "Navigate to Transaction -> Inbox");		
		
		
		logger.log(LogStatus.INFO, "Select practice ["+ practice +"] and click REGULAR queue icon");
		objInbox.select_Practice(practice); 
		objInbox.click_Image_Regular(true);	 			
	 	
		
		//	open first available account
		objInbox.open_PatientAccount(Constants.Queues.REGULAR);
		String UID = objClaimTransaction.get_UID();
		String sAccountNumber = objClaimTransaction.get_PatientAccountNumber();
		
		//	Assert
		Assert.assertTrue(objClaimTransaction.save_Clarification(reason, comments));		
		logger.log(LogStatus.INFO, "<b>Account [" +sAccountNumber + "] moved to Clarification, Reason [" + reason + "] and Comments [" + comments + "]</b>");
		
		// Sign Out
		objDashboard.signOut();
		
		
		
		
		
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.TL);
		String sTLUsername = login.get("ntlg");
		String sTLPassword = login.get("password");	

		// login - TL
		objLogin.login(sTLUsername, sTLPassword);
		
		String clarifiedBy = objDashboard.getWorkedBy().getAttribute("textContent");
	
		objDashboard.navigateTo_Transaction().navigateTo_Review_And_Approve();						
		objReviewAndApprove.click_Tab_Clarification();
		logger.log(LogStatus.INFO, " Navigate to Review and approve and switch to Clarification tab");		
		
		//	verify account presence , reason and comments
		Info search = objReviewAndApprove.search_Account_Clarification(UID, reason, comments);
		Assert.assertTrue(search.getStatus(), search.getDescription());
		logger.log(LogStatus.INFO, search.getDescription());
		
		
		//	Assert clarification release
		Info save = objReviewAndApprove.submit_Clarification(remarks);
		Assert.assertTrue(save.getStatus(), save.getDescription());
		logger.log(LogStatus.INFO, "<b>Account moved to Clarification release with  remarks [" + remarks + "]</b>");
		
		objDashboard.signOut();
		
		
		
		
		
		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		logger.log(LogStatus.INFO, "Navigate to Transaction -> Inbox");
		
		//select practice and select regular image link
		objInbox.select_Practice(practice); 
		objInbox.click_Image_Clarification();
	 	logger.log(LogStatus.INFO, "Select practice ["+ practice +"] and click Clarification queue icon");
	 	
	 	//	verify account presence, tlComments
	 	Info search1 = objInbox.search_Clarification(sAccountNumber, remarks);
		Assert.assertTrue(search1.getStatus(), search1.getDescription());
		logger.log(LogStatus.INFO, search1.getDescription());
	 	
	 	//	submit account - arclosed
	 	objInbox.open_PatientAccount(Constants.Queues.CLARIFICATION, "ACCOUNT_NUMBER", sAccountNumber);
	 	//String sUID = objClaimTransaction.get_UID();
	 	
	 	
	 	
	 	
	 	objClaimTransaction.verify_ClarificationHistory(reason, comments, postedBy, remarks, clarifiedBy);
	 	
	 	objClaimTransaction.close_ClaimTransaction();
	 	
	 	
	 	/*nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue2");		
		data = excel.get_TransactionData(nextQueue);
		
		String claimStatus = data.get("claim_status");
		String actionCode = data.get("action_code");
		String resolveType = data.get("resolve_type"); 
		String notes = data.get("note");
		
	 	Transaction rSubmit = objClaimTransaction.submit_Claim(sUID, claimStatus, actionCode, resolveType, notes);	 	
	 	Assert.assertTrue(rSubmit.getStatus(), rSubmit.getDescription());
		logger.log(LogStatus.INFO, "<b>Account moved from Clarification  to AR Closed</b>");*/
	 	
	 	
		
	}
}
