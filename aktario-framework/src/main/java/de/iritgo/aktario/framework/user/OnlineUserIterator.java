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

package de.iritgo.aktario.framework.user;


import java.util.Iterator;


/**
 * Return all online users.
 */
public class OnlineUserIterator implements Iterator
{
	private Iterator iterator;

	private User user;

	private boolean hasNext;

	public OnlineUserIterator(Iterator iterator)
	{
		this.iterator = iterator;
		user = getNextOnlineUser();
	}

	public boolean hasNext()
	{
		if (user != null)
		{
			return true;
		}

		return false;
	}

	public Object next()
	{
		User currentUser = user;

		user = getNextOnlineUser();

		return currentUser;
	}

	public void remove()
	{
	}

	private User getNextOnlineUser()
	{
		while (iterator.hasNext())
		{
			User user = (User) iterator.next();

			if (user.isOnline())
			{
				return user;
			}
		}

		return null;
	}
}
