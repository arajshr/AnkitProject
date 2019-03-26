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
import pageObjectRepository.transaction.AnalystInbox;

public class WorkOrderSummary extends Configuration
{
	WebDriverUtils utils = new WebDriverUtils();
	
	Login objLogin;
	Dashboard objDashboard;
	AnalystInbox objInbox;
	

	
 	@Test(groups={"REG"}, priority = 1, enabled = true)
 	public void verify_WorkOrderSummary_In_Analyst_Role()
 	{	
 		
		logger = extent.startTest("Verifying Work Order Summary in Analyst role");
		
		int executableRowIndex = excel.isExecutable("verify_WorkOrderSummary_In_Analyst_Role");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		String practice = excel.readValue(sheet, executableRowIndex, "practice");
		
 		
 		objLogin = PageFactory.initElements(driver, Login.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objInbox = PageFactory.initElements(driver, AnalystInbox.class);
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");	

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);
		
		// naviagte to inbox
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		logger.log(LogStatus.INFO, "Navigate to Inbox");
		
		
		objInbox.select_Practice(practice, false);
		logger.log(LogStatus.INFO, "<b>For Practice [" + practice + "]</b>");
		
			
		verify_Summary();		
		logger.log(LogStatus.INFO, "Work Order Summary verified");
 	}
 	
 	@Test(groups={"REG"}, priority = 2, enabled = true)
 	public void verify_WorkOrderSummary_In_TL_Role()
 	{	
 		logger = extent.startTest("Verifying Work Order Summary in TL role");
 		
 		int executableRowIndex = excel.isExecutable("verify_WorkOrderSummary_In_TL_Role");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		String practice = excel.readValue(sheet, executableRowIndex, "practice");
		
 		
 		objLogin = PageFactory.initElements(driver, Login.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objInbox = PageFactory.initElements(driver, AnalystInbox.class);
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.TL);
		String sTLUsername = login.get("ntlg");
		String sTLPassword = login.get("password");	

		// login - TL
		objLogin.login(sTLUsername, sTLPassword);		
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		logger.log(LogStatus.INFO, "Navigate to Inbox");
		
		objInbox.select_Practice(practice, false);
		logger.log(LogStatus.INFO, "For Practice [" + practice + "]");
				
		verify_Summary();	
		
		logger.log(LogStatus.INFO, "Work Order Summary verified");
 	}

 	@Test(groups={"REG"}, priority = 3, enabled = true)
 	public void verify_WorkOrderSummary_In_Manager_Role()
 	{	
 		logger = extent.startTest("Verifying Work Order Summary in Manager role");
 		
 		int executableRowIndex = excel.isExecutable("verify_WorkOrderSummary_In_Manager_Role");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		String practice = excel.readValue(sheet, executableRowIndex, "practice");
		
 		
 		objLogin = PageFactory.initElements(driver, Login.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objInbox = PageFactory.initElements(driver, AnalystInbox.class);
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.MANAGER);
		String sMngrUsername = login.get("ntlg");
		String sMngrPassword = login.get("password");	

		// login - Manager
		objLogin.login(sMngrUsername, sMngrPassword);
		
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		logger.log(LogStatus.INFO, "Navigate to Inbox");
		
		objInbox.select_Practice(practice, false);
		logger.log(LogStatus.INFO, "For Practice [" + practice + "]");
			
		verify_Summary();	
		
		logger.log(LogStatus.INFO, "Work Order Summary verified");
 	}
 	
 	
 	
