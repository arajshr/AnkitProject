package dashboard;

import java.util.HashMap;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.Constants;
import configuration.WebDriverUtils;
import customDataType.PMReport;
import customDataType.Transaction;
import pageObjectRepository.Dashboard;
import pageObjectRepository.Login;
import pageObjectRepository.dashboard.PrintAndMailReport;
import pageObjectRepository.transaction.AnalystInbox;
import pageObjectRepository.transaction.ClaimTransaction;
import pageObjectRepository.transaction.PrintAndMailInbox;

public class PrintAndMail extends Configuration
{
	WebDriverUtils utils = new WebDriverUtils();
	Login objLogin;
	Dashboard objDashboard;
	PrintAndMailReport objReport;
	AnalystInbox objAnalystInbox;
	ClaimTransaction objClaimTransaction;
	PrintAndMailInbox objPrintAndMailInbox;
	
	@Test
	public void verify_PrintAndMail_Dashboard()
	{
		
		int executableRowIndex = excel.isExecutable("verify_PrintAndMail_Dashboard");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		/*String client = excel.readValue(sheet, executableRowIndex, "client");
		String location = excel.readValue(sheet, executableRowIndex, "location");*/
		String practice = excel.readValue(sheet, executableRowIndex, "practice");
		String insurance = excel.readValue(sheet, executableRowIndex, "insurance");
		
		String nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue1");		
		HashMap<String, String> data = excel.get_TransactionData(nextQueue);
		
		String claimStatus = data.get("claim_status");
		String actionCode = data.get("action_code");
		String resolveType = data.get("resolve_type"); 
		String notes = data.get("note");
		
		
		
		objLogin = PageFactory.initElements(driver, Login.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objReport = PageFactory.initElements(driver, PrintAndMailReport.class);
		objAnalystInbox = PageFactory.initElements(driver, AnalystInbox.class);
		objClaimTransaction = PageFactory.initElements(driver, ClaimTransaction.class);
		objPrintAndMailInbox = PageFactory.initElements(driver, PrintAndMailInbox.class);
		
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.TL);
		String sTLUsername = login.get("ntlg");
		String sTLPassword = login.get("password");	

		// login - TL
		objLogin.login(sTLUsername, sTLPassword);			
		objDashboard.navigateTo_Dashboard().navigateTo_PrintAndMail_Queue();
		
		PMReport expData = objReport.get_ReportDetails(insurance);
		utils.save_ScreenshotToReport("PrintAndMailReport");		
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
	 	
		objAnalystInbox.open_PatientAccount(Constants.Queues.REGULAR, "INSURANCE", insurance);
		
		String sUID = objClaimTransaction.get_UID();
		String sAccountNumber = objClaimTransaction.get_PatientAccountNumber();
		String insbalance = objClaimTransaction.get_InsBalance();
		String totalCharges = objClaimTransaction.get_TotalCharges();
		
		
		/*Assert.assertTrue(openAccount.getStatus(), openAccount.getDescription());		
		logger.log(LogStatus.INFO,  "Open account [" + openAccount.getPateintAccountNumber() + "]");*/	
		
		
				
		Transaction rSubmit = objClaimTransaction.submit_Claim(sUID, claimStatus, actionCode, resolveType, notes);
		Assert.assertTrue(rSubmit.getStatus(), "<b>Claim not saved to Print and Mail queue. Account ["+sAccountNumber+"], UID ["+sUID+"]</b>");
		logger.log(LogStatus.INFO, "<b>Claim moved to Print and Mail queue. Account ["+sAccountNumber+"], UID ["+sUID+"]</b>");
				
		expData.update_ReportDetails(1, Double.parseDouble(totalCharges), Double.parseDouble(insbalance));
		
		objDashboard.signOut();
		
		
		
		// login - TL
		objLogin.login(sTLUsername, sTLPassword);			
		objDashboard.navigateTo_Dashboard().navigateTo_PrintAndMail_Queue();
		logger.log(LogStatus.INFO, "Navigate to Dashboard -> Print And Mail Report");
		
			
		
		/*result = objReport.verify_Insurance(insurance);
		
		if(result.getStatus())
		{*/
			PMReport actData = objReport.get_ReportDetails(insurance);
			
			SoftAssert s = new SoftAssert();			
			s.assertEquals(actData.getAccountCount(), expData.getAccountCount(), "<br>Account count do not match");
			s.assertEquals(actData.getDollarValue(), expData.getDollarValue(), "<br>Dollar value do not match");
			s.assertEquals(actData.getOutstandingBalance(), expData.getOutstandingBalance(), "<br>Outstanding Balance value do not match");
			
			utils.save_ScreenshotToReport("PrintAndMailReport");
			
			s.assertAll();
			
			logger.log(LogStatus.INFO, "Print and Mail report verified");
		/*}
		else
		{
			logger.log(LogStatus.FAIL, result.getDescription());
		}*/
		
		
	}
}
