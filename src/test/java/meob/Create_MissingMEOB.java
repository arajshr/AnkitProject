package meob;

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
import pageObjectRepository.transaction.MissisngEOBS;


public class Create_MissingMEOB extends Configuration
{	
	WebDriverUtils utils=new WebDriverUtils();
	
	Login objLogin;
	Dashboard objDashboard;
	MissisngEOBS  objMissisngEOBS;
	
	
	@Test
	public void Create_MEOB() 
	{
		logger = extent.startTest("Create MEOB inventory and verify Junior Caller MEOB inbox");
		
		int executableIndex=excel.isExecutable("Create_MEOB");
		XSSFSheet sheet=excel.setSheet("TestData");
		
		String practice=excel.readValue(sheet, executableIndex, "practice");
		String testId=excel.readValue(sheet, executableIndex, "meob");
		HashMap<String, String> data=excel.get_MEOBData(testId);
		
		
		/*String PaymentDate=data.get("Payment_Date");
		String DepositDate=data.get("Deposit_Date");*/
		String sPayerName = data.get("Payer_Name");
		String sModeOfPayment = data.get("Mode_Payment");
		String sEFTCheck = data.get("EFT/Check");
		String sTotalPaidAmount = data.get("Total_Paid");
		String sPendingAmount = data.get("Pending_Amount");
		String sFileName = data.get("File_Name");
		String sFileLocation = data.get("File_Location");
		String sRemarks = data.get("Remarks");
		
		
		objLogin=PageFactory.initElements(driver, Login.class);
		objDashboard=PageFactory.initElements(driver, Dashboard.class);
		objMissisngEOBS=PageFactory.initElements(driver, MissisngEOBS.class);
		
		
		// login as payment to create MEOB		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.PAYMENT);
		String sPaymentUsername = login.get("ntlg");
		String sPaymentPassword = login.get("password");	
		
		// login - Payment
		objLogin.login(sPaymentUsername, sPaymentPassword);
					
		objDashboard.navigateTo_Transaction().navigateTo_MissingEOB();
		logger.log(LogStatus.INFO, "Navigate to Missing EOB and select practice ["+ practice+"]");
		
		objMissisngEOBS.select_Practice(practice,false);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		
		
		//Create MEOB		
		objMissisngEOBS.set_PayerName(sPayerName)
						.set_ModeofPayment(sModeOfPayment)
						//.set_DepositDate(DepositDate)
						//.set_PaymentDate(PaymentDate)
						.set_EFTCheck(sEFTCheck)
						.set_TotalPaidAmount(sTotalPaidAmount)
						.set_PendingAmounttobePosted(sPendingAmount)
						.set_FileNameoftheCheckcopy(sFileName)
						.set_FileLocationoftheCheckcopy(sFileLocation)
						.set_Remarks(sRemarks);
				
				
		logger.log(LogStatus.INFO, "Set values as <br> Payer Name ["+ sPayerName +"], Mode Of Payment ["+ sModeOfPayment +"], EFT Check ["+ sEFTCheck +"], Total Paid Amount ["+ sTotalPaidAmount +"], "
								+ "Pending AmountTo Be Posted ["+ sPendingAmount +"], File Name Of Check Copy ["+ sFileName +"], File Location Of Check Copy ["+ sFileLocation +"], "
								+ "Remarks ["+ sRemarks +"]");		
				
		Info createMEOB = objMissisngEOBS.add_meob();
		Assert.assertTrue(createMEOB.getStatus(), createMEOB.getDescription());
		logger.log(LogStatus.INFO, "<b>MEOB created</b>");
	}
	
	
}