 	void verify_Summary()
 	{ 		
 		HashMap<String, String> summary = objInbox.get_PendingWorkOrder();
		System.out.println(summary);
		
		for (Map.Entry<String, String> row : summary.entrySet()) 
		{					
			try
			{
				System.out.println(row.getKey());
				
				new WebDriverUtils().wait(1000);
				if((row.getKey() != null) && (row.getKey().equals("Priority"))) 
				{
					String expPriorityCount = row.getValue(); //check Priority value
					
					//get account count from priority
					objInbox.click_Image_Priority(false);				
					int actPriorityCount = objInbox.get_TotalAccounts_WorkOrderSummary(Constants.Queues.PRIORITY);
					
					if(actPriorityCount != Integer.parseInt(expPriorityCount))
					{
						utils.save_ScreenshotToReport("WOS_Priority");
						logger.log(LogStatus.FAIL, "<br>Account count in <b>PRIORITY</b> queue don't match, expected ["+ expPriorityCount +"] but found ["+ actPriorityCount +"]");
					}
						
				}
				
				else if((row.getKey() != null) && (row.getKey().equals("Regular"))) 
				{
					String expRegularCount = row.getValue(); //check Regular value
					
					//get account count from regular
					objInbox.click_Image_Regular(false);				
					int actRegularCount = objInbox.get_TotalAccounts_WorkOrderSummary(Constants.Queues.REGULAR);
					
					if(actRegularCount != Integer.parseInt(expRegularCount)) 
					{
						utils.save_ScreenshotToReport("WOS_Regular");
						logger.log(LogStatus.FAIL, "<br>Account count in <b>REGLAR</b> queue don't match, expected ["+ expRegularCount +"] but found ["+ actRegularCount +"]");
					}
				}
				
				else if((row.getKey() != null) && (row.getKey().equals("Jr Caller Response"))) 
				{
					String expJrCallerCount = row.getValue(); //check Jr Caller value
					
					//get account count from JrCaller
					objInbox.click_Image_JrCallerResponse();			
					int actJrCallerCount = objInbox.get_TotalAccounts_WorkOrderSummary(Constants.Queues.NEEDCALL_RESPONSE);
					
					if(actJrCallerCount != Integer.parseInt(expJrCallerCount))
					{
						utils.save_ScreenshotToReport("WOS_JrCaller");
						logger.log(LogStatus.FAIL, "<br>Account count in <b>Junior Caller Response</b> queue don't match, expected ["+ expJrCallerCount +"] but found ["+ actJrCallerCount +"]");
					}
				}
				
				else if((row.getKey() != null) && (row.getKey().equals("Denial"))) 
				{
					String expDenialCount = row.getValue(); //check Denial value
					
					//get account count from denial
					objInbox.click_Image_Denail(false);				
					int actDenialCount = objInbox.get_TotalAccounts_WorkOrderSummary(Constants.Queues.DENIAL);
					
					if(actDenialCount != Integer.parseInt(expDenialCount))
					{
						utils.save_ScreenshotToReport("WOS_Denial");
						logger.log(LogStatus.FAIL, "<br>Account count in <b>DENIAL</b> queue don't match, expected ["+ expDenialCount +"] but found ["+ actDenialCount +"]");
					}
				}
				
				else if((row.getKey() != null) && (row.getKey().equals("Kickout"))) //check for Kickout 
				{
					String expKCKCount = row.getValue();
					
					//get account count from Kickout
					objInbox.click_Image_KickOut();				
					int actKCKCount = objInbox.get_TotalAccounts_WorkOrderSummary(Constants.Queues.KICKOUT);
					
					if(actKCKCount != Integer.parseInt(expKCKCount))
					{
						utils.save_ScreenshotToReport("WOS_Kickout");
						logger.log(LogStatus.FAIL, "<br>Account count in <b>KICKOUT</b> queue don't match, expected ["+ expKCKCount +"] but found ["+ actKCKCount +"]");
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
				
				else if ((row.getKey() != null) && (row.getKey().equals("Credit Balance"))) //check for Credit Balance
				{
					String expCreditBalanceCount = row.getValue();
					
					//get account count from Credit Balance
					objInbox.click_Image_CreditBalance();		
					int actCreditBalanceCount = objInbox.get_TotalAccounts_WorkOrderSummary(Constants.Queues.CREDIT_BALANCE);
					
					if(actCreditBalanceCount != Integer.parseInt(expCreditBalanceCount))
					{
						utils.save_ScreenshotToReport("WOS_CreditBalance");
						logger.log(LogStatus.FAIL, "<br>Account count in <b>CREDIT BALANCE</b> queue don't match, expected ["+ expCreditBalanceCount +"] but found ["+ actCreditBalanceCount +"]");
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
					{
						utils.save_ScreenshotToReport("WOS_Clarification");
						logger.log(LogStatus.FAIL, "<br>Account count in <b>PRINT AND MAIL RESPONSE</b> queue don't match, expected ["+actPRNTML_ANLCount+"] but found ["+expPRNTML_ANLCount+"]");
					}*/
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
				
				else if ((row.getKey() != null) && (row.getKey().equals("Credentialing Release"))) //check for Credentialing Release 
				{
					String expCRDRLSECount = row.getValue();
					
					//get account count from Credentialing Release 
					objInbox.click_Image_CredentialingResponse();	
					int actCRDRLSECount = objInbox.get_TotalAccounts_WorkOrderSummary(Constants.Queues.CREDENTIALING_RESPONSE);
					
					if(actCRDRLSECount != Integer.parseInt(expCRDRLSECount))
					{
						utils.save_ScreenshotToReport("WOS_Credentialing");
						logger.log(LogStatus.FAIL, "<br>Account count in <b>CREDENTIALING RESPONSE</b> queue don't match, expected ["+ expCRDRLSECount +"] but found ["+ actCRDRLSECount +"]");
					}
				}
				// add correspondance
				
				else if((row.getKey() != null) && (row.getKey().equals("TempSave"))) //check for TempSave 
				{
					String expTempSaveCount = row.getValue();
					
					//get account count from tempSave
					objInbox.click_Image_TempSave(false);
					int actTempSaveCount = objInbox.get_TotalAccounts_WorkOrderSummary(Constants.Queues.TEMP_SAVE);
					
					if(actTempSaveCount != Integer.parseInt(expTempSaveCount))
					{
						utils.save_ScreenshotToReport("WOS_TempSave");
						logger.log(LogStatus.FAIL, "<br>Account count in <b>TEMP SAVE</b> queue don't match, expected ["+ expTempSaveCount +"] but found ["+ actTempSaveCount +"]");
					}
				}		
				
				else if ((row.getKey() != null) && (row.getKey().equals("Appeals Response"))) //check for Appeals
				{
					String expAppealsCount = row.getValue();
					
					//get account count from appeals
					objInbox.click_Image_AppealResponse();
					int actAppealsCount = objInbox.get_TotalAccounts_WorkOrderSummary(Constants.Queues.APPEALS_RESPONSE);
					
					if(actAppealsCount != Integer.parseInt(expAppealsCount))
					{
						utils.save_ScreenshotToReport("WOS_Appeals");
						logger.log(LogStatus.FAIL, "<br>Account count in <b>APPEALS RESPONSE</b> queue don't match, expected ["+ expAppealsCount +"] but found ["+ actAppealsCount +"]");
					}
				}
				
				
				else
				{
					System.out.println("Can't find ["+ row.getKey() +"] queue");
				}
				
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
 	}
}
