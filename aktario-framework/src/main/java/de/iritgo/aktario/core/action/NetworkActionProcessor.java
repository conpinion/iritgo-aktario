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

package de.iritgo.aktario.core.action;


import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.base.Transceiver;
import de.iritgo.aktario.core.network.Channel;
import de.iritgo.aktario.core.network.ClientTransceiver;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;


/**
 * NetworkActionProcessors are used to perform actions. Actions are not executed directly,
 * because we want some basic conditions to hold, for example we want to
 * synchronize the actions among the clients. Network action processors have outputs to deliver the
 * action to the next processor.
 */
public abstract class NetworkActionProcessor extends BaseObject implements ActionProcessor,
				NetworkActionProcessorInterface, Cloneable
{
	protected ConcurrentHashMap channelProcessorMapping;

	protected List channelProcessors;

	protected Channel channel;

	protected NetworkActionProcessorInterface parentNetworkActionProcessor;

	/**
	 * Default constructor
	 */
	public NetworkActionProcessor(String typeId, Channel channel,
					NetworkActionProcessorInterface parentNetworkActionProcessor)
	{
		super(typeId);

		channelProcessorMapping = new ConcurrentHashMap();
		channelProcessors = new LinkedList();
		this.channel = channel;
		this.parentNetworkActionProcessor = parentNetworkActionProcessor;
	}

	/**
	 * Init a network action processor
	 */
	public void init()
	{
	}

	/**
	 * If a new channel is created this method will called.
	 *
	 * @param channel The new channel.
	 */
	public void newChannelCreated(Channel channel)
	{
		for (Iterator i = channelProcessorMapping.values().iterator(); i.hasNext();)
		{
			try
			{
				((NetworkActionProcessorInterface) i.next()).newChannelCreated(channel);
			}
			catch (ClassCastException nothingToDo)
			{
			}
		}

		for (Iterator i = channelProcessors.iterator(); i.hasNext();)
		{
			try
			{
				((NetworkActionProcessorInterface) i.next()).newChannelCreated(channel);
			}
			catch (ClassCastException nothingToDo)
			{
			}
		}
	}

	/**
	 * If a new channel is closed this method will called.
	 *
	 * @param channel The new channel.
	 */
	public void channelClosed(Channel channel)
	{
		for (Iterator i = channelProcessorMapping.keySet().iterator(); i.hasNext();)
		{
			try
			{
				((NetworkActionProcessorInterface) channelProcessorMapping.get(i.next())).channelClosed(channel);
			}
			catch (ClassCastException nothingToDo)
			{
			}
		}

		for (Iterator i = channelProcessors.iterator(); i.hasNext();)
		{
			try
			{
				((NetworkActionProcessorInterface) i.next()).channelClosed(channel);
			}
			catch (ClassCastException nothingToDo)
			{
			}
		}
	}

	/**
	 * Perform an action.
	 *
	 * @param action The action to perform.
	 */
	public void perform(Action action)
	{
		perform(action, action.getTransceiver());
	}

	/**
	 * Perform an action with a transceiver.
	 *
	 * @param action The action to perform.
	 * @param transceiver The transceiver for this action.
	 */
	public void perform(Action action, Transceiver transceiver)
	{
		Channel channel = ((ClientTransceiver) transceiver).getConnectedChannel();

		if (channel != null && channelProcessorMapping.get(channel) != null)
		{
			((ActionProcessor) channelProcessorMapping.get(channel)).perform(action, transceiver);
		}

		for (Iterator i = channelProcessors.iterator(); i.hasNext();)
		{
			try
			{
				((ActionProcessor) i.next()).perform(action, transceiver);
			}
			catch (ClassCastException nothingToDo)
			{
			}
		}
	}

	/**
	 * Add a output action processor to this network action processor and the specified channel.
	 *
	 * @param channel The channel.
	 * @param actionProcessor The action processor.
	 */
	public void addOutput(Channel channel, ActionProcessor actionProcessor)
	{
		channelProcessorMapping.put(channel, actionProcessor);
	}

	/**
	 * Remove a action processor from the output and the specified channel
	 *
	 * @param channel The channel.
	 */
	public void removeOutput(Channel channel)
	{
		channelProcessorMapping.remove(channel);
	}

	/**
	 * Add a output action processor to this network action processor. This processor will calls for all channels
	 *
	 * @param actionProcessor The action processor
	 */
	public void addOutput(ActionProcessor actionProcessor)
	{
		channelProcessors.add(actionProcessor);
	}

	/**
	 * Remove a action processor from the output.
	 *
	 * @param actionProcessor The action processor.
	 */
	public void removeOutput(ActionProcessor actionProcessor)
	{
		channelProcessors.remove(actionProcessor);
	}

	/**
	 * Clone a new instance from this processor with all outputs
	 *
	 * @return NetworkActionProcessor
	 */
	@Override
	public abstract Object clone();

	public void cloneOutputs(NetworkActionProcessorInterface clone)
	{
		for (Iterator i = channelProcessorMapping.keySet().iterator(); i.hasNext();)
		{
			try
			{
				Channel channel = (Channel) i.next();

				clone.addOutput(channel, (ActionProcessor) ((ActionProcessor) channelProcessorMapping.get(channel))
								.clone());
			}
			catch (ClassCastException nothingToDo)
			{
			}
		}

		for (Iterator i = channelProcessors.iterator(); i.hasNext();)
		{
			try
			{
				clone.addOutput((ActionProcessor) ((ActionProcessor) i.next()).clone());
			}
			catch (ClassCastException nothingToDo)
			{
			}
		}
	}

	/**
	 * Close a network action processor
	 */
	public void close()
	{
	}
}
