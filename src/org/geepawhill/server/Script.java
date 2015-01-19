package org.geepawhill.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Script implements Serializable
{

	private static final long serialVersionUID = 8948973834311724368L;
	
	public List<SimpleResponse> responses;
	
	public Script()
	{
		responses = new ArrayList<SimpleResponse>();
	}

}
