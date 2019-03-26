package master;

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
import customDataType.Info;
import pageObjectRepository.Login;
import pageObjectRepository.master.UsersAndRole;
import pageObjectRepository.Dashboard;

public class ConfigureUsersAndRoles extends Configuration
{
	WebDriverUtils utils = new WebDriverUtils();
	
	Login objLogin;
	Dashboard objDashboard;
	UsersAndRole objUsersAndRole;
	
	
	@Test(groups= {"REG"}, enabled = true, priority = 1)
	public void verify_UserConfiguration()
	{			
		logger = extent.startTest("Configure user");
		
		int executableRowIndex = excel.isExecutable("verify_UserConfiguration");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		String empID = excel.readValue(sheet, executableRowIndex, "empID");
		String role = excel.readValue(sheet, executableRowIndex, "role");
		String shift = excel.readValue(sheet, executableRowIndex, "shift");
		
		String expMessage = excel.readValue(sheet, executableRowIndex, "expMsg");
		
		objLogin = PageFactory.initElements(driver, Login.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objUsersAndRole = PageFactory.initElements(driver, UsersAndRole.class);
			
		
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.MANAGER);
		String sMngrUsername = login.get("ntlg");
		String sMngrPassword = login.get("password");	

		// login - Manager
		objLogin.login(sMngrUsername, sMngrPassword);
		
		// Navigate to Master -> Users & Roles	
		objDashboard.navigateTo_Master().navigateTo_UsersAndRole();
		logger.log(LogStatus.INFO, "Navigate to Master -> Users and Role sub menu");
				
		
		objUsersAndRole.click_Tab_UsersAndRoles();
		logger.log(LogStatus.INFO, "Navigate to Users and Role Tab");
		
		
		objUsersAndRole.click_Tab_ConfigureUser();
		boolean isFound = objUsersAndRole.search_ConfiguredUser(empID);
		utils.save_ScreenshotToReport("USER_" + empID);
		Assert.assertFalse(isFound, "User already congfigured");
		logger.log(LogStatus.INFO, "User not congfigured, Employee ID [" + empID + "]");
		
		
		
		// search employee using emp Id	
		objUsersAndRole.click_Tab_UsersAndRoles();
		Info search = objUsersAndRole.search_EmployeeByEmployeeId(empID);
		Assert.assertTrue(search.getStatus(), search.getDescription());
		
		
		// select employee, role and shift and Insert
		logger.log(LogStatus.INFO, "Configure User, Employee ID [" + empID + "], Role [" + role + "], Shift [" + shift + "]");
		
		String actMessage = objUsersAndRole.select_Emloyee(empID)
						.select_Role(role)
						.select_Shift(shift)
						.insert_RoleAndShift();
		
		SoftAssert s = new SoftAssert();
		s.assertEquals(actMessage, expMessage, "<b>Alert/ Popup message don't match, </b>");
		
		
		
		
		// verify message	
		objUsersAndRole.click_Tab_ConfigureUser();
		isFound = objUsersAndRole.search_ConfiguredUser(empID);
		Assert.assertTrue(isFound, "User not congfigured");
		
		s = objUsersAndRole.verify_UserConfiguration(empID, role, shift);
		s.assertAll();
		
		logger.log(LogStatus.INFO, "<b>User configuration details verified</b>");
		utils.save_ScreenshotToReport("ConfiguredUser");
	}
			
