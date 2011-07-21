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

package de.iritgo.aktario.framework.server.network.pingmanager;


import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.base.action.FrameworkAction;
import de.iritgo.aktario.framework.base.action.PingAction;
import de.iritgo.aktario.framework.user.User;


/**
 *
 */
public class PingContext extends BaseObject
{
	private User user;

	private boolean serverPingActive;

	/* Have we send a ping and have no answer? */
	private int failurePings;

	private long pingStartTime;

	/**
	 * Standard constructor
	 *
	 * @param user The user for this pingcontext.
	 */
	public PingContext (User user)
	{
		super ("" + user.getUniqueId ());
		this.user = user;

		failurePings = 0;
	}

	public void ping ()
	{
		if (serverPingActive)
		{
			++failurePings;
		}

		ClientTransceiver clientTransceiver = new ClientTransceiver (user.getNetworkChannel ());

		clientTransceiver.addReceiver (user.getNetworkChannel ());

		pingStartTime = System.currentTimeMillis ();

		FrameworkAction action = new PingAction (pingStartTime);

		action.setTransceiver (clientTransceiver);

		ActionTools.sendToServer (action);
	}

	public int getFailurePings ()
	{
		return failurePings;
	}

	public void receivedPing (long pingReceivedTime)
	{
		long c = System.currentTimeMillis ();

		user.addPingTime (c - pingStartTime);

		failurePings = 0;
	}
}
