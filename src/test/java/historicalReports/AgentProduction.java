package historicalReports;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.Constants;
import configuration.WebDriverUtils;
import customDataType.Info;
import customDataType.Transaction;
import pageObjectRepository.Dashboard;
import pageObjectRepository.Login;
import pageObjectRepository.historicalReports.AgentProductionReport;
import pageObjectRepository.transaction.AnalystInbox;
import pageObjectRepository.transaction.ClaimTransaction;

public class AgentProduction extends Configuration
{
	WebDriverUtils utils = new WebDriverUtils();
	Login objLogin;
	Dashboard objDashboard;
	AgentProductionReport objReport;
	AnalystInbox objAnalystInbox;
	ClaimTransaction objClaimTransaction;
	
	String fromDate = new SimpleDateFormat("MM/dd/yy").format(new Date());
	
	@Test(groups= {"REG"})
	public void verify_AgentProduction_Report()
	{
		

		int executableRowIndex = excel.isExecutable("verify_AgentProduction_Report");		
		XSSFSheet sheet = excel.setSheet("TestData");
		
		String client = excel.readValue(sheet, executableRowIndex, "client");
		String location = excel.readValue(sheet, executableRowIndex, "location");
		String practice = excel.readValue(sheet, executableRowIndex, "practice");
		
		String insurance = excel.readValue(sheet, executableRowIndex, "insurance");
		String nextQueue = excel.readValue(sheet, executableRowIndex, "nextQueue1");		
		HashMap<String, String> data = excel.get_TransactionData(nextQueue);
		
		String claimStatus = data.get("claim_status");
		String actionCode = data.get("action_code");
		String resolveType = data.get("resolve_type"); 
		String notes = data.get("note");
		
		
		objLogin = PageFactory.initElements(driver, Login.class);
		objDashboard = PageFactory.initElements(driver, Dashboard.class);
		objReport = PageFactory.initElements(driver, AgentProductionReport.class);
		objAnalystInbox = PageFactory.initElements(driver, AnalystInbox.class);
		objClaimTransaction = PageFactory.initElements(driver, ClaimTransaction.class);
		
		
		
		// get login credentials from excel
		HashMap<String, String> login = excel.get_LoginByRole(Constants.Role.TL);
		String sTLUsername = login.get("ntlg");
		String sTLPassword = login.get("password");	

		// login - TL
		objLogin.login(sTLUsername, sTLPassword);	
		
		objDashboard.navigateTo_HistoricalReport().navigateTo_AgentProduction_Report();
		logger.log(LogStatus.INFO, "Navigate to Agent Production Report");
		
		
		objReport.select_Client(client)	//.select_FromDate(fromDate)
					.select_Location(location)
					.select_Practice(practice)
					.get_Report();
		
		logger.log(LogStatus.INFO, "Set filter values as Client ["+ client +"], Location ["+ location +"], Practice ["+ practice +"]");
		
		
		
		int exp_0_30 = 0, exp_31_60 = 0, exp_61_90 = 0, exp_91_120 = 0, exp_120 = 0; 
		int exp_0_30Total = 0,exp_31_60Total = 0,exp_61_90Total = 0,exp_91_120Total = 0,exp_120Total = 0;
		int expRowTotal = 0, expTotal = 0;
		
		
		int act_0_30 = 0, act_31_60 = 0, act_61_90 = 0, act_91_120 = 0, act_120 = 0; 
		int act_0_30Total = 0,act_31_60Total = 0,act_61_90Total = 0,act_91_120Total = 0,act_120Total = 0;
		int actRowTotal = 0, actTotal = 0;
		
		
		Info result = objReport.verify_Insurance(insurance);
		utils.save_ScreenshotToReport("AgentProductionReport");
		
		if(result.getStatus())
		{			
			exp_0_30 = Integer.parseInt(objReport.get_0_30(insurance));
			exp_31_60 = Integer.parseInt(objReport.get_31_60(insurance));
			exp_61_90 = Integer.parseInt(objReport.get_61_90(insurance));
			exp_91_120 = Integer.parseInt(objReport.get_91_120(insurance));
			exp_120 = Integer.parseInt(objReport.get_120(insurance));
						
			exp_0_30Total = Integer.parseInt(objReport.get_0_30Total());
			exp_31_60Total = Integer.parseInt(objReport.get_31_60Total());
			exp_61_90Total = Integer.parseInt(objReport.get_61_90Total());
			exp_91_120Total = Integer.parseInt(objReport.get_91_120Total());
			exp_120Total = Integer.parseInt(objReport.get_120Total());
							
			expRowTotal = Integer.parseInt(objReport.get_rowTotal(insurance));
			expTotal = Integer.parseInt(objReport.getTotal());			
		}		
		
		objDashboard.signOut();
		
		
		// get login credentials from excel
		login = excel.get_LoginByRole(Constants.Role.ANALYST);
		String sAnalystUsername = login.get("ntlg");
		String sAnalystPassword = login.get("password");	

		// login - Analyst
		objLogin.login(sAnalystUsername, sAnalystPassword);	
		objDashboard.navigateTo_Transaction().navigateTo_Inbox();
		logger.log(LogStatus.INFO, "Navigate to Transaction -> Inbox");
		
		objAnalystInbox.select_Practice(practice);
		objAnalystInbox.click_Image_Regular(true);
		logger.log(LogStatus.INFO, "Select practice ["+practice+"] and click REGULAR icon");
		
		
		Transaction openAccount = objAnalystInbox.open_PatientAccount(Constants.Queues.REGULAR, "INSURANCE", insurance);
		
		if(openAccount.getStatus())
		{	
			/*logger.log(LogStatus.INFO,  "Open account [" + openAccount.getPateintAccountNumber() + "]");*/
			
			String sAccountNumber = objClaimTransaction.get_PatientAccountNumber();
			String sUID = objClaimTransaction.get_UID();
			
			Transaction submitAccount = objClaimTransaction.submit_Claim(sUID, claimStatus, actionCode, resolveType, notes);
			Assert.assertTrue(submitAccount.getStatus(), "Account [" + sAccountNumber +"] not submitted. Account Number ["+ sAccountNumber +"], UID ["+ sUID +"]");
			logger.log(LogStatus.INFO, "<b>Claim moved to AR Closed. Account Number ["+ sAccountNumber +"], UID ["+ sUID +"]</b>");
			
			exp_120Total += 1;
			exp_120 += 1;
			expRowTotal += 1;
			expTotal += 1;		
			
			objDashboard.signOut();
			
			
			// login - TL
			objLogin.login(sTLUsername, sTLPassword);	
			
			objDashboard.navigateTo_HistoricalReport().navigateTo_AgentProduction_Report();
			logger.log(LogStatus.INFO, "Navigate to Agent Production Report");
			
			
			objReport.select_Client(client)  // .select_FromDate(fromDate)
			//.select_Location(location)
			//.select_Practice(practice)
			.get_Report();
			logger.log(LogStatus.INFO, "Set filter values as Client ["+ client +"], Location ["+ location +"], Practice ["+ practice +"] and From Date ["+ fromDate +"]");
			
					
			result = objReport.verify_Insurance(insurance);
			utils.save_ScreenshotToReport("AgentProductionReport");
			
			if(result.getStatus())
			{
				act_0_30 = Integer.parseInt(objReport.get_0_30(insurance));
				act_31_60 = Integer.parseInt(objReport.get_31_60(insurance));
				act_61_90 = Integer.parseInt(objReport.get_61_90(insurance));
				act_91_120 = Integer.parseInt(objReport.get_91_120(insurance));
				act_120 = Integer.parseInt(objReport.get_120(insurance));
				
				
				act_0_30Total = Integer.parseInt(objReport.get_0_30Total());
				act_31_60Total = Integer.parseInt(objReport.get_31_60Total());
				act_61_90Total = Integer.parseInt(objReport.get_61_90Total());
				act_91_120Total = Integer.parseInt(objReport.get_91_120Total());
				act_120Total = Integer.parseInt(objReport.get_120Total());
				
								
				actRowTotal = Integer.parseInt(objReport.get_rowTotal(insurance));
				actTotal = Integer.parseInt(objReport.getTotal());
				
				SoftAssert s = new SoftAssert();
				
				s.assertEquals(act_120, exp_120, "Worked Account value do not match under 120+ category.. expected ["+ exp_120 +"] but found ["+ act_120 +"]<br>");				
				s.assertEquals(act_91_120, exp_91_120, "Worked Account value do not match under 91-120 category.. expected ["+ exp_91_120 +"] but found ["+ act_91_120 +"]<br>");				
				s.assertEquals(act_61_90, exp_61_90, "Worked Account value do not match under 61-90 category.. expected ["+ exp_61_90 +"] but found ["+ act_61_90 +"]<br>");
				s.assertEquals(act_31_60, exp_31_60, "Worked Account value do not match under 31-60 category.. expected ["+ exp_31_60 +"] but found ["+ act_31_60 +"]<br>");
				s.assertEquals(act_0_30, exp_0_30, "Worked Account value do not match under 0-30 category.. expected ["+ exp_0_30 +"] but found ["+ act_0_30 +"]<br>");
								
				s.assertEquals(actRowTotal, expRowTotal, "Row wise Total value do not match for insurance ["+insurance+"].. expected ["+ expRowTotal +"] but found ["+ actRowTotal +"]<br>");
								
				s.assertEquals(act_120Total, exp_120Total, "DOS wise Total value do not match for 120+ category.. expected ["+ exp_120Total +"] but found ["+ act_120Total +"]<br>");
				s.assertEquals(act_91_120Total, exp_91_120Total, "DOS wise Total value do not match for 91-120 category.. expected ["+ exp_91_120Total +"] but found ["+ act_91_120Total +"]<br>");
				s.assertEquals(act_61_90Total, exp_61_90Total, "DOS wise Total value do not match for 61-90 category.. expected ["+ exp_61_90Total +"] but found ["+ act_61_90Total +"]<br>");
				s.assertEquals(act_31_60Total, exp_31_60Total, "DOS wise Total value do not match for 31-60 category.. expected ["+ exp_31_60Total +"] but found ["+ act_31_60Total +"]<br>");
				s.assertEquals(act_0_30Total, exp_0_30Total, "DOS wise Total value do not match for 0-30 category.. expected ["+ exp_0_30Total +"] but found ["+ act_0_30Total +"]<br>");
				
				s.assertEquals(actTotal, expTotal, "Toatl Worked Account count do not match.. expected ["+ expTotal +"] but found ["+ actTotal +"]<br>");
				
				s.assertAll();
				
				logger.log(LogStatus.INFO, "Report values verified");
			}
			else
			{
				logger.log(LogStatus.FAIL, result.getDescription());
			}
		}
	}
}



