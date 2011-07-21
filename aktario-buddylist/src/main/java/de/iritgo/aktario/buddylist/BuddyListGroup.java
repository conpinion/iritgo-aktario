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
import de.iritgo.aktario.framework.dataobject.DynDataObject;
import java.util.Iterator;


/**
 * BuddyListGroup
 *
 * @version $Id: BuddyListGroup.java,v 1.5 2006/09/25 10:34:32 grappendorf Exp $
 */
public class BuddyListGroup extends DataObject
{
	public BuddyListGroup ()
	{
		super ("BuddyListGroup");

		addAttribute ("name", "");

		addAttribute ("iritgoUserName", "");

		addAttribute ("participants", new IObjectList ("participant", new FrameworkProxy (new DataObject ("dummy")),
						this));
	}

	/**
	 * Get the group name.
	 *
	 * @return The return the group name.
	 */
	public String getName ()
	{
		return getStringAttribute ("name");
	}

	/**
	 * Set the group name
	 *
	 * @param name The group name
	 */
	public void setName (String name)
	{
		setAttribute ("name", name);
	}

	/**
	 * Get the iritgo user name.
	 *
	 * @return The return the iritgo user name.
	 */
	public String getIritgoUserName ()
	{
		return getStringAttribute ("iritgoUserName");
	}

	/**
	 * Set the iritgo user name
	 *
	 * @param name The iritgo user name.
	 */
	public void setIritgoUserName (String iritgoUserName)
	{
		setAttribute ("iritgoUserName", iritgoUserName);
	}

	/**
	 * Add a participant to the buddy list group.
	 *
	 * @param user The participant to add.
	 */
	public void addParticipant (DynDataObject participant)
	{
		getIObjectListAttribute ("participants").add (participant);
	}

	/**
	 * Get the participant iterator
	 *
	 * @return Return the participant iterator.
	 */
	public Iterator participantIterator ()
	{
		return getIObjectListAttribute ("participants").iterator ();
	}

	/**
	 * Get the number of buddy list groups currently stored in the buddy list class.
	 *
	 * @return The buddy list group count.
	 */
	public int getParticipantCount ()
	{
		return getIObjectListAttribute ("participants").size ();
	}

	/**
	 * Get the IObjectListObject from the participants.
	 *
	 * @return Return the participant IObjectList
	 */
	public IObjectList getParticipantsIObjectList ()
	{
		return getIObjectListAttribute ("participants");
	}
}
