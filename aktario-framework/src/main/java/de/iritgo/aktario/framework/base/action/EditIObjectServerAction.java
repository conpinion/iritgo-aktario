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
import de.iritgo.aktario.core.iobject.NoSuchIObjectException;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.base.DataObject;
import de.iritgo.aktario.framework.base.IObjectModifiedEvent;
import de.iritgo.aktario.framework.server.Server;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.UserRegistry;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;


/**
 *
 */
public class EditIObjectServerAction extends FrameworkServerAction
{
	private IObject iObject;

	private long iObjectUniqueId;

	private String iObjectTypeId;

	/**
	 * Standard constructor
	 */
	public EditIObjectServerAction ()
	{
	}

	/**
	 * Standard constructor
	 */
	public EditIObjectServerAction (IObject iObject)
	{
		this.iObject = iObject;
		iObjectUniqueId = iObject.getUniqueId ();
		iObjectTypeId = iObject.getTypeId ();
	}

	/**
	 * Read the attributes from the given stream.
	 */
	@Override
	public void readObject (FrameworkInputStream stream) throws IOException, ClassNotFoundException
	{
		try
		{
			iObjectUniqueId = stream.readLong ();
			iObjectTypeId = stream.readUTF ();

			iObject = Engine.instance ().getIObjectFactory ().newInstance (iObjectTypeId);

			iObject.readObject (stream);
		}
		catch (NoSuchIObjectException x)
		{
			Log.logError ("system", "EditIObjectServerAction.readObject", "No such protoptye registered "
							+ x.getMessage ());
		}
	}

	/**
	 * Write the attributes to the given stream.
	 */
	@Override
	public void writeObject (FrameworkOutputStream stream) throws IOException
	{
		stream.writeLong (iObject.getUniqueId ());
		stream.writeUTF (iObjectTypeId);
		iObject.writeObject (stream);
	}

	/**
	 * Perform the action.
	 */
	@Override
	public void perform ()
	{
		ClientTransceiver clientTransceiver = (ClientTransceiver) transceiver;

		if (iObjectUniqueId <= 0)
		{
			// Its a new object and the client has always the wrong id;
			// Look in the Mapping
			User user = (User) clientTransceiver.getConnectedChannel ().getCustomerContextObject ();

			DataObject org = (DataObject) Engine.instance ().getBaseRegistry ().get (
							user.getNewObjectsMapping (new Long (iObjectUniqueId)).longValue (), iObjectTypeId);

			iObject.setUniqueId (org.getUniqueId ());
		}
		else
		{
			iObject.setUniqueId (iObjectUniqueId);
		}

		IObject updatediObject = assignCachedObjectToRealiObject ();

		Engine.instance ().getEventRegistry ().fire ("objectmodified",
						new IObjectModifiedEvent (updatediObject, clientTransceiver));

		UserRegistry userRegistry = Server.instance ().getUserRegistry ();

		for (Iterator i = userRegistry.userIterator (); i.hasNext ();)
		{
			User user = (User) i.next ();

			if (user.isOnline ())
			{
				clientTransceiver.addReceiver (user.getNetworkChannel ());
			}
		}

		EditIObjectAction editiObjectAction = new EditIObjectAction (EditIObjectAction.OK, iObject);

		editiObjectAction.setTransceiver (clientTransceiver);
		editiObjectAction.setUniqueId (getUniqueId ());
		ActionTools.sendToClient (editiObjectAction);
	}

	public IObject assignCachedObjectToRealiObject ()
	{
		try
		{
			ByteArrayOutputStream buffer = new ByteArrayOutputStream ();

			iObject.writeObject (new DataOutputStream (buffer));

			IObject updatediObject = (IObject) Engine.instance ().getBaseRegistry ().get (iObject.getUniqueId (),
							iObjectTypeId);

			updatediObject.readObject (new DataInputStream (new ByteArrayInputStream (buffer.toByteArray ())));

			return updatediObject;
		}
		catch (IOException x)
		{
		}
		catch (ClassNotFoundException x)
		{
		}

		return null;
	}
}