/*int status = 1;
StringBuilder sError = new StringBuilder();

if(act_120 != exp_120)
{
	sError.append("Worked Account value do not match under 120+ category.. expected ["+ exp_120 +"] but found ["+ act_120 +"]<br>");
	status = 0;
}
if(act_91_120 != exp_91_120)
{
	sError.append("Worked Account value do not match under 91-120 category.. expected ["+ exp_91_120 +"] but found ["+ act_91_120 +"]<br>");
	status = 0;
}
if(act_61_90 != exp_61_90)
{
	sError.append("Worked Account value do not match under 61-90 category.. expected ["+ exp_61_90 +"] but found ["+ act_61_90 +"]<br>");
	status = 0;
}
if(act_31_60 != exp_31_60)
{
	sError.append("Worked Account value do not match under 61-90 category.. expected ["+ exp_61_90 +"] but found ["+ act_61_90 +"]<br>");
	status = 0;
}
if(act_0_30 != exp_0_30)
{
	sError.append("Worked Account value do not match under 61-90 category.. expected ["+ exp_61_90 +"] but found ["+ act_61_90 +"]<br>");
	status = 0;
}





if(actRowTotal != expRowTotal)
{
	sError.append("Row wise Total value do not match for insurance ["+insurance+"].. expected ["+ expRowTotal +"] but found ["+ actRowTotal +"]<br>");
	status = 0;
}



if(act_120Total != exp_120Total)
{
	sError.append("DOS wise Total value do not match for 120+ category.. expected ["+ exp_120Total +"] but found ["+ act_120Total +"]<br>");
	status = 0;
}

if(act_91_120Total != exp_91_120Total)
{
	sError.append("DOS wise Total value do not match for 120+ category.. expected ["+ exp_120Total +"] but found ["+ act_120Total +"]<br>");
	status = 0;
}

if(act_61_90Total != exp_61_90Total)
{
	sError.append("DOS wise Total value do not match for 120+ category.. expected ["+ exp_120Total +"] but found ["+ act_120Total +"]<br>");
	status = 0;
}

if(act_31_60Total != exp_31_60Total)
{
	sError.append("DOS wise Total value do not match for 120+ category.. expected ["+ exp_120Total +"] but found ["+ act_120Total +"]<br>");
	status = 0;
}

if(act_0_30Total != exp_0_30Total)
{
	sError.append("DOS wise Total value do not match for 120+ category.. expected ["+ exp_120Total +"] but found ["+ act_120Total +"]<br>");
	status = 0;
}


if(actTotal != expTotal)
{
	sError.append("Toatl Worked Account count do not match.. expected ["+ expTotal +"] but found ["+ actTotal +"]<br>");
	status = 0;
}

if(status == 0)
{
	logger.log(LogStatus.FAIL, sError.toString());
}
else
{
	logger.log(LogStatus.PASS, "Report verified");
}*/
