package org.geepawhill.server;

import org.apache.http.client.methods.HttpUriRequest;

public interface SimpleServer
{
	static public final int STATUS_OK	=	200;
	
	public SimpleResponse execute(HttpUriRequest get) throws Exception;
}
