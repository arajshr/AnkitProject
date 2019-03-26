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
import configuration.WebDriverUtils;
import customDataType.Transaction;
import pageObjectRepository.Dashboard;
import pageObjectRepository.Login;
import pageObjectRepository.historicalReports.MyProductionDetailsReport;
import pageObjectRepository.transaction.AnalystInbox;
import pageObjectRepository.transaction.ClaimTransaction;
import pageObjectRepository.transaction.CodingInbox;
import pageObjectRepository.transaction.PrintAndMailInbox;

public class MyProductionDetails extends Configuration
{
WebDriverUtils utils = new WebDriverUtils();
	
	Login objLogin;
	AnalystInbox objInbox;
	ClaimTransaction objClaimTransaction;
	PrintAndMailInbox objPrintAndMailInbox;
	Dashboard objDashboard;	
	MyProductionDetailsReport objReport;
	CodingInbox objCodingInbox;
	
	
	@Test(groups= {"REG"})
	public void verify_MyProductionDetails_Report()
	{
		logger = extent.startTest("Verify My Production details report");
		
		int executableRowIndex = excel.isExecutable("verify_MyProductionDetails_Report");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		String client = excel.readValue(sheet, executableRowIndex, "client");
		String location = excel.readValue(sheet, executableRowIndex, "location");
		String practice = excel.readValue(sheet, executableRowIndex, "practice");
		
		String nextQueue1 = excel.readValue(sheet, executableRowIndex, "nextQueue1");		
		HashMap<String, String> data = excel.get_TransactionData(nextQueue1);
		
		String claimStatus = data.get("claim_status");
		String actionCode = data.get("action_code");
		String resolveType = data.get("resolve_type"); 
		String notes = data.get("note");
		
		
		
		objLogin = PageFactory.initElements(driver, Login.class);
		objClaimTransaction = PageFactory.initElements(driver, ClaimTransaction.class);
		objInbox = PageFactory.initElements(driver, AnalystInbox.class);
		objCodingInbox = PageFactory.initElements(driver, CodingInbox.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objPrintAndMailInbox = PageFactory.initElements(driver, PrintAndMailInbox.class);
		objReport = PageFactory.initElements(driver, MyProductionDetailsReport.class);
				
 		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");	

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);	
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
	 	Configuration.logger.log(LogStatus.INFO, "Navigate to Transaction -> Inbox");
	 		 	
	 	objInbox.select_Practice(practice); 
	 	objInbox.click_Image_Regular(true);
	 	Configuration.logger.log(LogStatus.INFO, "Select practice ["+ practice +"] and click REGULAR queue icon");
	 	
	 	objInbox.open_PatientAccount(Constants.Queues.REGULAR); 
	 	
		String sAccountNumber = objClaimTransaction.get_PatientAccountNumber();
		String encounterId = objClaimTransaction.get_EncounterID();
		String UID = objClaimTransaction.get_UID();
		
				
		
		Transaction submitAccount = objClaimTransaction.submit_Claim(claimStatus, actionCode, resolveType, notes);							
		Assert.assertTrue(submitAccount.getStatus(), "Account [" + sAccountNumber +"] not submitted!!");
		logger.log(LogStatus.INFO, "<b>Claim moved to "+ nextQueue1 +" queue. Account [" + sAccountNumber +"]</b>");	
		
			
		
		/* verify in My Production Report	*/
		objDashboard.navigateTo_HistoricalReport().navigateTo_MyProductionDetails();
		Configuration.logger.log(LogStatus.INFO, "Navigate to My Production Details report");
		
		objReport.select_Client(client)
				.select_Location(location)
				.select_Practice(practice)
				.get_Report();
		Configuration.logger.log(LogStatus.INFO, "Get report for Client ["+client+"], Location ["+location+"], Practice ["+practice+"]");
		
		SoftAssert s = objReport.verify_Production(sAccountNumber, encounterId, claimStatus, actionCode, notes);		
		s.assertAll();		
		Configuration.logger.log(LogStatus.INFO, "<b>Account["+ sAccountNumber +"] verified in My Production Details report</b>");
		
		objDashboard.signOut();
		
		
		
		
		/* verify account in respective next queue	*/
		
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.CODING);
		String sCodingUsername = login.get("ntlg");
		String sCodingPassword = login.get("password");	
		
		// login - Coding
		objLogin.login(sCodingUsername, sCodingPassword);
		
		Transaction status = objReport.verify_InNextQueue(nextQueue1, UID, practice);
		Assert.assertTrue(status.getStatus(), status.getDescription());
		
		Configuration.logger.log(LogStatus.INFO, status.getDescription());	
		
		objDashboard.signOut();
		
		
		
		/* edit account in  My Production Report	*/
		
		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);	
		
		objDashboard.navigateTo_HistoricalReport().navigateTo_MyProductionDetails();
		Configuration.logger.log(LogStatus.INFO, "Navigate to My Production Details report");
		
		objReport.select_Client(client)
				.select_Location(location)
				.select_Practice(practice)
				.get_Report();
		Configuration.logger.log(LogStatus.INFO, "Get report for Client ["+client+"], Location ["+location+"], Practice ["+practice+"]");
		
		objReport.edit(encounterId);
		
		
		
		
		String nextQueue2 = excel.readValue(sheet, executableRowIndex, "nextQueue2");		
		data = excel.get_TransactionData(nextQueue2);
		
		claimStatus = data.get("claim_status");
		actionCode = data.get("action_code");
		resolveType = data.get("resolve_type"); 
		notes = data.get("note");
		
		
		Transaction updateAccount = objClaimTransaction.submit_Claim(claimStatus, actionCode, resolveType, notes);
		Assert.assertTrue(updateAccount.getStatus(), "Account [" + sAccountNumber +"] not submitted");
		logger.log(LogStatus.INFO, "<b>Claim moved to "+ nextQueue2 +" queue</b>");
		
		
		/* verify in My Production Report	*/
		SoftAssert s1 = objReport.verify_Production(sAccountNumber, encounterId, claimStatus, actionCode, notes);
	 	s1.assertAll();
	 	Configuration.logger.log(LogStatus.INFO, "<b>Updated Claim status, Action code verified in report</b>");
	 	
		objDashboard.signOut();
		
		
		
		/* verify in old & new next queue	*/
		/* verify account in respective next queue	*/
		objLogin.login(sCodingUsername, sCodingPassword);
		
		status = objReport.verify_InNextQueue(nextQueue1, UID, practice);
		Assert.assertFalse(status.getStatus(), status.getDescription());
		Configuration.logger.log(LogStatus.INFO, status.getDescription());
		
		objDashboard.signOut();	

		
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.PAYMENT);
		String sPaymentUsername = login.get("ntlg");
		String sPaymentPassword = login.get("password");	
		
		// login - Payment
		objLogin.login(sPaymentUsername, sPaymentPassword);
		
		status = objReport.verify_InNextQueue(nextQueue2, UID, practice);
		Assert.assertTrue(status.getStatus(), status.getDescription());
		Configuration.logger.log(LogStatus.INFO, status.getDescription());
		
		objDashboard.signOut();	

		
	}
}
