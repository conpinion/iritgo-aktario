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

package de.iritgo.aktario.framework.base.command;


import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.base.action.EditIObjectAction;
import de.iritgo.aktario.framework.server.Server;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.UserRegistry;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 *
 */
public class EditIObject extends Command
{
	private List prototypes;

	public EditIObject ()
	{
		super ("EditIObject");
		prototypes = new LinkedList ();
	}

	public EditIObject (IObject prototype)
	{
		this ();
		prototypes.add (prototype);
	}

	public void addPrototype (IObject prototype)
	{
		prototypes.add (prototype);
	}

	/**
	 * Send all connected users the edit command, now the client will refresh the object.
	 */
	@Override
	public void perform ()
	{
		ClientTransceiver clientTransceiver = new ClientTransceiver (0);

		UserRegistry userRegistry = Server.instance ().getUserRegistry ();

		for (Iterator i = userRegistry.onlineUserIterator (); i.hasNext ();)
		{
			User user = (User) i.next ();

			clientTransceiver.addReceiver (user.getNetworkChannel ());
		}

		EditIObjectAction editPrototypeAction = new EditIObjectAction (EditIObjectAction.OK);

		for (Iterator i = prototypes.iterator (); i.hasNext ();)
		{
			editPrototypeAction.addIObject ((IObject) i.next ());
		}

		editPrototypeAction.setTransceiver (clientTransceiver);
		editPrototypeAction.setUniqueId (getUniqueId ());

		ActionTools.sendToClient (editPrototypeAction);
	}
}
