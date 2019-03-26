package pageObjectRepository.qc;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.WebDriverUtils;
import customDataType.Info;

public class QCInbox 
{
	WebDriverUtils utils = new WebDriverUtils();
		
	@FindBy(xpath="//div[@class = 'col-lg-12 col-md-12 col-sm-12 col-xs-12']//button[text() = 'CLOSE']")
	private WebElement btnClose;
	
	@FindBy(xpath = "//select[@ng-model='ddlPractices']")
	private WebElement ddlPractices;
	
	@FindBy(xpath = "//input[@id = 'summaryFromDate']")
	private WebElement txtFromDate;
	
	@FindBy(id="summaryToDate")
	private WebElement txtToDate;
	
	@FindBy(id="btnSearch")
	private WebElement btnSearch;
	
	@FindBy(id="btnGetAccounts")
	private WebElement btnGetAccounts;
	
	
	@FindBy(xpath = "(//div[@class = 'dtp-actual-month p80'])[1]")
	private WebElement dtpMonth;
	
	@FindBy(xpath = "(//a[@class = 'dtp-select-month-before'])[1]")
	private WebElement dtpFromDate_PreviousMonth;
	
	@FindBy(xpath = "(//div[@class = 'dtp-select-month-after'])[1]")
	private WebElement dtpFromDate_NextMonth;
		
	@FindBy(xpath = "(//div[@class = 'dtp-actual-year p80'])[1]")
	private WebElement dtpYear;
	
	@FindBy(xpath = "(//a[@class = 'dtp-select-year-before'])[1]")
	private WebElement dtpFromDate_PreviousYear;
	
	@FindBy(xpath = "(//div[@class = 'dtp-select-year-after'])[1]")
	private WebElement dtpFromDate_NextYear;
	
	
	@FindBy(xpath = "(//button[text() = 'OK'])[1]")
	private WebElement btn_FromDate_OK;
	
	
	@FindBy(xpath = "//div[@id='DataTables_Table_0_filter']//input")
	private WebElement search_Encounter;
	
	@FindBy(xpath = "//table[@id='DataTables_Table_0']")
	private WebElement tblQCA;
	String sTblQCA = "//table[@id='DataTables_Table_0']";
	
	private By panel_body = By.xpath("//div[@class = 'panel-body']");
	private By sweet_overlay = By.xpath("//div[@class = 'sweet-overlay']");
	
	
	
