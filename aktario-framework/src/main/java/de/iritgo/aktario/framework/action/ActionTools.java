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

package de.iritgo.aktario.framework.action;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.action.Action;
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.framework.appcontext.AppContext;
import de.iritgo.aktario.framework.server.Server;
import de.iritgo.aktario.framework.user.User;
import java.util.Iterator;


/**
 * Utility methods for easier action handling.
 */
public class ActionTools
{
	/**
	 * Perform an action via the processor that sends actions from the client
	 * to the server.
	 *
	 * @param action The action to execute.
	 */
	public static void sendToServer (Action action)
	{
		if (action.getTransceiver () == null)
		{
			ClientTransceiver ct = new ClientTransceiver (AppContext.instance ().getChannelNumber ());

			ct.addReceiver (AppContext.instance ().getChannelNumber ());
			action.setTransceiver (ct);
		}

		Engine.instance ().getActionProcessorRegistry ().get ("Client.SendEntryNetworkActionProcessor")
						.perform (action);
	}

	/**
	 * Perform an action via the processor that sends actions from the server
	 * to the client.
	 *
	 * @param action The action to execute.
	 */
	public static void sendToClient (Action action)
	{
		Engine.instance ().getActionProcessorRegistry ().get ("Server.SendEntryNetworkActionProcessor")
						.perform (action);
	}

	/**
	 * Perform an action via the processor that sends actions from the server
	 * to the client.
	 *
	 * @param action The action to execute.
	 * @param user The user to send.
	 */
	public static void sendToClient (User user, Action action)
	{
		ClientTransceiver clientTransceiver = new ClientTransceiver (user.getNetworkChannel ());

		clientTransceiver.addReceiver (user.getNetworkChannel ());
		action.setTransceiver (clientTransceiver);

		Engine.instance ().getActionProcessorRegistry ().get ("Server.SendEntryNetworkActionProcessor")
						.perform (action);
	}

	/**
	 * Perform an action via the processor that sends actions from the server
	 * to the client.
	 *
	 * @param action The action to execute.
	 * @param user The user to send.
	 */
	public static void sendToClient (long userUniqueId, Action action)
	{
		User user = Server.instance ().getUserRegistry ().getUser (userUniqueId);

		ClientTransceiver clientTransceiver = new ClientTransceiver (user.getNetworkChannel ());

		clientTransceiver.addReceiver (user.getNetworkChannel ());
		action.setTransceiver (clientTransceiver);

		Engine.instance ().getActionProcessorRegistry ().get ("Server.SendEntryNetworkActionProcessor")
						.perform (action);
	}

	/**
	 * Perform an action via the processor that sends actions from the server
	 * to the client.
	 *
	 * @param user The user name to send.
	 * @param action The action to execute.
	 */
	public static void sendToClient (String userName, Action action)
	{
		User user = Server.instance ().getUserRegistry ().getUser (userName);

		if (user == null)
		{
			return;
		}

		if (! user.isOnline ())
		{
			return;
		}

		ClientTransceiver clientTransceiver = new ClientTransceiver (user.getNetworkChannel ());

		clientTransceiver.addReceiver (user.getNetworkChannel ());
		action.setTransceiver (clientTransceiver);

		Engine.instance ().getActionProcessorRegistry ().get ("Server.SendEntryNetworkActionProcessor")
						.perform (action);
	}

	/**
	 * Send the action to all online users
	 *
	 * @param action The action to execute.
	 */
	public static void sendServerBroadcast (Action action)
	{
		ClientTransceiver clientTransceiver = new ClientTransceiver (- 1);

		for (Iterator i = Server.instance ().getUserRegistry ().onlineUserIterator (); i.hasNext ();)
		{
			User userToSend = (User) i.next ();

			clientTransceiver.addReceiver (userToSend.getNetworkChannel ());
		}

		action.setTransceiver (clientTransceiver);
		Engine.instance ().getActionProcessorRegistry ().get ("Server.SendEntryNetworkActionProcessor")
						.perform (action);
	}
}
