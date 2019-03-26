package pageObjectRepository.historicalReports;

import java.util.ArrayList;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.WebDriverUtils;
import customDataType.Info;

public class MEOB_ProductionReport
{
	
WebDriverUtils utils = new WebDriverUtils();
	
	@FindBy(xpath = "//select[@ng-model='ddlClient']")
	private WebElement ddlClient;
	
	@FindBy(xpath = "//select[@ng-model='ddllocation']")
	private WebElement ddlLocation;
	
	@FindBy(xpath = "//select[@ng-model='ddlPractice']")
	private WebElement ddlPractice;
	
	@FindBy(id = "btnShow")
	private WebElement btnShow;
	
	@FindBy(xpath = "//select[@ng-model='ddlUser']")
	private WebElement ddlUser;
	
	
	
	@FindBy(xpath = "//input[@title = 'Find Text in Report']")
	private WebElement txtFind;
	
	
	
	public void getReport(String client, String location, String practice, String user) 
	{		
		try 
		{
			utils.select(ddlClient, client);
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			utils.select(ddlLocation, location);
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			utils.select(ddlPractice, practice);			
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			btnShow.click();
		}
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, "Class: MEOB_ProductionReport , Mehtod: getReport <br>" + e.getMessage());
		}		
	}

	
	public Info verifyReport(String sInventoryId)
	{
		try
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(0));
			
			utils.wait(5000);
			new WebDriverWait(Configuration.driver, 20).until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@title = 'Find Text in Report']")));
			txtFind.sendKeys(sInventoryId, Keys.ENTER);
			utils.wait(2000);
			
			/*utils.captureScreenshot("", "UWA_" + expAccountNumber, LogStatus.INFO); 
			 * throws UnhandledAlertException 
			 * handle the alert before you can take a screenshot
			 * */
			
			Alert a = utils.isAlertPresent();			
			if(a != null)// account not found
			{	
				String sErrorMessage = a.getText();
				a.accept();
				utils.wait(3000);
				
				utils.save_ScreenshotToReport("MEOB_ProductionReport");
				return new Info(false, "Inventory not found at MEOB Production Report<br><b>Error message [" + sErrorMessage + "]</b>");
			}
			else // account found
			{
				utils.save_ScreenshotToReport("MEOBReport");
				return new Info(true, "");
			}	
		}
		catch (NoSuchElementException e)
		{
			e.printStackTrace();
			new Info(false, e.getMessage());
		}
		finally 
		{
			Configuration.driver.switchTo().defaultContent();
		}
		return null;
		
		
	}
	
}
