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

package de.iritgo.aktario.framework.server.cadencemanager;


import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.base.action.FrameworkAction;
import de.iritgo.aktario.framework.base.action.TurnAction;
import de.iritgo.aktario.framework.user.User;


/**
 *
 */
public class CadenceContext extends BaseObject
{
	private User user;

	private boolean isTurnFired;

	/**
	 * Standard constructor
	 *
	 * @param user The user for this turncontext.
	 */
	public CadenceContext (User user)
	{
		super ("turncontext");
		this.user = user;
		isTurnFired = false;
	}

	/**
	 * Standard constructor
	 *
	 * @param user The user for this turncontext.
	 */
	public CadenceContext (User user, boolean isTurnFired)
	{
		this (user);
		this.isTurnFired = isTurnFired;
	}

	/**
	 * Get the Ping time from this user.
	 */
	public long getPingTime ()
	{
		long pingTime = user.getPingTime ();

		if ((pingTime <= 10) || (pingTime >= 200))
		{
			return CadenceManager.TURN_INTERVAL - Math.round (user.getLastRealPingTime () / 2);
		}

		return CadenceManager.TURN_INTERVAL - Math.round (pingTime / 2);
	}

	public void turn ()
	{
		ClientTransceiver clientTransceiver = new ClientTransceiver (user.getNetworkChannel ());

		clientTransceiver.addReceiver (user.getNetworkChannel ());

		FrameworkAction action = new TurnAction (1);

		action.setTransceiver (clientTransceiver);

		isTurnFired = true;

		ActionTools.sendToClient (action);
	}

	public boolean isTurnFired ()
	{
		return isTurnFired;
	}

	public User getUser ()
	{
		return user;
	}

	public void reset ()
	{
		isTurnFired = false;
	}
}
