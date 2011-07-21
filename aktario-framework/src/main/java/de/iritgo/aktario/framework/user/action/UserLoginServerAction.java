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


import java.io.IOException;
import java.util.Properties;
import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.framework.IritgoEngine;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.base.action.FrameworkAction;
import de.iritgo.aktario.framework.base.action.FrameworkInputStream;
import de.iritgo.aktario.framework.base.action.FrameworkOutputStream;
import de.iritgo.aktario.framework.base.action.NetworkFrameworkServerAction;
import de.iritgo.aktario.framework.base.action.WrongVersionAction;
import de.iritgo.aktario.framework.command.CommandTools;
import de.iritgo.aktario.framework.server.Server;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.UserEvent;
import de.iritgo.aktario.framework.user.UserRegistry;


/**
 *
 */
public class UserLoginServerAction extends NetworkFrameworkServerAction
{
	private String userName;

	private String password;

	private String clientVersion;

	/**
	 * Standard constructor
	 */
	public UserLoginServerAction ()
	{
	}

	/**
	 * Standard constructor
	 */
	public UserLoginServerAction (String userName, String password, String clientVersion)
	{
		super (- 1);
		this.userName = userName;
		this.password = password;
		this.clientVersion = clientVersion;
	}

	/**
	 * Get the id of the iritgo object.
	 */
	@Override
	public String getTypeId ()
	{
		return "server.action.userlogin";
	}

	/**
	 * Read the attributes from the given stream.
	 */
	@Override
	public void readObject (FrameworkInputStream stream) throws IOException, ClassNotFoundException
	{
		userName = stream.readUTF ();
		password = stream.readUTF ();
		clientVersion = stream.readUTF ();
	}

	/**
	 * Write the attributes to the given stream.
	 */
	@Override
	public void writeObject (FrameworkOutputStream stream) throws IOException
	{
		stream.writeUTF (userName);
		stream.writeUTF (password);
		stream.writeUTF (clientVersion);
	}

	/**
	 * @see de.iritgo.aktario.framework.base.action.NetworkFrameworkServerAction#getAction(de.iritgo.aktario.core.network.ClientTransceiver)
	 */
	@Override
	public FrameworkAction getAction (ClientTransceiver clientTransceiver)
	{
		clientTransceiver.addReceiver (clientTransceiver.getSender ());
		if (! IritgoEngine.instance ().getCommandLine ().hasOption ("n") && ! "0".equals (clientVersion))
		{
			String serverVersion = System.getProperty ("iritgo.app.version.long");
			if (! serverVersion.equals (clientVersion))
			{
				return new WrongVersionAction ();
			}
		}
		return checkUserLogin (clientTransceiver);
	}

	/**
	 * @param clientTransceiver
	 * @return
	 */
	private FrameworkAction checkUserLogin (ClientTransceiver clientTransceiver)
	{
		UserRegistry userRegistry = Server.instance ().getUserRegistry ();
		User user = userRegistry.getUser (userName);
		if (user == null)
		{
			return new UserLoginFailureAction (UserLoginFailureAction.BAD_USERNAME_OR_PASSWORD);
		}

		if (CommandTools.commandExists ("LoginAllowed"))
		{
			Properties props = new Properties ();
			props.put ("userName", userName);
			Boolean allowed = (Boolean) CommandTools.performSimple ("LoginAllowed", props);
			if (! allowed)
			{
				return new UserLoginFailureAction (UserLoginFailureAction.LOGIN_NOT_ALLOWED);
			}
		}

		if (CommandTools.commandExists ("Authenticate"))
		{
			Properties props = new Properties ();
			props.put ("userName", userName);
			props.put ("password", password);
			Boolean authenticated = (Boolean) CommandTools.performSimple ("Authenticate", props);
			if (! authenticated)
			{
				return new UserLoginFailureAction (UserLoginFailureAction.BAD_USERNAME_OR_PASSWORD);
			}
		}
		else if (! password.equals (user.getPassword ()))
		{
			return new UserLoginFailureAction (UserLoginFailureAction.BAD_USERNAME_OR_PASSWORD);
		}

		if (user.isOnline ())
		{
			double channel = user.getNetworkChannel ();
			ClientTransceiver oldClientTransceiver = new ClientTransceiver (channel);
			UserKickAction userKickAction = new UserKickAction ();
			oldClientTransceiver.addReceiver (oldClientTransceiver.getSender ());
			userKickAction.setTransceiver (oldClientTransceiver);
			ActionTools.sendToClient (userKickAction);
			user.setOnline (false);
			return new UserLoginFailureAction (UserLoginFailureAction.USER_ALREADY_ONLINE);
		}

		user.setNetworkChannel (clientTransceiver.getSender ());
		user.setOnline (true);
		clientTransceiver.getConnectedChannel ().setCustomerContextObject (user);
		UserLoginAction userLoginAction = new UserLoginAction (user);
		userLoginAction.setAppId (System.getProperty ("iritgo.app.id"));
		userLoginAction.setTransceiver (clientTransceiver);
		Engine.instance ().getEventRegistry ().fire ("User", new UserEvent (user, UserEvent.USER_LOGGED_IN, password));
		ActionTools.sendToClient (userLoginAction);
		return null;
	}
}
