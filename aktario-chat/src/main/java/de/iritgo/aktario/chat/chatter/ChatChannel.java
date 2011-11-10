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


import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.UserRegistry;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class ChatChannel extends BaseObject
{
	private String name;

	private List chatter;

	private UserRegistry userRegistry;

	public ChatChannel(String name, UserRegistry userRegistry)
	{
		this.name = name;
		chatter = new LinkedList();
		this.userRegistry = userRegistry;
	}

	public void addChatter(Long chatterId)
	{
		User user = userRegistry.getUser(chatterId);

		if (user != null)
		{
			chatter.add(user);
		}
	}

	public boolean existsChatterInChannel(Long chatterId)
	{
		User user = userRegistry.getUser(chatterId);

		if (user != null)
		{
			return chatter.contains(user);
		}

		return false;
	}

	public void removeChatter(Long chatterId)
	{
		chatter.remove(userRegistry.getUser(chatterId));
	}

	public void removeChatter(User user)
	{
		chatter.remove(user);
	}

	public String getName()
	{
		return name;
	}

	public int getChannelId()
	{
		return name.hashCode();
	}

	public int getNumChatters()
	{
		return chatter.size();
	}

	public Iterator getMembersIterator()
	{
		return chatter.iterator();
	}
}
