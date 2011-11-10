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

package de.iritgo.aktario.buddylist;


import de.iritgo.aktario.core.iobject.IObjectList;
import de.iritgo.aktario.framework.base.DataObject;
import de.iritgo.aktario.framework.base.FrameworkProxy;
import java.util.Iterator;


/**
 * BuddyListRegistry
 *
 * @version $Id: BuddyListRegistry.java,v 1.4 2006/09/25 10:34:32 grappendorf Exp $
 */
public class BuddyListRegistry extends DataObject
{
	/**
	 * Create a new BuddyListRegistry.
	 */
	public BuddyListRegistry()
	{
		super("BuddyListRegistry");

		addAttribute("buddyLists", new IObjectList("buddyList", new FrameworkProxy(new BuddyList()), this));
	}

	/**
	 * Add a buddy list to the registry.
	 *
	 * @param user The buddy list to add.
	 */
	public void addBuddyList(BuddyList buddyList)
	{
		getIObjectListAttribute("buddyLists").add(buddyList);
	}

	/**
	 * Retrieve the buddy list at a specified index.
	 *
	 * @param index The index of the buddy list to retrieve.
	 * @return The buddy list at a specified index.
	 */
	public BuddyList getBuddyList(int index)
	{
		return (BuddyList) getIObjectListAttribute("buddyLists").get(index);
	}

	/**
	 * Get the buddy list list.
	 *
	 * @return The buddy list list.
	 */
	public IObjectList getBuddyLists()
	{
		return getIObjectListAttribute("buddyLists");
	}

	/**
	 * Get an iterator over all buddy lists.
	 *
	 * @return A buddy list iterator.
	 */
	public Iterator buddyListIterator()
	{
		return getIObjectListAttribute("buddyLists").iterator();
	}

	/**
	 * Get the number of buddy lists currently stored in the registry.
	 *
	 * @return The buddy list count.
	 */
	public int getBuddyListCount()
	{
		return getIObjectListAttribute("buddyLists").size();
	}

	/**
	 * Remove a buddy list from the registry.
	 *
	 * @param user The buddy list to remove.
	 */
	public void removeBuddyList(BuddyList buddyList)
	{
		getIObjectListAttribute("buddyLists").remove(buddyList);
	}

	/**
	 * Find a buddy list by name.
	 *
	 * @param name The name of the buddy list to find.
	 * @return The buddy list or null if no user with the given name exists.
	 */
	public BuddyList getBuddyListByName(String name)
	{
		for (int i = 0; i < getBuddyListCount(); ++i)
		{
			BuddyList buddyList = (BuddyList) getBuddyList(i);

			if (buddyList.getIritgoUserName().equals(name))
			{
				return buddyList;
			}
		}

		return null;
	}

	/**
	 * Find a buddy list by id.
	 *
	 * @param id The id of the buddy list to find.
	 * @return The buddy list or null if no user with the given id exists.
	 */
	public BuddyList getBuddyListById(long id)
	{
		for (int i = 0; i < getBuddyListCount(); ++i)
		{
			BuddyList buddyList = (BuddyList) getBuddyList(i);

			if (buddyList.getUniqueId() == id)
			{
				return buddyList;
			}
		}

		return null;
	}
}
