package pageObjectRepository.dashboard;

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
	
	@FindBy(xpath = "//label[contains(text(), 'Client')]/following-sibling::div//input")
	private WebElement txtClientDropdown;	
	
	@FindBy(xpath = "//label[contains(text(), 'Location')]/following-sibling::div//input")
	private WebElement txtLocationDropdown;	
	
	@FindBy(xpath = "//label[contains(text(), 'Practice')]/following-sibling::div//input")
	private WebElement txtPracticeDropdown;	
	
	@FindBy(xpath = "//select[@ng-model='ddlUser']")
	private WebElement ddlUser;
	
	@FindBy(xpath = "//input[@title = 'Find Text in Report']")
	private WebElement txtFind;

	@FindBy(id = "btnShow")
	private WebElement btnShow;
	
	
	public void getReport(String sClient, String sLocation, String sPractice, String sUser)
	{
		
		try
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			// select Client from multiSelect dropdown
			txtClientDropdown.click();
			txtClientDropdown.clear();
			txtClientDropdown.sendKeys(sClient);
			txtClientDropdown.findElement(By.xpath("./following-sibling::ul//a[contains(text(), '" + sClient + "')]")).click();
			txtClientDropdown.click();
			
			
			
			// select Location from multiSelect dropdown
			txtLocationDropdown.click();
			txtLocationDropdown.clear();
			txtLocationDropdown.sendKeys(sLocation);
			txtLocationDropdown.findElement(By.xpath("./following-sibling::ul//a[contains(text(), '" + sLocation + "')]")).click();
			txtLocationDropdown.click();
			
			
			
			// select Practice from multiSelect dropdown
			txtPracticeDropdown.click();
			txtPracticeDropdown.clear();
			txtPracticeDropdown.sendKeys(sPractice);
			txtPracticeDropdown.findElement(By.xpath("./following-sibling::ul//a[contains(text(), '" + sPractice + "')]")).click();
			txtPracticeDropdown.click();
					
			
			
			// select Practice from multiSelect dropdown
			utils.select(ddlUser, sUser, false);
			
			btnShow.click();
			
			utils.wait_Until_InvisibilityOf_LoadingScreen();			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}
	
	
	public Info verifyReport(String sAccountNmber, ArrayList<ArrayList<String>> history)
	{
		try
		{
			
			//td[3]/div[text() = '6070431']/../following-sibling::td/div[text() = 'Veena K']/../following-sibling::td/div[text() = 'DEDUCTIBLE']/../following-sibling::td/div[text() = 'NeedCall']
			
			Configuration.driver.switchTo().defaultContent();
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(0));
			
			//utils.wait(7000);
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
		catch (Exception e)
		{
			utils.save_ScreenshotToReport("UserWorkedAccountsReport");
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
