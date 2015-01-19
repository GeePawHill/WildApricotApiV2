package org.geepawhill.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class TestingServer implements SimpleServer
{
	
	public Script script;
	
	private boolean isRecording;
	private LiveServer server;
	private int nextResponse;

	public TestingServer(CloseableHttpClient client)
	{
		script = new Script();
		isRecording = true;
		server = new LiveServer(client);
		nextResponse=0;
	}
	
	public TestingServer()
	{
		this(HttpClients.createDefault());
	}

	@Override
	public SimpleResponse execute(HttpUriRequest request) throws Exception
	{
		if(isRecording) return executeWhileRecording(request);
		else return executeWhilePlaying();
	}
	
	private SimpleResponse executeWhilePlaying()
	{
		return script.responses.get(nextResponse++);
	}

	private SimpleResponse executeWhileRecording(HttpUriRequest request) throws Exception
	{
		SimpleResponse response = server.execute(request);
		script.responses.add(response);
		return response;
	}
	
	public void saveResponseBody(File file,int index) throws Exception
	{
		SimpleResponse response = script.responses.get(index);
		response.toBodyFile(file);
	}
	
	public void saveScript(File file) throws Exception
	{
		ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file));
		output.writeObject(script);
		output.close();
	}
	
	public void loadScript(File file) throws Exception
	{
		ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
		script = (Script) input.readObject();
		input.close();
	}

}
