package customDataType;

public class ClaimInfo 
{

	private String pateintAccountNumber;
	private String UID;
	private String encounter;
	private String insBalance;   //float
	private boolean status;
	private String description;
	
	/*private String encounterID;
	private float totalCharges;*/
	
	public ClaimInfo()
	{
		
	}
	
	public ClaimInfo(boolean status)
	{	
		this.status = status;		
	}
	
	public ClaimInfo(boolean status, String description)
	{
		this.status = status;
		this.description = description;
	}
	
	public ClaimInfo(String pateintAccountNumber, boolean status, String description)
	{
		this.pateintAccountNumber = pateintAccountNumber;
		this.status = status;
		this.description = description;
	}	

	
	public ClaimInfo(String pateintAccountNumber, String UID,  String encounter, String insBalance, boolean status, String description)
	{
		this.pateintAccountNumber = pateintAccountNumber;
		this.status = status;
		this.description = description;
		this.UID = UID;
		this.encounter = encounter;
		this.insBalance = insBalance;
	}	
	
	public String getUID() {
		return UID;
	}

	public void setUID(String UID) {
		this.UID = UID;
	}
	
	public String getInsBalance() {
		return insBalance;
	}

	public void setInsBalance(String insBalance) {
		this.insBalance = insBalance;
	}
	
	public boolean getStatus() {
		return status;
	}
	
	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getPateintAccountNumber() {
		return pateintAccountNumber;
	}

	public void setPateintAccountNumber(String pateintAccountNumber) {
		this.pateintAccountNumber = pateintAccountNumber;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public String getEncounter() {
		return encounter;
	}
	
	public void setEncounter(String encounter) {
		this.encounter = encounter;
	}
	
	
	

	
}
