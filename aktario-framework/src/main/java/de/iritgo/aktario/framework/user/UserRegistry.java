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


import de.iritgo.aktario.core.logger.Log;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;


/**
 * The user registry conatins all known users.
 */
public class UserRegistry
{
	/** All users currently known to the system. */
	private Map<Long, User> users;

	/**
	 * Create a new empty user registry.
	 */
	public UserRegistry()
	{
		users = new TreeMap<Long, User>();
	}

	/**
	 * Add the a user to the registry under a specific id.
	 *
	 * @param id The id under which the user should be stored.
	 * @param user The user to add.
	 */
	synchronized public void addUser(Long id, User user)
	{
		if (users.get(id) == null)
		{
			users.put(id, user);

			Log.logDebug("system", "UserRegistry", "Added a new user: " + user.getName() + ":" + id);
		}
	}

	/**
	 * Add the a user to the registry.
	 *
	 * @param user The user to add.
	 */
	synchronized public void addUser(User user)
	{
		if (users.get(new Long(user.getUniqueId())) == null)
		{
			users.put(new Long(user.getUniqueId()), user);

			Log.logDebug("system", "UserRegistry", "Added a new user " + user.getName() + ":" + user.getUniqueId());
		}
	}

	/**
	 * Remove a user from the registry.
	 *
	 * @param user The user to remove.
	 */
	synchronized public void removeUser(User user)
	{
		Log.logDebug("system", ".UserRegistry", "Removed user " + user.getName() + ":" + user.getUniqueId());

		users.remove(new Long(user.getUniqueId()));
	}

	/**
	 * Retrieve a user with a specific id.
	 *
	 * @param id The id of the user to retrieve.
	 * @return The user or null if none was found.
	 */
	synchronized public User getUser(long id)
	{
		return (User) users.get(new Long(id));
	}

	/**
	 * Retrieve a user with a specific id.
	 *
	 * @param id The id of the user to retrieve.
	 * @return The user or null if none was found.
	 */
	synchronized public User getUser(Long id)
	{
		return (User) users.get(id);
	}

	/**
	 * Retrieve a user with a specific name.
	 *
	 * @param name The name of the user to retrieve.
	 * @return The user or null if none was found.
	 */
	synchronized public User getUser(String name)
	{
		for (User user : users.values())
		{
			if (user.getName().equals(name))
			{
				return user;
			}
		}

		return null;
	}

	/**
	 * Retrieve a user with a specific email.
	 *
	 * @param email The email of the user to retrieve.
	 * @return The user or null if none was found.
	 */
	synchronized public User getUserByEMail(String email)
	{
		for (User user : users.values())
		{
			if (user.getEmail().equals(email))
			{
				return user;
			}
		}

		return null;
	}

	/**
	 * Retrieve a user by its assigned network channel.
	 *
	 * @param networkchannel The network channel of the user to retrieve.
	 * @return The user or null if none was found.
	 */
	synchronized public User getUserByNetworkChannel(double networkchannel)
	{
		for (User user : users.values())
		{
			if (user.getNetworkChannel() == networkchannel)
			{
				return user;
			}
		}

		return null;
	}

	/**
	 * Get an iterator over all users.
	 *
	 * @return An user iterator.
	 */
	synchronized public Iterator<User> userIterator()
	{
		LinkedList<User> tmpList = new LinkedList<User>(users.values());

		return tmpList.iterator();
	}

	/**
	 * Get an iterator over all online users.
	 *
	 * @return An online user iterator.
	 */
	synchronized public Iterator<User> onlineUserIterator()
	{
		LinkedList<User> tmpList = new LinkedList<User>(users.values());

		return new OnlineUserIterator(tmpList.iterator());
	}

	/**
	 * Check wether this registry contains any users or not.
	 *
	 * @return True if the registry contains users.
	 */
	synchronized public boolean isEmpty()
	{
		return users.isEmpty();
	}

	/**
	 * Return the number of user
	 */
	synchronized public int getUserSize()
	{
		return users.size();
	}

	/**
	 * Remove all user from the registry.
	 */
	synchronized public void clear()
	{
		users.clear();
	}
}
