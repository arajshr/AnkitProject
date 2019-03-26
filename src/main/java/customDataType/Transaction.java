package customDataType;

public class Transaction 
{
	private boolean status;
	private String description;
	

	public Transaction(boolean status, String description)
	{
		this.status = status;
		this.description = description;
	}
	
	
	public boolean getStatus() {
		return status;
	}
	
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}	
}
