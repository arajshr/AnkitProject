package historicalReports;

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

public class PercentageOfClarification extends Configuration 
{
	WebDriverUtils utils = new WebDriverUtils();	
	
	Login objLogin;
	AnalystInbox objInbox;
	ClaimTransaction objClaimTransaction;
	ReviewAndApprove objReviewAndApprove;	
	Dashboard objDashboard;
	
	@Test
	public void verify_PercentageOfClarificationReport()
	{
		/*
		 * login - analyst
		 * submit account to clsrification
		 * 
		 * login - TL
		 * verify account in clarification tab
		 * 
		 * navigate to percentage of clarification tab
		 * verify the repot
		 * 
		 */
		
		
		
		
		logger = extent.startTest("Verify Percentage of Clarification report");
		
		int executableRowIndex = excel.isExecutable("verify_PercentageOfClarificationReport");		
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
		
	
		/****************************	LOGIN AS ANALYST AND SUBMIT ACCOUNT TO CLARIFICATION	****************************/
	
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
		
		
		
		/****************************	LOGIN AS TL AND VERIFY REPORT	****************************/
		
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.TL);
		String sTLUsername = login.get("ntlg");
		String sTLPassword = login.get("password");	

		// login - TL
		objLogin.login(sTLUsername, sTLPassword);
	
		objDashboard.navigateTo_Transaction().navigateTo_Review_And_Approve();						
		objReviewAndApprove.click_Tab_Clarification();
		logger.log(LogStatus.INFO, " Navigate to Review and approve and switch to Clarification tab");		
		
		//	verify account presence , reason and comments
		Info search = objReviewAndApprove.search_Account_Clarification(UID, reason, comments);
		Assert.assertTrue(search.getStatus(), search.getDescription());
		logger.log(LogStatus.INFO, search.getDescription());
		
		
	}

}
