package pageObjectRepository.historicalReports;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import configuration.Configuration;
import configuration.WebDriverUtils;
import pageObjectRepository.SearchDate;

public class CodingAndPaymentReport
{
WebDriverUtils utils = new WebDriverUtils();
	
	@FindBy (id = "FromDate")
	private WebElement dtpFromDate;
	
	@FindBy (id = "ToDate")
	private WebElement dtpToDate;
	
	@FindBy(xpath = "//select[@ng-model='ddlClient']")
	private WebElement ddlClient;
	
	@FindBy(xpath = "//select[@ng-model='ddllocation']")
	private WebElement ddlLocation;
	
	@FindBy(xpath = "//select[@ng-model='ddlPractice']")
	private WebElement ddlPractices;
	
	@FindBy(xpath = "//select[@ng-model='ddlQueue']")
	private WebElement ddlQueue;
	
	@FindBy (id = "btnShow")
	private WebElement btnShow;
	
	

	public CodingAndPaymentReport select_FromDate(String fromDate)
	{		
		dtpFromDate.click();
		PageFactory.initElements(Configuration.driver, SearchDate.class).select_FromDate(fromDate);
		
		return this;
	}
		
	public CodingAndPaymentReport select_Client(String visibleText)
	{
		utils.select(ddlClient, visibleText);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return this;
	}
	
	public CodingAndPaymentReport select_Location(String visibleText)
	{
		utils.select(ddlLocation, visibleText);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return this;
	}
	
	public CodingAndPaymentReport select_Practice(String visibleText)
	{
		utils.select(ddlPractices, visibleText);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return this;
	}
	
	public CodingAndPaymentReport select_Queue(String visibleText)
	{
		utils.select(ddlQueue, visibleText);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return this;
	}
	
	public void get_Report()
	{
		btnShow.click();		
		utils.wait_Until_InvisibilityOf_LoadingScreen();
	}
	
	
	public int get_Worked_Accounts(String claimStatus, String actionCode)
	{
		try
		{
			Configuration.driver.switchTo().frame(0);		
			WebElement tblReport = Configuration.driver.findElement(By.xpath("(//div[@id = 'ReportViewer1_ctl09']//table)[last()]"));
			int column = get_table_column_index(tblReport, actionCode);

			if(column > 0)
			{
				String value = Configuration.driver.findElement(By.xpath("//td/div[text() = '"+claimStatus+"']/../following-sibling::td["+column+"]/div")).getAttribute("textContent");
				return Integer.parseInt(value);
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
		return 0;		
	}
	
	public int get_table_column_index(WebElement dataTable, String columnName)
	{
		int colCount = (dataTable.findElements(By.xpath("./tbody/tr[2]/td/div"))).size();
		
		for (int colIndex=1; colIndex<colCount; colIndex++)    //column index starts from 1, td[0] contains no data
		{
		    String sValue = null;
		    sValue = dataTable.findElement(By.xpath("./tbody/tr[2]/td["+(colIndex + 2 )+"]/div")).getText();
		    if(sValue.equals(columnName))
		    {
		    	return colIndex;
		    }
		}
		return -1;
	}
	
}
