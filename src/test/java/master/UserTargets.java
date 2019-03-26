package master;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import pageObjectRepository.Dashboard;
import pageObjectRepository.Login;
import pageObjectRepository.master.UsersAndRole;

public class UserTargets extends Configuration
{
	WebDriverUtils utils = new WebDriverUtils();
	Login objLogin;
	Dashboard objManagerDashboard;
	UsersAndRole objUsersAndRole;
	
	
	@Test(groups= {"REG"}, priority = 1, enabled = false)
	public void verify_UserTargets() throws ParseException
	{
		logger = extent.startTest("Set user target for a practice");
		
		
		int executableRowIndex = excel.isExecutable("verify_UserTargets");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		String empID = excel.readValue(sheet, executableRowIndex, "empID");
		String practice = excel.readValue(sheet, executableRowIndex, "practice");
		String target = excel.readValue(sheet, executableRowIndex, "target");
		String auditPercent = excel.readValue(sheet, executableRowIndex, "auditPercent");
		String targetFromDate = excel.readValue(sheet, executableRowIndex, "targetFromDate");
		targetFromDate = new SimpleDateFormat("MM/dd/yyyy").format(new SimpleDateFormat("MM/dd/yy").parse(targetFromDate));
		
		
		
		
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
		objUsersAndRole.click_Tab_UserTargets();
		logger.log(LogStatus.INFO, "Navigate to Users and Role -> User Targets tab");
		
		objUsersAndRole.search_UserTargetByEmployeeId(empID);
		logger.log(LogStatus.INFO, "Search User Target values , EmployeeID ["+empID+"]");
		
		
		logger.log(LogStatus.INFO, "Set User Target values , Target ["+target+"], Audit Percent ["+auditPercent+"], From Date ["+targetFromDate+"], Practice ["+practice+"]");
		Info status = objUsersAndRole.set_UserTarget(practice, target, auditPercent, targetFromDate);		
		Assert.assertTrue(status.getStatus(), status.getDescription());	
		logger.log(LogStatus.INFO, status.getDescription());
		
		
		boolean targetStatus = objUsersAndRole.verify_UserTarget(practice, target, auditPercent, targetFromDate);
		Assert.assertTrue(targetStatus, "User Target not found");		
		logger.log(LogStatus.INFO, "<b>Save User Target verified</b>");
		
	}
	