	@Test(groups= {"REG"}, enabled = true, priority = 2)
	public void verify_UpdateUserRole()
	{
		
		logger = extent.startTest("Update user role");
		
		int executableRowIndex = excel.isExecutable("verify_UpdateUserRole");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		String empID = excel.readValue(sheet, executableRowIndex, "empID");
		String role = excel.readValue(sheet, executableRowIndex, "role");
		String name = excel.readValue(sheet, executableRowIndex, "name");
		
		
		
		objLogin = PageFactory.initElements(driver, Login.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objUsersAndRole = PageFactory.initElements(driver, UsersAndRole.class);
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.MANAGER);
		String sMngrUsername = login.get("ntlg");
		String sMngrPassword = login.get("password");	

		// login - Manager
		objLogin.login(sMngrUsername, sMngrPassword);
		
		// Navigate to Master -> Users & Roles	
		objDashboard.navigateTo_Master().navigateTo_UsersAndRole();
		logger.log(LogStatus.INFO, "Navigate to Users and Role sub_Menu");
		
		objUsersAndRole.click_Tab_ConfigureUser();
		logger.log(LogStatus.INFO, "Navigate to Configured Users Tab and seach user ["+ empID +"]");
		Assert.assertTrue(objUsersAndRole.search_ConfiguredUser(empID), "User not found in Configured User tab");
		
		String sCurrentRole = objUsersAndRole.get_CurrentRoleFromConfiguredUser(empID);
		logger.log(LogStatus.INFO, "User found in Configured User tab, Employee ID [" + empID + "], Name ["+ name +"], Current role ["+ sCurrentRole +"]");
		
		
		
		utils.save_ScreenshotToReport("RoleBeforeUpdate");
		
		objUsersAndRole.update_Role(empID, role);
		
		
	}

	@Test(groups= {"REG"}, enabled = true, priority = 3)
	public void remove_User()
	{
		logger = extent.startTest("Remove user from application");
		
		int executableRowIndex = excel.isExecutable("remove_User");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		String empID = excel.readValue(sheet, executableRowIndex, "empID");
		String role = excel.readValue(sheet, executableRowIndex, "role");
		String shift = excel.readValue(sheet, executableRowIndex, "shift");
		String ntlg = excel.readValue(sheet, executableRowIndex, "ntlg");
		
		String expMessage = excel.readValue(sheet, executableRowIndex, "expMsg");
		
		objLogin = PageFactory.initElements(driver, Login.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objUsersAndRole = PageFactory.initElements(driver, UsersAndRole.class);
		
		
		
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.MANAGER);
		String sMngrUsername = login.get("ntlg");
		String sMngrPassword = login.get("password");	

		// login - Manager
		objLogin.login(sMngrUsername, sMngrPassword);
		
		// Navigate to Master -> Users & Roles	
		objDashboard.navigateTo_Master().navigateTo_UsersAndRole();
		logger.log(LogStatus.INFO, "Navigate to Users and Role sub_Menu");
		
		
		objUsersAndRole.click_Tab_ConfigureUser();
		boolean isConfigured = objUsersAndRole.search_ConfiguredUser(empID);
		
		/*Assert.assertFalse(isConfigured, "User Configuration not found for Employee ID ["+ empID +"] ");
		logger.log(LogStatus.INFO, "<b>User already configured, Employee ID ["+ empID +"]</b> ");*/
		
		
		if(!isConfigured)
		{
			logger.log(LogStatus.INFO, "User Configuration not found for Employee ID ["+ empID +"] ");
			
			objUsersAndRole.click_Tab_UsersAndRoles();
			logger.log(LogStatus.INFO, "Navigate to Users and Role Tab");
			
			
			// search employee using emp Id	
			Info search = objUsersAndRole.search_EmployeeByEmployeeId(empID);
			Assert.assertTrue(search.getStatus(), search.getDescription());
					
			
			// select employee, role and shift and Insert
			logger.log(LogStatus.INFO, "Configure User, Employee ID [" + empID + "], Role [" + role + "], Shift [" + shift + "]");
			
			String actMessage = objUsersAndRole.select_Emloyee(empID)
							.select_Role(role)
							.select_Shift(shift)
							.insert_RoleAndShift();
			
			if(!actMessage.equals(expMessage))
			{
				logger.log(LogStatus.ERROR, "<b>Alert/Popup message don't match, expected ["+actMessage+"] but found ["+actMessage+"]</b>");
			}//Assert.assertEquals(actMessage, expMessage, "<b>Alert/ Popup message don't match, </b>");
			
			
			
			objUsersAndRole.click_Tab_ConfigureUser();
			isConfigured = objUsersAndRole.search_ConfiguredUser(empID);
			
			Assert.assertTrue(isConfigured, "User Configuration not found for Employee ID ["+ empID +"] ");
			logger.log(LogStatus.INFO, "<b>User configured, Employee ID ["+ empID +"]</b> ");
		}
		
		
		String actMessage = objUsersAndRole.remove_User(empID);
		Assert.assertEquals(actMessage, expMessage, "<b>Alert/Popup message don't match, </b>");
		logger.log(LogStatus.INFO, "<b>User Configuration removed, Employee ID ["+ empID +"]</b> ");
		
		objUsersAndRole.click_Tab_ConfigureUser();
		isConfigured = objUsersAndRole.search_ConfiguredUser(empID);
		logger.log(LogStatus.INFO, "<b>Navigate to Configured User and Search Employee ID ["+ empID +"]</b> ");
		utils.save_ScreenshotToReport("ConfiguredUser");
		
		Assert.assertFalse(isConfigured, "User Configuration still found for Employee ID ["+ empID +"] ");
		logger.log(LogStatus.INFO, "<b>Employee ID ["+ empID +"] not found in Configured User</b> ");
		
		
		objDashboard.signOut();
		
		
		logger.log(LogStatus.INFO, "Log In as " + ntlg);
		Info loginStatus = objLogin.login(ntlg, "12345");
		utils.save_ScreenshotToReport("NoAlert");
		Assert.assertFalse(loginStatus.getStatus(), loginStatus.getDescription() + ", expected an alert message [User not mapped with ATOM]");
		
		if(loginStatus.getDescription().equals("No alert message shown"))
		{
			logger.log(LogStatus.FAIL, loginStatus.getDescription() + ", expected an alert message[User not mapped with ATOM]");
		}
		
	}
	
