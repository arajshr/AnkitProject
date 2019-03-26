package pageObjectRepository.historicalReports;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import configuration.Configuration;
import configuration.WebDriverUtils;
import customDataType.Info;
import pageObjectRepository.SearchDate;

public class AccountResolveTypeReport 
{
	WebDriverUtils utils = new WebDriverUtils();
	
	@FindBy (id = "FromDate")
	private WebElement dtpFromDate;
	
	@FindBy (id = "ToDate")
	private WebElement toDate;
	
	@FindBy(xpath = "//select[@ng-model='ddlClient']")
	private WebElement ddlClient;
	
	@FindBy(xpath = "//select[@ng-model='ddllocation']")
	private WebElement ddlLocation;
	
	@FindBy(xpath = "//select[@ng-model='ddlPractice']")
	private WebElement ddlPractices;
	
	@FindBy (id = "btnShow")
	private WebElement btnShow;
	
	
	public AccountResolveTypeReport select_FromDate(String fromDate)
	{		
		dtpFromDate.click();
		PageFactory.initElements(Configuration.driver, SearchDate.class).select_FromDate(fromDate);
		
		return this;
	}
		
	public AccountResolveTypeReport select_Client(String visibleText)
	{
		utils.select(ddlClient, visibleText);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return this;
	}
	
	public AccountResolveTypeReport select_Location(String visibleText)
	{
		utils.select(ddlLocation, visibleText);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return this;
	}
	
	public AccountResolveTypeReport select_Practice(String visibleText)
	{
		utils.select(ddlPractices, visibleText);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return this;
	}
	
	public void get_Report()
	{
		btnShow.click();
		
		utils.wait(2000);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
	}

	
	public Info verify_User(String user)
	{
		try
		{
			utils.wait(10);
			new WebDriverWait(Configuration.driver, 30).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(0));

			if(utils.isElementPresent(By.xpath("//div[text() = 'No Data Available']")))
			{
				return new Info(false, "No Data Available");
			}
			else
			{	
				new WebDriverWait(Configuration.driver, 50).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[text() = '"+ user +"']")));
				//Configuration.driver.findElement(By.xpath("//span[text() = '"+ user +"']")).getAttribute("textContent");
			}					
			return new Info(true, "");
		}
		catch (NoSuchElementException e)
		{
			return new Info(false, "");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Info(false, e.getMessage());
		}
		finally 
		{
			Configuration.driver.switchTo().defaultContent();
		}
	}
	
	public String get_Total_Worked_Accounts(String user)
	{
		try 
		{	
			Configuration.driver.switchTo().frame(0);
			
			
			int columnIndex = 0;
			List<WebElement> header = Configuration.driver.findElements(By.xpath("//div[text() = 'Users']/../following-sibling::td"));
			
			for(int i = 0; i<header.size(); i++)
			{
				String sHeader = header.get(i).getAttribute("textContent");
				
				if(sHeader.equals("Total Worked Claims"))
				{
					columnIndex = i+1;
					break;
				}
			}
			
		
			
			if(columnIndex>0)
			{
				String totalWorked = Configuration.driver.findElement(By.xpath("//div[text() = '"+ user +"']/../following-sibling::td["+columnIndex+"]")).getAttribute("textContent");
				Configuration.driver.switchTo().defaultContent();
				
				return totalWorked.replace("%", "");
			}
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			Configuration.driver.switchTo().defaultContent();
		}
		
		return "0";
		
	}
	
	public String get_Called_Accounts(String user)
	{
		try 
		{
			Configuration.driver.switchTo().frame(0);
			
			int columnIndex = 0;
			List<WebElement> header = Configuration.driver.findElements(By.xpath("//div[text() = 'Users']/../following-sibling::td"));
			
			for(int i = 0; i< header.size(); i++)
			{
				if(header.get(i).getAttribute("textContent").equals("#CalledAccounts"))
				{
					columnIndex = i+1;
					break;
				}
			}			
		
			
			if(columnIndex>0)
			{
				String calledAccounts = Configuration.driver.findElement(By.xpath("//div[text() = '"+ user +"']/../following-sibling::td["+columnIndex+"]")).getAttribute("textContent");
				return calledAccounts.replace("%", "");
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally 
		{
			Configuration.driver.switchTo().defaultContent();
		}
		
		return "0";
	}
	
	
	
	public String get_SelfResolved_Worked_Accounts(String user)
	{
		try 
		{	
			Configuration.driver.switchTo().frame(0);
			
			int columnIndex = 0;
			List<WebElement> header = Configuration.driver.findElements(By.xpath("//div[text() = 'Users']/../following-sibling::td"));
			
			for(int i = 0; i< header.size(); i++)
			{
				if(header.get(i).getAttribute("textContent").equals("#SelfResolvedAccounts"))
				{
					columnIndex = i+1;
					break;
				}
			}
			
			
			if(columnIndex>0)
			{
				String selfResolvedAccounts = Configuration.driver.findElement(By.xpath("//div[text() = '"+ user +"']/../following-sibling::td["+columnIndex+"]")).getAttribute("textContent");
				
				return selfResolvedAccounts.replace("%", "");
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally 
		{
			Configuration.driver.switchTo().defaultContent();
		}
		
		return "0";
	}
	
	public String get_Web_Accounts(String user)
	{
		try 
		{
			Configuration.driver.switchTo().frame(0);
						
			
			int columnIndex = 0;
			List<WebElement> header = Configuration.driver.findElements(By.xpath("//div[text() = 'Users']/../following-sibling::td"));
			
			for(int i = 0; i< header.size(); i++)
			{
				if(header.get(i).getAttribute("textContent").equals("#WebAccounts"))
				{
					columnIndex = i+1;
					break;
				}
			}
				
			
			if(columnIndex>0)
			{
				String webAccounts = Configuration.driver.findElement(By.xpath("//div[text() = '"+ user +"']/../following-sibling::td["+columnIndex+"]")).getAttribute("textContent");
				
				return webAccounts.replace("%", "");
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally 
		{
			Configuration.driver.switchTo().defaultContent();
		}
		
		return "0";	
	}

	
}
