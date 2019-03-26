package pageObjectRepository.dashboard;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import configuration.Configuration;
import configuration.WebDriverUtils;
import pageObjectRepository.SearchDate;

public class ProviderCredentialReport 
{
	WebDriverUtils utils = new WebDriverUtils();
	
	@FindBy(xpath = "//select[@ng-model='ddlClient']")
	private WebElement ddlClient;
	
	@FindBy(xpath = "//select[@ng-model='ddllocation']")
	private WebElement ddlLocation;
	
	@FindBy(xpath = "//select[@ng-model='ddlPracticesProviderCredential']")
	private WebElement ddlPractices;
	
	@FindBy (id = "FromDate")
	private WebElement dtpFromDate;
	
	@FindBy (id = "ToDate")
	private WebElement dtpToDate;
	
	@FindBy(xpath = "//label[contains(text(), 'Provider name')]/following-sibling::div//input")
	private WebElement txtProviderDropdown;		
	
	@FindBy(xpath = "//label[contains(text(), 'Payer name')]/following-sibling::div//input")
	private WebElement txtPayerDropdown;

	@FindBy (id = "btnShow")
	private WebElement btnShow;
	
	@FindBy(xpath = "//div[@id='DataTables_Table_0_filter']/label/input")
	private WebElement txtSearch;
	
	
	
	
	public int get_Report(String sClient, String sLocation, String sPractice, String sProvider, String sPayer, String sDOSStart, String sDOSEnd)
	{
		try 
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			
			utils.select(ddlClient, sClient);
			
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			utils.select(ddlLocation, sLocation);
			utils.select(ddlPractices, sPractice);
			
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			// select provider name from multiSelect dropdown
			txtProviderDropdown.click();
			txtProviderDropdown.clear();
			txtProviderDropdown.sendKeys(sProvider);
			txtProviderDropdown.findElement(By.xpath("./following-sibling::ul//a[contains(text(), '" + sProvider + "')]")).click();
			txtProviderDropdown.click();
			
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			// select payer name from multiSelect dropdown
			txtPayerDropdown.click();
			txtPayerDropdown.clear();
			txtPayerDropdown.sendKeys(sPayer);
			txtPayerDropdown.findElement(By.xpath("./following-sibling::ul//a[contains(text(), '" + sPayer + "')]")).click();
			txtPayerDropdown.click();
			
			
			dtpFromDate.click();
			PageFactory.initElements(Configuration.driver, SearchDate.class).select_FromDate(sDOSStart);
			
			dtpToDate.click();
			PageFactory.initElements(Configuration.driver, SearchDate.class).select_ToDate(sDOSEnd);
			
			
			btnShow.click();
			
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			int noOfAccounts = 0;
			String sXpathProvider = "//td[text() = '"+ sProvider +"']";
			if(utils.isElementPresent(By.xpath(sXpathProvider)))
			{
				noOfAccounts = Integer.parseInt(Configuration.driver.findElement(By.xpath(sXpathProvider + "/following-sibling::td[2]")).getAttribute("textContent"));
			}
			
			return noOfAccounts;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return 0;
		}
	}

	public Integer update_ProviderCredential(int noOfAccounts, int count) 
	{	
		return noOfAccounts+=count;
	}
}
