package pageObjectRepository.historicalReports;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.asserts.SoftAssert;

import configuration.Configuration;
import configuration.Constants;
import configuration.WebDriverUtils;
import excelLibrary.ExcelHelper;
import pageObjectRepository.SearchDate;

public class FirstPassPaymentReport 
{
	WebDriverUtils utils = new WebDriverUtils();
	
	@FindBy (id = "FromDate")
	private WebElement dtpFromDate;
	
	@FindBy (id = "ToDate")
	private WebElement dtpToDate;
	
	@FindBy(xpath = "//select[@ng-model='ddlRptType']")
	private WebElement ddlRptType;
	
	@FindBy(id = "btnShow")
	private WebElement btnShow;
	
	
	
	public FirstPassPaymentReport select_ReportType(String reportType)
	{
		utils.select(ddlRptType, reportType);
		return this;
	}
	
	public FirstPassPaymentReport select_FromDate(String fromDate)
	{
		dtpFromDate.click();
		PageFactory.initElements(Configuration.driver, SearchDate.class).select_FromDate(fromDate);
		return this;
	}
	
	public FirstPassPaymentReport select_ToDate(String toDate)
	{
		dtpToDate.click();
		PageFactory.initElements(Configuration.driver, SearchDate.class).select_ToDate(toDate);
		return this;
	}
	
	
	public void get_Report()
	{
		btnShow.click();
		
		
	}
	
	
	public SoftAssert verify_Report(String insurance, String fromDate, String toDate)
	{
		try 
		{		
			DateFormat inputDF  = new SimpleDateFormat("dd/MM/yy");
			
			ExcelHelper ePayment = new ExcelHelper(Constants.xlPayment);
			ArrayList<String> dos = ePayment.get_FirstPassPayment_DOS("Sheet1", inputDF.parse(fromDate), inputDF.parse(toDate));
						
			Date dDOS;
			Date today = new Date();
			
			int exp_0_45 = 0;
			int exp_46_90 = 0;
			int exp_91_135 = 0;
			int exp_135plus = 0;
		
			for(String date : dos)
			{
				dDOS = inputDF.parse(date);
				
				int days = (int) TimeUnit.DAYS.convert(today.getTime() - dDOS.getTime(), TimeUnit.MILLISECONDS);
				
				if(days >= 0 && days <=45)
					exp_0_45++;
				
				else if(days > 45 && days <=90)
					exp_46_90++;
				
				else if(days > 90 && days <=135)
					exp_91_135++;
				
				else if(days > 135)
					exp_135plus++;
			}
		
			Configuration.driver.switchTo().frame(0);
			
			int act_0_45 = Integer.parseInt(Configuration.driver.findElement(By.xpath("//div[text() = '"+insurance+"']/../following-sibling::td[1]")).getAttribute("textContent"));
			int act_46_90 = Integer.parseInt(Configuration.driver.findElement(By.xpath("//div[text() = '"+insurance+"']/../following-sibling::td[2]")).getAttribute("textContent"));
			int act_91_135 = Integer.parseInt(Configuration.driver.findElement(By.xpath("//div[text() = '"+insurance+"']/../following-sibling::td[3]")).getAttribute("textContent"));
			int act_135plus = Integer.parseInt(Configuration.driver.findElement(By.xpath("//div[text() = '"+insurance+"']/../following-sibling::td[4]")).getAttribute("textContent"));
			
			Configuration.driver.switchTo().defaultContent();
			
			SoftAssert s = new SoftAssert();
			
			s.assertEquals(act_0_45, exp_0_45, "<br> DOS 0-45");
			s.assertEquals(act_46_90, exp_46_90, "<br> DOS 46-90");
			s.assertEquals(act_91_135, exp_91_135, "<br> DOS 91-135");
			s.assertEquals(act_135plus, exp_135plus, "<br> DOS 135+");
			
			return s;
		
		} 
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	
}

