package org.geepawhill.wildapricot;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.apache.http.client.methods.CloseableHttpResponse;

public class SimpleResponse
{
	public final int code;
	public final String body;
	
	public SimpleResponse(CloseableHttpResponse response) throws Exception
	{
		code = response.getStatusLine().getStatusCode();
		body=readResponseBody(response);
	}
	
	private String readResponseBody(CloseableHttpResponse response) throws Exception
	{
		try
		{
			OutputStream outputStream = new ByteArrayOutputStream();
			response.getEntity().writeTo(outputStream);
			return outputStream.toString();
		}
		finally
		{
			response.close();
		}

	}
}
