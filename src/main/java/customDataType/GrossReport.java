package customDataType;

public class GrossReport 
{
	private double billedAmount;
	private double totalPayment;
	private double totalAdjustmentAmount;
	private double GCR;
	private double NCR;
	
	public GrossReport(double billedAmount, double totalPayment, double totalAdjustmentAmount, double GCR, double NCR) 
	{
		this.billedAmount = billedAmount;
		this.totalPayment = totalPayment;
		this.totalAdjustmentAmount = totalAdjustmentAmount;
		this.GCR = GCR;
		this.NCR = NCR;
	}

	public double getBilledAmount() {
		return billedAmount;
	}

	public double getTotalPayment() {
		return totalPayment;
	}

	public double getTotalAdjustmentAmount() {
		return totalAdjustmentAmount;
	}

	public double getGCR() {
		return GCR;
	}

	public double getNCR() {
		return NCR;
	}
	
	
}
