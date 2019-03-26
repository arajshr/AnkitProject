package dashboard;

import java.util.HashMap;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.Constants;
import pageObjectRepository.Dashboard;
import pageObjectRepository.Login;
import pageObjectRepository.dashboard.ARTrailBalanceReport;

public class ARTrailBalance extends Configuration
{
	Login objLogin;
	Dashboard objDashboard;
	ARTrailBalanceReport objReport;
	
	@Test
	public void verify_ARTrailBalance_Dashboard()
	{
		int executableRowIndex = excel.isExecutable("verify_ARTrailBalance_Dashboard");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		String client = excel.readValue(sheet, executableRowIndex, "client");
		String location = excel.readValue(sheet, executableRowIndex, "location");
		String practice = excel.readValue(sheet, executableRowIndex, "practice");
		
		String insurance = excel.readValue(sheet, executableRowIndex, "insurance");
		
		String fileName = excel.readValue(sheet, executableRowIndex, "fileName"); //"ALB-64-report.Output_20180411(11042018022730AM).xlsx";
		
		
		objLogin = PageFactory.initElements(driver, Login.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objReport = PageFactory.initElements(driver, ARTrailBalanceReport.class);
		
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.TL);
		String sTLUsername = login.get("ntlg");
		String sTLPassword = login.get("password");	

		// login - TL
		objLogin.login(sTLUsername, sTLPassword);		
		objDashboard.navigateTo_Dashboard().navigateTo_ClientPriorityTracking_Dashhboard();
		logger.log(LogStatus.INFO, "Navigate to Dashboard -> Client Priority Tracking Report");
		
		objReport.select_Client(client)
				.select_Location(location)
				.select_Practice(practice);
		
		objReport.get_ARTrailBalance_Report(practice, insurance).assertAll();
				
		
		
	}
}
