package pageObjectRepository.dashboard;

import java.time.LocalTime;
import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.asserts.SoftAssert;

import configuration.Configuration;
import configuration.WebDriverUtils;

public class ClientPriorityTrackingReport
{
	WebDriverUtils utils = new WebDriverUtils();
	
	@FindBy(xpath = "//select[@ng-model='ddlClient']")
	private WebElement ddlClient;
	
	@FindBy(xpath = "//select[@ng-model='ddllocation']")
	private WebElement ddlLocation;
	
	@FindBy(xpath = "//select[@ng-model='ddlPracticeClientPriority']")
	private WebElement ddlPractices;
	
	@FindBy (id = "btnShow")
	private WebElement btnShow;
	
	
	public ClientPriorityTrackingReport select_Client(String visibleText)
	{
		utils.select(ddlClient, visibleText);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return this;
	}
	
	public ClientPriorityTrackingReport select_Location(String visibleText)
	{
		utils.select(ddlLocation, visibleText);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return this;
	}
	
	public ClientPriorityTrackingReport select_Practice(String visibleText)
	{
		utils.select(ddlPractices, visibleText);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return this;
	}
	
	public void get_Report()
	{
		btnShow.click();		
		utils.wait_Until_InvisibilityOf_LoadingScreen();
	}
	
	
	public SoftAssert verify_Client_Priority_Report(String fileName, String uploadDate) // Mm/dd/yyyy
	{
		SoftAssert s = new SoftAssert();
		try 
		{
			HashMap<String, String> priority = Configuration.db.get_PriorityDetails(fileName, uploadDate);
			//System.out.println(priority);
			
			utils.save_ScreenshotToReport("ClientPriorityTracking_Historical");
			
			WebElement wbFileName = Configuration.driver.findElement(By.xpath("//td/div[contains(text(), '"+ fileName+"')]/../following-sibling::td/div[text() = '" 	+ uploadDate + "']"));
			
			//int actFileReceived = Integer.parseInt(wbFileName.findElement(By.xpath("./following-sibling::td[1]")).getAttribute("textContent"));
			int actOutStanding = Integer.parseInt(wbFileName.findElement(By.xpath("./following-sibling::td[1]")).getAttribute("textContent"));
			int actCompleted = Integer.parseInt(wbFileName.findElement(By.xpath("./following-sibling::td[2]")).getAttribute("textContent"));
			int actPending = Integer.parseInt(wbFileName.findElement(By.xpath("./following-sibling::td[3]")).getAttribute("textContent"));
			String actTimeTaken = wbFileName.findElement(By.xpath("./following-sibling::td[4]")).getAttribute("textContent");
			
			
			int expOutStanding = Integer.parseInt(priority.get("validRecords"));
			int expCompleted = Integer.parseInt(priority.get("completed"));
			int expPending = (expOutStanding - expCompleted);
			String expTimetaken =  LocalTime.ofSecondOfDay(Integer.parseInt(priority.get("timeTaken"))).toString();			
			//String expFileReceived = priority.get("fileReceived");
						
			//s.assertEquals(actFileReceived, expFileReceived);		
			s.assertEquals(actOutStanding, expOutStanding, "Total OutStanding, ");
			s.assertEquals(actCompleted, expCompleted, "Completed, ");
			s.assertEquals(actPending, expPending, "Pending, ");
			s.assertEquals(actTimeTaken, expTimetaken, "Time Taken, ");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return s;
		
	}
}
