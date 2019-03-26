package customDataType;

public class ErrorByAgentReport 
{
	private int encounters;
	private double dollarValue;
	private double percent;
	
	private int totalEncounters;
	private double totalDollarValue;
	private double totalPercent;
	
	public ErrorByAgentReport() 
	{
		
	}
	
	public ErrorByAgentReport(int encounters, double dollarValue, double percent, int totalEncounters, double totalDollarValue, double totalPercent) 
	{
		this.encounters = encounters;
		this.dollarValue = dollarValue;
		this.percent = percent;
		
		this.totalEncounters = totalEncounters;
		this.totalDollarValue = totalDollarValue;
		this.totalPercent = totalPercent;
	}
	
	public int getEncounters() {
		return encounters;
	}


	public double getDollarValue() {
		return dollarValue;
	}


	public double getPercent() {
		return percent;
	}


	public int getTotalEncounters() {
		return totalEncounters;
	}


	public double getTotalDollarValue() {
		return totalDollarValue;
	}


	public double getTotalPercent() {
		return totalPercent;
	}

}
