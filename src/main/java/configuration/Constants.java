package configuration;

import java.text.SimpleDateFormat;
import java.util.Date;

public interface Constants {
	
	String browser = "chrome";
	String url = "http://10.1.2.85/ATOM";
	
			
	/* variable for the connection string	*/
	String connectionUrl = "jdbc:sqlserver://10.1.2.85:1433;databaseName=ATOM_Test;"; 
	String dbUserName = "atom";
	String dbPassword = "atomusr";
		
	
	/* Test Data */	
	String xlFilePath = ".\\src\\test\\resources\\senerio_Import_RCM.xlsx";
	String xlPayment = ".\\src\\test\\resources\\Payment.xlsx";
	String xlAging = ".\\src\\test\\resources\\Aging_Upload.xlsx";
	
	String xlAging_ForAllocation = ".\\src\\test\\resources\\Allocation_Aging_BYP.xlsx";
	
	
	/* Report folder */
	
	SimpleDateFormat folderDateFormat = new SimpleDateFormat("dd_MM_yyyy");
	SimpleDateFormat reportDateFormat = new SimpleDateFormat("HH_mm");
	
	Date currentDate = new Date(); 
	String year = new SimpleDateFormat("yyyy").format(currentDate);
	String month = new SimpleDateFormat("MM").format(currentDate);
	String day = new SimpleDateFormat("dd").format(currentDate);
		
	
	String reportFolderPath = ".\\Reports\\" + year + "\\" + month + "\\" + day + "\\ExtentReport_"+ reportDateFormat.format(new Date())+".html";	
	String screenShotFolderPath = ".\\Reports\\"+ year + "\\" + month + "\\" + day + "\\screenshots\\";
	
	/* Extent report config file*/
	String configFilePath = ".\\src\\test\\resources\\extent-config.xml";
	
	
	/* next queue */
	enum Role {ANALYST, 
				//JUNIOR_CALLER,
				CALLER,
				PRINT_AND_MAIL, 
				PAYMENT, 
				CODING,
				CORRESPONDANCE,
				CREDIT_BALANCE,
				APPEALS,
				TL,
				MANAGER,
				QCA};
	
	enum Queues {PRIORITY, 
				REGULAR,
				DENIAL, 				
				KICKOUT,
				
				NEEDCALL_RESPONSE,
				CLARIFICATION,
				CLIENT_ESCALATION_RELEASE, 
				CREDIT_BALANCE,
				CORRESPONDENCE,
				PAYMENT_RESPONSE,
				CODING_RESPONSE, 
				CREDENTIALING_RESPONSE,
				PRINT_AND_MAIL_RESPONSE, 
				TEMP_SAVE,
				NEED_CALL,
				VOICE_CALL,
				
				APPEALS,
				APPEALS_RESPONSE, 
				
				MEOB, 
				MEOB_FOLLOWUP, 
				MEOB_PP_RESPONSE,
				
				MEOB_CALLER_RESPONSE,
				MEOB_CLIENTESCALATION,
				
				FOLLOW_UP};
				
				
				
	


}
