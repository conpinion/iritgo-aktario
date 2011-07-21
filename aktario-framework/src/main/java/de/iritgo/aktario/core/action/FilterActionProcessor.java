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


import de.iritgo.aktario.core.base.Transceiver;
import de.iritgo.aktario.core.network.Channel;
import de.iritgo.aktario.core.network.ClientTransceiver;
import java.util.LinkedList;
import java.util.List;


/**
 * FilterActionProcessor
 *
 * With this filter you can disable the processing on a channel.
 */
public class FilterActionProcessor extends NetworkActionProcessor
{
	private List filterChannels;

	public FilterActionProcessor (String typeId, Channel channel,
					NetworkActionProcessorInterface parentNetworkActionProcessor)
	{
		super (typeId, channel, parentNetworkActionProcessor);
		filterChannels = new LinkedList ();
	}

	/**
	 * Perform an action with a transceiver.
	 *
	 * @param action The action to perform.
	 * @param transceiver The transceiver for this action.
	 */
	@Override
	public void perform (Action action, Transceiver transceiver)
	{
		Double channel = new Double (((ClientTransceiver) transceiver).getSender ());

		if ((channel.doubleValue () >= 0) && (filterChannels.contains (channel)))
		{
			return;
		}

		super.perform (action, transceiver);
	}

	/**
	 * Add a channel. All actions on this channel will ignored
	 *
	 * @param channel The channel.
	 */
	public void addChannelToFilter (Double channel)
	{
		filterChannels.add (channel);
	}

	/**
	 * If a new channel is closed this method will called.
	 *
	 * @param channel The new channel.
	 */
	@Override
	public void channelClosed (Channel channel)
	{
		filterChannels.remove (channel);
		super.channelClosed (channel);
	}

	/**
	 * Clone a new instance from this processor with all outputs
	 *
	 * @return NetworkActionProcessor
	 */
	@Override
	public Object clone ()
	{
		FilterActionProcessor clone = new FilterActionProcessor (typeId, channel, parentNetworkActionProcessor);

		cloneOutputs (clone);

		return clone;
	}
}
