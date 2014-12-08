package org.geepawhill.wildapricot;

import static org.junit.Assert.*;

import org.geepawhill.wildapricot.data.ApiAccount;
import org.geepawhill.wildapricot.data.ApiContact;
import org.geepawhill.wildapricot.data.ApiContacts;
import org.junit.Before;
import org.junit.Test;

public abstract class WildApricotBaseTest
{
	
	WildApricot client;
	
	protected abstract String getApiKey();
	protected abstract String getFirstAccountId();
	protected abstract String getKnownFirstName();
	protected abstract String getKnownLastName();
	
	
	@Before
	public void before() throws Exception
	{
		client = new WildApricot();
		client.authenticate(getApiKey());
		client.chooseAccountId(getFirstAccountId());
	}

	@Test
	public void authenticates() throws Exception
	{
		WildApricot authClient = new WildApricot();
		authClient.authenticate(getApiKey());
	}

	@Test
	public void fetchesAccounts() throws Exception
	{
		ApiAccount[] accounts = client.fetchAccounts();
		assertEquals(1, accounts.length);
		assertEquals(getFirstAccountId(), accounts[0].Id);
	}

	@Test
	public void fetchesAllContacts() throws Exception
	{
		ApiContacts contacts = client.fetchAllContacts();
		assertTrue(contacts.Contacts.length > 0);
		for (ApiContact contact : contacts.Contacts)
		{
			if (contact.LastName.equals(getKnownLastName()))
			{
				assertEquals(getKnownFirstName(), contact.FirstName);
				System.out.println(contact.Id);
				return;
			}
		}
		fail("Could not find contact:" + getKnownFirstName()+" "+getKnownLastName());
	}


}
