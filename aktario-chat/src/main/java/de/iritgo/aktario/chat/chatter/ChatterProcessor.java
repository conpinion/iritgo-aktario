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


import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.server.Server;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.UserRegistry;
import java.util.Iterator;


public class ChatterProcessor
{
	private Long userId;

	private ChatChannel chatChannel;

	public ChatterProcessor(Long userId, ChatChannel chatChannel)
	{
		this.userId = userId;
		this.chatChannel = chatChannel;
	}

	public void doProcessor(Processor processor)
	{
		UserRegistry userRegistry = Server.instance().getUserRegistry();
		User newUser = userRegistry.getUser(userId);

		if (chatChannel == null)
		{
			return;
		}

		Iterator i = chatChannel.getMembersIterator();

		while (i.hasNext())
		{
			User user = (User) i.next();

			if (! user.isOnline())
			{
				continue;
			}

			de.iritgo.aktario.core.action.AbstractAction action = processor.getAction(user, newUser);

			ActionTools.sendToClient(action);
		}
	}
}
