package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import configuration.Constants;

public class connectDB 
{	
	// Declare the JDBC objects.
	Connection connection = null;
	Statement statement = null;
	PreparedStatement preparedStatement;
	ResultSet resultSet = null;
	
	public connectDB()
	{
		try
		{
			connection = DriverManager.getConnection(Constants.connectionUrl,Constants.dbUserName,Constants.dbPassword);
			System.out.println("Connection opened");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}		
	}
	
	public void close() 
	{
		if (resultSet != null) try { resultSet.close(); } catch(Exception e) {}
		if (statement != null) try { statement.close(); } catch(Exception e) {}
		if (preparedStatement != null) try { preparedStatement.close(); } catch(Exception e) {}
		if (connection != null) try { connection.close(); } catch(Exception e) {}
	}
	
	
	public String get_Allocation_Rule(String ruleDescription)
	{
		String sAllotRule = null;
		
		try
		{
			preparedStatement = connection.prepareStatement("select distinct AllotRule from AllotmentRules where DisplayAllotRule = ? and isActive = ?");
			preparedStatement.setString(1, ruleDescription);
			preparedStatement.setInt(2, 1);
			
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) 
			{
				sAllotRule = resultSet.getString("AllotRule");
				System.out.println(sAllotRule);	            
			}			 
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return sAllotRule;
			
	}
		
	public String get_KickOut_Rule(String ruleDescription)
	{
		String sKickOutRule = null;
		
		try
		{
			preparedStatement = connection.prepareStatement("select distinct KickoutRule where KickoutRuleDisplay = ? and isActive = ?");
			preparedStatement.setString(1, ruleDescription);
			preparedStatement.setInt(2, 1);
			
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) 
			{
				sKickOutRule = resultSet.getString("AllotRule");
				System.out.println(sKickOutRule);	            
			}			 
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return sKickOutRule;
			
	}
	
