package pageObjectRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;

public class SearchDate 
{
	@FindBy(xpath = "(//div[@class = 'dtp-actual-month p80'])[1]")
	private WebElement dtpFromDate_Month;
	
	@FindBy(xpath = "(//a[@class = 'dtp-select-month-before'])[1]")
	private WebElement dtpFromDate_PreviousMonth;
	
	@FindBy(xpath = "(//div[@class = 'dtp-select-month-after'])[1]")
	private WebElement dtpFromDate_NextMonth;
	
	
	
	@FindBy(xpath = "(//div[@class = 'dtp-actual-year p80'])[1]")
	private WebElement dtpFromDate_Year;
	
	@FindBy(xpath = "(//a[@class = 'dtp-select-year-before'])[1]")
	private WebElement dtpFromDate_PreviousYear;
	
	@FindBy(xpath = "(//div[@class = 'dtp-select-year-after'])[1]")
	private WebElement dtpFromDate_NextYear;
	
	
	@FindBy(xpath = "(//button[text() = 'OK'])[1]")
	private WebElement btn_FromDate_OK;
	
	
	
	@FindBy(xpath = "(//div[@class = 'dtp-actual-month p80'])[2]")
	private WebElement dtpToDate_Month;
	
	@FindBy(xpath = "(//a[@class = 'dtp-select-month-before'])[2]")
	private WebElement dtpToDate_PreviousMonth;
	
	@FindBy(xpath = "(//div[@class = 'dtp-select-month-after'])[2]")
	private WebElement dtpToDate_NextMonth;
	
	
	
	@FindBy(xpath = "(//div[@class = 'dtp-actual-year p80'])[2]")
	private WebElement dtpToDate_Year;
	
	@FindBy(xpath = "(//a[@class = 'dtp-select-year-before'])[2]")
	private WebElement dtpToDate_PreviousYear;
	
	@FindBy(xpath = "(//div[@class = 'dtp-select-year-after'])[2]")
	private WebElement dtpToDate_NextYear;
	
	
	@FindBy(xpath = "(//button[text() = 'OK'])[2]")
	private WebElement btn_ToDate_OK;
	
	
	
	
	
	public void select_FromDate(String fromDate)
	{
		try 
		{
			DateFormat inputDF  = new SimpleDateFormat("MM/dd/yyyy");
			
			
			/*System.out.println(fromDate);
			System.out.println(inputDF.parse(fromDate));
			System.out.println(new SimpleDateFormat("yyyy").format(inputDF.parse(fromDate)));*/
			
			
			int expMonth = Integer.parseInt(new SimpleDateFormat("MM").format(inputDF.parse(fromDate)));
			int expDay = Integer.parseInt(new SimpleDateFormat("dd").format(inputDF.parse(fromDate)));
			int expYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(inputDF.parse(fromDate)));
			
			
			
			int actYear; 
			int actMonth;
			
			while(true)
			{
				actYear = Integer.parseInt(dtpFromDate_Year.getAttribute("textContent"));
				if(expYear < actYear)
				{
					dtpFromDate_PreviousYear.click();
				}
				else if(expYear == actYear)
				{
					break;
				}
			}
			
			
			DateFormat inputDF2  = new SimpleDateFormat("MMM/dd/yy");			
			while(true)
			{		
				//System.out.println(new SimpleDateFormat("MM").format(inputDF2.parse(dtpMonth.getText() +"/01/1900")));	
				
				actMonth = Integer.parseInt(new SimpleDateFormat("MM").format(inputDF2.parse(dtpFromDate_Month.getText() +"/01/1900")));
				
				if(expMonth < actMonth)
				{
					dtpFromDate_PreviousMonth.click();
				}
				else if(expMonth > actMonth)
				{
					dtpFromDate_NextMonth.click();;
				}
				else if(expMonth == actMonth)
					break;
			}
			
			
			Configuration.driver.findElement(By.xpath("//a[text() = '" + ((expDay>0 && expDay<10) ? "0" + expDay : String.valueOf(expDay)) + "']")).click();
			//btn_FromDate_OK.click();
		} 
		catch (NumberFormatException | ParseException e) 
		{
			Configuration.logger.log(LogStatus.WARNING, "Mehtod Name: select_FromDate<br>" + e.getMessage());
		}
		
	}
		
	public void select_ToDate(String toDate)
	{
		try 
		{
			DateFormat inputDF  = new SimpleDateFormat("MM/dd/yyyy");
			
			
			int expMonth = Integer.parseInt(new SimpleDateFormat("MM").format(inputDF.parse(toDate)));
			int expDay = Integer.parseInt(new SimpleDateFormat("dd").format(inputDF.parse(toDate)));
			int expYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(inputDF.parse(toDate)));
			
			
			
			/*System.out.println(toDate);
			System.out.println(inputDF.parse(toDate));
			System.out.println(new SimpleDateFormat("yyyy").format(inputDF.parse(toDate)));*/
			
			int actYear; 
			int actMonth;
			
					
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(dtpToDate_Year));
			
			while(true)
			{				
				actYear = Integer.parseInt(dtpToDate_Year.getAttribute("textContent"));
				if(expYear < actYear)
				{
					dtpToDate_PreviousYear.click();
				}
				else if(expYear == actYear)
				{
					break;
				}
			}
			
			
			DateFormat inputDF2  = new SimpleDateFormat("MMM/dd/yy");			
			while(true)
			{		
				//System.out.println(new SimpleDateFormat("MM").format(inputDF2.parse(dtpMonth.getText() +"/01/1900")));	
				new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(dtpToDate_Month));
				actMonth = Integer.parseInt(new SimpleDateFormat("MM").format(inputDF2.parse(dtpToDate_Month.getText() +"/01/1900")));
				
				if(expMonth < actMonth)
				{
					dtpToDate_PreviousMonth.click();
				}
				else if(expMonth > actMonth)
				{
					dtpToDate_NextMonth.click();;
				}
				else if(expMonth == actMonth)
					break;
			}
			
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(By.xpath("(//a[text() = '" + ((expDay>0 && expDay<10) ? "0" + expDay : String.valueOf(expDay)) + "'])[2]"))).click();
			
			//btn_ToDate_OK.click();
		}		
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.WARNING, "Mehtod Name: select_ToDate<br>" + e.getMessage());
		}
		
	}

	
}
