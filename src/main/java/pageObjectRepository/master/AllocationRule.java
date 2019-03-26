package pageObjectRepository.master;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.WebDriverUtils;

public class AllocationRule 
{
	WebDriverUtils utils = new WebDriverUtils();
	
	@FindBy(xpath = "//a[contains(text(), 'Allot Rule')]")
	private WebElement tabAllotRule;
	
	@FindBy(xpath = "//a[contains(text(), 'User Mapping')]")
	private WebElement tabUserMapping;
	
	
	/* Elements in Allot Rule Tab */
	
	@FindBy(xpath = "(//select[@ng-model='ddlClient'])[1]")
	private WebElement ddlClient_AllotRule;
	
	@FindBy(xpath = "(//select[@ng-model='ddllocation'])[1]")
	private WebElement ddlLocation_AllotRule;
	
	@FindBy(xpath = "(//select[@ng-model='ddlPractices'])[1]")
	private WebElement ddlPractices_AllotRule;
	
	@FindBy(id = "btnADD")
	private WebElement btnAddNewRule;
	
	
	/* Create New Allotment Rule - Modal popup */
	
	@FindBy(id = "ddlAllocationRuleFor")
	private WebElement ddlAllocationRuleFor;
	
	@FindBy(xpath = "//select[@ng-model='ddlColumns']")
	private WebElement ddlColumns;
	
	@FindBy(xpath = "//select[@ng-model='ddlConditions']")
	private WebElement ddlConditions;
	
	@FindBy(id = "txtValue")
	private WebElement txtValue;
	
	@FindBy(id = "btnAdd")
	private WebElement btnAdd;
	
	@FindBy(id = "btnSave")
	private WebElement btnSave;
	
	@FindBy(xpath = "//button[text() = 'CANCEL']")
	private WebElement btnCancel;
	
	
	/* Elements in Uer Mapping Tab */
	
	@FindBy(xpath = "(//select[@ng-model='ddlClient'])[2]")
	private WebElement ddlClient_UserMapping;
	
	@FindBy(xpath = "(//select[@ng-model='ddllocation'])[2]")
	private WebElement ddlLocation_UserMapping;
		
	@FindBy(xpath = "(//select[@ng-model='ddlPractices'])[2]")
	private WebElement ddlPractices_UserMapping;	
	
	@FindBy(xpath = "//select[@ng-model='ddlAllotRule']")
	private WebElement ddlAllotRule;
	
	
	/* Message Popup */
	
	@FindBy(xpath="//div[@class = 'sweet-alert hideSweetAlert']/h2")
	private WebElement messageType;
	
	@FindBy(xpath = "//div[@class = 'sweet-alert hideSweetAlert']/h2/following-sibling::p")
	private WebElement messageDetails;
	
	
	public void click_Tab_Allot_Rule()
	{	
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class = 'page-loader-wrapper']")));
		tabAllotRule.click();
	}
	
	public void click_Tab_User_Mapping()
	{	
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class = 'page-loader-wrapper']")));
		tabUserMapping.click();
	}
	
	
	
	public AllocationRule select_ClientForAllotRule(String client)
	{
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(ddlClient_AllotRule));
		utils.select(ddlClient_AllotRule, client);
		return this;
	}

	public AllocationRule select_LocationForAllotRule(String location) 
	{
		utils.select(ddlLocation_AllotRule, location);
		return this;
	}
	
	public void select_PracticeForAllotRule(String practice)
	{
		utils.select(ddlPractices_AllotRule, practice);		
	}
		
	public void open_New_Allot_Rule_Window()
	{		
		/*((JavascriptExecutor)Configuration.driver).executeScript("window.scrollTo(0,document.body.scr‌​ollHeight);");*/
		
		JavascriptExecutor executor = (JavascriptExecutor)Configuration.driver;
 		executor.executeScript("arguments[0].click();", btnAddNewRule);
 		
		//btnAddNewRule.click();
	}
	
	public AllocationRule create_Allot_Rule(String ruleFor, String column, String condition, String value) 
	{		
		utils.select(ddlAllocationRuleFor, ruleFor);
		utils.select(ddlColumns, column);
		utils.select(ddlConditions, condition);
		txtValue.sendKeys(value);
		btnAdd.click();
		
		return this;
	}
	
	public boolean save_Allot_Rule()
	{
		btnSave.click();
		
		File scrFile = ((TakesScreenshot)Configuration.driver).getScreenshotAs(OutputType.FILE);
	 	
	 	if(messageType.getAttribute("textContent").toUpperCase().equals("SUCCESS"))
		{
			return true;
		}	 	
	 	else if(messageType.getAttribute("textContent").equals("ERROR"))
		{
			utils.save_ScreenshotToReport("save_Allot_Rule", scrFile);
			Configuration.logger.log(LogStatus.ERROR, messageType.getAttribute("textContent") + "_" + messageDetails.getAttribute("textContent"));
			
			try
			{
				new WebDriverWait(Configuration.driver, 5).until(ExpectedConditions.elementToBeClickable(btnCancel)).click();
			}
			catch (TimeoutException e) 
			{
				e.printStackTrace();
			}
		}				
		return false;
	}
	

	
	
	public AllocationRule select_ClientForUserMapping(String client)
	{
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(ddlClient_UserMapping));
		utils.select(ddlClient_UserMapping, client);
		return this;
	}

	public AllocationRule select_LocationForUserMapping(String location) 
	{
		utils.select(ddlLocation_UserMapping, location);
		return this;
	}

	public AllocationRule select_PracticeForUserMapping(String practice)
	{
		utils.select(ddlPractices_UserMapping, practice);
		return this;		
	}
	
	public AllocationRule select_AllocationRuleForUserMapping(String practice)
	{
		utils.select(ddlAllotRule, practice);
		return this;		
	}

	public String get_Selected_AllocationRule(int allotRuleIndex)
	{
		utils.select(ddlAllotRule, allotRuleIndex);
		return utils.get_SelectedOption(ddlAllotRule);
	}
	
	
	
	
	public int get_AllocationRule_Count()
	{
		return new Select(ddlAllotRule).getOptions().size();		
	}
	
	
	public List<String> get_Allocated_Users() 
	{				
		List<WebElement> webElementUser = Configuration.driver.findElements(By.xpath("//table[@id='DataTables_Table_1']/tbody/tr[*]/td[3]"));		
		ArrayList<String> users = new ArrayList<String>();
				
		if(webElementUser.size() > 0)
		{
			Iterator<WebElement> it = webElementUser.iterator();		
			while (it.hasNext())
				users.add(it.next().getText());
		}
		return	users;
	}

	public HashMap<String, List<String>> get_AllocationRules_Mapped_Users() 
	{
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		
		HashMap<String, List<String>> ruleUserMapping = new HashMap<String, List<String>>();		
		List<WebElement> ruleDescription = new Select(ddlAllotRule).getOptions();
		
		
		for (int i = 1; i < ruleDescription.size(); i++) 
		{
			
			utils.select(ddlAllotRule, ruleDescription.get(i).getText());
			System.out.println(ruleDescription.get(i).getText());
			
			String sAllotRule = Configuration.db.get_Allocation_Rule(ruleDescription.get(i).getText());
			
			if(sAllotRule!=null) 
			{
				//ruleUserMapping.put(rule, list of users);
				ruleUserMapping.put(sAllotRule, get_Allocated_Users());
			}
			else
			{
				System.out.println("get_Allocatio_Rule returned null for desc ["+ ruleDescription.get(i).getText() +"]");
			}
		}
		return ruleUserMapping;
	}

	

}
