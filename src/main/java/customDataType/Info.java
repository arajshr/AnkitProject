package customDataType;

public class Info 
{
	private boolean status;
	private String description;
	
	public Info(boolean found, String description) 
	{
		this.status = found;
		this.description = description;
	}

	
	public boolean getStatus() {
		return status;
	}

	public String getDescription() {
		return description;
	}	
	
}