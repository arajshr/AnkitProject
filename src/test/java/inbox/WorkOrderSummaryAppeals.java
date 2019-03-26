package inbox;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.Constants;
import configuration.WebDriverUtils;
import pageObjectRepository.Dashboard;
import pageObjectRepository.Login;
import pageObjectRepository.transaction.AppealsInbox;

public class WorkOrderSummaryAppeals extends Configuration
{
	WebDriverUtils utils = new WebDriverUtils();
	
	Login objLogin;
	Dashboard objDashboard;
	AppealsInbox objInbox;
	
	@Test(groups={"REG"})
 	public void verify_WorkOrderSummary_In_Appeals_Role()
 	{	
		logger = extent.startTest("Verifying Work Order Summary in Appeals role");
		
		int executableRowIndex = excel.isExecutable("verify_WorkOrderSummary_In_Appeals_Role");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		String practice = excel.readValue(sheet, executableRowIndex, "practice");
		
 		objLogin = PageFactory.initElements(driver, Login.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objInbox = PageFactory.initElements(driver, AppealsInbox.class);
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.APPEALS);
		String sAppealsUsername = login.get("ntlg");
		String sAppealsPassword = login.get("password");	
		
		// login - Credit Balance
		objLogin.login(sAppealsUsername, sAppealsPassword);
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		logger.log(LogStatus.INFO, "Navigate to Inbox");
		
		//String practice = "WES";
		
		/*ArrayList<String> lstPractice = objInbox.get_AllPractices();		
		for (String practice : lstPractice) 
		{*/
			objInbox.select_Practice(practice, false);
			logger.log(LogStatus.INFO, "<b>For Practice [" + practice + "]</b>");
			
			
			StringBuilder error = new StringBuilder();			
			verify_Summary(error, practice);			
			if(error.length() > 0)
				Configuration.logger.log(LogStatus.FAIL, error.toString());
			
		/*}*/
		
		logger.log(LogStatus.INFO, "Work Order Summary verified");
 	}
	

 	
 	StringBuilder verify_Summary(StringBuilder error, String sPractice)
 	{ 		
 		HashMap<String, String> summary = objInbox.get_PendingWorkOrder(sPractice);
		
		for (Map.Entry<String, String> row : summary.entrySet()) 
		{					
			try
			{
				if((row.getKey() != null) && (row.getKey().equals("TempSave"))) //check for TempSave 
				{
					String expTempSaveCount = row.getValue();
					
					//get account count from tempSave
					objInbox.click_Image_TempSave();
					int actTempSaveCount = objInbox.get_TotalAccounts_WorkOrderSummary(Constants.Queues.TEMP_SAVE);
					
					if(actTempSaveCount != Integer.parseInt(expTempSaveCount))
					{
						utils.save_ScreenshotToReport("WOS_TempSave");
						logger.log(LogStatus.FAIL, "<br>Account count in <b>TEMP SAVE</b> queue don't match, expected ["+ expTempSaveCount +"] but found ["+ actTempSaveCount +"]");
					}
				}
				
				else if((row.getKey() != null) && (row.getKey().equals("Clarification"))) //check for Clarification 
				{
					String expCLRCount = row.getValue();
					
					//get account count from Clarification 
					objInbox.click_Image_Clarification();				
					int actCLRCount = objInbox.get_TotalAccounts_WorkOrderSummary(Constants.Queues.CLARIFICATION);
					
					if(actCLRCount != Integer.parseInt(expCLRCount))
					{
						utils.save_ScreenshotToReport("WOS_Clarification");
						logger.log(LogStatus.FAIL, "<br>Account count in <b>CLARIFICATION</b> queue don't match, expected ["+ expCLRCount +"] but found ["+ actCLRCount +"]");
					}
				}
				
				else if ((row.getKey() != null) && (row.getKey().equals("ClientEscalation Release"))) //check for Client Escalation Release 
				{
					String expCLRSPRLSECount = row.getValue();
					
					//get account count from Client Escalation Release 
					objInbox.click_Image_ClientEscalationResponse();		
					int actCLRSPRLSECount = objInbox.get_TotalAccounts_WorkOrderSummary(Constants.Queues.CLIENT_ESCALATION_RELEASE);
					
					if(actCLRSPRLSECount != Integer.parseInt(expCLRSPRLSECount))
					{
						utils.save_ScreenshotToReport("WOS_Escalation");
						logger.log(LogStatus.FAIL, "<br>Account count in <b>CLIENT ESCALATION RELEASE</b> queue don't match, expected ["+ expCLRSPRLSECount +"] but found ["+ actCLRSPRLSECount +"]");
					}
				}
				
				else if ((row.getKey() != null) && (row.getKey().equals("PrintMail Response"))) //check for PrintMail Response 
				{
					logger.log(LogStatus.FAIL, "<br><b>PRINT AND MAIL RESPONSE queue doesn't exists</b>");
					
					/*String expPRNTML_ANLCount = row.getValue();
					
					//get account count from Printmail Response
					objInbox.click_Image_PrintAndMailResponse();		
					int actPRNTML_ANLCount = objInbox.get_TotalAccounts_WorkOrderSummary(Constants.Queues.PRINT_AND_MAIL_RESPONSE);
					
					if(actPRNTML_ANLCount != Integer.parseInt(expPRNTML_ANLCount))
						error.append("<br>Account count in <b>PRINT AND MAIL RESPONSE</b> queue don't match, expected ["+actPRNTML_ANLCount+"] but found ["+expPRNTML_ANLCount+"]");*/
				}
				
				else if ((row.getKey() != null) && (row.getKey().equals("Coding Response"))) //check for Coding Response 
				{
					String expCDGResponseCount = row.getValue();
					
					//get account count from coding response
					objInbox.click_Image_CodingResponse();			
					int actCDGResponseCount = objInbox.get_TotalAccounts_WorkOrderSummary(Constants.Queues.CODING_RESPONSE);
					
					if(actCDGResponseCount != Integer.parseInt(expCDGResponseCount))
					{
						utils.save_ScreenshotToReport("WOS_Coding");
						logger.log(LogStatus.FAIL, "<br>Account count in <b>CODING RESPONSE</b> queue don't match, expected ["+ expCDGResponseCount +"] but found ["+ actCDGResponseCount +"]");
					}
				}				
				
				else if ((row.getKey() != null) && (row.getKey().equals("Payment Response"))) //check for Payment Response 
				{
					String expPYMNTResponseCount = row.getValue();
					
					//get account count from payment response
					objInbox.click_Image_PaymentResponse();			
					int actPYMNTResponseCount = objInbox.get_TotalAccounts_WorkOrderSummary(Constants.Queues.PAYMENT_RESPONSE);
					
					if(actPYMNTResponseCount != Integer.parseInt(expPYMNTResponseCount))
					{
						utils.save_ScreenshotToReport("WOS_Payment");
						logger.log(LogStatus.FAIL, "<br>Account count in <b>PAYMENT RESPONSE</b> queue don't match, expected ["+ expPYMNTResponseCount +"] but found ["+ actPYMNTResponseCount +"]");
					}
				}
				
				else if((row.getKey() != null) && (row.getKey().equals("Appeals"))) //check for Appeals 
				{
					String expAppealsCount = row.getValue();
					
					//get account count from Appeals
					objInbox.click_Image_Appeals();
					int actAppealsCount = objInbox.get_TotalAccounts_WorkOrderSummary(Constants.Queues.APPEALS);
					
					if(actAppealsCount != Integer.parseInt(expAppealsCount))
					{
						utils.save_ScreenshotToReport("WOS_Appeals");
						logger.log(LogStatus.FAIL, "<br>Account count in <b>APPEALS</b> queue don't match, expected ["+expAppealsCount+"] but found ["+actAppealsCount+"]");
					}
				}
				
				else
				{
					System.out.println("Can't find ["+ row.getKey() +"] queue");
				}
				
			}
			catch (Exception e) 
			{
				System.out.println(e.getMessage());
			}
		}
		return error;
 	}
 	
}
