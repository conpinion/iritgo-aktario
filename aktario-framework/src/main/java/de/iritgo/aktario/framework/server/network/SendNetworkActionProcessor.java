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


import de.iritgo.aktario.core.action.AbstractAction;
import de.iritgo.aktario.core.action.Action;
import de.iritgo.aktario.core.action.NetworkActionProcessor;
import de.iritgo.aktario.core.action.NetworkActionProcessorInterface;
import de.iritgo.aktario.core.base.Transceiver;
import de.iritgo.aktario.core.network.BroadcastTransceiver;
import de.iritgo.aktario.core.network.Channel;
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.core.network.NetworkService;
import java.util.Iterator;


/**
 *
 */
public class SendNetworkActionProcessor extends NetworkActionProcessor
{
	private NetworkService networkService;

	/**
	 * Default constructer
	 */
	public SendNetworkActionProcessor (NetworkService networkService, Channel channel,
					NetworkActionProcessorInterface parentNetworkActionProcessor)
	{
		super ("Server.SendNetworkActionProcessor", channel, parentNetworkActionProcessor);
		this.networkService = networkService;
	}

	/**
	 * Get the id of this network action processor
	 *
	 * @return The id.
	 */
	public String getId ()
	{
		return "Server.SendNetworkActionProcessor";
	}

	/**
	 * Perform an action.
	 */
	public void perform (Action action)
	{
		perform (action, action.getTransceiver ());
	}

	/**
	 * Perform an action with a transceiver.
	 *
	 * @param action The action to perform.
	 * @param transceiver The transceiver for this action.
	 */
	public void perform (Action action, Transceiver transceiver)
	{
		AbstractAction networkAction = (AbstractAction) action;

		try
		{
			ClientTransceiver clientTransceiver = (ClientTransceiver) transceiver;

			Iterator i = clientTransceiver.getReceiverIterator ();

			while (i.hasNext ())
			{
				// TODO:Optimize the clienttranscleiver
				double channelNumber = ((Double) i.next ()).doubleValue ();

				Channel channel = (Channel) networkService.getConnectedChannel (channelNumber);

				if (channel == null)
				{
					// User is disconnected...we do nothing...
					continue;
				}

				networkAction.setNumObjects (channel.getNumAllObjects ());

				networkService.send (networkAction, channel);
				networkService.flush (channelNumber);
			}

			return;
		}
		catch (ClassCastException x)
		{
			BroadcastTransceiver clientTransceiver = (BroadcastTransceiver) transceiver;

			networkService.sendBroadcast (networkAction);
		}
	}

	/**
	 * Clone a new instance from this processor with all outputs
	 *
	 * @return NetworkActionProcessor
	 */
	public Object clone ()
	{
		SendNetworkActionProcessor clone = new SendNetworkActionProcessor (networkService, channel,
						parentNetworkActionProcessor);

		cloneOutputs (clone);

		return clone;
	}
}
