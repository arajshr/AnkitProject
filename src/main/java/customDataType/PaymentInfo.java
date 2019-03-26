package customDataType;

public class PaymentInfo 
{
	private String uploadDate;
	private String paymentPostedDate;
	private String paidDate;
	private String insuranceCode;	
	private String insuranceName;
	private String paidAmount;
	private String adjustment;
	private String dos;	
	private String checkNumber;
	
	public PaymentInfo(String uploadDate, String paymentPostedDate, String paidDate, String insuranceCode, String insuranceName, String paidAmount, String adjustment, String dos, String checkNumber) 
	{
		this.uploadDate = uploadDate;
		this.paymentPostedDate = paymentPostedDate;
		this.paidDate = paidDate;
		this.insuranceCode = insuranceCode;
		this.insuranceName = insuranceName;
		this.paidAmount = paidAmount;
		this.adjustment = adjustment;
		this.dos = dos;
		this.checkNumber = checkNumber;
	}

	public String getUploadDate() {
		return uploadDate;
	}

	public String getPaymentPostedDate() {
		return paymentPostedDate;
	}

	public String getPaidDate() {
		return paidDate;
	}

	public String getInsuranceCode() {
		return insuranceCode;
	}

	public String getInsuranceName() {
		return insuranceName;
	}

	public String getPaidAmount() {
		return paidAmount;
	}

	public String getAdjustment() {
		return adjustment;
	}

	public String getDos() {
		return dos;
	}

	public String getCheckNumber() {
		return checkNumber;
	}
	
	
}
