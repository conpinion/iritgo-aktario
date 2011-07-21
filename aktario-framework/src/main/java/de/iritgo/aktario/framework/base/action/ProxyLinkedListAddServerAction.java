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

package de.iritgo.aktario.framework.base.action;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.iobject.IObjectList;
import de.iritgo.aktario.core.iobject.IObjectListEvent;
import de.iritgo.aktario.core.iobject.NoSuchIObjectException;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.base.DataObject;
import de.iritgo.aktario.framework.server.Server;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.UserRegistry;
import java.io.IOException;
import java.util.Iterator;


/**
 *
 */
public class ProxyLinkedListAddServerAction extends FrameworkServerAction
{
	private IObject prototype;

	@SuppressWarnings("unused")
	private long prototypeUniqueId;

	private long ownerUniqueId;

	private String prototypeId;

	private String proxyLinkedListId;

	private String ownerTypeId;

	/**
	 * Standard constructor
	 */
	public ProxyLinkedListAddServerAction ()
	{
	}

	/**
	 * Standard constructor
	 */
	public ProxyLinkedListAddServerAction (long ownerUniqueId, String ownerTypeId, String proxyLinkedListId,
					IObject prototype)
	{
		this.prototype = prototype;
		this.ownerUniqueId = ownerUniqueId;
		this.proxyLinkedListId = proxyLinkedListId;
		prototypeId = prototype.getTypeId ();
		this.ownerTypeId = ownerTypeId;
	}

	/**
	 * Read the attributes from the given stream.
	 */
	@Override
	public void readObject (FrameworkInputStream stream) throws IOException, ClassNotFoundException
	{
		try
		{
			ownerTypeId = stream.readUTF ();
			ownerUniqueId = stream.readLong ();
			prototypeUniqueId = stream.readLong ();
			prototypeId = stream.readUTF ();
			proxyLinkedListId = stream.readUTF ();
			prototype = Engine.instance ().getIObjectFactory ().newInstance (prototypeId);
			prototype.readObject (stream);
		}
		catch (NoSuchIObjectException x)
		{
			Log.log ("network", "ProxyLinkedListAddServerAction.readObject",
							"DataObject not registred: " + prototypeId, Log.FATAL);
		}
	}

	/**
	 * Write the attributes to the given stream.
	 */
	@Override
	public void writeObject (FrameworkOutputStream stream) throws IOException
	{
		stream.writeUTF (ownerTypeId);
		stream.writeLong (ownerUniqueId);
		stream.writeLong (prototype.getUniqueId ());
		stream.writeUTF (prototypeId);
		stream.writeUTF (proxyLinkedListId);
		prototype.writeObject (stream);
	}

	/**
	 * Perform the action.
	 */
	@Override
	public void perform ()
	{
		ClientTransceiver clientTransceiver = (ClientTransceiver) transceiver;
		DataObject owner = null;

		if (ownerUniqueId <= 0)
		{
			// Its a new object and the client has always the wrong id;
			// Look in the Mapping
			User user = (User) clientTransceiver.getConnectedChannel ().getCustomerContextObject ();

			owner = (DataObject) Engine.instance ().getBaseRegistry ().get (
							user.getNewObjectsMapping (new Long (ownerUniqueId)).longValue (), ownerTypeId);
		}
		else
		{
			owner = (DataObject) Engine.instance ().getBaseRegistry ().get (ownerUniqueId, ownerTypeId);
		}

		long newUniqueId = Engine.instance ().getPersistentIDGenerator ().createId ();
		long oldUniqueId = prototype.getUniqueId ();

		if (owner == null)
		{
			Log.logError ("system", "ProxyLinkedListAddServerAction.perform", "Owner is null");

			return;
		}

		IObjectList proxyLinkedList = (IObjectList) owner.getAttribute (proxyLinkedListId);

		if (proxyLinkedList == null)
		{
			Log.logError ("system", "ProxyLinkedListAddServerAction.perform", "Proxy linked list is null");

			return;
		}

		prototype.setUniqueId (newUniqueId);

		proxyLinkedList.add (prototype);

		((User) clientTransceiver.getConnectedChannel ().getCustomerContextObject ()).putNewObjectsMapping (new Long (
						oldUniqueId), new Long (newUniqueId));

		ProxyLinkedListAddAction proxyLinkedListAction = new ProxyLinkedListAddAction (oldUniqueId, newUniqueId,
						prototype.getTypeId (), owner.getUniqueId (), owner.getTypeId ());

		clientTransceiver.addReceiver (clientTransceiver.getSender ());
		proxyLinkedListAction.setTransceiver (clientTransceiver);
		proxyLinkedListAction.setUniqueId (getUniqueId ());
		ActionTools.sendToClient (proxyLinkedListAction);

		clientTransceiver = new ClientTransceiver (clientTransceiver.getSender (), clientTransceiver
						.getConnectedChannel ());

		Engine.instance ().getEventRegistry ().fire (
						"objectcreated",
						new IObjectListEvent (prototype, owner, proxyLinkedListId, clientTransceiver,
										IObjectListEvent.ADD));

		UserRegistry userRegistry = Server.instance ().getUserRegistry ();

		for (Iterator i = userRegistry.onlineUserIterator (); i.hasNext ();)
		{
			User user = (User) i.next ();

			clientTransceiver.addReceiver (user.getNetworkChannel ());
		}

		EditIObjectAction editPrototypeAction = new EditIObjectAction (EditIObjectAction.OK, owner);

		editPrototypeAction.setTransceiver (clientTransceiver);
		editPrototypeAction.setUniqueId (getUniqueId ());
		ActionTools.sendToClient (editPrototypeAction);
	}
}
