package historicalReports;

import java.util.HashMap;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.Constants;
import customDataType.Transaction;
import pageObjectRepository.Dashboard;
import pageObjectRepository.Login;
import pageObjectRepository.historicalReports.Report120;
import pageObjectRepository.transaction.AnalystInbox;
import pageObjectRepository.transaction.ClaimTransaction;

public class DOS120 extends Configuration
{
	Login objLogin;
	Dashboard objDashboard;
	AnalystInbox objInbox;
	Report120 objReport;
	ClaimTransaction objClaimTransaction;
	
	@Test(groups= {"REG"})
	public void verify_120Report()
	{
		
		
		int executableRowIndex = excel.isExecutable("verify_120Report");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		/*String client = excel.readValue(sheet, executableRowIndex, "client");
		String location = excel.readValue(sheet, executableRowIndex, "location");*/
		String practice = excel.readValue(sheet, executableRowIndex, "practice");
		
		String nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue1");		
		HashMap<String, String> data = excel.get_TransactionData(nextQueue);
		
		String claimStatus = data.get("claim_status");
		String actionCode = data.get("action_code");
		String resolveType = data.get("resolve_type"); 
		String notes = data.get("note");
		
		
		
		objLogin = PageFactory.initElements(driver, Login.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objInbox = PageFactory.initElements(driver, AnalystInbox.class);
		objReport = PageFactory.initElements(driver, Report120.class);
		objClaimTransaction = PageFactory.initElements(driver, ClaimTransaction.class);
		
		
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.TL);
		String sTLUsername = login.get("ntlg");
		String sTLPassword = login.get("password");	

		// login - TL
		objLogin.login(sTLUsername, sTLPassword);
		objDashboard.navigateTo_HistoricalReport().navigateTo_120Report();
		
		//verify data
		
		objDashboard.signOut();
		
		
		
		
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");	

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);	
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		logger.log(LogStatus.INFO, "Navigate to Transaction -> Inbox");
		
		objInbox.select_Practice(practice); 
		objInbox.click_Image_Regular(true);
	 	logger.log(LogStatus.INFO, "Select practice ["+ practice +"] and click REGULAR queue icon");
	 	 	
	 	objInbox.open_PatientAccount(Constants.Queues.REGULAR);
		String sAccountNumber = objClaimTransaction.get_PatientAccountNumber();
		
		
	 	Transaction submitAccount = objClaimTransaction.submit_Claim(claimStatus, actionCode, resolveType, notes);

		Assert.assertTrue(submitAccount.getStatus(), "Account [" + sAccountNumber +"] not submitted!!");
		logger.log(LogStatus.INFO, "<b>Account moved to Payment</b>");	
		
		
		objDashboard.signOut();
		
		
		
		// login - TL
		objLogin.login(sTLUsername, sTLPassword);
		objDashboard.navigateTo_HistoricalReport().navigateTo_120Report();
		
		//verify data
		
		objDashboard.signOut();
		
	}
}
