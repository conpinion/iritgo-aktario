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

package de.iritgo.aktario.core.room;


import de.iritgo.aktario.core.application.ApplicationInstance;
import de.iritgo.aktario.core.iobject.IObjectList;
import de.iritgo.aktario.framework.base.DataObject;
import java.util.Iterator;


/**
 * Data object that represents a user of the Aktario application.
 *
 * @version $Id: Room.java,v 1.8 2006/09/25 10:34:32 grappendorf Exp $
 */
public class Room extends DataObject
{
	/**
	 * Create a new Room.
	 */
	public Room()
	{
		super("Room");

		addAttribute("name", "");
		addAttribute("applications", ApplicationInstance.class);
		addAttribute("participants", Participant.class);
	}

	/**
	 * Get the room name.
	 *
	 * @return The room name.
	 */
	public String getName()
	{
		return getStringAttribute("name");
	}

	/**
	 * Set the room name.
	 *
	 * @param name The new name.
	 */
	public void setName(String name)
	{
		setAttribute("name", name);
	}

	/**
	 * Add an application.
	 *
	 * @param application The application to add.
	 */
	public void addApplication(ApplicationInstance application)
	{
		getListAttribute("applications").add(application);
	}

	/**
	 * Retrieve the application at a specified index.
	 *
	 * @param index The index of the application to retrieve.
	 * @return The application at the specified index.
	 */
	public ApplicationInstance getApplication(int index)
	{
		return (ApplicationInstance) getListAttribute("applications").get(index);
	}

	/**
	 * Remove a application.
	 *
	 * @param application The application to remove.
	 */
	public void removeApplication(ApplicationInstance application)
	{
		getListAttribute("applications").remove(application);
	}

	/**
	 * Get the number of applications.
	 *
	 * @return The application count.
	 */
	public int getApplicationCount()
	{
		return getListAttribute("applications").size();
	}

	/**
	 * Get the application list.
	 *
	 * @return The application list.
	 */
	public IObjectList getApplications()
	{
		return getListAttribute("applications");
	}

	/**
	 * Get an iterator over all applications.
	 *
	 * @return An application iterator.
	 */
	public Iterator applicationIterator()
	{
		return getListAttribute("applications").iterator();
	}

	/**
	 * Add a participant.
	 *
	 * @param participant The participant to add.
	 */
	public void addParticipant(Participant participant)
	{
		getListAttribute("participants").add(participant);
	}

	/**
	 * Retrieve the participant at a specified index.
	 *
	 * @param index The index of the participant to retrieve.
	 * @return The participant at the specified index.
	 */
	public Participant getParticipant(int index)
	{
		return (Participant) getListAttribute("participants").get(index);
	}

	/**
	 * Remove a participant.
	 *
	 * @param participant The participant to remove.
	 */
	public void removeParticipant(Participant participant)
	{
		getListAttribute("participants").remove(participant);
	}

	/**
	 * Get the number of participants.
	 *
	 * @return The participant count.
	 */
	public int getParticipantCount()
	{
		return getListAttribute("participants").size();
	}

	/**
	 * Get the participant list.
	 *
	 * @return The participant list.
	 */
	public IObjectList getParticipants()
	{
		return getListAttribute("participants");
	}

	/**
	 * Get an iterator over all participants.
	 *
	 * @return An participant iterator.
	 */
	public Iterator participantIterator()
	{
		return getListAttribute("participants").iterator();
	}
}
