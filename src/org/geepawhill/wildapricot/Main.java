package org.geepawhill.wildapricot;

import org.geepawhill.wildapricot.data.ApiAccount;
import org.geepawhill.wildapricot.data.ApiContact;
import org.geepawhill.wildapricot.data.ApiContacts;

public class Main
{
	
	static final String APIKEY = "EDIT THIS TO HOLD YOUR APIKEY";
	
	public static void main(String[] args) throws Exception
	{
		
		WildApricot client = new WildApricot();
		client.authenticate(APIKEY);
		ApiAccount[] accounts = client.fetchAccounts();
		client.chooseAccountId(accounts[0].Id);
		ApiContacts contacts = client.fetchAllContacts();
		for(ApiContact contact : contacts.Contacts)
		{
			System.out.println(contact.LastName+", "+contact.FirstName);
		}
	}
}
