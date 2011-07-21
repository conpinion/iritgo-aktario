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
import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.iobject.IObjectProxy;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.base.DataObject;
import de.iritgo.aktario.framework.base.FrameworkProxy;
import de.iritgo.aktario.framework.base.action.EditIObjectAction;
import de.iritgo.aktario.framework.base.action.FrameworkInputStream;
import de.iritgo.aktario.framework.base.action.FrameworkOutputStream;
import de.iritgo.aktario.framework.base.action.NetworkFrameworkServerAction;
import de.iritgo.aktario.framework.server.Server;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.UserRegistry;
import java.io.IOException;
import java.util.Iterator;


/**
 *
 */
public class AddDataObjectRequest extends NetworkFrameworkServerAction
{
	private DataObject dataObject;

	private long dataObjectUniqueId;

	private String dataObjectTypeId;

	/**
	 * Create a new HistoricalDataServerAction.
	 */
	public AddDataObjectRequest ()
	{
		setTypeId ("ADORQ");
	}

	/**
	 * Create a new HistoricalDataServerAction.
	 */
	public AddDataObjectRequest (DataObject dataObject)
	{
		this ();
		this.dataObject = dataObject;
		dataObjectUniqueId = dataObject.getUniqueId ();
		dataObjectTypeId = dataObject.getTypeId ();
	}

	/**
	 * Read the attributes from the a stream.
	 *
	 * @param stream The stream to read from.
	 */
	public void readObject (FrameworkInputStream stream) throws IOException
	{
		try
		{
			dataObjectUniqueId = stream.readLong ();

			dataObjectTypeId = stream.readUTF ();
			dataObject = (DataObject) Engine.instance ().getIObjectFactory ().newInstance (dataObjectTypeId);
			dataObject.readObject (stream);
		}
		catch (Exception x)
		{
			x.printStackTrace ();

			// TODO: This is a hardcore bug! You have forgotten to register the DataObject!
			Log.log ("network", "AddDataObject.readObject", "DataObject not registred: " + dataObjectTypeId, Log.FATAL);
		}
	}

	/**
	 * Write the attributes to a stream.
	 *
	 * @param stream The stream to write to.
	 */
	public void writeObject (FrameworkOutputStream stream) throws IOException
	{
		stream.writeLong (dataObject.getUniqueId ());
		stream.writeUTF (dataObjectTypeId);
		dataObject.writeObject (stream);
	}

	/**
	 * Perform this action.
	 *
	 */
	public void perform ()
	{
		ClientTransceiver clientTransceiver = (ClientTransceiver) transceiver;

		long newUniqueId = Engine.instance ().getPersistentIDGenerator ().createId ();
		long oldUniqueId = dataObject.getUniqueId ();

		dataObject.setUniqueId (newUniqueId);

		((User) clientTransceiver.getConnectedChannel ().getCustomerContextObject ()).putNewObjectsMapping (new Long (
						oldUniqueId), new Long (newUniqueId));

		IObjectProxy proxy = (IObjectProxy) new FrameworkProxy ();

		proxy.setSampleRealObject ((IObject) dataObject);
		Engine.instance ().getBaseRegistry ().add ((BaseObject) dataObject);
		Engine.instance ().getProxyRegistry ().addProxy (proxy, dataObject.getTypeId ());

		AddDataObjectResponse addDataObjectResponse = new AddDataObjectResponse (oldUniqueId, newUniqueId, dataObject
						.getTypeId ());

		clientTransceiver.addReceiver (clientTransceiver.getSender ());
		addDataObjectResponse.setTransceiver (clientTransceiver);
		addDataObjectResponse.setUniqueId (getUniqueId ());
		ActionTools.sendToClient (addDataObjectResponse);

		clientTransceiver = new ClientTransceiver (clientTransceiver.getSender (), clientTransceiver
						.getConnectedChannel ());

		// 		Engine.instance ().getEventRegistry ().fire (
		// 			"objectcreated",
		// 			new IObjectListEvent(
		// 				prototype, owner, proxyLinkedListId, clientTransceiver, IObjectListEvent.ADD));
		UserRegistry userRegistry = Server.instance ().getUserRegistry ();

		for (Iterator i = userRegistry.onlineUserIterator (); i.hasNext ();)
		{
			User user = (User) i.next ();

			clientTransceiver.addReceiver (user.getNetworkChannel ());
		}

		EditIObjectAction editPrototypeAction = new EditIObjectAction (EditIObjectAction.OK, dataObject);

		editPrototypeAction.setTransceiver (clientTransceiver);
		editPrototypeAction.setUniqueId (getUniqueId ());
		ActionTools.sendToClient (editPrototypeAction);
	}
}
