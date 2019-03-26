package pageObjectRepository.qc;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.WebDriverUtils;

public class QCDashboard 
{
	WebDriverUtils utils = new WebDriverUtils();
	
	@FindBy(linkText = "QC Inbox")
	private WebElement lnkQCInbox;
	
	@FindBy(xpath = "//a[text() = 'Sign Out']") 
	private WebElement lnkSignOut;
	
	public void navigateTo_QCInbox()
	{
		utils.wait_Until_InvisibilityOf_LoadingScreen();
		new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.elementToBeClickable(lnkQCInbox)).click();
	}
	
	public void signOut()
	{
		new WebDriverWait(Configuration.driver, 20).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='sweet-overlay']")));
		
		new WebDriverWait(Configuration.driver, 20).until(ExpectedConditions.elementToBeClickable(lnkSignOut)).click();
		Configuration.logger.log(LogStatus.INFO, "Sign Out");
	}
}
