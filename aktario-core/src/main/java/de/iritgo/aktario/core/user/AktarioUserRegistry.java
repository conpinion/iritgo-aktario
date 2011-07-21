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


import de.iritgo.aktario.core.iobject.IObjectList;
import de.iritgo.aktario.framework.base.DataObject;
import de.iritgo.aktario.framework.base.FrameworkProxy;
import java.util.Iterator;


/**
 * AktarioUserRegistry
 *
 * @version $Id: AktarioUserRegistry.java,v 1.12 2006/09/25 10:34:30 grappendorf Exp $
 */
public class AktarioUserRegistry extends DataObject
{
	/**
	 * Create a new AktarioUserRegistry.
	 */
	public AktarioUserRegistry ()
	{
		super ("AktarioUserRegistry");

		addAttribute ("users", new IObjectList ("users", new FrameworkProxy (new AktarioUser ()), this));

		addAttribute ("profiles", new IObjectList ("profiles", new FrameworkProxy (new AktarioUserProfile ()), this));
	}

	/**
	 * Add a user to the registry.
	 *
	 * @param user The user to add.
	 */
	public void addUser (AktarioUser user)
	{
		getIObjectListAttribute ("users").add (user);
	}

	/**
	 * Retrieve the user at a specified index.
	 *
	 * @param index The index of the user to retrieve.
	 * @return The user at a specified index.
	 */
	public AktarioUser getUser (int index)
	{
		return (AktarioUser) getIObjectListAttribute ("users").get (index);
	}

	/**
	 * Get the user list.
	 *
	 * @return The user list.
	 */
	public IObjectList getUsers ()
	{
		return getIObjectListAttribute ("users");
	}

	/**
	 * Get an iterator over all users.
	 *
	 * @return A user iterator.
	 */
	public Iterator userIterator ()
	{
		return getIObjectListAttribute ("users").iterator ();
	}

	/**
	 * Get the number of users currently stored in the registry.
	 *
	 * @return The user count.
	 */
	public int getUserCount ()
	{
		return getIObjectListAttribute ("users").size ();
	}

	/**
	 * Remove a user from the registry.
	 *
	 * @param user The user to remove.
	 */
	public void removeUser (AktarioUser user)
	{
		getIObjectListAttribute ("users").remove (user);
	}

	/**
	 * Add a user profile to the registry.
	 *
	 * @param profile The profile to add.
	 */
	public void addProfile (AktarioUserProfile profile)
	{
		getIObjectListAttribute ("profiles").add (profile);
	}

	/**
	 * Retrieve the user profile at a specified index.
	 *
	 * @param index The index of the user profile to retrieve.
	 * @return The user profile at a specified index.
	 */
	public AktarioUserProfile getProfile (int index)
	{
		AktarioUserProfile a = (AktarioUserProfile) getIObjectListAttribute ("profiles").get (index);

		return a;
	}

	/**
	 * Get an iterator over all user profiles.
	 *
	 * @return A user profile iterator.
	 */
	public Iterator getProfileIterator ()
	{
		return getIObjectListAttribute ("profiles").iterator ();
	}

	/**
	 * Get the number of user profiles currently stored in the registry.
	 *
	 * @return The user profile count.
	 */
	public int getProfileCount ()
	{
		return getIObjectListAttribute ("profiles").size ();
	}

	/**
	 * Find a user by name.
	 *
	 * @param name The name of the user to find.
	 * @return The user or null if no user with the given name exists.
	 */
	public AktarioUser getUserByName (String name)
	{
		for (int i = 0; i < getUserCount (); ++i)
		{
			AktarioUser user = (AktarioUser) getUser (i);

			if (user.getName ().equals (name))
			{
				return user;
			}
		}

		return null;
	}

	/**
	 * Find a user by id.
	 *
	 * @param id The id of the user to find.
	 * @return The user or null if no user with the given id exists.
	 */
	public AktarioUser getUserById (long id)
	{
		for (int i = 0; i < getUserCount (); ++i)
		{
			AktarioUser user = (AktarioUser) getUser (i);

			if (user.getUniqueId () == id)
			{
				return user;
			}
		}

		return null;
	}
}
