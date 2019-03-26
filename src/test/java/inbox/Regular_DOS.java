package inbox;

import java.util.HashMap;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.Constants;
import configuration.WebDriverUtils;
import pageObjectRepository.Login;
import pageObjectRepository.transaction.AnalystInbox;
import pageObjectRepository.transaction.ClaimTransaction;
import pageObjectRepository.Dashboard;

public class Regular_DOS extends Configuration
{
	WebDriverUtils utils = new WebDriverUtils();
	
	Login objLogin;
	Dashboard objDashboard;
	AnalystInbox objInbox;
	ClaimTransaction objClaimTransaction;
	
		
	@Test
	public void verify_Regular_DOS()
	{
		logger = extent.startTest("Verify accounts under different DOS categories in Regular queue");
		
		
		int executableRowIndex = excel.isExecutable("verify_Regular_DOS");		
		XSSFSheet sheet = excel.setSheet("TestData");		
		
		String practice = excel.readValue(sheet, executableRowIndex, "practice");		
		
		objLogin = PageFactory.initElements(driver, Login.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objInbox = PageFactory.initElements(driver, AnalystInbox.class);
		objClaimTransaction = PageFactory.initElements(driver, ClaimTransaction.class);
		
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");	

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		logger.log(LogStatus.INFO, "Navigate to Inbox");
		
		objInbox.select_Practice(practice);
		logger.log(LogStatus.INFO, "Select practice [" + practice + "]");
		
		objInbox.click_Image_Regular(true);
		logger.log(LogStatus.INFO, "Navigate to Regular queue");
		
		objInbox.click_Aging_Category_120();
		logger.log(LogStatus.INFO, "Click DOS Aging 120+");
		
		int accountCount = objInbox.get_TotalAccounts(Constants.Queues.REGULAR);		
		if(accountCount > 0)
		{
			accountCount = (accountCount > 2) ? 2 : accountCount;
			for(int i=1; i<=accountCount; i++)
			{
				String accountNumber = objInbox.get_AccountNumber(Constants.Queues.REGULAR, i);
				//Configuration.logger.log(LogStatus.INFO, "Account Number: " + accountNumber);
				
				if(objInbox.open_PatientAccount(Constants.Queues.REGULAR, i).getStatus())
				{
					objClaimTransaction.verify_DOS(accountNumber, "120+");
					objClaimTransaction.close_ClaimTransaction();
				}
				utils.wait_Until_InvisibilityOf_LoadingScreen();
				
				//new WebDriverWait(driver, 5).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='modal-backdrop fade']")));
			}
		}
		else
			Configuration.logger.log(LogStatus.INFO, "<b>No accounts in REGULAR queue under DOS 120+</b>");
	

		objInbox.click_Aging_Category_91_120();
		logger.log(LogStatus.INFO, "Click DOS Aging 91-120");
		
		int accountCount_91_120 = objInbox.get_TotalAccounts(Constants.Queues.REGULAR);
		if(accountCount_91_120 > 0)
		{
			accountCount_91_120 = (accountCount_91_120 > 2) ? 2 : accountCount_91_120;
			
			for(int i=1; i<=accountCount_91_120; i++)
			{			
				String accountNumber = objInbox.get_AccountNumber(Constants.Queues.REGULAR, i);
				//Configuration.logger.log(LogStatus.INFO, "Account Number: " + accountNumber);
				
				if(objInbox.open_PatientAccount(Constants.Queues.REGULAR, i).getStatus())
				{
					objClaimTransaction.verify_DOS(accountNumber, "91-120");
					objClaimTransaction.close_ClaimTransaction();
				}
				utils.wait_Until_InvisibilityOf_LoadingScreen();
			}			
		}
		else
			Configuration.logger.log(LogStatus.INFO, "<b>No accounts in REGULAR queue under DOS 91-120</b>");
		
	
		
		objInbox.click_Aging_Category_61_90();
		logger.log(LogStatus.INFO, "Click DOS Aging 61-90");
		
		int accountCount_61_90 = objInbox.get_TotalAccounts(Constants.Queues.REGULAR);
		
		if(accountCount_61_90 > 0)
		{
			accountCount_61_90 = (accountCount_61_90 > 2) ? 2 : accountCount_61_90;
			
			for(int i=1; i<=accountCount_61_90; i++)
			{			
				String accountNumber = objInbox.get_AccountNumber(Constants.Queues.REGULAR, i);
				//Configuration.logger.log(LogStatus.INFO, "Account Number: " + accountNumber);
				
				if(objInbox.open_PatientAccount(Constants.Queues.REGULAR, i).getStatus())
				{
					objClaimTransaction.verify_DOS(accountNumber, "61-90");
					objClaimTransaction.close_ClaimTransaction();
				}
				
				utils.wait_Until_InvisibilityOf_LoadingScreen();
			}			
		}
		else
			Configuration.logger.log(LogStatus.INFO, "<b>No accounts in REGULAR queue under DOS 61-90</b>");
		
	
	
		objInbox.click_Aging_Category_31_60();
		logger.log(LogStatus.INFO, "Click DOS Aging 31-60");
		
		int accountCount_31_60 = objInbox.get_TotalAccounts(Constants.Queues.REGULAR);
		
		if(accountCount_31_60 > 0)
		{
			accountCount_31_60 = (accountCount_31_60 > 2) ? 2 : accountCount_31_60;
			
			for(int i=1; i<=accountCount_31_60; i++)
			{
			
				String accountNumber = objInbox.get_AccountNumber(Constants.Queues.REGULAR, i);
				//Configuration.logger.log(LogStatus.INFO, "Account Number: " + accountNumber);
				
				if(objInbox.open_PatientAccount(Constants.Queues.REGULAR, i).getStatus())
				{
					objClaimTransaction.verify_DOS(accountNumber, "31-60");
				}
				objClaimTransaction.close_ClaimTransaction();
				utils.wait_Until_InvisibilityOf_LoadingScreen();
			}			
		}
		else
			Configuration.logger.log(LogStatus.INFO, "<b>No accounts in REGULAR queue under DOS 31-60</b>");
		
		
		objInbox.click_Aging_Category_0_30();
		logger.log(LogStatus.INFO, "Click DOS Aging 0-30");
		
		int accountCount_0_30 = objInbox.get_TotalAccounts(Constants.Queues.REGULAR);
		
		if(accountCount_0_30 > 0)
		{
			accountCount_0_30 = (accountCount_0_30 > 2) ? 2 : accountCount_0_30;
			
			for(int i=1; i<=accountCount_0_30; i++)
			{
				String accountNumber = objInbox.get_AccountNumber(Constants.Queues.REGULAR, i);
				//Configuration.logger.log(LogStatus.INFO, "Account Number: " + accountNumber);
				
				if(objInbox.open_PatientAccount(Constants.Queues.REGULAR, i).getStatus())
				{
					objClaimTransaction.verify_DOS(accountNumber, "0-30");
				}
				objClaimTransaction.close_ClaimTransaction();
				utils.wait_Until_InvisibilityOf_LoadingScreen();
			}			
		}
		else
			Configuration.logger.log(LogStatus.INFO, "<b>No accounts in REGULAR queue under DOS 0-30</b>");
		
	}
	
	
	
	
	public void check_Encounterlevel_InsBalance()
	{
		
		int executableRowIndex = excel.isExecutable("check_Encounterlevel_InsBalance");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		
		String practice = excel.readValue(sheet, executableRowIndex, "practice");		
		
		objLogin = PageFactory.initElements(driver, Login.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objInbox = PageFactory.initElements(driver, AnalystInbox.class);
		objClaimTransaction = PageFactory.initElements(driver, ClaimTransaction.class);
		
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");	

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		logger.log(LogStatus.INFO, "Navigate to Inbox");
		
		objInbox.select_Practice(practice);
		objInbox.click_Image_Regular(true);
		logger.log(LogStatus.INFO, "Select practice [" + practice +"] for Regular queue");
		
		
		int accountCount = objInbox.get_TotalAccounts(Constants.Queues.REGULAR);
		System.out.println("Total acc: "+  accountCount);
		
		float sum_Ins_Balance = 0;
		float ins_Balance = 0;
		
		SoftAssert s= new SoftAssert();
		
		for(int i=1; i<=accountCount; i++)
		{
		
			String accountNumber = driver.findElement(By.xpath("//table[@id = 'example12']/tbody/tr["+ i +"]/td/a")).getText();
			System.out.println("account: " + accountNumber);
			
			if(objInbox.open_PatientAccount(Constants.Queues.REGULAR, i).getStatus())
			{
				//sum_Ins_Balance = objClaimTransaction.get_Sum_Ins_Balance();
				objClaimTransaction.close_ClaimTransaction();
			}
			//utils.wait_Until_Invisibility_Of_LoadingScreen();
			//ins_Balance = objTransactionInbox.get_Pateient_InsBalace(accountNumber);

			s.assertEquals(sum_Ins_Balance, ins_Balance, "expected ["+ins_Balance+"] but found ["+sum_Ins_Balance+"] for Patient_Account ["+accountCount+"]");
			
			sum_Ins_Balance = 0;
			ins_Balance = 0;
		}
		
		s.assertAll();
	}

	

}