	@Test(groups= {"REG"}, enabled = true, priority = 4)
	public void verify_UserAlreadyExist()
	{
		logger = extent.startTest("Configure existing user");
		
		int executableRowIndex = excel.isExecutable("verify_UserAlreadyExist");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		String empID = excel.readValue(sheet, executableRowIndex, "empID");
		String role = excel.readValue(sheet, executableRowIndex, "role");
		String shift = excel.readValue(sheet, executableRowIndex, "shift");
		
		String expMessage = excel.readValue(sheet, executableRowIndex, "expMsg");
		
		objLogin = PageFactory.initElements(driver, Login.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objUsersAndRole = PageFactory.initElements(driver, UsersAndRole.class);
		
		
		
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.MANAGER);
		String sMngrUsername = login.get("ntlg");
		String sMngrPassword = login.get("password");	

		// login - Manager
		objLogin.login(sMngrUsername, sMngrPassword);
		
		// Navigate to Master -> Users & Roles	
		objDashboard.navigateTo_Master().navigateTo_UsersAndRole();
		logger.log(LogStatus.INFO, "Navigate to Users and Role sub_Menu");
		
		
		objUsersAndRole.click_Tab_ConfigureUser();
		boolean isConfigured = objUsersAndRole.search_ConfiguredUser(empID);
		
		Assert.assertTrue(isConfigured, "User Configuration not found for Employee ID ["+ empID +"] ");
		logger.log(LogStatus.INFO, "<b>User already configured, Employee ID ["+ empID +"]</b> ");
		utils.save_ScreenshotToReport("UserAlreadyExists");
		
		objUsersAndRole.click_Tab_UsersAndRoles();
		logger.log(LogStatus.INFO, "Navigate to Users and Role Tab");
		
		
		// search employee using emp Id	
		Info search = objUsersAndRole.search_EmployeeByEmployeeId(empID);
		Assert.assertTrue(search.getStatus(), search.getDescription());
				
		
		// select employee, role and shift and Insert
		logger.log(LogStatus.INFO, "Configure User, Employee ID [" + empID + "], Role [" + role + "], Shift [" + shift + "]");
		
		String actMessage = objUsersAndRole.select_Emloyee(empID)
						.select_Role(role)
						.select_Shift(shift)
						.insert_RoleAndShift();
		
		
		if(!actMessage.equals(expMessage))
		{
			logger.log(LogStatus.FAIL, "<b>Alert/ Popup message don't match, expected ["+expMessage+"] but found ["+actMessage+"]</b>");
		}
		
		objUsersAndRole.click_Tab_ConfigureUser();
		isConfigured = objUsersAndRole.search_ConfiguredUser(empID);
		utils.save_ScreenshotToReport("ReConfigure");
	}
	

}
