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


/**
 * NetworkActionProcessors are used to perform actions. Actions are not executed directly,
 * because we want some basic conditions to hold, for example we want to
 * synchronize the actions among the clients. Network action processors have outputs to deliver the
 * action to the next processor.
 *
 */
public interface NetworkActionProcessorInterface
{
	/**
	 * Init a network action processor
	 */
	public abstract void init ();

	/**
	 * If a new channel is created this method will called.
	 *
	 * @param channel The new channel.
	 */
	public abstract void newChannelCreated (Channel channel);

	/**
	 * If a new channel is closed this method will called.
	 *
	 * @param channel The new channel.
	 */
	public abstract void channelClosed (Channel channel);

	/**
	 * Perform an action.
	 *
	 * @param action The action to perform.
	 */
	public abstract void perform (Action action);

	/**
	 * Perform an action with a transceiver.
	 *
	 * @param action The action to perform.
	 * @param transceiver The transceiver for this action.
	 */
	public abstract void perform (Action action, Transceiver transceiver);

	/**
	 * Add a output action processor to this network action processor and the specified channel.
	 *
	 * @param channel The channel.
	 * @param actionProcessor The action processor.
	 */
	public abstract void addOutput (Channel channel, ActionProcessor actionProcessor);

	/**
	 * Remove a action processor from the output and the specified channel
	 *
	 * @param channel The channel.
	 */
	public abstract void removeOutput (Channel channel);

	/**
	 * Add a output action processor to this network action processor. This processor will calls for all channels
	 *
	 * @param actionProcessor The action processor
	 */
	public abstract void addOutput (ActionProcessor actionProcessor);

	/**
	 * Remove a action processor from the output.
	 *
	 * @param actionProcessor The action processor.
	 */
	public abstract void removeOutput (ActionProcessor actionProcessor);

	/**
	 * Close a network action processor
	 */
	public abstract void close ();

	/**
	 * Clone all outputs
	 *
	 * @param clone The NetworkActionProcessorInterface to clone.
	 */
	public void cloneOutputs (NetworkActionProcessorInterface clone);
}
