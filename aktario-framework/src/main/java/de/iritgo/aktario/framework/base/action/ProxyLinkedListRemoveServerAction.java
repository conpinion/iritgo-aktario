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
public class ProxyLinkedListRemoveServerAction extends FrameworkServerAction
{
	private long ownerUniqueId;

	private long iObjectUniqueId;

	private String iObjectTypeId;

	private String iObjectListName;

	private String ownerTypeId;

	/**
	 * Standard constructor
	 */
	public ProxyLinkedListRemoveServerAction ()
	{
	}

	/**
	 * Standard constructor
	 */
	public ProxyLinkedListRemoveServerAction (long ownerUniqueId, String ownerTypeId, String iObjectListName,
					IObject prototype)
	{
		this.ownerUniqueId = ownerUniqueId;
		this.ownerTypeId = ownerTypeId;
		iObjectTypeId = prototype.getTypeId ();
		iObjectUniqueId = prototype.getUniqueId ();
		this.iObjectListName = iObjectListName;
	}

	/**
	 * Read the attributes from the given stream.
	 */
	@Override
	public void readObject (FrameworkInputStream stream) throws IOException, ClassNotFoundException
	{
		ownerUniqueId = stream.readLong ();
		ownerTypeId = stream.readUTF ();
		iObjectUniqueId = stream.readLong ();
		iObjectTypeId = stream.readUTF ();
		iObjectListName = stream.readUTF ();
	}

	/**
	 * Write the attributes to the given stream.
	 */
	@Override
	public void writeObject (FrameworkOutputStream stream) throws IOException
	{
		stream.writeLong (ownerUniqueId);
		stream.writeUTF (ownerTypeId);
		stream.writeLong (iObjectUniqueId);
		stream.writeUTF (iObjectTypeId);
		stream.writeUTF (iObjectListName);
	}

	/**
	 * Perform the action.
	 */
	@Override
	public void perform ()
	{
		ClientTransceiver clientTransceiver = (ClientTransceiver) transceiver;

		DataObject owner = null;
		User user = (User) clientTransceiver.getConnectedChannel ().getCustomerContextObject ();

		if (ownerUniqueId <= 0)
		{
			// Its a new object and the client has always the wrong id;
			// Look in the Mapping
			owner = (DataObject) Engine.instance ().getBaseRegistry ().get (
							user.getNewObjectsMapping (new Long (ownerUniqueId)).longValue (), ownerTypeId);
		}
		else
		{
			owner = (DataObject) Engine.instance ().getBaseRegistry ().get (ownerUniqueId, ownerTypeId);
		}

		DataObject iObject = null;

		if (iObjectUniqueId <= 0)
		{
			iObject = (DataObject) Engine.instance ().getBaseRegistry ().get (
							user.getNewObjectsMapping (new Long (iObjectUniqueId)).longValue (), iObjectTypeId);
		}
		else
		{
			iObject = (DataObject) Engine.instance ().getBaseRegistry ().get (iObjectUniqueId, iObjectTypeId);
		}

		if ((owner == null) || (iObject == null))
		{
			Log.logError ("system", "ProxyLinkedListRemoveServerAction.perform", "Owner or object is null");

			return;
		}

		IObjectList iObjectList = (IObjectList) owner.getAttribute (iObjectListName);

		iObjectList.removeIObject (iObject);

		if (iObjectList == null)
		{
			Log.logError ("system", "ProxyLinkedListRemoveServerAction.perform", "Object list is null");

			return;
		}

		ProxyLinkedListRemoveAction proxyLinkedListRemoveAction = new ProxyLinkedListRemoveAction (iObject
						.getUniqueId (), iObject.getTypeId (), ownerUniqueId, ownerTypeId, iObjectListName);

		proxyLinkedListRemoveAction.setUniqueId (getUniqueId ());

		UserRegistry userRegistry = Server.instance ().getUserRegistry ();

		for (Iterator i = userRegistry.userIterator (); i.hasNext ();)
		{
			User onlineUser = (User) i.next ();

			if (onlineUser.isOnline ())
			{
				clientTransceiver.addReceiver (onlineUser.getNetworkChannel ());
			}
		}

		proxyLinkedListRemoveAction.setTransceiver (clientTransceiver);
		ActionTools.sendToClient (proxyLinkedListRemoveAction);

		Engine.instance ().getEventRegistry ().fire (
						"objectremoved",
						new IObjectListEvent (iObject, owner, iObjectListName, clientTransceiver,
										IObjectListEvent.REMOVE));
	}
}
