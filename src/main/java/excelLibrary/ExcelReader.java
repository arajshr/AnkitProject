package excelLibrary;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Point;

import com.relevantcodes.extentreports.LogStatus;

import configuration.Configuration;
import configuration.Constants;
import configuration.Constants.Role;

public class ExcelReader 
{
	FileInputStream fis;
	XSSFWorkbook workbook;
	
	public ExcelReader(String xlPath) throws IOException 
	{
		try 
		{		
			fis = new FileInputStream(xlPath);
			workbook = new XSSFWorkbook(fis);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
			
	}
	
	public ExcelReader()
	{
		try 
		{	
			workbook = new XSSFWorkbook();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
			
	}
	
	public XSSFSheet setSheet(String sheetName)
	{
		XSSFSheet sheet = workbook.getSheet(sheetName);
		 if(sheet == null)
			 sheet = workbook.createSheet(sheetName);
		 
		return sheet;
	}
	
	public int getRowCount(XSSFSheet sheet)
	{
		return sheet.getLastRowNum();
	}
	
	public int getColumnCount(XSSFSheet sheet, int rowIndex)
	{
		return sheet.getRow(rowIndex).getLastCellNum();
	}
	
	public int isExecutable(String TestCaseId)
	{
		XSSFSheet sheet = setSheet("TestData");
		
		for (int rowIndex = 1; rowIndex <= getRowCount(sheet); rowIndex++)
		{
			if(readValue(sheet, rowIndex, "TC_ID").equals(TestCaseId))
			{
				String sRunMode = readValue(sheet, rowIndex, "RUN_MODE");
				
				if(sRunMode == null)
				{
					return -1;
				}
				else if(sRunMode.equalsIgnoreCase("Y"))
				{
					return rowIndex;
				}
				
			}
		}
		return 0;
	}
	
	public String readValue(XSSFSheet sheet, int rowIndex, int cellNum)
	{
		try
		{
			XSSFRow row = sheet.getRow(rowIndex);			
			XSSFCell cell = row.getCell(cellNum);
			
			return new DataFormatter().formatCellValue(cell);
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public String readValue(XSSFSheet sheet, int rowIndex, String colName) 
	{
		int colIndex = 0;
		for (int i = 0; i < getColumnCount(sheet, rowIndex); i++)
		{
			if(sheet.getRow(0).getCell(i).getStringCellValue().trim().equals(colName))
			{
				colIndex = i;				
				break;
			}
		}
		
		return readValue(sheet, rowIndex, colIndex);
	}
	
	public void writeHeaderValue(XSSFSheet sheet, int rowIndex, int col_Num, String value) 
	{
		XSSFFont font = workbook.createFont();
	    //font.setFontHeightInPoints((short)14);
        font.setBold(true);
        
		XSSFRow row = sheet.getRow(rowIndex);
        if(row == null)
        	row = sheet.createRow(rowIndex);
 
        XSSFCell cell = row.getCell(col_Num);
        if(cell == null)
        	cell = row.createCell(col_Num);
        
        
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        cell.setCellStyle(style);
        
        cell.setCellValue(value);
		
	}
	
	public void writeValue(XSSFSheet sheet, int rowIndex, int col_Num, String value) 
	{
	    //XSSFRow titleRow = sheet.createRow(0);
		
		XSSFRow row = sheet.getRow(rowIndex);
        if(row == null)
        	row = sheet.createRow(rowIndex);
 
        XSSFCell cell = row.getCell(col_Num);
        if(cell == null)
        	cell = row.createCell(col_Num);
        
	    
        cell.setCellValue(value);
		
	}

	public void saveAs(String string) throws IOException 
	{
		FileOutputStream fileOut = new FileOutputStream(string);
        workbook.write(fileOut);
        fileOut.close();

        // Closing the workbook
        workbook.close();
	}


	/**
	 * Returns Hashmap
	 * @param sheet
	 * @param rowNumber
	 * @return
	 */	
	public HashMap<String, String> get_TransactionData(XSSFSheet sheet, int rowNumber)
	{
		HashMap<String,String> currentRow = new HashMap<String,String>();		
				
		
		XSSFRow row = sheet.getRow(rowNumber);
		Row HeaderRow = sheet.getRow(0);
		
		try
		{			
			for(int col_num=1; col_num<row.getLastCellNum(); col_num++)
	        {				
				currentRow.put(HeaderRow.getCell(col_num).getStringCellValue(), getCellValue(sheet, rowNumber, col_num));				
	        }
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return currentRow;
	}
	
		
	private String getCellValue(XSSFSheet sheet, int rowNumber, int columnNumber) 
	{
		XSSFRow row = sheet.getRow(rowNumber);
		XSSFCell cell = row.getCell(columnNumber);
		
		return new DataFormatter().formatCellValue(cell);  // formatCellValue(Cell cell) method - returns the formatted value of a cell as String regardless of the cell type				
		
	}

	
	public HashMap<String, String> get_LoginByRole(Role role) 
	{
		String sRole = null;
		if(role.equals(Constants.Role.MANAGER))
		{
			sRole = "Manager";
		}
		else if(role.equals(Constants.Role.TL))
		{
			sRole = "TL";
		}
		else if(role.equals(Constants.Role.QCA ))
		{
			sRole = "QCA";
		}
		else if(role.equals(Constants.Role.ANALYST))
		{
			sRole = "Analyst";
		}		
		else if(role.equals(Constants.Role.CALLER))
		{
			sRole = "Caller";
		}
		else if(role.equals(Constants.Role.PAYMENT ))
		{
			sRole = "Payment";
		}
		else if(role.equals(Constants.Role.CODING))
		{
			sRole = "Coding";
		}
		else if(role.equals(Constants.Role.CREDIT_BALANCE))
		{
			sRole = "Credit Balance";
		}
		else if(role.equals(Constants.Role.CORRESPONDANCE))
		{
			sRole = "Correspondance";
		}
		else if(role.equals(Constants.Role.APPEALS))
		{
			sRole = "Appeals";
		}
		
		XSSFSheet sheet = setSheet("UserRoles");
		
		int rowIndex = -1;
		
		try 
		{
			// get row index using tc_id
			for(int i=1; i<=sheet.getLastRowNum();i++)
			{ 
				if (sRole.equals(sheet.getRow(i).getCell(3).getStringCellValue().trim())) 
				{
					rowIndex = i;
					break;
				}
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}		 
				
		if(rowIndex > 0) // when rowIndex greater than zero, get error parameters
		{
			HashMap<String,String> currentRow = new HashMap<String,String>();	
			
			//XSSFRow row = sheet.getRow(rowIndex);
			Row HeaderRow = sheet.getRow(0);
			
			try
			{			
				for(int col_num=0; col_num<4; col_num++)
		        {				
					currentRow.put(HeaderRow.getCell(col_num).getStringCellValue(), getCellValue(sheet, rowIndex, col_num));				
		        }
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			
			return currentRow;
		}
			
		else
			return null;
	}
	
	
	/**
	 * Returns user credentials
	 * @param tc_id
	 * @return
	 */
	public HashMap<String, String> get_UserCredential(String type, String value) 
	{
		
		XSSFSheet sheet = setSheet("UserRoles");
		
		int rowIndex = -1;
		int lastRow = sheet.getLastRowNum();
		try 
		{
			// get row index using tc_id
			for(int i=1; i<=lastRow;i++)
			{
				if(type.equals("ROLE"))
				{
					if (value.equals(sheet.getRow(i).getCell(3).getStringCellValue().trim())) 
					{
						rowIndex = i;
					}
				}
				else if(type.equals("USERNAME"))
				{
					if (value.equals(sheet.getRow(i).getCell(1).getStringCellValue().trim())) 
					{
						rowIndex = i;
					}
				}
				else if(type.equals("NAME"))
				{
					if (value.equals(sheet.getRow(i).getCell(0).getStringCellValue().trim())) 
					{
						rowIndex = i;
					}
				}
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}		 
				
		if(rowIndex > 0) // when rowIndex greater than zero, user credentials
		{
			HashMap<String,String> currentRow = new HashMap<String,String>();	
			
			XSSFRow row = sheet.getRow(rowIndex);
			Row HeaderRow = sheet.getRow(0);
			
			try
			{			
				for(int col_num=0; col_num<row.getLastCellNum(); col_num++)
		        {				
					currentRow.put(HeaderRow.getCell(col_num).getStringCellValue(), getCellValue(sheet, rowIndex, col_num));
		        }
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			return currentRow;
		}
			
		else
			return null;
	}
			
	
	/**
	 * Returns qc parameters for audit
	 * @param tc_id
	 * @return
	 */
	public HashMap<String, String> get_QCAErrorParameters(String tc_id) 
	{
		
		XSSFSheet sheet = setSheet("QCA");
		
		int rowIndex = -1;
		
		try 
		{
			// get row index using tc_id
			for(int i=1; i<=sheet.getLastRowNum();i++)
			{
				if (tc_id.equals(sheet.getRow(i).getCell(0).getStringCellValue().trim())) 
				{
					rowIndex = i;
				}
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}		 
				
		if(rowIndex > 0) // when rowIndex greater than zero, get error parameters
		{
			HashMap<String,String> currentRow = new HashMap<String,String>();	
			
			XSSFRow row = sheet.getRow(rowIndex);
			Row HeaderRow = sheet.getRow(0);
			
			try
			{			
				for(int col_num=1; col_num<row.getLastCellNum(); col_num++)
		        {				
					currentRow.put(HeaderRow.getCell(col_num).getStringCellValue(), getCellValue(sheet, rowIndex, col_num));				
		        }
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			return currentRow;
		}
			
		else
			return null;
	}
	

	/**
	 * Return data to submit transaction 
	 * @param nextQueue
	 * @return
	 */
	public HashMap<String, String> get_TransactionData(String nextQueue) 
	{
		
		XSSFSheet sheet = setSheet("Transaction");
		
		int rowIndex = -1;
		
		try 
		{
			for(int i=1; i<=sheet.getLastRowNum();i++)
			{
				if (nextQueue.equals(sheet.getRow(i).getCell(0).getStringCellValue().trim())) 
				{
					rowIndex = i;
					break;
				}
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}		 
				
		if(rowIndex > 0)
			return get_TransactionData(sheet, rowIndex);
		else
			return null;
	}
	
	
	/**
	 * Returns data to create meob inventory
	 * @param testId
	 * @return
	 */
	public HashMap<String, String> get_MEOBData(String tc_id)
	{	
		XSSFSheet sheet = setSheet("MEOB");		
				
		int rowIndex = -1;
				
		try 
		{
			for(int i=1; i<=sheet.getLastRowNum();i++)
			{
				if (tc_id.equals(sheet.getRow(i).getCell(0).getStringCellValue().trim())) 
				{
					rowIndex = i;
				}
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}		 
				
		if(rowIndex > 0)
			return get_TransactionData(sheet, rowIndex);
		else
			return null;
	}

	/**
	 * Returns accounts matching given provider name
	 * @param sExpProvider
	 * @return
	 */
	public List<String> getAccountsForProviderName(String sExpProvider) 
	{
		List<String> lstAccount = new ArrayList<>();
		
		try 
		{
			XSSFSheet sheet = setSheet("Sheet1");
			Point location_Provider =  get_CellIndex(sheet, "Rendering_Provider_Name");			
			int row_num = (location_Provider.getX() + 1);
			int col_num = location_Provider.getY();
			
			
			Point location_AccNo =  get_CellIndex(sheet, "Patient_Account_No");		
			int col_num_AccNo = location_AccNo.getY();
			
			
			while (true)
			{				
				sheet.getRow(row_num);						
				try
				{	
					String sProvider = getCellValue(sheet, row_num, col_num).trim();
					
					if(sExpProvider.trim().equals(sProvider)) // if provider matches increase count
					{						
						lstAccount.add(getCellValue(sheet, row_num, col_num_AccNo)); // get MRN 
					}		
					row_num++;
				}
				catch (NullPointerException e) 
				{
					break;
				}
			}			
		}
		catch (Exception e)
		{
			Configuration.logger.log(LogStatus.INFO, "Class: ExcelReader, Mehtod: getEncountersFromWorkFile <br>" + e.getStackTrace());
		}
		return lstAccount;
	}

	
	
	public HashMap<String, String> get_AccountInfoFromExcel(String sUID) 
	{		
		try 
		{
			XSSFSheet sheet = setSheet("Sheet1");
			Point location_UID =  get_CellIndex(sheet, "UID");			
			int row_num = (location_UID.getX() + 1);
			int col_num = location_UID.getY();
			
			
			
			while (true)
			{				
				sheet.getRow(row_num);						
				try
				{	
					
					String stempUID = getCellValue(sheet, row_num, col_num).trim();
					
					if(sUID.equals(stempUID)) // if UID matches get other values
					{	
						Point location_Encounter =  get_CellIndex(sheet, "Encounter_ID");						
						int col_num_Encounter = location_Encounter.getY();
						
						String sEncounterID = getCellValue(sheet, row_num, col_num_Encounter);
											
						
						// Patient Name
						Point location_Name =  get_CellIndex(sheet, "Patient_Name");
						int col_num_Name = location_Name.getY();
						
						String sPatientName = getCellValue(sheet, row_num, col_num_Name);
						
						
						// DOB
						Point location_DOB =  get_CellIndex(sheet, "DOB");
						int col_num_DOB = location_DOB.getY();						
						
						String sPatientDOB = getCellValue(sheet, row_num, col_num_DOB);
						
						
						// DOS Start date  
						Point location_DOS =  get_CellIndex(sheet, "DOS_Start");
						int col_num_DOS = location_DOS.getY();						
						
						String sDOS = getCellValue(sheet, row_num, col_num_DOS);
						
						
						
						HashMap<String, String> mapEncounter = new HashMap<>();
						mapEncounter.put("UID", sUID);
						mapEncounter.put("EncounterID", sEncounterID);
						mapEncounter.put("PatientName", sPatientName);
						mapEncounter.put("DOB", sPatientDOB);
						mapEncounter.put("DOS", sDOS);
						
						return mapEncounter;
					}
					
					
				}
				catch (NullPointerException e) 
				{
					break;
				}
				
				row_num++;
			}			
		}
		catch (Exception e)
		{
			Configuration.logger.log(LogStatus.INFO, "Class: ExcelReader, Mehtod: getEncountersFromWorkFile <br>" + e.getStackTrace());
		}
		return null;
		
	}
	
	
	private Point get_CellIndex(XSSFSheet currentSheet, String cellContent)
	 {
		 int rowIndex = 0;
		 int colIndex = 0;
		 
		 for(Row cRow : currentSheet)
		 {
			 for(Cell cCell : cRow)
			 {
				 if(cCell.getCellTypeEnum() == CellType.STRING)
				 {
					 if(cCell.getStringCellValue().equals(cellContent))
					 {
						 rowIndex = cRow.getRowNum();
						 colIndex = cCell.getColumnIndex();
						 
						 return new Point(rowIndex, colIndex);
					 }
				 }
			 }
		 }
		return null;
	 }
	
	
	
	
	
	
	
	
}
