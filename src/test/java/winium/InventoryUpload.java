package winium;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.winium.DesktopOptions;

import org.openqa.selenium.winium.WiniumDriver;
import org.openqa.selenium.winium.WiniumDriverService;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.Constants;
import configuration.WebDriverUtils;
import customDataType.Info;
import winium.elements.desktop.ComboBox;

public class InventoryUpload 
{
	WebDriverUtils utils = new WebDriverUtils();
	WiniumDriver wdriver;
	WiniumDriverService service;
	DesktopOptions option;
	
	public InventoryUpload() 
	{
		try 
		{
			option = new DesktopOptions();
			option.setApplicationPath("C:\\ATOM_RCM_Inventory_Module\\setup.exe");

			service = new WiniumDriverService.Builder()
					.usingDriverExecutable(new File("C:\\SelenuimDrivers\\Winium.Desktop.Driver.exe")).usingPort(9999)
					.withVerbose(true).withSilent(false).buildDesktopService();

			service.start();
		}
		catch (Exception e) 
		{
			//e.printStackTrace();
		}
		finally 
		{
			
		}
	}
	
	public Info upload(String sInventoryType, String sClient, String sLocation, String sPractice, String sFilePath) 
	{
		try 
		{		
			wdriver = new WiniumDriver(service, option); //new URL("http://localhost:9999")
			utils.wait(2000);
			
			
			// close popup
			wdriver.findElement(By.id("CommandLink_2")).click();
			
					
			
			
			// select Inventory type
			ComboBox cboInventory = new ComboBox(wdriver.findElement(By.id("cboInventory")));
			cboInventory.expand();
			cboInventory.findElement(By.name(sInventoryType)).click();  //"Priority Report"
			
			
			
			// select client
			ComboBox cboClient = new ComboBox(wdriver.findElement(By.id("cboClient")));
			cboClient.expand();
			cboClient.findElement(By.name(sClient)).click();

			
			
			// select location
			ComboBox cboLocation = new ComboBox(wdriver.findElement(By.id("cboLocation")));
			cboLocation.expand();
			
			WebElement wbSelect = cboLocation.findElement(By.name("--Select--"));
			wbSelect.click();
			
			wbSelect.sendKeys("ED");			
			
			
			
			// select practice
			ComboBox cboPractice = new ComboBox(wdriver.findElement(By.id("cboPractice")));
			cboPractice.expand();
			cboPractice.findElement(By.name(sPractice)).click();
			
			
			
			
			// click browse button to open priority file
			WebElement btnBrowse = wdriver.findElement(By.id("btnBrowse"));
			btnBrowse.click();
			
			// enter file name and click save
			wdriver.findElement(By.name("File name:")).sendKeys(sFilePath);
			wdriver.findElement(By.name("Open")).click();
			
			
			WebElement btnUpload = wdriver.findElement(By.id("btnUpload"));
			btnUpload.click();
			utils.wait(2000);
			
			
			
			// validate popup message
			WebElement wbAlert = new WebDriverWait(wdriver, 30).until(ExpectedConditions.presenceOfElementLocated(By.id("TitleBar")));
			
			while(true)
			{
				String sTitle = wbAlert.getAttribute("Name");				
				String sAlertText;
				

				
				
				if(sTitle.trim().toLowerCase().equals("required"))
				{
					sAlertText = wdriver.findElement(By.id("65535")).getAttribute("Name");
					save_ScreenshotToReport("PriorityUpload_ERROR");
					
					// close the alert
					new WebDriverWait(wdriver, 15).until(ExpectedConditions.elementToBeClickable(By.name("OK"))).click();
					
					return new Info(false, sTitle + "  " + sAlertText);
				}
				else if(sTitle.trim().toLowerCase().equals("upload success"))
				{
					sAlertText = wdriver.findElement(By.id("65535")).getAttribute("Name");			
					save_ScreenshotToReport("PriorityUpload");
					
					// close the alert
					new WebDriverWait(wdriver, 15).until(ExpectedConditions.elementToBeClickable(By.name("OK"))).click();
					
					return new Info(true, sTitle + "  " + sAlertText);
				}
				else
				{
					System.out.println("Waiting");
					utils.wait(2000);
					
					wbAlert = new WebDriverWait(wdriver, 30).until(ExpectedConditions.presenceOfElementLocated(By.id("TitleBar")));
				}
			}
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Info(false, "Class: InventoryUpload, Mehtod: uploadPriority <br>" + e.getMessage());
		}
		finally 
		{
			
			// close application
			wdriver.findElement(By.id("Close")).click();
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	public void save_ScreenshotToReport(String methodName) 
	{
		try 
		{
			SimpleDateFormat reportDateFormat = new SimpleDateFormat("HH_mm_ss");			
			methodName = methodName + "_" + reportDateFormat.format(new Date());
			
			
			File srcFile = ((TakesScreenshot)wdriver).getScreenshotAs(OutputType.FILE);		
			File destFile = new File(Constants.screenShotFolderPath + methodName +  ".png");			
			FileUtils.copyFile(srcFile, destFile);
						
			
			String screenPath = new File(Constants.screenShotFolderPath + methodName +  ".png").getAbsolutePath();			
			Configuration.logger.log(LogStatus.INFO, Configuration.logger.addScreenCapture(screenPath));
		} 
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, "Class: WebDriverUtils, Mehtod: save_ScreenshotToReport <br>" + e.getMessage());
		}		
	}
	
	
	
	
	
	
	

