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


import de.iritgo.aktario.chat.action.ChatMessageAction;
import de.iritgo.aktario.chat.action.UserJoinAction;
import de.iritgo.aktario.chat.action.UserLeaveAction;
import de.iritgo.aktario.core.action.AbstractAction;
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.framework.server.Server;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.UserRegistry;
import java.util.Iterator;


public class ChatServerManager extends ChatManager
{
	public ChatServerManager()
	{
		super("chat.server", Server.instance().getUserRegistry());
	}

	@Override
	public void joinChannel(String channelName, long chatter)
	{
		try
		{
			super.joinChannel(channelName, chatter);
			notifyMembersUserJoin(getChatChannel(channelName), new Long(chatter));
			notifyUserAboutAllUsers(getChatChannel(channelName), new Long(chatter));
		}
		catch (UserAllreadyJoindException x)
		{
		}
	}

	private void notifyMembersUserJoin(final ChatChannel chatChannel, Long userId)
	{
		ChatterProcessor chatterProcessor = new ChatterProcessor(userId, chatChannel);

		chatterProcessor.doProcessor(new Processor()
		{
			public AbstractAction getAction(User user, User newUser)
			{
				ClientTransceiver clientTransceiver = new ClientTransceiver(user.getNetworkChannel());

				clientTransceiver.addReceiver(user.getNetworkChannel());

				UserJoinAction action = new UserJoinAction(newUser.getName(), newUser.getUniqueId(), chatChannel
								.getName());

				action.setTransceiver(clientTransceiver);

				return action;
			}
		});
	}

	private void notifyUserAboutAllUsers(final ChatChannel chatChannel, Long userId)
	{
		UserRegistry userRegistry = Server.instance().getUserRegistry();
		User newUser = userRegistry.getUser(userId);
		final ClientTransceiver clientTransceiver = new ClientTransceiver(newUser.getNetworkChannel());

		clientTransceiver.addReceiver(newUser.getNetworkChannel());

		ChatterProcessor chatterProcessor = new ChatterProcessor(userId, chatChannel);

		chatterProcessor.doProcessor(new Processor()
		{
			public AbstractAction getAction(User user, User newUser)
			{
				UserJoinAction action = new UserJoinAction(user.getName(), user.getUniqueId(), chatChannel.getName());

				action.setTransceiver(clientTransceiver);

				return action;
			}
		});
	}

	@Override
	public void leaveChannel(Integer channelId, long chatter)
	{
		notifyMembersUserLeave((ChatChannel) chatChannels.get(channelId), new Long(chatter));
		super.leaveChannel(channelId, chatter);
	}

	private void notifyMembersUserLeave(final ChatChannel chatChannel, Long userId)
	{
		ChatterProcessor chatterProcessor = new ChatterProcessor(userId, chatChannel);

		chatterProcessor.doProcessor(new Processor()
		{
			public AbstractAction getAction(User user, User newUser)
			{
				ClientTransceiver clientTransceiver = new ClientTransceiver(user.getNetworkChannel());

				clientTransceiver.addReceiver(user.getNetworkChannel());

				UserLeaveAction action = new UserLeaveAction(newUser.getName(), chatChannel.getName().hashCode(),
								newUser.getUniqueId());

				action.setTransceiver(clientTransceiver);

				return action;
			}
		});
	}

	@Override
	public void messageChannel(String message, int channelName, long chatter)
	{
		notifyMembersMessage(message, getChatChannel(channelName), new Long(chatter));
	}

	private void notifyMembersMessage(final String message, final ChatChannel chatChannel, final Long userId)
	{
		ChatterProcessor chatterProcessor = new ChatterProcessor(userId, chatChannel);

		chatterProcessor.doProcessor(new Processor()
		{
			public AbstractAction getAction(User user, User newUser)
			{
				ClientTransceiver clientTransceiver = new ClientTransceiver(user.getNetworkChannel());

				clientTransceiver.addReceiver(user.getNetworkChannel());

				ChatMessageAction action = new ChatMessageAction(message, chatChannel.getChannelId(), userId
								.longValue(), newUser.getName());

				action.setTransceiver(clientTransceiver);

				return action;
			}
		});
	}

	@Override
	public void onShutdown()
	{
		// 		for (Iterator i = chatChannels.values ().iterator (); i.hasNext ();)
		// 		{
		// 			ChatChannel channel = (ChatChannel) i.next ();
		// 			final int channelName = getChatChannel (channel.getName ()).getChannelId ();
		// 			ChatterProcessor chatterProcessor = new ChatterProcessor(new Long(-1), channel);
		// 			chatterProcessor.doProcessor (
		// 				new Processor()
		// 				{
		// 					public AbstractAction getAction (User user, User newUser)
		// 					{
		// 						ClientTransceiver clientTransceiver =
		// 							new ClientTransceiver(user.getNetworkChannel ());
		// 						clientTransceiver.addReceiver (user.getNetworkChannel ());
		// 						ChatCloseAction action =
		// 							new ChatCloseAction(user.getUniqueId (), channelName);
		// 						action.setTransceiver (clientTransceiver);
		// 						return action;
		// 					}
		// 				});
		// 		}
		super.onShutdown();
	}

	@Override
	public void onUserLogoff(User user)
	{
		for (Iterator i = chatChannels.values().iterator(); i.hasNext();)
		{
			final ChatChannel channel = (ChatChannel) i.next();

			if (channel.existsChatterInChannel(new Long(user.getUniqueId())))
			{
				notifyMembersUserLeave(channel, new Long(user.getUniqueId()));
			}
		}

		super.onUserLogoff(user);
	}
}
