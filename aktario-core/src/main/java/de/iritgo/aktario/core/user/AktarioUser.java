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

package de.iritgo.aktario.core.user;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.iobject.IObjectList;
import de.iritgo.aktario.core.room.Room;
import de.iritgo.aktario.framework.base.DataObject;
import de.iritgo.aktario.framework.server.Server;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.UserRegistry;
import java.util.Iterator;


/**
 * Data object that represents a user of the Aktario application.
 *
 * @version $Id: AktarioUser.java,v 1.11 2006/09/25 10:34:30 grappendorf Exp $
 */
public class AktarioUser extends DataObject
{
	/** Administrator role. */
	public static final int ROLE_ADMIN = 0;

	/** Role of a teacher. */
	public static final int ROLE_USER = 1;

	/**
	 * Create a new AktarioUser.
	 */
	public AktarioUser()
	{
		super("AktarioUser");

		addAttribute("name", "");
		addAttribute("fullName", "");
		addAttribute("password", "");
		addAttribute("email", "");

		// 		addAttribute ("phoneNumber", "");
		addAttribute("role", ROLE_USER);
		addAttribute("rooms", Room.class);
	}

	/**
	 * Create a new AktarioUser.
	 *
	 * @param uniqueId The unique id of the new user.
	 */
	public AktarioUser(long uniqueId)
	{
		this();
		setUniqueId(uniqueId);
	}

	/**
	 * Get the user's name.
	 *
	 * @return The user name.
	 */
	public String getName()
	{
		return getStringAttribute("name");
	}

	/**
	 * Set the user's name.
	 *
	 * @param name The new name.
	 */
	public void setName(String name)
	{
		setAttribute("name", name);
	}

	/**
	 * Get the user's phoneNumber.
	 *
	 * @return The user phoneNumber.
	 */
	public String getPhoneNumber()
	{
		return getStringAttribute("phoneNumber");
	}

	/**
	 * Set the user's phoneNumber.
	 *
	 * @param phoneNumber The new phoneNumber.
	 */
	public void setPhoneNumber(String phoneNumber)
	{
		setAttribute("phoneNumber", phoneNumber);
	}

	/**
	 * Get the user's full name.
	 *
	 * @return The user's full name.
	 */
	public String getFullName()
	{
		return getStringAttribute("fullName");
	}

	/**
	 * Set the user's full name.
	 *
	 * @param fullName The new full name.
	 */
	public void setFullName(String fullName)
	{
		setAttribute("fullName", fullName);
	}

	/**
	 * Get the user's password.
	 *
	 * @return The user's password.
	 */
	public String getPassword()
	{
		return getStringAttribute("password");
	}

	/**
	 * Set the user's password.
	 *
	 * @param password The new password.
	 */
	public void setPassword(String password)
	{
		setAttribute("password", password);
	}

	/**
	 * Get the user's email.
	 *
	 * @return The user's email.
	 */
	public String getEmail()
	{
		return getStringAttribute("email");
	}

	/**
	 * Set the user's email.
	 *
	 * @param email The new email.
	 */
	public void setEmail(String email)
	{
		setAttribute("email", email);
	}

	/**
	 * Get the user's role.
	 *
	 * @return The user's role.
	 */
	public int getRole()
	{
		return getIntAttribute("role");
	}

	/**
	 * Set the user's role.
	 *
	 * @param role The new role.
	 */
	public void setRole(int role)
	{
		setAttribute("role", role);
	}

	/**
	 * Get the user's role as a string.
	 *
	 * @return The user's role as a string.
	 */
	public String getRoleString()
	{
		switch (getRole())
		{
			case ROLE_ADMIN:
				return Engine.instance().getResourceService().getString("aktario.roleAdmin");

			case ROLE_USER:
				return Engine.instance().getResourceService().getString("aktario.roleUser");

			default:
				return "---";
		}
	}

	/**
	 * Get the Iritgo user id.
	 *
	 * @return The Iritgo user id.
	 */
	public long getUserId()
	{
		UserRegistry userRegistry = Server.instance().getUserRegistry();
		User user = userRegistry.getUser(getName());

		return user != null ? user.getUniqueId() : - 1;
	}

	/**
	 * Add a room.
	 *
	 * @param room The room to add.
	 */
	public void addRoom(Room room)
	{
		getListAttribute("rooms").add(room);
	}

	/**
	 * Retrieve the room at a specified index.
	 *
	 * @param index The index of the room to retrieve.
	 * @return The room at the specified index.
	 */
	public Room getRoom(int index)
	{
		return (Room) getListAttribute("rooms").get(index);
	}

	/**
	 * Remove a room.
	 *
	 * @param room The room to remove.
	 */
	public void removeRoom(Room room)
	{
		getListAttribute("rooms").remove(room);
	}

	/**
	 * Get the number of rooms.
	 *
	 * @return The room count.
	 */
	public int getRoomCount()
	{
		return getListAttribute("rooms").size();
	}

	/**
	 * Get the room list.
	 *
	 * @return The room list.
	 */
	public IObjectList getRooms()
	{
		return getListAttribute("rooms");
	}

	/**
	 * Get an iterator over all rooms.
	 *
	 * @return A room iterator.
	 */
	public Iterator roomIterator()
	{
		return getListAttribute("rooms").iterator();
	}
}
