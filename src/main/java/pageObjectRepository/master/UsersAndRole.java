package pageObjectRepository.master;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.WebDriverUtils;
import customDataType.Info;

public class UsersAndRole 
{
	WebDriverUtils utils = new WebDriverUtils();
	
	@FindBy(xpath = "//a[@title = 'user_role']")
	private WebElement tabUsersAndRoles;
	
	@FindBy(xpath = "//a[@title = 'configure_user']")
	private WebElement tabConfigureUser;
	
	@FindBy(xpath = "//a[@title = 'client_configuration']")
	private WebElement tabClientConfiguration;
	
	@FindBy(xpath = "//a[@title = 'user_practice_mapping']")
	private WebElement tabUserPracticeMapping;
	
	@FindBy(xpath = "//a[@title = 'user_targets']")
	private WebElement tabUserTargets;
	
	
	@FindBy(id ="txtEmpID")
	private WebElement txtEmployeeId;
	
	@FindBy(id ="txtEmpNTLG")
	private WebElement txtEmployeeNTLG;
	
	@FindBy(xpath= "//button [@ng-click='Search()']")
	private WebElement btnSearch;
	
	@FindBy (xpath ="//select [@ng-model ='ddlRole']")
	private WebElement ddlRole;
	
	@FindBy (xpath ="//select [@ng-model ='ddlShift']")
	private WebElement ddlShift;
	
	@FindBy (id = "btnInsert")
	private WebElement btnInsert;
	
	@FindBy(xpath="//div[@class = 'sweet-alert hideSweetAlert']/h2")
	private WebElement messageHeader;
	
	@FindBy(xpath = "//div[@class = 'sweet-alert hideSweetAlert']/h2/following-sibling::p")
	private WebElement messageDetails;
	
	@FindBy(xpath = "//button[text() = 'Yes']")
	private WebElement btnYes;
	
	
	
	@FindBy(xpath="//div[@class = 'sweet-alert hideSweetAlert']/h2")
	private WebElement divSearchMessage;
	
	@FindBy(xpath="//div[@class = 'sweet-alert hideSweetAlert']/p")
	private WebElement pSearchMessage;
		
	
	@FindBy(xpath = "//button[text() = 'OK' and @class = 'confirm']")
	private WebElement btnOK;
	
	
	
	
	
	
	@FindBy(xpath = "//input[@aria-controls = 'DataTables_Table_0']")
	private WebElement txtSearch_ConfigureUser;
	
	@FindBy(xpath = "//button[@title = 'edit']")
	private WebElement btnEdit_User;
	
	@FindBy(xpath = "//button[@title = 'delete']")
	private WebElement btnDelete_User;
	
	@FindBy(xpath = "//button[@title = 'update']")
	private WebElement btnUpdate;
	
	
	
	
	@FindBy (xpath ="//select [@ng-model ='ddlClient']")
	private WebElement ddlClient;
	
	@FindBy (xpath ="//select [@ng-model ='ddlLocation']")
	private WebElement ddlLocation;
	
	@FindBy(xpath= "//button [@ng-click='SearchPratices()']")
	private WebElement btnSearchPratices;
	
	@FindBy(xpath = "//input[@aria-controls = 'DataTables_Table_1']")
	private WebElement txtSearch_Practice;
	
	@FindBy (xpath ="//div[@id = 'profile_with_icon_title']//select [@ng-model ='ddlUser']")
	private WebElement ddlUser_ClientConfig;
	
	@FindBy (id ="btnSubmit")
	private WebElement btnSubmit;
	
	
	
	
	@FindBy(xpath ="//div[@id = 'practice_with_icon_title']//select [@ng-model ='ddlUsers']")
	private WebElement ddlUser_PracticeMapping;
	
	@FindBy(name ="DataTables_Table_1_length")
	private WebElement ddlLength_tblPractice;
	
	@FindBy(xpath = "//input[@aria-controls = 'DataTables_Table_2']")
	private WebElement txtSearch_PracticeMapping;
	
	@FindBy(xpath = "//table[@id='DataTables_Table_2']//button[@title = 'delete']")
	private WebElement btnDelete_PracticeMapping;
	
	
	
	@FindBy(id ="txtID")
	private WebElement txtEmployeeId_UserTargets;
	
