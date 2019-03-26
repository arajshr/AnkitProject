package pageObjectRepository.transaction;

import java.util.HashMap;
import java.util.LinkedList;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.asserts.SoftAssert;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.WebDriverUtils;
import customDataType.Info;

public class ClaimTracerPage 
{
	WebDriverUtils utils = new WebDriverUtils();
	
	@FindBy(xpath = "//select[@ng-model='ddlPractices']")
	private WebElement ddlPractices;
	
	@FindBy(xpath = "//select[@ng-model='ddlColumns']")
	private WebElement ddlColumns;
	
	@FindBy(xpath = "//select[@ng-model='ddlConditions']")
	private WebElement ddlConditions;
	
	@FindBy(id = "txtValue")
	private WebElement txtValue;
	
	@FindBy(id = "btnAdd")
	private WebElement btnAdd;
	
	@FindBy(id = "btnSearch")
	private WebElement btnSearch;
	
	@FindBy(id = "DataTables_Table_0")
	private WebElement tblClaimTracer;
	
	
	public void add_SearchCondition(String practice, String column, String condition, String value) 
	{
		try 
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();			
			utils.select(ddlPractices, practice, false);
			
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			utils.select(ddlColumns, column);
			utils.select(ddlConditions, condition);
			txtValue.sendKeys(value);
			btnAdd.click();
			
		} 
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, "Class: ClaimTracerPage, Mehtod: add_SearchCondition  <br>" + e.getMessage());
		}
		
	}
	
	public Info search(String sUID) 
	{
		btnSearch.click();
		
		try
		{
			utils.wait(1000);
			int rowCount = tblClaimTracer.findElements(By.xpath("./tbody/tr")).size();
			
			return new Info(true, "["+rowCount+"] Rows found");
		}
		catch (NoSuchElementException e) 
		{
			return new Info(false, "No data rows found");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Info(false, e.getMessage());
		}
	}
	
	public SoftAssert verify_ClaimTracer(LinkedList<HashMap<String, String>> claimTrace) 
	{
		try 
		{
			SoftAssert s = new SoftAssert();
			
			// scroll to end of the table
			utils.scrollWindow(Configuration.driver.findElement(By.xpath(".//*[@id='DataTables_Table_0']//tr[1]/td[30]")));
			utils.save_ScreenshotToReport("CliamTracer");
			
			
			for(int i=0; i<claimTrace.size();i++)
			{
				HashMap<String, String> claim = claimTrace.get(i);			
				//System.out.println("Row - " + (i+1) );
				
				if(!(i == claimTrace.size()-1))
				{
					String actWorkedBy =  tblClaimTracer.findElement(By.xpath(".//tr["+(i+1)+"]/td[28]")).getAttribute("textContent").trim().toUpperCase();			
					String expWorkedBy = claim.get("workedBy").toUpperCase();
					
					s.assertEquals(actWorkedBy, expWorkedBy, "<br>Row ["+(i+1)+"],  WorkedBy mismatch");
				}
				
				String actQueue = tblClaimTracer.findElement(By.xpath(".//tr["+(i+1)+"]/td[29]")).getAttribute("textContent").trim().toUpperCase();		
				String expQueue = claim.get("queue").toUpperCase();
				
				
				
				s.assertEquals(actQueue, expQueue, "<br> in Row["+(i+1)+"]Queue mismatch");
				
			}
			
			return s;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
		
	}
	
	
}
