package com.shobhit;





import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class Citizen {
	
	int challans=0;
	
	@Id
	String id;
	String name,fname,address,status;
	
	public float balance=0;
	
	//setter methods
	public void setId(String id)
	{
		this.id=id;
	}
	
	public void setChallans(int ch)
	{
		this.challans=ch;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public void setFname(String fname)
	{
		this.fname=fname;
	}
	
	public void setAddress(String address)
	{
		this.address=address;
	}
	
	public void setStatus(String status)
	{
		this.status=status;
	}
	
	public void setBalance(float b)
	{
		this.balance=b;
	}
	
	//getter methods
	public String getId()
	{
		return this.id;
	}
	public int getChallans()
	{
		return this.challans;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getFname()
	{
		return this.fname;
	}
	
	public String getAddress()
	{
		return this.address;
	}
	
	public String getStatus()
	{
		return this.status;
	}
	public float getBalance()
	{
		return this.balance;
	}
	
}

