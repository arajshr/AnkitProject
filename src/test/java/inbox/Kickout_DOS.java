package inbox;

import java.util.HashMap;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.Constants;
import configuration.WebDriverUtils;
import pageObjectRepository.Dashboard;
import pageObjectRepository.Login;
import pageObjectRepository.transaction.AnalystInbox;
import pageObjectRepository.transaction.ClaimTransaction;

public class Kickout_DOS extends Configuration
{

	WebDriverUtils utils = new WebDriverUtils();
	
	Login objLogin;
	Dashboard objDashboard;
	AnalystInbox objTransactionInbox;
	ClaimTransaction objClaimTransaction;

	@Test(enabled = true, groups = {"REG"})
	public void verify_KickOut_DOS()
	{
		logger = extent.startTest("Verify accounts under different DOS categories in Kickout queue");
		
		int executableRowIndex = excel.isExecutable("verify_KickOut_DOS");		
		XSSFSheet sheet = excel.setSheet("TestData");		
		
		String practice = excel.readValue(sheet, executableRowIndex, "practice");
		
		objLogin = PageFactory.initElements(driver, Login.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objTransactionInbox = PageFactory.initElements(driver, AnalystInbox.class);
		objClaimTransaction = PageFactory.initElements(driver, ClaimTransaction.class);
		
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");	

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		logger.log(LogStatus.INFO, "Navigate to Inbox");
		
		objTransactionInbox.select_Practice(practice);
		logger.log(LogStatus.INFO, "Select practice [" + practice + "]");
		
		objTransactionInbox.click_Image_KickOut();
		logger.log(LogStatus.INFO, "Navigate to KICKOUT queue");
		
		objTransactionInbox.click_Aging_Category_120();
		logger.log(LogStatus.INFO, "Click DOS Aging 120+");
		
		int accountCount = objTransactionInbox.get_TotalAccounts(Constants.Queues.KICKOUT);		
		if(accountCount > 0)
		{
			accountCount = (accountCount > 2) ? 2 : accountCount;
			for(int i=1; i<=accountCount; i++)
			{
				String accountNumber = objTransactionInbox.get_AccountNumber(Constants.Queues.KICKOUT, i);
				//Configuration.logger.log(LogStatus.INFO, "Account Number: " + accountNumber);
				
				if(objTransactionInbox.open_PatientAccount(Constants.Queues.KICKOUT, i).getStatus())
				{
					objClaimTransaction.verify_DOS(accountNumber, "120+");
					objClaimTransaction.close_ClaimTransaction();
				}
				utils.wait_Until_InvisibilityOf_LoadingScreen();
				
				//new WebDriverWait(driver, 5).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='modal-backdrop fade']")));
			}
		}
		else
			Configuration.logger.log(LogStatus.INFO, "<b>No accounts in KICKOUT queue under DOS 120+</b>");
	

		objTransactionInbox.click_Aging_Category_91_120();
		logger.log(LogStatus.INFO, "Click DOS Aging 91-120");
		
		int accountCount_91_120 = objTransactionInbox.get_TotalAccounts(Constants.Queues.KICKOUT);
		if(accountCount_91_120 > 0)
		{
			accountCount_91_120 = (accountCount_91_120 > 2) ? 2 : accountCount_91_120;
			for(int i=1; i<=accountCount; i++)
			{
			
				String accountNumber = objTransactionInbox.get_AccountNumber(Constants.Queues.KICKOUT, i);
				//Configuration.logger.log(LogStatus.INFO, "Account Number: " + accountNumber);
				
				if(objTransactionInbox.open_PatientAccount(Constants.Queues.KICKOUT, i).getStatus())
				{
					objClaimTransaction.verify_DOS(accountNumber, "91-120");
					objClaimTransaction.close_ClaimTransaction();
				}
				utils.wait_Until_InvisibilityOf_LoadingScreen();
			}			
		}
		else
			Configuration.logger.log(LogStatus.INFO, "<b>No accounts in KICKOUT queue under DOS 91-120</b>");
		
	
		
		objTransactionInbox.click_Aging_Category_61_90();
		logger.log(LogStatus.INFO, "Click DOS Aging 61-90");
		
		int accountCount_61_90 = objTransactionInbox.get_TotalAccounts(Constants.Queues.KICKOUT);
		
		if(accountCount_61_90 > 0)
		{
			accountCount_61_90 = (accountCount_61_90 > 2) ? 2 : accountCount_61_90;
			for(int i=1; i<=accountCount; i++)
			{
			
				String accountNumber = objTransactionInbox.get_AccountNumber(Constants.Queues.KICKOUT, i);
				//Configuration.logger.log(LogStatus.INFO, "Account Number: " + accountNumber);
				
				if(objTransactionInbox.open_PatientAccount(Constants.Queues.KICKOUT, i).getStatus())
				{
					objClaimTransaction.verify_DOS(accountNumber, "61-90");
					objClaimTransaction.close_ClaimTransaction();
				}
				
				utils.wait_Until_InvisibilityOf_LoadingScreen();
			}			
		}
		else
			Configuration.logger.log(LogStatus.INFO, "<b>No accounts in KICKOUT queue under DOS 61-90</b>");
		
	
	
		objTransactionInbox.click_Aging_Category_31_60();
		logger.log(LogStatus.INFO, "Click DOS Aging 31-60");
		
		int accountCount_31_60 = objTransactionInbox.get_TotalAccounts(Constants.Queues.KICKOUT);
		
		if(accountCount_31_60 > 0)
		{
			accountCount_31_60 = (accountCount_31_60 > 2) ? 2 : accountCount_31_60;
			for(int i=1; i<=accountCount; i++)
			{
			
				String accountNumber = objTransactionInbox.get_AccountNumber(Constants.Queues.KICKOUT, i);
				//Configuration.logger.log(LogStatus.INFO, "Account Number: " + accountNumber);
				
				if(objTransactionInbox.open_PatientAccount(Constants.Queues.KICKOUT, i).getStatus())
				{
					objClaimTransaction.verify_DOS(accountNumber, "31-60");
				}
				objClaimTransaction.close_ClaimTransaction();
				utils.wait_Until_InvisibilityOf_LoadingScreen();
			}			
		}
		else
			Configuration.logger.log(LogStatus.INFO, "<b>No accounts in KICKOUT queue under DOS 31-60</b>");
		
		
		objTransactionInbox.click_Aging_Category_0_30();
		logger.log(LogStatus.INFO, "Click DOS Aging 0-30");
		
		int accountCount_0_30 = objTransactionInbox.get_TotalAccounts(Constants.Queues.KICKOUT);
		
		if(accountCount_0_30 > 0)
		{
			accountCount_0_30 = (accountCount_0_30 > 2) ? 2 : accountCount_0_30;
			for(int i=1; i<=accountCount; i++)
			{
			
				String accountNumber = objTransactionInbox.get_AccountNumber(Constants.Queues.KICKOUT, i);
				//Configuration.logger.log(LogStatus.INFO, "Account Number: " + accountNumber);
				
				if(objTransactionInbox.open_PatientAccount(Constants.Queues.KICKOUT, i).getStatus())
				{
					objClaimTransaction.verify_DOS(accountNumber, "0-30");
				}
				objClaimTransaction.close_ClaimTransaction();
				utils.wait_Until_InvisibilityOf_LoadingScreen();
			}			
		}
		else
			Configuration.logger.log(LogStatus.INFO, "<b>No accounts in KICKOUT queue under DOS 0-30</b>");
	}
}
