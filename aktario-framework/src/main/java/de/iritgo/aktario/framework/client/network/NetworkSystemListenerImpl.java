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

package de.iritgo.aktario.framework.client.network;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.network.Channel;
import de.iritgo.aktario.core.network.NetworkService;
import de.iritgo.aktario.core.network.NetworkSystemAdapter;
import de.iritgo.aktario.framework.appcontext.AppContext;
import de.iritgo.aktario.framework.base.action.AliveCheckServerAction;
import de.iritgo.aktario.framework.client.Client;
import de.iritgo.aktario.framework.user.UserEvent;
import java.net.SocketTimeoutException;


/**
 *
 */
public class NetworkSystemListenerImpl extends NetworkSystemAdapter
{
	public void error(NetworkService networkBase, Channel connectedChannel, SocketTimeoutException x)
	{
		if (connectedChannel.isAliveCheckSent())
		{
			connectionTerminated(networkBase, connectedChannel);

			return;
		}

		connectedChannel.setAliveCheckSent(true);
		connectedChannel.send(new AliveCheckServerAction(AliveCheckServerAction.CLIENT));
		connectedChannel.flush();
	}

	public void connectionTerminated(NetworkService networkBase, Channel connectedChannel)
	{
		if (connectedChannel.getConnectionState() == (Channel.NETWORK_ERROR_CLOSING))
		{
			Log.logError("network", "NetworkSystemListenerImpl.work", "Unable to close connection: "
							+ connectedChannel.getConnectionState());
		}

		double channelNumber = connectedChannel.getChannelNumber();

		if (connectedChannel.getConnectionState() != Channel.NETWORK_ERROR_CLOSING)
		{
			networkBase.closeChannel(connectedChannel.getChannelNumber());
		}

		Engine.instance().getEventRegistry().fire("User",
						new UserEvent(AppContext.instance().getUser(), UserEvent.USER_LOGGED_OUT));
		Client.instance().lostNetworkConnection();

		Log.log("network", "NetworkSystemListenerImpl.work", "Lost connection to server: "
						+ connectedChannel.getConnectionState(), Log.INFO);

		return;
	}
}
