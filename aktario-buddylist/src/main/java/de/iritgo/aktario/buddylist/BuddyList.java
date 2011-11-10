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
 * BuddyListGroup
 *
 * @version $Id: BuddyList.java,v 1.5 2006/09/25 10:34:32 grappendorf Exp $
 */
public class BuddyList extends DataObject
{
	public BuddyList()
	{
		super("BuddyList");

		addAttribute("iritgoUserName", "");

		addAttribute("buddyListGroups", new IObjectList("buddyListGroup", new FrameworkProxy(new DataObject("dummy")),
						this));
	}

	/**
	 * Get the iritgo user name.
	 *
	 * @return The return the iritgo user name.
	 */
	public String getIritgoUserName()
	{
		return getStringAttribute("iritgoUserName");
	}

	/**
	 * Set the iritgo user name
	 *
	 * @param name The iritgo user name.
	 */
	public void setIritgoUserName(String iritgoUserName)
	{
		setAttribute("iritgoUserName", iritgoUserName);
	}

	/**
	 * Add a buddy list group to the buddy list.
	 *
	 * @param user The buddy list group to add.
	 */
	public void addBuddyListGroup(BuddyListGroup buddyListGroup)
	{
		getIObjectListAttribute("buddyListGroups").add(buddyListGroup);
	}

	/**
	 * Get the number of buddy list groups currently stored in the buddy list class.
	 *
	 * @return The buddy list group count.
	 */
	public int getBuddyListGroupCount()
	{
		return getIObjectListAttribute("buddyListGroups").size();
	}

	/**
	 * Get the participant iterator
	 *
	 * @return Return the buddy list iterator.
	 */
	public Iterator buddyListGroupIterator()
	{
		return getIObjectListAttribute("buddyListGroups").iterator();
	}

	/**
	 * Get the IObjectListObject from the buddy list group.
	 *
	 * @return Return the participant IObjectList
	 */
	public IObjectList getBuddyListGroupsIObjectList()
	{
		return getIObjectListAttribute("buddyListGroup");
	}
}
