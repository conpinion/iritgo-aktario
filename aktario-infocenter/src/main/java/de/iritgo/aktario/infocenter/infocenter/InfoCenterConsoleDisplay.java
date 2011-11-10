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

package de.iritgo.aktario.infocenter.infocenter;


import de.iritgo.aktario.framework.user.User;


/**
 *
 */
public class InfoCenterConsoleDisplay implements InfoCenterDisplay
{
	/**
	 * Constructor
	 *
	 */
	public InfoCenterConsoleDisplay()
	{
	}

	/**
	 * Get the Id of the Logger
	 *
	 */
	public String getId()
	{
		return "system.out.infocenterdisplay";
	}

	/**
	 * Init Logger, called any time you add a logger to any category
	 *
	 * @param category
	 */
	public void init(String category, int context, User user)
	{
	}

	/**
	 * release
	 *
	 */
	public void release()
	{
	}

	/**
	 * Log
	 */
	public void info(User user, int context, String category, String icon, String message, String guiPaneId,
					long uniqueId, String iObjectTypeId, int level)
	{
		System.out.println("[" + level + "][" + category + "] ->" + message + "<-");
	}
}
