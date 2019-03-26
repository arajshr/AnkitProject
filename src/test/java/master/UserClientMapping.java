package master;

import java.util.HashMap;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.Constants;
import configuration.WebDriverUtils;
import customDataType.Info;
import pageObjectRepository.Login;
import pageObjectRepository.master.UsersAndRole;
import pageObjectRepository.Dashboard;

public class UserClientMapping extends Configuration
{
	WebDriverUtils utils = new WebDriverUtils();
	Login objLogin;
	Dashboard objManagerDashboard;
	UsersAndRole objUsersAndRole;
	
	
	
	@Test(enabled = true, priority = 1, groups= {"REG"})
	public void verify_UserClientMapping()
	{
		logger = extent.startTest("Configure client to user");
		
		int executableRowIndex = excel.isExecutable("verify_UserClientMapping");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		String client = excel.readValue(sheet, executableRowIndex, "client");
		String location = excel.readValue(sheet, executableRowIndex, "location");
		String practice = excel.readValue(sheet, executableRowIndex, "practice");
		
		String user = excel.readValue(sheet, executableRowIndex, "user");
		
		
		
		objLogin = PageFactory.initElements(driver, Login.class);
		objManagerDashboard = PageFactory.initElements(driver, Dashboard.class);
		objUsersAndRole = PageFactory.initElements(driver, UsersAndRole.class);
		


		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.MANAGER);
		String sMngrUsername = login.get("ntlg");
		String sMngrPassword = login.get("password");	

		// login - Manager
		objLogin.login(sMngrUsername, sMngrPassword);
		
		// Navigate to Master -> Users & Roles	
		objManagerDashboard.navigateTo_Master().navigateTo_UsersAndRole();
		
		// Navigate to User client configuration tab	
		objUsersAndRole.click_Tab_UserClientMapping();
		logger.log(LogStatus.INFO, "Navigate to Users and Role and User Client Mapping tab");
		
		objUsersAndRole.select_Client(client)
					.select_Location(location)
					.search_Practice(practice);
		logger.log(LogStatus.INFO, "Select client: " + client + " location: " + location + " practice: " + practice + " and user: "+ user);
		
		
		Info result = objUsersAndRole.select_Practice(practice)
									 .select_UserForPractice(user)
									 .submit_UserClientConfiguration();
		
