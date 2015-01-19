package org.geepawhill.wildapricot.data;

import com.google.gson.JsonElement;

public class ApiFieldValue
{
	public String FieldName;
	public JsonElement Value;
	
	public ApiFieldValue()
	{}
	
	public ApiFieldValue(String FieldName,JsonElement Value)
	{
		this.FieldName = FieldName;
		this.Value = Value;
	}
}
