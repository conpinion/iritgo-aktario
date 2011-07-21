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


import de.iritgo.aktario.core.action.Action;
import de.iritgo.aktario.core.action.NetworkActionProcessor;
import de.iritgo.aktario.core.action.NetworkActionProcessorInterface;
import de.iritgo.aktario.core.base.Transceiver;
import de.iritgo.aktario.core.network.Channel;


/**
 *
 */
public class ReceiveNetworkActionProcessor extends NetworkActionProcessor
{
	public ReceiveNetworkActionProcessor (Channel channel, NetworkActionProcessorInterface parentNetworkActionProcessor)
	{
		super ("ReceiveNetworkActionProcessor", channel, parentNetworkActionProcessor);
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
		action.setTransceiver (transceiver);
		super.perform (action, transceiver);
	}

	/**
	 * Clone a new instance from this processor with all outputs
	 *
	 * @return NetworkActionProcessor
	 */
	@Override
	public Object clone ()
	{
		ReceiveNetworkActionProcessor clone = new ReceiveNetworkActionProcessor (channel, parentNetworkActionProcessor);

		cloneOutputs (clone);

		return clone;
	}
}
