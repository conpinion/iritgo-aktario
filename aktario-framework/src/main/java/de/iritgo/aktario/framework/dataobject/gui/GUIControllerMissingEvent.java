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

package de.iritgo.aktario.framework.dataobject.gui;


import de.iritgo.aktario.core.event.Event;


/**
 * This event is fired if the system can not found a gui description for a data object.
 * Then other components can create a description.
 */
public class GUIControllerMissingEvent implements Event
{
	private String controllerTypeId;

	private GUIManager guiManager;

	private int displayType;

	private long userUniqueId;

	/**
	 * Standard constructor
	 */
	public GUIControllerMissingEvent (String controllerTypeId, GUIManager guiManager, int displayType, long userUniqueId)
	{
		this.controllerTypeId = controllerTypeId;
		this.guiManager = guiManager;
		this.displayType = displayType;
		this.userUniqueId = userUniqueId;
	}

	public String getControllerTypeId ()
	{
		return controllerTypeId;
	}

	public GUIManager getGUIManager ()
	{
		return guiManager;
	}

	public int getDisplayType ()
	{
		return displayType;
	}

	public long getUserUniqueId ()
	{
		return userUniqueId;
	}
}
