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
import de.iritgo.aktario.core.action.ActionProcessor;
import de.iritgo.aktario.core.action.ActionProcessorRegistry;
import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.base.Transceiver;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.network.Channel;
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.core.network.NetworkService;
import de.iritgo.aktario.framework.base.shutdown.ShutdownObserver;
import de.iritgo.aktario.framework.user.User;
import java.util.Iterator;


/**
 *
 */
public class BlockedNetworkActionProcessor extends BaseObject implements ActionProcessor, ShutdownObserver
{
	private ActionProcessorRegistry actionProcessorRegistry;

	private NetworkService networkBase;

	private long blockedId;

	private boolean blocked;

	/**
	 * Constructs and initializes a NetworkActionProcessor.
	 */
	public BlockedNetworkActionProcessor(ActionProcessorRegistry actionProcessorRegistry, NetworkService networkBase)
	{
		this.actionProcessorRegistry = actionProcessorRegistry;
		this.networkBase = networkBase;
	}

	public String getId()
	{
		return "syncproc";
	}

	/**
	 * Perform an action.
	 */
	public void perform(Action action)
	{
		blocked = true;
		blockedId = action.getUniqueId();

		try
		{
			AbstractAction networkAction = (AbstractAction) action;
			Transceiver transceiver = action.getTransceiver();

			ClientTransceiver clientTransceiver = (ClientTransceiver) transceiver;

			networkAction.setNumObjects(((Channel) networkBase.getConnectedChannel(clientTransceiver.getSender()))
							.getNumAllObjects());

			Iterator i = clientTransceiver.getReceiverIterator();

			while (i.hasNext())
			{
				double channel = ((Double) i.next()).doubleValue();

				networkBase.send(networkAction, channel);
				networkBase.flush(channel);
			}
		}
		catch (ClassCastException x)
		{
			Log.log("network", "NetworkActionProcessor.perform", "ClassCastException: " + x.getMessage(), Log.WARN);
		}

		try
		{
			while (blocked)
			{
				Thread.sleep(1);
			}
		}
		catch (InterruptedException x)
		{
		}
	}

	/**
	 * Perform an action with a transceiver.
	 *
	 * @param action The action to perform.
	 * @param transceiver The transceiver for this action.
	 */
	public void perform(Action action, Transceiver transceiver)
	{
		perform(action);
	}

	public long getBlockedId()
	{
		return blockedId;
	}

	public void onShutdown()
	{
		blocked = false;
		blockedId = 0;
	}

	public void onUserLogoff(User user)
	{
	}

	public void resume()
	{
		blocked = false;
		blockedId = 0;
	}

	public void close()
	{
	}

	/**
	 * Clone a new instance from this processor with all outputs
	 *
	 * @return NetworkActionProcessor
	 */
	public Object clone()
	{
		return null;
	}
}
