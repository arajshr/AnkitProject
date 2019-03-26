package TempSave;

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
import pageObjectRepository.transaction.AnalystInbox;
import pageObjectRepository.transaction.ClaimTransaction;
import pageObjectRepository.transaction.PaymentInbox;
import pageObjectRepository.Dashboard;

public class TempSave extends Configuration
{
	
	WebDriverUtils utils = new WebDriverUtils();
	
	private Login objLogin;
	private AnalystInbox objInbox;
	private ClaimTransaction objClaimTransaction;
	private Dashboard objDashboard;
	private PaymentInbox objPaymentInbox;
	
	@Test(groups = {"REG"}, priority = 1, enabled = true)
	public void verify_TempSave()
	{
		logger = extent.startTest("Verify Temp Save for Regular accounts");
		
		int executableRowIndex = excel.isExecutable("verify_TempSave");		
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
		objInbox = PageFactory.initElements(driver, AnalystInbox.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		


		/************************ SUBMIT ACCOUNT TO TEMP SAVE **************************/
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");	

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
		
		String workedBy = objDashboard.getWorkedBy().getAttribute("textContent");
		
		logger.log(LogStatus.INFO, "Navigate to Inbox and select practice ["+ practice +"]");
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		objInbox.select_Practice(practice);
		
		objInbox.click_Image_Regular(true);
		logger.log(LogStatus.INFO, "Navigate to REGULAR queue");
		
		
		String sAccountNumber = objInbox.get_AccountNumber(Constants.Queues.REGULAR, 1);
		HashMap<String, String> details = objInbox.getAccountDetailsFromRegularTable(sAccountNumber);
		
		
		
		objInbox.open_PatientAccount(Constants.Queues.REGULAR);
		//String sAccountNumber = objClaimTransaction.get_PatientAccountNumber();
		String sEncounterID = objClaimTransaction.get_EncounterID();		
		String sUID = objClaimTransaction.get_UID();		
		String sDOS = objClaimTransaction.get_DOSStart();
		
		// move account to temp save and assert values
		Info save = objClaimTransaction.tempSave(claimStatus, actionCode, resolveType, notes, false);
		Assert.assertTrue(save.getStatus(), save.getDescription() + "Account ["+sAccountNumber+"]");
		logger.log(LogStatus.INFO, "<b>Account moved to Temp Save queue. Account Number ["+ sAccountNumber +"], UID ["+ sUID +"]</b>");
		
		
		/*String sAccountNumber = "20190117-106";
		HashMap<String, String> details = new HashMap<>();
		details.put("PatientName", "SMITH, SUSAN");
		details.put("DOB", "02/14/2017");
		details.put("InsBalance", "$404.00");
		details.put("PolicyID", "39952126211");
		details.put("Insurance", "BLUE CROSS MEDICARE ADVAN");
		
		
		String sEncounterID = "20190117-106";*/
		
		
		
		/************************ VERRIFY ACCOUNT, TRANSACTION INPUTS IN TEMP SAVE QUEUE **************************/
		
		//navigate to temp save
		objInbox.click_Image_TempSave(false);
		logger.log(LogStatus.INFO, "Navigate to Temp Save queue and search for account ["+ sAccountNumber +"]");
			
		//	verify presence of account and dropdown values
		Info search = objInbox.search_TempSave(sAccountNumber, sEncounterID, claimStatus, actionCode, resolveType, notes, sDOS, details);
		Assert.assertTrue(search.getStatus(), search.getDescription());
		logger.log(LogStatus.INFO, search.getDescription());
		
		
		
		//open account in temp save
		objInbox.open_PatientAccount(Constants.Queues.TEMP_SAVE, "ACCOUNT_NUMBER", sAccountNumber);
		
		// verify tempSave history
		objClaimTransaction.verify_TempSaveHistory("Regular", workedBy, actionCode, claimStatus, notes);
		
		
		// verify transaction inputs
		objClaimTransaction.verify_TempSaveTransactionInputs(claimStatus, actionCode, resolveType, notes);
		
		
		
		
		
		
		/************************ TEMP SAVE SAME ACCOUNT IN TEMP SAVE QUEUE **************************/
		
		
		nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue2");		
		data = excel.get_TransactionData(nextQueue);
		
		claimStatus = data.get("claim_status");
		actionCode = data.get("action_code");
		resolveType = data.get("resolve_type"); 
		notes = data.get("note");
		
		
		
		// move account to temp save and assert values
		Info save1 = objClaimTransaction.tempSave(claimStatus, actionCode, resolveType, notes, false);
		Assert.assertTrue(save1.getStatus(), save1.getDescription() + "Account ["+sAccountNumber+"]");
		logger.log(LogStatus.INFO, "<b>Encounter temp saved in Temp Save queue</b>");
		
		
		
		
		
		//	verify updated action codes in temp save queue
		Info search1 = objInbox.search_TempSave(sAccountNumber, sEncounterID, claimStatus, actionCode, resolveType, notes, sDOS, details);
		Assert.assertTrue(search1.getStatus(), search1.getDescription());
		
		
						
	}	
	
	
	@Test(groups = {"REG"})
	public void verify_TempSave_InlineEdit()
	{
		logger = extent.startTest("Verify inline edit option in Temp Save queue");
		
		int executableRowIndex = excel.isExecutable("verify_TempSave");		
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
		objInbox = PageFactory.initElements(driver, AnalystInbox.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objPaymentInbox = PageFactory.initElements(driver, PaymentInbox.class);


		/************************ SUBMIT ACCOUNT TO TEMP SAVE **************************/
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");	

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
		
		String workedBy = objDashboard.getWorkedBy().getAttribute("textContent");
		
		logger.log(LogStatus.INFO, "Navigate to Inbox and select practice ["+ practice +"]");
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		objInbox.select_Practice(practice);
		
		objInbox.click_Image_Regular(true);
		logger.log(LogStatus.INFO, "Navigate to REGULAR queue");
		
		
		String sAccountNumber = objInbox.get_AccountNumber(Constants.Queues.REGULAR, 1);
		HashMap<String, String> details = objInbox.getAccountDetailsFromRegularTable(sAccountNumber);
		
		Assert.assertNotEquals(sAccountNumber, "", "<b>Patient account number is empty</b>");
		
		objInbox.open_PatientAccount(Constants.Queues.REGULAR, "ACCOUNT_NUMBER", sAccountNumber);
		String sEncounterID = objClaimTransaction.get_EncounterID();		
		String sUID = objClaimTransaction.get_UID();
		String sDOS = objClaimTransaction.get_DOSStart();
		
		// move account to temp save and assert values
		Info save = objClaimTransaction.tempSave(claimStatus, actionCode, resolveType, notes, false);
		Assert.assertTrue(save.getStatus(), save.getDescription() + "Account ["+sAccountNumber+"]");
		logger.log(LogStatus.INFO, "<b>Account moved to Temp Save queue, "
				+ "Account [" + sAccountNumber + "], Encounter ID ["+ sEncounterID +"]UID ["+ sUID +"]<br> <br>"

				+ "ClaimStatus ["+claimStatus+"], <br>"
				+ "ActionCode ["+actionCode+"], <br>"
				+ "ResolveType ["+resolveType+"], <br>"
				+ "Notes ["+notes+"]</b>");
	
		
		/************************ VERRIFY ACCOUNT, TRANSACTION INPUTS IN TEMP SAVE QUEUE **************************/
		
		//navigate to temp save
		objInbox.click_Image_TempSave(false);
		logger.log(LogStatus.INFO, "Navigate to Temp Save queue and search for account ["+ sAccountNumber +"]");
			
		//	verify presence of account and dropdown values
		Info search = objInbox.search_TempSave(sAccountNumber, sEncounterID, claimStatus, actionCode, resolveType, notes, sDOS, details);
		Assert.assertTrue(search.getStatus(), search.getDescription());
		logger.log(LogStatus.INFO, search.getDescription());
		
		
		
		/************************ EDIT AND CANCEL **************************/
		
		nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue2");	
		data = excel.get_TransactionData(nextQueue);
		
		String claimStatus2 = data.get("claim_status");
		String actionCode2 = data.get("action_code");
		String resolveType2 = data.get("resolve_type"); 
		String notes2 = data.get("note");
		
		// Edit and cancel 		
		objInbox.edit_InLineEdit_TempSaveTransactionInputs(sAccountNumber, sEncounterID, claimStatus2, actionCode2, resolveType2, notes2); // new values
		objInbox.cancel_InLineEdit(sAccountNumber, sEncounterID);
		
		
		//	verify presence of account and dropdown values
		Info search1 = objInbox.search_TempSave(sAccountNumber, sEncounterID, claimStatus, actionCode, resolveType, notes, sDOS, details); // verify for old values
		Assert.assertTrue(search1.getStatus(), search1.getDescription());	
		
		
		
		/************************ EDIT AND SUBMIT **************************/
		
		// Edit and submit
		objInbox.edit_InLineEdit_TempSaveTransactionInputs(sAccountNumber, sEncounterID, claimStatus2, actionCode2, resolveType2, notes2); // new values
		Info resSave = objInbox.save_InLineEdit(sAccountNumber, sEncounterID);
		Assert.assertTrue(resSave.getStatus(), resSave.getDescription());
		logger.log(LogStatus.INFO, "<b>Transaction inputs [claimstatus, action code, resolvetype, notes] are updated and saved through inline edit option</b>");
		
		objDashboard.signOut();
		
		
		/************************ VERIFY IN NEXT QUEUE **************************/
		
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.PAYMENT);
		String sPaymentUsername = login.get("ntlg");
		String sPaymentPassword = login.get("password");	
		
		// login - Payment
		objLogin.login(sPaymentUsername, sPaymentPassword);
		
		// Navigate to payment team inbox	
		objDashboard.navigateTo_Transaction().navigateTo_Payment_Inbox();
		
		// select practice	
		objPaymentInbox.select_Practice(practice, false); 
		logger.log(LogStatus.INFO, "Navigate to Payment Inbox and Select practice [" + practice+ "]");
		
		// verify account							
		Info search2 = objPaymentInbox.search_UID(sUID);
		Assert.assertTrue(search2.getStatus(), search2.getDescription());
		logger.log(LogStatus.INFO, search.getDescription());
	}

	
	
	public void verify_Clarification_Credentialing_TempSave()
	{
		
	}

}
