package pageObjectRepository.historicalReports;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import configuration.Configuration;
import configuration.WebDriverUtils;
import customDataType.ErrorByAgentReport;
import customDataType.Info;

public class ErrorParetoByAgentReport
{
	WebDriverUtils utils = new WebDriverUtils();
	
	@FindBy(xpath = "//input[@title = 'Find Text in Report']")
	private WebElement txtFind;
	
	@FindBy(xpath = "(//td/input[@title = 'Last Page'])[1]")
	private WebElement imgLastPage;
	
	
	
	public Info search_ReasonForNC(String reason, String type)
	{
		try
		{									
			Configuration.driver.switchTo().frame(0);
			
			txtFind.sendKeys(reason, Keys.ENTER);
			utils.wait(2000);
			Alert a = utils.isAlertPresent();			
			if(a != null)// reason not found
			{				
				a.accept();
				utils.wait(3000);
				return new Info(false, a.getText());
			}
			else // reason found
			{
				/*WebElement element = Configuration.driver.findElement(By.xpath("//div/span[text() = '"+reason+"']"));
				((JavascriptExecutor)Configuration.driver).executeScript("arguments[0].scrollIntoView(true);", element);*/
				utils.wait(2000);
				
				utils.save_ScreenshotToReport("ErrorParetoReport_"+ type);
				return new Info(true, "");
			}	
		}
		catch (Exception e) 
		{
			return new Info(false, e.getMessage());
		}
		finally 
		{
			Configuration.driver.switchTo().defaultContent();
		}
	}
	
	
	public ErrorByAgentReport get_ErrorByAgent_Details(String reasonForNC, String type)
	{
		try
		{
			
			Info status = search_ReasonForNC(reasonForNC, type);
			if(status.getStatus())
			{
				
				new WebDriverWait(Configuration.driver, 15).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(0));
				
				int expEncounters = Integer.parseInt(Configuration.driver.findElement(By.xpath("//td/div/span[text() = '"+reasonForNC+"']/../../following-sibling::td[1]/div")).getAttribute("textContent"));
				double expDollarValue = Double.parseDouble(Configuration.driver.findElement(By.xpath("//td/div/span[text() = '"+reasonForNC+"']/../../following-sibling::td[2]/div")).getAttribute("textContent").trim().replace("$", "").replace(",", ""));
				double expPercentage = Double.parseDouble(Configuration.driver.findElement(By.xpath("//td/div/span[text() = '"+reasonForNC+"']/../../following-sibling::td[3]/div")).getAttribute("textContent").trim().replace("%", ""));
				
				Configuration.driver.switchTo().defaultContent();
				utils.scrollWindow(0, -400);
				new WebDriverWait(Configuration.driver, 15).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(0));
				
				imgLastPage.click();
				
				int totalEncounters = Integer.parseInt(Configuration.driver.findElement(By.xpath("//td/div[text() = 'Total']/../following-sibling::td[1]")).getAttribute("textContent"));
				double totalDollarValue = Double.parseDouble(Configuration.driver.findElement(By.xpath("//td/div[text() = 'Total']/../following-sibling::td[2]")).getAttribute("textContent").trim().replace("$", "").replace(",", ""));
				double totalPercent = Double.parseDouble(Configuration.driver.findElement(By.xpath("//td/div[text() = 'Total']/../following-sibling::td[3]")).getAttribute("textContent").trim().replace("%", ""));
				
				return new ErrorByAgentReport(expEncounters, expDollarValue, expPercentage, totalEncounters, totalDollarValue, totalPercent);
			}
			else
			{
				return new ErrorByAgentReport();
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
		finally 
		{
			Configuration.driver.switchTo().defaultContent();
		}
		
	}
	
	
}
