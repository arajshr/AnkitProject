package customDataType;

public class PMReport {
	private int accountCount;
	private double dollarValue;
	private double outstandingBalance;

	public PMReport(int accountCount, double dollarValue, double outstandingBalance) 
	{
		this.accountCount = accountCount;
		this.dollarValue = Math.round(dollarValue * 100.0) / 100.0;
		this.outstandingBalance = Math.round(outstandingBalance * 100.0) / 100.0;
	}

	public int getAccountCount() 
	{
		return accountCount;
	}

	public double getDollarValue() 
	{
		return dollarValue;
	}

	public double getOutstandingBalance()
	{
		return outstandingBalance;
	}

	public void update_ReportDetails(int account, double dollar, double balance) 
	{
		accountCount += account;
		dollarValue += dollar;
		outstandingBalance += balance;
	}
}
