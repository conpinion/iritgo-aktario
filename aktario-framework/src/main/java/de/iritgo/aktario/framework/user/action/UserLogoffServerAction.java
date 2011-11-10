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

package de.iritgo.aktario.framework.user.action;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.action.FilterActionProcessor;
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.base.action.FrameworkServerAction;


/**
 *
 */
public class UserLogoffServerAction extends FrameworkServerAction
{
	/**
	 * Standard constructor
	 */
	public UserLogoffServerAction()
	{
	}

	/**
	 * Perform the action.
	 * Disable the action processor for the user
	 */
	public void perform()
	{
		ClientTransceiver clientTransceiver = (ClientTransceiver) transceiver;
		Double channel = new Double(clientTransceiver.getSender());

		FilterActionProcessor filterActionProcessor = (FilterActionProcessor) Engine.instance()
						.getActionProcessorRegistry().get("Server.FilterActionProcessor");

		UserLogoffAction userLogoffAction = new UserLogoffAction();

		clientTransceiver.addReceiver(clientTransceiver.getSender());
		userLogoffAction.setTransceiver(clientTransceiver);
		ActionTools.sendToClient(userLogoffAction);

		filterActionProcessor.addChannelToFilter(channel);
	}
}
