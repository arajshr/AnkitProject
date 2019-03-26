package pageObjectRepository.historicalReports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.WebDriverUtils;
import customDataType.Quality;
import pageObjectRepository.SearchDate;

public class SimpleQualityReport
{
	WebDriverUtils utils = new WebDriverUtils();
	
	@FindBy (id = "FromDate")
	private WebElement dtpFromDate;
	
	@FindBy (id = "FromDate")
	private WebElement toDate;
	
	@FindBy(xpath = "//select[@ng-model='ddlClient']")
	private WebElement ddlClient;
	
	@FindBy(xpath = "//select[@ng-model='ddllocation']")
	private WebElement ddlLocation;
	
	@FindBy(xpath = "//select[@ng-model='ddlPractice']")
	private WebElement ddlPractices;
		
	@FindBy (id = "btnShow")
	private WebElement btnShow;
	
	
	public SimpleQualityReport select_FromDate(String fromDate)
	{
		dtpFromDate.click();
		PageFactory.initElements(Configuration.driver, SearchDate.class).select_FromDate(fromDate);		
		return this;
	}
		
	public SimpleQualityReport select_Client(String visibleText)
	{
		utils.select(ddlClient, visibleText);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return this;
	}
	
	public SimpleQualityReport select_Location(String visibleText)
	{
		utils.select(ddlLocation, visibleText);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return this;
	}
	
	public SimpleQualityReport select_Practice(String visibleText)
	{
		utils.select(ddlPractices, visibleText,false);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return this;
	}
	
	public void get_Report()
	{
		btnShow.click();		
		utils.wait_Until_InvisibilityOf_LoadingScreen();
	}
	
	
	public boolean verify_User(String user)
	{
		try
		{
			Configuration.driver.switchTo().frame(0);
			return utils.isElementPresent(By.xpath("//div[text() = '"+ user +"']"));
			
			//Configuration.driver.findElement(By.xpath("//div[text() = '"+ user +"']")).getAttribute("textContent");
			//return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally 
		{
			Configuration.driver.switchTo().defaultContent();
		}
		return false;
	}
	
	public Quality get_Quality_Details(String user)
	{
		Quality quality = null;
		
		try 
		{
			Configuration.driver.switchTo().defaultContent();
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			utils.wait(3000);
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(0));
			
			
			if(utils.isElementPresent(By.xpath("//div[text() = 'No Data Available']")))
			{
				Configuration.logger.log(LogStatus.INFO, "<b>No Data Available</b>");
				quality = new Quality(0, 0, 0, 0.0, 0.0);
			}
			else if(utils.isElementPresent(By.xpath("//div[text() = '"+user+"']")))
			{
				
				
				
				
				
				/* ******************** verify report columns ********************  */
				
				List<String> lstExpHeader = Arrays.asList(new String[] { "Employee ID", "Name", "TL Name", "Functional Manager", "Omega Location", "Worked", "Audited",
																			"Error", "Error %","Accuracy %" });
				List<String> lstActHeader = new ArrayList<>();

				List<WebElement> lstWbHeader = Configuration.driver	.findElements(By.xpath("(//table)[last()]//tr[2]/td/div"));

				for (WebElement wbHeader : lstWbHeader) 
				{
					lstActHeader.add(wbHeader.getText().trim());
				}

				if (!lstExpHeader.equals(lstActHeader)) 
				{
					Configuration.logger.log(LogStatus.ERROR, "<b>Report columns do not match, </b><br> expected "+ lstExpHeader + "<br>but found " + lstActHeader);
				}

				/* ******************** verify report columns ******************** */ 
				
				
				
				
				String worked = Configuration.driver.findElement(By.xpath("//div[text() = '"+user+"']/../following-sibling::td[4]")).getAttribute("textContent");			
				String audited = Configuration.driver.findElement(By.xpath("//div[text() = '"+user+"']/../following-sibling::td[5]")).getAttribute("textContent");
				String error = Configuration.driver.findElement(By.xpath("//div[text() = '"+user+"']/../following-sibling::td[6]")).getAttribute("textContent");
				String errorPercent = Configuration.driver.findElement(By.xpath("//div[text() = '"+user+"']/../following-sibling::td[7]")).getAttribute("textContent").replace("%", "");
				String accuracy = Configuration.driver.findElement(By.xpath("//div[text() = '"+user+"']/../following-sibling::td[8]")).getAttribute("textContent").replace("%", "");
				
				
				
							
				quality = new Quality(Integer.parseInt(worked), Integer.parseInt(audited), Integer.parseInt(error), Double.parseDouble(errorPercent.equals("NA") ? "0.0" : errorPercent), Double.parseDouble(accuracy.equals("NA") ? "0.0" : accuracy));
			}
			else
			{
				quality = new Quality(0, 0, 0, 0.0, 0.0);
			}
			return quality;			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			Configuration.driver.switchTo().defaultContent();
		}
		return null;
	}
	
	public String get_Worked(String user)
	{		
		try 
		{
			Configuration.driver.switchTo().frame(0);
			String worked = Configuration.driver.findElement(By.xpath("//div[text() = '"+user+"']/../following-sibling::td[1]")).getAttribute("textContent");
			Configuration.driver.switchTo().defaultContent();
			
			return worked;			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;		
	}
	
	public String get_Audited(String user)
	{		
		try 
		{
			Configuration.driver.switchTo().frame(0);
			String audited = Configuration.driver.findElement(By.xpath("//div[text() = '"+user+"']/../following-sibling::td[2]")).getAttribute("textContent");
			Configuration.driver.switchTo().defaultContent();
			
			return audited;			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;		
	}
	
	public String get_Error(String user)
	{		
		try 
		{
			Configuration.driver.switchTo().frame(0);
			String error = Configuration.driver.findElement(By.xpath("//div[text() = '"+user+"']/../following-sibling::td[3]")).getAttribute("textContent");
			Configuration.driver.switchTo().defaultContent();
			
			return error;
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;		
	}
	
	public String get_Error_Percent(String user)
	{		
		try 
		{
			Configuration.driver.switchTo().frame(0);
			String errorPercent = Configuration.driver.findElement(By.xpath("//div[text() = '"+user+"']/../following-sibling::td[4]")).getAttribute("textContent");
			Configuration.driver.switchTo().defaultContent();
			
			return errorPercent;			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;	
	}
	
	public String get_Accuracy(String user)
	{		
		try 
		{
			Configuration.driver.switchTo().frame(0);
			String accuracy = Configuration.driver.findElement(By.xpath("//div[text() = '"+user+"']/../following-sibling::td[5]")).getAttribute("textContent");
			Configuration.driver.switchTo().defaultContent();
			
			return accuracy;			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
		
	}
}
