package pageObjectRepository.historicalReports;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import configuration.Configuration;
import configuration.WebDriverUtils;
import customDataType.Info;

public class AgentProductionReport 
{
	WebDriverUtils utils = new WebDriverUtils();
	
	@FindBy(xpath = "//select[@ng-model='ddlClient']")
	private WebElement ddlClient;
	
	@FindBy(xpath = "//select[@ng-model='ddllocation']")
	private WebElement ddlLocation;
	
	@FindBy(xpath = "//select[@ng-model='ddlPractice']")
	private WebElement ddlPractices;
	
	@FindBy (id = "btnShow")
	private WebElement btnShow;
	
	
	public AgentProductionReport select_Client(String visibleText)
	{
		utils.select(ddlClient, visibleText);
		return this;
	}
	
	public AgentProductionReport select_Location(String visibleText)
	{
		utils.select(ddlLocation, visibleText);
		return this;
	}
	
	public AgentProductionReport select_Practice(String visibleText)
	{
		utils.select(ddlPractices, visibleText);
		return this;
	}
	
	public void get_Report()
	{
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		btnShow.click();	
		
		System.out.println();
	}
	
	
	public Info verify_Insurance(String insurance)
	{
		try
		{

			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			new WebDriverWait(Configuration.driver, 30).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(0));
			
			new WebDriverWait(Configuration.driver, 40).until(ExpectedConditions.presenceOfElementLocated(By.tagName("table")));
			
			if(utils.isElementPresent(By.xpath("//div[text() = 'No Data Available']")))
			{
				return new Info(false, "No Data Available");
			}
			else
			{	
				
				new WebDriverWait(Configuration.driver, 50).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[text() = '"+ insurance +"']")));
				//Configuration.driver.findElement(By.xpath("//div[text() = '"+ insurance +"']")).getAttribute("textContent");
			}					
			return new Info(true, "");
		}
		catch (NoSuchElementException e)
		{
			e.printStackTrace();
			return new Info(false, "No Such Element found");
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

	public String get_0_30(String insurance) 
	{		
		
		try
		{
			Configuration.driver.switchTo().frame(0);
			return Configuration.driver.findElement(By.xpath("//div[text() = '"+insurance+"']/../following-sibling::td[1]/div")).getAttribute("textContent");
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

	public String get_31_60(String insurance)
	{
		try
		{
			Configuration.driver.switchTo().frame(0);
			return Configuration.driver.findElement(By.xpath("//div[text() = '"+insurance+"']/../following-sibling::td[2]/div")).getAttribute("textContent");
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

	public String get_61_90(String insurance) 
	{	
		try
		{
			Configuration.driver.switchTo().frame(0);
			return Configuration.driver.findElement(By.xpath("//div[text() = '"+insurance+"']/../following-sibling::td[3]/div")).getAttribute("textContent");
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

	public String get_91_120(String insurance) 
	{
		try
		{
			Configuration.driver.switchTo().frame(0);
			return Configuration.driver.findElement(By.xpath("//div[text() = '"+insurance+"']/../following-sibling::td[4]/div")).getAttribute("textContent");
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

	public String get_120(String insurance) 
	{
		try
		{
			Configuration.driver.switchTo().frame(0);	
			return Configuration.driver.findElement(By.xpath("//div[text() = '"+insurance+"']/../following-sibling::td[5]/div")).getAttribute("textContent");
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

	public String get_rowTotal(String insurance) 
	{
		try
		{
			Configuration.driver.switchTo().frame(0);	
			return Configuration.driver.findElement(By.xpath("//div[text() = '"+insurance+"']/../following-sibling::td[6]/div")).getAttribute("textContent");
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
	
	
	
	
	
	
	
	public String get_0_30Total() 
	{
		try
		{
			Configuration.driver.switchTo().frame(0);	
			return Configuration.driver.findElement(By.xpath("//tbody/tr[2]//div[text() = 'Total']/../following-sibling::td[1]")).getAttribute("textContent");
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
	
	public String get_31_60Total() 
	{
		try
		{
			Configuration.driver.switchTo().frame(0);	
	
			return Configuration.driver.findElement(By.xpath("//tbody/tr[2]//div[text() = 'Total']/../following-sibling::td[2]")).getAttribute("textContent");
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
	
	public String get_61_90Total() 
	{
		
		try
		{
			Configuration.driver.switchTo().frame(0);	
			return Configuration.driver.findElement(By.xpath("//tbody/tr[2]//div[text() = 'Total']/../following-sibling::td[3]")).getAttribute("textContent");
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
	
	
	public String get_91_120Total() 
	{
		try
		{
			Configuration.driver.switchTo().frame(0);	
			return Configuration.driver.findElement(By.xpath("//tbody/tr[2]//div[text() = 'Total']/../following-sibling::td[4]")).getAttribute("textContent");
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
	
	public String get_120Total() 
	{
		try
		{
			Configuration.driver.switchTo().frame(0);	
			return Configuration.driver.findElement(By.xpath("//tbody/tr[2]//div[text() = 'Total']/../following-sibling::td[5]")).getAttribute("textContent");
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
	
	public String getTotal() 
	{
		try
		{
			Configuration.driver.switchTo().frame(0);	
			return Configuration.driver.findElement(By.xpath("//tbody/tr[2]//div[text() = 'Total']/../following-sibling::td[6]")).getAttribute("textContent");
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
