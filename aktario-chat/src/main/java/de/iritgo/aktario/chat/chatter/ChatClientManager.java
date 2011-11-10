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

package de.iritgo.aktario.chat.chatter;


import de.iritgo.aktario.chat.command.UserLeaveCommand;
import de.iritgo.aktario.chat.gui.ChatGUI;
import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.flowcontrol.CountingFlowRule;
import de.iritgo.aktario.core.flowcontrol.FlowControl;
import de.iritgo.aktario.core.gui.IDesktopManager;
import de.iritgo.aktario.core.gui.IDisplay;
import de.iritgo.aktario.framework.IritgoEngine;
import de.iritgo.aktario.framework.appcontext.AppContext;
import de.iritgo.aktario.framework.client.Client;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.UserRegistry;
import java.util.Iterator;


public class ChatClientManager extends ChatManager
{
	@SuppressWarnings("unused")
	private String displayId;

	public ChatClientManager()
	{
		super("chat.client", Client.instance().getUserRegistry());
	}

	public ChatClientManager(String displayId)
	{
		this();
		this.displayId = displayId;
	}

	public void joinChannel(String channelName, long chatter, String chatterName)
	{
		if (getUserName(chatter) == null)
		{
			UserRegistry userRegistry = Client.instance().getUserRegistry();

			userRegistry.addUser(new User(chatterName, "", chatter, "", 0));
		}

		try
		{
			super.joinChannel(channelName, chatter);
		}
		catch (UserAllreadyJoindException x)
		{
		}

		ChatGUI chatGUI = getChatGUI();

		if (chatGUI != null)
		{
			chatGUI.joinChannel(channelName, chatterName);
		}
	}

	/**
	 * Overwritten from baseclass (chatmanager), becourse we must remove channel if its the user form the client
	 *
	 * @param channelId
	 * @param chatter The useruniqueid
	 */
	@Override
	public void leaveChannel(Integer channelId, long chatter)
	{
		ChatChannel chatChannel = (ChatChannel) chatChannels.get(channelId);

		if (chatChannel != null)
		{
			chatChannel.removeChatter(new Long(chatter));

			UserRegistry userRegistry = Client.instance().getUserRegistry();
			User user = userRegistry.getUser(chatter);

			if (user == null)
			{
				return;
			}

			if (user.getUniqueId() == chatter)
			{
				chatChannels.remove(channelId);
			}
		}
	}

	public void leaveChannel(Integer channelId, long chatter, String chatterName)
	{
		Engine.instance().getFlowControl().ruleSuccess("shutdown.in.progress." + getTypeId());

		ChatGUI chatGUI = getChatGUI();

		if (chatGUI != null)
		{
			chatGUI.leaveChannel(channelId, chatterName);
		}

		leaveChannel(channelId, chatter);
	}

	public void messageChannel(String message, int channelId, long chatter, String chatterName)
	{
		super.messageChannel(message, channelId, chatter);

		ChatGUI chatGUI = getChatGUI();

		if (chatGUI != null)
		{
			chatGUI.addMessage(message, channelId, chatterName);
		}
	}

	private ChatGUI getChatGUI()
	{
		IDesktopManager displayManager = Client.instance().getClientGUI().getDesktopManager();
		IDisplay display = displayManager.getDisplay("common.chatview");

		if (display != null)
		{
			return (ChatGUI) display.getGUIPane();
		}

		return null;
	}

	private String getUserName(long userId)
	{
		UserRegistry userRegistry = Client.instance().getUserRegistry();
		User user = userRegistry.getUser(userId);

		if (user != null)
		{
			return user.getName();
		}

		return null;
	}

	public Integer getCurrentChannel()
	{
		ChatGUI chatGUI = getChatGUI();

		if (chatGUI != null)
		{
			return chatGUI.getCurrentChannel();
		}

		return new Integer(- 1);
	}

	/**
	 * This client close all open channels
	 */
	public void doShutdownNotify()
	{
		closeAllChannels();
	}

	/**
	 * This client close all open channels
	 */
	public void closeAllChannels()
	{
		if (! AppContext.instance().isUserLoggedIn())
		{
			return;
		}

		Object lockObject = AppContext.instance().getLockObject();

		synchronized (lockObject)
		{
			int channels = 0;

			for (Iterator i = getChatChannelIterator(); i.hasNext();)
			{
				ChatChannel channel = null;

				try
				{
					channel = (ChatChannel) i.next();
				}
				catch (Exception x)
				{
					x.printStackTrace();

					continue;
				}

				UserLeaveCommand userLeaveCommand = new UserLeaveCommand(channel.getChannelId());

				IritgoEngine.instance().getAsyncCommandProcessor().perform(userLeaveCommand);
				++channels;
			}

			if (channels != 0)
			{
				FlowControl flowControll = Engine.instance().getFlowControl();

				flowControll.add(new CountingFlowRule("shutdown.in.progress." + getTypeId(), channels));
			}
		}
	}
}
