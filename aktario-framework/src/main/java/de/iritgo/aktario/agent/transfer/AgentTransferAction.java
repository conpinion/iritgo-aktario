/**
 * This file is part of the Iritgo/Aktario Framework.
 *
 * Copyright (C) 2005-2011 Iritgo Technologies.
 * Copyright (C) 2003-2005 BueroByte GbR.
 *
 * Iritgo licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.iritgo.aktario.agent.transfer;


import de.iritgo.aktario.agent.Agent;
import de.iritgo.aktario.agent.AgentManager;
import de.iritgo.aktario.core.iobject.IObjectSerializer;
import de.iritgo.aktario.core.iobject.NoSuchIObjectException;
import de.iritgo.aktario.framework.base.DataObject;
import de.iritgo.aktario.framework.base.action.FrameworkAction;
import de.iritgo.aktario.framework.base.action.FrameworkInputStream;
import de.iritgo.aktario.framework.base.action.FrameworkOutputStream;
import java.io.IOException;


/**
 * Far caster action, on client we recieved a entity and now we bing it to life.
 */
public class AgentTransferAction extends FrameworkAction
{
	/** The IObject / DataObject stream serializer */
	private static IObjectSerializer streamSerializer = new IObjectSerializer ();

	/** The DataObject */
	private DataObject dataObject;

	/** The currentTime for this createn */
	private long currentTime;

	/**
	 * Standard constructor
	 */
	public AgentTransferAction ()
	{
		super (- 1);
	}

	/**
	 * Standard constructor
	 */
	public AgentTransferAction (DataObject dataObject)
	{
		currentTime = System.currentTimeMillis ();
		this.dataObject = dataObject;
	}

	/**
	 * Read the attributes from the given stream.
	 */
	@Override
	public void readObject (FrameworkInputStream stream) throws IOException, ClassNotFoundException
	{
		currentTime = stream.readLong ();

		try
		{
			dataObject = (DataObject) streamSerializer.read (stream);
		}
		catch (NoSuchIObjectException x)
		{
		}
	}

	/**
	 * Write the attributes to the given stream.
	 */
	@Override
	public void writeObject (FrameworkOutputStream stream) throws IOException
	{
		stream.writeLong (currentTime);
		streamSerializer.write (stream, dataObject);
	}

	/**
	 * Perform the action.
	 */
	@Override
	public void perform ()
	{
		AgentManager agentManager = AgentManager.instance ();

		agentManager.bringAgentToLife ((Agent) dataObject);
	}
}
