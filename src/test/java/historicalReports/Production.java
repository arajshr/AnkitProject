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
import pageObjectRepository.historicalReports.ProductionReport;
import pageObjectRepository.transaction.AnalystInbox;
import pageObjectRepository.transaction.ClaimTransaction;

public class Production extends Configuration
{
	Login objLogin;
	Dashboard objDashboard;
	ProductionReport objReport;
	AnalystInbox objAnalystInbox;
	ClaimTransaction objClaimTransaction;
	
	String fromDate = new SimpleDateFormat("dd/MM/yy").format(new Date()); // -- dd/MM/yyyy
	
	@Test(groups= {"REG"})
	public void verify_Production_Report()
	{
		int executableRowIndex = excel.isExecutable("verify_Production_Report");		
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
		objReport = PageFactory.initElements(driver, ProductionReport.class);
		objAnalystInbox = PageFactory.initElements(driver, AnalystInbox.class);
		objClaimTransaction = PageFactory.initElements(driver, ClaimTransaction.class);
		
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.MANAGER);
		String sMngrUsername = login.get("ntlg");
		String sMngrPassword = login.get("password");	

		// login - Manager
		objLogin.login(sMngrUsername, sMngrPassword);
		
		objDashboard.navigateTo_HistoricalReport().navigateTo_Production_Report();
		logger.log(LogStatus.INFO, "Navigate to Production report");
		
		objReport.select_FromDate(fromDate)
				.select_Client(client)
				.select_Location(location)
				.select_Practice(practice)
				//.select_User(data_report.get("emp_Id")) //emp_Id
				.get_Report();
		logger.log(LogStatus.INFO, "Get report for Client ["+ client +"], Location ["+ location +"], Practice ["+ practice +"]");
		
		int expProduction = 0, expTarget = 0, expProductionPercent = 0;
		int actProduction = 0, actTarget = 0, actProductionPercent = 0;
		
		
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");	

			
		
		if(!objReport.verify_User(sAnalystUsername))
		{
			expProduction = Integer.parseInt(objReport.get_Production(sAnalystUsername));
			expTarget = Integer.parseInt(objReport.get_Target(sAnalystUsername));
			expProductionPercent = Integer.parseInt(objReport.get_Production_Percent(sAnalystUsername));
		}
		
		objDashboard.signOut();
		
		
		
		
		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		logger.log(LogStatus.INFO, "Navigate to Transaction -> Inbox");
				
		objAnalystInbox.select_Practice(practice);
		objAnalystInbox.click_Image_Regular(true);
		logger.log(LogStatus.INFO, "Select practice ["+practice+"] and click REGULAR icon");
		
		Transaction rOpen = objAnalystInbox.open_PatientAccount(Constants.Queues.REGULAR);
		
		if(rOpen.getStatus())
		{
			
			String sAccountNumber = objClaimTransaction.get_PatientAccountNumber();
			logger.log(LogStatus.INFO,  "Open account [" + sAccountNumber + "]");
			
			Assert.assertTrue(objClaimTransaction.submit_Claim(claimStatus, actionCode, resolveType, notes).getStatus());
			logger.log(LogStatus.INFO, "<b>Account AR closed</b>");
			
			objDashboard.signOut();			
			
			
			// login - Manager
			objLogin.login(sMngrUsername, sMngrPassword);
			objDashboard.navigateTo_HistoricalReport().navigateTo_Production_Report();
			logger.log(LogStatus.INFO, "Navigate to Production report");
			
			objReport.select_FromDate(fromDate)
					.select_Client(client)
					.select_Location(location)
					.select_Practice(practice)
					.select_User(sAnalystUsername)
					.get_Report();
			logger.log(LogStatus.INFO, "Get report for Client ["+ client +"], Location ["+ location +"], Practice ["+ practice +"]");
			
			if(objReport.verify_User(sAnalystUsername))
			{
				actProduction = Integer.parseInt(objReport.get_Production(sAnalystUsername));
				actTarget = Integer.parseInt(objReport.get_Target(sAnalystUsername));
				actProductionPercent = Integer.parseInt(objReport.get_Production_Percent(sAnalystUsername));
			}
			
			
			int status = 1;
			StringBuilder sError = new StringBuilder();
			
			if(actProduction != expProduction)
			{
				sError.append("Production Accounts do not match.. expected ["+ expProduction +"] but found ["+ actProduction +"]<br>");
				status = 0;
			}
			if(actTarget != expTarget)
			{
				sError.append("Target Accounts do not match.. expected ["+ expTarget +"] but found ["+ actTarget +"]<br>");
				status = 0;
			}
			if(actProductionPercent != expProductionPercent)
			{
				sError.append("Production percentage do not match.. expected ["+ expProductionPercent +"] but found ["+ actProductionPercent +"]<br>");
				status = 0;
			}
			
			WebDriverUtils utils = new WebDriverUtils();
			if(status == 0)
			{
				utils.save_ScreenshotToReport("ProductionReport");
				Configuration.logger.log(LogStatus.FAIL, sError.toString());
			}
			else
			{
				Configuration.logger.log(LogStatus.INFO, "Poduction details verified");
			}
			
			
		}
		else
		{
			Configuration.logger.log(LogStatus.INFO, rOpen.getDescription());
		}
		
	}
}