	public boolean get_Accounts_To_QC()
	{
		try 
		{
			String SQL = "SET NOCOUNT ON;\r\n" + 
					"\r\n" + 
					"	DECLARE @AuditDate DATE = CAST( DATEADD(DAY,-1, dbo.udfGetProductionDate()) AS DATE)\r\n" + 
					"\r\n" + 
					"	DECLARE @QcAccountsCount INT = 0\r\n" + 
					"\r\n" + 
					"	DECLARE @TotalUsers INT = 0\r\n" + 
					"	DECLARE @TotalAccounts INT = 0\r\n" + 
					"	DECLARE @TotalTargets INT = 0\r\n" + 
					"\r\n" + 
					"	DECLARE @UserCurrentRow INT = 1\r\n" + 
					"	DECLARE @TargetCurrentRow INT = 1\r\n" + 
					"	DECLARE @AccountCurrentRow INT = 1\r\n" + 
					"\r\n" + 
					"	DECLARE @UserID INT = 0\r\n" + 
					"	DECLARE @TransID INT = 0\r\n" + 
					"	DECLARE @TempCHMapID INT = 0\r\n" + 
					"	DECLARE @AuditPercent INT = 0\r\n" + 
					"	DECLARE @Target INT = 0\r\n" + 
					"\r\n" + 
					"	DECLARE @ToBeAuditedCount INT = 0\r\n" + 
					"	DECLARE @UserTargertRow INT = 0\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"	IF OBJECT_ID('tempdb..#tblUsers') IS NOT NULL\r\n" + 
					"		DROP TABLE #tblUsers\r\n" + 
					"\r\n" + 
					"	CREATE TABLE #tblUsers\r\n" + 
					"	( \r\n" + 
					"		RowID INT IDENTITY ( 1 , 1 ),\r\n" + 
					"		UserID INT\r\n" + 
					"	) \r\n" + 
					"\r\n" + 
					"	INSERT INTO #tblUsers(UserID)\r\n" + 
					"	SELECT DISTINCT(AllotedTo) FROM AgentTransaction \r\n" + 
					"	WHERE AuditStatusID = dbo.udfGetAuditStatusID('RTA') AND TransStatusID = dbo.udfGetTransStatusID('CMP') AND ProductionDate = @AuditDate\r\n" + 
					"\r\n" + 
					"	SET @TotalUsers = (SELECT COUNT(RowID) FROM #tblUsers) \r\n" + 
					"	SET @UserCurrentRow = 1\r\n" + 
					"\r\n" + 
					"	WHILE (@UserCurrentRow <= @TotalUsers) \r\n" + 
					"	BEGIN \r\n" + 
					"		SELECT @UserID = UserID \r\n" + 
					"		FROM   #tblUsers \r\n" + 
					"		WHERE  RowID = @UserCurrentRow 	\r\n" + 
					"		--------------------------------------------------User Target Start-------------------------------------------------------\r\n" + 
					"		IF OBJECT_ID('tempdb..#tblUserTarget') IS NOT NULL\r\n" + 
					"			DROP TABLE #tblUserTarget\r\n" + 
					"\r\n" + 
					"		CREATE TABLE #tblUserTarget\r\n" + 
					"		( \r\n" + 
					"			RowID INT IDENTITY ( 1 , 1 ),\r\n" + 
					"			CHMapID INT,\r\n" + 
					"			Target INT,\r\n" + 
					"			AuditPercent INT\r\n" + 
					"		) \r\n" + 
					"\r\n" + 
					"		INSERT INTO #tblUserTarget(CHMapID,AuditPercent,Target)\r\n" + 
					"		SELECT CHMapID , ISNULL(AuditPercent,0) , ISNULL(Target,0) FROM UserTargets \r\n" + 
					"		WHERE UserID = @UserID AND @AuditDate BETWEEN CAST(FromDate AS DATE) AND CAST(ToDate AS DATE)\r\n" + 
					"		AND CHMapID IN ( SELECT DISTINCT(Inv.CHMapID) FROM Inventory Inv WITH(NOLOCK) \r\n" + 
					"		INNER JOIN  AgentTransaction Trans on Inv.InventoryID = Trans.InventoryID \r\n" + 
					"		WHERE Trans.AllotedTo = @UserID AND AuditStatusID = dbo.udfGetAuditStatusID('RTA') AND TransStatusID = dbo.udfGetTransStatusID('CMP') AND ProductionDate = @AuditDate ) \r\n" + 
					"	\r\n" + 
					"		SET @TargetCurrentRow = 1\r\n" + 
					"		SET @TotalTargets = (SELECT COUNT(RowID) FROM #tblUserTarget) \r\n" + 
					"\r\n" + 
					"		WHILE (@TargetCurrentRow <= @TotalTargets) \r\n" + 
					"		BEGIN \r\n" + 
					"			SELECT @TempCHMapID = CHMapID , @Target = ISNULL(Target,0)  , @AuditPercent = ISNULL(AuditPercent,0) \r\n" + 
					"			FROM   #tblUserTarget \r\n" + 
					"			WHERE  RowID = @TargetCurrentRow \r\n" + 
					"\r\n" + 
					"			IF( @Target > 0 AND  @AuditPercent > 0)\r\n" + 
					"			BEGIN\r\n" + 
					"				---------------------------------------Agent Transaction Start------------------------------------------------------------\r\n" + 
					"				IF OBJECT_ID('tempdb..#tblAccounts') IS NOT NULL\r\n" + 
					"					DROP TABLE #tblAccounts\r\n" + 
					"\r\n" + 
					"				CREATE TABLE #tblAccounts\r\n" + 
					"				( \r\n" + 
					"					RowID INT IDENTITY ( 1 , 1 ),\r\n" + 
					"					TransID INT\r\n" + 
					"				) \r\n" + 
					"\r\n" + 
					"				INSERT INTO #tblAccounts(TransID)\r\n" + 
					"				SELECT Trans.TransID FROM Inventory Inv WITH(NOLOCK) \r\n" + 
					"				INNER JOIN  AgentTransaction Trans on Inv.InventoryID = Trans.InventoryID \r\n" + 
					"				WHERE Trans.AllotedTo = @UserID AND Inv.CHMapID = @TempCHMapID \r\n" + 
					"				AND AuditStatusID = dbo.udfGetAuditStatusID('RTA') AND TransStatusID = dbo.udfGetTransStatusID('CMP')\r\n" + 
					"				AND ProductionDate = @AuditDate\r\n" + 
					"\r\n" + 
					"				SET @TotalAccounts = (SELECT COUNT(RowID) FROM #tblAccounts) \r\n" + 
					"\r\n" + 
					"				IF ( @TotalAccounts > 0 )\r\n" + 
					"				BEGIN\r\n" + 
					"					\r\n" + 
					"					SET @ToBeAuditedCount = CAST( @TotalAccounts AS DECIMAL(8,2)) * ( CAST(@AuditPercent AS DECIMAL(8,2)) /100)\r\n" + 
					"					--SET @ToBeAuditedCount = CAST( @TotalAccounts AS DECIMAL(8,2)) * @AuditPercent\r\n" + 
					"					--SET @ToBeAuditedCount = CAST( @TotalAccounts AS DECIMAL(8,2)) / @Target * @AuditPercent\r\n" + 
					"					--SET @ToBeAuditedCount = CAST( @TotalAccounts AS DECIMAL(8,2))  * @AuditPercent / 100\r\n" + 
					"					\r\n" + 
					"					SET @AccountCurrentRow = 1\r\n" + 
					"\r\n" + 
					"					WHILE (@AccountCurrentRow <= @TotalAccounts AND @AccountCurrentRow <= @ToBeAuditedCount) \r\n" + 
					"					BEGIN \r\n" + 
					"						SELECT @TransID = TransID \r\n" + 
					"						FROM   #tblAccounts \r\n" + 
					"						WHERE  RowID = @AccountCurrentRow \r\n" + 
					"						------------------------------QcTransaction Start---------------------------------------\r\n" + 
					"						IF NOT EXISTS ( SELECT TransID FROM QcTransaction WHERE TransID = @TransID )\r\n" + 
					"						BEGIN\r\n" + 
					"							INSERT INTO QcTransaction(TransID,AllotedDate) VALUES(@TransID,getdate())\r\n" + 
					"							UPDATE AgentTransaction SET AuditStatusID = dbo.udfGetAuditStatusID('ALT') \r\n" + 
					"							WHERE TransID = @TransID\r\n" + 
					"							SET @QcAccountsCount = @QcAccountsCount + 1\r\n" + 
					"						END\r\n" + 
					"						------------------------------QcTransaction End---------------------------------------\r\n" + 
					"						SET @AccountCurrentRow = @AccountCurrentRow + 1 \r\n" + 
					"					END\r\n" + 
					"\r\n" + 
					"				END\r\n" + 
					"				---------------------------------------Agent Transaction End------------------------------------------------------------\r\n" + 
					"			END\r\n" + 
					"			SET @TargetCurrentRow = @TargetCurrentRow + 1 \r\n" + 
					"		END\r\n" + 
					"		--------------------------------------------------User Target End-------------------------------------------------------\r\n" + 
					"		SET @UserCurrentRow = @UserCurrentRow + 1 \r\n" + 
					"	END   \r\n" + 
					"	SELECT @QcAccountsCount as UserCurrentRow , @QcAccountsCount AS QcAccountsCount";
			
			statement = connection.createStatement();
			statement.execute(SQL);
			
			return true;
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}		
	}

	
	public HashMap<String, String> get_PriorityDetails(String fileName, String uploadDate) 
	{
		HashMap<String, String> priority = new HashMap<>();
		
		try
		{
			statement = connection.createStatement();
			
			String sqlQuery = "select top 1 Convert(varchar(10),UploadedDate,101)as [FileReceived], ImportFileID, ValidRecords from ImportFileDetails " + 
							  " where InventoryTypeID = 3 and Convert(varchar(10),UploadedDate,101) = '"+uploadDate+"' order by UploadedDate desc";
			resultSet = statement.executeQuery(sqlQuery);
						
			while (resultSet.next()) 
			{
				priority.put("fileReceived", resultSet.getString("FileReceived"));
				priority.put("importFileID", resultSet.getString("ImportFileID"));
				priority.put("validRecords", resultSet.getString("ValidRecords"));          
			}
			
			
			sqlQuery = "select count( distinct InventoryID) as completed, sum(TimeTaken) as timeTaken from AgentTransaction where InventoryID in( \r\n" + 
						"select InventoryID from Priorities where ImportFileID = " + priority.get("importFileID") +")";			
			resultSet = statement.executeQuery(sqlQuery);
			
			while (resultSet.next()) 
			{
				priority.put("completed", resultSet.getString("completed"));
				priority.put("timeTaken", resultSet.getString("timeTaken"));
			}
		}		
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return priority;
	}