	@FindBy(xpath= "//button [@ng-click='SearchUsersTarget()']")
	private WebElement btnSearch_UserTargets;
	
	@FindBy(xpath= "//input[@aria-controls = 'DataTables_Table_3' and @type = 'search']")
	private WebElement txtSearch_UserTargets;
	
		
	@FindBy(xpath ="(//h1[@id='largeModalLabel']/../following-sibling::div//button[text() = 'CANCEL'])[2]")
	private WebElement btnCancel_UserTargetSummary;
	
	@FindBy(id ="txtUserTarget")
	private WebElement txtUserTarget;
	
	@FindBy(id ="FromDate")
	private WebElement txtFromDate_UsertTarget;
	
	@FindBy(id ="TxtAudit")
	private WebElement txtAudit;
	
	@FindBy(id ="btnSave")
	private WebElement btnSave_UserTarget;
	
	@FindBy(id ="(//div[@id='largeModal']//../following-sibling::div//button[text() = 'CANCEL'])")
	private WebElement btnCancel_UserTarget;
	
	
	
	@FindBy(id ="DataTables_Table_0")
	private WebElement tblConfigured_User;
	
	@FindBy(id ="DataTables_Table_1")
	private WebElement tblUser_Client_Config;
	
	@FindBy(id ="DataTables_Table_2")
	private WebElement tblUser_Practice_Mapping;	
	
	@FindBy(id ="DataTables_Table_3")
	private WebElement tblUserTarget;
	
	@FindBy(id ="DataTables_Table_4")
	private WebElement tblSet_UserTarget;
	
	private By page_loader_wrapper = By.xpath ("//div[@class = 'page-loader-wrapper']");
	private By sweet_overlay = By.xpath ("//div[@class = 'sweet-overlay']"); 
	//private By container_fluid = By.xpath ("//div[@class = 'container-fluid']");
	
	
	
	/* DateTime Picker - FollowUp date	*/
	
	@FindBy(xpath = "//div[@class = 'dtp-actual-month p80']")
	private WebElement dtp_Month;
	
	@FindBy(xpath = "//a[@class = 'dtp-select-month-before']")
	private WebElement dtp_PreviousMonth;
	
	@FindBy(xpath = "//a[@class = 'dtp-select-month-after']")
	private WebElement dtp_NextMonth;
	
	
	
	@FindBy(xpath = "//div[@class = 'dtp-actual-year p80']")
	private WebElement dtp_Year;
	
	@FindBy(xpath = "//a[@class = 'dtp-select-year-before']")
	private WebElement dtp_PreviousYear;
	
	@FindBy(xpath = "//a[@class = 'dtp-select-year-after']")
	private WebElement dtp_NextYear;
	
	
	@FindBy(xpath = "//button[text() = 'OK']")
	private WebElement btn_DateOK;
	
