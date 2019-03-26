package master;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.Constants;
import configuration.WebDriverUtils;
import pageObjectRepository.Login;
import pageObjectRepository.master.AllocationRule;
import pageObjectRepository.transaction.AnalystInbox;
import pageObjectRepository.Dashboard;

public class AllocationRules extends Configuration
{
	WebDriverUtils utils = new WebDriverUtils();
	
	Login objLogin;
	Dashboard objDashboard;
	AllocationRule objAllocationRule;
	AnalystInbox objAnalystInbox;
	
	@Test(enabled = false)
	public void create_AllocationRule()
	{
		int executableRowIndex = excel.isExecutable("create_AllocationRule");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		String client = excel.readValue(sheet, executableRowIndex, "client");
		String location = excel.readValue(sheet, executableRowIndex, "location");
		String practice = excel.readValue(sheet, executableRowIndex, "practice");
		
		objLogin = PageFactory.initElements(driver, Login.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objAllocationRule = PageFactory.initElements(driver, AllocationRule.class);
		
		
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.MANAGER);
		String sMngrUsername = login.get("ntlg");
		String sMngrPassword = login.get("password");	

		// login - Manager
		objLogin.login(sMngrUsername, sMngrPassword);
		objDashboard.navigateTo_Master().navigateTo_AllocationRule();
		objAllocationRule.click_Tab_Allot_Rule();
		
		objAllocationRule.select_ClientForUserMapping("Med Data ED")						
						.select_LocationForUserMapping("ED")
						.select_PracticeForUserMapping("BYP");
		
		objAllocationRule.open_New_Allot_Rule_Window();
		boolean condition = objAllocationRule.create_Allot_Rule("", "", "", "").save_Allot_Rule();
		
		Assert.assertTrue(condition, "Allocation rule not created!!");
		Configuration.logger.log(LogStatus.PASS, "Allocation rule created successfully!!");
		
		
	}
	
	@Test(enabled = true)
	public void verify_AllocationRule()
	{
		
		/*
		 * set allocation rule
		 * upload data
		 * verify 
		 *  
		 */
		
		
		int executableRowIndex = excel.isExecutable("verify_AllocationRule");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		String client = excel.readValue(sheet, executableRowIndex, "client");
		String location = excel.readValue(sheet, executableRowIndex, "location");
		String practice = excel.readValue(sheet, executableRowIndex, "practice");
		
		String ruleFor = excel.readValue(sheet, executableRowIndex, "ruleFor");
		String column = excel.readValue(sheet, executableRowIndex, "column");
		String condition = excel.readValue(sheet, executableRowIndex, "condition");
		String value = excel.readValue(sheet, executableRowIndex, "value");
		
		String user = excel.readValue(sheet, executableRowIndex, "name");
		
		
		objLogin = PageFactory.initElements(driver, Login.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objAllocationRule = PageFactory.initElements(driver, AllocationRule.class);
		objAnalystInbox = PageFactory.initElements(driver, AnalystInbox.class);
		
		
		
		
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.MANAGER);
		String sMngrUsername = login.get("ntlg");
		String sMngrPassword = login.get("password");	

		// login - Manager
		objLogin.login(sMngrUsername, sMngrPassword);
		
		
		// save allot rule
		objDashboard.navigateTo_Master().navigateTo_AllocationRule();
		objAllocationRule.click_Tab_Allot_Rule();
		
		objAllocationRule.select_ClientForAllotRule(client)						
						.select_LocationForAllotRule(location)
						.select_PracticeForAllotRule(practice);
		
		objAllocationRule.open_New_Allot_Rule_Window();
		boolean result = objAllocationRule.create_Allot_Rule(ruleFor, column, condition, value).save_Allot_Rule();
		
		Assert.assertTrue(result, "Allocation rule not created!!");
		Configuration.logger.log(LogStatus.INFO, "<b>Allocation rule created</b>");
		
		
		
			
		
		// map user
		objAllocationRule.click_Tab_User_Mapping();
		
		objAllocationRule.select_ClientForUserMapping(client)						
						.select_LocationForUserMapping(location)
						.select_PracticeForUserMapping(practice);
		
		
		//HashMap<String, List<String>> ruleUserMapping = objAllocationRule.get_AllocationRules_Mapped_Users();		
		objDashboard.signOut();
		
				
		// get login credentials
		HashMap<String, String> userCredentials = excel.get_UserCredential("NAME", user);
		objLogin.login(userCredentials.get("userName"), userCredentials.get("password"));
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		objAnalystInbox.select_Practice(practice);
		objAnalystInbox.click_Image_Regular(false);
		 
		/* 
		ExcelHelper eAging = new ExcelHelper(Constants.xlAging_ForAllocation);
		ArrayList<String> lstAccountNumbers = eAging.get_AccountsForAllocation("Sheet1");
		objAnalystInbox.verify_Allocation_Rule(lstAccountNumbers);*/
			 
			 
			
	}
}
