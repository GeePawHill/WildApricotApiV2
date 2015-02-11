package org.geepawhill.server;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class ProxyServer implements SimpleServer
{
	private final CloseableHttpClient client;

	@Override
	public SimpleResponse execute(HttpUriRequest request) throws Exception
	{
		return new SimpleResponse(client.execute(request));
	}

	public ProxyServer(HttpHost proxy) throws Exception
	{
		CloseableHttpClient httpclient = HttpClients.custom().setProxy(proxy).build();
		this.client = httpclient;
	}
}
