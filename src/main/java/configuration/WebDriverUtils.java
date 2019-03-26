package configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.relevantcodes.extentreports.LogStatus;

import customDataType.Info;
import customDataType.Transaction;
import excelLibrary.ExcelReader;
import pageObjectRepository.transaction.ClaimTransaction;

public class WebDriverUtils 
{
	
	/**
     * XPath ignore case
     * @param attribute - String attribute to convert, i.e. @value, text(), etc.
     * @param value - String value to check, i.e. 'Cancel'
     * @return String formatted section of XPath to match string ignoring case
     *         in the format: translate(attribute, 'ABC...', 'abc...') = 'value')
     */
    public String ignoreCaseEqualsXPath(String attribute, String value) {
    	
    	return "translate(" + attribute + ", 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz') = '" + value.toLowerCase() + "'";
    	
    }
    
    
    
    public String getToday() 
	{
    	try
    	{
    		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");    		
    	    return sdf.format(new Date());
		}
    	catch (Exception e)
    	{
    		Configuration.logger.log(LogStatus.INFO, "Class: WebDriverUtils, Mehtod: get_TodayDate  <br>" + e.getMessage());
		}
		return null;
	    
	    
	}
    
	public String getYesterday() 
	{
		try
    	{
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");    	
			
    		final Calendar cal = Calendar.getInstance();
    	    cal.add(Calendar.DATE, -1);
    	    return sdf.format(cal.getTime());
		}
    	catch (Exception e)
    	{
    		Configuration.logger.log(LogStatus.INFO, "Class: WebDriverUtils, Mehtod: get_YesterdayDate  <br>" + e.getMessage());
		}
		return null;	    
	}
	
	public String getNextDay() 
	{
		try
    	{
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");    	
			
    		final Calendar cal = Calendar.getInstance();
    	    cal.add(Calendar.DATE, +1);
    	    return sdf.format(cal.getTime());
		}
    	catch (Exception e)
    	{
    		Configuration.logger.log(LogStatus.INFO, "Class: WebDriverUtils, Mehtod: get_YesterdayDate  <br>" + e.getMessage());
		}
		return null;	    
	}
	
	
	public String getDate(int daysToAdd) 
	{
		try
    	{
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");    	
			
    		final Calendar cal = Calendar.getInstance();
    	    cal.add(Calendar.DATE, daysToAdd);
    	    return sdf.format(cal.getTime());
		}
    	catch (Exception e)
    	{
    		Configuration.logger.log(LogStatus.INFO, "Class: WebDriverUtils, Mehtod: get_YesterdayDate  <br>" + e.getMessage());
		}
		return null;	    
	}
	
	
	
	
	public void moveMousePointerTo(WebElement element)
	{
		Actions act = new Actions(Configuration.driver);
	 	act.moveToElement(element).build().perform();
	}
	
	public void select(WebElement element, String visibleText)
	{
		select(element, visibleText, true);
		
	}
	
	public void select(WebElement element, String visibleText, boolean matchWholeText)
	{	
		try 
		{
			//wait_Until_InvisibilityOf_LoadingScreen();
			
			if(matchWholeText)
			{
				new Select(element).selectByVisibleText(visibleText);
			}
			else
			{
				Select dropDown = new Select(element);
				boolean isPresent = false;
				
				List<WebElement> options = dropDown.getOptions();
				for (int i=0; i<options.size();i++) 
				{
					/*System.out.println(options.get(i).getText() + " - " + visibleText);					
					System.out.println(options.get(i).getText().trim().toLowerCase().compareToIgnoreCase(visibleText.trim().toLowerCase()));*/
					
					if(options.get(i).getText().trim().replace("\n", "").contains(visibleText.trim().replace("\n", "")))  // replace added newly
					{
						dropDown.selectByIndex(i);
						isPresent = true;
						break;
					}
				}
				
				if(!isPresent)
				{
					Configuration.logger.log(LogStatus.ERROR, "Cannot locate element with text:" + visibleText);
					System.out.println("Cannot locate element with text:" + visibleText);
				}
			}
		} 
		catch (NoSuchElementException e) 
		{
			Configuration.logger.log(LogStatus.FAIL, "Cannot locate element with text:" + visibleText);
			System.out.println("Cannot locate element with text:" + visibleText);
		}
		catch (Exception e) 
		{
			System.out.println("Cannot locate element with text:" + visibleText);
			e.printStackTrace();
		}
	}
			
