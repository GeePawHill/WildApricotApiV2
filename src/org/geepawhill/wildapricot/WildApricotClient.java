package org.geepawhill.wildapricot;

import java.net.URI;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.geepawhill.server.LiveServer;
import org.geepawhill.server.SimpleResponse;
import org.geepawhill.server.SimpleServer;
import org.geepawhill.wildapricot.data.ApiAccount;
import org.geepawhill.wildapricot.data.ApiAuthenticationToken;
import org.geepawhill.wildapricot.data.ApiContactField;
import org.geepawhill.wildapricot.data.ApiContacts;

import com.google.gson.Gson;

public class WildApricotClient
{

	private static final String V2_ACCOUNTS = "/v2/Accounts/";
	
	
	private static final String ALL_SCOPES = "general_info contacts finances events event_registrations account membership_levels settings";
	private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
	private static final String CONTENT_TYPE_LABEL = "Content-Type";
	private static final String ACCEPT_JSON = "application/json";
	private static final String ACCEPT_LABEL = "Accept";
	private static final String USER_AGENT = "open_wa_api_client";
	private static final String USER_AGENT_LABEL = "User-Agent";
	private static final String AUTH_URL = "https://oauth.wildapricot.org/auth/token";
	private static final String AUTHORIZATION_LABEL = "Authorization";
	private static final String AUTHORIZATION_BEARER = "Bearer";

	private Gson gson;

	
	public WildApricotClient()
	{
		gson = new Gson();
	}

	public ApiAccount[] fetchAccounts(SimpleServer server, String token) throws Exception
	{
		URI uri = makeUriBuilder(V2_ACCOUNTS).build();
		return gson.fromJson(performGet(server, token, uri), ApiAccount[].class);
	}

	public ApiContacts fetchContactsUsing(SimpleServer server, String token, String account, String filterString) throws Exception
	{
		URI uri = makeContactsUriBuilder(account).addParameter("$async","false").addParameter("$filter",filterString).build();
		String result = performGet(server, token, uri);
		return gson.fromJson(result, ApiContacts.class);
	}


	public ApiContacts fetchContactsUsing(SimpleServer server, String token, String account,List<NameValuePair> parameters) throws Exception
	{
		URI uri = makeContactsUriBuilder(account).addParameter("$async","false").addParameters(parameters).build();
		return gson.fromJson(performGet(server, token, uri), ApiContacts.class);
	}

	public ApiContacts fetchAllContacts(SimpleServer server, String token, String account) throws Exception
	{
		URI uri = makeContactsUriBuilder(account).addParameter("$async","false").build();
		return gson.fromJson(performGet(server, token, uri), ApiContacts.class);
	}
	
	public ApiContactField[] fetchContactFields(SimpleServer server, String token, String account) throws Exception
	{
		URI uri = makeUriBuilder(V2_ACCOUNTS+account+"/ContactFields").build();
		return gson.fromJson(performGet(server, token, uri), ApiContactField[].class);
	}

	public ApiAuthenticationToken authenticate(SimpleServer server,String apikey) throws Exception
	{
		HttpPost post = makeAuthorizationPost(apikey);
		SimpleResponse response = server.execute(post);
		ApiAuthenticationToken token = null;
		if (response.code == 200)
		{
			token = gson.fromJson(response.body, ApiAuthenticationToken.class);
			return token;
		}
		System.out.println(response.code);
		System.out.println(response.body);
		throw new RuntimeException("Could not authorize.\nCode: "+response.code+"\nBody:"+response.body);
	}

	private String performGet(SimpleServer server, String access, URI uri) throws Exception
	{
		HttpGet get = makeGet(access, uri);
		SimpleResponse response = server.execute(get);
		if(response.code==SimpleServer.STATUS_OK) return response.body;
		System.out.println(response.code);
		System.out.println(response.body);
		throw new RuntimeException("GET request failed.\nCode: "+response.code+"\nBody:"+response.body);
	}

	private HttpGet makeGet(String access, URI uri)
	{
		HttpGet get = new HttpGet(uri);
		get.addHeader(AUTHORIZATION_LABEL, AUTHORIZATION_BEARER+" "+access);
		get.addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE);
		get.addHeader(USER_AGENT_LABEL, USER_AGENT);
		get.addHeader(ACCEPT_LABEL, ACCEPT_JSON);
		return get;
	}
	
	private HttpPost makeAuthorizationPost(String apikey) throws Exception
	{
		HttpPost post = new HttpPost(AUTH_URL);
		post.addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE);
		
		String credentials = "APIKEY:" + apikey;
		String encodedCredentials = Base64.encodeBase64URLSafeString(credentials.getBytes());
		post.addHeader("Authorization", "Basic " + encodedCredentials + "==");

		String body = "grant_type=client_credentials&scope=" + ALL_SCOPES;
		HttpEntity bodyEntity = new ByteArrayEntity(body.getBytes("UTF-8"));
		post.setEntity(bodyEntity);
		return post;
	}

	private URIBuilder makeContactsUriBuilder(String account)
	{
		return makeUriBuilder("/v2/Accounts/"+account+"/Contacts");
	}
	
	private URIBuilder makeUriBuilder(String path)
	{
		return new URIBuilder().setScheme("https").setPath(path).setHost("api.wildapricot.org");
	}

	
}
