package pageObjectRepository.dashboard;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import configuration.Configuration;
import configuration.WebDriverUtils;
import customDataType.PMReport;

public class PrintAndMailReport 
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
	
	@FindBy(xpath = "//div[@id='DataTables_Table_0_filter']//input")
	private WebElement txtSearch;
	
	
	/*public Result verify_Insurance(String insurance)
	{
		try
		{
			
			if(utils.isElementPresent(By.xpath("//div[text() = 'No Data Available']")))
			{
				return new Result(false, "No Data Available");
			}
			else
			{			
				Configuration.driver.findElement(By.xpath("//td[text() = '"+ insurance +"']")).getAttribute("textContent");
			}					
			return new Result(true);
		}
		catch (NoSuchElementException e)
		{
			return new Result(false, e.getMessage());
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Result(false, e.getMessage());
		}
		
	}*/
	
	
	public PMReport get_ReportDetails(String insurance)
	{
		try 
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			txtSearch.clear();
			txtSearch.sendKeys(insurance);
			
			if(utils.isElementPresent(By.xpath("//td[text() = '"+ insurance +"']")))
			{
				String account = Configuration.driver.findElement(By.xpath("//td[text() = '"+insurance+"']/following-sibling::td[1]")).getAttribute("textContent");		
				String dollarValue = Configuration.driver.findElement(By.xpath("//td[text() = '"+insurance+"']/following-sibling::td[2]")).getAttribute("textContent");		
				String balance = Configuration.driver.findElement(By.xpath("//td[text() = '"+insurance+"']/following-sibling::td[3]")).getAttribute("textContent");
				
				
				return new PMReport(Integer.parseInt(account), Double.parseDouble(dollarValue),Double.parseDouble(balance));
			}
			else
			{
				return new PMReport(Integer.parseInt("0"), Double.parseDouble("0"),Double.parseDouble("0"));
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
		
		
		
	}
	
}


