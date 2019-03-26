package pageObjectRepository.dashboard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.asserts.SoftAssert;

import configuration.Configuration;
import configuration.WebDriverUtils;

public class ARTrailBalanceReport
{
	WebDriverUtils utils = new WebDriverUtils();
	
	@FindBy(xpath = "//select[@ng-model='ddlClient']")
	private WebElement ddlClient;
	
	@FindBy(xpath = "//select[@ng-model='ddllocation']")
	private WebElement ddlLocation;
	
	@FindBy(xpath = "//select[@ng-model='ddlPractice']")
	private WebElement ddlPractices;
	
	
	public ARTrailBalanceReport select_Client(String visibleText)
	{
		utils.select(ddlClient, visibleText);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return this;
	}
	
	public ARTrailBalanceReport select_Location(String visibleText)
	{
		utils.select(ddlLocation, visibleText);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return this;
	}
	
	public ARTrailBalanceReport select_Practice(String visibleText)
	{
		utils.select(ddlPractices, visibleText);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return this;
	}
		
	public SoftAssert get_ARTrailBalance_Report(String practice, String insurance)
	{
		SoftAssert s = new SoftAssert();
		try 
		{
			int act0_30, act31_60,act61_90, act91_120, act120plus;
			act0_30 = act31_60 = act61_90 = act91_120 = act120plus = 0;
			
			if(utils.isElementPresent(By.xpath("//td[text() = '"+insurance+"']")))
			{
				act0_30 = Integer.parseInt(Configuration.driver.findElement(By.xpath("//td[text() = '"+insurance+"']/following-sibling::td[1]")).getAttribute("textAttribute"));
				act31_60 = Integer.parseInt(Configuration.driver.findElement(By.xpath("//td[text() = '"+insurance+"']/following-sibling::td[2]")).getAttribute("textAttribute"));
				act61_90 = Integer.parseInt(Configuration.driver.findElement(By.xpath("//td[text() = '"+insurance+"']/following-sibling::td[3]")).getAttribute("textAttribute"));
				act91_120 = Integer.parseInt(Configuration.driver.findElement(By.xpath("//td[text() = '"+insurance+"']/following-sibling::td[4]")).getAttribute("textAttribute"));
				act120plus = Integer.parseInt(Configuration.driver.findElement(By.xpath("//td[text() = '"+insurance+"']/following-sibling::td[5]")).getAttribute("textAttribute"));
			}

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
			Date today = sdf.parse(sdf.format(new Date()));		
			long diff = 0;
			
			int exp120plus, exp91_120, exp61_90, exps31_60, exp0_30;
			exp120plus = exp91_120 = exp61_90 = exps31_60 = exp0_30 = 0;
			
			ArrayList<String> dos = Configuration.db.get_ARTrailBalance(practice, insurance);		
			for (String startDate : dos) 
			{	
				diff = TimeUnit.DAYS.convert(today.getTime() - (sdf.parse(startDate)).getTime(), TimeUnit.MILLISECONDS);
				
				if(diff >120)
			    {
			    	exp120plus++; 
			    }
			    else if(diff>90 && diff<=120)
			    {
			    	exp91_120++;
			    }
			    else if(diff>60 && diff<=90)
			    {
			    	exp61_90++;
			    }
			    else if(diff>30 && diff<=60)
			    {
			    	exps31_60++;
			    }
			    else if(diff>=0 && diff<=30)
			    {
			    	exp0_30++;
			    }
			}
			
			
			s.assertEquals(act0_30, exp0_30, "0_30, ");
			s.assertEquals(act31_60, exps31_60, "31_60, ");
			s.assertEquals(act61_90, exp61_90, "61_90, ");
			s.assertEquals(act91_120, exp91_120, "91_120, ");
			s.assertEquals(act120plus, exp120plus, "120+, ");
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return s;
		
	}
	
	
}
