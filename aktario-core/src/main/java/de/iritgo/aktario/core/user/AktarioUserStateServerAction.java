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

package de.iritgo.aktario.core.user;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.base.action.FrameworkAction;
import de.iritgo.aktario.framework.base.action.FrameworkInputStream;
import de.iritgo.aktario.framework.base.action.FrameworkOutputStream;
import de.iritgo.aktario.framework.server.Server;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.UserRegistry;
import java.io.IOException;
import java.util.Iterator;


/**
 * @version $Id: AktarioUserStateServerAction.java,v 1.8 2006/09/25 10:34:30 grappendorf Exp $
 */
public class AktarioUserStateServerAction extends FrameworkAction
{
	/** The user asking for state information. */
	protected long userId;

	/**
	 * Create a new action.
	 */
	public AktarioUserStateServerAction ()
	{
		setTypeId ("AktarioUserStateServerAction");
	}

	/**
	 * Create a new action.
	 *
	 * @param user The requesting user.
	 */
	public AktarioUserStateServerAction (User user)
	{
		this ();
		this.userId = user.getUniqueId ();
	}

	/**
	 * Read the attributes from a stream.
	 */
	@Override
	public void readObject (FrameworkInputStream stream) throws IOException
	{
		userId = stream.readLong ();
	}

	/**
	 * Write the attributes to a stream.
	 */
	@Override
	public void writeObject (FrameworkOutputStream stream) throws IOException
	{
		stream.writeLong (userId);
	}

	/**
	 * Perform the action.
	 */
	@Override
	public void perform ()
	{
		UserRegistry userRegistry = Server.instance ().getUserRegistry ();
		AktarioUserManager userManager = (AktarioUserManager) Engine.instance ().getManager ("AktarioUserManager");
		User user = userRegistry.getUser (userId);

		ClientTransceiver ct = new ClientTransceiver (user.getNetworkChannel ());

		if (user == null)
		{
			return;
		}

		ct.addReceiver (user.getNetworkChannel ());

		AktarioUserStateAction action = new AktarioUserStateAction (true);

		for (Iterator i = userRegistry.userIterator (); i.hasNext ();)
		{
			User aUser = (User) i.next ();

			if (aUser.isOnline ())
			{
				action.addUser (userManager.getUserRegistry ().getUserByName (aUser.getName ()));
			}
		}

		action.setTransceiver (ct);
		ActionTools.sendToClient (action);
	}
}
