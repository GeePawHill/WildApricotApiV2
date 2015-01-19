package org.geepawhill.wildapricot;

import org.geepawhill.server.LiveServer;
import org.geepawhill.wildapricot.data.ApiAccount;
import org.geepawhill.wildapricot.data.ApiAuthenticationToken;
import org.geepawhill.wildapricot.data.ApiContact;
import org.geepawhill.wildapricot.data.ApiContacts;

public class Main
{
	
	static final String APIKEY = "EDIT THIS TO HOLD YOUR APIKEY";
	
	public static void main(String[] args) throws Exception
	{
		
		WildApricotClient client = new WildApricotClient();
		LiveServer server = new LiveServer();
		ApiAuthenticationToken token = client.authenticate(server,APIKEY);
		ApiAccount[] accounts = client.fetchAccounts(server,token.access_token);
		ApiContacts contacts = client.fetchAllContacts(server,token.access_token,accounts[0].Id);
		for(ApiContact contact : contacts.Contacts)
		{
			System.out.println(contact.LastName+", "+contact.FirstName);
		}
	}
}