	public void close_Accounts_For_Audit_Popup()
	{
		try 
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(btnClose)).click();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
		/*JavascriptExecutor executor = (JavascriptExecutor)TestConfiguration.driver;
 		executor.executeScript("arguments[0].click();", btnClose);*/
	}
	
	public void select_Search_FromDate(int expYear, int expMonth, int expDay) throws NumberFormatException, ParseException 
	{
		int actYear; 
		int actMonth;
				
		while(true)
		{
			actYear = Integer.parseInt(dtpYear.getAttribute("textContent"));
			if(expYear < actYear)
			{
				new WebDriverWait(Configuration.driver, 5).until(ExpectedConditions.elementToBeClickable(dtpFromDate_PreviousYear)).click();
				//dtpFromDate_PreviousYear.click();
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
			
			actMonth = Integer.parseInt(new SimpleDateFormat("MM").format(inputDF2.parse(dtpMonth.getText() +"/01/1900")));
			
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
		
		String sDay = (expDay>0 && expDay<10) ? "0" + expDay : String.valueOf(expDay);
		
		Configuration.driver.findElement(By.xpath("//a[text() = '" + sDay + "']")).click();
		//btn_FromDate_OK.click();
	}
	
	
	public void filter_QC_Accounts(String practice)
	{
		try 
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			new WebDriverWait(Configuration.driver, 30).until(ExpectedConditions.invisibilityOfElementLocated(panel_body));
	
			utils.select(ddlPractices, practice, false);
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			btnGetAccounts.click();
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(sweet_overlay));
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(btnSearch)).click();
			utils.wait_Until_InvisibilityOf_LoadingScreen();
		} 
		catch (Exception e) 
		{			
			Configuration.logger.log(LogStatus.INFO, "Class: QCInbox, Mehtod: filter_QC_Accounts <br>" + e.getMessage());
		}
		
	}
	
	public void filter_QC_Accounts(String practice, String fromDate)
	{
		try 
		{
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(panel_body));
	
			utils.select(ddlPractices, practice, false);		
			btnGetAccounts.click();
			utils.wait(2000);
			
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(sweet_overlay));
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(txtFromDate)).click();
			
			DateFormat inputDF  = new SimpleDateFormat("MM/dd/yy");
			
			int expMonth = Integer.parseInt(new SimpleDateFormat("MM").format(inputDF.parse(fromDate)));
			int expDay = Integer.parseInt(new SimpleDateFormat("dd").format(inputDF.parse(fromDate)));
			int expYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(inputDF.parse(fromDate)));
			
			//require month/year validation
			select_Search_FromDate(expYear, expMonth, expDay);
			
			new WebDriverWait(Configuration.driver, 5).until(ExpectedConditions.elementToBeClickable(btnSearch)).click();
			utils.wait_Until_InvisibilityOf_LoadingScreen();
		} 
		catch (ParseException e) 
		{			
			Configuration.logger.log(LogStatus.INFO, "Class: QCInbox, Mehtod: filter_QC_Accounts <br>" + e.getMessage());
		}
	}
	
	public boolean open_Encounter_QCA(int index)
	{
		WebElement linkAccount = tblQCA.findElement(By.xpath("//tbody/tr["+ index+"]/td/a"));
		
		if(utils.isAccountLocked_QC(linkAccount) == true)
		{
			Configuration.logger.log(LogStatus.INFO, "<b>This Account Is Locked By The Other User</b>");
			return false;
		}		
			
		return true;
	}
	
	public boolean open_Encounter_QCA(String sUID) 
	{
		// add claim status and action code in search [multiple uids are displayed]
		
		WebElement linkAccount = tblQCA.findElement(By.xpath("//a[text() = '"+ sUID +"']"));
		
 		//WebElement linkAccount = tblQCA.findElement(By.xpath("//td[text() = '"+ encounterID +"']/preceding-sibling::td/a"));
 		//String account = linkAccount.getText();
		if(utils.isAccountLocked_QC(linkAccount) == true)
		{
			Configuration.logger.log(LogStatus.INFO, "<b>This Account Is Locked By The Other User<b>");
			return false;
		}		
			
		return true;
	}
	
	
	public boolean open_Encounter_QCA(String sUID, String sActionCode, String sClaimStatus) 
	{
		WebElement linkAccount = tblQCA.findElement(By.xpath("//td[text()= '"+ sClaimStatus +"']/preceding-sibling::td[text()= '"+ sActionCode +"']/preceding-sibling::td/a[text()= '"+ sUID +"']"));
	
		if(utils.isAccountLocked_QC(linkAccount) == true)
		{
			Configuration.logger.log(LogStatus.INFO, "<b>This Account Is Locked By The Other User<b>");
			return false;
		}		
			
		return true;
	}
	
	

	
	@SuppressWarnings("rawtypes")
	public void verify_Ecounters(Map<String, Long> encounters) 
	{
		ArrayList<String> match = new ArrayList<String>();
		ArrayList<String> mismatch = new ArrayList<String>();
		
		
		Iterator<Entry<String, Long>> it = ((HashMap<String, Long>) encounters).entrySet().iterator();		
	    while (it.hasNext()) 
	    {
	        Map.Entry encounter = (Map.Entry)it.next();	        
	        try
			{
				//utils.turnOffImplicitWaits();
				Configuration.driver.findElement(By.xpath("//td[text() = '"+encounter.getKey().toString()+"']"));
				match.add(encounter.getKey().toString());
			}
			catch (NoSuchElementException e) 
			{
				mismatch.add(encounter.getKey().toString());
			}
			finally 
			{
				//utils.turnOnImplicitWaits();
			}
	        
	    }
		
	    /* Verify Analyst submitted accounts in QC Screen */
	    //Configuration.logger.log(LogStatus.INFO, "Following encounters found in QC Inbox - " + match.toString());
	    //Configuration.logger.log(LogStatus.INFO, match.toString());
		
		if(mismatch.size() > 0)
		{
			Configuration.logger.log(LogStatus.INFO, "Following encounters not found in QC Inbox");
			Configuration.logger.log(LogStatus.WARNING, mismatch.toString());
		}
		
	}

	/***
	 * Retruns true when Encounter is present in QC Inbox else false
	 * @param encounterID
	 * @return boolean value
	 */
	public Info search_Encounter(String encounterID) 
	{
		try 
		{
			search_Encounter.clear();
			search_Encounter.sendKeys(encounterID);
			utils.wait(1000);
			
			if(utils.isElementPresent(tblQCA, By.xpath("//td[text() = '"+encounterID+"']")))
			{
				return new Info(true, "Encounter found");
			}
			
			utils.save_ScreenshotToReport("QCA_search_Encounter");
			return new Info(false, "Encounter ID ["+ encounterID +"] not found at QC inbox");
		}
		catch (Exception e) 
		{
			// TODO: handle exception
		}
		return null;
	}
	
	public Info search_UID(String sUID) 
	{
		try 
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			utils.wait(2000);
			search_Encounter.clear();
			search_Encounter.sendKeys(sUID);
			
			utils.save_ScreenshotToReport("QCInboxSearch");
			
			if(utils.isElementPresent(By.xpath("//a[text() = '"+ sUID +"']"))) 
			{
				return new Info(true, "<b>UID ["+ sUID +"] found in QC Inbox</b>");
			}
			return new Info(false, "<b>UID ["+ sUID +"] not found in QC Inbox</b>");
			
		} 
		catch (Exception e) 
		{
			return new Info(false, "Class: QCInbox, Mehtod: search_UID <br>" + e.getMessage());
		}
	}
	
	
	
	
	public Info search_UID(String sUID, String sActionCode, String sClaimStatus) 
	{
		try 
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			utils.wait(2000);
			search_Encounter.clear();
			search_Encounter.sendKeys(sUID);
			
			
			if(utils.isElementPresent(By.xpath("//td[text()= '"+ sClaimStatus +"']/preceding-sibling::td[text()= '"+ sActionCode +"']/preceding-sibling::td/a[text()= '"+ sUID +"']"))) 
			{				
				utils.highlightElement(tblQCA.findElement(By.xpath("//td[text()= '"+ sClaimStatus +"']/preceding-sibling::td[text()= '"+ sActionCode +"']/preceding-sibling::td/a[text()= '"+ sUID +"']/../..")));				
				utils.save_ScreenshotToReport("QCInboxSearch");
				
				return new Info(true, "<b>UID ["+ sUID +"] found in QC Inbox</b>");
			}
			
			utils.save_ScreenshotToReport("QCInboxSearch");
			return new Info(false, "<b>UID ["+ sUID +"] not found in QC Inbox</b>");
			
		} 
		catch (Exception e) 
		{
			return new Info(false, "Class: QCInbox, Mehtod: search_UID <br>" + e.getMessage());
		}
	}
	
	public String get_Encounter_By_AgentName() 
	{
		return tblQCA.findElement(By.xpath("(.//td[text() = 'VEENA K'])[1]/preceding-sibling::td[1]/a")).getText();
		
	}
	
	public int get_AccountCount()
	{
		return Configuration.driver.findElements(By.xpath("//table[@id='"+tblQCA+"']//a")).size();
	}
	
}
