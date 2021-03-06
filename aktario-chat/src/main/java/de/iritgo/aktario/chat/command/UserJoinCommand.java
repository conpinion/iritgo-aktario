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


import de.iritgo.aktario.chat.action.UserJoinServerAction;
import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.appcontext.AppContext;
import java.util.Properties;


public class UserJoinCommand extends Command
{
	private String userName;

	private String channel;

	private AppContext appContext;

	public UserJoinCommand()
	{
	}

	public UserJoinCommand(String channel, String userName)
	{
		appContext = AppContext.instance();
		this.userName = userName;
		this.channel = channel;
	}

	/**
	 * Constructor for ConsoleCommand
	 *
	 * @param channel
	 */
	public UserJoinCommand(String channel)
	{
		appContext = AppContext.instance();
		this.userName = appContext.getUser().getName();
		this.channel = channel;
	}

	/**
	 * Set the command Properties
	 *
	 * @param properties The properties.
	 */
	@Override
	public void setProperties(Properties properties)
	{
	}

	@Override
	public void perform()
	{
		double channelNumber = appContext.getChannelNumber();
		ClientTransceiver clientTransceiver = new ClientTransceiver(channelNumber);

		clientTransceiver.addReceiver(channelNumber);

		UserJoinServerAction userJoinServerAction = new UserJoinServerAction(userName, channel);

		userJoinServerAction.setTransceiver(clientTransceiver);

		ActionTools.sendToServer(userJoinServerAction);
	}

	@Override
	public boolean canPerform()
	{
		return appContext.isConnectedWithServer();
	}
}
