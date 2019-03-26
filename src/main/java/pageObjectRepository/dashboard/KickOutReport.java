package pageObjectRepository.dashboard;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import configuration.WebDriverUtils;

public class KickOutReport 
{
WebDriverUtils utils = new WebDriverUtils();
	
	@FindBy(xpath = "//select[@ng-model='ddlClient']")
	private WebElement ddlClient;
	
	@FindBy(xpath = "//select[@ng-model='ddllocation']")
	private WebElement ddlLocation;
	
	@FindBy(xpath = "//select[@ng-model='ddlPractice']")
	private WebElement ddlPractices;
	
	@FindBy(xpath = "//select[@ng-model='ddlInsuranceKickOut']")
	private WebElement ddlInsurance;
	
	@FindBy(xpath = "//table[@id='DataTables_Table_0']")
	private WebElement tblKickoutRules;
	
	
	public KickOutReport select_Client(String visibleText)
	{
		utils.select(ddlClient, visibleText);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return this;
	}
	
	public KickOutReport select_Location(String visibleText)
	{
		utils.select(ddlLocation, visibleText);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return this;
	}
	
	public KickOutReport select_Practice(String visibleText)
	{
		utils.select(ddlPractices, visibleText);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return this;
	}
	
	public KickOutReport select_Insurance(String visibleText)
	{
		utils.select(ddlInsurance, visibleText);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		return this;
	}
	
	
	public LinkedList<HashMap<String, String>> get_ReportData()
	{
		HashMap<String, String> mapKickoutRules = new HashMap<>();
		LinkedList<HashMap<String, String>> lstRuleDetails = new LinkedList<>();
		
		List<WebElement> kickoutRules = tblKickoutRules.findElements(By.xpath("./tbody/tr"));
		for (WebElement rule : kickoutRules) 
		{
			String desc = rule.findElement(By.xpath("./td[1]")).getAttribute("textContent").trim();
			mapKickoutRules.put("description", desc);
			
			mapKickoutRules.put("totalAccounts", rule.findElement(By.xpath("./td[2]")).getAttribute("textContent").trim());
			mapKickoutRules.put("dollarValue", rule.findElement(By.xpath("./td[3]")).getAttribute("textContent").trim());
			
			
			if(desc!=null) 
			{
				/*ruleUserMapping.put(rule, list of users);*/
				//ruleUserMapping.put(sAllotRule, get_Allocated_Users());
			}
			else
			{
				System.out.println("get_Allocatio_Rule returned null for desc ["+desc+"]");
			}
			
			
			lstRuleDetails.add(mapKickoutRules);
		}
		
		return lstRuleDetails;
	}
}
