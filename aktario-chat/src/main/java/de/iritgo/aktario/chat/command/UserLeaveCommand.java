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

package de.iritgo.aktario.chat.command;


import de.iritgo.aktario.chat.action.UserLeaveServerAction;
import de.iritgo.aktario.chat.chatter.ChatClientManager;
import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.appcontext.AppContext;


public class UserLeaveCommand extends Command
{
	private String userName;

	private int channel;

	private AppContext appContext;

	public UserLeaveCommand ()
	{
		channel = - 1;
	}

	public UserLeaveCommand (int channel)
	{
		this.channel = channel;
	}

	@Override
	public void perform ()
	{
		appContext = AppContext.instance ();
		this.userName = appContext.getUser ().getName ();

		double channelNumber = appContext.getChannelNumber ();
		ClientTransceiver clientTransceiver = new ClientTransceiver (channelNumber);

		clientTransceiver.addReceiver (channelNumber);

		ChatClientManager chatManager = (ChatClientManager) Engine.instance ().getManagerRegistry ().getManager (
						"chat.client");

		if (channel == - 1)
		{
			channel = chatManager.getCurrentChannel ().intValue ();
		}

		UserLeaveServerAction userLeaveServerAction = new UserLeaveServerAction (userName, channel);

		userLeaveServerAction.setTransceiver (clientTransceiver);

		ActionTools.sendToServer (userLeaveServerAction);
	}
}
