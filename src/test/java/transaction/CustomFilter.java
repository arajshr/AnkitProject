package transaction;

import java.util.HashMap;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.Constants;
import pageObjectRepository.Dashboard;
import pageObjectRepository.Login;
import pageObjectRepository.transaction.AnalystInbox;
import pageObjectRepository.transaction.ClaimTransaction;

public class CustomFilter extends Configuration
{
	
	Login objLogin;
	Dashboard objDashboard;
	AnalystInbox objInbox;
	ClaimTransaction objClaimTransaction;
	
	
	
	@Test
	public void verify_CustomFilter()
	{
		logger = extent.startTest("Verifying Custom filter in Analyst inbox");
		
		int executableRowIndex = excel.isExecutable("verify_CustomFilter");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		
		String practice = excel.readValue(sheet, executableRowIndex, "practice");
		
		
		
		
		objLogin = PageFactory.initElements(Configuration.driver, Login.class);
		objDashboard = PageFactory.initElements(Configuration.driver, Dashboard.class);
		objInbox = PageFactory.initElements(Configuration.driver, AnalystInbox.class);
		objClaimTransaction = PageFactory.initElements(Configuration.driver, ClaimTransaction.class);
		
		
		
		
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sUsername = login.get("ntlg");
		String sPassword = login.get("password");	
		
		// login
		objLogin.login(sUsername, sPassword);
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		logger.log(LogStatus.INFO, "Navigate to Transaction -> Inbox");
		
	 	//	select practice
		logger.log(LogStatus.INFO, "Select practice as ["+ practice +"] and click Regular queue");
		objInbox.select_Practice(practice); 	
	 	objInbox.click_Image_Regular(true);	 	
	 	
		objInbox.open_CustomFilter();
		
		
		String sColumn = "Patient_Account_No";
		String sCondition = "Equal to";
		String sValue = "20181029-108";
		
		objInbox.add_Condition(sColumn, sCondition, sValue);
		objInbox.search_CustomFilter();
		
		objInbox.verify_CustomFilter(sColumn, sCondition, sValue);
	 	
	}
}
