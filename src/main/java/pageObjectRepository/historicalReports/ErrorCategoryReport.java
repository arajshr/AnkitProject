package pageObjectRepository.historicalReports;

import java.util.ArrayList;
import java.util.HashMap;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.asserts.SoftAssert;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.WebDriverUtils;

public class ErrorCategoryReport 
{
	WebDriverUtils utils = new WebDriverUtils();
	
	@FindBy(xpath = "//label[contains(text(), 'Client')]/following-sibling::div//input")
	private WebElement txtClientDropdown;	
	
	@FindBy(xpath = "//label[contains(text(), 'Location')]/following-sibling::div//input")
	private WebElement txtLocationDropdown;	
	
	@FindBy(xpath = "//label[contains(text(), 'Practice')]/following-sibling::div//input")
	private WebElement txtPracticeDropdown;	
	
	@FindBy(xpath = "//select[@ng-model='ddlErrorType']")
	private WebElement ddlErrortype;
	
	@FindBy(xpath = "//label[contains(text(), 'User')]/following-sibling::div//input")
	private WebElement txtUserDropdown;

	
	@FindBy(xpath = "//input[@title = 'Find Text in Report']")
	private WebElement txtFind;

	@FindBy(xpath = "//table")
	private WebElement tblErrorCategory;
	
	@FindBy(id = "btnShow")
	private WebElement btnShow;
	
	
	public HashMap<String, Integer> getReport(String sClient, String sLocation, String sPractice, String sUser, String sCatogory, String sSubCategory, String sMicroCategory, String sNanoCategry)
	{
		
		try
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			// select Client from multiSelect dropdown
			txtClientDropdown.click();
			txtClientDropdown.clear();
			txtClientDropdown.sendKeys(sClient);
			txtClientDropdown.findElement(By.xpath("./following-sibling::ul//a[contains(text(), '" + sClient + "')]")).click();
			txtClientDropdown.click();
			
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			// select Location from multiSelect dropdown
			txtLocationDropdown.click();
			txtLocationDropdown.clear();
			txtLocationDropdown.sendKeys(sLocation);
			txtLocationDropdown.findElement(By.xpath("./following-sibling::ul//a[contains(text(), '" + sLocation + "')]")).click();
			txtLocationDropdown.click();
			
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			// select Practice from multiSelect dropdown
			txtPracticeDropdown.click();
			txtPracticeDropdown.clear();
			txtPracticeDropdown.sendKeys(sPractice);
			txtPracticeDropdown.findElement(By.xpath("./following-sibling::ul//a[contains(text(), '" + sPractice + "')]")).click();
			txtPracticeDropdown.click();
			
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			// select Practice from multiSelect dropdown
			txtUserDropdown.click();
			txtUserDropdown.clear();
			txtUserDropdown.sendKeys(sUser);
			txtUserDropdown.findElement(By.xpath("./following-sibling::ul//a[contains(text(), '" + sUser + "')]")).click();
			txtUserDropdown.click();
			
			
			
			
			String[] arrErrors = {sCatogory, sSubCategory, sMicroCategory, sNanoCategry};
			
			// select error type			
			HashMap<String, Integer> mapErrorCount = new HashMap<>();
			ArrayList<String> lstErrorType = utils.get_Options(ddlErrortype);
			
			int i = 0;
			for (String sType : lstErrorType) 
			{
				utils.select(ddlErrortype, sType);
				btnShow.click();
				utils.wait_Until_InvisibilityOf_LoadingScreen();
				
				
				int colIndex = utils.getColumnIndex(tblErrorCategory, sUser + " - 20344");
				boolean isErrorPresent = utils.isElementPresent(By.xpath("//td[contains(text(), '"+ arrErrors[i] +"')]/following-sibling::td["+ (colIndex-1) +"]"));
				
				if(colIndex < 1 || (!isErrorPresent)) // user not found (or) Error not found
				{
					mapErrorCount.put(arrErrors[i], 0);  // add zero as count
					i++;
					continue;
				}
				
				// get count from report
				int iCount = Integer.parseInt(Configuration.driver.findElement(By.xpath("//td[contains(text(), '"+ arrErrors[i] +"')]/following-sibling::td["+ (colIndex-1) +"]")).getAttribute("textContent").trim());
				
				
				
				/*// verify error percentage
				int iActTotal = Integer.parseInt(Configuration.driver.findElement(By.xpath("//td[contains(text(), '"+ arrErrors[i] +"')]/following-sibling::td["+ (colIndex) +"]")).getAttribute("textContent").trim());
				
				int iActErrorPercent = Integer.parseInt(Configuration.driver.findElement(By.xpath("//td[contains(text(), '"+ arrErrors[i] +"')]/following-sibling::td["+ (colIndex+1) +"]")).getAttribute("textContent").trim());	
				
				int iActGrandTotal = Integer.parseInt(Configuration.driver.findElement(By.xpath("//td[contains(text(), 'Grand Total')]/following-sibling::td[2]")).getAttribute("textContent").trim());
								
				int iExpErrorPercent = (iActTotal/iActGrandTotal)*100;
				
				if(iActErrorPercent != iExpErrorPercent)
				{
					Configuration.logger.log(LogStatus.ERROR, "<b>Error Percent mismatch for " + sType + "[" + arrErrors[i] + "]</b>");
				}*/
				
				
				// add count to map
				mapErrorCount.put(sType, iCount);
				System.out.println(sType + " - " + iCount);
				
				
				i++;
			}
			
			
			Configuration.logger.log(LogStatus.INFO, "<b>" + mapErrorCount.toString() + "</b>");
			System.out.println(mapErrorCount);
			
			return mapErrorCount;
		} 
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, "Class: ErrorCategoryReport, Mehtod: getReport <br>" + e.getMessage());
			e.printStackTrace();
			return null;
		}		
	}


	public HashMap<String, Integer> update_ReportDetails(HashMap<String, Integer> initValues, int iCount)
	{
		try 
		{			
			for (String key : initValues.keySet()) 
			{
				Integer value = initValues.get(key);				
				initValues.put(key, (value + iCount));
			}
			return initValues;
		}
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, "Class: ErrorCategoryReport, Mehtod: update_ReportDetails <br>" + e.getMessage());
			return null;
		}
	}
	
	
	
	public SoftAssert verify_Report(HashMap<String, Integer> initValues, HashMap<String, Integer> finalValues) 
	{
		try 
		{
			SoftAssert s = new SoftAssert();
			
			for (String key : initValues.keySet()) 
			{
				Integer expValue = initValues.get(key);
				Integer actValue = finalValues.get(key);
				
				s.assertEquals(actValue, expValue, key + " ");
			}		

			return s;

		} 
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO,"Class: ErrorCategoryReport, Mehtod: verify_Report <br>" + e.getMessage());
		}
		return null;
	}
	


}
