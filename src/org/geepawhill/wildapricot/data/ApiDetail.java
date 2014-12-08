package org.geepawhill.wildapricot.data;

public class ApiDetail
{
	public String Name;
	public String Url;
	public String[] AllowedOperations;
	
	public String toString()
	{
		String result= "Api: "+Name+"\t"+Url;
		
		// version 2
		if(AllowedOperations!=null)
		{
			for(String operation : AllowedOperations) result+=" "+operation;
		}
		return result+"\n";
	}
}
