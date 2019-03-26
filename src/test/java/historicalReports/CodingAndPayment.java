package historicalReports;

import java.text.SimpleDateFormat;
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
import customDataType.Transaction;
import pageObjectRepository.Dashboard;
import pageObjectRepository.Login;
import pageObjectRepository.historicalReports.CodingAndPaymentReport;
import pageObjectRepository.transaction.AnalystInbox;
import pageObjectRepository.transaction.ClaimTransaction;

public class CodingAndPayment extends Configuration
{
	WebDriverUtils utils = new WebDriverUtils();
	
	Login objLogin;
	Dashboard objDashboard;
	CodingAndPaymentReport objReport;
	AnalystInbox objAnalystInbox;
	ClaimTransaction objClaimTransaction;
    
	String fromDate = new SimpleDateFormat("MM/dd/yy").format(new Date()); // -- MM/dd/yyyy  new Date()
	
	
	
	@Test(groups= {"REG"})
	public void verify_Coding_Report()
	{
		
		int executableRowIndex = excel.isExecutable("verify_Coding_Report");		
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
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objReport = PageFactory.initElements(driver, CodingAndPaymentReport.class);
		objAnalystInbox = PageFactory.initElements(driver, AnalystInbox.class);
		objClaimTransaction = PageFactory.initElements(driver, ClaimTransaction.class);
		


		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.TL);
		String sTLUsername = login.get("ntlg");
		String sTLPassword = login.get("password");	

		// login - TL
		objLogin.login(sTLUsername, sTLPassword);	
		
		objDashboard.navigateTo_HistoricalReport().navigateTo_CodingAndPaymentQueue_Report();
		logger.log(LogStatus.INFO, "Navigate to Coding and Payment report");
		
		objReport.select_Client(client)   //.select_FromDate(fromDate)
				.select_Location(location)
				.select_Practice(practice)
				.select_Queue("Coding")
				.get_Report();
		Configuration.logger.log(LogStatus.INFO, "Get report for Client ["+client+"], Location ["+location+"], Practice ["+practice+"]");
		
		utils.save_ScreenshotToReport("CodingReport");
		
		int expValue = objReport.get_Worked_Accounts(claimStatus, actionCode);
		
