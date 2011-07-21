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


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.manager.Manager;
import de.iritgo.aktario.framework.base.shutdown.ShutdownManager;
import de.iritgo.aktario.framework.base.shutdown.ShutdownObserver;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.UserRegistry;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ChatManager extends BaseObject implements Manager, ShutdownObserver
{
	protected Map chatChannels = new HashMap ();

	private UserRegistry userRegistry;

	public ChatManager (String id, UserRegistry userRegistry)
	{
		super (id);
		this.userRegistry = userRegistry;
	}

	public void joinChannel (String channelName, long chatter) throws UserAllreadyJoindException
	{
		Integer hashCode = new Integer (channelName.hashCode ());
		ChatChannel chatChannel = (ChatChannel) chatChannels.get (hashCode);

		if (chatChannel == null)
		{
			chatChannel = new ChatChannel (channelName, userRegistry);
			chatChannels.put (hashCode, chatChannel);
			Log.log ("system", "ChatManager.joinChannel", "Channel added: " + channelName, Log.INFO);
		}

		if (! chatChannel.existsChatterInChannel (new Long (chatter)))
		{
			chatChannel.addChatter (new Long (chatter));
		}
		else
		{
			throw new UserAllreadyJoindException ();
		}
	}

	public void leaveChannel (String channelName, long chatter)
	{
		Integer channelId = new Integer (channelName.hashCode ());

		leaveChannel (channelId, chatter);
	}

	public void leaveChannel (Integer channelId, long chatter)
	{
		ChatChannel chatChannel = (ChatChannel) chatChannels.get (channelId);

		if (chatChannel != null)
		{
			chatChannel.removeChatter (new Long (chatter));

			if (chatChannel.getNumChatters () <= 0)
			{
				chatChannels.remove (channelId);
			}
		}
	}

	public void messageChannel (@SuppressWarnings("unused") String message, @SuppressWarnings("unused") int channelId,
					@SuppressWarnings("unused") long chatter)
	{
	}

	protected ChatChannel getChatChannel (int channelId)
	{
		return ((ChatChannel) chatChannels.get (new Integer (channelId)));
	}

	protected ChatChannel getChatChannel (String channelName)
	{
		return getChatChannel (channelName.hashCode ());
	}

	protected Iterator getChatChannelIterator ()
	{
		return chatChannels.values ().iterator ();
	}

	public void onShutdown ()
	{
		chatChannels.clear ();
	}

	public void onUserLogoff (User user)
	{
		for (Iterator i = chatChannels.values ().iterator (); i.hasNext ();)
		{
			((ChatChannel) i.next ()).removeChatter (user);
		}
	}

	public void init ()
	{
		((ShutdownManager) Engine.instance ().getManagerRegistry ().getManager ("shutdown")).addObserver (this);
	}

	public void unload ()
	{
		((ShutdownManager) Engine.instance ().getManagerRegistry ().getManager ("shutdown")).removeObserver (this);
	}
}
