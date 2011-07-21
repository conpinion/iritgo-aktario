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

package de.iritgo.aktario.framework.user.action;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.core.uid.IDGenerator;
import de.iritgo.aktario.framework.base.action.FrameworkAction;
import de.iritgo.aktario.framework.base.action.FrameworkInputStream;
import de.iritgo.aktario.framework.base.action.FrameworkOutputStream;
import de.iritgo.aktario.framework.base.action.NetworkFrameworkServerAction;
import de.iritgo.aktario.framework.server.Server;
import de.iritgo.aktario.framework.user.NewUserEvent;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.UserRegistry;
import java.io.IOException;


/**
 *
 */
public class RegisterNewUserServerAction extends NetworkFrameworkServerAction
{
	private String userName;

	private String email;

	/**
	 * Standard constructor
	 */
	public RegisterNewUserServerAction ()
	{
		super (- 1);
	}

	/**
	 * Standard constructor
	 */
	public RegisterNewUserServerAction (String userName, String email)
	{
		super (- 1);

		this.userName = userName;
		this.email = email;
	}

	/**
	 * Get the id of the iritgo object.
	 */
	public String getTypeId ()
	{
		return "server.action.registernewuser";
	}

	/**
	 * Get the UserName.
	 */
	public String getUserName ()
	{
		return userName;
	}

	/**
	 * Read the attributes from the given stream.
	 */
	public void readObject (FrameworkInputStream stream) throws IOException, ClassNotFoundException
	{
		userName = stream.readUTF ();
		email = stream.readUTF ();
	}

	/**
	 * Write the attributes to the given stream.
	 */
	public void writeObject (FrameworkOutputStream stream) throws IOException
	{
		stream.writeUTF (userName);
		stream.writeUTF (email);
	}

	public FrameworkAction getAction (ClientTransceiver clientTransceiver)
	{
		UserRegistry userRegistry = Server.instance ().getUserRegistry ();

		IDGenerator g = Server.instance ().getApplicationIdGenerator ();

		long userUniqueId = g.getUniqueId ();

		//TODO: Generate a temp password!
		String password = "nix";

		User user = new User (userName, email, userUniqueId, password, clientTransceiver.getSender ());

		clientTransceiver.addReceiver (clientTransceiver.getSender ());

		if (userRegistry.getUser (userName) != null)
		{
			return (FrameworkAction) new RegisterNewUserFailureAction (RegisterNewUserFailureAction.userNAME_INUSE);
		}

		if (userRegistry.getUserByEMail (email) != null)
		{
			return (FrameworkAction) new RegisterNewUserFailureAction (RegisterNewUserFailureAction.EMAIL_INUSE);
		}

		userRegistry.addUser (user);

		Engine.instance ().getBaseRegistry ().add (user);

		Engine.instance ().getEventRegistry ().fire ("newuser", new NewUserEvent (user));

		return (FrameworkAction) new RegisterNewUserAction (userName, email, userUniqueId, password);
	}
}