		objDashboard.signOut();
		
		
		
		
		
		
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");	

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);	
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		Configuration.logger.log(LogStatus.INFO, "Navigate to Transaction -> Inbox");
		
		objAnalystInbox.select_Practice(practice);
		objAnalystInbox.click_Image_Regular(true);
	 	Configuration.logger.log(LogStatus.INFO, "Select practice ["+ practice +"] and click REGULAR queue icon");
	 	
		Transaction openAccount = objAnalystInbox.open_PatientAccount(Constants.Queues.REGULAR);
		
		if(openAccount.getStatus())
		{
			/*logger.log(LogStatus.INFO,  "Open account [" + openAccount.getPateintAccountNumber() + "]");	*/
			
			String sAccountNumber = objClaimTransaction.get_PatientAccountNumber();
			//String sUID = objClaimTransaction.get_First_UID();
			
			Transaction submitAccount = objClaimTransaction.submit_Claim(claimStatus, actionCode, resolveType, notes);
			Assert.assertTrue(submitAccount.getStatus(), "Account [" + sAccountNumber +"] not submitted");
			logger.log(LogStatus.INFO, "<b>Account moved to Coding queue</b>");
			
			expValue += 1;
			objDashboard.signOut();
			
			
			
			// login - TL
			objLogin.login(sTLUsername, sTLPassword);			
			objDashboard.navigateTo_HistoricalReport().navigateTo_CodingAndPaymentQueue_Report();
			logger.log(LogStatus.INFO, "Navigate to Coding and Payment report");
			
			objReport//.select_FromDate(fromDate)
					.select_Client(client)
					.select_Location(location)
					.select_Practice(practice)
					.select_Queue("Coding")
					.get_Report();
			Configuration.logger.log(LogStatus.INFO, "Get report for Client ["+client+"], Location ["+location+"], Practice ["+practice+"]");
			
			utils.save_ScreenshotToReport("CodingReport");
			int actValue = objReport.get_Worked_Accounts(claimStatus, actionCode);
			
			Assert.assertEquals(actValue, expValue, "Value do not match for Action Code ["+ actionCode +"] under Claim Status ["+ claimStatus +"]..");
			logger.log(LogStatus.INFO, "Coding report verified");
			
		}
		else
		{
			logger.log(LogStatus.INFO, openAccount.getDescription());
		}
		
	}
	
	
	
	@Test(enabled = true)
	public void verify_Payment_Report()
	{
		int executableRowIndex = excel.isExecutable("verify_Payment_Report");		
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
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objReport = PageFactory.initElements(driver, CodingAndPaymentReport.class);
		objAnalystInbox = PageFactory.initElements(driver, AnalystInbox.class);
		objClaimTransaction = PageFactory.initElements(driver, ClaimTransaction.class);
		


		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.TL);
		String sTLUsername = login.get("ntlg");
		String sTLPassword = login.get("password");	

		// login - TL
		objLogin.login(sTLUsername, sTLPassword);	
		
		
		objDashboard.navigateTo_HistoricalReport().navigateTo_CodingAndPaymentQueue_Report();
		logger.log(LogStatus.INFO, "Navigate to Coding and Payment report");
		
		objReport.select_Client(client) 	//.select_FromDate(fromDate)
				.select_Location(location)
				.select_Practice(practice)
				.select_Queue("Payment")
				.get_Report();
		Configuration.logger.log(LogStatus.INFO, "Get report for Client ["+client+"], Location ["+location+"], Practice ["+practice+"]");
		utils.save_ScreenshotToReport("PaymentReport");
		
		
		int expValue = objReport.get_Worked_Accounts(claimStatus, actionCode);
		
		objDashboard.signOut();
		


		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");	

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);	
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		Configuration.logger.log(LogStatus.INFO, "Navigate to Transaction -> Inbox");
		
		objAnalystInbox.select_Practice(practice);
		objAnalystInbox.click_Image_Regular(true);
	 	Configuration.logger.log(LogStatus.INFO, "Select practice ["+ practice +"] and click REGULAR queue icon");
		
		Transaction openAccount = objAnalystInbox.open_PatientAccount(Constants.Queues.REGULAR);
		
		if(openAccount.getStatus())
		{
			/*logger.log(LogStatus.INFO,  "Open account [" + openAccount.getPateintAccountNumber() + "]");	*/
			
			String sAccountNumber = objClaimTransaction.get_PatientAccountNumber();
		
			Transaction submitAccount = objClaimTransaction.submit_Claim(claimStatus, actionCode, resolveType, notes);
			Assert.assertTrue(submitAccount.getStatus(), "Account [" + sAccountNumber +"] not submitted");
			logger.log(LogStatus.INFO, "<b>Account moved to Payment queue</b>");
			
			expValue += 1;
			
			objDashboard.signOut();
			
			
			
			//verify in report
			
			// login - TL
			objLogin.login(sTLUsername, sTLPassword);	
			objDashboard.navigateTo_HistoricalReport().navigateTo_CodingAndPaymentQueue_Report();
			logger.log(LogStatus.INFO, "Navigate to Coding and Payment report");
			
			objReport//.select_FromDate(fromDate)
					.select_Client(client)
					.select_Location(location)
					.select_Practice(practice)
					.select_Queue("Payment")
					.get_Report();
			Configuration.logger.log(LogStatus.INFO, "Get report for Client ["+client+"], Location ["+location+"], Practice ["+practice+"]");
			
			utils.save_ScreenshotToReport("PaymentReport");
			
			int actValue = objReport.get_Worked_Accounts(claimStatus, actionCode);			
			Assert.assertEquals(actValue, expValue, "Value do not match for Action Code ["+actionCode+"] under Claim Status ["+claimStatus+"]..");
		}
		else
		{
			logger.log(LogStatus.INFO, openAccount.getDescription());
		}
		
	}
}
