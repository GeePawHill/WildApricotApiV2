package org.geepawhill.wildapricot;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.geepawhill.wildapricot.data.ApiAccount;
import org.geepawhill.wildapricot.data.ApiAuthenticationToken;
import org.geepawhill.wildapricot.data.ApiContactField;
import org.geepawhill.wildapricot.data.ApiContacts;
import org.geepawhill.wildapricot.data.ApiDetail;
import org.geepawhill.wildapricot.data.ApiVersion;

import com.google.gson.Gson;

public class WildApricot
{

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
	private CloseableHttpClient client;

	private ApiAuthenticationToken token;
	private String accountId;

	public WildApricot()
	{
		gson = new Gson();
		client = HttpClients.createDefault();
		accountId = "";
	}

	public ApiVersion[] fetchApiVersions() throws Exception
	{
		URI uri = makeBuilder("").build();
		return gson.fromJson(performFullQuery(uri), ApiVersion[].class);
	}
	
	public void chooseAccountId(String accountId)
	{
		this.accountId = accountId;
	}

	public ApiDetail[] fetchApiDetail(String version) throws Exception
	{
		URI uri = makeBuilder("/"+version).build();
		return gson.fromJson(performFullQuery(uri), ApiDetail[].class);
	}

	public ApiAccount[] fetchAccounts() throws Exception
	{
		URI uri = makeBuilder("/v2/Accounts").build();
		return gson.fromJson(performFullQuery(uri), ApiAccount[].class);
	}

	public ApiContactField[] fetchApiContactFields() throws Exception
	{
		URI uri = makeBuilder("/v1/Accounts/" + accountId + "/ContactFields").build();
		return gson.fromJson(performFullQuery(uri), ApiContactField[].class);
	}
	
	public ApiContacts fetchContactsUsing(String filterString) throws Exception
	{
		URI uri = makeBuilder("/v2/Accounts/"+accountId+"/Contacts").addParameter("$async","false").addParameter("$filter",filterString).build();
		return gson.fromJson(performFullQuery(uri), ApiContacts.class);
	}

	public ApiContacts fetchContactsUsing(List<NameValuePair> parameters) throws Exception
	{
		URI uri = makeBuilder("/v2/Accounts/"+accountId+"/Contacts").addParameter("$async","false").addParameters(parameters).build();
		return gson.fromJson(performFullQuery(uri), ApiContacts.class);
	}

	public ApiContacts fetchAllContacts() throws Exception
	{
		URI uri = makeBuilder("/v2/Accounts/"+accountId+"/Contacts").addParameter("$async","false").build();
		return gson.fromJson(performFullQuery(uri), ApiContacts.class);
	}

	private String performFullQuery(URI uri) throws Exception
	{
		HttpGet get = makeGet(uri);
		return performGet(get);
	}

	private String performGet(HttpGet get) throws Exception
	{
		SimpleResponse response = new SimpleResponse(client.execute(get));
		if(response.code==200) return response.body;
		System.out.println(response.code);
		System.out.println(response.body);
		throw new RuntimeException("GET request failed.\nCode: "+response.code+"\nBody:"+response.body);
	}

	private void addHeaders(HttpGet get)
	{
		get.addHeader(AUTHORIZATION_LABEL, AUTHORIZATION_BEARER+" "+token.access_token);
		get.addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE);
		get.addHeader(USER_AGENT_LABEL, USER_AGENT);
		get.addHeader(ACCEPT_LABEL, ACCEPT_JSON);
	}

	private HttpGet makeGet(URI uri)
	{
		HttpGet get = new HttpGet(uri);
		addHeaders(get);
		return get;
	}

	public void authenticate(String apikey) throws Exception
	{
		HttpPost post = makeAuthorizationPost(apikey);
		SimpleResponse response = new SimpleResponse(client.execute(post));
		if (response.code == 200)
		{
			token = gson.fromJson(response.body, ApiAuthenticationToken.class);
			return;
		}
		else
		{
			token = null;
		}
		System.out.println(response.code);
		System.out.println(response.body);
		throw new RuntimeException("Could not authorize.\nCode: "+response.code+"\nBody:"+response.body);
	}

	private HttpPost makeAuthorizationPost(String apikey) throws UnsupportedEncodingException
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

	private URIBuilder makeBuilder(String path)
	{
		return new URIBuilder().setScheme("https").setPath(path).setHost("api.wildapricot.org");
	}
}
