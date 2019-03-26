package inbox;

import java.util.ArrayList;
import java.util.HashMap;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.Constants;
import configuration.WebDriverUtils;
import customDataType.Info;
import pageObjectRepository.Dashboard;
import pageObjectRepository.Login;
import pageObjectRepository.transaction.CodingInbox;
import pageObjectRepository.transaction.PaymentInbox;
import pageObjectRepository.transaction.PrintAndMailInbox;

public class WorkOrderSummaryResponse extends Configuration
{
	WebDriverUtils utils = new WebDriverUtils();
	
	Login objLogin;
	Dashboard objDashboard;
	

	@Test(groups={"REG"}, enabled = true)
 	public void verify_WorkOrderSummary_In_PaymentInbox()
 	{	
		logger = extent.startTest("Verifying Work Order Summary in Payment role");
		
		/*int executableRowIndex = excel.isExecutable("verify_WorkOrderSummary_In_PaymentInbox");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		String practice = excel.readValue(sheet, executableRowIndex, "practice");*/
		
		PaymentInbox objInbox;
		
 		objLogin = PageFactory.initElements(driver, Login.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objInbox = PageFactory.initElements(driver, PaymentInbox.class);
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.PAYMENT);
		String sPaymentUsername = login.get("ntlg");
		String sPaymentPassword = login.get("password");	
		
		// login - Payment
		objLogin.login(sPaymentUsername, sPaymentPassword);
			
				
		ArrayList<String> lstPractice = objInbox.get_AllPractices();
		
		for (String practice : lstPractice) 
		{
			logger.log(LogStatus.INFO, "<b>For Practice [" + practice + "]</b>");
			
			Info result = objInbox.select_Practice(practice.substring(0, practice.indexOf("(")-1), false);
			if(!result.getStatus())
			{
				Configuration.logger.log(LogStatus.INFO, result.getDescription());
				driver.navigate().refresh();
				continue;
			}
			
			
			StringBuilder error = new StringBuilder();
			
			HashMap<String, String> summary = objInbox.get_PendingWorkOrder(practice);
			
			if(summary.size() > 0) 
			{
				String expPaymentCount = summary.get("Payment");
				
				//get account count from Payment Inbox
				int actPaymentCount = objInbox.get_TotalAccounts_WorkOrderSummary();
				
				// verify
				if(actPaymentCount != Integer.parseInt(expPaymentCount))
				{
					utils.save_ScreenshotToReport("WOS_Payment");
					logger.log(LogStatus.FAIL, "<br>Account count in <b>PAYMENT RESPONSE</b> queue don't match, expected ["+ expPaymentCount +"] but found ["+ actPaymentCount +"]");
				}
			
				// log
				if(error.length() > 0)
					Configuration.logger.log(LogStatus.FAIL, error.toString());
				else
					logger.log(LogStatus.INFO, "<b>Work Order Summary verified for practice ["+ practice +"] </b>" );
			}
		}
		
 	}
	
	
	
	@Test(groups={"REG"})
 	public void verify_WorkOrderSummary_In_CodingInbox()
 	{	
		logger = extent.startTest("Verifying Work Order Summary in Coding role");
		
		/*int executableRowIndex = excel.isExecutable("verify_WorkOrderSummary_In_CodingInbox");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		String practice = excel.readValue(sheet, executableRowIndex, "practice");*/
		
		CodingInbox objInbox;
		
 		objLogin = PageFactory.initElements(driver, Login.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objInbox = PageFactory.initElements(driver, CodingInbox.class);
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.CODING);
		String sCodingUsername = login.get("ntlg");
		String sCodingPassword = login.get("password");	
		
		// login - Coding
		objLogin.login(sCodingUsername, sCodingPassword);
		
		
		
		ArrayList<String> lstPractice = objInbox.get_AllPractices();
		
		for (String practice : lstPractice) 
		{
			logger.log(LogStatus.INFO, "<b>For Practice [" + practice + "]</b>");
			
			Info result = objInbox.select_Practice(practice.substring(0, practice.indexOf("(")-1), false);
			if(!result.getStatus())  // if target not set
			{
				Configuration.logger.log(LogStatus.INFO, result.getDescription());
				driver.navigate().refresh();
				continue;
			}
				
			
			
			
			StringBuilder error = new StringBuilder();
			
			HashMap<String, String> summary = objInbox.get_PendingWorkOrder(practice);
			
			if(summary.size() > 0) 
			{
				String expCodingCount = summary.get("Coding");
				
				//get account count from Payment Inbox
				int actCodingCount = objInbox.get_TotalAccounts_WorkOrderSummary();
				
				// verify
				if(actCodingCount != Integer.parseInt(expCodingCount))
				{
					utils.save_ScreenshotToReport("WOS_Coding");
					logger.log(LogStatus.FAIL, "<br>Account count in <b>CODING RESPONSE</b> queue don't match, expected ["+ expCodingCount +"] but found ["+ actCodingCount +"]");
				}
			
				// log
				if(error.length() > 0)
					Configuration.logger.log(LogStatus.FAIL, error.toString());
				else
					logger.log(LogStatus.INFO, "<b>Work Order Summary verified for practice ["+ practice +"] </b>" );
			}			
		}
		
 	}
	
	
	@Test(groups={"REG"}, enabled = false)
 	public void verify_WorkOrderSummary_In_PrintMailInbox()
 	{	
		logger = extent.startTest("Verifying Work Order Summary in TL role");
		
		PrintAndMailInbox objInbox;
		
 		objLogin = PageFactory.initElements(driver, Login.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objInbox = PageFactory.initElements(driver, PrintAndMailInbox.class);
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.PRINT_AND_MAIL);
		String sPrintUsername = login.get("ntlg");
		String sPrintPassword = login.get("password");	
		
		// login - print and mail
		objLogin.login(sPrintUsername, sPrintPassword);
		
				
		ArrayList<String> lstPractice = objInbox.get_AllPractices();
		
		for (String practice : lstPractice) 
		{			
			logger.log(LogStatus.INFO, "<b>For Practice [" + practice + "]</b>");
			
			Info result = objInbox.select_Practice(practice, false);
			if(!result.getStatus())
				continue;
			
			
			
			
			StringBuilder error = new StringBuilder();
			
			HashMap<String, String> summary = objInbox.get_PendingWorkOrder(practice);
			
			if(summary.size() > 0) 
			{
				String expPrintMailCount = summary.get("PrintMail");
				
				//get account count from Payment Inbox
				int actPrintMailCount = objInbox.get_TotalAccounts_WorkOrderSummary();
				
				// verify
				if(actPrintMailCount != Integer.parseInt(expPrintMailCount))
					error.append("<br>Account count in <b>PRINT AND MAIL</b> queue don't match, expected ["+expPrintMailCount+"] but found ["+actPrintMailCount+"]");
			
				// log
				if(error.length() > 0)
					Configuration.logger.log(LogStatus.FAIL, error.toString());
			}
		}
		logger.log(LogStatus.INFO, "Work Order Summary verified");
 	}
	
}