	public void select(WebElement element, int index)
	{
		new Select(element).selectByIndex(index);
	}
		
	public String get_SelectedOption(WebElement element)
	{	
		return new Select(element).getFirstSelectedOption().getText();
	}

	
	public ArrayList<String> get_Options(WebElement element) 
	{	
		try 
		{
			ArrayList<String> value = new ArrayList<>();
			
			List<WebElement> options = new Select(element).getOptions();
			for (int i=1; i<options.size();i++) 
			{
				//System.out.println(options.get(i).getText());
				
				value.add(options.get(i).getAttribute("textContent").trim().replace("\n", ""));
			}
			return value;
		}
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, "Class: WebDriverUtils, Mehtod: get_Options  <br>" + e.getMessage());
		}
		return null;		
	}
	
	/**
	 * Adds screenshot to the extent report
	 * @param fileName
	 * @param srcFile
	 */
	public void save_ScreenshotToReport(String methodName, File srcFile)
	{
		try 
		{	
			SimpleDateFormat reportDateFormat = new SimpleDateFormat("HH_mm_ss");			
			methodName = methodName + "_" + reportDateFormat.format(new Date());			
			File destFile = new File(Constants.screenShotFolderPath + methodName +  ".png");
			FileUtils.copyFile(srcFile, destFile);
			
			/*String screenPath = new File(Constants.screenShotFolderPath + methodName +  ".png").getAbsolutePath();			
			Configuration.logger.log(LogStatus.INFO, Configuration.logger.addScreenCapture(screenPath));*/
			
			String screenPath = "screenshots/"+ methodName +  ".png";	
			Configuration.logger.log(LogStatus.INFO, Configuration.logger.addScreenCapture(screenPath));
			
		} 
		catch (IOException e1) 
		{
		      e1.printStackTrace();
		}		
	}
	
	/**
	 * Adds screenshot to the extent report
	 * @param fileName
	 */
	public void save_ScreenshotToReport(String methodName) 
	{
		try 
		{
			SimpleDateFormat reportDateFormat = new SimpleDateFormat("HH_mm_ss");			
			methodName = methodName + "_" + reportDateFormat.format(new Date());
			
			
			File srcFile = ((TakesScreenshot)Configuration.driver).getScreenshotAs(OutputType.FILE);		
			File destFile = new File(Constants.screenShotFolderPath + methodName +  ".png");			
			FileUtils.copyFile(srcFile, destFile);
						
			
			/*String screenPath = new File(Constants.screenShotFolderPath + methodName +  ".png").getAbsolutePath();			
			Configuration.logger.log(LogStatus.INFO, Configuration.logger.addScreenCapture(screenPath));*/
			
			String screenPath = "screenshots/"+ methodName +  ".png";	
			Configuration.logger.log(LogStatus.INFO, Configuration.logger.addScreenCapture(screenPath));
		} 
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, "Class: WebDriverUtils, Mehtod: save_ScreenshotToReport <br>" + e.getMessage());
		}		
	}
	
	
	/**
	 * returns screenshot 
	 * @return file
	 */
	public File captureScreenshot() 
	{
		try 
		{
			return ((TakesScreenshot)Configuration.driver).getScreenshotAs(OutputType.FILE);
		} 
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, "Class: WebDriverUtils, Mehtod: save_ScreenshotToReport <br>" + e.getMessage());
		}
		return null;
	}
	
	
	public Transaction uploadInventory(String filePath)
	{
		try 
		{
			Process p = Runtime.getRuntime().exec(filePath);	//".\\src\\test\\resources\\atom1.exe"
			p.waitFor();
			
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

			new WebDriverUtils().wait(2000);
			
			ArrayList<String> data = new ArrayList<>();
			String line;
			while ((line = input.readLine()) != null) 
			{				
			  data.add(line);
			}
			
			if(data.get(4).equals("ERROR"))
			{
				return new Transaction(false, (data.get(4) + " - " + data.get(2)));
			}
			else if(data.get(4).equals("SUCCESS"))
			{
				if(data.get(2).contains("File uploaded already"))
					return new Transaction(false, (data.get(4) + " - " + data.get(2)));
				
				
				return new Transaction(true, (data.get(4) + " - " + data.get(2)));
			}
			else if(data.get(4).equals("Required"))
			{
				return new Transaction(false, (data.get(4) + " - " + data.get(2)));
			}
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public Alert isAlertPresent() 
	{ 
	    try 
	    { 
	    	return Configuration.driver.switchTo().alert(); 
	       
	    } 
	    catch (NoAlertPresentException Ex) 
	    { 
	        return null; 
	    }  
	}
	
	public boolean isAccountLocked_QC(WebElement account)
	{
		try 
		{				
			new WebDriverWait(Configuration.driver,10).until(ExpectedConditions.elementToBeClickable(account)).click();
			new WebDriverWait(Configuration.driver,10).until(ExpectedConditions.visibilityOfElementLocated(By.id("TxtComments")));
			
	 	}
		catch (Exception e1) 
		{
			//e1.printStackTrace();
			ClaimTransaction objClaimTransaction = PageFactory.initElements(Configuration.driver, ClaimTransaction.class);
			if(objClaimTransaction.getMessageDetails().contains("This Account Is Locked By The Other User"))//success
			{				
				return true;
			}	
			
		}		
		return false;		
	}
	
	
	
	public Info isAccountLocked_MEOB(WebElement account)
	{
		try 
		{
			// click on the link [inventory id]
			new WebDriverWait(Configuration.driver,10).until(ExpectedConditions.elementToBeClickable(account)).click();			
			wait_Until_InvisibilityOf_LoadingScreen();
			
			// wait for transaction popup
			new WebDriverWait(Configuration.driver,5).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='largeModalMEOB']//select[@ng-model = 'ddlClaimStatus']")));
	 		 	
	 	}
		catch (TimeoutException e) 
		{
			// when account locked by other user
			ClaimTransaction objClaimTransaction = PageFactory.initElements(Configuration.driver, ClaimTransaction.class);			
			try
			{
				if(objClaimTransaction.getMessageDetails().contains("This Account Is Locked By The Other User"))
				{				
					return new Info(true, "This Account Is Locked By The Other User");
				}
			}
			catch (NoSuchElementException e1) 
			{
				// transaction window disappears immeditely
				//Configuration.logger.log(LogStatus.ERROR, "Can't open Claim Transaction popup window");
				System.out.println(" ---- ddlClaimStatus not found and Account Locked message not displayed ---- ");
				return new Info(true, "Can't open Claim Transaction popup window");
			}
		}
		catch (Exception e1) 
		{
			Configuration.logger.log(LogStatus.INFO, "Class: WebDriverUtils, Mehtod: isAccountLocked_MEOB <br>" + e1.getMessage());
		}
		
		return new Info(false, "Account not locked");		
	}
	
	
	public Info isAccountLocked(WebElement account)
	{
		try 
		{	
			/*new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(By.id("largeModal")));
			new WebDriverWait(Configuration.driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(By.id("Pagefooter")));*/
			
			
			
			try 
			{
				// click on the link [account number/ inventory id]
				new WebDriverWait(Configuration.driver,10).until(ExpectedConditions.elementToBeClickable(account)).click();			
				wait_Until_InvisibilityOf_LoadingScreen();
			} 
			catch (WebDriverException e) 
			{
				if(e.getMessage().contains("is not clickable at point"))
				{
					scrollWindow(0,100);
				}
				
				// click on the link [account number/ inventory id]
				new WebDriverWait(Configuration.driver,10).until(ExpectedConditions.elementToBeClickable(account)).click();			
				wait_Until_InvisibilityOf_LoadingScreen();
			}
			
			
			
			// wait for transaction popup
			new WebDriverWait(Configuration.driver,5).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[@ng-model = 'ddlClaimStatus']")));
	 		 	
	 	}
		catch (TimeoutException e) 
		{
			// when account locked by other user
			ClaimTransaction objClaimTransaction = PageFactory.initElements(Configuration.driver, ClaimTransaction.class);			
			try
			{
				if(objClaimTransaction.getMessageDetails().contains("This Account Is Locked By The Other User"))
				{				
					return new Info(true, "This Account Is Locked By The Other User");
				}
			}
			catch (NoSuchElementException e1) 
			{
				// transaction window disappears immeditely
				System.out.println(" ---- ddlClaimStatus not found and Account Locked message not displayed ---- ");
				return new Info(true, "Can't open Claim Transaction popup window");
			}
		}
		
		catch (Exception e1) 
		{
			Configuration.logger.log(LogStatus.INFO, "Class: WebDriverUtils, Mehtod: isAccountLocked <br>" + e1.getMessage());
		}
		
		return new Info(false, "Account not locked");		
	}
	
	public boolean isElementPresent(WebElement element)
	{
		try
		{
			turnOffImplicitWaits();
			element.getTagName();
			turnOnImplicitWaits();
			return true;
		}
		catch (NoSuchElementException e) 
		{
			turnOnImplicitWaits();
			return false;
		}	
		
	}
	
	public boolean isElementPresent(By findBy)
	{
		try
		{
			turnOffImplicitWaits();
			Configuration.driver.findElement(findBy);
			turnOnImplicitWaits();
			return true;
		}
		catch (NoSuchElementException e) 
		{
			turnOnImplicitWaits();
			return false;
		}	
		
	}
	
	public boolean isElementPresent(WebElement element, By findBy)
	{
		try
		{
			turnOffImplicitWaits();
			element.findElement(findBy);
			turnOnImplicitWaits();
			return true;
		}
		catch (NoSuchElementException e) 
		{
			turnOnImplicitWaits();
			return false;
		}	
		
	}
	
	
	private void turnOffImplicitWaits() 
	{
	    Configuration.driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
	}

	private void turnOnImplicitWaits() 
	{
		Configuration.driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	}
	
	
	public void scrollWindow(WebElement element)
	{	 
		Actions	actions = new Actions(Configuration.driver); actions.moveToElement(element);
		actions.perform();		
		
		//((JavascriptExecutor)Configuration.driver).executeScript("arguments[0].scrollIntoView(true);", element);
	}
	
	public void scrollWindow(int width, int height)
	{
		((JavascriptExecutor)Configuration.driver).executeScript("window.scrollBy(" + width + "," + height + ")", "");
	}
	
	public void scrollToBottomOfPage()
	{
		((JavascriptExecutor)Configuration.driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
	}
	
	public int getColumnIndex(WebElement dataTable, String columnName)
	{
		//get column count
		int colCount = (dataTable.findElements(By.xpath("thead/tr/th"))).size();
		
		for (int colIndex=1; colIndex<=colCount; colIndex++)
		{
		    String sValue = null;
		    sValue = dataTable.findElement(By.xpath("./thead/tr/th["+colIndex+"]")).getText();
		    if(sValue.equals(columnName))
		    {
		    	return colIndex;
		    }
		}
		return -1;
	}
	

	
	
	
	public void wait(int timeInMilliSeconds)
	{
		try 
		{
			Thread.sleep(timeInMilliSeconds);
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void wait_Until_InvisibilityOf_LoadingScreen()
	{
		
		try 
		{
			turnOffImplicitWaits();
			
			while(true)
			{
				try
				{	
					if(!isElementPresent(By.xpath("//div[@id = 'divLoading']")) && !isElementPresent(By.xpath("//div[@id = 'loading-bar-spinner']")))
					{
						break;
					}
				}
				catch (NoSuchElementException e) 
				{
					break;
				}
			}
			
			turnOnImplicitWaits();
		}
		catch (Exception e) 
		{
			Configuration.logger.log(LogStatus.INFO, "Class: WebDriverUtils, Mehtod: wait_Until_InvisibilityOf_LoadingScreen <br>" + e.getMessage());
		}
			
	}

	public void wait_Until_Select_Options_Populates(final Select select) 
	{

		Wait<WebDriver> wait = new FluentWait<>(Configuration.driver)
					    .withTimeout(60, TimeUnit.SECONDS)
					    .pollingEvery(1, TimeUnit.SECONDS)
					    .ignoring(NoSuchElementException.class);
		
		wait.until(new Function<WebDriver, Boolean>() {
		  @Override
		  public Boolean apply(WebDriver driver) {
		    return  (select.getOptions().size() > 1);
		  }
		});
    }
	
	public void wait_Until_Table_Populates(WebElement element, final int rowCount) 
	{
        final WebElement table = element;

        new FluentWait<WebDriver>(Configuration.driver)
        .withTimeout(60, TimeUnit.SECONDS)
        .pollingEvery(1, TimeUnit.SECONDS)
        .until(new Predicate<WebDriver>()
        {
            public boolean apply(WebDriver d) {
                 List<WebElement> rawList = table.findElements(By.xpath("./tbody/tr"));
                 return (rawList.size() >= rowCount);
            }
        });
    }


	
	
	public Info upload_Inventory(String filePath)
	{
		try 
		{
			Process p = Runtime.getRuntime().exec(filePath);	// ".\\src\\test\\resources\\atom1.exe"
			p.waitFor();
			
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			new WebDriverUtils().wait(3000);
			
			ArrayList<String> data = new ArrayList<>();
			String line;
			while ((line = input.readLine()) != null) 
			{				
			  data.add(line);
			}
			
			if(data.get(4).equals("SUCCESS"))
			{
				if(data.get(2).contains("File uploaded already"))
					return new Info(false, (data.get(4) + " - " + data.get(2)));
				
				
				return new Info(true, data.get(2));    // (data.get(4) + " - " + data.get(2))
			}
			
			return new Info(false, (data.get(4) + " - " + data.get(2)));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Info(false, e.getMessage());
		}
		
	}
	

	public Info create_PriorityFile(List<String> lstUID, String practice) 
	{
		try 
		{			
			// create new excel obj 	
			ExcelReader xlPriority = new ExcelReader();
			
			// set sheet
			XSSFSheet sheet = xlPriority.setSheet("Sheet1");
			
			// set header
			xlPriority.writeValue(sheet, 0, 0, "UID");
			xlPriority.writeValue(sheet, 0, 1, "DOS");
			xlPriority.writeValue(sheet, 0, 2, "ReceivedDate");
			xlPriority.writeValue(sheet, 0, 3, "ETA");
			xlPriority.writeValue(sheet, 0, 4, "PriorityReason");
			
			String sDOS = getDate(-120);
			String sReceivedDate = new SimpleDateFormat("MM/dd/yyy").format(new Date());
			
			String sETA = getDate(3);
			String sPriorityReason = "priority";
			
			int iRowIndex = 1;
			for (String uid : lstUID) 
			{
				// set value			
				xlPriority.writeValue(sheet, iRowIndex, 0, uid);
				xlPriority.writeValue(sheet, iRowIndex, 1, sDOS);
				xlPriority.writeValue(sheet, iRowIndex, 2, sReceivedDate);
				xlPriority.writeValue(sheet, iRowIndex, 3, sETA);
				xlPriority.writeValue(sheet, iRowIndex, 4, sPriorityReason);
				
				iRowIndex++;
			}
			
			
			
			
			// file name and directory			
			String fileName = "Priority_"+ Constants.reportDateFormat.format(new Date()) +".xlsx";		
			String directoryName  =  System.getProperty("user.dir") + "\\src\\test\\resources\\Input\\Priority\\" + Constants.year + "\\" + Constants.month + "\\" + Constants.day + "\\";
			
			// create directory if doesnt exist
			File directory = new File(directoryName);
			if (! directory.exists())
			{
			    directory.mkdirs();
			}		
			
			// save excel 
			xlPriority.saveAs( directoryName + fileName);
			
			
	
		    String sFilePath = directoryName + fileName;
		    return new Info(true, sFilePath);
		    
		    
		    
		    //sScreenshotFileName = sScreenshotFileName + "_" + new SimpleDateFormat("mm_ss").format(new Date());
		    //String completePath = sExePath + " " + sClient + " " + sLocation + " " + "\"" + sPractice + "\"" + " " + sFilePath + " " + sScreenshotFileName;		    		    
		    //Info result = upload_Inventory(completePath);		    
		    //save_InventoryScreenshot(sScreenshotFileName);
		    
		    
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Info(false, "Class: WebDriverUtils, Mehtod: create_PriorityFile  <br>" + e.getMessage());
		}
	}

	

	public Info create_DenialFileForAzalea(List<String> lstUID, String practice) 
	{
		try 
		{			
			// create new excel obj 	
			ExcelReader xlPriority = new ExcelReader();
			
			// set sheet
			XSSFSheet sheet = xlPriority.setSheet("Sheet1");
			
			// set header
			xlPriority.writeValue(sheet, 0, 0, "UID");
			xlPriority.writeValue(sheet, 0, 1, "DenialPostedDate");
			xlPriority.writeValue(sheet, 0, 2, "DenialCode");
			xlPriority.writeValue(sheet, 0, 3, "DenialDescription");
			xlPriority.writeValue(sheet, 0, 4, "DenialRemarks");
			
			
			
			// set first row data with denail date more than 30 days
			String sDenialPostedDate = getDate(-15);			
			String sDenialCode = "CO246";
			String sDenialDescription = "NON-PAYBLE CODE-FOR REQUIRED REPORTING";
			String sDenialRemarks = "test denial upload";
			
			// set value			
			xlPriority.writeValue(sheet, 1, 0, lstUID.get(0));
			xlPriority.writeValue(sheet, 1, 1, sDenialPostedDate);
			xlPriority.writeValue(sheet, 1, 2, sDenialCode);
			xlPriority.writeValue(sheet, 1, 3, sDenialDescription);
			xlPriority.writeValue(sheet, 1, 4, sDenialRemarks);
			
			
			
			
			// set first row data with denail date less than 30 days
			sDenialPostedDate = getDate(-31);
			
			// set value			
			xlPriority.writeValue(sheet, 2, 0, lstUID.get(1));
			xlPriority.writeValue(sheet, 2, 1, sDenialPostedDate);
			xlPriority.writeValue(sheet, 2, 2, sDenialCode);
			xlPriority.writeValue(sheet, 2, 3, sDenialDescription);
			xlPriority.writeValue(sheet, 2, 4, sDenialRemarks);
			
				
			
			
			// file name and directory			
			String fileName = "Denial_Azalea_"+ Constants.reportDateFormat.format(new Date()) +".xlsx";		
			String directoryName  =  System.getProperty("user.dir") + "\\src\\test\\resources\\Input\\Denial\\" + Constants.year + "\\" + Constants.month + "\\" + Constants.day + "\\";
			
			// create directory if doesnt exist
			File directory = new File(directoryName);
			if (! directory.exists())
			{
			    directory.mkdirs();
			}		
			
			// save excel 
			xlPriority.saveAs(directoryName + fileName);
			
			
		    String sFilePath = directoryName + fileName; 
		    return new Info(true, sFilePath);
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return new Info(false, "Class: WebDriverUtils, Mehtod: create_DenialFileForAzalea  <br>" + e.getMessage());
		}
	}
	


	public void highlightElement(WebElement element) 
	{
		try 
		{
			JavascriptExecutor js = (JavascriptExecutor) Configuration.driver;
			js.executeScript("arguments[0].setAttribute('style', 'border: 4px solid red;');", element);
			
			//js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 3px solid yellow;');", element);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}



}