	/*public static void main(String[] args) throws InterruptedException, IOException {

		

		// close popup
		driver.findElement(By.id("CommandLink_2")).click();

		
		
		// select dropdown values
		ComboBox cboInventory = new ComboBox(driver.findElement(By.id("cboInventory")));
		cboInventory.expand();
		cboInventory.findElement(By.name("Priority Report")).click();
		
		ComboBox cboClient = new ComboBox(driver.findElement(By.id("cboClient")));
		cboClient.expand();
		cboClient.findElement(By.name("MedData Incorporated")).click();

		ComboBox cboLocation = new ComboBox(driver.findElement(By.id("cboLocation")));
		cboLocation.expand();
		cboLocation.findElement(By.name("ED")).click();

		ComboBox cboPractice = new ComboBox(driver.findElement(By.id("cboPractice")));
		cboPractice.expand();
		cboPractice.findElement(By.name("WES")).click();
		
		
		WebElement txtFilePath = driver.findElement(By.id("txtFilePath"));
		txtFilePath.sendKeys(keysToSend);
		
		
		WebElement btnUpload = driver.findElement(By.id("btnUpload"));
		btnUpload.click();
		
		
		

		
		
		
		// set from date if required
		// driver.findElement(By.id("dtpFromDate")).sendKeys("2018/09/20");

		// find the listview
		WebElement lsvInventory = driver.findElement(By.id("lsvInventory"));

		// find the listitem with givern file name
		WebElement lstItem = lsvInventory.findElement(By.name("Allocation Bucketing"));

		// reght click and select export option
		Actions act = new Actions(driver);
		act.contextClick(lstItem).build().perform();
		driver.findElement(By.name("Export Error Records")).click();

		
		// save error file navigate to
		String sErrorFilePath =  "E:\\SVN\\branches\\ATI\\src\\test\\resources\\InventoryErrorLogs\\";
		
		// enter file name and click save
		driver.findElement(By.name("File name:")).sendKeys(sErrorFilePath + "AllocationError.xlsx");
		driver.findElement(By.name("Save")).click();
		
		new WebDriverWait(driver, 15).until(ExpectedConditions.elementToBeClickable(By.name("OK"))).click();
		
		
		driver.findElement(By.id("Close")).click();
	}*/

}