		if(result.getStatus())
		{			
			objUsersAndRole.click_Tab_UserPracticeMapping();
			logger.log(LogStatus.INFO, "Navigate to User Practice Mapping tab");
			
			Info search = objUsersAndRole.verify_UserPracticeMapping(user, practice);
			Assert.assertTrue(search.getStatus(), search.getDescription());
			logger.log(LogStatus.INFO, "<b>User Client Mapping verified</b>");
		}
	}
	
	

	
	
	
	
	@Test(enabled = true, priority = 2, groups= {"REG"})
	public void verify_ClientDuplicateMapping() throws InterruptedException
	{		
		logger = extent.startTest("Configure already mapped client to user");
		
		int executableRowIndex = excel.isExecutable("verify_ClientDuplicateMapping");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		String client = excel.readValue(sheet, executableRowIndex, "client");
		String location = excel.readValue(sheet, executableRowIndex, "location");
		String practice = excel.readValue(sheet, executableRowIndex, "practice");
		
		String user = excel.readValue(sheet, executableRowIndex, "user");
		
		
		
		objLogin = PageFactory.initElements(driver, Login.class);
		objManagerDashboard = PageFactory.initElements(driver, Dashboard.class);
		objUsersAndRole = PageFactory.initElements(driver, UsersAndRole.class);
		


		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.MANAGER);
		String sMngrUsername = login.get("ntlg");
		String sMngrPassword = login.get("password");	

		// login - Manager
		objLogin.login(sMngrUsername, sMngrPassword);
		
		// Navigate to Master -> Users & Roles	
		objManagerDashboard.navigateTo_Master().navigateTo_UsersAndRole();
		objUsersAndRole.click_Tab_UserPracticeMapping();
		logger.log(LogStatus.INFO, "Navigate to Users and Role and click User Practice Mapping tab");
		
		Info search = objUsersAndRole.verify_UserPracticeMapping(user, practice);
		Assert.assertTrue(search.getStatus(), search.getDescription());
		logger.log(LogStatus.INFO, search.getDescription());
		
		// Navigate to User client configuration tab	
		objUsersAndRole.click_Tab_UserClientMapping();
		logger.log(LogStatus.INFO, "Navigate to User Client Configuration tab");
		
		
		objUsersAndRole.select_Client(client)
						.select_Location(location)
						.search_Practice(practice);
		logger.log(LogStatus.INFO, "<b>Select client: " + client + " location: " + location + " practice: " + practice + " and user: "+ user + "</b>");
				
		
		
		Info result = objUsersAndRole.select_Practice(practice)
				 .select_UserForPractice(user)
				 .submit_UserClientConfiguration();
		
		//logger.log(LogStatus.INFO, "<b>User Client mapping saved</b>");
		
		
		if(search.getStatus() ) // already present
		{
			if(result.getStatus()) // saved successfully
			{			
				Assert.assertFalse(true, "<b>'User Mapping Already Exist' message not displayed</b>"); // assert for mapping already exists
				
			}
			else // User Mapping Already Exist
			{				
				logger.log(LogStatus.INFO, "<b>User Mapping Already Exist</b>");
				
				objUsersAndRole.click_Tab_UserPracticeMapping();
				logger.log(LogStatus.INFO, "Navigate to User Practice Mapping tab");
				
				objUsersAndRole.search_UserPracticeMapping(user);
				logger.log(LogStatus.INFO, "<b>User Practice mapping verified</b>");
			}
		}
		else // not found
		{
			if(result.getStatus()) // saved successfully
			{			
				
				utils.scrollWindow(0, -2000);
				
				objUsersAndRole.select_Client(client)
								.select_Location(location)
								.search_Practice(practice);
				logger.log(LogStatus.INFO, "Select client: " + client + " location: " + location + " practice: " + practice + " and user: "+ user);
				
				
				result = objUsersAndRole.select_Practice(practice)
						 .select_UserForPractice(user)
						 .submit_UserClientConfiguration();  // SAVE AGAIN
				
				Assert.assertFalse(result.getStatus(), result.getDescription()); // assert for User Mapping Already Exist
				
			}
		}
	}

	@Test(enabled = true, priority = 3, groups= {"REG"})
	public void remove_ClientMapping()
	{
		logger = extent.startTest("Remove client configuration for user");
		
		int executableRowIndex = excel.isExecutable("remove_ClientMapping");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		String client = excel.readValue(sheet, executableRowIndex, "client");
		String location = excel.readValue(sheet, executableRowIndex, "location");
		String practice = excel.readValue(sheet, executableRowIndex, "practice");
		
		String user = excel.readValue(sheet, executableRowIndex, "user");
		
		
		
		objLogin = PageFactory.initElements(driver, Login.class);
		objManagerDashboard = PageFactory.initElements(driver, Dashboard.class);
		objUsersAndRole = PageFactory.initElements(driver, UsersAndRole.class);
		


		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.MANAGER);
		String sMngrUsername = login.get("ntlg");
		String sMngrPassword = login.get("password");	

		// login - Manager
		objLogin.login(sMngrUsername, sMngrPassword);
		
		// Navigate to Master -> Users & Roles	
		objManagerDashboard.navigateTo_Master().navigateTo_UsersAndRole();
		logger.log(LogStatus.INFO, "Navigate to Users and Role and click User Client Mapping tab");
		
		// Navigate to User client configuration tab	
		objUsersAndRole.click_Tab_UserClientMapping();
		
		objUsersAndRole.select_Client(client)
					.select_Location(location)
					.search_Practice(practice);
		logger.log(LogStatus.INFO, "Select client: " + client + " location: " + location + " practice: " + practice + " and user: "+ user);
		
		Info result = objUsersAndRole.select_Practice(practice)
						.select_UserForPractice(user)
						.submit_UserClientConfiguration();
						
		Assert.assertTrue(result.getStatus(), result.getDescription());
		logger.log(LogStatus.INFO, result.getDescription());
		

		// Navigate to User client configuration tab
		objUsersAndRole.click_Tab_UserPracticeMapping();
		logger.log(LogStatus.INFO, "Navigate to User Client Practice Mapping tab");
		
		objUsersAndRole.search_UserPracticeMapping(user);

		objUsersAndRole.delete_UserPracticeMapping(practice);
		
		Info search = objUsersAndRole.verify_UserPracticeMapping(user, practice);
		Assert.assertFalse(search.getStatus(), search.getDescription());
		
	}
	
	
}
