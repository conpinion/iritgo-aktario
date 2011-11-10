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

package de.iritgo.aktario.framework.dataobject;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.framework.base.action.FrameworkAction;
import de.iritgo.aktario.framework.base.action.FrameworkInputStream;
import de.iritgo.aktario.framework.base.action.FrameworkOutputStream;
import de.iritgo.aktario.framework.base.action.NetworkFrameworkServerAction;
import de.iritgo.aktario.framework.server.Server;
import de.iritgo.aktario.framework.user.User;
import java.io.IOException;
import java.util.Iterator;


/**
 *
 */
public class AnnounceDynDataObjectRequest extends NetworkFrameworkServerAction
{
	private DynDataObject dynDataObject;

	/**
	 * Create a new HistoricalDataServerAction.
	 */
	public AnnounceDynDataObjectRequest()
	{
		setTypeId("ADDOREQ");
	}

	/**
	 * Create a new HistoricalDataServerAction.
	 */
	public AnnounceDynDataObjectRequest(DynDataObject dynDataObject)
	{
		this();
		this.dynDataObject = dynDataObject;
	}

	/**
	 * Read the attributes from the a stream.
	 *
	 * @param stream The stream to read from.
	 */
	public void readObject(FrameworkInputStream stream) throws IOException
	{
		String dynDataObjectTypeId = stream.readUTF();

		dynDataObject = new DynDataObject(dynDataObjectTypeId);

		try
		{
			dynDataObject.readTypeInformations(stream, dynDataObject);
		}
		catch (Exception x)
		{
			Log.log("network", "AnnounceDynDataObjectRequest.readObject", "Unknown objectType in:"
							+ dynDataObjectTypeId, Log.FATAL);
		}
	}

	/**
	 * Write the attributes to a stream.
	 *
	 * @param stream The stream to write to.
	 */
	public void writeObject(FrameworkOutputStream stream) throws IOException
	{
		stream.writeUTF(dynDataObject.getTypeId());
		dynDataObject.writeTypeInformations(stream, dynDataObject);
	}

	/**
	 * Get the action to execute in the client.
	 *
	 * @param clientTransceiver The client transceiver.
	 * @return The client action.
	 */
	public FrameworkAction getAction(ClientTransceiver clientTransceiver)
	{
		Engine.instance().getIObjectFactory().register(dynDataObject);

		Iterator iterator = Server.instance().getUserRegistry().onlineUserIterator();

		while (iterator.hasNext())
		{
			clientTransceiver.addReceiver(((User) iterator.next()).getNetworkChannel());
		}

		return new AnnounceDynDataObjectResponse(dynDataObject);
	}
}
