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


import de.iritgo.aktario.core.action.ActionProcessor;
import de.iritgo.aktario.core.action.NetworkActionProcessor;
import de.iritgo.aktario.core.action.NetworkActionProcessorInterface;
import de.iritgo.aktario.core.action.ThreadNetworkActionProcessor;
import de.iritgo.aktario.core.network.Channel;


/**
 *
 */
public class ConcurrencyNetworkActionProcessor extends NetworkActionProcessor
{
	private ThreadNetworkActionProcessor threadNetworkActionProcessor;

	/**
	 * Default Constructor
	 *
	 * @param channel The channel for this processor
	 * @param parentNetworkActionProcessor The network action processor
	 */
	public ConcurrencyNetworkActionProcessor (Channel channel,
					NetworkActionProcessorInterface parentNetworkActionProcessor)
	{
		super ("Server.ConcurrencyNetworkActionProcessor", channel, parentNetworkActionProcessor);
	}

	/**
	 * Default Constructor
	 *
	 * @param channel The channel for this processor
	 * @param parentNetworkActionProcessor The network action processor
	 * @param threadNetworkActionProcessor The thread network action processor
	 */
	public ConcurrencyNetworkActionProcessor (Channel channel,
					NetworkActionProcessorInterface parentNetworkActionProcessor,
					ThreadNetworkActionProcessor threadNetworkActionProcessor)
	{
		super ("Server.ConcurrencyNetworkActionProcessor", channel, parentNetworkActionProcessor);
		this.threadNetworkActionProcessor = threadNetworkActionProcessor;
	}

	/**
	 * Helper method for inital create.
	 *
	 * @param threadNetworkActionProcessor The action processor.
	 */
	public void setThreadNetworkActionProcessor (ThreadNetworkActionProcessor threadNetworkActionProcessor)
	{
		this.threadNetworkActionProcessor = threadNetworkActionProcessor;
	}

	/**
	 * If a new channel is created this method will called.
	 *
	 * @param channel The new channel.
	 */
	@Override
	public void newChannelCreated (Channel channel)
	{
		addOutput (channel, (ActionProcessor) threadNetworkActionProcessor.clone ());
		super.newChannelCreated (channel);
	}

	/**
	 * If a new channel is closed this method will called.
	 *
	 * @param channel The new channel.
	 */
	@Override
	public void channelClosed (Channel channel)
	{
		((NetworkActionProcessorInterface) channelProcessorMapping.get (channel)).channelClosed (channel);
		((NetworkActionProcessorInterface) channelProcessorMapping.get (channel)).close ();
		removeOutput (channel);
	}

	/**
	 * Clone a new instance from this processor with all outputs
	 *
	 * @return NetworkActionProcessor
	 */
	@Override
	public Object clone ()
	{
		ConcurrencyNetworkActionProcessor clone = new ConcurrencyNetworkActionProcessor (channel,
						parentNetworkActionProcessor, threadNetworkActionProcessor);

		cloneOutputs (clone);

		return clone;
	}
}