	/* ********************************************************* */
	
	
	
	
	public void click_Tab_UsersAndRoles()
	{
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(page_loader_wrapper));
		tabUsersAndRoles.click();
	}
	
	public void click_Tab_ConfigureUser()
	{
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		
		/*new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(page_loader_wrapper));		
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(sweet_overlay));*/
		
		tabConfigureUser.click();
	}
	
	public void click_Tab_UserClientMapping()
	{
		try 
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			utils.scrollWindow(0, -2000);
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(page_loader_wrapper));
			tabClientConfiguration.click();
			
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
		} 
		catch (Exception e) 
		{
			// TODO: handle exception
		}
		
		
	}
	
	public void click_Tab_UserPracticeMapping()
	{		
		try 
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			utils.scrollWindow(0, -2000);
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(page_loader_wrapper));
			tabUserPracticeMapping.click();
			
		} 
		catch (Exception e) 
		{
			// TODO: handle exception
		}
		
	}
	
	public void click_Tab_UserTargets()
	{
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(page_loader_wrapper));
		tabUserTargets.click();
	}
	
	
	
	
	public Info search_EmployeeByEmployeeId(String employeeId)
	{
		txtEmployeeId.sendKeys(employeeId);
		btnSearch.click();
		
		File scrFile = ((TakesScreenshot)Configuration.driver).getScreenshotAs(OutputType.FILE);
				
		if(utils.isElementPresent(pSearchMessage))
		{
			if(pSearchMessage.getAttribute("textContent").contains("User Not Active in Employee Matrix"))
			{
				utils.save_ScreenshotToReport("searchEmployee", scrFile);
				btnOK.click();				
				return new Info(false, messageDetails.getAttribute("textContent"));
			}
		}
		else if(utils.isElementPresent(messageHeader))
		{
			if(messageDetails.getAttribute("textContent").equals("Please Enter Emp ID or Ntlg"))
			{
				utils.save_ScreenshotToReport("searchEmployee", scrFile);				
				return new Info(false, messageDetails.getAttribute("textContent"));
			}		
		}
		return new Info(true, employeeId);
	}
	
	public String search_EmployeeByNTLG(String employeeNTLG)
	{
		txtEmployeeNTLG.sendKeys(employeeNTLG);
		btnSearch.click();
		return employeeNTLG;
	}
	
	
	
	
	public UsersAndRole select_Emloyee(String employeeId)
	{		
		WebElement employee = Configuration.driver.findElement(By.xpath("//input[@value = '" + employeeId + "']"));
		((JavascriptExecutor) Configuration.driver).executeScript("arguments[0].click();", employee);
		
		return this;
	}
	
	public UsersAndRole select_Role(String role)
	{
		utils.select(ddlRole, role);
		return this;		
	}
	
	public UsersAndRole select_Shift(String shift)
	{
		utils.select(ddlShift, shift);
		return this;
	}
	
	public String insert_RoleAndShift()
	{	
		try 
		{

			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(sweet_overlay));
			
			btnInsert.click();
			File scrFile = ((TakesScreenshot)Configuration.driver).getScreenshotAs(OutputType.FILE);
			
			
			try
			{	
				// wait for confirmation popup [yes/no]
				utils.wait(2000);
				btnYes.click();
				
				utils.wait(500);
				utils.save_ScreenshotToReport("RoleSaved");
			}
			catch (TimeoutException e) 
			{
				
				// when no confirmation popup dispalyed, instead some info displayed, capture it
				if(messageHeader.getAttribute("textContent").toUpperCase().equals("INFORMATION") || messageHeader.getAttribute("textContent").toUpperCase().equals("ERROR"))
				{
					utils.save_ScreenshotToReport("UserConfiguration", scrFile);					
					return messageDetails.getAttribute("textContent");
				}
			}
						
			
			utils.wait_Until_InvisibilityOf_LoadingScreen();			
			
			if(utils.isElementPresent(By.xpath("//button[text() = 'OK'")) && btnOK.isDisplayed())
				new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(btnOK)).click();
			
			
			String actMessage = null;
			try 
			{
				actMessage = pSearchMessage.getAttribute("textContent");
				
			} 
			catch (NoSuchElementException e) 
			{
				Configuration.logger.log(LogStatus.ERROR, "<b>No Alert/Popup message displayed</b>");
			}
			
			return actMessage;
			
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return e.getMessage();
		}
		
		
	}
	
	
	
	/**
	 * Searchs user with employee Id
	 * 
	 * @param employeeId
	 * @return boolean
	 */
	public boolean search_ConfiguredUser(String employeeId)
	{
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		
		txtSearch_ConfigureUser.clear();
		txtSearch_ConfigureUser.sendKeys(employeeId);
		
		/*File scrFile = ((TakesScreenshot)Configuration.driver).getScreenshotAs(OutputType.FILE);
		utils.captureScreenshot("USER", scrFile);*/
		
		try
		{
			tblConfigured_User.findElement(By.xpath(".//td[text() = '"+employeeId+"']"));
			return true;
		}
		catch (NoSuchElementException e) 
		{
			//Configuration.logger.log(LogStatus.FAIL, "<b>User not found</b>");
			return false;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return false;
		}
	}
	
	
	public SoftAssert verify_UserConfiguration(String empId, String role, String shift) 
	{
		String actRole =  tblConfigured_User.findElement(By.xpath(".//td[text() = '"+ empId +"']/following-sibling::td[2]/div")).getAttribute("textContent");
		String actShift = tblConfigured_User.findElement(By.xpath(".//td[text() = '"+ empId +"']/following-sibling::td[3]")).getAttribute("textContent").trim();
		
		SoftAssert s = new SoftAssert();
		
		s.assertEquals(actRole,role);	
		s.assertEquals(actShift,shift);
		
		return s;
		
	}
	
	public void update_Role(String employeeId, String role) 
	{
		//Configuration.logger.log(LogStatus.INFO, "Update User role to ["+ role +"]");
		
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(btnEdit_User)).click();
		
		WebElement ddlEdit_Role = tblConfigured_User.findElement(By.xpath(".//td[text() = '"+employeeId+"']/following-sibling::td//select"));		
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(ddlEdit_Role));
		
		utils.select(ddlEdit_Role,role);
		btnUpdate.click();
		
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		
		txtSearch_ConfigureUser.clear();
		txtSearch_ConfigureUser.sendKeys(employeeId);		
		
		File scrFile = ((TakesScreenshot)Configuration.driver).getScreenshotAs(OutputType.FILE);
		
		String actRole = tblConfigured_User.findElement(By.xpath(".//td[text() = '"+employeeId+"']/following-sibling::td[2]")).getText();
		Assert.assertEquals(actRole.trim(), role, "<b>Role not updated</b>");
		Configuration.logger.log(LogStatus.INFO, "<b>Role updated to ["+ role +"]</b>");
		
		utils.save_ScreenshotToReport("RoleAfterUpdate", scrFile);
		
	}
	
	
	public String remove_User(String empID) 
	{
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(btnDelete_User)).click();
		
		
		try 
		{
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.alertIsPresent());
			Alert aDelete = Configuration.driver.switchTo().alert();			
			aDelete.accept();
			
			utils.save_ScreenshotToReport("DeleteUser");
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			
			return messageDetails.getAttribute("textContent");
			
		} 
		catch (Exception e)
		{
			Configuration.logger.log(LogStatus.ERROR, "Method: remove_User <br>" + e.getMessage());
			return null;
		}
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	public UsersAndRole select_Client(String client)
	{			
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		utils.select(ddlClient, client);
		return this;
	}
	
	public UsersAndRole select_Location(String location)
	{	
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		utils.select(ddlLocation, location);
		return this;
	}
	
	public String search_Practice(String practice) 
	{
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		
		utils.wait(1000);
		btnSearchPratices.click();
		
		/*File scrFile = ((TakesScreenshot)Configuration.driver).getScreenshotAs(OutputType.FILE);
		
		if(utils.isElementPresent(messageHeader))
		{
			if(messageHeader.getAttribute("textContent").equals("ERROR"))
			{
				utils.save_ScreenshotToReport("practice", scrFile);
				Configuration.logger.log(LogStatus.INFO, messageHeader.getAttribute("textContent") + "_" + messageDetails.getAttribute("textContent"));
				return messageDetails.getAttribute("textContent");
			}
		}*/
		
		return practice;
		
	}
	
	public UsersAndRole select_Practice(String practice)
	{	
		txtSearch_Practice.sendKeys(practice);	
		
		WebElement chkPractice = tblUser_Client_Config.findElement(By.xpath(".//td/input[@value = '" + practice + "']"));
		((JavascriptExecutor) Configuration.driver).executeScript("arguments[0].click();", chkPractice);
		
		return this;
	}
	
	public UsersAndRole select_UserForPractice(String user) 
	{
		utils.wait(2000);
		
		utils.select(ddlUser_ClientConfig, user);
		return this;
	}
	
	public Info submit_UserClientConfiguration()
	{
		try 
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			
			btnSubmit.click();
			utils.wait(1000);
			
			
			
			try
			{			
				new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(btnYes)).click();	
				
				utils.save_ScreenshotToReport("SaveUserConfig");
			}
			catch (TimeoutException e) 
			{
				if(messageHeader.getAttribute("textContent").toUpperCase().equals("INFORMATION") || messageHeader.getAttribute("textContent").toUpperCase().equals("ERROR"))
				{					
					return new Info(false, messageDetails.getAttribute("textContent"));
				}
			}
			
			
			
			if(utils.isElementPresent(btnOK) && btnOK.isDisplayed())
			{
				btnOK.click();
			}
			
			
			if(utils.isElementPresent(messageHeader))
			{
				if(messageHeader.getAttribute("textContent").toUpperCase().equals("SUCCESS"))
				{			
					return new Info(true, messageDetails.getAttribute("textContent"));							
				}
				else 
				{			
					return new Info(false, messageDetails.getAttribute("textContent"));
				}
			}
			else
			{
				Configuration.logger.log(LogStatus.WARNING, "<b>No alert message is displayed after submitting the mapping</b>");
				return new Info(true, "");				
			}
		
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
		
		
	}
	
	public Info verify_UserPracticeMapping(String user, String practice)
	{	
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		
		utils.select(ddlUser_PracticeMapping, 1);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		
		utils.select(ddlUser_PracticeMapping, user);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		
		txtSearch_PracticeMapping.sendKeys(practice);
		
		utils.save_ScreenshotToReport("USER_PracticeMapping_search");
		
		try 
		{
			if(utils.isElementPresent(tblUser_Practice_Mapping, By.xpath(".//td[text() = '"+ practice +"']")))
			{
				return new Info(true, "<b>Practice mapping verified for User ["+user+"]</b>");
			}
			return new Info(false, "<b>User Practice mapping not found for User ["+user+"]</b>");
		}
		catch (Exception e) 
		{
			return new Info(false, "<b>Practice ["+ practice +"] not found</b>");
		}
	}
	
	
	public void search_UserPracticeMapping(String user)
	{	
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		
		utils.select(ddlUser_PracticeMapping, 1);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		
		utils.select(ddlUser_PracticeMapping, user);
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		
		utils.save_ScreenshotToReport("User_Client_Mapping");
	}

	public void delete_UserPracticeMapping(String practice) 
	{
		utils.wait(1000);
		txtSearch_PracticeMapping.clear();
		txtSearch_PracticeMapping.sendKeys(practice);
		
		
		if(utils.isElementPresent(btnDelete_PracticeMapping) && btnDelete_PracticeMapping.isDisplayed())
		{
			btnDelete_PracticeMapping.click();
			Configuration.logger.log(LogStatus.INFO, "Search and delete practice ["+ practice +"]");
			
			Alert a = utils.isAlertPresent();
			if(a != null)
			{
				a.accept();
			}
		}
		else
		{
			utils.save_ScreenshotToReport("USER_Delete");
			Configuration.logger.log(LogStatus.INFO, "<b>Practice ["+ practice +"] not found</b>");
		}
	}

	
	
	
	
	
	
	
	public void search_UserTargetByEmployeeId(String employeeID) 
	{
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		
		txtEmployeeId_UserTargets.clear();
		txtEmployeeId_UserTargets.sendKeys(employeeID);
	
		btnSearch_UserTargets.click();
	}

	public Info set_UserTarget(String practice, String target, String auditPercent, String fromDate) 
	{
		try 
		{
			utils.wait(2000);
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(txtSearch_UserTargets));
			
			txtSearch_UserTargets.clear();
			txtSearch_UserTargets.sendKeys(practice);
			
			Configuration.driver.findElement(By.linkText("Set new User Target")).click();
			
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(txtUserTarget));
			txtUserTarget.clear();
			txtUserTarget.sendKeys(target);
			
			txtAudit.clear();
			txtAudit.sendKeys(auditPercent);
			
			
			
			
			txtFromDate_UsertTarget.click();
			select_Date(fromDate);
					
			btnSave_UserTarget.click();	
			utils.wait(200);
			
			File scrFile1 = ((TakesScreenshot)Configuration.driver).getScreenshotAs(OutputType.FILE);	
			utils.save_ScreenshotToReport("CLIENT", scrFile1);
			
			utils.wait(1000);			

			if(utils.isElementPresent(btnOK) && btnOK.isDisplayed())
			{
				btnOK.click();
			}
			
			if(utils.isElementPresent(btnCancel_UserTarget) && btnCancel_UserTarget.isDisplayed())
			{
				btnCancel_UserTarget.click();
			}
		
			
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			if(messageHeader.getAttribute("textContent").toUpperCase().equals("SUCCESS"))
			{			
				return new Info(true, "<b>User target saved successfully</b>");								
			}
			else 
			{							
				return new Info(false, messageDetails.getAttribute("textContent"));
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
		
	}
	
	public void modify_UserTarget(String practice, String target, String auditPercent, String targetFromDate) 
	{
		try
		{
			utils.wait(1000);
			txtSearch_UserTargets.clear();
			txtSearch_UserTargets.sendKeys(practice);		
			Configuration.driver.findElement(By.linkText("User Target")).click();
					
			
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(tblSet_UserTarget));
			tblSet_UserTarget.findElement(By.xpath(".//td[7]/div[text() = '" + targetFromDate + "']/../following-sibling::td//button[text() = 'Modify']")).click();
			
						
			String xpathTarget = ".//td[7]/div[text() = '" + targetFromDate + "']/../../td[" + utils.getColumnIndex(tblSet_UserTarget, "Target") + "]//input";		
			tblSet_UserTarget.findElement(By.xpath(xpathTarget)).clear();
			tblSet_UserTarget.findElement(By.xpath(xpathTarget)).sendKeys(target);
			
			
			String xpathAudit = ".//td[7]/div[text() = '" + targetFromDate + "']/../../td[" + utils.getColumnIndex(tblSet_UserTarget, "Audit Percent") + "]//input";
			tblSet_UserTarget.findElement(By.xpath(xpathAudit)).clear();
			tblSet_UserTarget.findElement(By.xpath(xpathAudit)).sendKeys(auditPercent);
			
			
			tblSet_UserTarget.findElement(By.xpath(".//td[7]/div[text() = '" + targetFromDate + "']/../following-sibling::td//button[text() = 'Update']")).click();
			
			utils.wait(100);
			utils.save_ScreenshotToReport("UpdateTrarget");
			
			btnCancel_UserTargetSummary.click();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
	}

	public boolean verify_UserTarget(String practice, String target, String auditPercent, String fromDate) 
	{
		try
		{	
			utils.wait(2000);
			txtSearch_UserTargets.clear();
			txtSearch_UserTargets.sendKeys(practice);		
			Configuration.driver.findElement(By.linkText("User Target")).click();
			
			By xpath = By.xpath("//td/div[text() = '" + fromDate + "']/../preceding-sibling::td/div[text() = '"+auditPercent+"']/../preceding-sibling::td/div[text() = '"+target+"']");
			
			
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(tblUserTarget));
			utils.wait(2000);
			utils.save_ScreenshotToReport("UserTarget");
			
			if(utils.isElementPresent(xpath))
			{
				btnCancel_UserTargetSummary.click();
				return true;
			}
			
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.visibilityOf(btnCancel_UserTargetSummary)).click();
			return false;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return false;
		}
		
		
		
	}

	
	public void select_Date(String date)
	{
		try 
		{
			DateFormat inputDF  = new SimpleDateFormat("MM/dd/yy");
			
			int expMonth = Integer.parseInt(new SimpleDateFormat("MM").format(inputDF.parse(date)));
			int expDay = Integer.parseInt(new SimpleDateFormat("dd").format(inputDF.parse(date)));
			int expYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(inputDF.parse(date)));
						
			
			int actYear; 
			int actMonth;
			
					
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(dtp_Year));
			
			while(true)
			{				
				actYear = Integer.parseInt(dtp_Year.getAttribute("textContent"));
				if(expYear < actYear)
				{
					dtp_PreviousYear.click();
				}
				if(expYear > actYear)
				{
					dtp_NextYear.click();
				}
				else if(expYear == actYear)
				{
					break;
				}
			}
			
			
			DateFormat inputDF2  = new SimpleDateFormat("MMM/dd/yy");			
			while(true)
			{	
				new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(dtp_Month));
				actMonth = Integer.parseInt(new SimpleDateFormat("MM").format(inputDF2.parse(dtp_Month.getText() +"/01/1900")));
				
				if(expMonth < actMonth)
				{
					dtp_PreviousMonth.click();
				}
				else if(expMonth > actMonth)
				{
					dtp_NextMonth.click();;
				}
				else if(expMonth == actMonth)
					break;
			}
			
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text() = '" + ((expDay>0 && expDay<10) ? "0" + expDay : String.valueOf(expDay)) + "']"))).click();
			
			//btn_DateOK.click();
		}		
		catch (Exception e) {
			Configuration.logger.log(LogStatus.WARNING, "Mehtod Name: select_Date<br>" + e.getMessage());
		}
	}

	public void verify_TargetDates(String practice) throws ParseException 
	{
		
		utils.wait(1000);
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@aria-controls = 'DataTables_Table_3' and @type = 'search']")));
		
		txtSearch_UserTargets.clear();
		txtSearch_UserTargets.sendKeys(practice);		
		Configuration.driver.findElement(By.linkText("User Target")).click();
		
		
		//sort dates in desc order
		
		utils.wait(2000);
		Configuration.driver.findElement(By.xpath("//table[@id='DataTables_Table_4']/thead/tr/th[text() = 'From Date']")).click();
		Configuration.driver.findElement(By.xpath("//table[@id='DataTables_Table_4']/thead/tr/th[text() = 'From Date']")).click(); // click twice for desc order
				
		utils.save_ScreenshotToReport("OverlappingDates");
		btnCancel_UserTargetSummary.click();
		
		
		
		
		//get target , dates
		
		ArrayList<ArrayList<String>> lstTargets = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");		
		StringBuilder errorMessage = new StringBuilder();
		
		
		List<WebElement> targetRows = Configuration.driver.findElements(By.xpath("//table[@id='DataTables_Table_4']/tbody/tr"));
				
		for (WebElement webElement : targetRows) 
		{
			ArrayList<String> temp = new ArrayList<>();
			
			String sTarget = webElement.findElement(By.xpath("./td[5]")).getAttribute("textContent").trim();
			String sFromDate = webElement.findElement(By.xpath("./td[7]")).getAttribute("textContent").trim();
			String sToDate = webElement.findElement(By.xpath("./td[8]")).getAttribute("textContent").trim();
			
			
			temp.add(sTarget);
			temp.add(sFromDate);
			temp.add(sToDate);
			
		
			lstTargets.add(temp);
		}
		
		
		
		
		for(int i=0; i<lstTargets.size();i++) // start from second row
		{
			Date start1 = sdf.parse(lstTargets.get(i).get(1));
			Date end1 = sdf.parse(lstTargets.get(i).get(2));
			String target1 = lstTargets.get(i).get(0);
			
			
			if(end1.before(start1))
            {			
				errorMessage.append("ToDate ["+ sdf.format(end1) +"] is less than FromDate["+sdf.format(start1)+"] for Target ["+target1+"]<br>");
            	System.out.println("ToDate ["+ sdf.format(end1) +"] is less than FromDate["+sdf.format(start1)+"] for Target ["+target1+"]");
            	continue; 
            }
			
			
			for(int j=i+1;j<lstTargets.size();j++) 
			{
				Date start2 = sdf.parse((lstTargets.get(j)).get(1));
				Date end2 = sdf.parse((lstTargets.get(j)).get(2)); //previous todates
				String target2 = lstTargets.get(j).get(0);
				
				
				
				//System.out.println("start1:"+sdf.format(start1)+" , end1:"+sdf.format(end1)+" , start2:"+sdf.format(start2)+" end2:"+sdf.format(end2));
				
				
				if((start1.before(start2) && end1.after(start2)) ||
					(start1.before(end2) && end1.after(end2)) ||
					(start1.before(start2) && end1.after(end2)) ||
					(start1.after(start2) && end1.before(end2)) ||
					(start1.equals(start2)) || (end1.equals(end2)) )
				{
					
					if(!target1.equals(target2))
					{
						errorMessage.append("Date ranges [" + sdf.format(start1) + " - " + sdf.format(end1) +"] and [" + sdf.format(start2) + " - " + sdf.format(end2) +"] overlap with differnt targets <br>");
						System.out.println("Date ranges [" + sdf.format(start1) + " - " + sdf.format(end1) +"] and [" + sdf.format(start2) + " - " + sdf.format(end2) +"] overlap with differnt targets ");
					}
				}
				
			}
			
		}
		
		
		String sError = errorMessage.toString();
		
		if (errorMessage.length() > 0)
		{
			
			//Configuration.logger.log(LogStatus.INFO, sError);
			Assert.fail("Assertion failed. <br>" + sError);
		}
		else
		{
			Configuration.logger.log(LogStatus.INFO, "User Target dates verified");
		}
		
	}

	public String get_CurrentRoleFromConfiguredUser(String sEmployeeID) 
	{
		try 
		{
			return Configuration.driver.findElement(By.xpath("//table[@id='DataTables_Table_0']//td[text() = '21638']/following-sibling::td[2]")).getAttribute("textContent");
		}
		catch (Exception e) 
		{
			return "";
		}
		
	}
	

	
	

}
