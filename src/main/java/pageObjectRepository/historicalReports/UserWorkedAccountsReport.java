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

import configuration.Configuration;
import configuration.WebDriverUtils;
import customDataType.Info;

public class UserWorkedAccountsReport 
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
	
	@FindBy(xpath = "//select[@ng-model='ddlRole']")
	private WebElement ddlRole;
	
	@FindBy(xpath = "//input[@title = 'Find Text in Report']")
	private WebElement txtFind;

	@FindBy(xpath = "//div[text() = 'Practice Name']")
	private WebElement txtPracticeName;
	
	
	
	public void getReport(String client, String location, String practice, String user) 
	{
		try {
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			
			utils.select(ddlClient, client);
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			utils.select(ddlLocation, location);
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			utils.select(ddlPractice, practice);
					
			btnShow.click();
			
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	
	
	
	 
    
    
	public Info verifyReport(String sAccountNmber, ArrayList<ArrayList<String>> history)
	{
		try
		{
			
			//td[3]/div[text() = '6070431']/../following-sibling::td/div[text() = 'Veena K']/../following-sibling::td/div[text() = 'DEDUCTIBLE']/../following-sibling::td/div[text() = 'NeedCall']
			
			
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(0));
			
			utils.wait(7000);
			new WebDriverWait(Configuration.driver, 30).until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@title = 'Find Text in Report']")));
			txtFind.sendKeys(sAccountNmber, Keys.ENTER);
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
				
				utils.save_ScreenshotToReport("UserWorkedAccountsReport");
				return new Info(false, "Account not found at User Worked Reports<br><b>Error message [" + sErrorMessage + "]</b>");
			}
			else // account found
			{
				utils.save_ScreenshotToReport("UserWorkedAccountsReport");				
				StringBuilder s = new StringBuilder();
				
				for (ArrayList<String> transaction : history) 
				{
					/* 0 - Employee Name
					 * 1 - Claim Status
					 * 2 - Action Code	 */
					
					String sEmployeeName = transaction.get(0);
					String sClaimStatus = transaction.get(1);
					String sActionCode = transaction.get(2);
					
					try 
					{
						
						/*Configuration.driver.findElement(By.xpath("//td[3]/div/span[text() = '"+sAccountNmber+"']/../.."
								+ "/following-sibling::td/div["+ ignoreCaseEqualsXPath("text()", sEmployeeName)+"]"));*/
										
										
						Configuration.driver.findElement(By.xpath("//td[3]/div/span[text() = '"+sAccountNmber+"']/../.."
								+ "/following-sibling::td/div["+ utils.ignoreCaseEqualsXPath("text()", sEmployeeName)+"]/.."
								+ "/following-sibling::td/div["+ utils.ignoreCaseEqualsXPath("text()", sClaimStatus)+"]/.."
								+ "/following-sibling::td/div["+ utils.ignoreCaseEqualsXPath("text()", sActionCode)+"]"));
						
					}
					catch (NoSuchElementException e) 
					{
						s.append("Employee ["+sEmployeeName+"], Claim Status["+sClaimStatus+"], Action Code["+sActionCode+"] not found<br>");						
						//Configuration.logger.log(LogStatus.FAIL, "Employee ID["+sEmployeeId+"], Claim Status["+sClaimStatus+"], Action Code["+sActionCode+"] not found");
					}
					
					if(s.length() > 0)
					{
						new Info(false, "<b>Account don't verify at User Worked Reports <br>" + s + "</b>");
					}
				}
								
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
