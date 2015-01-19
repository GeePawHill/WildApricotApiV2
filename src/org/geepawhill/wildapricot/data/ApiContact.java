package org.geepawhill.wildapricot.data;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class ApiContact
{
	
	static public final String ADDRESS_TAG	=	"Address";
	static public final String ACCESS_ID_TAG =	"AccessID";

	public int Id;
	public String FirstName;
	public String LastName;
	
	public String Url;
	public String Email;
	public String DisplayName;
	public String Organization;
	public String ProfileLastUpdated;
	public ArrayList<ApiFieldValue> FieldValues;
	
	public ApiContact()
	{
		
	}
	
	public ApiContact(String firstName,String lastName,int id)
	{
		this.FirstName = firstName;
		this.LastName = lastName;
		this.Id = id;
	}
	
	public String makeNameKey()
	{
		return LastName+", "+FirstName;
	}
	
	public boolean getBooleanByName(String name)
	{
		JsonElement element = getValueByName(name);
		if(element==null) return false;
		if(element.isJsonNull()) return false;
		return element.getAsBoolean();
	}
	
	public String getStringByName(String name)
	{
		JsonElement element = getValueByName(name);
		if(element==null) return "";
		if(element.isJsonNull()) return "";
		return element.getAsString();
	}
	
	public JsonElement getValueByName(String name)
	{
		for(ApiFieldValue value : FieldValues)
		{
			if(value.FieldName.equalsIgnoreCase(name)) return value.Value;
		}
		return null;
	}
	
	public void addOrReplaceFieldValue(ApiFieldValue value)
	{
		for(int v=0; v<FieldValues.size();v++)
		{
			if( FieldValues.get(v).FieldName.equalsIgnoreCase(value.FieldName))
			{
				FieldValues.set(v, value);
				return;
			}
		}
		FieldValues.add(value);
	}
	
	public void addOrReplaceString(String name,String value)
	{
		ApiFieldValue apiValue = new ApiFieldValue();
		apiValue.FieldName =name;
		apiValue.Value = new Gson().fromJson ("{"+value+"}", JsonElement.class);
		for(int v=0; v<FieldValues.size();v++)
		{
			if( FieldValues.get(v).FieldName.equalsIgnoreCase(name))
			{
				FieldValues.set(v, apiValue);
				return;
			}
		}
		FieldValues.add(apiValue);
	}
}
