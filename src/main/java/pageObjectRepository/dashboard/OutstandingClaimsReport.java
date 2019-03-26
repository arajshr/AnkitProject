package pageObjectRepository.dashboard;

import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.WebDriverUtils;

public class OutstandingClaimsReport 
{
WebDriverUtils utils = new WebDriverUtils();
	
	@FindBy(xpath = "//select[@ng-model='ddlClient']")
	private WebElement ddlClient;
	
	@FindBy(xpath = "//select[@ng-model='ddllocation']")
	private WebElement ddlLocation;
	
	@FindBy(xpath = "//select[@ng-model='ddlPractice']")
	private WebElement ddlPractices;
	
	@FindBy(xpath = "//div[@id='DataTables_Table_0_filter']/label/input")
	private WebElement txtSearch;
	
	@FindBy(xpath = "//input[@id = 'WTD']")
	private WebElement rdWTD;
	
	@FindBy(xpath = "//input[@id = 'MTD']")
	private WebElement rdMTD;
	
	
	
	public OutstandingClaimsReport select_Client(String visibleText)
	{
		utils.select(ddlClient, visibleText);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return this;
	}
	
	public OutstandingClaimsReport select_Location(String visibleText)
	{
		utils.select(ddlLocation, visibleText);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return this;
	}
	
	public OutstandingClaimsReport select_Practice(String visibleText)
	{
		utils.select(ddlPractices, visibleText);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return this;
	}
	
	public HashMap<String, Integer> get_OutstandingClaims(String queue)
	{
		//td[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'), 'appeals') ]
		
		HashMap<String, Integer> mapClaims = new HashMap<>();
		try
		{
			
			long startTime = System.currentTimeMillis();
			
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			
			long endTime = System.currentTimeMillis();
			
			Configuration.logger.log(LogStatus.INFO, "Time to load Queue Based OutStanding Claims: " + (endTime - startTime)/1000 + " seconds");
			System.out.println("Time to load Queue Based OutStanding Claims: " + (endTime - startTime)/1000 + " sec");
			
			
			
			
			String sXpathQueueName = "//td[text() = '"+queue+"']";
			int count0_24 = 0, rowTotal = 0, coltotal = 0;
			
			txtSearch.sendKeys(queue);
			
			if(utils.isElementPresent(By.xpath(sXpathQueueName)))
			{
				count0_24 = Integer.parseInt(Configuration.driver.findElement(By.xpath( sXpathQueueName + "/following-sibling::td[1]")).getAttribute("textContent"));
			}
			
			rowTotal = Integer.parseInt(Configuration.driver.findElement(By.xpath( sXpathQueueName + "/following-sibling::td[7]")).getAttribute("textContent"));
			coltotal = Integer.parseInt(Configuration.driver.findElement(By.xpath("//td[text() = 'Total']/following-sibling::td[1]")).getAttribute("textContent"));			
			
			mapClaims.put("Value", count0_24);
			mapClaims.put("rowTotal", rowTotal);
			mapClaims.put("colTotal", coltotal);			
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return mapClaims;
		
	}

	public HashMap<String, Integer> update_OutStandingClaims(HashMap<String, Integer> expClaimsData) 
	{
		try 
		{
			int expValue = expClaimsData.get("Value")+1;		
			expClaimsData.put("Value", expValue);
			
			int expRowTotal = expClaimsData.get("rowTotal")+1;
			expClaimsData.put("rowTotal", expRowTotal);		
			
			int expColTotal = expClaimsData.get("colTotal")+1;
			expClaimsData.put("percentOfResolution", expColTotal);
			
			return expClaimsData;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;		
	}
}
