package dashboard;

import java.util.HashMap;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.Constants;
import configuration.WebDriverUtils;
import pageObjectRepository.Dashboard;
import pageObjectRepository.Login;
import pageObjectRepository.dashboard.ClientPriorityTrackingReport;

public class ClientPriorityTracking extends Configuration
{
	Login objLogin;
	Dashboard objDashboard;
	ClientPriorityTrackingReport objReport;
	
	
	@Test
	public void verify_ClientPriorityTracking_Dashboard()
	{
		int executableRowIndex = excel.isExecutable("verify_ClientPriorityTracking_Dashboard");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		String client = excel.readValue(sheet, executableRowIndex, "client");
		String location = excel.readValue(sheet, executableRowIndex, "location");
		String practice = excel.readValue(sheet, executableRowIndex, "practice");
		
		String fileName = excel.readValue(sheet, executableRowIndex, "fileName"); //"ALB-64-report.Output_20180411(11042018022730AM).xlsx";
		String uploadDate = excel.readValue(sheet, executableRowIndex, "uploadDate");
		
		objLogin = PageFactory.initElements(driver, Login.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objReport = PageFactory.initElements(driver, ClientPriorityTrackingReport.class);
		
		
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
				//.get_Report();
		
		
		
		objReport.verify_Client_Priority_Report(fileName, uploadDate).assertAll();
	
		new WebDriverUtils().save_ScreenshotToReport("ClientPriorityReport");
		logger.log(LogStatus.INFO, "<b>Client Priority Tracking Report verified</b>");
	}
}
