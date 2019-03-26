package inbox;

import java.util.HashMap;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import configuration.Configuration;
import configuration.Constants;
import pageObjectRepository.Login;

public class Sample extends Configuration
{
	Login objLogin;
	
	@Test
	public void verify_Denail_DOS()
	{
		logger = extent.startTest("Verify accounts under different DOS categories in Denial queue");
		
		int executableRowIndex = excel.isExecutable("verify_Denail_DOS");		
		XSSFSheet sheet = excel.setSheet("TestData");		
		
		String practice = excel.readValue(sheet, executableRowIndex, "practice");
		
		
		objLogin = PageFactory.initElements(driver, Login.class);		
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");	

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
		
		Assert.assertFalse(true);
	}
	
	@Test
	public void verify_KickOut_DOS()
	{
		logger = extent.startTest("Verify accounts under different DOS categories in Kickout queue");
		
		int executableRowIndex = excel.isExecutable("verify_KickOut_DOS");		
		XSSFSheet sheet = excel.setSheet("TestData");		
		
		String practice = excel.readValue(sheet, executableRowIndex, "practice");
		
		objLogin = PageFactory.initElements(driver, Login.class);
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");	

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
	}
	
	@Test
	public void verify_Regular_DOS()
	{
		logger = extent.startTest("Verify accounts under different DOS categories in Regular queue");
		
		int executableRowIndex = excel.isExecutable("verify_Regular_DOS");		
		XSSFSheet sheet = excel.setSheet("TestData");		
		
		String practice = excel.readValue(sheet, executableRowIndex, "practice");
		
		objLogin = PageFactory.initElements(driver, Login.class);		
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");	

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
	}
}
