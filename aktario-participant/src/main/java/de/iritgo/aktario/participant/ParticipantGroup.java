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

package de.iritgo.aktario.participant;


import de.iritgo.aktario.core.iobject.IObjectList;
import de.iritgo.aktario.framework.base.DataObject;
import de.iritgo.aktario.framework.base.FrameworkProxy;
import java.util.Iterator;


/**
 * ParticipantManager
 *
 * @version $Id: ParticipantGroup.java,v 1.3 2006/09/25 10:34:31 grappendorf Exp $
 */
public class ParticipantGroup extends DataObject
{
	public ParticipantGroup()
	{
		super("ParticipantGroup");

		addAttribute("iritgoUserName", "");

		addAttribute("participants", new IObjectList("participants", new FrameworkProxy(new DataObject("dummy")), this));
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
	 * Get the participant iterator
	 *
	 * @param transaction The transaction for this object.
	 * @return Return the participant iterator.
	 */
	public Iterator participantIteartor()
	{
		return getIObjectListAttribute("participants").iterator();
	}

	/**
	 * Get the IObjectListObject from the participants.
	 *
	 * @return Return the participant IObjectList
	 */
	public IObjectList getParticipantsIObjectList()
	{
		return getIObjectListAttribute("participants");
	}
}
