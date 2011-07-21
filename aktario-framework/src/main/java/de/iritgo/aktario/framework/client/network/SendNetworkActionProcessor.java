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


import de.iritgo.aktario.core.action.AbstractAction;
import de.iritgo.aktario.core.action.Action;
import de.iritgo.aktario.core.action.NetworkActionProcessor;
import de.iritgo.aktario.core.action.NetworkActionProcessorInterface;
import de.iritgo.aktario.core.base.Transceiver;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.network.Channel;
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.core.network.NetworkService;
import java.util.Iterator;


/**
 *
 */
public class SendNetworkActionProcessor extends NetworkActionProcessor
{
	private static int collectTime = 100; //The time before all networkbuffers are flushed (speed mass packets up) in ms

	private NetworkService networkService;

	/**
	 * Constructs and initializes a NetworkActionProcessor.
	 */
	public SendNetworkActionProcessor (NetworkService networkService, Channel channel,
					NetworkActionProcessorInterface parentNetworkActionProcessor)
	{
		super ("Client.ReceiveNetworkActionProcessor", channel, parentNetworkActionProcessor);
		this.networkService = networkService;
	}

	/**
	 * Get the id of this network action processor
	 *
	 * @return The id.
	 */
	public String getId ()
	{
		return "Client.SendNetworkActionProcessor";
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
		try
		{
			AbstractAction networkAction = (AbstractAction) action;

			ClientTransceiver clientTransceiver = (ClientTransceiver) transceiver;

			Channel channel = (Channel) networkService.getConnectedChannel (clientTransceiver.getSender ());

			if (channel == null)
			{
				return;
			}

			networkAction.setNumObjects (channel.getNumAllObjects ());

			Iterator i = clientTransceiver.getReceiverIterator ();

			while (i.hasNext ())
			{
				double channelNumber = ((Double) i.next ()).doubleValue ();

				networkService.send (networkAction, channelNumber);
				networkService.flush (channelNumber);
			}
		}
		catch (ClassCastException x)
		{
			Log.log ("network", "NetworkActionProcessor.perform", "ClassCastException: " + x.getMessage (), Log.WARN);
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
