package CreditBalance;

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
import customDataType.Transaction;
import pageObjectRepository.Login;
import pageObjectRepository.transaction.ClaimTransaction;
import pageObjectRepository.transaction.CreditBalanceInbox;
import pageObjectRepository.Dashboard;

public class TempSave extends Configuration
{
	
	WebDriverUtils utils = new WebDriverUtils();
	
	private Login objLogin;
	private CreditBalanceInbox objInbox;
	private ClaimTransaction objClaimTransaction;
	private Dashboard objDashboard;

	//String fromDate = new SimpleDateFormat("MM/dd/yy").format(new Date()); // -- MM/dd/yyyy

	
	@Test(enabled = true, groups = {"REG"})
	public void verify_TempSave_In_CreditBalance()
	{
		logger = extent.startTest("Verify TempSave in Credit Balance Role");
		
		int executableRowIndex = excel.isExecutable("verify_TempSave_In_CreditBalance");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		
		String practice = excel.readValue(sheet, executableRowIndex, "practice");		
		String nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue1");		
		HashMap<String, String> data = excel.get_TransactionData(nextQueue);
		
		String claimStatus = data.get("claim_status");
		String actionCode = data.get("action_code");
		String resolveType = data.get("resolve_type"); 
		String notes = data.get("note");
		
		
		objLogin = PageFactory.initElements(Configuration.driver, Login.class);
		objClaimTransaction = PageFactory.initElements(driver, ClaimTransaction.class);
		objInbox = PageFactory.initElements(driver, CreditBalanceInbox.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		


		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.CREDIT_BALANCE);
		String sCreditUsername = login.get("ntlg");
		String sCreditPassword = login.get("password");	
		
		// login - Credit Balance
		objLogin.login(sCreditUsername, sCreditPassword);
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		objInbox.select_Practice(practice, false);
		logger.log(LogStatus.INFO, "Navigate to Inbox and select practice ["+ practice +"]");		
		
		objInbox.click_Image_CreditBalance();
		logger.log(LogStatus.INFO, "Navigate to CREDIT BALANCE queue");
		
		
		// open account
		Info result = objInbox.open_PatientAccount(Constants.Queues.CREDIT_BALANCE);
		Assert.assertTrue(result.getStatus(), result.getDescription());
		
		String sAccountNumber = objClaimTransaction.get_PatientAccountNumber();	
		String sUID = objClaimTransaction.get_UID();
		
		// move account to temp save and assert values
		Info save = objClaimTransaction.tempSave(claimStatus, actionCode, resolveType, notes, false);
		Assert.assertTrue(save.getStatus(), save.getDescription() + "Account ["+sAccountNumber+"]");
		logger.log(LogStatus.INFO, "<b>Account moved to TempSave queue. Account Number ["+ sAccountNumber +"], UID ["+ sUID +"]</b>");	
		
					
		//navigate to temp save
		objInbox.click_Image_TempSave();
		logger.log(LogStatus.INFO, "Navigate to TempSave queue and search for account ["+ sAccountNumber +"]");
			
		//	verify presence of account and dropdown values
		Info search = objInbox.search_Account(sAccountNumber, Constants.Queues.TEMP_SAVE);
		Assert.assertTrue(search.getStatus(), search.getDescription());
		
		objInbox.open_PatientAccount(Constants.Queues.TEMP_SAVE, "ACCOUNT_NUMBER", sAccountNumber);
		objClaimTransaction.verify_TempSaveTransactionInputs(claimStatus, actionCode, resolveType, notes);
		logger.log(LogStatus.INFO, "<b>Account verified</b>");
	}	
	
}
