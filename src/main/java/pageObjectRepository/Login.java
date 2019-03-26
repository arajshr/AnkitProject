package pageObjectRepository;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.WebDriverUtils;
import customDataType.Info;

public class Login {
	
	WebDriverUtils utils = new WebDriverUtils();
	
	
	@FindBy(name = "username")
	private WebElement txtUsername;
	
	@FindBy(name = "password")
	private WebElement txtPassword;
	
	@FindBy(xpath = "//button[text() = 'Login']")
	private WebElement btnLogin;
	
	public Info login(String userName, String password)
	{
		try
		{
			//System.out.println();
			Dashboard objDashboard = PageFactory.initElements(Configuration.driver, Dashboard.class);
			
			txtUsername.sendKeys(userName);
			txtPassword.sendKeys(password);
			btnLogin.click();
		
			
			Alert a = utils.isAlertPresent();			
			if(a != null) // not successful login
			{
				return new Info(false, a.getText());
			}
			else 
			{				
				utils.wait_Until_InvisibilityOf_LoadingScreen();
				
				try 
				{
					
					String sRole = objDashboard.getRole().getAttribute("textContent");
					String sWorkedBy = objDashboard.getWorkedBy().getAttribute("textContent");
					
					// successful login
					Configuration.logger.log(LogStatus.INFO, "Logged in as "+ sRole +" - [" + sWorkedBy + "]");
					return new Info(true, "Login successful");
				} 
				catch (Exception e) // No login, No Alert
				{
					Configuration.logger.log(LogStatus.WARNING, "<b>No alert message shown</b>");
					return new Info(false, "<b>No alert message shown</b>");
				}				
			}
		}		
		catch (Exception e) 
		{
			e.printStackTrace();
			Configuration.logger.log(LogStatus.FAIL, "Mehod: <br>" + e.getMessage());
			return null;
		}
	}

}