	@Test(groups= {"REG"}, priority = 2, enabled = false)
	public void update_UserTargets() throws ParseException
	{
		logger = extent.startTest("Update user target for a practice");
		
		int executableRowIndex = excel.isExecutable("update_UserTargets");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		String empID = excel.readValue(sheet, executableRowIndex, "empID");
		String practice = excel.readValue(sheet, executableRowIndex, "practice");
		String target = excel.readValue(sheet, executableRowIndex, "target");
		String auditPercent = excel.readValue(sheet, executableRowIndex, "auditPercent");
		
		String targetFromDate = excel.readValue(sheet, executableRowIndex, "targetFromDate");
		targetFromDate = new SimpleDateFormat("MM/dd/yyyy").format(new SimpleDateFormat("MM/dd/yy").parse(targetFromDate));
		
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
		logger.log(LogStatus.INFO, "Navigate to Users and Role");
		
		// Navigate to User client configuration tab	
		objUsersAndRole.click_Tab_UserTargets();
		logger.log(LogStatus.INFO, "Navigate to User Targets tab");
		
		objUsersAndRole.search_UserTargetByEmployeeId(empID);
		logger.log(LogStatus.INFO, "Search User Target values , EmployeeID ["+empID+"]");
		
		
		logger.log(LogStatus.INFO, "Update User Target to ["+ target +"]");
		objUsersAndRole.modify_UserTarget(practice, target, auditPercent, targetFromDate);
		
		
		objUsersAndRole.search_UserTargetByEmployeeId(empID);
		logger.log(LogStatus.INFO, "Search User Target values , EmployeeID ["+empID+"]");
		
		
		boolean targetStatus = objUsersAndRole.verify_UserTarget(practice, target, auditPercent, targetFromDate);
		Assert.assertTrue(targetStatus, "User Target not found");		
		logger.log(LogStatus.INFO, "<b>Updated User Target verified</b>");
		
	}
	
		
	@Test(groups= {"REG"}, priority = 3, enabled = true)
	public void verify_UserTargetForPrevoiusDates() throws ParseException
	{
		logger = extent.startTest("Set user target for a practice with previous dates");
		
		int executableRowIndex = excel.isExecutable("verify_UserTargetForPrevoiusDates");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		String empID = excel.readValue(sheet, executableRowIndex, "empID");
		String practice = excel.readValue(sheet, executableRowIndex, "practice");
		String target = excel.readValue(sheet, executableRowIndex, "target");
		String auditPercent = excel.readValue(sheet, executableRowIndex, "auditPercent");
		
		String targetFromDate = excel.readValue(sheet, executableRowIndex, "targetFromDate");
		targetFromDate = new SimpleDateFormat("MM/dd/yyyy").format(new SimpleDateFormat("MM/dd/yy").parse(targetFromDate));
		
		
		
		
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
		objUsersAndRole.click_Tab_UserTargets();
		logger.log(LogStatus.INFO, "Navigate to Users and Role -> User Targets tab");
		
		
		objUsersAndRole.search_UserTargetByEmployeeId(empID);
		logger.log(LogStatus.INFO, "Search User Target values , EmployeeID ["+empID+"]");
		
		logger.log(LogStatus.INFO, "Set user target for prevoius date , Target ["+ target +"], Audit Percent ["+auditPercent+"], From Date ["+targetFromDate+"], Practice ["+practice+"]");
		Info status = objUsersAndRole.set_UserTarget(practice, target, auditPercent, targetFromDate);		
		
		if(status.getStatus()) // when saved successfully
		{
			boolean targetStatus = objUsersAndRole.verify_UserTarget(practice, target, auditPercent, targetFromDate);
			
			if(!targetStatus)
				logger.log(LogStatus.INFO, "<b>User Target not found</b>");
			
			
			Assert.assertFalse(status.getStatus(), "<b>Target for date ["+targetFromDate+"] saved successfully</b><br>" + status.getDescription());
		}
		
		
	}
	
	
	@Test(groups= {"REG"}, enabled = false, priority = 4)
	public void verify_UserTargetOverlappingDates() throws ParseException
	{
		logger = extent.startTest("Set user target for a practice with overlapping dates");
		
		int executableRowIndex = excel.isExecutable("verify_UserTargetOverlappingDates");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		String empID = excel.readValue(sheet, executableRowIndex, "empID");
		String practice = excel.readValue(sheet, executableRowIndex, "practice");
		
		
		/*String target = excel.readValue(sheet, executableRowIndex, "target");
		String auditPercent = excel.readValue(sheet, executableRowIndex, "auditPercent");
		String targetFromDate = excel.readValue(sheet, executableRowIndex, "targetFromDate");*/
		
		
		
		
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
		objUsersAndRole.click_Tab_UserTargets();
		logger.log(LogStatus.INFO, "Navigate to Users and Role -> User Targets tab");
		
		
		
		
		objUsersAndRole.search_UserTargetByEmployeeId(empID);		
		logger.log(LogStatus.INFO, "Search User Target values , EmployeeID ["+empID+"]");
		
		String target1 = excel.readValue(sheet, executableRowIndex, "target");
		String auditPercent1 = excel.readValue(sheet, executableRowIndex, "auditPercent");
		String targetFromDate1 = excel.readValue(sheet, executableRowIndex, "targetFromDate");
		
		logger.log(LogStatus.INFO, "Set User Target values , Target ["+target1+"], Audit Percent ["+auditPercent1+"], From Date ["+targetFromDate1+"], Practice ["+practice+"]");
		Info status = objUsersAndRole.set_UserTarget(practice, target1, auditPercent1, targetFromDate1);	
		Assert.assertTrue(status.getStatus(), status.getDescription());	
		
		
		boolean targetStatus = objUsersAndRole.verify_UserTarget(practice, target1, auditPercent1, targetFromDate1);
		if(!targetStatus)
			logger.log(LogStatus.INFO, "<b>User Target not found</b>");
		
		
		
		
		String target2 = excel.readValue(sheet, executableRowIndex, "target2");
		String auditPercent2 = excel.readValue(sheet, executableRowIndex, "auditPercent2");
		String targetFromDate2 = excel.readValue(sheet, executableRowIndex, "targetFromDate2");
		
		
		logger.log(LogStatus.INFO, "User Target values , Target ["+target2+"], Audit Percent ["+auditPercent2+"], From Date ["+targetFromDate2+"], Practice ["+practice+"]");
	    status = objUsersAndRole.set_UserTarget(practice, target2, auditPercent2, targetFromDate2);	
		Assert.assertTrue(status.getStatus(), status.getDescription());
		
		
		targetStatus = objUsersAndRole.verify_UserTarget(practice, target2, auditPercent2, targetFromDate2);
		if(!targetStatus)
			logger.log(LogStatus.INFO, "<b>User Target not found</b>");
		
		
		
		
		
		
		String target3 = excel.readValue(sheet, executableRowIndex, "target3");
		String auditPercent3 = excel.readValue(sheet, executableRowIndex, "auditPercent3");
		String targetFromDate3 = excel.readValue(sheet, executableRowIndex, "targetFromDate3");
		
		
		logger.log(LogStatus.INFO, "User Target values , Target ["+target3+"], Audit Percent ["+auditPercent3+"], From Date ["+targetFromDate3+"], Practice ["+practice+"]");
		status = objUsersAndRole.set_UserTarget(practice, target3, auditPercent3, targetFromDate3);	
		Assert.assertTrue(status.getStatus(), status.getDescription());
		
		
		targetStatus = objUsersAndRole.verify_UserTarget(practice, target3, auditPercent3, targetFromDate3);
		if(!targetStatus)
			logger.log(LogStatus.INFO, "<b>User Target not found</b>");
		
		
		
		
		
		String target4 = excel.readValue(sheet, executableRowIndex, "target4");
		String auditPercent4 = excel.readValue(sheet, executableRowIndex, "auditPercent4");
		String targetFromDate4 = excel.readValue(sheet, executableRowIndex, "targetFromDate4");
		
		
		logger.log(LogStatus.INFO, "User Target values , Target ["+target4+"], Audit Percent ["+auditPercent4+"], From Date ["+targetFromDate4+"], Practice ["+practice+"]");
		status = objUsersAndRole.set_UserTarget(practice, target4, auditPercent4, targetFromDate4);	
		Assert.assertTrue(status.getStatus(), status.getDescription());
		
		
		targetStatus = objUsersAndRole.verify_UserTarget(practice, target4, auditPercent4, targetFromDate4);
		if(!targetStatus)
			logger.log(LogStatus.INFO, "<b>User Target not found</b>");
		
		
		
		objUsersAndRole.verify_TargetDates(practice);
		
	}
	
	
	
	
	
	
}
