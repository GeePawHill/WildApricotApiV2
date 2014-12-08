package org.geepawhill.wildapricot.data;

public class ApiContactField
{
	public String FieldName;
	public String Type;
	public boolean IsSystem;
	public String Access;
	public String FieldInstructions;
	public String Description;
	public ApiChoiceValue[] AllowedValues;
	
	@Override
	public String toString()
	{
		String result = FieldName+": "+Type+" "+Access+" "+IsSystem+"\n";
		result+="\t"+Description+"\n";
		if(Type.equals("MultipleChoice") || Type.equals("Choice"))
		{
			result+="\t";
			for(ApiChoiceValue value : AllowedValues)
			{
				result+=" "+value.Label;
			}
			result+="\n";
		}
		return result;
	}
}
