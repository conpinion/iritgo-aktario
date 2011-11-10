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

package de.iritgo.aktario.framework.server.network;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.network.Channel;
import de.iritgo.aktario.core.network.NetworkService;
import de.iritgo.aktario.core.network.NetworkSystemAdapter;
import de.iritgo.aktario.core.thread.Threadable;
import de.iritgo.aktario.framework.base.action.AliveCheckAction;
import de.iritgo.aktario.framework.base.shutdown.ShutdownManager;
import de.iritgo.aktario.framework.server.Server;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.UserEvent;
import de.iritgo.aktario.framework.user.UserRegistry;
import java.net.SocketTimeoutException;
import java.util.Date;


/**
 *
 */
public class NetworkSystemListenerImpl extends NetworkSystemAdapter
{
	private NetworkService networkService;

	public void error(NetworkService networkBase, Channel connectedChannel, SocketTimeoutException x)
	{
		if (connectedChannel.isAliveCheckSent())
		{
			Server.instance().getNetworkService().fireConnectionTerminated(connectedChannel);
			return;
		}

		connectedChannel.setAliveCheckSent(true);
		connectedChannel.send(new AliveCheckAction(AliveCheckAction.SERVER));
		connectedChannel.flush();
	}

	public void connectionTerminated(NetworkService networkBase, Channel connectedChannel)
	{
		connectedChannel.setState(Threadable.CLOSING);

		if (connectedChannel.getConnectionState() == Channel.NETWORK_ERROR_CLOSING)
		{
			Log.logError("network", "NetworkSystemListenerImpl", "Unable to close connection for channel "
							+ connectedChannel.getChannelNumber() + " (connection state: "
							+ connectedChannel.getConnectionState() + ")");
		}

		Server server = Server.instance();
		UserRegistry userRegistry = server.getUserRegistry();

		User user = null;

		user = (User) connectedChannel.getCustomerContextObject();

		if (user == null)
		{
			user = new User();
			user.setNetworkChannel(connectedChannel.getChannelNumber());
			user.setName("");
		}

		Log.log("network", "NetworkSystemListenerImpl", "Lost connection to user '" + user + "' (channel number: "
						+ connectedChannel.getChannelNumber() + ")", Log.INFO);

		user.setOnline(false);
		user.setLoggedOutDate(new Date());
		user.clearNewObjectsMapping();

		Engine.instance().getEventRegistry().fire("User", new UserEvent(user, UserEvent.USER_LOGGED_OUT));

		((ShutdownManager) Engine.instance().getManagerRegistry().getManager("shutdown")).shutdown(user);

		try
		{
			if (connectedChannel.getConnectionState() != Channel.NETWORK_ERROR_CLOSING)
			{
				networkBase.closeChannel(connectedChannel.getChannelNumber());
			}
		}
		catch (Exception x)
		{
		}

		Log.log("network", "NetworkSystemListenerImpl", "Cleaned up connection to user '" + user
						+ "' (channel number: " + connectedChannel.getChannelNumber() + ")", Log.INFO);
	}
}