	public ArrayList<String> get_ARTrailBalance(String practice, String insurance) 
	{
		ArrayList<String> dos =  new ArrayList<>();
		try
		{
			statement = connection.createStatement();
			
			String sqlQuery = "select DOS_Start from Inventory where (queueId <> 16 or InvStatusId = 5) and Current_Insurance_ID in \r\n" + 
						"(select i.insuranceId from ClientHierarchyMapping c\r\n" + 
						"join Practices p on p.PracticeID = c.practiceId\r\n" + 
						"join Locations l on l.LocationID = c.locationId\r\n" + 
						"join Clients cl on cl.clientID = c.clientId\r\n" + 
						"join Insurances i on i.chMApId = c.chmapId\r\n" + 
						"where p.practiceName = '"+practice+"' and PayerName = '"+insurance+"')";
			resultSet = statement.executeQuery(sqlQuery);
			
			while (resultSet.next()) 
			{
				dos.add(resultSet.getString("DOS_Start"));
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return dos;
	}


	public HashMap<String, String> get_LastImportFileDetails()
	{
		HashMap<String, String> importFile = new HashMap<>();
		
		try 
		{			
			String SQL = "select top 1 ImportFileID, FileName from importFileDetails  order by uploadeddate desc";
			
			statement = connection.createStatement();
			resultSet = statement.executeQuery(SQL);			
			
			while (resultSet.next()) 
			{
				importFile.put("fileName", resultSet.getString("FileName"));
				importFile.put("importFileID", resultSet.getString("ImportFileID"));
			}
			
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return importFile;
	}

	public void update_ImportFileDetails_UploadDate(String importFileId) 
	{
		try 
		{			
			String SQL = "update ImportFileDetails set UploadedDate = '2016-01-01' where ImportFileID = " + importFileId;
			
			statement = connection.createStatement();
			statement.execute(SQL);			
			
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
