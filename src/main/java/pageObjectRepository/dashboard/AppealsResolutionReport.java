package pageObjectRepository.dashboard;

import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import configuration.Configuration;
import configuration.WebDriverUtils;

public class AppealsResolutionReport 
{
	WebDriverUtils utils = new WebDriverUtils();
	
	@FindBy(xpath = "//div[@id='DataTables_Table_0_filter']/label/input")
	private WebElement txtSearch;
	
	
	public HashMap<String, Integer> get_AppealsReportData(String insurance)
	{
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		
		txtSearch.clear();
		txtSearch.sendKeys(insurance);
		
		String sXpathInsurance = "//td[text() = '"+insurance+"']";
		HashMap<String, Integer> mapAppeals = new HashMap<>();
		
		mapAppeals.put("totalAppealsSent", 0);
		mapAppeals.put("appealsInPPReport", 0);
		mapAppeals.put("percentOfResolution", 0);
		
		try
		{
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath(sXpathInsurance)));
							
			mapAppeals.put("totalAppealsSent", Integer.parseInt(Configuration.driver.findElement(By.xpath( sXpathInsurance + "/following-sibling::td[1]")).getAttribute("textContent")));
			mapAppeals.put("appealsInPPReport", Integer.parseInt(Configuration.driver.findElement(By.xpath( sXpathInsurance + "/following-sibling::td[2]")).getAttribute("textContent")));
			mapAppeals.put("percentOfResolution", Integer.parseInt(Configuration.driver.findElement(By.xpath( sXpathInsurance + "/following-sibling::td[3]")).getAttribute("textContent")));
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return mapAppeals;
	}


	public HashMap<String, Integer> update_AppealsReportData(HashMap<String, Integer> expAppeals, int appeal, int payment) 
	{
		int expTotal = expAppeals.get("totalAppealsSent")+appeal;		
		expAppeals.put("totalAppealsSent", expTotal);
		
		int expPayment = expAppeals.get("appealsInPPReport")+payment;
		expAppeals.put("appealsInPPReport", expPayment);		
		
		int expPercent = (expAppeals.get("appealsInPPReport")/expAppeals.get("totalAppealsSent"));
		expAppeals.put("percentOfResolution", expPercent);
		
		return expAppeals;
	}
	
}
