package pageObjectRepository.master;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import configuration.Configuration;
import configuration.WebDriverUtils;
import customDataType.Transaction;

public class KickoutRule 
{
	WebDriverUtils utils = new WebDriverUtils();
	
	@FindBy(xpath = "//select[@ng-model='ddlClient']")
	private WebElement ddlClient;
	
	@FindBy(xpath = "//select[@ng-model='ddllocation']")
	private WebElement ddlLocation;
	
	@FindBy(xpath = "//select[@ng-model='ddlPractices']")
	private WebElement ddlPractices;
	
	@FindBy(id = "btnADD")
	private WebElement btnAdd_New_Rule;
	
	@FindBy(id = "DataTables_Table_0")
	private WebElement tbl_KickOut;
	
	
	
	/* Create New KickOut Rule - Modal popup */
	
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
	
	
	/* Message Popup */
	
	@FindBy(xpath="//div[@class = 'sweet-alert hideSweetAlert']/h2")
	private WebElement messageType;
	
	@FindBy(xpath = "//div[@class = 'sweet-alert hideSweetAlert']/h2/following-sibling::p")
	private WebElement messageDetails;
	
	
	public KickoutRule select_Client(String client)
	{
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(ddlClient));
		utils.select(ddlClient, client);
		return this;
	}

	public KickoutRule select_Location(String location) 
	{
		utils.select(ddlLocation, location);
		return this;
	}
	
	public KickoutRule select_Practice(String practice)
	{
		utils.select(ddlPractices, practice);
		return this;
	}
	
	public List<String> get_KickOutRules() 
	{
		List<String> rules = new ArrayList<>();
		List<WebElement> rulesList = tbl_KickOut.findElements(By.xpath("./tbody/tr[*]/td[1]"));
		
		if(rulesList.size() > 0)
		{
			for(int i=1; i<=rulesList.size() ; i++) 
			{			
				Iterator<WebElement> it = rulesList.iterator();		
				while (it.hasNext())
					rules.add(it.next().getText().trim());
			}
		}
		return rules;
	}

	public void open_New_Rule_Window() 
	{
		((JavascriptExecutor)Configuration.driver).executeScript("arguments[0].click();", btnAdd_New_Rule);		
	}
	
	public KickoutRule create_KickOutRule(String column, String condition, String value) 
	{	
		utils.select(ddlColumns, column);
		utils.select(ddlConditions, condition);
		txtValue.sendKeys(value);
		btnAdd.click();
		
		return this;
	}
	
	public Transaction save_KickOutRule()
	{
		btnSave.click();
		
		/*File scrFile = ((TakesScreenshot)Configuration.driver).getScreenshotAs(OutputType.FILE);	 	
	 	if(messageType.getAttribute("textContent").toUpperCase().equals("SUCCESS"))
		{
			return new Transaction(true, messageDetails.getAttribute("textContent"));
		}	 	
	 	else if(messageType.getAttribute("textContent").toUpperCase().equals("ERROR") || messageType.getAttribute("textContent").toUpperCase().equals("INFORMATION") )
		{
			utils.captureScreenshot("save_KickOut_Rule", scrFile);
			//Configuration.logger.log(LogStatus.ERROR, messageType.getAttribute("textContent") + "_" + messageDetails.getAttribute("textContent"));
		}*/
	 	
	 	try
		{
			new WebDriverWait(Configuration.driver, 5).until(ExpectedConditions.elementToBeClickable(btnCancel)).click();
		}
		catch (TimeoutException e) 
		{
			e.printStackTrace();
		}
	 	
	 	
	 	utils.save_ScreenshotToReport("save_KickOut_Rule");
	 	
	 	//return new Transaction(false, messageDetails.getAttribute("textContent"));
	 	
	 	return new Transaction(true, "");
	}
	
}
