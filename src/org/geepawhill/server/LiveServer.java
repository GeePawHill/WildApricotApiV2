package org.geepawhill.server;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class LiveServer implements SimpleServer
{
	private final CloseableHttpClient client;
	
	public LiveServer(CloseableHttpClient client)
	{
		this.client = client;
	}
	
	public LiveServer()
	{
		this(HttpClients.createDefault());
	}

	@Override
	public SimpleResponse execute(HttpUriRequest request) throws Exception
	{
		return new SimpleResponse(client.execute(request));
	}
}
