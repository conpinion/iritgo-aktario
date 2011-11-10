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

package de.iritgo.aktario.agent.transfer;


import de.iritgo.aktario.agent.Agent;
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.appcontext.AppContext;
import de.iritgo.aktario.framework.base.DataObject;
import de.iritgo.aktario.framework.user.User;


/**
 * A dispatcher recieved and send a entitys to other systems.
 */
public class Dispatcher
{
	/** The current user from this dispatcher */
	private User user;

	/** The current channel from this dispatcher */
	private double channel;

	/** The uniqueId from this far caster portal */
	private String uniqueId;

	/** Is the dispatcher online and ready to work? */
	private boolean isOnline;

	/**
	 * The standard constructor
	 *
	 * @param user The user.
	 */
	public Dispatcher(User user)
	{
		this.user = user;
		this.channel = user.getNetworkChannel();
	}

	/**
	 * Set the unique id for this far caster
	 *
	 * @param uniqueId The unique id from this far caster.
	 */
	public void setUniqueId(String uniqueId)
	{
		this.uniqueId = uniqueId;
	}

	/**
	 * Get the unique id from this far caster
	 *
	 * @return String The unique id from this far caster.
	 */
	public String getUniqueId()
	{
		return uniqueId;
	}

	/**
	 * Get the channel from this far caster
	 *
	 * @return double The channel from this far caster.
	 */
	public double getChannel()
	{
		return channel;
	}

	/**
	 * Check if this portal connected with the given user.
	 *
	 * @param user The user iritgo object.
	 */
	public boolean isUserDispatcher(User user)
	{
		return this.user == user;
	}

	/**
	 * Check if this dispatcher the right one
	 *
	 * @param uniqueId The unique id from the dispatcher.
	 */
	public boolean isUniqueId(String uniqueId)
	{
		return this.uniqueId.equals(uniqueId);
	}

	/**
	 * Is this farcaster portal ready and online
	 *
	 * @return online state.
	 */
	public boolean isOnline()
	{
		return isOnline;
	}

	/**
	 * Send a Agent over the dispatcher.
	 *
	 * @param agent The agent to transfer.
	 */
	public void sendAgent(Agent agent)
	{
		double channelNumber = getChannel();
		ClientTransceiver clientTransceiver = new ClientTransceiver(channelNumber);

		clientTransceiver.addReceiver(channelNumber);

		AgentTransferAction agentTransferAction = new AgentTransferAction((DataObject) agent);

		agentTransferAction.setTransceiver(clientTransceiver);

		if (AppContext.instance().getServer())
		{
			ActionTools.sendToClient(agentTransferAction);
		}
		else
		{
			ActionTools.sendToServer(agentTransferAction);
		}
	}
}
