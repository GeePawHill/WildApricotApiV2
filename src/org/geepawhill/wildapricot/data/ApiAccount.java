package org.geepawhill.wildapricot.data;

public class ApiAccount
{
	public String Id;
	public String Name;
	public String Url;
	public String PrimaryDomainName;
	public ApiDetail[] Resources;
	
	@Override
	public String toString()
	{
		String result = "Account: "+Name+" Id: "+Id+"\n";
		for(ApiDetail detail : Resources) result+="\t"+detail;
		return result;
	}
}
