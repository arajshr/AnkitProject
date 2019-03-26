package master;

import java.util.HashMap;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.Constants;
import customDataType.Transaction;
import pageObjectRepository.Dashboard;
import pageObjectRepository.Login;
import pageObjectRepository.master.Insurance;
import pageObjectRepository.transaction.AnalystInbox;

public class CreateInsurance extends Configuration
{
	Login objLogin;
	Dashboard objDashboard;
	Insurance objInsurance;
	AnalystInbox objAnalystInbox;
	
	@Test
	public void verify_Insurance()
	{
		objLogin = PageFactory.initElements(driver, Login.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objInsurance = PageFactory.initElements(driver, Insurance.class);
		


		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.MANAGER);
		String sMngrUsername = login.get("ntlg");
		String sMngrPassword = login.get("password");	

		// login - Manager
		objLogin.login(sMngrUsername, sMngrPassword);
		objDashboard.navigateTo_Master().navigateTo_Insurance();
	
		
		objInsurance.select_Practice("");
		Transaction rAddIns = objInsurance.set_InsuranceCode("insCode")
					.set_PayerName("payerName")
					.set_Address("address")
					.set_ContactPhone("contact")
					.set_State("state")
					.select_PayerWebsite("payerWebsite")
					.add_Insurance();
	
		if(rAddIns.getStatus())
		{
			objInsurance.verify_Insurance("insCode");
			
		}
		else
		{
			logger.log(LogStatus.FAIL, rAddIns.getDescription());
		}
	}
}
