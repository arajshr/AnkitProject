package pageObjectRepository.transaction;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.WebDriverUtils;
import customDataType.Transaction;

public class CorrespondanceInbox {
	WebDriverUtils utils = new WebDriverUtils();

	@FindBy(xpath = "//select[@ng-model='ddlPractices']")
	private WebElement ddlPractices;

	@FindBy(xpath = "//select[@ng-model='ddlColumns']")
	private WebElement ddlColumns;

	@FindBy(xpath = "//select[@ng-model='ddlConditions']")
	private WebElement ddlConditions;

	@FindBy(id = "txtValue")
	private WebElement txtValue;

	@FindBy(id = "btnAdd")
	private WebElement btnAdd;

	@FindBy(id = "btnSearch")
	private WebElement btnSearch;

	@FindBy(id = "DataTables_Table_0")
	private WebElement tblCorrespondance;

	@FindBy(xpath = "//select[@ng-model = 'ddlClaimStatus']")
	private WebElement ddlClaimStatus;

	@FindBy(xpath = "//select[@ng-model = 'ddlActionCode']")
	private WebElement ddlActionCode;

	@FindBy(xpath = "//select[@ng-model = 'ddlCallType']")
	private WebElement ddlResolveType;

	@FindBy(xpath = "//textarea[@ng-model = 'Txtnotes']")
	private WebElement txtNotes;

	@FindBy(id = "btnSave")
	private WebElement btnSave;

	@FindBy(xpath = "//div[@id='largeModal']/div/div[1]//button[text() = 'CANCEL']")
	private WebElement btnCancel;

	@FindBy(xpath = "//input[@id='chkNeedCall']")
	private WebElement chkNeedCall;

	@FindBy(id = "txtPhoneno")
	private WebElement txtPhoneno;

	@FindBy(xpath = "//div[@class = 'sweet-alert hideSweetAlert']/h2")
	private WebElement messageHeader;

	@FindBy(xpath = "//div[@class = 'sweet-alert hideSweetAlert']/h2/following-sibling::p")
	private WebElement messageDetails;

	/*
	 * private String sBtnOK =
	 * "//div[@class = 'sweet-alert showSweetAlert visible']//button[text() = 'OK'";
	 */
	@FindBy(xpath = "//div[@class = 'sweet-alert showSweetAlert visible']//button[text() = 'OK']")
	private WebElement btnOK;

	public CorrespondanceInbox add_Search_Condition(String practice, String column, String condition, String value) 
	{
		try 
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			utils.select(ddlPractices, practice, false);
			utils.wait(2000);
			utils.select(ddlColumns, column);
			utils.select(ddlConditions, condition);
			txtValue.sendKeys(value);
			btnAdd.click();
		} 
		catch (Exception e) 
		{
			// TODO: handle exception
		}

		return this;
	}

	public void search(String sUID) 
	{
		btnSearch.click();
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		try 
		{
			utils.wait(2000);
			tblCorrespondance.findElement(By.xpath(".//td[text() = '" + sUID + "']"));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			
		}
	}

	public Transaction open_PatientAccount(String sUID) 
	{
		try 
		{
			utils.wait_Until_InvisibilityOf_LoadingScreen();
			WebElement linkAccount = tblCorrespondance.findElement(By.xpath(".//td[text() = '" + sUID + "']/preceding-sibling::td/a"));
			
			if (utils.isAccountLocked(linkAccount).getStatus() == true) 
			{
				Configuration.logger.log(LogStatus.INFO,"<b>UID [" + sUID + "] - This Account Is Locked By The Other User</b>");
				return new Transaction(false, sUID);
			}

			return new Transaction(true, sUID);
		}
		catch (Exception e) 
		{
			return new Transaction(false, "Class: CorrespondanceInbox, Mehtod: open_PatientAccount <br>" + e.getMessage());
		}
		
	}

	public Transaction submit_Claim_ToJunioCaller(String claimStatus, String actionCode, String notes, String phone) 
	{
		try 
		{
			new WebDriverWait(Configuration.driver, 10).until(
					ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[@ng-model = 'ddlClaimStatus']")));

			WebElement needCall = Configuration.driver.findElement(By.id("chkNeedCall"));
			JavascriptExecutor executor = (JavascriptExecutor) Configuration.driver;
			executor.executeScript("arguments[0].click();", needCall);

			utils.wait(100);
			txtPhoneno.clear();
			txtPhoneno.sendKeys(phone);
			utils.select(ddlClaimStatus, claimStatus);
			utils.select(ddlActionCode, actionCode);

			utils.select(ddlResolveType, "Website");

			txtNotes.clear();
			txtNotes.sendKeys(notes);

			btnSave.click();

			File scrFile = ((TakesScreenshot) Configuration.driver).getScreenshotAs(OutputType.FILE);
			utils.wait_Until_InvisibilityOf_LoadingScreen();

			new WebDriverWait(Configuration.driver, 10).until(
					ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class = 'sweet-overlay']")));
			if (utils.isElementPresent(btnCancel) && btnCancel.isDisplayed()) {
				btnCancel.click();
			}

			if (messageHeader.getAttribute("textContent").equals("ERROR")
					|| messageHeader.getAttribute("textContent").toUpperCase().equals("INFORMATION")) {
				utils.save_ScreenshotToReport("submit_Claim_ToJunioCaller", scrFile);
				btnCancel.click();
				Configuration.logger.log(LogStatus.FAIL,
						messageHeader.getAttribute("textContent") + "_" + messageDetails.getAttribute("textContent"));
			} else if (messageHeader.getAttribute("textContent").equals("success")) {
				return new Transaction(true, phone);
			}

			return new Transaction(false, phone);
		} 
		catch (Exception e) {
			e.printStackTrace();
			return new Transaction(false, phone);
		}
	}
	
	
	
}
