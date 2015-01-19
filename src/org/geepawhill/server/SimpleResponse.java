package org.geepawhill.server;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;

public class SimpleResponse implements Serializable
{
	private static final long serialVersionUID = 8985047729553736921L;

	public final int code;
	public final String body;

	
	public static SimpleResponse fromBodyFile(int code,File fromFile) throws Exception
	{
		return new SimpleResponse(code,IOUtils.toString(new FileInputStream(fromFile)));
	}
	
	public SimpleResponse(int code,String body)
	{
		this.code = code;
		this.body = body;
	}
	
	public SimpleResponse(String body)
	{
		this.code = SimpleServer.STATUS_OK;
		this.body = body;
	}
	
	public SimpleResponse(CloseableHttpResponse response) throws Exception
	{
		code = response.getStatusLine().getStatusCode();
		body=readResponseBody(response);
	}
	
	public void toBodyFile(File toFile) throws Exception
	{
		IOUtils.write(body,new FileOutputStream(toFile));
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
