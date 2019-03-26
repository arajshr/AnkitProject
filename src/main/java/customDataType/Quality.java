package customDataType;

import java.text.DecimalFormat;

public class Quality 
{
	private int worked;
	private int audited;
	private int error;
	private double errorPercent;
	private double accuracy;
	
	public Quality(int worked, int audited, int error, double errorPercent, double accuracy) 
	{
		this.worked = worked;
		this.audited = audited;
		this.error = error;
		this.errorPercent = errorPercent;
		this.accuracy = accuracy;
	}
	
	public void update_Quality(int iWork, int iAudit, int iError)
	{
		try 
		{
			worked += iWork;
			audited += iAudit;
			error += iError;
			
			//error percent
			errorPercent = Double.parseDouble(new DecimalFormat("#.00").format(((double)error/audited) * 100)) ;
			
			// get accuracy calculation			
			accuracy = Double.parseDouble(new DecimalFormat("#.00").format((((double)audited - error)/audited)*100));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public int getWorked() {
		return worked;
	}

	public int getAudited() {
		return audited;
	}

	public int getError() {
		return error;
	}

	public double getErrorPercent() {
		return errorPercent;
	}

	public double getAccuracy() {
		return accuracy;
	}
}
